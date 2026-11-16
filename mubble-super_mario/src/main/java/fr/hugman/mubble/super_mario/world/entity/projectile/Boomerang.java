package fr.hugman.mubble.super_mario.world.entity.projectile;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.super_mario.references.SuperMarioDamageTypeIds;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.tags.SuperMarioEntityTypeTags;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.item.SuperMarioItems;
import fr.hugman.mubble.world.entity.PickupKeeper;
import fr.hugman.mubble.world.entity.item.collectible.CollectibleEntity;
import fr.hugman.mubble.world.entity.projectile.Ball;
import fr.hugman.mubble.world.power_up.PowerUp;
import fr.hugman.mubble.world.power_up.PowerUpHolder;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Ease;
import net.minecraft.util.EasingType;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class Boomerang extends Projectile implements PickupKeeper {
    public static final byte OUT = 0;
    public static final byte HANG = 1;
    public static final byte RETURN = 2;
    public static final byte CAUGHT = 3;

    /** Ticks the boomerang takes to vanish into the hand once caught. Cosmetic. */
    public static final int CATCH_TICKS = 3;
    /** Height between two carried entities. Cosmetic. */
    private static final double STACK_GAP = 0.18D;
    /** Ticks a newly carried entity takes to pop up into its place. Cosmetic. */
    private static final int POP_TICKS = 6;
    private static final float SPIN_BASE = 18.0F;
    private static final float SPIN_PER_SPEED = 40.0F;

    private static final byte EVENT_REBOUND_X = 100;
    private static final byte EVENT_REBOUND_Y = 101;
    private static final byte EVENT_REBOUND_Z = 102;
    private static final byte EVENT_GRAB = 103;

    private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(Boomerang.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<ParticleOptions> DATA_TRAIL = SynchedEntityData.defineId(Boomerang.class, EntityDataSerializers.PARTICLE);
    private static final EntityDataAccessor<Byte> DATA_PHASE = SynchedEntityData.defineId(Boomerang.class, EntityDataSerializers.BYTE);

    private Flight flight = Flight.DEFAULT;
    private Look look = Look.DEFAULT;
    private int age;
    private int legTicks;
    private int pass;
    private float range = Flight.DEFAULT.range();
    private float speed;
    @Nullable
    private Vec3 heading;
    private Vec3 passStart = Vec3.ZERO;
    private final IntSet hitThisPass = new IntOpenHashSet();
    private final Int2IntMap mountedAt = new Int2IntOpenHashMap();

    private float spin;
    private float spinO;
    private int reboundTick = Integer.MIN_VALUE;
    private Direction.Axis reboundAxis = Direction.Axis.Y;
    private int grabTick = Integer.MIN_VALUE;
    private int phaseTick;

    public Boomerang(EntityType<? extends Boomerang> type, Level level) {
        super(type, level);
        this.setLook(Look.DEFAULT);
    }

    public Boomerang(Level level, Flight flight, Look look) {
        this(SuperMarioEntityTypes.BOOMERANG, level);
        this.setFlight(flight);
        this.setLook(look);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_ITEM, ItemStack.EMPTY);
        builder.define(DATA_TRAIL, Look.DEFAULT.trailParticle());
        builder.define(DATA_PHASE, OUT);
    }

    //region State

    public Flight getFlight() {
        return this.flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
        this.range = flight.range();
    }

    public Look getLook() {
        return this.look;
    }

    public void setLook(Look look) {
        this.look = look;
        this.entityData.set(DATA_ITEM, new ItemStack(look.item()));
        this.entityData.set(DATA_TRAIL, look.trailParticle());
    }

    public ItemStack getItem() {
        return this.entityData.get(DATA_ITEM);
    }

    public byte getPhase() {
        return this.entityData.get(DATA_PHASE);
    }

    private void setPhase(byte phase) {
        this.entityData.set(DATA_PHASE, phase);
        this.legTicks = 0;
    }

    /**
     * @return how far the current pass goes, which shrinks every time the boomerang misses
     */
    public float getRange() {
        return this.range;
    }

    public int getPass() {
        return this.pass;
    }

    //endregion

    //region Flight

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.tickClient();
            return;
        }
        if (this.getPhase() == CAUGHT) {
            this.tickCaught();
            return;
        }
        if (this.heading == null) {
            Vec3 motion = this.getDeltaMovement();
            this.heading = motion.lengthSqr() > 1.0E-7D ? motion.normalize() : this.calculateViewVector(this.getXRot(), this.getYRot());
            this.passStart = this.position();
        }
        if (++this.age > this.flight.lifetime()) {
            this.vanish();
            return;
        }

        AABB before = this.getBoundingBox();
        Vec3 motion = this.steer();
        if (this.isRemoved()) {
            return;
        }
        this.moveAndRebound(motion);
        if (this.isRemoved()) {
            return;
        }
        this.touchEntities(before.minmax(this.getBoundingBox()));
        if (this.isRemoved()) {
            return;
        }
        Entity owner = this.liveOwner();
        if (owner != null && (this.getPhase() != OUT || this.pass > 0)
                && owner.getBoundingBox().inflate(this.flight.catchReach()).intersects(this.getBoundingBox())) {
            this.catchBy(owner);
        }
    }

    private Vec3 steer() {
        switch (this.getPhase()) {
            case OUT -> {
                int duration = Math.max(1, this.flight.outTicks());
                this.legTicks++;
                EasingType easing = this.flight.outEasing();
                float travelled = easing.apply(Math.min(1.0F, (float) this.legTicks / duration)) - easing.apply(Math.min(1.0F, (float) (this.legTicks - 1) / duration));
                Vec3 motion = this.heading.scale(this.range * travelled);
                if (this.legTicks >= duration) {
                    this.speed = 0.0F;
                    if (this.flight.hangTicks() > 0) {
                        this.setPhase(HANG);
                    } else {
                        this.startReturn();
                    }
                }
                return motion;
            }
            case HANG -> {
                if (++this.legTicks >= this.flight.hangTicks()) {
                    this.startReturn();
                }
                return Vec3.ZERO;
            }
            default -> {
                Vec3 toTarget = this.returnTarget().subtract(this.getBoundingBox().getCenter());
                if (toTarget.lengthSqr() > 1.0E-6D) {
                    Vec3 direction = toTarget.normalize();
                    // the target is behind: the boomerang flew past it
                    if (this.legTicks > 0 && this.heading.dot(direction) < 0.0D) {
                        this.miss();
                        return this.isRemoved() ? Vec3.ZERO : this.steer();
                    }
                    this.heading = this.heading.lerp(direction, this.flight.homing()).normalize();
                }
                this.legTicks++;
                this.speed = Math.min(this.flight.returnSpeed(), this.speed + this.flight.returnAcceleration());
                return this.heading.scale(this.speed);
            }
        }
    }

    private void startReturn() {
        this.setPhase(RETURN);
        Vec3 toTarget = this.returnTarget().subtract(this.getBoundingBox().getCenter());
        if (toTarget.lengthSqr() > 1.0E-6D) {
            this.heading = toTarget.normalize();
        }
    }

    private void miss() {
        this.range *= 1.0F - this.flight.rangeLoss();
        if (this.range < this.flight.minRange()) {
            this.vanish();
            return;
        }
        this.pass++;
        this.passStart = this.position();
        this.hitThisPass.clear();
        this.setPhase(OUT);
    }

    private Vec3 returnTarget() {
        Entity owner = this.liveOwner();
        return owner != null ? owner.getBoundingBox().getCenter() : this.passStart.add(0.0D, this.getBbHeight() / 2.0D, 0.0D);
    }

    @Nullable
    private Entity liveOwner() {
        Entity owner = this.getOwner();
        return owner != null && owner.isAlive() && owner.level() == this.level() ? owner : null;
    }

    private void moveAndRebound(Vec3 motion) {
        this.setDeltaMovement(motion);
        this.needsSync = true;
        if (motion.lengthSqr() < 1.0E-10D) {
            return;
        }
        double halfHeight = this.getBbHeight() / 2.0D;
        Vec3 from = this.position().add(0.0D, halfHeight, 0.0D);
        BlockHitResult hit = this.level().clip(new ClipContext(from, from.add(motion), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (hit.getType() != HitResult.Type.BLOCK) {
            this.setPos(this.position().add(motion));
            return;
        }
        Vec3 normal = hit.getDirection().getUnitVec3();
        this.setPos(hit.getLocation().add(normal.scale(this.getBbWidth() / 2.0D + 1.0E-3D)).subtract(0.0D, halfHeight, 0.0D));
        this.hitTargetOrDeflectSelf(hit);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (this.isRemoved()) {
            return;
        }
        Vec3 normal = result.getDirection().getUnitVec3();
        this.heading = this.heading.subtract(normal.scale(2.0D * this.heading.dot(normal))).normalize();
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), this.look.reboundSound(), SoundSource.NEUTRAL, 0.5F, 1.0F);
        this.level().broadcastEntityEvent(this, switch (result.getDirection().getAxis()) {
            case X -> EVENT_REBOUND_X;
            case Y -> EVENT_REBOUND_Y;
            case Z -> EVENT_REBOUND_Z;
        });
        if (this.getPhase() == OUT) {
            this.speed = (float) this.getDeltaMovement().length();
            this.setPhase(RETURN);
        } else {
            this.miss();
        }
    }

    //endregion

    //region Interactions

    private void touchEntities(AABB swept) {
        Entity owner = this.getOwner();
        for (Entity entity : this.level().getEntities(this, swept.inflate(this.flight.pickupReach()), e -> e != owner && e.isAlive() && !e.isSpectator())) {
            if (this.canCarry(entity)) {
                this.carry(entity);
            } else if (!entity.getBoundingBox().intersects(swept)) {
                continue;
            } else if (entity instanceof Projectile projectile && this.canDestroy(projectile, owner)) {
                this.destroy(projectile);
            } else if (entity instanceof LivingEntity && this.canHit(entity, owner)) {
                this.onHit(new EntityHitResult(entity));
            }
            if (this.isRemoved()) {
                return;
            }
        }
    }

    private boolean canCarry(Entity entity) {
        return entity.is(SuperMarioEntityTypeTags.BOOMERANG_CAN_CARRY) && !entity.isPassenger() && !entity.isVehicle()
                && this.getPassengers().size() < this.flight.maxCarried();
    }

    private void carry(Entity entity) {
        if (entity.startRiding(this, true, true)) {
            this.level().broadcastEntityEvent(this, EVENT_GRAB);
            this.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), this.look.grabSound(), SoundSource.NEUTRAL, 0.4F, 1.4F);
        }
    }

    private boolean canDestroy(Projectile projectile, @Nullable Entity owner) {
        // one at rest, such as an arrow stuck in a block, is left alone
        boolean inFlight = projectile.position().distanceToSqr(projectile.xo, projectile.yo, projectile.zo) > 1.0E-8D;
        return inFlight && !projectile.is(SuperMarioEntityTypeTags.BOOMERANG_CANNOT_DESTROY) && !projectile.isPassenger() && (owner == null || projectile.getOwner() != owner);
    }

    private void destroy(Projectile projectile) {
        this.burst(this.look.vanishParticle(), projectile.getBoundingBox().getCenter(), 4);
        projectile.discard();
    }

    private boolean canHit(Entity entity, @Nullable Entity owner) {
        if (this.hitThisPass.contains(entity.getId()) || !this.canHitEntity(entity)) {
            return false;
        }
        return owner == null || !(entity instanceof OwnableEntity pet && pet.getOwner() == owner);
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return target != this.getOwner() && super.canHitEntity(target);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        Entity owner = this.getOwner();
        this.hitThisPass.add(entity.getId());
        if (owner instanceof LivingEntity livingOwner) {
            livingOwner.setLastHurtMob(entity);
        }
        if (this.level() instanceof ServerLevel level
                && entity.hurtServer(level, this.damageSources().source(SuperMarioDamageTypeIds.BOOMERANG, this, owner), this.flight.damage())
                && this.flight.knockback() > 0.0F) {
            Vec3 push = this.heading.multiply(1.0D, 0.0D, 1.0D).normalize().scale(this.flight.knockback());
            entity.push(push.x, 0.1D, push.z);
            entity.hurtMarked = true;
        }
    }

    private void catchBy(Entity owner) {
        Vec3 feet = owner.position();
        for (Entity passenger : List.copyOf(this.getPassengers())) {
            this.release(passenger, feet, false);
            if (owner instanceof Player player) {
                if (passenger instanceof CollectibleEntity collectible) {
                    collectible.collect(player);
                } else {
                    passenger.playerTouch(player);
                }
            }
        }
        this.level().playSound(null, owner.getX(), owner.getY(), owner.getZ(), this.look.catchSound(), SoundSource.NEUTRAL, 0.6F, 1.0F);
        this.burst(this.look.catchParticle(), this.getBoundingBox().getCenter(), 6);
        if (owner instanceof Player player) {
            player.swing(PowerUp.otherHand(player), true);
        }
        if (owner instanceof PowerUpHolder holder && holder.getPowerUpProperties() != null) {
            holder.getPowerUpProperties().removeEntity(this.getUUID());
        }
        this.setPhase(CAUGHT);
        this.setDeltaMovement(Vec3.ZERO);
    }

    private void tickCaught() {
        Entity owner = this.liveOwner();
        if (++this.legTicks >= CATCH_TICKS || owner == null) {
            this.discard();
            return;
        }
        Vec3 hand = owner.getBoundingBox().getCenter().subtract(0.0D, this.getBbHeight() / 2.0D, 0.0D);
        this.setPos(this.position().lerp(hand, 0.5D));
        this.needsSync = true;
    }

    /**
     * Drops everything carried where the boomerang is, then removes it. A thrower who missed the catch has to wait the
     * miss cooldown out before the next throw.
     */
    public void vanish() {
        for (Entity passenger : List.copyOf(this.getPassengers())) {
            this.release(passenger, this.position(), true);
        }
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), this.look.vanishSound(), SoundSource.NEUTRAL, 0.5F, 1.0F);
        this.burst(this.look.vanishParticle(), this.getBoundingBox().getCenter(), 8);
        if (this.getOwner() instanceof PowerUpHolder holder) {
            var properties = holder.getPowerUpProperties();
            if (properties != null && properties.removeEntity(this.getUUID())) {
                properties.startCooldown(this.flight.missCooldown());
            }
        }
        this.discard();
    }

    private void release(Entity passenger, Vec3 pos, boolean freshPickupDelay) {
        passenger.stopRiding();
        passenger.setDeltaMovement(Vec3.ZERO);
        passenger.teleportTo(pos.x, pos.y, pos.z);
        if (passenger instanceof CollectibleEntity collectible) {
            collectible.setFixed(false);
        }
        if (freshPickupDelay && passenger instanceof ItemEntity item) {
            item.setDefaultPickUpDelay();
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        if (!this.level().isClientSide() && reason.shouldDestroy()) {
            for (Entity passenger : List.copyOf(this.getPassengers())) {
                this.release(passenger, this.position(), true);
            }
        }
        super.remove(reason);
    }

    private void burst(ParticleOptions particle, Vec3 pos, int count) {
        if (this.level() instanceof ServerLevel level) {
            level.sendParticles(particle, pos.x, pos.y, pos.z, count, 0.15D, 0.15D, 0.15D, 0.05D);
        }
    }

    @Override
    public boolean keepsFromPickup(Entity passenger, @Nullable Entity taker) {
        return true;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return false;
    }

    //endregion

    //region Passengers

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().size() < this.flight.maxCarried();
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        this.mountedAt.put(passenger.getId(), this.tickCount);
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        this.mountedAt.remove(passenger.getId());
    }

    @Override
    protected void positionRider(Entity passenger, MoveFunction callback) {
        Vec3 pos = this.getPassengerRidingPosition(passenger);
        callback.accept(passenger, pos.x, pos.y, pos.z);
    }

    @Override
    public Vec3 getPassengerRidingPosition(Entity passenger) {
        int index = Math.max(0, this.getPassengers().indexOf(passenger));
        float pop = Ease.outBack(Mth.clamp((this.tickCount - this.mountedAt.getOrDefault(passenger.getId(), this.tickCount - POP_TICKS)) / (float) POP_TICKS, 0.0F, 1.0F));
        return new Vec3(this.getX(), this.getY() + this.getBbHeight() * pop + index * STACK_GAP * pop, this.getZ());
    }

    //endregion

    //region Client animations

    private void tickClient() {
        this.spinO = this.spin;
        this.spin += SPIN_BASE + (float) this.getDeltaMovement().length() * SPIN_PER_SPEED;
        if (this.getPhase() != CAUGHT) {
            Ball.spawnTrailParticles(this, this.entityData.get(DATA_TRAIL));
        }
    }

    public float getSpin(float partialTicks) {
        return Mth.lerp(partialTicks, this.spinO, this.spin);
    }

    /**
     * @return ticks since the last rebound on a block, or a negative value if there was none
     */
    public float ticksSinceRebound(float partialTicks) {
        return this.reboundTick == Integer.MIN_VALUE ? -1.0F : this.tickCount - this.reboundTick + partialTicks;
    }

    public Direction.Axis getReboundAxis() {
        return this.reboundAxis;
    }

    public float ticksSinceGrab(float partialTicks) {
        return this.grabTick == Integer.MIN_VALUE ? -1.0F : this.tickCount - this.grabTick + partialTicks;
    }

    public float ticksInPhase(float partialTicks) {
        return this.tickCount - this.phaseTick + partialTicks;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if (DATA_PHASE.equals(accessor)) {
            this.phaseTick = this.tickCount;
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void handleEntityEvent(byte event) {
        switch (event) {
            case EVENT_REBOUND_X -> this.startRebound(Direction.Axis.X);
            case EVENT_REBOUND_Y -> this.startRebound(Direction.Axis.Y);
            case EVENT_REBOUND_Z -> this.startRebound(Direction.Axis.Z);
            case EVENT_GRAB -> this.grabTick = this.tickCount;
            default -> super.handleEntityEvent(event);
        }
    }

    @Environment(EnvType.CLIENT)
    private void startRebound(Direction.Axis axis) {
        this.reboundAxis = axis;
        this.reboundTick = this.tickCount;
    }

    //endregion

    //region Serialization

    private static final String FLIGHT_KEY = "flight";
    private static final String LOOK_KEY = "look";
    private static final String AGE_KEY = "age";
    private static final String LEG_TICKS_KEY = "leg_ticks";
    private static final String PASS_KEY = "pass";
    private static final String RANGE_KEY = "range";
    private static final String SPEED_KEY = "speed";
    private static final String PHASE_KEY = "phase";
    private static final String HEADING_KEY = "heading";
    private static final String PASS_START_KEY = "pass_start";

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.store(FLIGHT_KEY, Flight.CODEC, this.flight);
        output.store(LOOK_KEY, Look.CODEC, this.look);
        output.putInt(AGE_KEY, this.age);
        output.putInt(LEG_TICKS_KEY, this.legTicks);
        output.putInt(PASS_KEY, this.pass);
        output.putFloat(RANGE_KEY, this.range);
        output.putFloat(SPEED_KEY, this.speed);
        output.putByte(PHASE_KEY, this.getPhase());
        output.storeNullable(HEADING_KEY, Vec3.CODEC, this.heading);
        output.store(PASS_START_KEY, Vec3.CODEC, this.passStart);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setFlight(input.read(FLIGHT_KEY, Flight.CODEC).orElse(Flight.DEFAULT));
        this.setLook(input.read(LOOK_KEY, Look.CODEC).orElse(Look.DEFAULT));
        this.age = input.getIntOr(AGE_KEY, 0);
        this.pass = input.getIntOr(PASS_KEY, 0);
        this.range = input.getFloatOr(RANGE_KEY, this.flight.range());
        this.speed = input.getFloatOr(SPEED_KEY, 0.0F);
        this.setPhase(input.getByteOr(PHASE_KEY, OUT));
        this.legTicks = input.getIntOr(LEG_TICKS_KEY, 0);
        this.heading = input.read(HEADING_KEY, Vec3.CODEC).orElse(null);
        this.passStart = input.read(PASS_START_KEY, Vec3.CODEC).orElse(this.position());
    }

    //endregion

    /**
     * Distances are in blocks, speeds in blocks per tick, durations in ticks.
     *
     * @param homing       share of the way to its target the heading turns each tick on the way back, from 0 to 1
     * @param rangeLoss    share of the range lost on every missed pass
     * @param missCooldown wait before the next throw once the boomerang vanished uncaught
     */
    public record Flight(
            float range,
            int outTicks,
            EasingType outEasing,
            int hangTicks,
            float returnSpeed,
            float returnAcceleration,
            float homing,
            float rangeLoss,
            float minRange,
            int lifetime,
            float damage,
            float knockback,
            float pickupReach,
            float catchReach,
            int maxCarried,
            int missCooldown
    ) {
        public static final Flight DEFAULT = new Flight(8.0F, 14, EasingType.OUT_QUAD, 3, 0.9F, 0.1F, 0.35F, 0.25F, 2.0F, 200, 3.0F, 0.0F, 0.5F, 0.6F, 8, 20);

        public static final MapCodec<Flight> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("range", DEFAULT.range).forGetter(Flight::range),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("out_ticks", DEFAULT.outTicks).forGetter(Flight::outTicks),
                EasingType.CODEC.optionalFieldOf("out_easing", DEFAULT.outEasing).forGetter(Flight::outEasing),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("hang_ticks", DEFAULT.hangTicks).forGetter(Flight::hangTicks),
                ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("return_speed", DEFAULT.returnSpeed).forGetter(Flight::returnSpeed),
                ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("return_acceleration", DEFAULT.returnAcceleration).forGetter(Flight::returnAcceleration),
                ExtraCodecs.floatRange(0.0F, 1.0F).optionalFieldOf("homing", DEFAULT.homing).forGetter(Flight::homing),
                ExtraCodecs.floatRange(0.0F, 1.0F).optionalFieldOf("range_loss", DEFAULT.rangeLoss).forGetter(Flight::rangeLoss),
                ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("min_range", DEFAULT.minRange).forGetter(Flight::minRange),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("lifetime", DEFAULT.lifetime).forGetter(Flight::lifetime),
                ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("damage", DEFAULT.damage).forGetter(Flight::damage),
                ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("knockback", DEFAULT.knockback).forGetter(Flight::knockback),
                ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("pickup_reach", DEFAULT.pickupReach).forGetter(Flight::pickupReach),
                ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("catch_reach", DEFAULT.catchReach).forGetter(Flight::catchReach),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("max_carried", DEFAULT.maxCarried).forGetter(Flight::maxCarried),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("miss_cooldown", DEFAULT.missCooldown).forGetter(Flight::missCooldown)
        ).apply(instance, Flight::new));
        public static final Codec<Flight> CODEC = MAP_CODEC.codec();
    }

    public record Look(
            Holder<Item> item,
            Holder<SoundEvent> reboundSound,
            Holder<SoundEvent> grabSound,
            Holder<SoundEvent> catchSound,
            Holder<SoundEvent> vanishSound,
            ParticleOptions trailParticle,
            ParticleOptions catchParticle,
            ParticleOptions vanishParticle
    ) {
        public static final Look DEFAULT = new Look(
                SuperMarioItems.BOOMERANG.builtInRegistryHolder(),
                SuperMarioSounds.BOOMERANG_REBOUND,
                BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ITEM_PICKUP),
                SuperMarioSounds.BOOMERANG_CATCH,
                SuperMarioSounds.BOOMERANG_VANISH,
                ParticleTypes.CRIT,
                ParticleTypes.CRIT,
                ParticleTypes.POOF
        );

        public static final MapCodec<Look> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Item.CODEC.optionalFieldOf("item", DEFAULT.item).forGetter(Look::item),
                SoundEvent.CODEC.optionalFieldOf("rebound_sound", DEFAULT.reboundSound).forGetter(Look::reboundSound),
                SoundEvent.CODEC.optionalFieldOf("grab_sound", DEFAULT.grabSound).forGetter(Look::grabSound),
                SoundEvent.CODEC.optionalFieldOf("catch_sound", DEFAULT.catchSound).forGetter(Look::catchSound),
                SoundEvent.CODEC.optionalFieldOf("vanish_sound", DEFAULT.vanishSound).forGetter(Look::vanishSound),
                ParticleTypes.CODEC.optionalFieldOf("trail_particle", DEFAULT.trailParticle).forGetter(Look::trailParticle),
                ParticleTypes.CODEC.optionalFieldOf("catch_particle", DEFAULT.catchParticle).forGetter(Look::catchParticle),
                ParticleTypes.CODEC.optionalFieldOf("vanish_particle", DEFAULT.vanishParticle).forGetter(Look::vanishParticle)
        ).apply(instance, Look::new));
        public static final Codec<Look> CODEC = MAP_CODEC.codec();
    }
}
