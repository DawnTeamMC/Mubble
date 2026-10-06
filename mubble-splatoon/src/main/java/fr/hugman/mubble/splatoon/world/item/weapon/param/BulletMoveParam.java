package fr.hugman.mubble.splatoon.world.item.weapon.param;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.codec.MubbleCodecs;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec3;

/**
 * How a bullet flies, as {@code spl__BulletSimpleMoveParam} in Splatoon 3. Speeds are in units per frame, gravities in
 * units per frame per frame, and resistances are the share of the speed lost each frame.
 *
 * <p>A bullet goes through three states:
 * <ol>
 *     <li><b>straight</b>, for {@code goStraightToBrakeStateFrame} frames: it keeps its spawn speed, without gravity;</li>
 *     <li><b>brake</b>, for {@code brakeToFreeStateFrame} frames: its speed is first capped to
 *     {@code goStraightStateEndMaxSpeed}, then each frame it loses {@code brakeAirResist} of it and falls by
 *     {@code brakeGravity};</li>
 *     <li><b>free</b>, until it lands: each frame it loses {@code freeAirResist} of its speed and falls by
 *     {@code freeGravity}.</li>
 * </ol>
 * Air resistance applies in all directions, before gravity.
 *
 * <p>The defaults are the ones of the game, which its parameter tables leave out.
 *
 * @author Hugman
 * @since v4.0.0
 */
public record BulletMoveParam(
        float spawnSpeed,
        int goStraightToBrakeStateFrame,
        float goStraightStateEndMaxSpeed,
        float brakeAirResist,
        float brakeGravity,
        int brakeToFreeStateFrame,
        float freeAirResist,
        float freeGravity
) {
    public static final int DEFAULT_GO_STRAIGHT_TO_BRAKE_STATE_FRAME = 10;
    public static final float DEFAULT_BRAKE_AIR_RESIST = 0.36F;
    public static final float DEFAULT_BRAKE_GRAVITY = 0.07F;
    public static final int DEFAULT_BRAKE_TO_FREE_STATE_FRAME = 4;
    public static final float DEFAULT_FREE_AIR_RESIST = 0.02F;
    public static final float DEFAULT_FREE_GRAVITY = 0.016F;

    public static final Codec<BulletMoveParam> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("spawn_speed").forGetter(BulletMoveParam::spawnSpeed),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("go_straight_to_brake_state_frame", DEFAULT_GO_STRAIGHT_TO_BRAKE_STATE_FRAME).forGetter(BulletMoveParam::goStraightToBrakeStateFrame),
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("go_straight_state_end_max_speed").forGetter(BulletMoveParam::goStraightStateEndMaxSpeed),
            Codec.floatRange(0.0F, 1.0F).optionalFieldOf("brake_air_resist", DEFAULT_BRAKE_AIR_RESIST).forGetter(BulletMoveParam::brakeAirResist),
            Codec.FLOAT.optionalFieldOf("brake_gravity", DEFAULT_BRAKE_GRAVITY).forGetter(BulletMoveParam::brakeGravity),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("brake_to_free_state_frame", DEFAULT_BRAKE_TO_FREE_STATE_FRAME).forGetter(BulletMoveParam::brakeToFreeStateFrame),
            Codec.floatRange(0.0F, 1.0F).optionalFieldOf("free_air_resist", DEFAULT_FREE_AIR_RESIST).forGetter(BulletMoveParam::freeAirResist),
            Codec.FLOAT.optionalFieldOf("free_gravity", DEFAULT_FREE_GRAVITY).forGetter(BulletMoveParam::freeGravity)
    ).apply(instance, BulletMoveParam::new));

    public static final StreamCodec<ByteBuf, BulletMoveParam> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, BulletMoveParam::spawnSpeed,
            ByteBufCodecs.VAR_INT, BulletMoveParam::goStraightToBrakeStateFrame,
            ByteBufCodecs.FLOAT, BulletMoveParam::goStraightStateEndMaxSpeed,
            ByteBufCodecs.FLOAT, BulletMoveParam::brakeAirResist,
            ByteBufCodecs.FLOAT, BulletMoveParam::brakeGravity,
            ByteBufCodecs.VAR_INT, BulletMoveParam::brakeToFreeStateFrame,
            ByteBufCodecs.FLOAT, BulletMoveParam::freeAirResist,
            ByteBufCodecs.FLOAT, BulletMoveParam::freeGravity,
            BulletMoveParam::new
    );

    /**
     * Only sets what the parameter tables of the game set, leaving the rest to the defaults.
     */
    public static BulletMoveParam of(float spawnSpeed, int goStraightToBrakeStateFrame, float goStraightStateEndMaxSpeed, float freeGravity) {
        return new BulletMoveParam(spawnSpeed, goStraightToBrakeStateFrame, goStraightStateEndMaxSpeed,
                DEFAULT_BRAKE_AIR_RESIST, DEFAULT_BRAKE_GRAVITY, DEFAULT_BRAKE_TO_FREE_STATE_FRAME, DEFAULT_FREE_AIR_RESIST, freeGravity);
    }

    /**
     * @param velocity the velocity of the previous frame, in units per frame
     * @param frame    the frame coming up, counted from 0 for the first one
     * @return the velocity of that frame
     */
    public Vec3 nextVelocity(Vec3 velocity, int frame) {
        if (frame < this.goStraightToBrakeStateFrame) {
            return velocity;
        }
        if (frame == this.goStraightToBrakeStateFrame && velocity.length() > this.goStraightStateEndMaxSpeed) {
            velocity = velocity.normalize().scale(this.goStraightStateEndMaxSpeed);
        }
        return switch (this.state(frame)) {
            case STRAIGHT -> velocity;
            case BRAKE -> velocity.scale(1.0 - this.brakeAirResist).subtract(0, this.brakeGravity, 0);
            case FREE -> velocity.scale(1.0 - this.freeAirResist).subtract(0, this.freeGravity, 0);
        };
    }

    public BulletState state(int frame) {
        if (frame < this.goStraightToBrakeStateFrame) {
            return BulletState.STRAIGHT;
        }
        return frame < this.goStraightToBrakeStateFrame + this.brakeToFreeStateFrame ? BulletState.BRAKE : BulletState.FREE;
    }

    public enum BulletState {
        STRAIGHT,
        BRAKE,
        FREE
    }
}
