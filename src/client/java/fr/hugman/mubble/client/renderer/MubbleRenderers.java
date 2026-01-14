package fr.hugman.mubble.client.renderer;

import fr.hugman.mubble.client.renderer.entity.BallRenderer;
import fr.hugman.mubble.client.renderer.blockentity.BumpableBlockRenderer;
import fr.hugman.mubble.client.renderer.entity.CollectibleEntityRenderer;
import fr.hugman.mubble.client.renderer.entity.GoombaRenderer;
import fr.hugman.mubble.client.renderer.entity.KoopaShellRenderer;
import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import fr.hugman.mubble.world.level.block.entity.MubbleBlockEntityTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class MubbleRenderers {
    public static void registerEntities() {
        EntityRenderers.register(MubbleEntityTypes.COLLECTIBLE, CollectibleEntityRenderer::new);
        EntityRenderers.register(MubbleEntityTypes.GOOMBA, GoombaRenderer::new);
        EntityRenderers.register(MubbleEntityTypes.GREEN_KOOPA_SHELL, KoopaShellRenderer::new);
        EntityRenderers.register(MubbleEntityTypes.RED_KOOPA_SHELL, KoopaShellRenderer::new);
        EntityRenderers.register(MubbleEntityTypes.FIREBALL, BallRenderer::new);
        EntityRenderers.register(MubbleEntityTypes.ICEBALL, BallRenderer::new);
        EntityRenderers.register(MubbleEntityTypes.GOLD_FIREBALL, BallRenderer::new);
    }

    public static void registerBlockEntities() {
        BlockEntityRenderers.register(MubbleBlockEntityTypes.BUMPABLE_BLOCK, context -> new BumpableBlockRenderer());
    }
}
