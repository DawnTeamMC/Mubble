package fr.hugman.mubble.super_mario.client;

import fr.hugman.mubble.super_mario.client.gui.screens.inventory.BumpableScreen;
import fr.hugman.mubble.super_mario.client.model.SuperMarioModelLayers;
import fr.hugman.mubble.super_mario.client.particle.SuperMarioParticleResources;
import fr.hugman.mubble.super_mario.client.renderer.SuperMarioRenderPipelines;
import fr.hugman.mubble.super_mario.client.renderer.SuperMarioRenderTypes;
import fr.hugman.mubble.super_mario.client.renderer.SuperMarioRenderers;
import com.google.common.reflect.Reflection;
import fr.hugman.mubble.super_mario.world.inventory.SuperMarioMenuTypes;
import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ChunkSectionLayerMap;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

@Environment(EnvType.CLIENT)
public class SuperMarioClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Reflection.initialize(SuperMarioModelLayers.class);
        Reflection.initialize(SuperMarioRenderPipelines.class);
        Reflection.initialize(SuperMarioRenderTypes.class);

        registerBlockRenderLayers();
        registerHandledScreens();
        SuperMarioRenderers.registerEntities();
        SuperMarioRenderers.registerBlockEntities();
        SuperMarioParticleResources.register();
    }

    private static void registerBlockRenderLayers() {
        ChunkSectionLayerMap.putBlock(SuperMarioBlocks.RED_BEEP_BLOCK, ChunkSectionLayer.CUTOUT);
        ChunkSectionLayerMap.putBlock(SuperMarioBlocks.BLUE_BEEP_BLOCK, ChunkSectionLayer.CUTOUT);
    }

    private static void registerHandledScreens() {
        MenuScreens.register(SuperMarioMenuTypes.BUMPABLE_BLOCK, BumpableScreen::new);
    }
}
