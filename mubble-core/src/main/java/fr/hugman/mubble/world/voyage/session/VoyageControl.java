package fr.hugman.mubble.world.voyage.session;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;

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
 *
 * @param kind        finish the node, or give up
 * @param destination which route to take, when the node offers more than one; empty means the only
 *                    one there is
 */
public record VoyageControl(VoyageControl.Kind kind, Optional<String> destination) {
    public static final Codec<VoyageControl> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Kind.CODEC.fieldOf("kind").forGetter(VoyageControl::kind),
            Codec.STRING.optionalFieldOf("destination").forGetter(VoyageControl::destination)
    ).apply(instance, VoyageControl::new));

    public static final VoyageControl ADVANCE = new VoyageControl(Kind.ADVANCE, Optional.empty());
    public static final VoyageControl FAIL = new VoyageControl(Kind.FAIL, Optional.empty());

    /** Finishes the node and takes the route to {@code destination}. */
    public static VoyageControl route(String destination) {
        return new VoyageControl(Kind.ADVANCE, Optional.of(destination));
    }

    public enum Kind implements StringRepresentable {
        /** Finishes the current node: on to the next, or the end of the voyage if there is none. */
        ADVANCE("advance"),
        /** Ends the voyage as a loss, immediately. */
        FAIL("fail");

        public static final Codec<Kind> CODEC = StringRepresentable.fromEnum(Kind::values);

        private final String name;

        Kind(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
