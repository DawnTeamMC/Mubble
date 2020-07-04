package hugman.mubble.entry.block;

import com.google.common.collect.ImmutableMap;
import hugman.mubble.object.block.PressurePlateBlock;
import hugman.mubble.object.block.StairsBlock;
import hugman.mubble.object.block.StoneButtonBlock;
import hugman.mubble.object.block.VerticalSlabBlock;
import hugman.mubble.util.creator.BlockCreatorUtil;
import hugman.mubble.util.creator.BlockShape;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.PressurePlateBlock.ActivationRule;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.util.DyeColor;

import java.util.Map;

public class ColoredBlockEntry {
	private final Map<DyeColor, Block> COLOR_MAP;

	public ColoredBlockEntry(String name, BlockShape shape, Block baseBlock, FabricBlockSettings settings) {
		ImmutableMap.Builder builder = new ImmutableMap.Builder<DyeColor, Block>();
		for(DyeColor color : DyeColor.values()) {
			Block block = BlockCreatorUtil.registerBlock(getConfiguredBaseBlock(baseBlock, shape, settings, color), color.getName() + "_" + name + shape.getSuffix(), baseBlock);
			builder.put(color, block);
		}
		COLOR_MAP = builder.build();
	}

	public ColoredBlockEntry(String name, BlockShape shape, Block baseBlock) {
		this(name, shape, baseBlock, FabricBlockSettings.copyOf(baseBlock));
	}

	protected Block getConfiguredBaseBlock(Block baseBlock, BlockShape shape, FabricBlockSettings settings, DyeColor color) {
		switch(shape) {
			case CUBE:
			default:
				return new Block(settings.materialColor(color));
			case STAIRS:
				return new StairsBlock(baseBlock, settings.materialColor(color));
			case SLAB:
				return new SlabBlock(settings.materialColor(color));
			case VERTICAL_SLAB:
				return new VerticalSlabBlock(settings.materialColor(color));
			case WALL:
				return new WallBlock(settings.materialColor(color));
			case PRESSURE_PLATE:
				return new PressurePlateBlock(ActivationRule.MOBS, settings);
			case BUTTON:
				return new StoneButtonBlock(settings);
		}
	}

	public Block getBlock(DyeColor color) {
		return COLOR_MAP.get(color);
	}
}
