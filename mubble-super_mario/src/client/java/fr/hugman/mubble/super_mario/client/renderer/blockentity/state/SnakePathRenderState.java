package fr.hugman.mubble.super_mario.client.renderer.blockentity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class SnakePathRenderState extends BlockEntityRenderState {
    public @Nullable MovingBlockRenderState block = null;
}
