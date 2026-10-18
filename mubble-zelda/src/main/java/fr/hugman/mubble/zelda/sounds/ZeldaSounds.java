package fr.hugman.mubble.zelda.sounds;

import fr.hugman.mubble.zelda.Zelda;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class ZeldaSounds {
    public static final SoundEvent LOFTWING_AMBIENT = register("entity.loftwing.ambient");
    public static final SoundEvent LOFTWING_HURT = register("entity.loftwing.hurt");
    public static final SoundEvent LOFTWING_DEATH = register("entity.loftwing.death");
    public static final SoundEvent LOFTWING_FLAP = register("entity.loftwing.flap");
    public static final SoundEvent LOFTWING_STEP = register("entity.loftwing.step");
    public static final SoundEvent LOFTWING_PREEN = register("entity.loftwing.preen");

    private static SoundEvent register(String name) {
        Identifier id = Zelda.id(name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }
}
