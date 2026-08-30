package fr.hugman.mubble.test.unit.support;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import io.netty.buffer.Unpooled;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Round trip helpers. A codec that drops or swaps a field still compiles and still writes valid JSON,
 * so the only thing catching it is feeding a value in and comparing what comes back out.
 */
public final class CodecAssertions {
    private CodecAssertions() {
    }

    /** Encodes {@code value} to JSON, decodes it again, and asserts nothing changed on the way. */
    public static <T> T assertJsonRoundTrip(Codec<T> codec, T value) {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);

        JsonElement encoded = codec.encodeStart(ops, value)
                .getOrThrow(error -> new AssertionError("could not encode " + value + ": " + error));
        T decoded = codec.parse(ops, encoded)
                .getOrThrow(error -> new AssertionError("could not decode " + encoded + ": " + error));

        assertEquals(value, decoded, () -> "the value changed through a JSON round trip, encoded as " + encoded);
        return decoded;
    }

    /** Same, over the network codec. */
    public static <T> T assertStreamRoundTrip(StreamCodec<? super RegistryFriendlyByteBuf, T> codec, T value) {
        var buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), TestBootstrap.registries());

        codec.encode(buf, value);
        T decoded = codec.decode(buf);

        assertEquals(value, decoded, "the value changed through a network round trip");
        assertTrue(buf.readableBytes() == 0, "the decoder left " + buf.readableBytes() + " unread bytes behind");
        return decoded;
    }

    /** Asserts that {@code json} cannot be read as a {@code T}, and that the failure is not a crash. */
    public static <T> void assertRejects(Codec<T> codec, JsonElement json) {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);
        var result = codec.parse(ops, json);
        assertTrue(result.isError(), () -> json + " was accepted, it should not have been");
    }
}
