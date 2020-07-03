package hugman.mubble.util.entry.block;

import hugman.mubble.objects.block.SaplingBlock;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;

public class SaplingEntry extends BlockEntry {
	private final Block sapling;
	private final Block pottedPlant;

	public SaplingEntry(String name, SaplingGenerator generator) {
		sapling = registerBlock(new SaplingBlock(generator, FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.GRASS)), name);
		BlockRenderLayerMap.INSTANCE.putBlock(this.getSapling(), RenderLayer.getCutout());
		registerBlockItem(this.getSapling(), ItemGroup.DECORATIONS);
		pottedPlant = createPottedPlant(this.getSapling());
	}

	public Block getSapling() {
		return sapling;
	}

	public Block getPottedPlant() {
		return pottedPlant;
	}
}
