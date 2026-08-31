package fr.hugman.mubble.super_mario.world.entity.projectile;

import fr.hugman.mubble.super_mario.references.SuperMarioDamageTypeIds;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * A huge flower grown by the Super Flower Pot power-up.
 * <p>
 * It is aimed at nothing: it goes straight up from where it was planted, at a speed of its own that neither
 * gravity nor drag ever touches, and defeats whatever it grows through on the way. It is not something to
 * stand on, to shoot down or to bounce off — it is only ever in the way of what it is about to hit.
 * <p>
 * In Super Mario Bros. Wonder the flowers rise through ceilings, which they keep doing here: a flower that
 * stopped at the first block would be useless underground, where most of the game is played. What keeps that
 * from reaching halfway across the world is that a flower runs out of both time and height, whichever comes
 * first. A data pack that would rather have them pop against blocks can say so instead.
 *
 * @since v4.0.0
 */
public class Flower extends Projectile {
    /** Both the width and the height of a flower: these are 2×2 blocks, not small projectiles. */
    public static final float SIZE = 2.0F;

    /** How fast a flower rises, in blocks per tick. */
    public static final double DEFAULT_SPEED = 0.5D;
    /** How long a flower lasts at most, in ticks. */
    public static final int DEFAULT_LIFETIME = 30;
    /** How high a flower can climb before it wilts, in blocks. */
    public static final double DEFAULT_MAX_CLIMB = 12.0D;
    /** The damage a flower deals, the same as the ball projectiles of the mod. */
    public static final float DAMAGE = 3.0F;

    /** Particles spawned per tick, strung along the height the flower covers during it. */
    private static final int PARTICLES_PER_TICK = 3;
    /** How far the particles scatter around the middle of the flower, as a share of its width. */
    private static final double PARTICLE_SPREAD = 0.9D;
    private static final int WILT_PARTICLES = 12;

    private static final String AGE_KEY = "age";
    private static final String CLIMBED_KEY = "climbed";
    private static final String SPEED_KEY = "speed";
    private static final String LIFETIME_KEY = "lifetime";
    private static final String MAX_CLIMB_KEY = "max_climb";
    private static final String STOPPED_BY_BLOCKS_KEY = "stopped_by_blocks";

    private double speed = DEFAULT_SPEED;
    private int lifetime = DEFAULT_LIFETIME;
    private double maxClimb = DEFAULT_MAX_CLIMB;
    private boolean stoppedByBlocks;

    private int age;
    private double climbed;
    /**
     * Everything already hit, so that a flower only ever hits the same entity once.
     * <p>
     * Deliberately not saved: these are network ids, which nothing hands back to the same entity once the
     * world has been reloaded, and a flower lasting a second and a half is never around to see one anyway.
     */
    private final IntSet hitEntities = new IntOpenHashSet();

    public Flower(EntityType<? extends Flower> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public Flower(Level level, LivingEntity owner) {
        this(SuperMarioEntityTypes.FLOWER, level);
        this.setOwner(owner);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    //region Settings

    public double getSpeed() {
        return this.speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getLifetime() {
        return this.lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    public double getMaxClimb() {
        return this.maxClimb;
    }

    public void setMaxClimb(double maxClimb) {
        this.maxClimb = maxClimb;
    }

    /**
     * @return whether the flower pops against the first solid block it meets, rather than growing through it
     */
    public boolean isStoppedByBlocks() {
        return this.stoppedByBlocks;
    }

    public void setStoppedByBlocks(boolean stoppedByBlocks) {
        this.stoppedByBlocks = stoppedByBlocks;
        this.noPhysics = !stoppedByBlocks;
    }

    /**
     * @return how far the flower has already grown, in blocks
     */
    public double getClimbed() {
        return this.climbed;
    }

    //endregion

    //region Ticking

    @Override
    public void tick() {
        // Server-side only: the sound it plays is broadcast to every client, which would double up on
        // the ones ticking the flower themselves.
        if (this.firstTick && !this.level().isClientSide()) {
            this.playSound(this.getGrowthSound(), 1.0F, 0.6F);
        }

        super.tick();
        this.grow();

        // A flower stopped by a block wilts on its way up, and a wilted one has nothing left to hit.
        if (this.level().isClientSide() || this.isRemoved()) {
            return;
        }

        this.age++;
        this.hitEntitiesInTheWay();
        if (this.age >= this.lifetime || this.climbed >= this.maxClimb) {
            this.wilt();
        }
    }

    /**
     * Takes the flower up by one tick's worth of growth.
     * <p>
     * Nothing is added to the movement and nothing is taken off it: two flowers grown from the same spot
     * follow the exact same path, whatever is going on around them.
     */
    private void grow() {
        Vec3 movement = new Vec3(0.0D, this.speed, 0.0D);
        this.setDeltaMovement(movement);
        this.move(MoverType.SELF, movement);
        this.climbed += this.speed;
        this.needsSync = true;

        if (this.level().isClientSide()) {
            this.spawnGrowthParticles();
        } else if (this.stoppedByBlocks && this.verticalCollision) {
            this.wilt();
        }
    }

    /**
     * Defeats whatever the flower is growing through.
     * <p>
     * A flower is not spent by what it hits: it keeps going until it runs out of time or of height, which is
     * what lets one flower clear a whole column of enemies. It only ever hits the same one once, though.
     */
    private void hitEntitiesInTheWay() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        for (Entity entity : this.level().getEntities(this, this.getBoundingBox(), this::canHurt)) {
            if (!this.hitEntities.add(entity.getId())) {
                continue;
            }
            if (this.getOwner() instanceof LivingEntity owner) {
                owner.setLastHurtMob(entity);
            }
            entity.hurtServer(serverLevel, this.damageSources().source(SuperMarioDamageTypeIds.FLOWER, this, this.getOwner()), DAMAGE);
        }
    }

    /**
     * @return whether the flower is allowed to hurt the given entity
     */
    private boolean canHurt(Entity target) {
        if (!(target instanceof LivingEntity) || !target.isAlive() || target.isRemoved() || target.isSpectator()) {
            return false;
        }
        if (!target.canBeHitByProjectile()) {
            return false;
        }
        Entity owner = this.getOwner();
        if (owner == null) {
            return true;
        }
        if (target == owner || owner.isAlliedTo(target) || target.isAlliedTo(owner)) {
            return false;
        }
        // A pet is spared whatever the teams say: it belongs to the very player who grew the flower.
        return !(target instanceof OwnableEntity ownable) || ownable.getRootOwner() != owner;
    }

    private void wilt() {
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(this.getWiltParticle(),
                    this.getX(), this.getY() + SIZE / 2.0D, this.getZ(),
                    WILT_PARTICLES, SIZE / 4.0D, SIZE / 4.0D, SIZE / 4.0D, 0.0D);
        }
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), this.getWiltSound(), this.getSoundSource(), 0.7F, 1.0F);
        this.discard();
    }

