package hugman.mubble.util.creator.block;

import com.google.common.collect.ImmutableMap;
import hugman.mubble.util.MoreWordUtils;
import hugman.mubble.util.creator.BlockEntry;
import hugman.mubble.util.creator.BlockTemplate;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MaterialColor;

import java.util.Map;

public class MSBlockCreator {
	private final Map<BlockTemplate, BlockEntry> blocks;

	public MSBlockCreator(String name, MaterialColor color, Block baseBlock, BlockTemplate... templates) {
		ImmutableMap.Builder builder = new ImmutableMap.Builder<BlockTemplate, Block>();
		for(BlockTemplate template : templates) {
			BlockEntry entry = new BlockEntry.Builder(MoreWordUtils.parseShapeName(name, template), template, FabricBlockSettings.copyOf(baseBlock).materialColor(color)).copy(baseBlock).copy(template).build();
			builder.put(template, entry);
		}
		blocks = builder.build();
	}

	public MSBlockCreator(String name, Block baseBlock, BlockTemplate... templates) {
		this(name, baseBlock.getDefaultMaterialColor(), baseBlock, templates);
	}

	public Block getBlock(BlockTemplate shape) {
		return blocks.get(shape).getBlock();
	}
}
