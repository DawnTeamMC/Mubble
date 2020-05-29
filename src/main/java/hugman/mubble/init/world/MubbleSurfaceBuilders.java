package hugman.mubble.init.world;

import hugman.mubble.Mubble;
import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.data.MubbleBlockStateProperties;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceConfig;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

public class MubbleSurfaceBuilders
{
	private static final BlockState SMW_GROUND_TOP = MubbleBlocks.SMW_GROUND_GROUND_BLOCK.getDefaultState().with(MubbleBlockStateProperties.OVER, true);
	private static final BlockState SMW_GROUND_DIRT = MubbleBlocks.SMW_GROUND_GROUND_BLOCK.getDefaultState().with(MubbleBlockStateProperties.OVER, false);
	private static final BlockState SMW_DESERT_TOP = MubbleBlocks.SMW_DESERT_GROUND_BLOCK.getDefaultState().with(MubbleBlockStateProperties.OVER, true);
	private static final BlockState SMW_DESERT_DIRT = MubbleBlocks.SMW_DESERT_GROUND_BLOCK.getDefaultState().with(MubbleBlockStateProperties.OVER, false);
	private static final BlockState PERMAROCK = MubbleBlocks.PERMAROCK.getDefaultState();
	private static final BlockState DIRT = Blocks.DIRT.getDefaultState();

	public static final TernarySurfaceConfig PERMAROCK_SURFACE = new TernarySurfaceConfig(PERMAROCK, PERMAROCK, PERMAROCK);
	public static final TernarySurfaceConfig SMW_GROUND_SURFACE = new TernarySurfaceConfig(SMW_GROUND_TOP, SMW_GROUND_DIRT, SMW_GROUND_DIRT);
	public static final TernarySurfaceConfig SMW_DESERT_SURFACE = new TernarySurfaceConfig(SMW_DESERT_TOP, SMW_DESERT_DIRT, DIRT);

	private static <C extends SurfaceConfig, F extends SurfaceBuilder<C>> F register(String name, F surfaceBuilder)
	{
		return Registry.register(Registry.SURFACE_BUILDER, new Identifier(Mubble.MOD_ID, name), surfaceBuilder);
	}
}