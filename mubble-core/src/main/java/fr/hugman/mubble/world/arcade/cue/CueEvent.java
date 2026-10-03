package fr.hugman.mubble.world.arcade.cue;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

/**
 * When a move plays one of its cues.
 */
public enum CueEvent implements StringRepresentable {
    /** The move was just entered. */
    START("start"),
    /** Every {@link Cue#interval()} ticks while the move lasts. */
    TICK("tick"),
    /** The move ended on the ground. */
    LAND("land"),
    /** A move-specific burst: a roll boost, a bounce off a block... */
    BOOST("boost");

    public static final Codec<CueEvent> CODEC = StringRepresentable.fromEnum(CueEvent::values);

    private final String name;

    CueEvent(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