    /**
     * Strings the growth particles along the height the flower covered during the tick, rather than dropping
     * them all where it ended up: a flower moving half a block a tick would otherwise leave a dotted line.
     */
    private void spawnGrowthParticles() {
        ParticleOptions particle = this.getGrowthParticle();
        double spread = this.getBbWidth() * PARTICLE_SPREAD;
        for (int i = 0; i < PARTICLES_PER_TICK; i++) {
            double y = this.getY() - this.speed * ((i + 0.5D) / PARTICLES_PER_TICK) + this.getBbHeight() / 2.0D;
            this.level().addParticle(particle, this.getRandomX(spread), y, this.getRandomZ(spread), 0.0D, 0.0D, 0.0D);
        }
    }

    protected ParticleOptions getGrowthParticle() {
        return ParticleTypes.HAPPY_VILLAGER;
    }

    protected ParticleOptions getWiltParticle() {
        return ParticleTypes.CHERRY_LEAVES;
    }

    protected SoundEvent getGrowthSound() {
        return SoundEvents.BONE_MEAL_USE;
    }

    protected SoundEvent getWiltSound() {
        return SoundEvents.AZALEA_LEAVES_BREAK;
    }

    //endregion

    //region Physics

    @Override
    protected double getDefaultGravity() {
        return 0.0D;
    }

    /** Hits are decided from the bounding box, in {@link #hitEntitiesInTheWay}. */
    @Override
    protected boolean canHitEntity(Entity target) {
        return false;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
    }

    /** A flower is grown through, not stood on. */
    @Override
    public boolean canBeCollidedWith(@Nullable Entity other) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean deflect(ProjectileDeflection deflection, @Nullable Entity entity, @Nullable EntityReference<Entity> owner, boolean fromAttack) {
        return false;
    }

    //endregion

    //region Saving

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt(AGE_KEY, this.age);
        output.putDouble(CLIMBED_KEY, this.climbed);
        output.putDouble(SPEED_KEY, this.speed);
        output.putInt(LIFETIME_KEY, this.lifetime);
        output.putDouble(MAX_CLIMB_KEY, this.maxClimb);
        output.putBoolean(STOPPED_BY_BLOCKS_KEY, this.stoppedByBlocks);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.age = input.getIntOr(AGE_KEY, 0);
        this.climbed = input.getDoubleOr(CLIMBED_KEY, 0.0D);
        this.speed = input.getDoubleOr(SPEED_KEY, DEFAULT_SPEED);
        this.lifetime = input.getIntOr(LIFETIME_KEY, DEFAULT_LIFETIME);
        this.maxClimb = input.getDoubleOr(MAX_CLIMB_KEY, DEFAULT_MAX_CLIMB);
        this.setStoppedByBlocks(input.getBooleanOr(STOPPED_BY_BLOCKS_KEY, false));
    }

    //endregion
}
