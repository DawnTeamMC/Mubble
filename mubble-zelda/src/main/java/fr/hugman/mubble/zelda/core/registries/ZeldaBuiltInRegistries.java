package fr.hugman.mubble.zelda.core.registries;

import fr.hugman.mubble.zelda.world.entity.animal.loftwing.LoftwingVariant;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;

public class ZeldaBuiltInRegistries {
    public static void register() {
        DynamicRegistries.registerSynced(ZeldaRegistries.LOFTWING_VARIANT, LoftwingVariant.DIRECT_CODEC);
    }
}
