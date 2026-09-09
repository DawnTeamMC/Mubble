package fr.hugman.mubble.super_mario.world.entity.projectile;

import fr.hugman.mubble.super_mario.references.SuperMarioDamageTypeIds;
import fr.hugman.mubble.super_mario.tags.SuperMarioEntityTypeTags;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * A huge flower grown by the Super Flower Pot power-up.
 * <p>
 * It is aimed at nothing: it goes straight up from where it was planted, at a speed of its own that neither
 * gravity nor drag ever touches, and defeats whatever it grows through on the way — outright, for the enemies
 * of the module.
 * <p>
 * It cannot go through blocks, and a ceiling does not stop it either: it is knocked down and forward, along
 * the way its holder was facing when they grew it, and then arcs back up. What bends that arc is gravity
 * upside down — the flower is pulled towards the sky rather than away from it, so a duck reads as a jump
 * played the wrong way round rather than as two changes of direction. A flower grown under a roof keeps
 * working its way out from under it rather than dying against the first slab, and only ever runs out of time
 * or of distance.
 * <p>
 * It is not something to stand on, to shoot down or to bounce off: it is only ever in the way of what it is
 * about to hit. Blocks it runs into are hit the way any projectile hits them, and the ones it passes through
 * are entered the way any entity enters them.
 *
 * @since v4.0.0
 */
public class Flower extends Projectile {
    /** Both the width and the height of a flower: these are 2×2 blocks, not small projectiles. */
    public static final float SIZE = 2.0F;

    /** How fast a flower travels, in blocks per tick. */
    public static final double DEFAULT_SPEED = 0.5D;
    /** How long a flower lasts at most, in ticks. */
    public static final int DEFAULT_LIFETIME = 30;
    /** How far a flower can travel before it wilts, in blocks. */
    public static final double DEFAULT_RANGE = 12.0D;
    /** The damage a flower deals, the same as the ball projectiles of the mod. */
    public static final float DAMAGE = 3.0F;

    /**
     * Gravity, upside down, as a share of the speed of the flower: what it gains towards the sky every tick,
     * and therefore how tightly a duck arcs back into a climb.
     */
    private static final double LIFT = 0.16D;
    /** The share of its speed a ceiling knocks a flower down by, the way a jump is a push off the ground. */
    private static final double ESCAPE_DROP = 0.6D;
    /** The share of its speed a ceiling sends a flower forward by, which is what carries it out from under. */
    private static final double ESCAPE_FORWARD = 1.0D;
    /** What the forward drift of a knock keeps every tick, so that it eases off rather than being switched off. */
    private static final double ESCAPE_DRAG = 0.85D;
    /** Below this, a drift is worth nothing and is dropped, so that a settled flower climbs exactly straight. */
    private static final double MIN_DRIFT = 1.0E-4D;
    /** Ticks the squish of hitting a ceiling lasts. */
    public static final int SQUISH_DURATION = 6;
    private static final byte EVENT_SQUISH = 100;

    /** Particles spawned per tick, strung along the ground the flower covers during it. */
    private static final int PARTICLES_PER_TICK = 3;
    /** How far the particles scatter around the middle of the flower, as a share of its width. */
    private static final double PARTICLE_SPREAD = 0.9D;
    private static final int WILT_PARTICLES = 12;

    private static final String AGE_KEY = "age";
    private static final String TRAVELLED_KEY = "travelled";
    private static final String SPEED_KEY = "speed";
    private static final String LIFETIME_KEY = "lifetime";
    private static final String RANGE_KEY = "range";
    private static final String FORWARD_YAW_KEY = "forward_yaw";

    private double speed = DEFAULT_SPEED;
    private int lifetime = DEFAULT_LIFETIME;
    private double range = DEFAULT_RANGE;
    /** The way its holder was facing when they grew it, which is the way a bounce sends it on. */
    private float forwardYaw;

    private int age;
    private double travelled;
    /**
     * Everything already hit, so that a flower only ever hits the same entity once.
     * <p>
     * Deliberately not saved: these are network ids, which nothing hands back to the same entity once the
     * world has been reloaded, and a flower lasting a second and a half is never around to see one anyway.
     */
    private final IntSet hitEntities = new IntOpenHashSet();

    private int squishTicks;
    private int squishTicksO;

    public Flower(EntityType<? extends Flower> type, Level level) {
        super(type, level);
        this.setDeltaMovement(0.0D, DEFAULT_SPEED, 0.0D);
    }

    public Flower(Level level, LivingEntity owner) {
        this(SuperMarioEntityTypes.FLOWER, level);
        this.setOwner(owner);
        this.setForwardYaw(owner.getYRot());
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
        // A flower that has not gone anywhere yet is still pointing straight up, and takes the new speed
        // right away — including in the packet that spawns it on the clients. One already on its way keeps
        // whatever heading it is on, and is brought back up to the new speed by the pull towards the sky.
        if (this.travelled <= 0.0D) {
            this.setDeltaMovement(0.0D, speed, 0.0D);
        }
    }

