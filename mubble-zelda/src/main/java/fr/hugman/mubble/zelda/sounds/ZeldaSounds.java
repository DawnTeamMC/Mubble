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
    /** The cry of a bird that has lost its owner. */
    public static final SoundEvent LOFTWING_CALL = register("entity.loftwing.call");
    public static final SoundEvent LOFTWING_BOND = register("entity.loftwing.bond");
    public static final SoundEvent LOFTWING_CRASH = register("entity.loftwing.crash");
    public static final SoundEvent LOFTWING_WHISTLE = register("item.loftwing_whistle.blow");

    public static final SoundEvent LOFTWING_EGG_CRACK = register("block.loftwing_egg.crack");
    public static final SoundEvent LOFTWING_EGG_HATCH = register("block.loftwing_egg.hatch");

    private static SoundEvent register(String name) {
        Identifier id = Zelda.id(name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }
}
