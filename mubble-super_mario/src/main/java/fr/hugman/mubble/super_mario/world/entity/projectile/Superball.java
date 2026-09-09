package fr.hugman.mubble.super_mario.world.entity.projectile;

import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.references.SuperMarioDamageTypeIds;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.tags.SuperMarioItemTags;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import fr.hugman.mubble.world.entity.item.collectible.CollectibleEntity;
import fr.hugman.mubble.world.entity.projectile.Ball;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * A superball shot by the Superball Flower power-up.
 * <p>
 * It flies in a straight line and bounces off every surface it meets, reflecting on the normal of the face it
 * actually hit and keeping its speed all the way, until its four seconds are up or it strikes something. The
 * whole point of it is that the path is repeatable: two shots taken from the same spot, along the same aim,
 * follow the exact same trajectory, which is what makes the superball worth building a puzzle around.
 *
 * @author Hugman
 * @since v4.0.0
 */
public class Superball extends Ball {
    private static final ClientAsset.ResourceTexture TEXTURE = new ClientAsset.ResourceTexture(SuperMario.id("entity/superball"));

    /** Ticks a superball lives for: the four seconds Super Mario Land gives it. */
    public static final int LIFETIME = 80;
    /**
     * Damage a superball deals. Deliberately low next to the three points of a fire or ice ball: the superball
     * is thrown for where it goes rather than for what it hits, so a regular mob takes several of them.
     */
    private static final float DAMAGE = 1.0F;
    /**
     * How far past the face a bounce puts the ball, in blocks. The tick leaves it sitting exactly on the block
     * it hit, and starting the next sweep from there is what would let it catch the same face again, or slip
     * into the block around a corner.
     */
    private static final double SURFACE_OFFSET = 1.0E-4D;
    /** How far around itself the ball sweeps up coins, in blocks. */
    private static final double COIN_REACH = 0.25D;

    private static final String AGE_KEY = "age";
    private static final String SPEED_KEY = "speed";

    private int age;
    /** The speed the ball was thrown at, which it then keeps for its whole life. Zero until the throw. */
    private double speed;

    public Superball(EntityType<? extends Superball> type, Level level) {
        super(type, level);
    }

    public Superball(Level level, LivingEntity owner) {
        super(SuperMarioEntityTypes.SUPERBALL, level, owner);
    }

    public Superball(double x, double y, double z, Level level) {
        super(SuperMarioEntityTypes.SUPERBALL, x, y, z, level);
    }

    //region Physics

    @Override
    protected double getDefaultGravity() {
        return 0.0D;
    }

    @Override
    protected float getAirDrag() {
        return 1.0F;
    }

    /**
     * A superball is not counted out in rebounds: it bounces for as long as it lives.
     */
    @Override
    protected boolean hasReboundLimit() {
        return false;
    }

    @Override
    public boolean spins() {
        return false;
    }

    @Override
    public void tick() {
        if (this.speed <= 0.0D) {
            // A ball settles on its speed the moment it starts moving, so one placed by a command and pushed
            // afterwards still leaves with whatever it was given rather than being pinned at a standstill.
            this.speed = this.getDeltaMovement().length();
        } else {
            // Gravity and air drag are already off, but water is not: it scales the movement by a fixed amount
            // no projectile gets a say in. Putting the speed back every tick is what keeps a shot through a
            // pond on the same path as the same shot through open air.
            this.setDeltaMovement(this.getDeltaMovement().normalize().scale(this.speed));
        }

        super.tick();

        if (this.level().isClientSide() || this.isRemoved()) {
            return;
        }
        this.collectCoins();
        if (++this.age >= LIFETIME) {
            this.finalHit(SuperMarioSounds.SUPERBALL_DISAPPEAR.value());
        }
    }

    //endregion

    //region Interactions

    @Override
    protected boolean canHitEntity(Entity target) {
        // Invulnerable entities are passed straight through without so much as a bounce, as in Super Mario Land.
        if (target.isInvulnerable()) {
            return false;
        }
        // Vanilla stops shielding the owner as soon as the projectile has left them, which is fine for something
        // thrown away in a straight line. A ball that ricochets comes back at its thrower all the time.
        if (target == this.getOwner()) {
            return false;
        }
        return super.canHitEntity(target);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        Entity owner = this.getOwner();

        if (owner instanceof LivingEntity livingOwner) {
            livingOwner.setLastHurtMob(entity);
        }
        entity.hurt(this.damageSources().source(SuperMarioDamageTypeIds.SUPERBALL, this, owner), DAMAGE);
        this.finalHit(SuperMarioSounds.SUPERBALL_DISAPPEAR.value());
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        // Lets the block react as it would to any thrown projectile: bumpable blocks pop, targets light up.
        super.onHitBlock(result);
        if (this.isRemoved()) {
            return;
        }

        // The tick has already moved the ball onto the face it hit, and the raycast walks the blocks it crosses
        // one by one, so this is the face the ball actually met rather than a guess made from its heading. That
        // is what makes an edge or a corner come out right instead of letting the ball through the seam.
        Vec3 normal = result.getDirection().getUnitVec3();
        Vec3 movement = this.getDeltaMovement();
        double approach = movement.dot(normal);
        // A face the ball is already travelling away from was grazed along rather than run into; reflecting on
        // it would send the ball back the way it came for no reason.
        if (approach >= 0.0D) {
            return;
        }

        this.setDeltaMovement(movement.subtract(normal.scale(2.0D * approach)));
        this.setPos(this.position().add(normal.scale(SURFACE_OFFSET)));
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SuperMarioSounds.SUPERBALL_BOUNCE.value(), SoundSource.NEUTRAL, 0.3F, 1.0F);
    }

    /**
     * Sweeps up the coins the ball flies through and hands them to whoever threw it.
     * <p>
     * Coins are not something a projectile can hit — they are walked through, not bounced off — so they are
     * looked up around the ball rather than left to the raycast. Only coins are taken: bringing anything else
     * back is the Boomerang Flower's job.
     */
    private void collectCoins() {
        if (!(this.getOwner() instanceof Player player)) {
            return;
        }
        AABB reach = this.getBoundingBox().inflate(COIN_REACH);

        for (CollectibleEntity collectible : this.level().getEntities(MubbleEntityTypes.COLLECTIBLE, reach,
                entity -> !entity.isRemoved() && entity.getItem().is(SuperMarioItemTags.COINS))) {
            collectible.collect(player);
        }
        for (ItemEntity item : this.level().getEntitiesOfClass(ItemEntity.class, reach,
                entity -> !entity.isRemoved() && entity.getItem().is(SuperMarioItemTags.COINS))) {
            item.playerTouch(player);
        }
    }

    //endregion

    //region Cosmetics

    @Override
    protected SoundEvent getDeathSound() {
        return SuperMarioSounds.SUPERBALL_DISAPPEAR.value();
    }

    @Override
    protected ParticleOptions getDeathParticle() {
        return ParticleTypes.HAPPY_VILLAGER;
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.HAPPY_VILLAGER;
    }

    @Override
    public ClientAsset.ResourceTexture getTexture() {
        return TEXTURE;
    }

    //endregion

    //region Saving

    @Override
    protected void addAdditionalSaveData(ValueOutput view) {
        super.addAdditionalSaveData(view);
        view.putInt(AGE_KEY, this.age);
        view.putDouble(SPEED_KEY, this.speed);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput view) {
        super.readAdditionalSaveData(view);
        this.age = view.getIntOr(AGE_KEY, 0);
        this.speed = view.getDoubleOr(SPEED_KEY, 0.0D);
    }

    //endregion
}
