package fr.hugman.mubble.super_mario.references;

import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.world.level.block.CustomNoteBlockInstrument;
import net.minecraft.resources.ResourceKey;

public class SuperMarioNoteBlockInstrumentIds {
    public static final ResourceKey<CustomNoteBlockInstrument> YOSHI = createKey("yoshi");
    public static final ResourceKey<CustomNoteBlockInstrument> FLOWER = createKey("flower");
    public static final ResourceKey<CustomNoteBlockInstrument> DOG = createKey("dog");
    public static final ResourceKey<CustomNoteBlockInstrument> CAT = createKey("cat");
    public static final ResourceKey<CustomNoteBlockInstrument> PIG = createKey("pig");
    public static final ResourceKey<CustomNoteBlockInstrument> SWAN = createKey("swan");
    public static final ResourceKey<CustomNoteBlockInstrument> FACE = createKey("face");

    private static ResourceKey<CustomNoteBlockInstrument> createKey(String path) {
        return ResourceKey.create(MubbleRegistries.NOTE_BLOCK_INSTRUMENT, SuperMario.id(path));
    }
}
