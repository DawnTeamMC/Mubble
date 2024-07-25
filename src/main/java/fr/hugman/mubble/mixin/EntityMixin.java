package fr.hugman.mubble.mixin;

import fr.hugman.mubble.block.HittableBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
	// Inject right before the second call of setPosition() in the method move()
	@Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setPosition(DDD)V", ordinal = 1))
	private void mubble$move(MovementType type, Vec3d movement, CallbackInfo ci) {
		Entity thisEntity = (Entity) (Object) this;
		World world = thisEntity.getWorld();
		Vec3d vec3d = this.adjustMovementForCollisions(movement);
		if(vec3d != null && vec3d.getY() > 0) {
			Vec3d headPos = thisEntity.getPos().add(0, thisEntity.getHeight(), 0);
			BlockHitResult hit = world.raycast(new RaycastContext(headPos, headPos.add(vec3d).add(0, HittableBlock.HIT_Y_OFFSET, 0), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, thisEntity));
			if(hit.getType() == HitResult.Type.BLOCK && hit.getSide() == Direction.DOWN) {
				BlockPos blockPos = hit.getBlockPos();
				BlockState state = world.getBlockState(blockPos);
				if(state.getBlock() instanceof HittableBlock hittableBlock) {
					hittableBlock.onHit(world, state, thisEntity, hit);
				}
			}
		}
	}

	@Shadow
	private Vec3d adjustMovementForCollisions(Vec3d movement) {return null;}
}