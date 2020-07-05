package hugman.mubble.util.creator.block;

import hugman.mubble.object.block.SaplingBlock;
import hugman.mubble.util.creator.BlockTemplate;
import net.minecraft.block.Block;
import net.minecraft.block.sapling.SaplingGenerator;

public class SaplingCreator extends PottedPlantCreator {
	public SaplingCreator(String name, SaplingGenerator generator) {
		super(name + "_sapling", new SaplingBlock(generator, BlockTemplate.saplingSettings));
	}

	public Block getSapling() {
		return getPlant();
	}
}
