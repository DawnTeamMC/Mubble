package hugman.mubble.entry.block;

import hugman.mubble.object.block.PileBlock;
import hugman.mubble.util.creator.BlockCreatorUtil;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;

public class LeavesEntry {
	private final Block leaves;
	private final Block leafPile;

	public LeavesEntry(String name) {
		leaves = BlockCreatorUtil.registerBlock(new LeavesBlock(FabricBlockSettings
				.of(Material.LEAVES)
				.strength(0.2F)
				.ticksRandomly()
				.sounds(BlockSoundGroup.GRASS)
				.nonOpaque()
				.allowsSpawning(BlockCreatorUtil::canSpawnOnLeaves)
				.suffocates(BlockCreatorUtil::never)
				.blockVision(BlockCreatorUtil::never)), name + "_leaves");
		leafPile = BlockCreatorUtil.registerBlock(new PileBlock(FabricBlockSettings
				.of(Material.LEAVES)
				.strength(0.1F)
				.ticksRandomly()
				.sounds(BlockSoundGroup.GRASS)
				.noCollision()
				.nonOpaque()
				.suffocates(BlockCreatorUtil::never)
				.blockVision(BlockCreatorUtil::never)), name + "_leaf_pile");
		BlockRenderLayerMap.INSTANCE.putBlock(this.getLeaves(), RenderLayer.getCutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(this.getLeafPile(), RenderLayer.getCutoutMipped());
		BlockCreatorUtil.registerBlockItem(this.getLeaves(), ItemGroup.DECORATIONS);
		BlockCreatorUtil.registerBlockItem(this.getLeafPile(), ItemGroup.DECORATIONS);
		BlockCreatorUtil.setFlammability(this.getLeaves(), 30, 60);
		BlockCreatorUtil.setFlammability(this.getLeafPile(), 30, 60);
	}

	public Block getLeaves() {
		return leaves;
	}

	public Block getLeafPile() {
		return leafPile;
	}
}
