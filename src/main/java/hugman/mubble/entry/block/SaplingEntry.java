package hugman.mubble.entry.block;

import hugman.mubble.object.block.SaplingBlock;
import hugman.mubble.util.creator.BlockCreatorUtil;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;

public class SaplingEntry extends PottedPlantEntry {
	public SaplingEntry(String name, SaplingGenerator generator) {
		super(BlockCreatorUtil.registerBlock(
				new SaplingBlock(generator, FabricBlockSettings
						.of(Material.PLANT)
						.noCollision()
						.ticksRandomly()
						.breakInstantly()
						.sounds(BlockSoundGroup.GRASS)), name + "_sapling", ItemGroup.DECORATIONS));
	}
}
