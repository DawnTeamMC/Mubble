package hugman.mubble.util.entry.pack;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.object.item.AxeItem;
import hugman.mubble.util.entry.EntryHelper;
import hugman.mubble.util.entry.BlockEntry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.Direction;

public class LogsEntryPack extends EntryHelper {
	private final Block log;
	private final Block strippedLog;
	private final Block wood;
	private final Block strippedWood;
	private final String logSuffix;
	private final String woodSuffix;

	private final boolean isNether;

	/**
	 * Creates an entry pack containing a log and wood block and their stripped variants.
	 * @param suffix The suffix of the leaves.
	 * @param insideColor The inside color of the log block.
	 * @param barkColor The bark color of the log block.
	 * @param isNether Defines if the the blocks are used for nether trees. (changes the name, sounds and materials)
	 */
	public LogsEntryPack(String suffix, MaterialColor insideColor, MaterialColor barkColor, boolean isNether) {
		this.logSuffix = isNether ? "_stem" : "_log";
		this.woodSuffix = isNether ? "_hyphae" : "_wood";
		this.isNether = isNether;
		this.log = new BlockEntry.Builder(suffix + logSuffix, createLog(insideColor, barkColor)).copy(isNether ? Blocks.CRIMSON_STEM : Blocks.OAK_LOG).build();
		this.strippedLog = new BlockEntry.Builder("stripped_" + suffix + logSuffix, createLog(insideColor)).copy(isNether ? Blocks.STRIPPED_CRIMSON_STEM : Blocks.STRIPPED_OAK_LOG).build();
		this.wood = new BlockEntry.Builder(suffix + woodSuffix, createLog(barkColor)).copy(isNether ? Blocks.CRIMSON_HYPHAE : Blocks.OAK_WOOD).build();
		this.strippedWood = new BlockEntry.Builder("stripped_" + suffix + woodSuffix, createLog(insideColor)).copy(isNether ? Blocks.STRIPPED_CRIMSON_HYPHAE : Blocks.STRIPPED_OAK_WOOD).build();
		AxeItem.BLOCK_STRIPPING_MAP.put(log, strippedLog);
		AxeItem.BLOCK_STRIPPING_MAP.put(wood, strippedWood);
	}

	protected Block createLog(MaterialColor color) {
		FabricBlockSettings settings = isNether ? MubbleBlocks.Settings.STEM : MubbleBlocks.Settings.LOG;
		return new PillarBlock(settings.materialColor(color));
	}

	protected Block createLog(MaterialColor insideColor, MaterialColor barkColor) {
		return new PillarBlock(AbstractBlock.Settings.of(isNether ? Material.NETHER_WOOD : Material.WOOD, (blockState) -> {
			return blockState.get(PillarBlock.AXIS) == Direction.Axis.Y ? insideColor : barkColor;
		}).strength(2.0F).sounds(isNether ? BlockSoundGroup.NETHER_STEM : BlockSoundGroup.WOOD));
	}

	public Block getLog() {
		return log;
	}

	public Block getStrippedLog() {
		return strippedLog;
	}

	public Block getWood() {
		return wood;
	}

	public Block getStrippedWood() {
		return strippedWood;
	}

	public String getLogSuffix() {
		return logSuffix;
	}

	public String getWoodSuffix() {
		return woodSuffix;
	}
}
