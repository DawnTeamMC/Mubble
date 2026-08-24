package fr.hugman.mubble.test.unit;

import com.mojang.serialization.Codec;
import fr.hugman.mubble.test.unit.support.TestBootstrap;
import fr.hugman.mubble.world.voyage.session.PlayerStash;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The on-disk form of a stashed player.
 *
 * <p>This is the one piece of voyage code where a bug costs somebody something real: the stash is
 * written the moment a voyage starts and read back after a crash, so a field that does not survive
 * the trip is an inventory that does not come back.
 *
 * <p>Round-tripped through {@link NbtOps} rather than JSON, because NBT is what it is actually
 * stored as and it is the only form that keeps a byte a byte.
 *
 * <p>What is <em>in</em> the tag is the game's own player serialisation, so it is not this test's
 * business — {@code PlayerStashGameTest} covers taking one off a real player and putting it back.
 */
public class PlayerStashCodecTest {
    @BeforeAll
    static void bootstrapMinecraft() {
        TestBootstrap.bootstrap();
    }

    @Test
    @DisplayName("the player tag survives a round trip untouched")
    void tagRoundTrips() {
        CompoundTag data = new CompoundTag();
        data.putInt("XpTotal", 394);
        data.putFloat("Health", 17.5F);
        data.putByte("playerGameType", (byte) 2);
        CompoundTag nested = new CompoundTag();
        nested.putString("id", "mubble:cloud_flower");
        data.put("power_up", nested);

        PlayerStash decoded = roundTrip(stash(data, Level.OVERWORLD));

        assertEquals(data, decoded.data(), "the player tag came back different");
    }

    @Test
    @DisplayName("an empty tag is a valid stash")
    void emptyTagRoundTrips() {
        assertEquals(new CompoundTag(), roundTrip(stash(new CompoundTag(), Level.OVERWORLD)).data());
    }

    @Test
    @DisplayName("where to put the player back survives")
    void returnPositionRoundTrips() {
        // The one that matters in practice is a player who started from the nether, or from another
        // mod's dimension: sending them to the overworld instead would be a bug nobody notices until
        // it happens to them.
        ResourceKey<Level> custom = ResourceKey.create(Registries.DIMENSION,
                Identifier.fromNamespaceAndPath("mubble", "somewhere_else"));
        PlayerStash decoded = roundTrip(new PlayerStash(new CompoundTag(), custom,
                new Vec3(1.5D, 2.5D, 3.5D), 90.0F, -45.0F));

        assertEquals(custom, decoded.returnDimension());
        assertEquals(new Vec3(1.5D, 2.5D, 3.5D), decoded.returnPos());
        assertEquals(90.0F, decoded.returnYRot());
        assertEquals(-45.0F, decoded.returnXRot());
    }

    private static PlayerStash stash(CompoundTag data, ResourceKey<Level> dimension) {
        return new PlayerStash(data, dimension, new Vec3(100.5D, 64.0D, -200.5D), 123.0F, -12.0F);
    }

    private static PlayerStash roundTrip(PlayerStash stash) {
        Codec<PlayerStash> codec = PlayerStash.CODEC;
        Tag encoded = codec.encodeStart(NbtOps.INSTANCE, stash)
                .getOrThrow(error -> new AssertionError("could not write the stash: " + error));
        return codec.parse(NbtOps.INSTANCE, encoded)
                .getOrThrow(error -> new AssertionError("could not read the stash back: " + error));
    }
}
