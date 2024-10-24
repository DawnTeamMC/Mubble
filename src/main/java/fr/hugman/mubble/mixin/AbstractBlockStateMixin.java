package fr.hugman.mubble.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockStateMixin {
    @Inject(method = "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", at = @At("RETURN"), cancellable = true)
    private void mubble$getCollisionShape(BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        var state = (AbstractBlock.AbstractBlockState) (Object) this;
        if (context instanceof EntityShapeContext entityShapeContext
                && entityShapeContext.getEntity() instanceof PlayerEntity player
                && player.getPowerUp().flatMap(power -> Optional.of(power.value().canSprintOnWater())).orElse(false)
                && player.isSprinting()
        ) {
            var fluidState = state.getFluidState();
            if (fluidState.isIn(FluidTags.WATER)) {
                var shape = fluidState.getShape(world, pos);
                var stateAbove = world.getFluidState(pos.up());
                if (player.getY() > (double) pos.getY() + shape.getMax(Direction.Axis.Y) - player.getStepHeight()
                        && !stateAbove.getFluid().matchesType(fluidState.getFluid())
                ) {
                    cir.setReturnValue(shape);
                }
            }

        }
    }
}
