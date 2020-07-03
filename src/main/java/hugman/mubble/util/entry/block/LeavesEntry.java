package hugman.mubble.util.entry.block;

import hugman.mubble.objects.block.PileBlock;
import hugman.mubble.objects.block.SaplingBlock;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class LeavesEntry extends BlockEntry {
	private final Block leaves;
	private final Block leafPile;

	public LeavesEntry(String name) {
		leaves = registerBlock(new LeavesBlock(FabricBlockSettings
				.of(Material.LEAVES)
				.strength(0.2F)
				.ticksRandomly()
				.sounds(BlockSoundGroup.GRASS)
				.nonOpaque()
				.allowsSpawning(BlockEntry::canSpawnOnLeaves)
				.suffocates(BlockEntry::never)
				.blockVision(BlockEntry::never)), name + "_leaves");
		leafPile = registerBlock(new PileBlock(FabricBlockSettings
				.of(Material.LEAVES)
				.strength(0.1F)
				.ticksRandomly()
				.sounds(BlockSoundGroup.GRASS)
				.noCollision()
				.nonOpaque()
				.suffocates(BlockEntry::never)
				.blockVision(BlockEntry::never)), name + "_leaf_pile");
		BlockRenderLayerMap.INSTANCE.putBlock(this.getLeaves(), RenderLayer.getCutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(this.getLeafPile(), RenderLayer.getCutoutMipped());
		registerBlockItem(this.getLeaves(), ItemGroup.DECORATIONS);
		registerBlockItem(this.getLeafPile(), ItemGroup.DECORATIONS);
		setFlammability(this.getLeaves(), 30, 60);
		setFlammability(this.getLeafPile(), 30, 60);
	}

	public Block getLeaves() {
		return leaves;
	}

	public Block getLeafPile() {
		return leafPile;
	}
}
