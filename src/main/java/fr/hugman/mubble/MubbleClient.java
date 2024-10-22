package fr.hugman.mubble;

import fr.hugman.mubble.block.MubbleBlocks;
import fr.hugman.mubble.client.gui.screen.BumpableScreen;
import fr.hugman.mubble.client.keybind.MubbleKeyBindings;
import fr.hugman.mubble.client.render.MubbleRenderLayers;
import fr.hugman.mubble.client.render.MubbleRenderers;
import fr.hugman.mubble.client.texture.MubbleSpriteManagers;
import fr.hugman.mubble.network.MubbleClientReceivers;
import fr.hugman.mubble.screen.MubbleScreenHandlerTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class MubbleClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STOPPING.register(MubbleClient::onClientStop);

        registerBlockRenderLayers();
        registerHandledScreens();
        MubbleRenderers.registerEntities();
        MubbleRenderers.registerBlockEntities();
        MubbleRenderLayers.registerLayers();
        MubbleKeyBindings.registerEvents();
        MubbleClientReceivers.register();
    }

    private static void registerBlockRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(MubbleBlocks.RED_BEEP_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(MubbleBlocks.BLUE_BEEP_BLOCK, RenderLayer.getCutout());
    }

    private static void registerHandledScreens() {
        HandledScreens.register(MubbleScreenHandlerTypes.BUMPABLE_BLOCK, BumpableScreen::new);
    }

    private static void onClientStop(MinecraftClient client) {
        MubbleSpriteManagers.stopSpriteManagers();
    }
}
