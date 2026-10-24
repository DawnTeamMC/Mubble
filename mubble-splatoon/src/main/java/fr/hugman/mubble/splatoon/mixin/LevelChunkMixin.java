package fr.hugman.mubble.splatoon.mixin;

import fr.hugman.mubble.splatoon.world.level.ink.InkBlockChanges;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunk.class)
public class LevelChunkMixin {
    /**
     * Every block change of a loaded chunk goes through here, whatever made it (players, pistons, explosions,
     * flowing liquids...), which keeps the ink in line with the blocks it is painted on.
     */
    @Inject(method = "setBlockState", at = @At("RETURN"))
    private void splatoon$updateInk(BlockPos pos, BlockState state, int flags, CallbackInfoReturnable<BlockState> cir) {
        var oldState = cir.getReturnValue();
        var chunk = (LevelChunk) (Object) this;
        if (oldState != null && chunk.getLevel() instanceof ServerLevel level) {
            InkBlockChanges.onBlockChanged(level, chunk, pos, oldState, state);
        }
    }
}
