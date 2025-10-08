package fr.hugman.mubble.client.render.block.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.MovingBlockRenderState;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class BumpableBlockEntityRenderState extends BlockEntityRenderState {
	public float bumpProgress;
	public @Nullable Vec3i bumpVector = null;

	public @Nullable MovingBlockRenderState movingState = null;

	public boolean isBumping() {
		return bumpProgress > 0.0f;
	}
}
