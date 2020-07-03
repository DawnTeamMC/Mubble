package hugman.mubble.util.entry.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class LogsEntry extends BlockEntry {
	private final Block log;
	private final Block strippedLog;
	private final Block wood;
	private final Block strippedWood;

	private final boolean isNether;

	public LogsEntry(String name, Block baseBlock, MaterialColor insideMaterialColor, MaterialColor barkMaterialColor, boolean isNether) {
		String logSuffix = isNether ? "_stem" : "_log";
		String woodSuffix = isNether ? "_hyphae" : "_wood";
		this.isNether = isNether;
		this.log = registerBlock(createLog(insideMaterialColor, barkMaterialColor), name + logSuffix);
		this.strippedLog = registerBlock(createLog(insideMaterialColor), "stripped_" + name + logSuffix);
		this.wood = registerBlock(createLog(barkMaterialColor), name + woodSuffix);
		this.strippedWood = registerBlock(createLog(insideMaterialColor), "stripped_" + name + woodSuffix);
		for(Block block : getLogs()) {
			registerBlockItem(block, baseBlock);
			copyFlammability(block, baseBlock);
		}
	}

	protected Block createLog(MaterialColor insideMaterialColor) {
		return new PillarBlock(FabricBlockSettings
				.of(isNether ? Material.NETHER_WOOD : Material.WOOD)
				.materialColor(insideMaterialColor)
				.strength(2.0F)
				.sounds(isNether ? BlockSoundGroup.NETHER_STEM : BlockSoundGroup.WOOD));
	}

	protected Block createLog(MaterialColor insideMaterialColor, MaterialColor barkMaterialColor) {
		return new PillarBlock(AbstractBlock.Settings
				.of(isNether ? Material.NETHER_WOOD : Material.WOOD, (blockState) -> {
					return blockState.get(PillarBlock.AXIS) == Direction.Axis.Y ? insideMaterialColor : barkMaterialColor;
				})
				.strength(2.0F)
				.sounds(isNether ? BlockSoundGroup.NETHER_STEM : BlockSoundGroup.WOOD));
	}

	public Block getLog() {
		return log;
	}

	public Block getStrippedLog() {
		return strippedLog;
	}

	public Block getStrippedWood() {
		return strippedWood;
	}

	public Block getWood() {
		return wood;
	}

	public List<Block> getLogs() {
		List<Block> list = new ArrayList<>();
		list.add(getLog());
		list.add(getStrippedLog());
		list.add(getStrippedWood());
		list.add(getWood());
		return list;
	}
}
