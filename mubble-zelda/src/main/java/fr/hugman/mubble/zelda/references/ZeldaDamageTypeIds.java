package fr.hugman.mubble.zelda.references;

import fr.hugman.mubble.zelda.Zelda;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class ZeldaDamageTypeIds {
    /** Flying a loftwing straight into a wall, which hurts the bird and throws the rider off. */
    public static final ResourceKey<DamageType> LOFTWING_CRASH = createKey("loftwing_crash");

    private static ResourceKey<DamageType> createKey(String path) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Zelda.id(path));
    }
}
