package fr.hugman.mubble.client.render;

import fr.hugman.mubble.block.MubbleBlockEntityTypes;
import fr.hugman.mubble.client.render.entity.GoombaRenderer;
import fr.hugman.mubble.client.render.entity.ShooterInkBulletRenderer;
import fr.hugman.mubble.entity.MubbleEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

@Environment(EnvType.CLIENT)
public class MubbleRenderers {
    public static void registerEntities() {
        EntityRendererRegistry.register(MubbleEntityTypes.GOOMBA, GoombaRenderer::new);
        EntityRendererRegistry.register(MubbleEntityTypes.SHOOTER_INK_BULLET, ShooterInkBulletRenderer::new);
    }

    public static void registerBlockEntities() {
        BlockEntityRendererFactories.register(MubbleBlockEntityTypes.BUMPABLE_BLOCK, BumpableBlockEntityRenderer::new);
    }
}
