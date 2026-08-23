package fr.hugman.mubble.test.unit;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import fr.hugman.mubble.test.unit.support.TestBootstrap;
import fr.hugman.mubble.world.voyage.session.PlayerStash;
import java.util.List;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The on-disk form of a stashed player, minus its items.
 *
 * <p>This is the one piece of voyage code where a bug costs somebody something real: the stash is
 * written the moment a voyage starts and read back after a crash, so a field that does not survive
 * the trip is an inventory that does not come back.
 *
 * <p>Item stacks are missing here on purpose. Constructing one needs the item's default components,
 * which are bound by a running server and not by {@code Bootstrap} — {@code PlayerStashGameTest}
 * covers the half of the stash that holds items.
 */
public class PlayerStashCodecTest {
    @BeforeAll
    static void bootstrapMinecraft() {
        TestBootstrap.bootstrap();
    }

    @Test
    @DisplayName("every itemless field of a stash survives a round trip")
    void fullStashRoundTrips() {
        PlayerStash original = populated();
        PlayerStash decoded = roundTrip(original);

        assertEquals(original.selectedSlot(), decoded.selectedSlot());
        assertEquals(original.gameMode(), decoded.gameMode());
        assertEquals(original.health(), decoded.health());
        assertEquals(original.foodLevel(), decoded.foodLevel());
        assertEquals(original.saturation(), decoded.saturation());
        assertEquals(original.totalExperience(), decoded.totalExperience());
        assertEquals(original.returnDimension(), decoded.returnDimension());
        assertEquals(original.returnPos(), decoded.returnPos());
        assertEquals(original.returnYRot(), decoded.returnYRot());
        assertEquals(original.returnXRot(), decoded.returnXRot());
    }

    @Test
    @DisplayName("attribute modifiers come back, so nothing a voyage granted leaks out")
    void attributesRoundTrip() {
        assertEquals(populated().attributes(), roundTrip(populated()).attributes());
    }

    @Test
    @DisplayName("active effects come back with their duration and strength")
    void effectsRoundTrip() {
        PlayerStash decoded = roundTrip(populated());

        assertEquals(1, decoded.effects().size());
        MobEffectInstance effect = decoded.effects().getFirst();
        assertEquals(MobEffects.SPEED, effect.getEffect());
        assertEquals(600, effect.getDuration());
        assertEquals(1, effect.getAmplifier());
    }

    @Test
    @DisplayName("an empty-handed player is a valid stash")
    void emptyStashRoundTrips() {
        PlayerStash empty = new PlayerStash(List.of(), 0, List.of(), List.of(), GameType.SURVIVAL,
                20.0F, 20, 5.0F, 0, Level.OVERWORLD, Vec3.ZERO, 0.0F, 0.0F);

        PlayerStash decoded = roundTrip(empty);

        assertTrue(decoded.inventory().isEmpty());
        assertTrue(decoded.effects().isEmpty());
        assertEquals(Level.OVERWORLD, decoded.returnDimension());
    }

    @Test
    @DisplayName("a return dimension that is not the overworld survives")
    void returnDimensionRoundTrips() {
        // The one that matters in practice is a player who started a voyage from the nether, or from
        // another mod's dimension: sending them back to the overworld instead would be a bug nobody
        // notices until it happens to them.
        ResourceKey<Level> custom = ResourceKey.create(Registries.DIMENSION,
                Identifier.fromNamespaceAndPath("mubble", "somewhere_else"));
        PlayerStash stash = new PlayerStash(List.of(), 0, List.of(), List.of(), GameType.ADVENTURE,
                1.0F, 3, 0.0F, 7, custom, new Vec3(1.5D, 2.5D, 3.5D), 90.0F, -45.0F);

        PlayerStash decoded = roundTrip(stash);

        assertEquals(custom, decoded.returnDimension());
        assertEquals(new Vec3(1.5D, 2.5D, 3.5D), decoded.returnPos());
        assertEquals(90.0F, decoded.returnYRot());
        assertEquals(-45.0F, decoded.returnXRot());
    }

    private static PlayerStash roundTrip(PlayerStash stash) {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);
        JsonElement encoded = PlayerStash.CODEC.encodeStart(ops, stash)
                .getOrThrow(error -> new AssertionError("could not encode the stash: " + error));
        return PlayerStash.CODEC.parse(ops, encoded)
                .getOrThrow(error -> new AssertionError("could not read the stash back: " + error));
    }

    private static PlayerStash populated() {
        return new PlayerStash(
                List.of(),
                4,
                List.of(new MobEffectInstance(MobEffects.SPEED, 600, 1)),
                List.of(new AttributeInstance.Packed(Attributes.MOVEMENT_SPEED, 0.1D, List.of(
                        new AttributeModifier(Identifier.fromNamespaceAndPath("mubble", "test"), 0.5D,
                                AttributeModifier.Operation.ADD_MULTIPLIED_BASE)))),
                GameType.SURVIVAL,
                17.5F,
                14,
                2.5F,
                394,
                Level.NETHER,
                new Vec3(100.5D, 64.0D, -200.5D),
                123.0F,
                -12.0F
        );
    }
}
