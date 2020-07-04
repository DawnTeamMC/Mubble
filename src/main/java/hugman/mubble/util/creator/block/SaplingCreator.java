package hugman.mubble.util.creator.block;

import hugman.mubble.object.block.SaplingBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.sound.BlockSoundGroup;

public class SaplingCreator extends PottedPlantCreator {
	private static final FabricBlockSettings defaultSaplingSettings = FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.GRASS);

	public SaplingCreator(String name, SaplingGenerator generator) {
		super(name + "_sapling", new SaplingBlock(generator, defaultSaplingSettings));
	}

	public Block getSapling() {
		return getPlant();
	}
}
