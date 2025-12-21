package fr.hugman.mubble.mixin;

import fr.hugman.mubble.power_up.PowerUpHolder;
import fr.hugman.mubble.tags.MubbleEntityTypeTags;
import fr.hugman.mubble.world.entity.Stompable;
import fr.hugman.mubble.references.MubbleDamageTypeKeys;
import fr.hugman.mubble.world.level.block.HittableBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

@Mixin(Entity.class)
public class EntityMixin implements Stompable {
    // Inject right before the second call of setPosition() in the method move()
    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setPos(Lnet/minecraft/world/phys/Vec3;)V", ordinal = 0))
    private void mubble$move(MoverType type, Vec3 movement, CallbackInfo ci) {
        Entity this_ = (Entity) (Object) this;
        Level level = this_.level();
        Vec3 vec3d = this.collide(movement);
        if (vec3d != null && vec3d.y() > 0) {
            Vec3 headPos = this_.position().add(0, this_.getBbHeight(), 0);
            BlockHitResult hit = level.clip(new ClipContext(headPos, headPos.add(vec3d).add(0, HittableBlock.HIT_Y_OFFSET, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this_));
            if (hit.getType() == HitResult.Type.BLOCK && hit.getDirection() == Direction.DOWN) {
                BlockPos blockPos = hit.getBlockPos();
                BlockState state = level.getBlockState(blockPos);
                if (state.getBlock() instanceof HittableBlock hittableBlock) {
                    hittableBlock.onHit(level, state, this_, hit);
                }
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void mubble$tick(CallbackInfo ci) {
        Entity this_ = (Entity) (Object) this;
        if (this.canBeStomped()) {
            AABB hitBox = this.getStompBox();
            if (hitBox != null) {
                List<Entity> list = this_.level().getEntities(this_, hitBox, this.getStompableBy());
                if (!list.isEmpty()) {
                    this.onStompedBy(list);
                }
            }
        }
    }

    @Inject(method = "onRemoval", at = @At("HEAD"))
    private void mubble$onRemove(CallbackInfo ci) {
        Entity this_ = (Entity) (Object) this;
        if(this_ instanceof TraceableEntity ownable && ownable.getOwner() instanceof PowerUpHolder powerUpHolder) {
            // if the projectile isn't in the properties it won't set dirty so it's okay to not check for it
            powerUpHolder.getPowerUpProperties().removeProjectile(this_.getUUID());
        }
    }

    @Shadow
    private Vec3 collide(Vec3 movement) {
        return null;
    }

    @Override
    public boolean canBeStomped() {
        var this_ = ((Entity) (Object) this);
        return this_.is(MubbleEntityTypeTags.STOMPABLE) && !this_.isSpectator() && !this_.isVehicle();
    }

    @Override
    public AABB getStompBox() {
        var this_ = ((Entity) (Object) this);
        AABB hitBox = this_.getBoundingBox();
        hitBox = hitBox.setMinY(hitBox.maxY - (0.2D * (hitBox.maxY - hitBox.minY)));
        hitBox = hitBox.setMaxY(hitBox.maxY + 0.5D);

        return hitBox;
    }

    @Override
    public Predicate<? super Entity> getStompableBy() {
        return EntitySelector.NO_SPECTATORS.and(entity ->
                entity.is(MubbleEntityTypeTags.CAN_STOMP) &&
                        !entity.onGround() &&
                        entity.getDeltaMovement().y() < 0.3D &&
                        entity.isAlive());
    }

    @Override
    public void onStompedBy(List<Entity> entities) {
        var this_ = ((Entity) (Object) this);
        //TODO: display particles!
        if (this_.level() instanceof ServerLevel serverLevel) {
            //TODO: calculate damage using boots?
            this_.hurtServer(serverLevel, this_.damageSources().source(MubbleDamageTypeKeys.STOMP, entities.getFirst()), 2.0F);
            for (Entity entity : entities) {
                entity.setDeltaMovement(entity.getDeltaMovement().x, 0.5D, entity.getDeltaMovement().z);
                if (entity instanceof Player player) {
                    ((ServerPlayer) player).connection.send(new ClientboundSetEntityMotionPacket(player));
                }
                entity.fallDistance = 0.0F;
            }
        }
    }
}