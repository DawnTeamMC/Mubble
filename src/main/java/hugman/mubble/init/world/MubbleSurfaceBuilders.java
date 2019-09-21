package hugman.mubble.init.world;

import hugman.mubble.init.MubbleBlockStateProperties;
import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.objects.world.surface_builder.PermafrostSurfaceBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilders.ShatteredSavannaSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class MubbleSurfaceBuilders
{
	public static class BlockStates
	{
		public static final BlockState SMW_GROUND_TOP = MubbleBlocks.SMW_GROUND_GROUND_BLOCK.getDefaultState().with(MubbleBlockStateProperties.OVER, true);
		public static final BlockState SMW_GROUND_DIRT = MubbleBlocks.SMW_GROUND_GROUND_BLOCK.getDefaultState().with(MubbleBlockStateProperties.OVER, false);
		public static final BlockState SMW_DESERT_TOP = MubbleBlocks.SMW_DESERT_GROUND_BLOCK.getDefaultState().with(MubbleBlockStateProperties.OVER, true);
		public static final BlockState SMW_DESERT_DIRT = MubbleBlocks.SMW_DESERT_GROUND_BLOCK.getDefaultState().with(MubbleBlockStateProperties.OVER, false);
		public static final BlockState PERMAROCK = MubbleBlocks.PERMAROCK.getDefaultState();
	}

	public static final SurfaceBuilder<SurfaceBuilderConfig> PERMAFROST_SURFACE_BUILDER = new PermafrostSurfaceBuilder(SurfaceBuilderConfig::deserialize);
	public static final SurfaceBuilder<SurfaceBuilderConfig> SHATTERED_SAVANNA = new ShatteredSavannaSurfaceBuilder(SurfaceBuilderConfig::deserialize);

	public static final SurfaceBuilderConfig PERMAROCK_SURFACE = new SurfaceBuilderConfig(BlockStates.PERMAROCK, BlockStates.PERMAROCK, BlockStates.PERMAROCK);
	public static final SurfaceBuilderConfig SMW_GROUND_SURFACE = new SurfaceBuilderConfig(BlockStates.SMW_GROUND_TOP, BlockStates.SMW_GROUND_DIRT, BlockStates.SMW_GROUND_DIRT);
	public static final SurfaceBuilderConfig SMW_DESERT_SURFACE = new SurfaceBuilderConfig(BlockStates.SMW_DESERT_TOP, BlockStates.SMW_DESERT_DIRT, Blocks.DIRT.getDefaultState());
}