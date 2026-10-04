package fr.hugman.mubble.world.level.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

/**
 * An instrument a vanilla note block plays when it sits on top of one of the given blocks, the same way it plays a
 * guitar on top of wool. They are loaded from data packs, so that anyone can add their own:
 * <pre>{@code
 * // data/<namespace>/mubble/note_block_instrument/<name>.json
 * {
 *   "sound": "super_mario:block.note_block.instrument.yoshi",
 *   "blocks_below": "#super_mario:note_block_instruments/yoshi"
 * }
 * }</pre>
 * The sound is either the ID of a registered sound event, or an inline one such as
 * {@code {"sound_id": "<namespace>:<sound>"}} for a sound that only a resource pack declares. Like the vanilla
 * instruments, it is pitched from the note the note block is set to, a pitch of 1 being F#.
 * <p>
 * The instrument never shows in the block state, which keeps its vanilla values: the sound is only swapped when the
 * note is played, so worlds and vanilla clients are left unaffected.
 *
 * @param sound       the sound played, pitched from the note
 * @param blocksBelow the blocks the note block has to sit on top of. When a block is given by several instruments,
 *                    which one plays is not specified
 * @author Hugman
 * @since v4.0.0
 */
public record CustomNoteBlockInstrument(Holder<SoundEvent> sound, HolderSet<Block> blocksBelow) {
    public static final Codec<CustomNoteBlockInstrument> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SoundEvent.CODEC.fieldOf("sound").forGetter(CustomNoteBlockInstrument::sound),
            RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks_below").forGetter(CustomNoteBlockInstrument::blocksBelow)
    ).apply(instance, CustomNoteBlockInstrument::new));

    /**
     * @return the instrument a note block plays when sitting on top of the given block, if any
     */
    public static Optional<Holder.Reference<CustomNoteBlockInstrument>> byBlockBelow(RegistryAccess registries, BlockState below) {
        return registries.lookupOrThrow(MubbleRegistries.NOTE_BLOCK_INSTRUMENT).listElements()
                .filter(instrument -> instrument.value().blocksBelow().contains(below.typeHolder()))
                .findFirst();
    }
}
