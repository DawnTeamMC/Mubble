package fr.hugman.mubble.client.renderer;

import fr.hugman.mubble.client.renderer.entity.CollectibleEntityRenderer;
import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class MubbleRenderers {
    public static void registerEntities() {
        EntityRenderers.register(MubbleEntityTypes.COLLECTIBLE, CollectibleEntityRenderer::new);
    }
}
