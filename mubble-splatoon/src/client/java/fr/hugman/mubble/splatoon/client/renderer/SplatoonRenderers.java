package fr.hugman.mubble.splatoon.client.renderer;

import fr.hugman.mubble.splatoon.client.renderer.entity.ShooterInkBulletRenderer;
import fr.hugman.mubble.splatoon.world.entity.SplatoonEntityTypes;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class SplatoonRenderers {
    public static void registerEntities() {
        EntityRenderers.register(SplatoonEntityTypes.SHOOTER_INK_BULLET, ShooterInkBulletRenderer::new);
    }
}
