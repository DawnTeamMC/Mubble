package fr.hugman.mubble.world.arcade.cue;

import com.mojang.serialization.Codec;
import java.util.Map;
import java.util.Optional;

/**
 * The cues of one move, by the event that plays them.
 */
public record MoveCues(Map<CueEvent, Cue> byEvent) {
    public static final MoveCues NONE = new MoveCues(Map.of());
    public static final Codec<MoveCues> CODEC = Codec.unboundedMap(CueEvent.CODEC, Cue.CODEC).xmap(MoveCues::new, MoveCues::byEvent);

    public Optional<Cue> get(CueEvent event) {
        return Optional.ofNullable(this.byEvent.get(event));
    }

    public boolean isEmpty() {
        return this.byEvent.isEmpty();
    }
}
