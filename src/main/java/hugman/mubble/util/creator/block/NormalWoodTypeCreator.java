package hugman.mubble.util.creator.block;

import hugman.mubble.object.block.SaplingBlock;
import hugman.mubble.util.creator.BlockEntry;
import hugman.mubble.util.creator.BlockTemplate;
import net.minecraft.block.Block;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.sapling.SaplingGenerator;

public class NormalWoodTypeCreator extends WoodTypeCreator {
	private final PottedPlantCreator saplingCreator;
	private final LeavesCreator leavesCreator;

	public NormalWoodTypeCreator(String name, SaplingGenerator saplingGenerator, MaterialColor planksColor, MaterialColor barkColor) {
		this(name, saplingGenerator, planksColor, planksColor, barkColor);
	}

	public NormalWoodTypeCreator(String name, SaplingGenerator saplingGenerator, MaterialColor planksColor, MaterialColor insideColor, MaterialColor barkColor) {
		super(name, planksColor, insideColor, barkColor, false);
		this.saplingCreator = new PottedPlantCreator(new BlockEntry.Builder(name + "_sapling", new SaplingBlock(saplingGenerator, BlockTemplate.SAPLING_SETTINGS)).build());
		this.leavesCreator = new LeavesCreator(name);
	}

	public Block getSapling() {
		return saplingCreator.getPlant();
	}

	public Block getPottedSapling() {
		return saplingCreator.getPottedPlant();
	}

	public Block getLeaves() {
		return leavesCreator.getLeaves();
	}

	public Block getLeafPile() {
		return leavesCreator.getLeafPile();
	}
}
