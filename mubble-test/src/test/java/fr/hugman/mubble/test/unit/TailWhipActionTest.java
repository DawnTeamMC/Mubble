package fr.hugman.mubble.test.unit;

import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import fr.hugman.mubble.super_mario.world.power_up.action.TailWhipPowerUpAction;
import fr.hugman.mubble.test.unit.support.CodecAssertions;
import fr.hugman.mubble.test.unit.support.TestBootstrap;
import fr.hugman.mubble.world.power_up.PowerUpCharges;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TailWhipActionTest {
    @BeforeAll
    static void bootstrapMinecraft() {
        TestBootstrap.bootstrap();
    }

    static TailWhipPowerUpAction action() {
        return new TailWhipPowerUpAction(2.5D, 1.25D, Optional.of(6.0F), 0.45D,
                Optional.of(HolderSet.direct(EntityTypes.VILLAGER.builtInRegistryHolder())),
                Optional.of(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.PLAYER_ATTACK_SWEEP)),
                Optional.of(ParticleTypes.CLOUD),
                PowerUpCharges.cooldownRecharge(2, 7));
    }

    @Test
    @DisplayName("a tail whip survives a JSON and a network round trip")
    void roundTrips() {
        CodecAssertions.assertJsonRoundTrip(TailWhipPowerUpAction.CODEC.codec(), action());
        CodecAssertions.assertStreamRoundTrip(TailWhipPowerUpAction.STREAM_CODEC, action());
    }

    @Test
    @DisplayName("a tail whip with no field of its own reads back as the defaults, hitting like the holder does")
    void defaults() {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);
        var decoded = TailWhipPowerUpAction.CODEC.codec().parse(ops, JsonParser.parseString("{}")).getOrThrow(AssertionError::new);

        assertEquals(new TailWhipPowerUpAction(TailWhipPowerUpAction.DEFAULT_RADIUS, TailWhipPowerUpAction.DEFAULT_VERTICAL_REACH, Optional.empty(),
                TailWhipPowerUpAction.DEFAULT_UPWARD_SPEED, Optional.empty(), Optional.empty(), Optional.empty(), TailWhipPowerUpAction.DEFAULT_CHARGES), decoded);
    }
}
