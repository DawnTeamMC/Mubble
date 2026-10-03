package fr.hugman.mubble.fortnite.world.entity.projectile;

import fr.hugman.mubble.fortnite.world.entity.FortniteEntityTypes;
import fr.hugman.mubble.fortnite.world.item.FortniteItems;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

/**
 * An impulse grenade, thrown from the item of the same name.
 * <p>
 * As in Fortnite, it is not a weapon but a way of moving people around: it bounces about for a short while,
 * then goes off in a burst that hurts no one and breaks nothing, but sends everything around it flying away
 * from the blast — its thrower included, which is the whole trick of throwing one at your own feet. Running
 * straight into an entity sets it off on the spot.
 * <p>
 * The blast is a vanilla explosion stripped of both its damage and its block breaking, the very same recipe as
 * the wind charge, only much wider and far stronger. That is what keeps the launch in line with the rest of the
 * game: it fades with distance, is blocked by walls, and is resisted by explosion knockback resistance.
 *
 * @since v4.0.0
 */
public class ImpulseGrenade extends ThrowableItemProjectile {
    /** Ticks between the throw and the blast: the second and a half Fortnite gives it. */
    public static final int FUSE = 30;
    /** Radius of the blast, in blocks. Anything up to twice as far is still pushed, if only barely. */
    public static final float RADIUS = 3.0F;
    /**
     * Strength of the push. The vanilla explosion scales it down by how far away and how hidden from the blast
     * an entity is, so this is the speed something sitting right on top of the grenade would leave at, in blocks
     * per tick. It is enough to throw a player some ten blocks up from a grenade at their feet.
     */
    public static final float KNOCKBACK = 2.0F;

    private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new SimpleExplosionDamageCalculator(
            false, false, Optional.of(KNOCKBACK), Optional.empty()
    );

    private static final double GRAVITY = 0.05D;
    /** Share of its speed across a face the grenade keeps when bouncing off it. */
    private static final double BOUNCINESS = 0.4D;
    /** Share of its speed along a face the grenade keeps when bouncing off it, or every tick it rolls on the ground. */
    private static final double FRICTION = 0.7D;
    /** Speed across a face under which the grenade no longer bounces off it, but settles against it. */
    private static final double MIN_BOUNCE_SPEED = 0.1D;
    /**
     * How far past the face a bounce puts the grenade, on top of half its size, in blocks. The tick leaves its
     * centre sitting exactly on the block it hit: lifting it by half its size is what makes it rest on the face
     * rather than halfway through it, and the extra bit keeps the next sweep from catching the same face again.
     */
    private static final double SURFACE_OFFSET = 1.0E-4D;

    private static final String FUSE_KEY = "fuse";

    private int fuse = FUSE;

    public ImpulseGrenade(EntityType<? extends ImpulseGrenade> type, Level level) {
        super(type, level);
    }

    public ImpulseGrenade(Level level, LivingEntity owner, ItemStack stack) {
        super(FortniteEntityTypes.IMPULSE_GRENADE, owner, level, stack);
    }

    public ImpulseGrenade(Level level, double x, double y, double z, ItemStack stack) {
        super(FortniteEntityTypes.IMPULSE_GRENADE, x, y, z, level, stack);
    }

    @Override
    protected Item getDefaultItem() {
        return FortniteItems.IMPULSE_GRENADE;
    }

    public int getFuse() {
        return this.fuse;
    }

    public void setFuse(int fuse) {
        this.fuse = fuse;
    }

    //region Physics

    @Override
    protected double getDefaultGravity() {
        return GRAVITY;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide() || this.isRemoved()) {
            return;
        }
        if (--this.fuse <= 0) {
            this.detonate();
        }
    }

    //endregion

    //region Interactions

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide()) {
            this.detonate();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        // Lets the block react as it would to any thrown projectile: targets light up, chorus flowers break.
        super.onHitBlock(result);
        if (this.isRemoved()) {
            return;
        }

        // The tick has already moved the grenade onto the face it hit, so this is the face it actually met rather
        // than a guess made from its heading.
        Direction face = result.getDirection();
        Vec3 normal = face.getUnitVec3();
        Vec3 movement = this.getDeltaMovement();
        double approach = movement.dot(normal);
        // A face the grenade is already travelling away from was grazed along rather than run into.
        if (approach >= 0.0D) {
            return;
        }

        Vec3 along = movement.subtract(normal.scale(approach)).scale(FRICTION);
        // Too slow to bounce: the grenade settles against the face instead, which on the ground means it rolls
        // to a stop rather than hopping on the spot for the rest of its fuse.
        Vec3 across = -approach < MIN_BOUNCE_SPEED ? Vec3.ZERO : normal.scale(-approach * BOUNCINESS);
        this.setDeltaMovement(along.add(across));
        double halfSize = (face.getAxis() == Direction.Axis.Y ? this.getBbHeight() : this.getBbWidth()) / 2.0D;
        this.setPos(this.position().add(normal.scale(halfSize + SURFACE_OFFSET)));

        if (!across.equals(Vec3.ZERO)) {
            this.playSound(SoundEvents.WIND_CHARGE_THROW, 0.25F, 1.8F);
        }
    }

    /**
     * Sets the grenade off where it stands, sending everything around it flying, and removes it.
     */
    public void detonate() {
        if (this.level().isClientSide() || this.isRemoved()) {
            return;
        }
        this.level().explode(
                this,
                null,
                EXPLOSION_DAMAGE_CALCULATOR,
                this.getX(),
                this.getY(),
                this.getZ(),
                RADIUS,
                false,
                Level.ExplosionInteraction.NONE,
                ParticleTypes.GUST_EMITTER_SMALL,
                ParticleTypes.GUST_EMITTER_LARGE,
                WeightedList.of(),
                SoundEvents.WIND_CHARGE_BURST
        );
        this.discard();
    }

    //endregion

    //region Saving

    @Override
    protected void addAdditionalSaveData(ValueOutput view) {
        super.addAdditionalSaveData(view);
        view.putInt(FUSE_KEY, this.fuse);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput view) {
        super.readAdditionalSaveData(view);
        this.fuse = view.getIntOr(FUSE_KEY, FUSE);
    }

    //endregion
}
