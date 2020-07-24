package hugman.mubble.util.entry.pack;

import com.google.common.collect.ImmutableMap;
import hugman.mubble.util.MoreWordUtils;
import hugman.mubble.util.entry.BlockEntry;
import hugman.mubble.util.entry.BlockTemplate;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MaterialColor;

import java.util.Map;

public class MSBlockEntryPack {
	private final Map<BlockTemplate, Block> blocks;

	public MSBlockEntryPack(String name, MaterialColor color, Block baseBlock, BlockTemplate... templates) {
		ImmutableMap.Builder builder = new ImmutableMap.Builder<BlockTemplate, Block>();
		for(BlockTemplate template : templates) {
			Block block = new BlockEntry.Builder(MoreWordUtils.parseShapeName(name, template), template, FabricBlockSettings.copyOf(baseBlock).materialColor(color)).copy(baseBlock).copy(template).build();
			builder.put(template, block);
		}
		blocks = builder.build();
	}

	public MSBlockEntryPack(String name, Block baseBlock, BlockTemplate... templates) {
		this(name, baseBlock.getDefaultMaterialColor(), baseBlock, templates);
	}

	public Block getBlock(BlockTemplate shape) {
		return blocks.get(shape);
	}
}
