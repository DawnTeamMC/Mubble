package fr.hugman.mubble.fortnite.client.renderer;

import fr.hugman.mubble.fortnite.world.entity.FortniteEntityTypes;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class FortniteRenderers {
    public static void registerEntities() {
        // The grenade glows the whole time it is out, so it is drawn at full brightness, even in the dark.
        EntityRenderers.register(FortniteEntityTypes.IMPULSE_GRENADE, context -> new ThrownItemRenderer<>(context, 0.75F, true));
    }
}
