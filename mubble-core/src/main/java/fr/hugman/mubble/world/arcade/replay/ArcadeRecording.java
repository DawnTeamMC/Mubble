package fr.hugman.mubble.world.arcade.replay;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.world.arcade.sim.ArcadeInputFrame;
import fr.hugman.mubble.world.arcade.sim.MoveResult;
import java.util.List;
import java.util.Optional;
import net.minecraft.world.phys.Vec3;

/**
 * A sequence of input frames, with what the world did with each step, as recorded from a player.
 * <p>
 * Played back through the client path, the inputs alone rebuild the trajectory; played back through
 * the server path, the recorded results stand in for the client. Both have to agree, step for step,
 * which is the whole point of sharing the simulation.
 *
 * @param start the position the recording starts from, relative to wherever it is played back
 * @param steps the recorded steps
 */
public record ArcadeRecording(Vec3 start, List<Step> steps) {
    public static final Codec<ArcadeInputFrame> FRAME_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("tick").forGetter(ArcadeInputFrame::tick),
            Codec.FLOAT.fieldOf("stick_x").forGetter(ArcadeInputFrame::stickX),
            Codec.FLOAT.fieldOf("stick_z").forGetter(ArcadeInputFrame::stickZ),
            Codec.FLOAT.fieldOf("view_yaw").forGetter(ArcadeInputFrame::viewYaw),
            Codec.BOOL.fieldOf("coupled").forGetter(ArcadeInputFrame::coupled),
            Codec.BYTE.fieldOf("held").forGetter(ArcadeInputFrame::held),
            Codec.BYTE.fieldOf("pressed").forGetter(ArcadeInputFrame::pressed),
            Codec.SHORT.fieldOf("jump_age_ms").forGetter(ArcadeInputFrame::jumpAgeMs),
            Codec.SHORT.fieldOf("action_age_ms").forGetter(ArcadeInputFrame::actionAgeMs)
    ).apply(instance, ArcadeInputFrame::new));

    public static final Codec<MoveResult> RESULT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("dx").forGetter(MoveResult::dx),
            Codec.DOUBLE.fieldOf("dy").forGetter(MoveResult::dy),
            Codec.DOUBLE.fieldOf("dz").forGetter(MoveResult::dz),
            Codec.BOOL.fieldOf("on_ground").forGetter(MoveResult::onGround),
            Codec.BOOL.fieldOf("horizontal_collision").forGetter(MoveResult::horizontalCollision),
            Codec.BOOL.fieldOf("vertical_collision").forGetter(MoveResult::verticalCollision)
    ).apply(instance, MoveResult::new));

    /**
     * @param frame       the input of the step
     * @param from        where the step started, relative to the start of the recording
     * @param result      what the world did with the step, if it was recorded
     * @param fingerprint the fingerprint of the state after the step, if it was recorded
     */
    public record Step(ArcadeInputFrame frame, Vec3 from, Optional<MoveResult> result, Optional<Long> fingerprint) {
        public static final Codec<Step> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                FRAME_CODEC.fieldOf("frame").forGetter(Step::frame),
                Vec3.CODEC.fieldOf("from").forGetter(Step::from),
                RESULT_CODEC.optionalFieldOf("result").forGetter(Step::result),
                Codec.LONG.optionalFieldOf("fingerprint").forGetter(Step::fingerprint)
        ).apply(instance, Step::new));
    }

    public static final Codec<ArcadeRecording> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Vec3.CODEC.fieldOf("start").forGetter(ArcadeRecording::start),
            Step.CODEC.listOf().fieldOf("steps").forGetter(ArcadeRecording::steps)
    ).apply(instance, ArcadeRecording::new));

    /** A recording of inputs only, for the client path to play. */
    public static ArcadeRecording ofInputs(List<ArcadeInputFrame> frames) {
        return new ArcadeRecording(Vec3.ZERO, frames.stream().map(frame -> new Step(frame, Vec3.ZERO, Optional.empty(), Optional.empty())).toList());
    }
}
