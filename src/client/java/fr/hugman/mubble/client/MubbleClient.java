package fr.hugman.mubble.client;

import com.google.common.reflect.Reflection;
import fr.hugman.mubble.client.gui.screens.inventory.BumpableScreen;
import fr.hugman.mubble.client.keybind.MubbleKeyBindings;
import fr.hugman.mubble.client.renderer.MubbleRenderers;
import fr.hugman.mubble.client.model.MubbleModelLayers;
import fr.hugman.mubble.client.network.MubbleClientReceivers;
import fr.hugman.mubble.world.inventory.MubbleMenuTypes;
import fr.hugman.mubble.world.level.block.MubbleBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

@Environment(EnvType.CLIENT)
public class MubbleClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Reflection.initialize(MubbleModelLayers.class);

        registerBlockRenderLayers();
        registerHandledScreens();
        MubbleRenderers.registerEntities();
        MubbleRenderers.registerBlockEntities();
        MubbleKeyBindings.registerEvents();
        MubbleClientReceivers.register();
    }

    private static void registerBlockRenderLayers() {
        BlockRenderLayerMap.putBlock(MubbleBlocks.RED_BEEP_BLOCK, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(MubbleBlocks.BLUE_BEEP_BLOCK, ChunkSectionLayer.CUTOUT);
    }

    private static void registerHandledScreens() {
        MenuScreens.register(MubbleMenuTypes.BUMPABLE_BLOCK, BumpableScreen::new);
    }
}
