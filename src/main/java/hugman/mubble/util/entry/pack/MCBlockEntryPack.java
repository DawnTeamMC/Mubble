package hugman.mubble.util.entry.pack;

import com.google.common.collect.ImmutableMap;
import hugman.mubble.Mubble;
import hugman.mubble.util.entry.BlockEntry;
import hugman.mubble.util.entry.BlockTemplate;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.util.DyeColor;

import java.util.Map;

public class MCBlockEntryPack {
	private final Map<DyeColor, Block> BLOCK_MAP;

	/**
	 * Creates an entry pack containing blocks of 16 different colors.
	 * @param template The template to use.
	 * @param settings The settings.
	 */
	public MCBlockEntryPack(BlockTemplate template, FabricBlockSettings settings) {
		ImmutableMap.Builder mapBuilder = new ImmutableMap.Builder<DyeColor, Block>();
		for(DyeColor color : DyeColor.values()) {
			Block entry = new BlockEntry.Builder(color.getName(), template, settings.materialColor(color.getMaterialColor())).copy(template).build();
			mapBuilder.put(color, entry);
		}
		BLOCK_MAP = mapBuilder.build();
	}

	public Block getBlock(DyeColor color) {
		return BLOCK_MAP.get(color);
	}
}
