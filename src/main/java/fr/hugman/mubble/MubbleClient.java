package fr.hugman.mubble;

import fr.hugman.mubble.client.render.BumpedBlockEntityRenderer;
import fr.hugman.mubble.registry.SuperMarioContent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class MubbleClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(SuperMarioContent.RED_BEEP_BLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SuperMarioContent.BLUE_BEEP_BLOCK, RenderLayer.getCutout());

		BlockEntityRendererRegistry.register(SuperMarioContent.BUMPED_BLOCK_ENTITY_TYPE, BumpedBlockEntityRenderer::new);
	}
}
