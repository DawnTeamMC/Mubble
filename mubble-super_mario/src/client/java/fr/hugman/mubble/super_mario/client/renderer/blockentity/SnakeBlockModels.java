package fr.hugman.mubble.super_mario.client.renderer.blockentity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Packing a block model up so a renderer can draw it on its own, away from the chunk it stands in.
 *
 * @author Hugman
 * @since v4.0.0
 */
@Environment(EnvType.CLIENT)
final class SnakeBlockModels {
    private SnakeBlockModels() {
    }

    static MovingBlockRenderState extract(ClientLevel level, BlockPos pos, BlockState state) {
        MovingBlockRenderState renderState = new MovingBlockRenderState();
        renderState.randomSeedPos = pos;
        renderState.blockPos = pos;
        renderState.blockState = state;
        renderState.biome = level.getBiome(pos);
        renderState.cardinalLighting = level.cardinalLighting();
        renderState.lightEngine = level.getLightEngine();
        return renderState;
    }
}
