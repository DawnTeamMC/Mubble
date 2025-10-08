package fr.hugman.mubble.client;

import fr.hugman.mubble.block.MubbleBlocks;
import fr.hugman.mubble.client.gui.screen.BumpableScreen;
import fr.hugman.mubble.client.render.MubbleRenderLayers;
import fr.hugman.mubble.client.render.MubbleRenderers;
import fr.hugman.mubble.screen.MubbleScreenHandlerTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.BlockRenderLayer;

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

    private static void registerBlockRenderLayers() {
        BlockRenderLayerMap.putBlock(MubbleBlocks.RED_BEEP_BLOCK, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(MubbleBlocks.BLUE_BEEP_BLOCK, BlockRenderLayer.CUTOUT);
    }

    private static void registerHandledScreens() {
        HandledScreens.register(MubbleScreenHandlerTypes.BUMPABLE_BLOCK, BumpableScreen::new);
    }
}
