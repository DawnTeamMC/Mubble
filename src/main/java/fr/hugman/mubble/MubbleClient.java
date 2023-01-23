package fr.hugman.mubble;

import fr.hugman.mubble.client.render.BumpableBlockEntityRenderer;
import fr.hugman.mubble.registry.SuperMario;
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
		BlockRenderLayerMap.INSTANCE.putBlock(SuperMario.RED_BEEP_BLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SuperMario.BLUE_BEEP_BLOCK, RenderLayer.getCutout());

		BlockEntityRendererRegistry.register(SuperMario.BUMPABLE_BLOCK_ENTITY_TYPE, BumpableBlockEntityRenderer::new);
	}
}
