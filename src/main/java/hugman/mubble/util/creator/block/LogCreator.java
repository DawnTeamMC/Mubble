package hugman.mubble.util.creator.block;

import hugman.mubble.object.item.AxeItem;
import hugman.mubble.util.creator.CreatorHelper;
import hugman.mubble.util.creator.BlockEntry;
import hugman.mubble.util.creator.BlockTemplate;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.Direction;

public class LogCreator extends CreatorHelper {
	private final BlockEntry logEntry;
	private final BlockEntry strippedLogEntry;
	private final BlockEntry woodEntry;
	private final BlockEntry strippedWoodEntry;
	private final String logSuffix;
	private final String woodSuffix;

	private final boolean isNether;

	public LogCreator(String name, MaterialColor insideColor, MaterialColor barkColor, boolean isNether) {
		this.logSuffix = isNether ? "_stem" : "_log";
		this.woodSuffix = isNether ? "_hyphae" : "_wood";
		this.isNether = isNether;
		this.logEntry = new BlockEntry.Builder(name + logSuffix, createLog(insideColor, barkColor)).copy(isNether ? Blocks.CRIMSON_STEM : Blocks.OAK_LOG).build();
		this.strippedLogEntry = new BlockEntry.Builder("stripped_" + name + logSuffix, createLog(insideColor)).copy(isNether ? Blocks.STRIPPED_CRIMSON_STEM : Blocks.STRIPPED_OAK_LOG).build();
		this.woodEntry = new BlockEntry.Builder(name + woodSuffix, createLog(barkColor)).copy(isNether ? Blocks.CRIMSON_HYPHAE : Blocks.OAK_WOOD).build();
		this.strippedWoodEntry = new BlockEntry.Builder("stripped_" + name + woodSuffix, createLog(insideColor)).copy(isNether ? Blocks.STRIPPED_CRIMSON_HYPHAE : Blocks.STRIPPED_OAK_WOOD).build();
		AxeItem.BLOCK_STRIPPING_MAP.put(logEntry.getBlock(), strippedLogEntry.getBlock());
		AxeItem.BLOCK_STRIPPING_MAP.put(woodEntry.getBlock(), strippedWoodEntry.getBlock());
	}

	protected Block createLog(MaterialColor color) {
		FabricBlockSettings settings = isNether ? BlockTemplate.STEM_SETTINGS : BlockTemplate.NORMAL_LOG_SETTINGS;
		return new PillarBlock(settings.materialColor(color));
	}

	protected Block createLog(MaterialColor insideColor, MaterialColor barkColor) {
		return new PillarBlock(AbstractBlock.Settings.of(isNether ? Material.NETHER_WOOD : Material.WOOD, (blockState) -> {
			return blockState.get(PillarBlock.AXIS) == Direction.Axis.Y ? insideColor : barkColor;
		}).strength(2.0F).sounds(isNether ? BlockSoundGroup.NETHER_STEM : BlockSoundGroup.WOOD));
	}

	public Block getLog() {
		return logEntry.getBlock();
	}

	public Block getStrippedLog() {
		return strippedLogEntry.getBlock();
	}

	public Block getWood() {
		return woodEntry.getBlock();
	}

	public Block getStrippedWood() {
		return strippedWoodEntry.getBlock();
	}

	public String getLogSuffix() {
		return logSuffix;
	}

	public String getWoodSuffix() {
		return woodSuffix;
	}
}
