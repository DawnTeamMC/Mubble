package fr.hugman.mubble.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class BlockStateBaseMixin {
    /**
     * Gives the surface of the water something to stand on, for the players that are running on it.
     * <p>
     * Whether the player is on such a run is not decided here: it depends on where their sprint
     * started, which {@code PlayerMixin} follows tick after tick. All this asks on top of it is that
     * the player is above the surface rather than under it.
     */
    @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("RETURN"), cancellable = true)
    private void mubble$getCollisionShape(BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        var state = (BlockBehaviour.BlockStateBase) (Object) this;
        if (context instanceof EntityCollisionContext entityShapeContext
                && entityShapeContext.getEntity() instanceof Player player
                && player.isRunningOnWater()
        ) {
            var fluidState = state.getFluidState();
            if (fluidState.is(FluidTags.WATER)) {
                var shape = fluidState.getShape(world, pos);
                var stateAbove = world.getFluidState(pos.above());
                if (player.getY() > (double) pos.getY() + shape.max(Direction.Axis.Y) - player.maxUpStep()
                        && !stateAbove.getType().isSame(fluidState.getType())
                ) {
                    cir.setReturnValue(shape);
                }
            }

        }
    }
}
