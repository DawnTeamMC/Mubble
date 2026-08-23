package fr.hugman.mubble.world.voyage.session;

import com.mojang.serialization.Codec;

import net.minecraft.util.StringRepresentable;

/**
 * What a control item does when it is used.
 *
 * <p>Stored as a data component on an otherwise ordinary item, which is the whole gate: the
 * behaviour keys off this marker and nothing else, so an emerald picked up somewhere is still an
 * emerald.
 *
 * <p>Not synced to the client. The client needs the item's name to know what it is holding, and
 * that is a vanilla custom name; the marker is only read where the decision is made, which is the
 * server.
 */
public enum VoyageControl implements StringRepresentable {
    /** Finishes the current trial: on to the next one, or the end of the voyage if it was the last. */
    ADVANCE("advance"),
    /** Ends the voyage as a loss, immediately. */
    FAIL("fail");

    public static final Codec<VoyageControl> CODEC = StringRepresentable.fromEnum(VoyageControl::values);

    private final String name;

    VoyageControl(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
