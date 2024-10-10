package fr.hugman.mubble.client.render;

import fr.hugman.mubble.block.MubbleBlockEntityTypes;
import fr.hugman.mubble.client.render.entity.GoombaRenderer;
import fr.hugman.mubble.client.render.entity.KoopaShellRenderer;
import fr.hugman.mubble.entity.MubbleEntityTypes;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class MubbleRenderers {
    public static void registerEntities() {
        EntityRendererRegistry.register(MubbleEntityTypes.GOOMBA, GoombaRenderer::new);
        EntityRendererRegistry.register(MubbleEntityTypes.GREEN_KOOPA_SHELL, KoopaShellRenderer::new);
        EntityRendererRegistry.register(MubbleEntityTypes.RED_KOOPA_SHELL, KoopaShellRenderer::new);
    }

    public static void registerBlockEntities() {
        BlockEntityRendererFactories.register(MubbleBlockEntityTypes.BUMPABLE_BLOCK, BumpableBlockEntityRenderer::new);
    }
}
