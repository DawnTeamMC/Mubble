package fr.hugman.mubble.super_mario.world.entity.projectile;

import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.tags.SuperMarioEntityTypeTags;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.entity.Stompable;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
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
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * A bubble entity that can be shot by the Bubble Flower power-up.
 * It travels forward, rebounds off blocks, and can trap small entities.
 *
 * @author Copilot
 */
public class Bubble extends Projectile implements Stompable {
    public static final ClientAsset.ResourceTexture TEXTURE = new ClientAsset.ResourceTexture(SuperMario.id("entity/bubble"));

    // Ticks before the bubble pops naturally (5 seconds)
    public static final int EMPTY_LIFETIME = 100;
    // Ticks after catching an entity before the bubble pops (5 seconds)
    public static final int FILLED_LIFETIME = 100;
    // Ticks before the owner can pop the bubble
    public static final int OWNER_POP_DELAY = 20;
    // Radius at which the bubble is attracted to trappable entities
    public static final double ATTRACT_RADIUS = 2.0;
    // Max HP for automatically trappable entities
    public static final float MAX_TRAPPABLE_HP = 20.0f;
    // Max size for automatically trappable entities (width and height)
    public static final float MAX_TRAPPABLE_SIZE = 2.0f;
    // Air friction applied each tick
    public static final double AIR_FRICTION = 0.98;
    // Upward float speed when horizontal is slow
    public static final double FLOAT_SPEED = 0.03;
    // Horizontal speed threshold for floating
    public static final double FLOAT_THRESHOLD = 0.15;

    private static final String SPAWN_TIME_KEY = "spawn_time";
    private static final String CATCH_TIME_KEY = "catch_time";
    private static final String TRAPPED_ENTITY_KEY = "trapped_entity";

    private static final EntityDataAccessor<Boolean> HAS_TRAPPED_ENTITY = SynchedEntityData.defineId(Bubble.class, EntityDataSerializers.BOOLEAN);

    private int spawnTime = 0;
    private int catchTime = -1;
    @Nullable
    private UUID trappedEntityUUID = null;

    public Bubble(EntityType<? extends Bubble> type, Level level) {
        super(type, level);
    }

    public Bubble(Level level, LivingEntity owner) {
        super(SuperMarioEntityTypes.BUBBLE, level);
        this.setOwner(owner);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(HAS_TRAPPED_ENTITY, false);
    }

    public boolean hasTrappedEntity() {
        return this.entityData.get(HAS_TRAPPED_ENTITY);
    }

    public void setHasTrappedEntity(boolean value) {
        this.entityData.set(HAS_TRAPPED_ENTITY, value);
    }

    @Nullable
    public Entity getTrappedEntity() {
        if (this.trappedEntityUUID == null) return null;
        return this.level().getEntity(this.trappedEntityUUID);
    }

    public boolean isEmpty() {
        return !this.hasTrappedEntity();
    }

    @Override
    public void tick() {
        if (this.firstTick) {
            this.spawnTime = this.tickCount;
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SuperMarioSounds.BUBBLE_APPEAR.value(), this.getSoundSource(), 0.5F, 1.0F);
        }

        super.tick();

        if (!this.level().isClientSide()) {
            // Check natural expiry
            if (this.isEmpty()) {
                if (this.tickCount - this.spawnTime >= EMPTY_LIFETIME) {
                    this.pop();
                    return;
                }
            } else {
                if (this.catchTime >= 0 && this.tickCount - this.catchTime >= FILLED_LIFETIME) {
                    this.pop();
                    return;
                }
            }

            // Update trapped entity position
            Entity trapped = this.getTrappedEntity();
            if (trapped != null && !trapped.isPassengerOfSameVehicle(this)) {
                // Passenger was removed, clear trapped entity
                this.clearTrappedEntity();
            }
        }

        Vec3 movement = this.getDeltaMovement();

        // Apply air friction
        movement = movement.multiply(AIR_FRICTION, AIR_FRICTION, AIR_FRICTION);

        // Slightly float upward when horizontal speed is low
        if (movement.horizontalDistance() < FLOAT_THRESHOLD) {
            movement = movement.add(0.0, FLOAT_SPEED, 0.0);
        }

        this.setDeltaMovement(movement);

