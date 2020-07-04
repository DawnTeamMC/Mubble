package hugman.mubble.entry.block;

import hugman.mubble.util.creator.BlockCreatorUtil;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.Material;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.registry.Registry;

public class PottedPlantEntry {
	private final Block plant;
	private final Block pottedPlant;

	protected PottedPlantEntry(Block plant) {
		this.plant = plant;
		pottedPlant = BlockCreatorUtil.registerBlock(new FlowerPotBlock(getPlant(), FabricBlockSettings
				.of(Material.SUPPORTED)
				.breakInstantly()
				.nonOpaque()
				.lightLevel(plant.getDefaultState().getLuminance())), "potted_" + Registry.BLOCK.getId(getPlant()).getPath());
		BlockRenderLayerMap.INSTANCE.putBlock(pottedPlant, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(this.getPlant(), RenderLayer.getCutout());
	}

	public Block getPlant() {
		return plant;
	}

	public Block getPottedPlant() {
		return pottedPlant;
	}
}
