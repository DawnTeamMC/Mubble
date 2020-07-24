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

	public PottedPlantCreator(BlockEntry entry) {
		this.plantEntry = entry;
		this.pottedPlantEntry = new BlockEntry.Builder("potted_" + entry.getName(), new FlowerPotBlock(getPlant(), BlockTemplate.POTTED_PLANT_SETTINGS.lightLevel(entry.getBlock().getDefaultState().getLuminance()))).setRenderLayer(RenderLayer.getCutout()).noItem().build();
	}

	public Block getPlant() {
		return this.plantEntry.getBlock();
	}

	public Block getPottedPlant() {
		return this.pottedPlantEntry.getBlock();
	}
}