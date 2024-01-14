package fr.hugman.mubble;

import fr.hugman.mubble.client.gui.screen.BumpableBlockScreen;
import fr.hugman.mubble.client.render.BumpableBlockEntityRenderer;
import fr.hugman.mubble.entity.BeanstalkEntityModel;
import fr.hugman.mubble.entity.BeanstalkEntityRenderer;
import fr.hugman.mubble.registry.SuperMario;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MubbleClient implements ClientModInitializer {
	public static final EntityModelLayer MODEL_CUBE_LAYER = new EntityModelLayer(
			new Identifier("mubble", "beanstalk"), "main"
	);

	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(SuperMario.RED_BEEP_BLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SuperMario.BLUE_BEEP_BLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(SuperMario.BEANSTALK, RenderLayer.getCutout());

		HandledScreens.register(SuperMario.BUMPABLE_BLOCK_SCREEN_HANDLER, BumpableBlockScreen::new);
		BlockEntityRendererFactories.register(SuperMario.BUMPABLE_BLOCK_ENTITY_TYPE, BumpableBlockEntityRenderer::new);

		EntityRendererRegistry.register(SuperMario.BEANSTALK_ENTITY, BeanstalkEntityRenderer::new);
		// In 1.17, use EntityRendererRegistry.register (seen below) instead of EntityRendererRegistry.INSTANCE.register (seen above)
		EntityRendererRegistry.register(SuperMario.BEANSTALK_ENTITY, BeanstalkEntityRenderer::new);

		EntityModelLayerRegistry.registerModelLayer(MODEL_CUBE_LAYER, BeanstalkEntityModel::getTexturedModelData);
	}
}
