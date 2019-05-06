package hugman.mod.init.world;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.objects.world.surface_builder.PermafrostSurfaceBuilder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class MubbleSurfaceBuilders
{
	protected static final IBlockState PERMAROCK = MubbleBlocks.PERMAROCK.getDefaultState();
	
	public static final ISurfaceBuilder<SurfaceBuilderConfig> PERMAFROST_SURFACE_BUILDER = new PermafrostSurfaceBuilder();
	public static final SurfaceBuilderConfig PERMAROCK_SURFACE = new SurfaceBuilderConfig(PERMAROCK, PERMAROCK, PERMAROCK);
}