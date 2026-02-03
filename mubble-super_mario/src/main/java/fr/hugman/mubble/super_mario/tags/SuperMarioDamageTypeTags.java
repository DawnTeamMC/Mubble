package fr.hugman.mubble.super_mario.tags;

import fr.hugman.mubble.super_mario.SuperMario;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class SuperMarioDamageTypeTags {
    public static final TagKey<DamageType> INSTANT_KILLS_GOOMBAS = bind("instant_kills_goombas");

    private static TagKey<DamageType> bind(String path) {
        return TagKey.create(Registries.DAMAGE_TYPE, SuperMario.id(path));
    }
}
