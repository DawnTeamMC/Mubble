package fr.hugman.mubble.super_mario.client.renderer.blockentity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class SnakeMovingBlockRenderState extends BlockEntityRenderState {
    public @Nullable MovingBlockRenderState block = null;
    /** Where the block sits relative to the cell it is stored in, in blocks. */
    public double offsetX;
    public double offsetY;
    public double offsetZ;
}
