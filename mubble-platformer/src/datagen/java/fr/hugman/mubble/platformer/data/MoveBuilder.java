package fr.hugman.mubble.platformer.data;

import fr.hugman.mubble.platformer.cue.CueEvent;
import fr.hugman.mubble.platformer.cue.MoveCues;
import fr.hugman.mubble.platformer.move.PlatformerMove;
import fr.hugman.mubble.platformer.move.MoveParam;
import fr.hugman.mubble.platformer.move.MoveSettings;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Builds the settings of one move of a shipped profile. Every parameter of the move is written out,
 * starting from its default: the files are meant to be read and copied by data pack authors.
 */
public final class MoveBuilder {
    private final PlatformerMove move;
    private float exhaustion;
    private final Map<String, Double> params = new LinkedHashMap<>();
    private final Map<CueEvent, fr.hugman.mubble.platformer.cue.Cue> cues = new EnumMap<>(CueEvent.class);

    private MoveBuilder(PlatformerMove move) {
        this.move = move;
        for (var param : move.params()) {
            this.params.put(param.name(), param.defaultValue());
        }
    }

    public static MoveBuilder move(PlatformerMove move) {
        return new MoveBuilder(move);
    }

    public MoveBuilder set(MoveParam param, double value) {
        if (!this.params.containsKey(param.name())) {
            throw new IllegalArgumentException(param.name() + " is not a parameter of " + this.move);
        }
        this.params.put(param.name(), value);
        return this;
    }

    public MoveBuilder exhaustion(float exhaustion) {
        this.exhaustion = exhaustion;
        return this;
    }

    public MoveBuilder cue(CueEvent event, CueBuilder cue) {
        this.cues.put(event, cue.build());
        return this;
    }

    public PlatformerMove target() {
        return this.move;
    }

    public MoveSettings build() {
        // insertion ordered copies, so that the generated files come out the same on every run
        return new MoveSettings(this.exhaustion, Optional.empty(), Optional.empty(),
                new MoveCues(Collections.unmodifiableMap(new EnumMap<>(this.cues))),
                Collections.unmodifiableMap(new LinkedHashMap<>(this.params)));
    }
}