        // Check hit result for collisions
        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitResult.getType() != HitResult.Type.MISS) {
            this.hitTargetOrDeflectSelf(hitResult);
        }

        // Move
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.needsSync = true;

        if (!this.level().isClientSide() && this.isEmpty()) {
            // Attract to nearby trappable entities
            this.attractToNearbyEntities();

            // Check for entity interactions in the bubble's bounding box
            this.checkEntityCollisions();
        }
    }

    private void attractToNearbyEntities() {
        AABB attractBox = this.getBoundingBox().inflate(ATTRACT_RADIUS);
        List<Entity> nearby = this.level().getEntities(this, attractBox, e -> canTrap(e) && e.isAlive());
        if (!nearby.isEmpty()) {
            Entity target = nearby.getFirst();
            Vec3 dir = target.position().add(0, target.getBbHeight() / 2, 0)
                    .subtract(this.getX(), this.getY() + this.getBbHeight() / 2, this.getZ())
                    .normalize()
                    .scale(0.05);
            this.setDeltaMovement(this.getDeltaMovement().add(dir));
        }
    }

    private void checkEntityCollisions() {
        AABB bb = this.getBoundingBox();
        List<Entity> entities = this.level().getEntities(this, bb, Entity::isAlive);
        Entity owner = this.getOwner();
        int age = this.tickCount - this.spawnTime;

        for (Entity entity : entities) {
            // Skip passengers
            if (entity.isPassengerOfSameVehicle(this)) continue;

            if (this.isEmpty()) {
                // Boss or blacklisted → pop
                if (isBoss(entity) || entity.getType().is(SuperMarioEntityTypeTags.BUBBLE_CANNOT_TRAP)) {
                    this.pop();
                    return;
                }
                // Owner (after delay) → pop
                if (entity == owner && age >= OWNER_POP_DELAY) {
                    this.pop();
                    return;
                }
                // Super Mario entity → insert coin (coin spawns inside bubble as passenger)
                if (entity.getType().is(SuperMarioEntityTypeTags.ALL)) {
                    // Just pop for now – TODO: create coin inside bubble
                    this.pop();
                    return;
                }
                // Try to trap the entity
                if (canTrap(entity)) {
                    this.trapEntity(entity);
                    return;
                }
            } else {
                // Filled bubble: any entity touching it pops it (except the trapped entity)
                if (!entity.isPassengerOfSameVehicle(this)) {
                    this.pop();
                    return;
                }
            }
        }
    }

    private boolean isBoss(Entity entity) {
        return entity instanceof EnderDragon || entity instanceof WitherBoss;
    }

    public boolean canTrap(Entity entity) {
        if (!(entity instanceof LivingEntity living)) return false;
        if (entity.getType().is(SuperMarioEntityTypeTags.BUBBLE_CANNOT_TRAP)) return false;
        if (isBoss(entity)) return false;
        if (entity.getType().is(SuperMarioEntityTypeTags.BUBBLE_CAN_TRAP)) return true;
        // Auto-trappable if small enough and low enough HP
        return entity.getBbWidth() <= MAX_TRAPPABLE_SIZE
                && entity.getBbHeight() <= MAX_TRAPPABLE_SIZE
                && living.getMaxHealth() <= MAX_TRAPPABLE_HP;
    }

    public void trapEntity(Entity entity) {
        if (this.level().isClientSide()) return;
        if (!entity.startRiding(this, true)) return;

        this.trappedEntityUUID = entity.getUUID();
        this.catchTime = this.tickCount;
        this.setHasTrappedEntity(true);

        // Resize the bubble slightly to fit the entity
        this.refreshDimensions();

        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SuperMarioSounds.BUBBLE_FILL.value(), this.getSoundSource(), 0.5F, 1.0F);
    }

    public void clearTrappedEntity() {
        this.trappedEntityUUID = null;
        this.catchTime = -1;
        this.setHasTrappedEntity(false);
        this.refreshDimensions();
    }

    public void pop() {
        if (!this.level().isClientSide()) {
            // Eject any trapped entity
            Entity trapped = this.getTrappedEntity();
            if (trapped != null) {
                trapped.stopRiding();
            }
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SuperMarioSounds.BUBBLE_POP.value(), this.getSoundSource(), 0.5F, 1.0F);
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        // Rebound off block - reverse velocity component based on hit face
        Vec3 vel = this.getDeltaMovement();
        switch (result.getDirection().getAxis()) {
            case X -> this.setDeltaMovement(-vel.x * 0.8, vel.y, vel.z);
            case Y -> this.setDeltaMovement(vel.x, -vel.y * 0.8, vel.z);
            case Z -> this.setDeltaMovement(vel.x, vel.y, -vel.z * 0.8);
        }
        this.needsSync = true;
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SuperMarioSounds.BUBBLE_REBOUND.value(), this.getSoundSource(), 0.5F, 1.0F);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        // Entity collision is handled in checkEntityCollisions during tick
    }

    @Override
    public boolean canBeCollidedWith(@Nullable Entity other) {
        return other != null && !other.isSpectator() && this.isAlive();
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        // Bubble has no health, but can be destroyed by projectiles
        if (source.getDirectEntity() instanceof Projectile) {
            this.pop();
            return true;
        }
        return false;
    }

    @Override
    public boolean isPickable() {
        return this.isAlive();
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0;
    }

    // Stompable interface
    @Override
    public boolean canBeStomped() {
        return this.isAlive();
    }

    @Override
    public AABB getStompBox() {
        AABB bb = this.getBoundingBox();
        return new AABB(bb.minX, bb.maxY - 0.1, bb.minZ, bb.maxX, bb.maxY + 0.5, bb.maxZ);
    }

    @Override
    public Predicate<? super Entity> getStompableBy() {
        return EntitySelector.NO_SPECTATORS.and(e -> !e.onGround() && e.getDeltaMovement().y() < 0.0 && e.isAlive());
    }

    @Override
    public void onStompedBy(Entity entity) {
        if (this.level() instanceof ServerLevel) {
            // Give stomp boost to the stomper
            entity.setDeltaMovement(entity.getDeltaMovement().x, 0.5, entity.getDeltaMovement().z);
            entity.fallDistance = 0.0F;
            this.pop();
        }
    }

    @Override
    public Vec3 getPassengerRidingPosition(Entity passenger) {
        return this.position().add(0.0, this.getBbHeight() * 0.5 - passenger.getRidingOffset(this), 0.0);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.spawnTime = input.getIntOr(SPAWN_TIME_KEY, 0);
        this.catchTime = input.getIntOr(CATCH_TIME_KEY, -1);
        Optional<UUID> uuid = input.read(TRAPPED_ENTITY_KEY, UUIDUtil.CODEC);
        this.trappedEntityUUID = uuid.orElse(null);
        this.setHasTrappedEntity(this.trappedEntityUUID != null);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt(SPAWN_TIME_KEY, this.spawnTime);
        output.putInt(CATCH_TIME_KEY, this.catchTime);
        if (this.trappedEntityUUID != null) {
            output.store(TRAPPED_ENTITY_KEY, UUIDUtil.CODEC, this.trappedEntityUUID);
        }
    }

    public ClientAsset.ResourceTexture getTexture() {
        return TEXTURE;
    }
}
