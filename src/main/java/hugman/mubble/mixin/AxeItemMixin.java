package hugman.mubble.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.ImmutableMap.Builder;

import hugman.mubble.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.AxeItem;

@Mixin(AxeItem.class)
public class AxeItemMixin
{
	@Shadow protected static final Map<Block, Block> STRIPPED_BLOCKS;
	
	static
	{
		STRIPPED_BLOCKS = (new Builder<Block, Block>())
				.put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD)
				.put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG)
				.put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD)
				.put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG)
				.put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD)
				.put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG)
				.put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD)
				.put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG)
				.put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD)
				.put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG)
				.put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD)
				.put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG)
				.put(MubbleBlocks.PALM_LOG, MubbleBlocks.STRIPPED_PALM_LOG)
				.put(MubbleBlocks.PALM_WOOD, MubbleBlocks.STRIPPED_PALM_WOOD)
				.put(MubbleBlocks.SCARLET_LOG, MubbleBlocks.STRIPPED_SCARLET_LOG)
				.put(MubbleBlocks.SCARLET_WOOD, MubbleBlocks.STRIPPED_SCARLET_WOOD)
				.put(MubbleBlocks.CHERRY_OAK_LOG, MubbleBlocks.STRIPPED_CHERRY_OAK_LOG)
				.put(MubbleBlocks.CHERRY_OAK_WOOD, MubbleBlocks.STRIPPED_CHERRY_OAK_WOOD)
				.put(MubbleBlocks.PRESS_GARDEN_LOG, MubbleBlocks.STRIPPED_PRESS_GARDEN_LOG)
				.put(MubbleBlocks.PRESS_GARDEN_WOOD, MubbleBlocks.STRIPPED_PRESS_GARDEN_WOOD)
				.build();
	}
}
