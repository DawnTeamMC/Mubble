package fr.hugman.mubble;

import com.google.common.reflect.Reflection;
import fr.hugman.mubble.block.MubbleBlockEntityTypes;
import fr.hugman.mubble.block.MubbleBlocks;
import fr.hugman.mubble.client.gui.screen.BumpableScreen;
import fr.hugman.mubble.client.render.BumpableBlockEntityRenderer;
import fr.hugman.mubble.client.render.entity.ShooterInkBulletRenderer;
import fr.hugman.mubble.client.render.entity.model.MubbleEntityModelLayers;
import fr.hugman.mubble.entity.MubbleEntityTypes;
import fr.hugman.mubble.screen.MubbleScreenHandlerTypes;
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
        Reflection.initialize(MubbleEntityModelLayers.class);

        registerEntityRenderers();
        registerBlockRenderLayers();
        registerHandledScreens();
        registerBlockEntitiesRenderers();
    }

    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(MubbleEntityTypes.SHOOTER_INK_BULLET, ShooterInkBulletRenderer::new);
    }

    private static void registerBlockRenderLayers() {
        var map = BlockRenderLayerMap.INSTANCE;
        var cutout = RenderLayer.getCutout();

        map.putBlock(MubbleBlocks.RED_BEEP_BLOCK, cutout);
        map.putBlock(MubbleBlocks.BLUE_BEEP_BLOCK, cutout);
    }

    public static void registerBlockEntitiesRenderers() {
        BlockEntityRendererFactories.register(MubbleBlockEntityTypes.BUMPABLE_BLOCK, BumpableBlockEntityRenderer::new);
    }

    private static void registerHandledScreens() {
        HandledScreens.register(MubbleScreenHandlerTypes.BUMPABLE_BLOCK, BumpableScreen::new);
    }
}
