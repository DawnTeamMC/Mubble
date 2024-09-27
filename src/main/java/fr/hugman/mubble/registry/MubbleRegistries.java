package fr.hugman.mubble.registry;

import fr.hugman.mubble.entity.GoombaVariant;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;

public class MubbleRegistries {
    public static void register() {
        DynamicRegistries.registerSynced(MubbleRegistryKeys.GOOMBA_VARIANT, GoombaVariant.CODEC);
    }
}
