package hugman.mubble.object.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class BalloonBlock extends FlyingBlock {
	public BalloonBlock(Block.Settings builder) {
		super(builder);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public float getAmbientOcclusionLightLevel(BlockState state, BlockView worldIn, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView reader, BlockPos pos) {
		return true;
	}
}
