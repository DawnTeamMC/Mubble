package fr.hugman.mubble.client.render;

import fr.hugman.mubble.block.MubbleBlockEntityTypes;
import fr.hugman.mubble.entity.MubbleEntities;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class MubbleRenderers {
    public static void registerEntities() {
        EntityRendererRegistry.register(MubbleEntities.GOOMBA, GoombaEntityRenderer::new);
    }


    public static void registerBlockEntities() {
        BlockEntityRendererFactories.register(MubbleBlockEntityTypes.BUMPABLE_BLOCK, BumpableBlockEntityRenderer::new);
    }
}
