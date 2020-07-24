package hugman.mubble.util.entry.pack;

import hugman.mubble.util.entry.BlockEntry;
import hugman.mubble.util.entry.BlockTemplate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MaterialColor;

public class WoodTypeEntryPack {
	private final Block planks;
	private final LogsEntryPack logsEntryPack;
	private final MSBlockEntryPack planksBlocksCreator;
	private final MSBlockEntryPack woodBlocksCreator;

	public WoodTypeEntryPack(String name, MaterialColor planksColor, MaterialColor insideColor, MaterialColor barkColor, boolean isNether) {
		this.planks = new BlockEntry.Builder(name, BlockTemplate.PLANKS, isNether ? Blocks.CRIMSON_PLANKS : Blocks.OAK_PLANKS, planksColor).build();
		this.logsEntryPack = new LogsEntryPack(name, insideColor, barkColor, isNether);
		this.planksBlocksCreator = new MSBlockEntryPack(name, this.planks,
				BlockTemplate.STAIRS,
				BlockTemplate.SLAB,
				BlockTemplate.VERTICAL_SLAB,
				BlockTemplate.TRAPDOOR,
				BlockTemplate.WOOD_PRESSURE_PLATE,
				BlockTemplate.WOOD_BUTTON,
				BlockTemplate.FENCE,
				BlockTemplate.FENCE_GATE,
				BlockTemplate.DOOR);
		this.woodBlocksCreator = new MSBlockEntryPack(name + logsEntryPack.getWoodSuffix(), logsEntryPack.getWood(),
				BlockTemplate.STAIRS,
				BlockTemplate.SLAB,
				BlockTemplate.VERTICAL_SLAB,
				BlockTemplate.WOOD_BUTTON);
	}

	public Block getPlanks() {
		return planks;
	}

	public Block getLog() {
		return logsEntryPack.getLog();
	}

	public Block getStrippedLog() {
		return logsEntryPack.getStrippedLog();
	}

	public Block getWood() {
		return logsEntryPack.getWood();
	}

	public Block getStrippedWood() {
		return logsEntryPack.getStrippedWood();
	}

	public Block getWoodStairs() {
		return woodBlocksCreator.getBlock(BlockTemplate.STAIRS);
	}

	public Block getWoodSlab() {
		return woodBlocksCreator.getBlock(BlockTemplate.SLAB);
	}

	public Block getWoodVerticalSlab() {
		return woodBlocksCreator.getBlock(BlockTemplate.VERTICAL_SLAB);
	}

	public Block getWoodButton() {
		return woodBlocksCreator.getBlock(BlockTemplate.WOOD_BUTTON);
	}

	public Block getPressurePlate() {
		return planksBlocksCreator.getBlock(BlockTemplate.WOOD_PRESSURE_PLATE);
	}

	public Block getTrapdoor() {
		return planksBlocksCreator.getBlock(BlockTemplate.TRAPDOOR);
	}

	public Block getButton() {
		return planksBlocksCreator.getBlock(BlockTemplate.WOOD_BUTTON);
	}

	public Block getStairs() {
		return planksBlocksCreator.getBlock(BlockTemplate.STAIRS);
	}

	public Block getSlab() {
		return planksBlocksCreator.getBlock(BlockTemplate.SLAB);
	}

	public Block getVerticalSlab() {
		return planksBlocksCreator.getBlock(BlockTemplate.VERTICAL_SLAB);
	}

	public Block getFenceGate() {
		return planksBlocksCreator.getBlock(BlockTemplate.FENCE_GATE);
	}

	public Block getFence() {
		return planksBlocksCreator.getBlock(BlockTemplate.FENCE);
	}

	public Block getDoor() {
		return planksBlocksCreator.getBlock(BlockTemplate.DOOR);
	}
}
