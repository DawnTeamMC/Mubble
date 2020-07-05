package hugman.mubble.util.creator.block;

import net.minecraft.block.Block;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.sapling.SaplingGenerator;

public class NormalWoodTypeCreator extends WoodTypeCreator {
	private final SaplingCreator saplingCreator;
	private final LeavesCreator leavesCreator;

	public NormalWoodTypeCreator(String name, SaplingGenerator saplingGenerator, MaterialColor planksColor, MaterialColor barkColor) {
		this(name, saplingGenerator, planksColor, planksColor, barkColor);
	}

	public NormalWoodTypeCreator(String name, SaplingGenerator saplingGenerator, MaterialColor planksColor, MaterialColor insideColor, MaterialColor barkColor) {
		super(name, planksColor, insideColor, barkColor, false);
		this.saplingCreator = new SaplingCreator(name, saplingGenerator);
		this.leavesCreator = new LeavesCreator(name);
	}

	public Block getSapling()
	{
		return saplingCreator.getSapling();
	}

	public Block getPottedSapling()
	{
		return saplingCreator.getPottedPlant();
	}

	public Block getLeaves()
	{
		return leavesCreator.getLeaves();
	}

	public Block getLeafPile()
	{
		return leavesCreator.getLeafPile();
	}
}
