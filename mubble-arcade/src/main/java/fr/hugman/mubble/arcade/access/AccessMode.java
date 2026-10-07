package fr.hugman.mubble.arcade.access;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

/**
 * What a source says about one move.
 * <p>
 * When several sources speak about the same move, the strongest wins: {@code deny} beats
 * {@code force}, which beats {@code enable}. A source silent about a move has no say in it.
 */
public enum AccessMode implements StringRepresentable {
    /** The move is available, but only to a player who unlocked it. */
    ENABLE("enable"),
    /** The move is available, unlocked or not. */
    FORCE("force"),
    /** The move is not available, whatever any other source says. Items can never carry it. */
    DENY("deny");

    public static final Codec<AccessMode> CODEC = StringRepresentable.fromEnum(AccessMode::values);
    public static final StreamCodec<ByteBuf, AccessMode> STREAM_CODEC = ByteBufCodecs.idMapper(ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO), Enum::ordinal);
    /** The modes an item may carry: {@code deny} is kept for rulesets and commands, so that a trial can always lock a move. */
    public static final Codec<AccessMode> ITEM_CODEC = CODEC.validate(mode -> mode == DENY
            ? DataResult.error(() -> "Items cannot deny a move, only rulesets and commands can")
            : DataResult.success(mode));

    private final String name;

    AccessMode(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    /** The stronger of the two, following {@code deny > force > enable}. */
    public static AccessMode strongest(AccessMode a, AccessMode b) {
        return a.ordinal() >= b.ordinal() ? a : b;
    }
}
