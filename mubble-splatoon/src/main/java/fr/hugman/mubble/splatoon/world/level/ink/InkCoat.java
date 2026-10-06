package fr.hugman.mubble.splatoon.world.level.ink;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * Ink covering a whole block rather than its faces, for the plants and other small things that have no surface to
 * paint but should still look inked: grass, flowers, vines... They are drawn in the color of their ink, over their
 * own texture.
 *
 * @param style     the ink covering the block
 * @param paintedAt when the block was last inked, which only the server keeps track of
 * @author Hugman
 * @since v4.0.0
 */
public record InkCoat(InkStyle style, long paintedAt) {
    public static final MapCodec<InkCoat> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            InkStyle.CODEC.fieldOf("style").forGetter(InkCoat::style),
            Codec.LONG.optionalFieldOf("painted_at", 0L).forGetter(InkCoat::paintedAt)
    ).apply(instance, InkCoat::new));

    public static final StreamCodec<ByteBuf, InkCoat> STREAM_CODEC = InkStyle.STREAM_CODEC.map(style -> new InkCoat(style, 0L), InkCoat::style);
}
