package hugman.mubble.entry.block;

import hugman.mubble.util.creator.BlockCreatorUtil;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.Direction;

public class LogsEntry extends BlockCreatorUtil {
	private final Block log;
	private final Block strippedLog;
	private final Block wood;
	private final Block strippedWood;

	private final boolean isNether;

	public LogsEntry(String name, MaterialColor insideMaterialColor, MaterialColor barkMaterialColor, boolean isNether) {
		String logSuffix = isNether ? "_stem" : "_log";
		String woodSuffix = isNether ? "_hyphae" : "_wood";
		this.isNether = isNether;
		this.log = BlockCreatorUtil.registerBlock(createLog(insideMaterialColor, barkMaterialColor), name + logSuffix, ItemGroup.BUILDING_BLOCKS, 5, 5);
		this.strippedLog = BlockCreatorUtil.registerBlock(createLog(insideMaterialColor), "stripped_" + name + logSuffix, ItemGroup.BUILDING_BLOCKS, 5, 5);
		this.wood = BlockCreatorUtil.registerBlock(createLog(barkMaterialColor), name + woodSuffix, ItemGroup.BUILDING_BLOCKS, 5, 5);
		this.strippedWood = registerLog(createLog(insideMaterialColor), "stripped_" + name + woodSuffix);
	}

	public Block registerLog(Block block, String name) {
		if(!isNether) {
			return BlockCreatorUtil.registerBlock(block, name, ItemGroup.BUILDING_BLOCKS, 5, 5);
		}
		else {
			return BlockCreatorUtil.registerBlock(block, name, ItemGroup.BUILDING_BLOCKS);
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
}
