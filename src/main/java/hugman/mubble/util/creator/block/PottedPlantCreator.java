package hugman.mubble.util.creator.block;

import hugman.mubble.util.creator.BlockEntry;
import hugman.mubble.util.creator.BlockTemplate;
import net.minecraft.block.Block;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemGroup;

public class PottedPlantCreator {
	private final BlockEntry plantEntry;
	private final BlockEntry pottedPlantEntry;

	protected PottedPlantCreator(String name, Block plantBlock) {
		this.plantEntry = new BlockEntry.Builder(name, plantBlock).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout()).build();
		this.pottedPlantEntry = new BlockEntry.Builder("potted_" + name, new FlowerPotBlock(getPlant(), BlockTemplate.pottedPlantSettings.lightLevel(plantBlock.getDefaultState().getLuminance()))).setRenderLayer(RenderLayer.getCutout()).noItem().build();
	}

	protected Block getPlant() {
		return this.plantEntry.getBlock();
	}

	public Block getPottedPlant() {
		return this.pottedPlantEntry.getBlock();
	}
}
