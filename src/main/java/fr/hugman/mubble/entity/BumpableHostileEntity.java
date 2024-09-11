package fr.hugman.mubble.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

/**
 * Represents a hostile entity that can be bumped on top of.
 *
 * @author Hugman
 * @since v4.0.0
 */
abstract public class BumpableHostileEntity extends HostileEntity {
    protected BumpableHostileEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        // TODO: disable all this if the entity is being ridden
        // TODO: stack eachother ontop?
        if (isBumpable()) {
            Box hitBox = this.getBumpBox();
            if (!this.getWorld().isClient) {
                List<Entity> list = this.getWorld().getOtherEntities(this, hitBox, this.getBumpableOnBy());
                if (!list.isEmpty()) {
                    this.onBumpedOnTopBy(list);
                }
            }
        }
    }

    /**
     * Returns whether this entity can be bumped on top of.
     */
    public boolean isBumpable() {
        return this.getHealth() > 0.0F && !this.isSpectator();
    }

    /**
     * Returns the box that is used to check if an entity can bump on top of this entity.
     */
    public Box getBumpBox() {
        Box hitBox = this.getBoundingBox();
        hitBox = hitBox.withMinY(hitBox.maxY - (0.2D * (hitBox.maxY - hitBox.minY)));
        hitBox = hitBox.withMaxY(hitBox.maxY + 0.5D);

        return hitBox;
    }

    /**
     * Returns a predicate that determines if an entity can bump on top of this entity.
     */
    public Predicate<? super Entity> getBumpableOnBy() {
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
    public void onBumpedOnTopBy(List<Entity> entities) {
        // TODO: set damage source to first entity in list
        this.damage(this.getDamageSources().genericKill(), Float.MAX_VALUE);
        for (Entity entity : entities) {
            entity.setVelocity(entity.getVelocity().x, 0.5D, entity.getVelocity().z);
            if (entity instanceof PlayerEntity player) {
                ((ServerPlayerEntity) player).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
            }
            entity.fallDistance = 0.0F;
        }
    }
}
