package fr.hugman.mubble.world.arcade.move;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.world.arcade.cue.MoveCues;
import java.util.Map;
import java.util.Optional;
import net.minecraft.resources.Identifier;

/**
 * What a profile says about one of its moves.
 * <p>
 * A move a profile does not list is not available under it, whatever the sources of the player say:
 * listing a move is how a profile supports it, and these settings are its numbers there.
 *
 * @param exhaustion food exhaustion added each time the move starts, when the profile charges for it
 * @param handsBusy  whether the move keeps the hands from attacking and using items, when the move
 *                   should not keep its own default
 * @param animation  the animation played during the move, when it should not be the one named after
 *                   the move
 * @param cues       the sounds and particles of the move
 * @param params     the numbers of the move, see {@link ArcadeMove#params()}
 */
public record MoveSettings(
        float exhaustion,
        Optional<Boolean> handsBusy,
        Optional<Identifier> animation,
        MoveCues cues,
        Map<String, Double> params
) {
    public static final MoveSettings DEFAULT = new MoveSettings(0.0F, Optional.empty(), Optional.empty(), MoveCues.NONE, Map.of());

    private static final Codec<MoveSettings> UNCHECKED_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.floatRange(0.0F, 40.0F).optionalFieldOf("exhaustion", 0.0F).forGetter(MoveSettings::exhaustion),
            Codec.BOOL.optionalFieldOf("hands_busy").forGetter(MoveSettings::handsBusy),
            Identifier.CODEC.optionalFieldOf("animation").forGetter(MoveSettings::animation),
            MoveCues.CODEC.optionalFieldOf("cues", MoveCues.NONE).forGetter(MoveSettings::cues),
            Codec.unboundedMap(Codec.STRING, Codec.DOUBLE).optionalFieldOf("params", Map.of()).forGetter(MoveSettings::params)
    ).apply(instance, MoveSettings::new));

    /**
     * The settings of {@code move}: a parameter the move does not declare, or one out of its range,
     * is an error rather than something silently ignored, since a typo in a profile would otherwise
     * look exactly like a parameter left at its default.
     */
    public static Codec<MoveSettings> codecFor(ArcadeMove move) {
        return UNCHECKED_CODEC.validate(settings -> {
            for (var entry : settings.params.entrySet()) {
                var param = move.param(entry.getKey());
                if (param.isEmpty()) {
                    return DataResult.error(() -> "Unknown parameter '" + entry.getKey() + "' for move " + move + ", expected one of " + move.params().stream().map(MoveParam::name).toList());
                }
                if (!param.get().accepts(entry.getValue())) {
                    return DataResult.error(() -> "Parameter '" + entry.getKey() + "' of move " + move + " must be within [" + param.get().min() + ", " + param.get().max() + "], got " + entry.getValue());
                }
            }
            return DataResult.success(settings);
        });
    }

    public double get(MoveParam param) {
        var value = this.params.get(param.name());
        return value == null ? param.defaultValue() : value;
    }

    public int ticks(MoveParam param) {
        return (int) Math.round(this.get(param));
    }

    public boolean handsBusy(ArcadeMove move) {
        return this.handsBusy.orElse(move.handsBusyByDefault());
    }
}
