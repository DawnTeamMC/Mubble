package fr.hugman.mubble;

import fr.hugman.mubble.client.gui.screen.BumpableScreen;
import fr.hugman.mubble.client.render.BumpableBlockEntityRenderer;
import fr.hugman.mubble.registry.SuperMario;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

@Environment(EnvType.CLIENT)
public class MubbleClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(SuperMario.RED_BEEP_BLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SuperMario.BLUE_BEEP_BLOCK, RenderLayer.getCutout());

		HandledScreens.register(SuperMario.BUMPABLE_BLOCK_SCREEN_HANDLER, BumpableScreen::new);
		BlockEntityRendererFactories.register(SuperMario.BUMPABLE_BLOCK_ENTITY_TYPE, BumpableBlockEntityRenderer::new);
	}
}
