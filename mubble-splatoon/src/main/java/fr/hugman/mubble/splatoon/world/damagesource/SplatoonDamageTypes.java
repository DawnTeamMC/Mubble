package fr.hugman.mubble.splatoon.world.damagesource;

import fr.hugman.mubble.splatoon.Splatoon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class SplatoonDamageTypes {
    /**
     * Being hit by ink. Like in Splatoon, it neither knocks back nor gives a moment of invulnerability, so that every
     * shot of a fast shooter counts.
     */
    public static final ResourceKey<DamageType> INK = createKey("ink");

    private static ResourceKey<DamageType> createKey(String path) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Splatoon.id(path));
    }
}
