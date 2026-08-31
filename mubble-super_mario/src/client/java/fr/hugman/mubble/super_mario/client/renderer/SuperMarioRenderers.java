package fr.hugman.mubble.super_mario.client.renderer;

import fr.hugman.mubble.super_mario.client.renderer.blockentity.BumpableBlockRenderer;
import fr.hugman.mubble.client.renderer.entity.BallRenderer;
import fr.hugman.mubble.super_mario.client.renderer.entity.BubbleRenderer;
import fr.hugman.mubble.super_mario.client.renderer.entity.CloudPlatformRenderer;
import fr.hugman.mubble.super_mario.client.renderer.entity.FlowerRenderer;
import fr.hugman.mubble.super_mario.client.renderer.entity.GoombaRenderer;
import fr.hugman.mubble.super_mario.client.renderer.entity.KoopaShellRenderer;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.level.block.entity.SuperMarioBlockEntityTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;

public class SuperMarioRenderers {
    public static void registerEntities() {
        EntityRenderers.register(SuperMarioEntityTypes.GOOMBA, GoombaRenderer::new);
        EntityRenderers.register(SuperMarioEntityTypes.GREEN_KOOPA_SHELL, KoopaShellRenderer::new);
        EntityRenderers.register(SuperMarioEntityTypes.RED_KOOPA_SHELL, KoopaShellRenderer::new);
        EntityRenderers.register(SuperMarioEntityTypes.FIREBALL, BallRenderer::new);
        EntityRenderers.register(SuperMarioEntityTypes.ICEBALL, BallRenderer::new);
        EntityRenderers.register(SuperMarioEntityTypes.GOLD_FIREBALL, BallRenderer::new);
        EntityRenderers.register(SuperMarioEntityTypes.CLOUD_PLATFORM, CloudPlatformRenderer::new);
        EntityRenderers.register(SuperMarioEntityTypes.BUBBLE, BubbleRenderer::new);
        EntityRenderers.register(SuperMarioEntityTypes.FLOWER, FlowerRenderer::new);
    }

    public static void registerBlockEntities() {
        BlockEntityRenderers.register(SuperMarioBlockEntityTypes.BUMPABLE_BLOCK, context -> new BumpableBlockRenderer());
    }
}
