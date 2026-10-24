package fr.hugman.mubble.splatoon.world.item.weapon.param;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.codec.MubbleCodecs;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;

/**
 * The splat a bullet leaves where it lands, as {@code spl__BulletShooterPaintParam} in Splatoon 3. Lengths are in
 * units.
 *
 * <p>The splat is an ellipse, stretched in the direction the bullet was going:
 * <ul>
 *     <li>its half width depends on how far the bullet flew: {@code widthHalfNear} at the muzzle,
 *     {@code widthHalfMiddle} at {@code distanceMiddle}, and {@code widthHalfFar} from {@code distanceFar} on;</li>
 *     <li>its half length is its half width times a depth scale, from {@code depthScaleMin} for a bullet landing
 *     head-on to {@code depthScaleMax} for one grazing the surface. Bullets that already broke free use the
 *     {@code BreakFree} scales instead.</li>
 * </ul>
 *
 * <p>The game's tables leave out {@code distanceFar}, so it defaults to {@value #DEFAULT_DISTANCE_FAR}, about where
 * shooters stop flying.
 *
 * @author Hugman
 * @since v4.0.0
 */
public record BulletPaintParam(
        float widthHalfNear,
        float widthHalfMiddle,
        float widthHalfFar,
        float distanceMiddle,
        float distanceFar,
        float depthScaleMin,
        float depthScaleMax,
        float depthScaleMinBreakFree,
        float depthScaleMaxBreakFree
) {
    public static final float DEFAULT_DISTANCE_FAR = 12.0F;

    /**
     * Impacts closer to the surface than this angle, in degrees, get the most stretched splat.
     */
    public static final float GRAZING_ANGLE = 15.0F;
    /**
     * Impacts steeper than this angle, in degrees, get the least stretched splat.
     */
    public static final float HEAD_ON_ANGLE = 75.0F;

    public static final Codec<BulletPaintParam> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("width_half_near").forGetter(BulletPaintParam::widthHalfNear),
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("width_half_middle").forGetter(BulletPaintParam::widthHalfMiddle),
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("width_half_far").forGetter(BulletPaintParam::widthHalfFar),
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("distance_middle").forGetter(BulletPaintParam::distanceMiddle),
            MubbleCodecs.NONNEGATIVE_FLOAT.optionalFieldOf("distance_far", DEFAULT_DISTANCE_FAR).forGetter(BulletPaintParam::distanceFar),
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("depth_scale_min").forGetter(BulletPaintParam::depthScaleMin),
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("depth_scale_max").forGetter(BulletPaintParam::depthScaleMax),
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("depth_scale_min_break_free").forGetter(BulletPaintParam::depthScaleMinBreakFree),
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("depth_scale_max_break_free").forGetter(BulletPaintParam::depthScaleMaxBreakFree)
    ).apply(instance, BulletPaintParam::new));

    public static final StreamCodec<ByteBuf, BulletPaintParam> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, BulletPaintParam::widthHalfNear,
            ByteBufCodecs.FLOAT, BulletPaintParam::widthHalfMiddle,
            ByteBufCodecs.FLOAT, BulletPaintParam::widthHalfFar,
            ByteBufCodecs.FLOAT, BulletPaintParam::distanceMiddle,
            ByteBufCodecs.FLOAT, BulletPaintParam::distanceFar,
            ByteBufCodecs.FLOAT, BulletPaintParam::depthScaleMin,
            ByteBufCodecs.FLOAT, BulletPaintParam::depthScaleMax,
            ByteBufCodecs.FLOAT, BulletPaintParam::depthScaleMinBreakFree,
            ByteBufCodecs.FLOAT, BulletPaintParam::depthScaleMaxBreakFree,
            BulletPaintParam::new
    );

    public static BulletPaintParam of(float widthHalfNear, float widthHalfMiddle, float widthHalfFar, float distanceMiddle,
                                      float depthScaleMin, float depthScaleMax, float depthScaleMinBreakFree, float depthScaleMaxBreakFree) {
        return new BulletPaintParam(widthHalfNear, widthHalfMiddle, widthHalfFar, distanceMiddle, DEFAULT_DISTANCE_FAR,
                depthScaleMin, depthScaleMax, depthScaleMinBreakFree, depthScaleMaxBreakFree);
    }

    /**
     * @param distance how far the bullet flew, in units
     * @return the half width of the splat, in units
     */
    public float widthHalf(double distance) {
        if (distance <= this.distanceMiddle) {
            return this.distanceMiddle <= 0 ? this.widthHalfMiddle
                    : (float) Mth.lerp(distance / this.distanceMiddle, this.widthHalfNear, this.widthHalfMiddle);
        }
        if (distance >= this.distanceFar || this.distanceFar <= this.distanceMiddle) {
            return this.widthHalfFar;
        }
        return (float) Mth.lerp((distance - this.distanceMiddle) / (this.distanceFar - this.distanceMiddle), this.widthHalfMiddle, this.widthHalfFar);
    }

    /**
     * @param sinAngle  the sine of the angle between the path of the bullet and the surface it landed on
     * @param brokeFree whether the bullet was past its straight state
     * @return how many times longer than wide the splat is
     */
    public float depthScale(double sinAngle, boolean brokeFree) {
        float min = brokeFree ? this.depthScaleMinBreakFree : this.depthScaleMin;
        float max = brokeFree ? this.depthScaleMaxBreakFree : this.depthScaleMax;
        double grazing = Math.sin(Math.toRadians(GRAZING_ANGLE));
        double headOn = Math.sin(Math.toRadians(HEAD_ON_ANGLE));
        double progress = Mth.clamp((Math.abs(sinAngle) - grazing) / (headOn - grazing), 0.0, 1.0);
        return (float) Mth.lerp(progress, max, min);
    }
}
