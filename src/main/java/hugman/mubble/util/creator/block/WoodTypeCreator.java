package hugman.mubble.util.creator.block;

import hugman.mubble.util.creator.BlockTemplate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MaterialColor;

public class WoodTypeCreator {
	private final BlockCreator planksCreator;
	private final LogCreator logCreator;
	private final MSBlockCreator planksBlocksCreator;
	private final MSBlockCreator woodBlocksCreator;

	public WoodTypeCreator(String name, MaterialColor planksColor, MaterialColor insideColor, MaterialColor barkColor, boolean isNether) {
		this.planksCreator = new BlockCreator(name, BlockTemplate.PLANKS, isNether ? Blocks.CRIMSON_PLANKS : Blocks.OAK_PLANKS, planksColor);
		this.logCreator = new LogCreator(name, insideColor, barkColor, isNether);
		this.planksBlocksCreator = new MSBlockCreator(name, this.planksCreator.getBlock(),
				BlockTemplate.STAIRS,
				BlockTemplate.SLAB,
				BlockTemplate.VERTICAL_SLAB,
				BlockTemplate.TRAPDOOR,
				BlockTemplate.WOOD_PRESSURE_PLATE,
				BlockTemplate.WOOD_BUTTON,
				BlockTemplate.FENCE,
				BlockTemplate.FENCE_GATE,
				BlockTemplate.DOOR);
		this.woodBlocksCreator = new MSBlockCreator(name + logCreator.getWoodSuffix(), logCreator.getWood(),
				BlockTemplate.STAIRS,
				BlockTemplate.SLAB,
				BlockTemplate.VERTICAL_SLAB,
				BlockTemplate.WOOD_BUTTON);
	}

	public Block getPlanks() {
		return planksCreator.getBlock();
	}

	public Block getLog() {
		return logCreator.getLog();
	}

	public Block getStrippedLog() {
		return logCreator.getStrippedLog();
	}

	public Block getWood() {
		return logCreator.getWood();
	}

	public Block getStrippedWood() {
		return logCreator.getStrippedWood();
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
