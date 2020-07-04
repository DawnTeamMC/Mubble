package hugman.mubble.util.creator.block;

import hugman.mubble.util.creator.BlockCreatorHelper;
import hugman.mubble.util.creator.BlockEntry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.Direction;

public class LogCreator extends BlockCreatorHelper {
	private final BlockEntry log;
	private final BlockEntry strippedLog;
	private final BlockEntry wood;
	private final BlockEntry strippedWood;

	private final boolean isNether;

	public final FabricBlockSettings defaultNormalLogSettings = FabricBlockSettings.of(Material.WOOD).strength(2.0F).sounds(BlockSoundGroup.WOOD);
	public final FabricBlockSettings defaultNetherLogSettings = FabricBlockSettings.of(Material.NETHER_WOOD).strength(2.0F).sounds(BlockSoundGroup.NETHER_STEM);
	public static final int defaultNormalLogBurnValue = 5;
	public static final int defaultNormalLogBurnSpread = 5;

	public LogCreator(String name, MaterialColor insideMaterialColor, MaterialColor barkMaterialColor, boolean isNether) {
		String logSuffix = isNether ? "_stem" : "_log";
		String woodSuffix = isNether ? "_hyphae" : "_wood";
		this.isNether = isNether;
		this.log = new BlockEntry(name + logSuffix, createLog(insideMaterialColor, barkMaterialColor)).setItemGroup(ItemGroup.BUILDING_BLOCKS).setFlammability(defaultNormalLogBurnValue, defaultNormalLogBurnSpread);
		this.strippedLog = new BlockEntry("stripped_" + name + logSuffix, createLog(insideMaterialColor)).setItemGroup(ItemGroup.BUILDING_BLOCKS).setFlammability(defaultNormalLogBurnValue, defaultNormalLogBurnSpread);
		this.wood = new BlockEntry(name + woodSuffix, createLog(barkMaterialColor)).setItemGroup(ItemGroup.BUILDING_BLOCKS).setFlammability(defaultNormalLogBurnValue, defaultNormalLogBurnSpread);
		this.strippedWood = new BlockEntry("stripped_" + name + woodSuffix, createLog(barkMaterialColor)).setItemGroup(ItemGroup.BUILDING_BLOCKS).setFlammability(defaultNormalLogBurnValue, defaultNormalLogBurnSpread);
		BlockCreatorHelper.addEntries(log, strippedLog, wood, strippedWood);
	}

	protected Block createLog(MaterialColor color) {
		FabricBlockSettings settings = isNether ? defaultNetherLogSettings : defaultNormalLogSettings;
		return new PillarBlock(settings.materialColor(color));
	}

	protected Block createLog(MaterialColor insideColor, MaterialColor barkColor) {
		FabricBlockSettings settings = isNether ? defaultNetherLogSettings : defaultNormalLogSettings;
		return new PillarBlock(AbstractBlock.Settings.of(isNether ? Material.NETHER_WOOD : Material.WOOD, (blockState) -> {
			return blockState.get(PillarBlock.AXIS) == Direction.Axis.Y ? insideColor : barkColor;
		}).strength(2.0F).sounds(isNether ? BlockSoundGroup.NETHER_STEM : BlockSoundGroup.WOOD));
	}

	public Block getLog() {
		return log.getBlock();
	}

	public Block getStrippedLog() {
		return strippedLog.getBlock();
	}

	public Block getWood() {
		return wood.getBlock();
	}

	public Block getStrippedWood() {
		return strippedWood.getBlock();
	}
}
