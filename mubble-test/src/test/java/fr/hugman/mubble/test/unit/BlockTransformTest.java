package fr.hugman.mubble.test.unit;

import fr.hugman.mubble.tags.MubbleBlockTags;
import fr.hugman.mubble.test.unit.support.CodecAssertions;
import fr.hugman.mubble.test.unit.support.TestBootstrap;
import fr.hugman.mubble.world.attribute.BlockTransform;
import net.minecraft.advancements.predicates.TagPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Serialisation of what a fireball or an iceball does to the block it hits.
 * <p>
 * The matching itself is not tested here: {@link TagPredicate} needs bound tags, which only exist
 * once a data pack is loaded, so it lives in {@code BlockTransformGameTest} against the real tags.
 */
public class BlockTransformTest {
    @BeforeAll
    static void bootstrapMinecraft() {
        TestBootstrap.bootstrap();
    }

    @Test
    @DisplayName("a transform survives a JSON round trip, sound included")
    void roundTripsThroughJson() {
        var withSound = new BlockTransform(
                TagPredicate.is(MubbleBlockTags.MELTABLE_TO_ICE),
                Blocks.ICE.defaultBlockState(),
                Optional.of(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.FIRE_EXTINGUISH))
        );

        var decoded = CodecAssertions.assertJsonRoundTrip(BlockTransform.CODEC, withSound);

        assertEquals(Blocks.ICE.defaultBlockState(), decoded.result(), "the resulting block state");
        assertEquals(withSound.predicate(), decoded.predicate(), "the tag being matched");
    }

    @Test
    @DisplayName("the sound of a transform is optional")
    void soundIsOptional() {
        var silent = new BlockTransform(
                TagPredicate.is(MubbleBlockTags.MELTABLE_TO_WATER),
                Blocks.WATER.defaultBlockState(),
                Optional.empty()
        );

        var decoded = CodecAssertions.assertJsonRoundTrip(BlockTransform.CODEC, silent);

        assertEquals(Optional.empty(), decoded.sound(), "a transform without a sound must stay without one");
    }

    @Test
    @DisplayName("an empty list never yields a transform")
    void emptyListYieldsNull() {
        assertNull(BlockTransform.testList(List.of(), BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.ICE)));
    }
}
