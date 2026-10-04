package fr.hugman.mubble.test.gametest;

import fr.hugman.mubble.super_mario.references.SuperMarioNoteBlockInstrumentIds;
import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import fr.hugman.mubble.world.level.block.CustomNoteBlockInstrument;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

/**
 * The instruments a vanilla note block plays on top of some blocks, which data packs define. They are only known
 * once a world is loaded, hence game tests rather than unit tests.
 */
public class NoteBlockInstrumentGameTest {
    @GameTest
    public void aBlockBelowPicksItsInstrument(GameTestHelper helper) {
        helper.assertValueEqual(instrumentOn(helper, SuperMarioBlocks.WHITE_EGG_BLOCK.defaultBlockState()), Optional.of(SuperMarioNoteBlockInstrumentIds.YOSHI), "the instrument on an egg block");
        helper.assertValueEqual(instrumentOn(helper, SuperMarioBlocks.NOTE_BLOCK.defaultBlockState()), Optional.of(SuperMarioNoteBlockInstrumentIds.FACE), "the instrument on a note block");
        helper.succeed();
    }

    /** Vanilla blocks keep their own instrument, so no music made before the mod was installed changes. */
    @GameTest
    public void vanillaBlocksKeepTheirInstrument(GameTestHelper helper) {
        helper.assertValueEqual(instrumentOn(helper, Blocks.CLAY.defaultBlockState()), Optional.empty(), "the instrument on clay");
        helper.assertValueEqual(instrumentOn(helper, Blocks.STONE.defaultBlockState()), Optional.empty(), "the instrument on stone");
        helper.succeed();
    }

    /** A data pack can add an instrument, even with a sound that only its resource pack declares. */
    @GameTest
    public void aDataPackCanAddAnInstrument(GameTestHelper helper) {
        var instrument = CustomNoteBlockInstrument.byBlockBelow(helper.getLevel().registryAccess(), Blocks.SPONGE.defaultBlockState())
                .orElseThrow(() -> new AssertionError("the instrument of the data pack was not loaded"));

        helper.assertValueEqual(instrument.key().identifier(), Identifier.fromNamespaceAndPath("mubble-gametest", "from_data_pack"), "the instrument on a sponge");
        helper.assertValueEqual(instrument.value().sound().value().location(), Identifier.fromNamespaceAndPath("mubble-gametest", "block.note_block.from_data_pack"), "the sound of the instrument");
        helper.succeed();
    }

    private static Optional<ResourceKey<CustomNoteBlockInstrument>> instrumentOn(GameTestHelper helper, BlockState below) {
        return CustomNoteBlockInstrument.byBlockBelow(helper.getLevel().registryAccess(), below).map(Holder.Reference::key);
    }
}
