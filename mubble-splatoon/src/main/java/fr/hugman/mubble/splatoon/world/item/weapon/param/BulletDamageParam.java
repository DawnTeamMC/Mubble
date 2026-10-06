package fr.hugman.mubble.splatoon.world.item.weapon.param;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.splatoon.SplatoonConversions;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;

/**
 * How hard a bullet hits, as {@code spl__BulletShooterDamageParam} in Splatoon 3. Values are in the game's damage
 * units (1000 for a full health bar), and fall off linearly from {@code valueMax} to {@code valueMin} between the
 * {@code reduceStartFrame}th and the {@code reduceEndFrame}th frame of flight.
 *
 * @author Hugman
 * @since v4.0.0
 */
public record BulletDamageParam(int valueMax, int valueMin, int reduceStartFrame, int reduceEndFrame) {
    public static final Codec<BulletDamageParam> CODEC = RecordCodecBuilder.<BulletDamageParam>create(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("value_max").forGetter(BulletDamageParam::valueMax),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("value_min").forGetter(BulletDamageParam::valueMin),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("reduce_start_frame").forGetter(BulletDamageParam::reduceStartFrame),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("reduce_end_frame").forGetter(BulletDamageParam::reduceEndFrame)
    ).apply(instance, BulletDamageParam::new)).validate(param -> param.reduceEndFrame() < param.reduceStartFrame()
            ? DataResult.error(() -> "The damage cannot stop falling off before it starts to")
            : DataResult.success(param));

    public static final StreamCodec<ByteBuf, BulletDamageParam> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, BulletDamageParam::valueMax,
            ByteBufCodecs.VAR_INT, BulletDamageParam::valueMin,
            ByteBufCodecs.VAR_INT, BulletDamageParam::reduceStartFrame,
            ByteBufCodecs.VAR_INT, BulletDamageParam::reduceEndFrame,
            BulletDamageParam::new
    );

    /**
     * @return the damage, in Minecraft health points, of a bullet that has flown for the given number of frames
     */
    public float damage(int frame) {
        float value;
        if (frame <= this.reduceStartFrame) {
            value = this.valueMax;
        } else if (frame >= this.reduceEndFrame) {
            value = this.valueMin;
        } else {
            float progress = (float) (frame - this.reduceStartFrame) / (this.reduceEndFrame - this.reduceStartFrame);
            value = Mth.lerp(progress, this.valueMax, this.valueMin);
        }
        return SplatoonConversions.damage(value);
    }
}
