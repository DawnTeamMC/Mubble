package fr.hugman.mubble.tags;

import fr.hugman.mubble.Mubble;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class MubbleDamageTypeTags {
    // SUPER MARIO
    public static final TagKey<DamageType> INSTANT_KILLS_GOOMBAS = bind("instant_kills_goombas");

    private static TagKey<DamageType> bind(String path) {
        return TagKey.create(Registries.DAMAGE_TYPE, Mubble.id(path));
    }
}
