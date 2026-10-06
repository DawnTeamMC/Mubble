package fr.hugman.mubble.splatoon.world.item.weapon.param;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.codec.MubbleCodecs;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;

/**
 * The splats of the droplets a bullet lets fall, as {@code spl__BulletSplashShooterPaintParam} in Splatoon 3. Lengths
 * are in units.
 *
 * <p>A droplet paints {@code widthHalf} around where it lands, or {@code widthHalfNearest} for the one dropped in
 * front of the shooter. Its splat is stretched in the direction of the shot, by the bullet's
 * {@link BulletPaintParam#depthScaleMax() maximum depth scale} when it falls from {@code depthMaxDropHeight} or lower,
 * down to the {@link BulletPaintParam#depthScaleMin() minimum one} from {@code depthMinDropHeight} or higher.
 *
 * @author Hugman
 * @since v4.0.0
 */
public record SplashPaintParam(float widthHalf, float widthHalfNearest, float depthMaxDropHeight, float depthMinDropHeight) {
    public static final Codec<SplashPaintParam> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("width_half").forGetter(SplashPaintParam::widthHalf),
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("width_half_nearest").forGetter(SplashPaintParam::widthHalfNearest),
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("depth_max_drop_height").forGetter(SplashPaintParam::depthMaxDropHeight),
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("depth_min_drop_height").forGetter(SplashPaintParam::depthMinDropHeight)
    ).apply(instance, SplashPaintParam::new));

    public static final StreamCodec<ByteBuf, SplashPaintParam> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, SplashPaintParam::widthHalf,
            ByteBufCodecs.FLOAT, SplashPaintParam::widthHalfNearest,
            ByteBufCodecs.FLOAT, SplashPaintParam::depthMaxDropHeight,
            ByteBufCodecs.FLOAT, SplashPaintParam::depthMinDropHeight,
            SplashPaintParam::new
    );

    /**
     * @param dropHeight how far the droplet fell, in units
     * @return how many times longer than wide its splat is
     */
    public float depthScale(double dropHeight, BulletPaintParam paint) {
        if (this.depthMinDropHeight <= this.depthMaxDropHeight) {
            return paint.depthScaleMax();
        }
        double progress = Mth.clamp((dropHeight - this.depthMaxDropHeight) / (this.depthMinDropHeight - this.depthMaxDropHeight), 0.0, 1.0);
        return (float) Mth.lerp(progress, paint.depthScaleMax(), paint.depthScaleMin());
    }
}
