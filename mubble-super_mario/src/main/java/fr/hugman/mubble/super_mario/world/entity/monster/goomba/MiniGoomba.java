package fr.hugman.mubble.super_mario.world.entity.monster.goomba;

import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.entity.Stompable;
import fr.hugman.mubble.world.entity.ai.goal.target.SurprisedActiveTargetGoal;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

/**
 * The smallest of the goombas, which fights by holding on rather than by biting.
 * <p>
 * Catching a {@link MiniGoombaCarrier} takes it out of the world: it stops being an entity and becomes a
 * number the carrier holds, slowing them down and cutting their jump short for as long as it hangs on.
 * Shaking it off — spamming sneak — puts it back on the ground, unharmed, the way it works in
 * <i>Super Mario Bros. 3</i>. It never costs the carrier any health, in that game or in this one.
 *
 * @author Hugman
 * @since v4.0.0
 */
public class MiniGoomba extends AbstractGoomba implements Stompable {
    // Built the same way a goomba variant's are, so both families spell their texture paths alike.
    private static final Identifier TEXTURE = new ClientAsset.ResourceTexture(SuperMario.id("entity/goomba/mini/normal")).texturePath();
    private static final Identifier SURPRISED_TEXTURE = new ClientAsset.ResourceTexture(SuperMario.id("entity/goomba/mini/surprised")).texturePath();

    private static final String CLING_COOLDOWN_KEY = "cling_cooldown";

    /** How many mini goombas one carrier can hold at once. */
    public static final int MAX_CLINGING = 8;

    /**
     * How long a shaken-off mini goomba has to wait before it can hold on again, in ticks.
     * <p>
     * Without it the struggle would be pointless: the mini goomba is put back right where the carrier is
     * standing, close enough to latch on again the very next tick it gets to attack.
     */
    public static final int SHAKE_OFF_COOLDOWN = 40;

    /**
     * How hard a stomp throws whoever landed it back up, against {@value Stompable#DEFAULT_STOMP_BOUNCE}
     * for a full-grown goomba. There is far less to push off.
     */
    public static final double STOMP_BOUNCE = 0.3D;

    /** How fast a shaken-off mini goomba is thrown clear, sideways and upwards. */
    private static final double SHAKE_OFF_SPEED = 0.25D;
    private static final double SHAKE_OFF_LIFT = 0.3D;

    private int clingCooldown;

    public MiniGoomba(EntityType<? extends MiniGoomba> type, Level level) {
        super(type, level);
    }

    // BEHAVIOR

    public static AttributeSupplier.Builder createMiniGoombaAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 10.0)
                .add(Attributes.MAX_HEALTH, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 0.75)
                .add(Attributes.JUMP_STRENGTH, 0.65)
                .add(Attributes.GRAVITY, 0.12)
                .add(Attributes.SCALE, 0.5);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, this.level()));
        // Reaching the target is what makes it cling, see doHurtTarget.
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new SurprisedActiveTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.clingCooldown > 0) {
            this.clingCooldown--;
        }
    }

    /**
     * Holds onto whoever it reached instead of biting them.
     * <p>
     * Latching on is the whole attack, and it costs the carrier no health — being weighed down is the
     * price, not the damage. Anything that cannot be clung to is bitten the ordinary way.
     */
    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        if (!(target instanceof MiniGoombaCarrier carrier)) {
            return super.doHurtTarget(level, target);
        }
        if (this.clingCooldown > 0 || !carrier.addClingingMiniGoomba()) {
            return false;
        }
        //TODO: a mini goomba of its own, rather than the goomba grunt pitched up
        this.playSound(SuperMarioSounds.GOOMBA_FIND_TARGET, 0.6F, 1.5F);
        this.discard();
        return true;
    }

    /**
     * Puts a mini goomba that was clinging back into the world, thrown clear of whoever was carrying it.
     * <p>
     * It comes back alive and whole: shaking one off in <i>Super Mario Bros. 3</i> gets it away from you,
     * it does not kill it.
     *
     * @return the mini goomba that was put back, or null if it could not be created
     */
    @Nullable
    public static MiniGoomba shakeOff(ServerLevel level, LivingEntity carrier) {
        MiniGoomba mini = SuperMarioEntityTypes.MINI_GOOMBA.create(level, EntitySpawnReason.TRIGGERED);
        if (mini == null) {
            return null;
        }
        float angle = level.getRandom().nextFloat() * Mth.TWO_PI;
        double offsetX = Mth.cos(angle) * carrier.getBbWidth();
        double offsetZ = Mth.sin(angle) * carrier.getBbWidth();
        mini.snapTo(
                carrier.getX() + offsetX,
                carrier.getY() + carrier.getBbHeight() * 0.5D,
                carrier.getZ() + offsetZ,
                angle * Mth.RAD_TO_DEG,
                0.0F
        );
        mini.setDeltaMovement(offsetX * SHAKE_OFF_SPEED, SHAKE_OFF_LIFT, offsetZ * SHAKE_OFF_SPEED);
        mini.clingCooldown = SHAKE_OFF_COOLDOWN;
        level.addFreshEntity(mini);
        return mini;
    }

    public int getClingCooldown() {
        return this.clingCooldown;
    }

    public void setClingCooldown(int ticks) {
        this.clingCooldown = ticks;
    }

    // STOMPING

    @Override
    public double getStompBounce() {
        return STOMP_BOUNCE;
    }

    // NBT DATA

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt(CLING_COOLDOWN_KEY, this.clingCooldown);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.clingCooldown = input.getIntOr(CLING_COOLDOWN_KEY, 0);
    }

    // TEXTURE

    @Override
    public Identifier getTexture() {
        return this.isSurprised() ? SURPRISED_TEXTURE : TEXTURE;
    }
}
