package fr.hugman.mubble.entity.damage;

import fr.hugman.mubble.Mubble;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class MubbleDamageTypeTags {
    // SUPER MARIO
    public static final TagKey<DamageType> INSTANT_KILLS_GOOMBAS = of("instant_kills_goombas");

    private static TagKey<DamageType> of(String path) {
        return TagKey.of(RegistryKeys.DAMAGE_TYPE, Mubble.id(path));
    }

}
