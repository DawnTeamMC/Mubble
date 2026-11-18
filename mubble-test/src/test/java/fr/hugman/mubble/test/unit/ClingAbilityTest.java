package fr.hugman.mubble.test.unit;

import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import fr.hugman.mubble.test.unit.support.CodecAssertions;
import fr.hugman.mubble.test.unit.support.TestBootstrap;
import fr.hugman.mubble.world.power_up.ability.ClingAbility;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static fr.hugman.mubble.test.unit.PowerUpCodecTest.sound;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Clinging to a set of blocks like a ladder: how an ability written by a data pack is read and sent. What it
 * does to a player standing next to a wall needs a level, and is covered by the game tests.
 */
public class ClingAbilityTest {
    private static final HolderSet<Block> HONEY = HolderSet.direct(
            Blocks.HONEY_BLOCK.builtInRegistryHolder(), Blocks.BEE_NEST.builtInRegistryHolder());

    @BeforeAll
    static void bootstrapMinecraft() {
        TestBootstrap.bootstrap();
    }

    @Test
    @DisplayName("an ability with every field filled survives a JSON round trip")
    void roundTripsThroughJson() {
        CodecAssertions.assertJsonRoundTrip(ClingAbility.CODEC, full());
    }

    @Test
    @DisplayName("an ability with every field filled survives a network round trip")
    void roundTripsThroughTheNetwork() {
        CodecAssertions.assertStreamRoundTrip(ClingAbility.STREAM_CODEC, full());
    }

    @Test
    @DisplayName("a list of blocks is enough, and everything else reads as the defaults")
    void onlyTheBlocksAreRequired() {
        var decoded = ClingAbility.CODEC.parse(
                        TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE),
                        JsonParser.parseString("""
                                {"blocks": ["minecraft:honey_block", "minecraft:bee_nest"]}
                                """))
                .getOrThrow(error -> new AssertionError("could not read a bare cling: " + error));

        assertEquals(ClingAbility.of(HONEY), decoded, "a cling with no field of its own");
        assertEquals(ClingAbility.DEFAULT_REACH, decoded.reach(), "the default reach");
        assertFalse(decoded.blocks().contains(Blocks.STONE.builtInRegistryHolder()), "a block that is not in the list");
    }

    @Test
    @DisplayName("an ability without blocks is rejected, since it would cling to nothing")
    void theBlocksAreRequired() {
        CodecAssertions.assertRejects(ClingAbility.CODEC, JsonParser.parseString("{}"));
    }

    @Test
    @DisplayName("a negative reach is brought back to touching")
    void aNegativeReachIsRefused() {
        var cling = new ClingAbility(HONEY, -1.0F, Optional.empty(), Optional.empty());

        assertEquals(0.0F, cling.reach(), "a negative reach");
    }

    private static ClingAbility full() {
        return new ClingAbility(HONEY, 0.12F, Optional.of(sound(SoundEvents.HONEY_BLOCK_SLIDE)), Optional.of(ParticleTypes.FALLING_HONEY));
    }
}
