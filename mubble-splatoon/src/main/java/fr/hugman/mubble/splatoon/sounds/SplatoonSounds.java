package fr.hugman.mubble.splatoon.sounds;

import fr.hugman.mubble.splatoon.Splatoon;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class SplatoonSounds {
    public static final SoundEvent INK_SPLASH = register("block.ink.splash");

    public static final SoundEvent SPLATTERSHOT_SHOOT = register("item.splattershot.shoot");

    private static SoundEvent register(String path) {
        Identifier id = Splatoon.id(path);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    private static Holder.Reference<SoundEvent> registerForHolder(String path) {
        Identifier id = Splatoon.id(path);
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }
}
