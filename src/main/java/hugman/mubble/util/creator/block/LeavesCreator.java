package hugman.mubble.util.creator.block;

import hugman.mubble.util.creator.BlockCreatorHelper;
import hugman.mubble.util.creator.BlockEntry;
import hugman.mubble.util.creator.BlockTemplate;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class LeavesCreator {
	private final BlockEntry leaves;
	private final BlockEntry leafPile;

	public static final FabricBlockSettings defaultLeavesSettings = FabricBlockSettings.of(Material.LEAVES).strength(0.2F).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().allowsSpawning(BlockCreatorHelper::canSpawnOnLeaves).suffocates(BlockCreatorHelper::never).blockVision(BlockCreatorHelper::never);
	public static final FabricBlockSettings defaultLeafPileSettings = FabricBlockSettings.of(Material.LEAVES).strength(0.1F).ticksRandomly().sounds(BlockSoundGroup.GRASS).noCollision().nonOpaque().suffocates(BlockCreatorHelper::never).blockVision(BlockCreatorHelper::never);
	public static final int defaultLeavesBurnValue = 60;
	public static final int defaultLeavesBurnSpread = 30;

	public LeavesCreator(String name) {
		this.leaves = new BlockEntry(name, BlockTemplate.LEAVES, defaultLeavesSettings).setFlammability(defaultLeavesBurnValue, defaultLeavesBurnSpread);
		this.leafPile = new BlockEntry(name, BlockTemplate.LEAF_PILE, defaultLeafPileSettings).setFlammability(defaultLeavesBurnValue, defaultLeavesBurnSpread);
		BlockCreatorHelper.addEntries(this.leaves, this.leafPile);
	}

	public Block getLeaves() {
		return leaves.getBlock();
	}

	public Block getLeafPile() {
		return leafPile.getBlock();
	}
}
