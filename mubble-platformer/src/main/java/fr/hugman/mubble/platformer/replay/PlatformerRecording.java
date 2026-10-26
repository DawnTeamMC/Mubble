package fr.hugman.mubble.platformer.replay;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.platformer.sim.PlatformerInputFrame;
import fr.hugman.mubble.platformer.sim.MoveResult;
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
public record PlatformerRecording(Vec3 start, List<Step> steps) {
    public static final Codec<PlatformerInputFrame> FRAME_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("tick").forGetter(PlatformerInputFrame::tick),
            Codec.FLOAT.fieldOf("stick_x").forGetter(PlatformerInputFrame::stickX),
            Codec.FLOAT.fieldOf("stick_z").forGetter(PlatformerInputFrame::stickZ),
            Codec.FLOAT.fieldOf("view_yaw").forGetter(PlatformerInputFrame::viewYaw),
            Codec.BOOL.fieldOf("coupled").forGetter(PlatformerInputFrame::coupled),
            Codec.BYTE.fieldOf("held").forGetter(PlatformerInputFrame::held),
            Codec.BYTE.fieldOf("pressed").forGetter(PlatformerInputFrame::pressed),
            Codec.SHORT.fieldOf("jump_age_ms").forGetter(PlatformerInputFrame::jumpAgeMs),
            Codec.SHORT.fieldOf("action_age_ms").forGetter(PlatformerInputFrame::actionAgeMs)
    ).apply(instance, PlatformerInputFrame::new));

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
    public record Step(PlatformerInputFrame frame, Vec3 from, Optional<MoveResult> result, Optional<Long> fingerprint) {
        public static final Codec<Step> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                FRAME_CODEC.fieldOf("frame").forGetter(Step::frame),
                Vec3.CODEC.fieldOf("from").forGetter(Step::from),
                RESULT_CODEC.optionalFieldOf("result").forGetter(Step::result),
                Codec.LONG.optionalFieldOf("fingerprint").forGetter(Step::fingerprint)
        ).apply(instance, Step::new));
    }

    public static final Codec<PlatformerRecording> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Vec3.CODEC.fieldOf("start").forGetter(PlatformerRecording::start),
            Step.CODEC.listOf().fieldOf("steps").forGetter(PlatformerRecording::steps)
    ).apply(instance, PlatformerRecording::new));
}
