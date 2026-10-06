package fr.hugman.mubble.splatoon.world.item.weapon.param;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.codec.MubbleCodecs;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;

/**
 * How big a bullet is, as {@code spl__BulletSimpleCollisionParam} in Splatoon 3: one radius against terrain, another
 * against players, each growing from an initial to an end value. Radii are in units.
 *
 * @author Hugman
 * @since v4.0.0
 */
public record BulletCollisionParam(
        float initRadiusForField,
        float endRadiusForField,
        int changeFrameForField,
        float initRadiusForPlayer,
        float endRadiusForPlayer
) {
    public static final BulletCollisionParam DEFAULT = new BulletCollisionParam(0.2F, 0.2F, 0, 0.285F, 0.285F);

    public static final Codec<BulletCollisionParam> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MubbleCodecs.NONNEGATIVE_FLOAT.optionalFieldOf("init_radius_for_field", DEFAULT.initRadiusForField).forGetter(BulletCollisionParam::initRadiusForField),
            MubbleCodecs.NONNEGATIVE_FLOAT.optionalFieldOf("end_radius_for_field", DEFAULT.endRadiusForField).forGetter(BulletCollisionParam::endRadiusForField),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("change_frame_for_field", DEFAULT.changeFrameForField).forGetter(BulletCollisionParam::changeFrameForField),
            MubbleCodecs.NONNEGATIVE_FLOAT.optionalFieldOf("init_radius_for_player", DEFAULT.initRadiusForPlayer).forGetter(BulletCollisionParam::initRadiusForPlayer),
            MubbleCodecs.NONNEGATIVE_FLOAT.optionalFieldOf("end_radius_for_player", DEFAULT.endRadiusForPlayer).forGetter(BulletCollisionParam::endRadiusForPlayer)
    ).apply(instance, BulletCollisionParam::new));

    public static final StreamCodec<ByteBuf, BulletCollisionParam> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, BulletCollisionParam::initRadiusForField,
            ByteBufCodecs.FLOAT, BulletCollisionParam::endRadiusForField,
            ByteBufCodecs.VAR_INT, BulletCollisionParam::changeFrameForField,
            ByteBufCodecs.FLOAT, BulletCollisionParam::initRadiusForPlayer,
            ByteBufCodecs.FLOAT, BulletCollisionParam::endRadiusForPlayer,
            BulletCollisionParam::new
    );

    public static BulletCollisionParam of(float radiusForField, float radiusForPlayer) {
        return new BulletCollisionParam(radiusForField, radiusForField, 0, radiusForPlayer, radiusForPlayer);
    }

    public float radiusForField(int frame) {
        return this.changeFrameForField <= 0 ? this.endRadiusForField
                : Mth.lerp(Math.min(1.0F, (float) frame / this.changeFrameForField), this.initRadiusForField, this.endRadiusForField);
    }

    public float radiusForPlayer(int frame) {
        return this.changeFrameForField <= 0 ? this.endRadiusForPlayer
                : Mth.lerp(Math.min(1.0F, (float) frame / this.changeFrameForField), this.initRadiusForPlayer, this.endRadiusForPlayer);
    }
}
