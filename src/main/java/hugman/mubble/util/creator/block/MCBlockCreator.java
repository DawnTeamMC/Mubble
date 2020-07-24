package hugman.mubble.util.creator.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import hugman.mubble.util.creator.BlockEntry;
import hugman.mubble.util.creator.BlockTemplate;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.util.DyeColor;

import java.util.Map;

public class MCBlockCreator {
	private final Map<Pair<BlockTemplate, DyeColor>, BlockEntry> BLOCK_MAP;

	public MCBlockCreator(BlockTemplate template, FabricBlockSettings settings) {
		ImmutableMap.Builder builder = new ImmutableMap.Builder<DyeColor, Block>();
		for(DyeColor color : DyeColor.values()) {
			BlockEntry entry = new BlockEntry.Builder(color.getName(), template, settings.materialColor(color.getMaterialColor())).copy(template).build();
			builder.put(Pair.of(template, color), entry);
		}
		BLOCK_MAP = builder.build();
	}

	public Block getBlock(BlockTemplate shape, DyeColor color) {
		return BLOCK_MAP.get(Pair.of(shape, color)).getBlock();
	}
}
