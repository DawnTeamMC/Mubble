package fr.hugman.mubble.test.unit;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import fr.hugman.mubble.super_mario.world.entity.projectile.Boomerang;
import fr.hugman.mubble.super_mario.world.power_up.action.ThrowBoomerangPowerUpAction;
import fr.hugman.mubble.test.unit.support.CodecAssertions;
import fr.hugman.mubble.test.unit.support.Registrations;
import fr.hugman.mubble.test.unit.support.TestBootstrap;
import fr.hugman.mubble.world.power_up.action.PowerUpAction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.EasingType;
import net.minecraft.world.item.Items;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThrowBoomerangActionCodecTest {
    private static final com.mojang.serialization.Codec<ThrowBoomerangPowerUpAction> CODEC = ThrowBoomerangPowerUpAction.CODEC.codec();

    @BeforeAll
    static void registerContent() {
        Registrations.registerEverything();
    }

    @Test
    @DisplayName("an action with every field left out is the default one")
    void emptyJsonIsTheDefault() {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);
        var decoded = PowerUpAction.TYPE_CODEC.parse(ops, JsonParser.parseString("{\"type\":\"super_mario:throw_boomerang\"}"))
                .getOrThrow(error -> new AssertionError("could not decode: " + error));

        assertEquals(ThrowBoomerangPowerUpAction.DEFAULT, decoded);
    }

    @Test
    @DisplayName("the default action writes no field at all")
    void defaultWritesNothing() {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);
        var encoded = CODEC.encodeStart(ops, ThrowBoomerangPowerUpAction.DEFAULT).getOrThrow();

        assertEquals(new JsonObject(), encoded);
    }

    @Test
    @DisplayName("keeps every field through JSON and network round trips")
    void customActionRoundTrips() {
        var action = new ThrowBoomerangPowerUpAction(
                new Boomerang.Flight(12.0F, 20, EasingType.OUT_CUBIC, 0, 1.5F, 0.2F, 0.5F, 0.5F, 3.0F, 100, 5.0F, 0.4F, 1.0F, 1.0F, 2, 40),
                new Boomerang.Look(
                        BuiltInRegistries.ITEM.wrapAsHolder(Items.STICK),
                        BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ANVIL_LAND),
                        BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.BELL_BLOCK),
                        BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ANVIL_USE),
                        BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ANVIL_BREAK),
                        ParticleTypes.FLAME,
                        ParticleTypes.HEART,
                        ParticleTypes.SMOKE
                ),
                3,
                BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ARROW_SHOOT),
                BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.EXPERIENCE_ORB_PICKUP),
                ParticleTypes.END_ROD
        );

        CodecAssertions.assertJsonRoundTrip(CODEC, action);
        CodecAssertions.assertStreamRoundTrip(ThrowBoomerangPowerUpAction.STREAM_CODEC, action);
    }

    @Test
    @DisplayName("a homing above 1 is rejected")
    void homingIsAShare() {
        CodecAssertions.assertRejects(CODEC, JsonParser.parseString("{\"homing\":1.5}"));
    }
}