    public int getLifetime() {
        return this.lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    /**
     * @return how far the flower can travel, in blocks, counting the whole of its path and not just its climb
     */
    public double getRange() {
        return this.range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public float getForwardYaw() {
        return this.forwardYaw;
    }

    public void setForwardYaw(float forwardYaw) {
        this.forwardYaw = forwardYaw;
    }

    /**
     * @return the way ducking out of a ceiling sends the flower on, as a horizontal unit vector
     */
    public Vec3 getForward() {
        float yaw = this.forwardYaw * (float) (Math.PI / 180.0);
        return new Vec3(-Mth.sin(yaw), 0.0D, Mth.cos(yaw));
    }

    /**
     * @return how far the flower has already travelled, in blocks
     */
    public double getTravelled() {
        return this.travelled;
    }

    /**
     * @return how much a flower gains towards the sky every tick, in blocks per tick per tick
     */
    public double getLiftPerTick() {
        return this.speed * LIFT;
    }

    /**
     * @return whether the flower is still winning back the speed a ceiling knocked out of it, rather than
     * climbing at its own
     */
    public boolean isEscaping() {
        return this.getDeltaMovement().y() < this.speed - MIN_DRIFT;
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

        if (this.level().isClientSide()) {
            this.tickSquish();
            return;
        }
        if (this.isRemoved()) {
            return;
        }

        this.age++;
        this.hitEntitiesInTheWay();
        if (this.age >= this.lifetime || this.travelled >= this.range) {
            this.wilt();
        }
    }

    /**
     * Takes the flower along one tick's worth of its path.
     * <p>
     * Nothing is added to the movement and nothing is taken off it: two flowers grown from the same spot
     * follow the exact same path, whatever is going on around them.
     */
    private void grow() {
        Vec3 movement = this.getDeltaMovement();
        Vec3 before = this.position();
        this.move(MoverType.SELF, movement);
        // move() only records which blocks were crossed; this is what actually runs their "entity inside"
        // behaviour. Without it the flower ignores tripwires, pressure plates and every other trigger block.
        this.applyEffectsFromBlocks();
        this.travelled += this.position().subtract(before).length();
        this.needsSync = true;

        if (this.level().isClientSide()) {
            this.spawnGrowthParticles();
        } else if (this.horizontalCollision || this.verticalCollision) {
            this.hitBlocks(movement);
            // Only a ceiling knocks the flower away. Everything else it runs into, the pull towards the sky
            // sorts out on its own: the movement it just lost is won back over the ticks that follow.
            if (this.verticalCollision && movement.y() > 0.0D) {
                this.escape();
            }
        }
        this.accelerate();
    }

    /**
     * Pulls the flower towards the sky, and lets the drift of a duck ease off.
     * <p>
     * This is the whole of what bends a duck back into a climb. Gravity upside down turns the knock of a
     * ceiling around over several ticks rather than at once, and tops the flower out at its own speed on the
     * way back up, so the path it takes is the arc of a jump rather than two corners.
     */
    private void accelerate() {
        Vec3 movement = this.getDeltaMovement();
        double y = Math.min(this.speed, movement.y() + this.getLiftPerTick());
        double x = movement.x() * ESCAPE_DRAG;
        double z = movement.z() * ESCAPE_DRAG;
        this.setDeltaMovement(Math.abs(x) < MIN_DRIFT ? 0.0D : x, y, Math.abs(z) < MIN_DRIFT ? 0.0D : z);
    }

    /**
     * Knocks the flower down and forward off the ceiling it just hit, along the way its holder was facing.
     * <p>
     * This is not the end of its climb but the start of an attempt to get out from under whatever is in the
     * way: it is a push and nothing more, and {@link #accelerate} is what turns it back into a climb. A
     * ceiling the flower fails to clear simply knocks it down again, until it runs out of time or distance.
     */
    private void escape() {
        Vec3 forward = this.getForward().scale(this.speed * ESCAPE_FORWARD);
        this.setDeltaMovement(forward.x(), -this.speed * ESCAPE_DROP, forward.z());
        this.level().broadcastEntityEvent(this, EVENT_SQUISH);
        this.playSound(this.getEscapeSound(), 0.7F, 1.4F);
    }

    /**
     * Hits whatever the flower ran into the way any projectile would, so that target blocks and everything
     * else keyed on being shot at answer to it.
     * <p>
     * {@link Entity#move} zeroes out whichever component ran into a block, which is how the faces that were
     * actually hit are found.
     *
     * @param requested the movement the flower asked for, before collisions cut it short
     */
    private void hitBlocks(Vec3 requested) {
        Vec3 actual = this.getDeltaMovement();
        if (blocked(requested.x(), actual.x())) {
            this.hitBlock(Direction.get(requested.x() > 0.0D ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE, Direction.Axis.X));
        }
        if (blocked(requested.y(), actual.y())) {
            this.hitBlock(Direction.get(requested.y() > 0.0D ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE, Direction.Axis.Y));
        }
        if (blocked(requested.z(), actual.z())) {
            this.hitBlock(Direction.get(requested.z() > 0.0D ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE, Direction.Axis.Z));
        }
    }

    private static boolean blocked(double requested, double actual) {
        return Math.abs(requested) > 1.0E-7D && Math.abs(actual) < 1.0E-7D;
    }

    private void hitBlock(Direction direction) {
        Vec3 from = this.getBoundingBox().getCenter();
        Vec3 to = from.add(Vec3.atLowerCornerOf(direction.getUnitVec3i()).scale(SIZE / 2.0D + 0.25D));
        BlockHitResult hit = this.level().clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (hit.getType() != HitResult.Type.BLOCK) {
            return;
        }
        BlockState state = this.level().getBlockState(hit.getBlockPos());
        state.onProjectileHit(this.level(), state, hit, this);
    }

    /**
     * Defeats whatever the flower is growing through.
     * <p>
     * A flower is not spent by what it hits: it keeps going until it runs out of time or of distance, which
     * is what lets one flower clear a whole column of enemies. It only ever hits the same one once, though.
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
            // The enemies of the module go down in one, the way they do in the games they come from; anything
            // else takes what a ball would have dealt.
            float damage = entity.is(SuperMarioEntityTypeTags.ENEMIES) ? Float.MAX_VALUE : DAMAGE;
            entity.hurtServer(serverLevel, this.damageSources().source(SuperMarioDamageTypeIds.FLOWER, this, this.getOwner()), damage);
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
     * Strings the growth particles along the ground the flower covered during the tick, rather than dropping
     * them all where it ended up: a flower moving half a block a tick would otherwise leave a dotted line.
     */
    private void spawnGrowthParticles() {
        ParticleOptions particle = this.getGrowthParticle();
        Vec3 movement = this.getDeltaMovement();
        double spread = this.getBbWidth() * PARTICLE_SPREAD;
        for (int i = 0; i < PARTICLES_PER_TICK; i++) {
            Vec3 back = movement.scale((i + 0.5D) / PARTICLES_PER_TICK);
            this.level().addParticle(particle,
                    this.getRandomX(spread) - back.x(),
                    this.getY() + this.getBbHeight() / 2.0D - back.y(),
                    this.getRandomZ(spread) - back.z(),
                    0.0D, 0.0D, 0.0D);
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

    protected SoundEvent getEscapeSound() {
        return SoundEvents.AZALEA_LEAVES_HIT;
    }

    protected SoundEvent getWiltSound() {
        return SoundEvents.AZALEA_LEAVES_BREAK;
    }

    //endregion

    //region Animation

    private void tickSquish() {
        this.squishTicksO = this.squishTicks;
        if (this.squishTicks > 0) {
            this.squishTicks--;
        }
    }

    /**
     * @return how squashed the flower is by the ceiling it just hit, from 0 (upright) to 1 (fully squished)
     */
    public float getSquish(float partialTicks) {
        float ticks = Mth.lerp(partialTicks, this.squishTicksO, this.squishTicks);
        if (ticks <= 0.0F) {
            return 0.0F;
        }
        // Fully squished on impact, easing back out.
        return Mth.sin((ticks / SQUISH_DURATION) * (Mth.PI / 2.0F));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void handleEntityEvent(byte state) {
        if (state == EVENT_SQUISH) {
            this.squishTicks = SQUISH_DURATION;
            this.squishTicksO = SQUISH_DURATION;
        } else {
            super.handleEntityEvent(state);
        }
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

    /** Blocks are handled from the actual movement in {@link #grow}. */
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
        output.putDouble(TRAVELLED_KEY, this.travelled);
        output.putDouble(SPEED_KEY, this.speed);
        output.putInt(LIFETIME_KEY, this.lifetime);
        output.putDouble(RANGE_KEY, this.range);
        output.putFloat(FORWARD_YAW_KEY, this.forwardYaw);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.age = input.getIntOr(AGE_KEY, 0);
        this.travelled = input.getDoubleOr(TRAVELLED_KEY, 0.0D);
        this.speed = input.getDoubleOr(SPEED_KEY, DEFAULT_SPEED);
        this.lifetime = input.getIntOr(LIFETIME_KEY, DEFAULT_LIFETIME);
        this.range = input.getDoubleOr(RANGE_KEY, DEFAULT_RANGE);
        this.forwardYaw = input.getFloatOr(FORWARD_YAW_KEY, 0.0F);
    }

    //endregion
}
