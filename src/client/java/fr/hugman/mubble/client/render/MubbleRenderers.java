package fr.hugman.mubble.client.render;

import fr.hugman.mubble.block.MubbleBlockEntityTypes;
import fr.hugman.mubble.client.render.entity.BallEntityRenderer;
import fr.hugman.mubble.client.render.block.BumpableBlockEntityRenderer;
import fr.hugman.mubble.client.render.entity.GoombaEntityRenderer;
import fr.hugman.mubble.client.render.entity.KoopaShellRenderer;
import fr.hugman.mubble.entity.MubbleEntityTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class MubbleRenderers {
    public static void registerEntities() {
        EntityRenderers.register(MubbleEntityTypes.GOOMBA, GoombaEntityRenderer::new);
        EntityRenderers.register(MubbleEntityTypes.GREEN_KOOPA_SHELL, KoopaShellRenderer::new);
        EntityRenderers.register(MubbleEntityTypes.RED_KOOPA_SHELL, KoopaShellRenderer::new);
        EntityRenderers.register(MubbleEntityTypes.FIREBALL, BallEntityRenderer::new);
        EntityRenderers.register(MubbleEntityTypes.ICEBALL, BallEntityRenderer::new);
    }

    public static void registerBlockEntities() {
        BlockEntityRenderers.register(MubbleBlockEntityTypes.BUMPABLE_BLOCK, context -> new BumpableBlockEntityRenderer());
    }
}
