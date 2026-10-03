package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.world.level.block.MarioPaintInstrument;
import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;

import java.util.Optional;

/**
 * The Mario Paint instruments, which a vanilla note block plays on top of their blocks. Which block gives which
 * instrument is only known once the tags are loaded, hence game tests rather than unit tests.
 */
public class MarioPaintInstrumentGameTest {
    @GameTest
    public void aBlockBelowPicksItsInstrument(GameTestHelper helper) {
        helper.assertValueEqual(MarioPaintInstrument.byBlockBelow(SuperMarioBlocks.QUESTION_BLOCK.defaultBlockState()), Optional.of(MarioPaintInstrument.MARIO), "the instrument on a question block");
        helper.assertValueEqual(MarioPaintInstrument.byBlockBelow(SuperMarioBlocks.RED_BEEP_BLOCK.defaultBlockState()), Optional.of(MarioPaintInstrument.GAME_BOY), "the instrument on a beep block");
        helper.assertValueEqual(MarioPaintInstrument.byBlockBelow(SuperMarioBlocks.WHITE_EGG_BLOCK.defaultBlockState()), Optional.of(MarioPaintInstrument.YOSHI), "the instrument on an egg block");
        helper.succeed();
    }

    /** Vanilla blocks keep their own instrument, so no music made before the mod was installed changes. */
    @GameTest
    public void vanillaBlocksKeepTheirInstrument(GameTestHelper helper) {
        helper.assertValueEqual(MarioPaintInstrument.byBlockBelow(Blocks.CLAY.defaultBlockState()), Optional.empty(), "the instrument on clay");
        helper.assertValueEqual(MarioPaintInstrument.byBlockBelow(Blocks.STONE.defaultBlockState()), Optional.empty(), "the instrument on stone");
        helper.succeed();
    }

    /** An instrument no block gives would be one no player can ever hear. */
    @GameTest
    public void everyInstrumentHasABlock(GameTestHelper helper) {
        for (MarioPaintInstrument instrument : MarioPaintInstrument.values()) {
            helper.assertTrue(BuiltInRegistries.BLOCK.getTagOrEmpty(instrument.getBlocksBelow()).iterator().hasNext(),
                    "no block gives the " + instrument + " instrument");
        }
        helper.succeed();
    }
}
