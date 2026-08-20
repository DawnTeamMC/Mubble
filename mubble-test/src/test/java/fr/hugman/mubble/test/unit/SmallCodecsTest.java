package fr.hugman.mubble.test.unit;

import fr.hugman.mubble.sounds.SoundConfig;
import fr.hugman.mubble.test.unit.support.CodecAssertions;
import fr.hugman.mubble.test.unit.support.TestBootstrap;
import fr.hugman.mubble.world.item.spawn_egg.VariantSpawnEggInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The remaining data holders of the mod, small enough that a round trip is the whole test.
 */
public class SmallCodecsTest {
    @BeforeAll
    static void bootstrapMinecraft() {
        TestBootstrap.bootstrap();
    }

    @Nested
    @DisplayName("SoundConfig")
    class SoundConfigCodec {
        @Test
        @DisplayName("keeps its sound, volume and pitch through a round trip")
        void roundTripsThroughJson() {
            var config = new SoundConfig(
                    BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.BELL_BLOCK),
                    ConstantFloat.of(0.75F),
                    ConstantFloat.of(1.25F)
            );

            var ops = TestBootstrap.registries().createSerializationContext(com.mojang.serialization.JsonOps.INSTANCE);
            var encoded = SoundConfig.CODEC.encodeStart(ops, config)
                    .getOrThrow(error -> new AssertionError("could not encode: " + error));
            var decoded = SoundConfig.CODEC.parse(ops, encoded)
                    .getOrThrow(error -> new AssertionError("could not decode: " + error));

            // SoundConfig is a mutable holder without equals, so the fields are compared one by one.
            assertEquals(config.sound(), decoded.sound(), "sound");
            assertEquals(config.volume(), decoded.volume(), "volume");
            assertEquals(config.pitch(), decoded.pitch(), "pitch");
        }

        @Test
        @DisplayName("accepts a random range as well as a constant")
        void acceptsAFloatRange() {
            var config = new SoundConfig(
                    BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.BELL_BLOCK),
                    UniformFloat.of(0.5F, 1.5F),
                    ConstantFloat.of(1.0F)
            );

            var ops = TestBootstrap.registries().createSerializationContext(com.mojang.serialization.JsonOps.INSTANCE);
            var encoded = SoundConfig.CODEC.encodeStart(ops, config)
                    .getOrThrow(error -> new AssertionError("could not encode a ranged volume: " + error));
            var decoded = SoundConfig.CODEC.parse(ops, encoded)
                    .getOrThrow(error -> new AssertionError("could not decode a ranged volume: " + error));

            assertEquals(0.5F, decoded.volume().min(), "the bottom of the range");
            assertEquals(1.5F, decoded.volume().max(), "the top of the range");
        }
    }

    @Nested
    @DisplayName("VariantSpawnEggInfo")
    class SpawnEggInfoCodec {
        @Test
        @DisplayName("keeps its name and its visibility through a round trip")
        void roundTripsThroughJson() {
            var info = new VariantSpawnEggInfo(Component.translatable("entity.super_mario.goomba"), true);

            var decoded = CodecAssertions.assertJsonRoundTrip(VariantSpawnEggInfo.CODEC, info);

            assertTrue(decoded.onlyInSearch(), "the egg should have stayed search-only");
        }

        @Test
        @DisplayName("defaults to being shown in the tab, not only in search")
        void defaultsToVisible() {
            var decoded = CodecAssertions.assertJsonRoundTrip(
                    VariantSpawnEggInfo.CODEC,
                    new VariantSpawnEggInfo(Component.literal("Goomba"))
            );

            assertFalse(decoded.onlyInSearch(), "an egg without the flag belongs in its tab");
        }
    }
}
