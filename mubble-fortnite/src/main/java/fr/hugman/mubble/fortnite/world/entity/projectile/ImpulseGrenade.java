package fr.hugman.mubble.fortnite.world.entity.projectile;

import fr.hugman.mubble.fortnite.world.entity.FortniteEntityTypes;
import fr.hugman.mubble.fortnite.world.item.FortniteItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

/**
 * An impulse grenade, thrown from the item of the same name.
 * <p>
 * As in Fortnite, it is not a weapon but a way of moving people around. It does not bounce: it sticks to the first
 * block it lands on and, once stuck, goes off after a short delay in a burst that hurts no one and breaks nothing,
 * but sends everything around it flying away from the blast — its thrower included, which is the whole trick of
 * throwing one at your own feet. It flies straight through entities, so it always ends up stuck to the ground.
 * <p>
 * The blast is a vanilla explosion stripped of both its damage and its block breaking, the very same recipe as
 * the wind charge, only much wider and far stronger. That is what keeps the launch in line with the rest of the
 * game: it fades with distance, is blocked by walls, and is resisted by explosion knockback resistance.
 *
 * @since v4.0.0
 */
public class ImpulseGrenade extends ThrowableItemProjectile {
    private static final EntityDataAccessor<Boolean> DATA_STUCK = SynchedEntityData.defineId(ImpulseGrenade.class, EntityDataSerializers.BOOLEAN);

    /** Ticks between the grenade sticking to a block and the blast. */
    public static final int FUSE = 20;
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
    /**
     * How far past the face the grenade is put when it sticks, on top of half its size, in blocks. The tick leaves
     * its centre sitting exactly on the block it hit: lifting it by half its size is what makes it rest on the face
     * rather than halfway through it.
     */
    private static final double SURFACE_OFFSET = 1.0E-4D;

    private static final String FUSE_KEY = "fuse";
    private static final String ARMED_KEY = "armed";
    private static final String STUCK_TO_KEY = "stuck_to";

    private int fuse = FUSE;
    /** Whether the grenade has stuck to something since it was thrown, from which point its fuse burns. */
    private boolean armed;
    /** The block the grenade is stuck to, if any. */
    @Nullable
    private BlockPos stuckTo;

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
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_STUCK, false);
    }

    @Override
    protected Item getDefaultItem() {
        return FortniteItems.IMPULSE_GRENADE;
    }

    public boolean isStuck() {
        return this.entityData.get(DATA_STUCK);
    }

    public boolean isArmed() {
        return this.armed;
    }

    public int getFuse() {
        return this.fuse;
    }

    //region Physics

    @Override
    protected double getDefaultGravity() {
        return this.isStuck() ? 0.0D : GRAVITY;
    }

    @Override
    public void tick() {
        if (this.isStuck()) {
            // Held in place by the block it is stuck to, whatever pushes it: explosions, pistons, water.
            this.setDeltaMovement(Vec3.ZERO);
        }
        super.tick();

        if (this.level().isClientSide() || this.isRemoved()) {
            return;
        }
        // A grenade whose block is gone has nothing left to hold on to: it falls until it sticks to the next one.
        if (this.isStuck() && (this.stuckTo == null || this.level().getBlockState(this.stuckTo).getCollisionShape(this.level(), this.stuckTo).isEmpty())) {
            this.unstick();
        }
        if (this.armed && --this.fuse <= 0) {
            this.detonate();
        }
    }

    //endregion

    //region Interactions

    /**
     * The grenade flies straight through entities: it is meant to be stuck to the ground, not to whoever stands in
     * the way.
     */
    @Override
    protected boolean canHitEntity(Entity target) {
        return false;
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        // Lets the block react as it would to any thrown projectile: targets light up, chorus flowers break.
        super.onHitBlock(result);
        if (this.isRemoved() || this.isStuck()) {
            return;
        }

        // The tick has already moved the centre of the grenade onto the face it hit.
        Direction face = result.getDirection();
        double halfSize = (face.getAxis() == Direction.Axis.Y ? this.getBbHeight() : this.getBbWidth()) / 2.0D;
        this.setPos(this.position().add(face.getUnitVec3().scale(halfSize + SURFACE_OFFSET)));
        this.stick(result.getBlockPos());
    }

    private void stick(BlockPos pos) {
        this.setDeltaMovement(Vec3.ZERO);
        this.stuckTo = pos.immutable();
        this.entityData.set(DATA_STUCK, true);
        if (!this.armed) {
            this.armed = true;
            this.playSound(SoundEvents.WIND_CHARGE_THROW, 0.5F, 1.8F);
        }
    }

    private void unstick() {
        this.stuckTo = null;
        this.entityData.set(DATA_STUCK, false);
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
        view.putBoolean(ARMED_KEY, this.armed);
        view.storeNullable(STUCK_TO_KEY, BlockPos.CODEC, this.stuckTo);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput view) {
        super.readAdditionalSaveData(view);
        this.fuse = view.getIntOr(FUSE_KEY, FUSE);
        this.armed = view.getBooleanOr(ARMED_KEY, false);
        this.stuckTo = view.read(STUCK_TO_KEY, BlockPos.CODEC).orElse(null);
        this.entityData.set(DATA_STUCK, this.stuckTo != null);
    }

    //endregion
}
