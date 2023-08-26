package fr.hugman.mubble;

import fr.hugman.mubble.client.gui.screen.BumpableBlockScreen;
import fr.hugman.mubble.client.render.BumpableBlockEntityRenderer;
import fr.hugman.mubble.client.render.entity.ShooterInkBulletRenderer;
import fr.hugman.mubble.entity.projectile.ShooterInkBulletEntity;
import fr.hugman.mubble.registry.Splatoon;
import fr.hugman.mubble.registry.SuperMario;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

@Environment(EnvType.CLIENT)
public class MubbleClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		registerEntityRenderers();
		registerBlockEntitiesRenderers();
		registerScreens();
		registerBlockRenderLayers();
	}

	public static void registerEntityRenderers() {
		EntityRendererRegistry.register(Splatoon.SHOOTER_INK_BULLET, ShooterInkBulletRenderer::new);
	}

	public static void registerBlockEntitiesRenderers() {
		BlockEntityRendererFactories.register(SuperMario.BUMPABLE_BLOCK_ENTITY_TYPE, BumpableBlockEntityRenderer::new);
	}

	public static void registerScreens() {
		HandledScreens.register(SuperMario.BUMPABLE_BLOCK_SCREEN_HANDLER, BumpableBlockScreen::new);
	}

	public static void registerBlockRenderLayers() {
		var map = BlockRenderLayerMap.INSTANCE;
		var cutout = RenderLayer.getCutout();

		map.putBlock(SuperMario.RED_BEEP_BLOCK, cutout);
		map.putBlock(SuperMario.BLUE_BEEP_BLOCK, cutout);
	}
}
