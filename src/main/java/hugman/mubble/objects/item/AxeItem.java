package hugman.mubble.objects.item;

import com.google.common.collect.ImmutableMap.Builder;
import hugman.mubble.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;

import java.util.Map;

public class AxeItem extends net.minecraft.item.AxeItem {
	public static final Map<Block, Block> BLOCK_STRIPPING_MAP;

	/* Extension for internal publicity */
	public AxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
		super(material, attackDamage, attackSpeed, settings);
	}

	static {
		BLOCK_STRIPPING_MAP = (new Builder<Block, Block>())
				.putAll(STRIPPED_BLOCKS)
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
