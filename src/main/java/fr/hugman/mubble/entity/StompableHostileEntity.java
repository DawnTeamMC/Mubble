package fr.hugman.mubble.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

/**
 * Represents a hostile entity that can be stomped on.
 *
 * @author Hugman
 * @since v4.0.0
 */
abstract public class StompableHostileEntity extends HostileEntity {
    protected static final TrackedData<Boolean> STOMPED = DataTracker.registerData(StompableHostileEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected StompableHostileEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        if (this.canBeStomped()) {
            Box hitBox = this.getStompBox();
            List<Entity> list = this.getWorld().getOtherEntities(this, hitBox, this.getStompableBy());
            if (!list.isEmpty()) {
                this.stomp(true);
                if (this.getWorld() instanceof ServerWorld serverWorld) {
                    this.onStompedBy(serverWorld, list);
                }
            }
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(STOMPED, false);
    }

    public boolean isStomped() {
        return this.dataTracker.get(STOMPED);
    }

    public void stomp(boolean b) {
        this.dataTracker.set(STOMPED, b);
    }

    public boolean canBeStomped() {
        return this.getHealth() > 0.0F && !this.isSpectator() && !this.hasPassengers();
    }

    /**
     * Returns the box that is used to check if an entity can bump on top of this entity.
     */
    public Box getStompBox() {
        Box hitBox = this.getBoundingBox();
        hitBox = hitBox.withMinY(hitBox.maxY - (0.2D * (hitBox.maxY - hitBox.minY)));
        hitBox = hitBox.withMaxY(hitBox.maxY + 0.5D);

        return hitBox;
    }

    /**
     * Returns a predicate that determines if an entity can bump on top of this entity.
     */
    public Predicate<? super Entity> getStompableBy() {
        return EntityPredicates.EXCEPT_SPECTATOR.and(entity ->
                entity.getType().isIn(MubbleEntityTypeTags.CAN_JUMP_BUMP) &&
                !entity.isOnGround() &&
                entity.getVelocity().getY() < 0.3D &&
                entity.isAlive());
    }

    /**
     * Called when this entity is bumped on top by another entity. Fired on the server side only.
     * @param entities the entities that bumped on top of this entity
     */
    public void onStompedBy(ServerWorld serverWorld, List<Entity> entities) {
        if(!entities.isEmpty()) {
            // TODO: custom damage source
            this.damage(serverWorld, this.getDamageSources().fallingBlock(entities.getFirst()), Float.MAX_VALUE);
        }
        else {
            this.damage(serverWorld, this.getDamageSources().genericKill(), Float.MAX_VALUE);
        }
        for (Entity entity : entities) {
            entity.setVelocity(entity.getVelocity().x, 0.5D, entity.getVelocity().z);
            if (entity instanceof PlayerEntity player) {
                ((ServerPlayerEntity) player).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
            }
            entity.fallDistance = 0.0F;
        }
    }
}
