package fr.hugman.mubble.super_mario.world.level.block;

import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.tags.SuperMarioBlockTags;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

/**
 * The instruments of the Mario Paint music composer, which a vanilla note block plays when it sits on top of a
 * block from the matching tag, the same way it plays a guitar on top of wool.
 * <p>
 * They come in the order of the composer's palette. Their samples are tuned to F#, like the vanilla instruments,
 * so that a note block keeps playing the note it is set to whichever instrument it uses.
 *
 * @author Hugman
 * @since v4.0.0
 */
public enum MarioPaintInstrument {
    MARIO(SuperMarioSounds.NOTE_BLOCK_MARIO, SuperMarioBlockTags.MARIO_NOTE_BLOCK_INSTRUMENT),
    MUSHROOM(SuperMarioSounds.NOTE_BLOCK_MUSHROOM, SuperMarioBlockTags.MUSHROOM_NOTE_BLOCK_INSTRUMENT),
    YOSHI(SuperMarioSounds.NOTE_BLOCK_YOSHI, SuperMarioBlockTags.YOSHI_NOTE_BLOCK_INSTRUMENT),
    STAR(SuperMarioSounds.NOTE_BLOCK_STAR, SuperMarioBlockTags.STAR_NOTE_BLOCK_INSTRUMENT),
    FLOWER(SuperMarioSounds.NOTE_BLOCK_FLOWER, SuperMarioBlockTags.FLOWER_NOTE_BLOCK_INSTRUMENT),
    GAME_BOY(SuperMarioSounds.NOTE_BLOCK_GAME_BOY, SuperMarioBlockTags.GAME_BOY_NOTE_BLOCK_INSTRUMENT),
    DOG(SuperMarioSounds.NOTE_BLOCK_DOG, SuperMarioBlockTags.DOG_NOTE_BLOCK_INSTRUMENT),
    CAT(SuperMarioSounds.NOTE_BLOCK_CAT, SuperMarioBlockTags.CAT_NOTE_BLOCK_INSTRUMENT),
    PIG(SuperMarioSounds.NOTE_BLOCK_PIG, SuperMarioBlockTags.PIG_NOTE_BLOCK_INSTRUMENT),
    SWAN(SuperMarioSounds.NOTE_BLOCK_SWAN, SuperMarioBlockTags.SWAN_NOTE_BLOCK_INSTRUMENT),
    FACE(SuperMarioSounds.NOTE_BLOCK_FACE, SuperMarioBlockTags.FACE_NOTE_BLOCK_INSTRUMENT),
    PLANE(SuperMarioSounds.NOTE_BLOCK_PLANE, SuperMarioBlockTags.PLANE_NOTE_BLOCK_INSTRUMENT),
    BOAT(SuperMarioSounds.NOTE_BLOCK_BOAT, SuperMarioBlockTags.BOAT_NOTE_BLOCK_INSTRUMENT),
    CAR(SuperMarioSounds.NOTE_BLOCK_CAR, SuperMarioBlockTags.CAR_NOTE_BLOCK_INSTRUMENT),
    HEART(SuperMarioSounds.NOTE_BLOCK_HEART, SuperMarioBlockTags.HEART_NOTE_BLOCK_INSTRUMENT);

    private final Holder<SoundEvent> sound;
    private final TagKey<Block> blocksBelow;

    MarioPaintInstrument(Holder<SoundEvent> sound, TagKey<Block> blocksBelow) {
        this.sound = sound;
        this.blocksBelow = blocksBelow;
    }

    public Holder<SoundEvent> getSound() {
        return this.sound;
    }

    public TagKey<Block> getBlocksBelow() {
        return this.blocksBelow;
    }

    /**
     * @return the instrument a note block plays when sitting on top of the given block, if any. A block in several
     * tags plays the first of them in the palette order.
     */
    public static Optional<MarioPaintInstrument> byBlockBelow(BlockState below) {
        for (MarioPaintInstrument instrument : values()) {
            if (below.is(instrument.blocksBelow)) {
                return Optional.of(instrument);
            }
        }
        return Optional.empty();
    }
}
