package fr.hugman.mubble;

import fr.hugman.mubble.client.render.BumpedBlockEntityRenderer;
import fr.hugman.mubble.registry.SuperMarioContent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class MubbleClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockEntityRendererRegistry.register(SuperMarioContent.BUMPED_BLOCK_ENTITY_TYPE, BumpedBlockEntityRenderer::new);
	}
}
