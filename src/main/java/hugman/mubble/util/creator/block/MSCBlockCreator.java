package hugman.mubble.util.creator.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import hugman.mubble.util.MoreWordUtils;
import hugman.mubble.util.creator.BlockCreatorHelper;
import hugman.mubble.util.creator.BlockEntry;
import hugman.mubble.util.creator.BlockTemplate;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.util.DyeColor;

import java.util.Map;

public class MSCBlockCreator {
	private final Map<Pair<BlockTemplate, DyeColor>, BlockEntry> BLOCK_MAP;

	public MSCBlockCreator(String name, FabricBlockSettings settings, Block baseBlock, BlockTemplate... templates) {
		ImmutableMap.Builder builder = new ImmutableMap.Builder<DyeColor, Block>();
		for(BlockTemplate template : templates) {
			for(DyeColor color : DyeColor.values()) {
				BlockEntry entry = new BlockEntry(color.getName() + "_" + MoreWordUtils.parseShapeName(name, template), template, settings.materialColor(color.getMaterialColor())).setFlammability(baseBlock);
				builder.put(Pair.of(template, color), entry);
				BlockCreatorHelper.addEntries(entry);
			}
		}
		BLOCK_MAP = builder.build();
	}

	public MSCBlockCreator(String name, Block baseBlock, BlockTemplate... shapes) {
		this(name, FabricBlockSettings.copyOf(baseBlock), baseBlock, shapes);
	}

	public Block getBlock(BlockTemplate shape, DyeColor color) {
		return BLOCK_MAP.get(Pair.of(shape, color)).getBlock();
	}
}
