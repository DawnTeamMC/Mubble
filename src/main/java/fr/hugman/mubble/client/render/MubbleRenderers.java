package fr.hugman.mubble.client.render;

import fr.hugman.mubble.block.MubbleBlockEntityTypes;
import fr.hugman.mubble.client.render.entity.model.GoombaRenderer;
import fr.hugman.mubble.entity.MubbleEntityTypes;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class MubbleRenderers {
    public static void registerEntities() {
        EntityRendererRegistry.register(MubbleEntityTypes.GOOMBA, GoombaRenderer::new);
    }

    public static void registerBlockEntities() {
        BlockEntityRendererFactories.register(MubbleBlockEntityTypes.BUMPABLE_BLOCK, BumpableBlockEntityRenderer::new);
    }
}