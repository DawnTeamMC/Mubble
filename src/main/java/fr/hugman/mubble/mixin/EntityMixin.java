package fr.hugman.mubble.mixin;

import fr.hugman.mubble.block.HittableBlock;
import fr.hugman.mubble.entity.MubbleEntityTypeTags;
import fr.hugman.mubble.entity.Stompable;
import fr.hugman.mubble.entity.damage.MubbleDamageTypeKeys;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Predicate;

@Mixin(Entity.class)
public class EntityMixin implements Stompable {
    // Inject right before the second call of setPosition() in the method move()
    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setPosition(DDD)V", ordinal = 1))
    private void mubble$move(MovementType type, Vec3d movement, CallbackInfo ci) {
        Entity thisEntity = (Entity) (Object) this;
        World world = thisEntity.getWorld();
        Vec3d vec3d = this.adjustMovementForCollisions(movement);
        if (vec3d != null && vec3d.getY() > 0) {
            Vec3d headPos = thisEntity.getPos().add(0, thisEntity.getHeight(), 0);
            BlockHitResult hit = world.raycast(new RaycastContext(headPos, headPos.add(vec3d).add(0, HittableBlock.HIT_Y_OFFSET, 0), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, thisEntity));
            if (hit.getType() == HitResult.Type.BLOCK && hit.getSide() == Direction.DOWN) {
                BlockPos blockPos = hit.getBlockPos();
                BlockState state = world.getBlockState(blockPos);
                if (state.getBlock() instanceof HittableBlock hittableBlock) {
                    hittableBlock.onHit(world, state, thisEntity, hit);
                }
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void mubble$tick(CallbackInfo ci) {
        Entity thisEntity = (Entity) (Object) this;
        if (this.canBeStomped()) {
            Box hitBox = this.getStompBox();
            if (hitBox != null) {
                List<Entity> list = thisEntity.getWorld().getOtherEntities(thisEntity, hitBox, this.getStompableBy());
                if (!list.isEmpty()) {
                    this.onStompedBy(list);
                }
            }
        }
    }

    @Shadow
    private Vec3d adjustMovementForCollisions(Vec3d movement) {
        return null;
    }

    @Override
    public boolean canBeStomped() {
        var thisEntity = ((Entity) (Object) this);
        return thisEntity.getType().isIn(MubbleEntityTypeTags.STOMPABLE) && !thisEntity.isSpectator() && !thisEntity.hasPassengers();
    }

    @Override
    public Box getStompBox() {
        var thisEntity = ((Entity) (Object) this);
        Box hitBox = thisEntity.getBoundingBox();
        hitBox = hitBox.withMinY(hitBox.maxY - (0.2D * (hitBox.maxY - hitBox.minY)));
        hitBox = hitBox.withMaxY(hitBox.maxY + 0.5D);

        return hitBox;
    }

    @Override
    public Predicate<? super Entity> getStompableBy() {
        return EntityPredicates.EXCEPT_SPECTATOR.and(entity ->
                entity.getType().isIn(MubbleEntityTypeTags.CAN_STOMP) &&
                        !entity.isOnGround() &&
                        entity.getVelocity().getY() < 0.3D &&
                        entity.isAlive());
    }

    @Override
    public void onStompedBy(List<Entity> entities) {
        var thisEntity = ((Entity) (Object) this);
        //TODO: display particles!
        if (thisEntity.getWorld() instanceof ServerWorld serverWorld) {
            //TODO: calculate damage using boots?
            thisEntity.damage(serverWorld, thisEntity.getDamageSources().create(MubbleDamageTypeKeys.STOMP, entities.getFirst()), 2.0F);
            for (Entity entity : entities) {
                entity.setVelocity(entity.getVelocity().x, 0.5D, entity.getVelocity().z);
                if (entity instanceof PlayerEntity player) {
                    ((ServerPlayerEntity) player).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
                }
                entity.fallDistance = 0.0F;
            }
        }
    }
}