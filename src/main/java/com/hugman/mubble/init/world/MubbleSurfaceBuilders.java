package com.hugman.mubble.init.world;

import com.hugman.mubble.init.MubbleBlockPack;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

public class MubbleSurfaceBuilders {
	private static final BlockState SMW_GROUND_TOP = MubbleBlockPack.SMW_GROUND_GROUND_BLOCK.getDefaultState().with(MubbleBlockPack.Properties.OVER, true);
	private static final BlockState SMW_GROUND_DIRT = MubbleBlockPack.SMW_GROUND_GROUND_BLOCK.getDefaultState().with(MubbleBlockPack.Properties.OVER, false);
	private static final BlockState SMW_DESERT_TOP = MubbleBlockPack.SMW_DESERT_GROUND_BLOCK.getDefaultState().with(MubbleBlockPack.Properties.OVER, true);
	private static final BlockState SMW_DESERT_DIRT = MubbleBlockPack.SMW_DESERT_GROUND_BLOCK.getDefaultState().with(MubbleBlockPack.Properties.OVER, false);
	private static final BlockState AMARANTH_DYLIUM = MubbleBlockPack.AMARANTH_DYLIUM.getDefaultState();
	private static final BlockState END_STONE = Blocks.END_STONE.getDefaultState();
	private static final BlockState DIRT = Blocks.DIRT.getDefaultState();

	public static final TernarySurfaceConfig SMW_GROUND_SURFACE = new TernarySurfaceConfig(SMW_GROUND_TOP, SMW_GROUND_DIRT, SMW_GROUND_DIRT);
	public static final TernarySurfaceConfig SMW_DESERT_SURFACE = new TernarySurfaceConfig(SMW_DESERT_TOP, SMW_DESERT_DIRT, DIRT);
	public static final TernarySurfaceConfig AMARANTH_DYLIUM_SURFACE = new TernarySurfaceConfig(AMARANTH_DYLIUM, END_STONE, END_STONE);
}