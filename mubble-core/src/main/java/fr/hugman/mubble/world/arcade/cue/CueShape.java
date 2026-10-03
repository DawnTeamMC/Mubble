package fr.hugman.mubble.world.arcade.cue;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

/**
 * How the particles of a cue are laid out around where it plays.
 */
public enum CueShape implements StringRepresentable {
    /** Scattered in a small box around the feet. */
    BURST("burst"),
    /** Evenly spread on a flat ring, flying outwards: the ground pound shockwave. */
    RING("ring"),
    /** Trailing behind the player, against the movement. */
    TRAIL("trail");

    public static final Codec<CueShape> CODEC = StringRepresentable.fromEnum(CueShape::values);

    private final String name;

    CueShape(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
