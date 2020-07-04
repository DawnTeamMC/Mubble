package hugman.mubble.util.creator.block;

import com.google.common.collect.ImmutableMap;
import hugman.mubble.util.MoreWordUtils;
import hugman.mubble.util.creator.BlockCreatorHelper;
import hugman.mubble.util.creator.BlockEntry;
import hugman.mubble.util.creator.BlockTemplate;
import net.minecraft.block.Block;
import net.minecraft.block.MaterialColor;

import java.util.Map;

public class MSBlockCreator {
	private final Map<BlockTemplate, BlockEntry> SHAPE_MAP;

	public MSBlockCreator(String name, MaterialColor color, Block baseBlock, BlockTemplate... templates) {
		ImmutableMap.Builder builder = new ImmutableMap.Builder<BlockTemplate, Block>();
		for(BlockTemplate template : templates) {
			BlockEntry entry = new BlockEntry(MoreWordUtils.parseShapeName(name, template), template, baseBlock, color);
			builder.put(template, entry);
			BlockCreatorHelper.addEntries(entry);
		}
		SHAPE_MAP = builder.build();
	}

	public MSBlockCreator(String name, Block baseBlock, BlockTemplate... templates) {
		this(name, baseBlock.getDefaultMaterialColor(), baseBlock, templates);
	}

	public Block getBlock(BlockTemplate shape) {
		return SHAPE_MAP.get(shape).getBlock();
	}
}
