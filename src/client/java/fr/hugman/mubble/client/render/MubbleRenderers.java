package fr.hugman.mubble.client.render;

import fr.hugman.mubble.block.MubbleBlockEntityTypes;
import fr.hugman.mubble.client.render.entity.BallEntityRenderer;
import fr.hugman.mubble.client.render.block.BumpableBlockEntityRenderer;
import fr.hugman.mubble.client.render.entity.GoombaEntityRenderer;
import fr.hugman.mubble.entity.MubbleEntityTypes;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class MubbleRenderers {
    public static void registerEntities() {
		EntityRendererRegistry.register(MubbleEntityTypes.GOOMBA, GoombaEntityRenderer::new);
        EntityRendererRegistry.register(MubbleEntityTypes.FIREBALL, BallEntityRenderer::new);
        EntityRendererRegistry.register(MubbleEntityTypes.ICEBALL, BallEntityRenderer::new);
    }

    public static void registerBlockEntities() {
        BlockEntityRendererFactories.register(MubbleBlockEntityTypes.BUMPABLE_BLOCK, context -> new BumpableBlockEntityRenderer());
    }
}
