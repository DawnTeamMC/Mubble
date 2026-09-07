package fr.hugman.mubble.world.power_up.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;

/**
 * What a power-up lets its holder do beyond its action, on keys the game already has.
 * <p>
 * An action is what the power-up trigger key is worth, and a power-up only ever has one. Abilities are the
 * rest: they hang off the movement a player was going to make anyway, so several of them can sit on the same
 * power-up without ever getting in each other's way. Each is a set of numbers rather than a piece of code
 * bound to one form, which is what lets two forms grant the very same ability on their own terms.
 * <p>
 * The two below are the halves of a jump held on. A form is free to take both, as the Flower form does, or
 * only the one it wants.
 *
 * @param flutter  how the holder climbs again past the peak of a jump, if they can at all
 * @param floating how the holder comes down once nothing is lifting them any more, if they do it slowly
 */
public record PowerUpAbilities(
        Optional<FlutterAbility> flutter,
        Optional<FloatAbility> floating
) {
    public static final PowerUpAbilities EMPTY = new PowerUpAbilities(Optional.empty(), Optional.empty());

    public static final Codec<PowerUpAbilities> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FlutterAbility.CODEC.optionalFieldOf("flutter").forGetter(PowerUpAbilities::flutter),
            FloatAbility.CODEC.optionalFieldOf("float").forGetter(PowerUpAbilities::floating)
    ).apply(instance, PowerUpAbilities::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PowerUpAbilities> STREAM_CODEC = StreamCodec.composite(
            FlutterAbility.STREAM_CODEC.apply(ByteBufCodecs::optional), PowerUpAbilities::flutter,
            FloatAbility.STREAM_CODEC.apply(ByteBufCodecs::optional), PowerUpAbilities::floating,
            PowerUpAbilities::new
    );
}
