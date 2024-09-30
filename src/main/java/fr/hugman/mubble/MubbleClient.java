package fr.hugman.mubble;

import fr.hugman.mubble.block.MubbleBlocks;
import fr.hugman.mubble.client.gui.screen.BumpableScreen;
import fr.hugman.mubble.client.render.MubbleRenderLayers;
import fr.hugman.mubble.client.render.MubbleRenderers;
import fr.hugman.mubble.client.render.entity.ShooterInkBulletRenderer;
import fr.hugman.mubble.entity.MubbleEntityTypes;
import fr.hugman.mubble.screen.MubbleScreenHandlerTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class MubbleClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerBlockRenderLayers();
        registerHandledScreens();
        MubbleRenderers.registerEntities();
        MubbleRenderers.registerBlockEntities();
        MubbleRenderLayers.registerLayers();
    }

    //TODO: move to MubbleRenderers
    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(MubbleEntityTypes.SHOOTER_INK_BULLET, ShooterInkBulletRenderer::new);
    }

    private static void registerBlockRenderLayers() {
        var map = BlockRenderLayerMap.INSTANCE;
        var cutout = RenderLayer.getCutout();

        map.putBlock(MubbleBlocks.RED_BEEP_BLOCK, cutout);
        map.putBlock(MubbleBlocks.BLUE_BEEP_BLOCK, cutout);
    }

    private static void registerHandledScreens() {
        HandledScreens.register(MubbleScreenHandlerTypes.BUMPABLE_BLOCK, BumpableScreen::new);
    }
}
