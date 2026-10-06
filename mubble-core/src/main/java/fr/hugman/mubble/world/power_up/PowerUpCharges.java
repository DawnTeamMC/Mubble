package fr.hugman.mubble.world.power_up;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.world.power_up.PowerUpProperties.ChargeCounting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * How many charges a power-up hands out, and how spent ones come back.
 *
 * @param counting how spent charges come back
 * @param max      how many charges the power-up holds at once
 * @param interval the tick count the counting runs on, when it needs one
 */
public record PowerUpCharges(ChargeCounting counting, int max, int interval) {
    /**
     * One charge per entity currently out, with no limit on how many that can be.
     */
    public static final PowerUpCharges DEFAULT = fromActiveEntities(Integer.MAX_VALUE);

    public static final Codec<PowerUpCharges> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ChargeCounting.CODEC.fieldOf("counting").forGetter(PowerUpCharges::counting),
            Codec.INT.optionalFieldOf("max", Integer.MAX_VALUE).forGetter(PowerUpCharges::max),
            Codec.INT.optionalFieldOf("interval", 0).forGetter(PowerUpCharges::interval)
    ).apply(instance, PowerUpCharges::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PowerUpCharges> STREAM_CODEC = StreamCodec.composite(
            ChargeCounting.STREAM_CODEC, PowerUpCharges::counting,
            ByteBufCodecs.INT, PowerUpCharges::max,
            ByteBufCodecs.INT, PowerUpCharges::interval,
            PowerUpCharges::new
    );

    /**
     * Charges that are never counted, so the power-up can always be used.
     */
    public static PowerUpCharges none() {
        return new PowerUpCharges(ChargeCounting.NONE, Integer.MAX_VALUE, 0);
    }

    /**
     * One charge per entity the power-up currently has out; a charge comes back once its entity is gone.
     */
    public static PowerUpCharges fromActiveEntities(int max) {
        return new PowerUpCharges(ChargeCounting.FROM_ACTIVE_ENTITIES, max, 0);
    }

    /**
     * Charges that never come back, so the power-up runs out for good.
     */
    public static PowerUpCharges onlyDecrease(int max) {
        return new PowerUpCharges(ChargeCounting.ONLY_DECREASE, max, 0);
    }

    /**
     * One charge back {@code cooldown} ticks after the last use.
     */
    public static PowerUpCharges cooldownRecharge(int max, int cooldown) {
        return new PowerUpCharges(ChargeCounting.COOLDOWN_RECHARGE, max, cooldown);
    }

    /**
     * One charge back every {@code interval} ticks, for as long as charges are missing.
     */
    public static PowerUpCharges timedRecharge(int max, int interval) {
        return new PowerUpCharges(ChargeCounting.TIMED_RECHARGE, max, interval);
    }

    /**
     * All {@code max} charges at once, {@code window} ticks after the use that opened the window.
     * <p>
     * The power-up is used in bursts: a first use opens a window of {@code window} ticks in which up to
     * {@code max} uses fit, then everything comes back and the next use opens a fresh window.
     */
    public static PowerUpCharges burst(int max, int window) {
        return new PowerUpCharges(ChargeCounting.BURST_RECHARGE, max, window);
    }

    public PowerUpProperties createProperties() {
        return new PowerUpProperties(this.counting, this.max, this.interval);
    }
}
