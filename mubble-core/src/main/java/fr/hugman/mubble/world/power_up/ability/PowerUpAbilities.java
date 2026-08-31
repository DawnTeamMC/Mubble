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
 *
 * @param flutter how the holder extends a jump by fluttering, if they can at all
 */
public record PowerUpAbilities(
        Optional<FlutterAbility> flutter
) {
    public static final PowerUpAbilities EMPTY = new PowerUpAbilities(Optional.empty());

    public static final Codec<PowerUpAbilities> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FlutterAbility.CODEC.optionalFieldOf("flutter").forGetter(PowerUpAbilities::flutter)
    ).apply(instance, PowerUpAbilities::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PowerUpAbilities> STREAM_CODEC = StreamCodec.composite(
            FlutterAbility.STREAM_CODEC.apply(ByteBufCodecs::optional), PowerUpAbilities::flutter,
            PowerUpAbilities::new
    );
}
