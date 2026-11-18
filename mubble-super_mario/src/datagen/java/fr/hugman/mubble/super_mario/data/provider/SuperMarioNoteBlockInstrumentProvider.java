package fr.hugman.mubble.super_mario.data.provider;

import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.super_mario.references.SuperMarioNoteBlockInstrumentIds;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.tags.SuperMarioBlockTags;
import fr.hugman.mubble.world.level.block.CustomNoteBlockInstrument;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

/**
 * The instruments of the Mario Paint music composer that vanilla has nothing close to. The other ones (Mario,
 * the mushroom, the star...) keep their sound event, so that a data pack can still give them a block.
 */
public class SuperMarioNoteBlockInstrumentProvider extends FabricDynamicRegistryProvider {
    public SuperMarioNoteBlockInstrumentProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(MubbleRegistries.NOTE_BLOCK_INSTRUMENT));
    }

    @Override
    public String getName() {
        return "Note Block Instruments";
    }

    public static void bootstrap(BootstrapContext<CustomNoteBlockInstrument> context) {
        register(context, SuperMarioNoteBlockInstrumentIds.YOSHI, SuperMarioSounds.NOTE_BLOCK_YOSHI, SuperMarioBlockTags.YOSHI_NOTE_BLOCK_INSTRUMENT);
        register(context, SuperMarioNoteBlockInstrumentIds.FLOWER, SuperMarioSounds.NOTE_BLOCK_FLOWER, SuperMarioBlockTags.FLOWER_NOTE_BLOCK_INSTRUMENT);
        register(context, SuperMarioNoteBlockInstrumentIds.DOG, SuperMarioSounds.NOTE_BLOCK_DOG, SuperMarioBlockTags.DOG_NOTE_BLOCK_INSTRUMENT);
        register(context, SuperMarioNoteBlockInstrumentIds.CAT, SuperMarioSounds.NOTE_BLOCK_CAT, SuperMarioBlockTags.CAT_NOTE_BLOCK_INSTRUMENT);
        register(context, SuperMarioNoteBlockInstrumentIds.PIG, SuperMarioSounds.NOTE_BLOCK_PIG, SuperMarioBlockTags.PIG_NOTE_BLOCK_INSTRUMENT);
        register(context, SuperMarioNoteBlockInstrumentIds.SWAN, SuperMarioSounds.NOTE_BLOCK_SWAN, SuperMarioBlockTags.SWAN_NOTE_BLOCK_INSTRUMENT);
        register(context, SuperMarioNoteBlockInstrumentIds.FACE, SuperMarioSounds.NOTE_BLOCK_FACE, SuperMarioBlockTags.FACE_NOTE_BLOCK_INSTRUMENT);
    }

    private static void register(BootstrapContext<CustomNoteBlockInstrument> context, ResourceKey<CustomNoteBlockInstrument> key, Holder<SoundEvent> sound, TagKey<Block> blocksBelow) {
        context.register(key, new CustomNoteBlockInstrument(sound, context.lookup(Registries.BLOCK).getOrThrow(blocksBelow)));
    }
}
