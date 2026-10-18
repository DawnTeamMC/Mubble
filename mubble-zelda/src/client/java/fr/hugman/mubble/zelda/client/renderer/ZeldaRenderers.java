package fr.hugman.mubble.zelda.client.renderer;

import fr.hugman.mubble.zelda.client.renderer.entity.LoftwingRenderer;
import fr.hugman.mubble.zelda.world.entity.ZeldaEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRenderers;

@Environment(EnvType.CLIENT)
public class ZeldaRenderers {
    public static void registerEntities() {
        EntityRenderers.register(ZeldaEntityTypes.LOFTWING, LoftwingRenderer::new);
    }
}
