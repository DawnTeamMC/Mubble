package fr.hugman.mubble.super_mario.tags;

import fr.hugman.mubble.super_mario.SuperMario;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class SuperMarioDamageTypeTags {
    public static final TagKey<DamageType> INSTANT_KILLS_GOOMBAS = bind("instant_kills_goombas");

    /**
     * Damage that breaks a block of ice open on the spot rather than chipping away at it.
     * <p>
     * Fire is the whole of it, plus the mod's own fireballs, which are fire in everything but the
     * vanilla tag.
     *
     * @see fr.hugman.mubble.super_mario.world.entity.freeze.Freezing
     */
    public static final TagKey<DamageType> MELTS_FREEZE = bind("melts_freeze");

    private static TagKey<DamageType> bind(String path) {
        return TagKey.create(Registries.DAMAGE_TYPE, SuperMario.id(path));
    }
}
