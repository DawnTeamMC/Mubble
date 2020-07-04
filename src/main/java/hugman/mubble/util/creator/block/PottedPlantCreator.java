package hugman.mubble.util.creator.block;

import hugman.mubble.util.creator.BlockCreatorHelper;
import hugman.mubble.util.creator.BlockEntry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.Material;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemGroup;

public class PottedPlantCreator {
	private final BlockEntry plant;
	private final BlockEntry pottedPlant;

	public static final FabricBlockSettings defaultPottedPlantSettings = FabricBlockSettings.of(Material.SUPPORTED).breakInstantly().nonOpaque();

	protected PottedPlantCreator(String name, Block plantBlock) {
		this.plant = new BlockEntry(name, plantBlock).setItemGroup(ItemGroup.DECORATIONS).setRenderLayer(RenderLayer.getCutout());
		this.pottedPlant = new BlockEntry("potted_" + name, new FlowerPotBlock(getPlant(), defaultPottedPlantSettings.lightLevel(plantBlock.getDefaultState().getLuminance()))).setRenderLayer(RenderLayer.getCutout());
		BlockCreatorHelper.addEntries(this.plant, this.pottedPlant);
	}

	public Block getPlant() {
		return this.plant.getBlock();
	}

	public Block getPottedPlant() {
		return this.pottedPlant.getBlock();
	}
}
