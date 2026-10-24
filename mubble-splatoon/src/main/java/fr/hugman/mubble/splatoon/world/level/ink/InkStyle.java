package fr.hugman.mubble.splatoon.world.level.ink;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.splatoon.Splatoon;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

/**
 * What a cell of ink looks like and does: its color, and its type.
 *
 * <p>The type is kept as a plain identifier rather than a registry entry, so that ink painted with a type that
 * disappears later (a removed data pack) still loads. The client draws a type with the
 * {@code <namespace>:block/ink/<path>} texture of the block atlas, which resource packs can replace or animate, and
 * falls back on the {@link #NORMAL} one when it is missing. Gameplay effects of ink types (poison, slowness...) are
 * meant to hang off the same identifier.
 *
 * @param color the RGB color of the ink, which tints the type's texture
 * @param type  the type of the ink
 * @author Hugman
 * @since v4.0.0
 */
public record InkStyle(int color, Identifier type) {
    public static final Identifier NORMAL = Splatoon.id("normal");
    public static final InkStyle DEFAULT = new InkStyle(0x3AAFD9, NORMAL);

    /**
     * Accepts {@code "#RRGGBB"}, a packed integer or {@code [r, g, b]} floats, and writes the first. Ink has no alpha.
     */
    public static final Codec<Integer> COLOR_CODEC = ExtraCodecs.STRING_RGB_COLOR.xmap(color -> color & 0xFFFFFF, color -> color);

    public static final Codec<InkStyle> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            COLOR_CODEC.fieldOf("color").forGetter(InkStyle::color),
            Identifier.CODEC.optionalFieldOf("type", NORMAL).forGetter(InkStyle::type)
    ).apply(instance, InkStyle::new));

    public static final StreamCodec<ByteBuf, InkStyle> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, InkStyle::color,
            Identifier.STREAM_CODEC, InkStyle::type,
            InkStyle::new
    );

    public InkStyle {
        color &= 0xFFFFFF;
    }

    public InkStyle(int color) {
        this(color, NORMAL);
    }
}
