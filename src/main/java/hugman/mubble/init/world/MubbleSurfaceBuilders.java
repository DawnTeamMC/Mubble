package hugman.mubble.init.world;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.data.MubbleBlockStateProperties;
import hugman.mubble.objects.world.surface_builder.PermafrostSurfaceBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class MubbleSurfaceBuilders
{
	private static final BlockState SMW_GROUND_TOP = MubbleBlocks.SMW_GROUND_GROUND_BLOCK.getDefaultState().with(MubbleBlockStateProperties.OVER, true);
	private static final BlockState SMW_GROUND_DIRT = MubbleBlocks.SMW_GROUND_GROUND_BLOCK.getDefaultState().with(MubbleBlockStateProperties.OVER, false);
	private static final BlockState SMW_DESERT_TOP = MubbleBlocks.SMW_DESERT_GROUND_BLOCK.getDefaultState().with(MubbleBlockStateProperties.OVER, true);
	private static final BlockState SMW_DESERT_DIRT = MubbleBlocks.SMW_DESERT_GROUND_BLOCK.getDefaultState().with(MubbleBlockStateProperties.OVER, false);
	private static final BlockState PERMAROCK = MubbleBlocks.PERMAROCK.getDefaultState();
	private static final BlockState DIRT = Blocks.DIRT.getDefaultState();

	public static final SurfaceBuilder<SurfaceBuilderConfig> PERMAFROST_SURFACE_BUILDER = new PermafrostSurfaceBuilder(SurfaceBuilderConfig::deserialize);

	public static final SurfaceBuilderConfig PERMAROCK_SURFACE = new SurfaceBuilderConfig(PERMAROCK, PERMAROCK, PERMAROCK);
	public static final SurfaceBuilderConfig SMW_GROUND_SURFACE = new SurfaceBuilderConfig(SMW_GROUND_TOP, SMW_GROUND_DIRT, SMW_GROUND_DIRT);
	public static final SurfaceBuilderConfig SMW_DESERT_SURFACE = new SurfaceBuilderConfig(SMW_DESERT_TOP, SMW_DESERT_DIRT, DIRT);
}