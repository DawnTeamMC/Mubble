package fr.hugman.mubble;

import com.google.common.reflect.Reflection;
import fr.hugman.mubble.block.MubbleBlocks;
import fr.hugman.mubble.client.gui.screen.BumpableScreen;
import fr.hugman.mubble.client.render.MubbleRenderers;
import fr.hugman.mubble.client.render.entity.model.MubbleModelLayers;
import fr.hugman.mubble.screen.MubbleScreenHandlerTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class MubbleClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Reflection.initialize(MubbleModelLayers.class);

        registerBlockRenderLayers();
        registerHandledScreens();
        MubbleRenderers.registerEntities();
        MubbleRenderers.registerBlockEntities();
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
