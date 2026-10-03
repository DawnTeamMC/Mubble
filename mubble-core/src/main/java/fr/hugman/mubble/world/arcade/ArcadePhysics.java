package fr.hugman.mubble.world.arcade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * The physics block of a profile: everything the moves share.
 * <p>
 * Speeds are in blocks per tick, durations in ticks, at the usual 20 ticks per second.
 *
 * @param ground        locomotion on the ground
 * @param air           horizontal control in the air
 * @param gravity       vertical motion
 * @param slope         how stairs, slabs and step-downs are read as slopes
 * @param bounce        what blocks tagged {@code mubble:bounce} do
 * @param effects       how vanilla effects bend the physics
 * @param safetyCeiling the speed nothing may exceed, enforced again by the server validator
 */
public record ArcadePhysics(Ground ground, Air air, Gravity gravity, Slope slope, Bounce bounce, Effects effects, double safetyCeiling) {
    public static final Codec<ArcadePhysics> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Ground.CODEC.fieldOf("ground").forGetter(ArcadePhysics::ground),
            Air.CODEC.fieldOf("air").forGetter(ArcadePhysics::air),
            Gravity.CODEC.fieldOf("gravity").forGetter(ArcadePhysics::gravity),
            Slope.CODEC.fieldOf("slope").forGetter(ArcadePhysics::slope),
            Bounce.CODEC.fieldOf("bounce").forGetter(ArcadePhysics::bounce),
            Effects.CODEC.fieldOf("effects").forGetter(ArcadePhysics::effects),
            Codec.doubleRange(0.1D, 16.0D).fieldOf("safety_ceiling").forGetter(ArcadePhysics::safetyCeiling)
    ).apply(instance, ArcadePhysics::new));

    /**
     * @param walkSpeed          top speed when running is not available, or when an analog stick is
     *                           only tilted a little
     * @param runSpeed           top speed input can reach; the base value of {@code mubble:arcade_run_speed}
     * @param accelTicks         ticks from standing still to the run speed, on a normal block
     * @param decelTicks         ticks from the run speed to a stop once the stick is released
     * @param turnSpeed          how fast the run turns towards the stick at full speed, in degrees per tick
     * @param overCapDrag        what speed above the run speed keeps each tick on the ground
     * @param analogRunThreshold stick tilt above which an analog stick runs rather than walks
     * @param stickDeadzone      stick tilt below which the stick counts as released
     * @param minGrip            the least grip a slippery block leaves, as a share of the grip of a
     *                           plain block: blue ice still lets the stick steer that much
     */
    public record Ground(double walkSpeed, double runSpeed, double accelTicks, double decelTicks, double turnSpeed,
                         double overCapDrag, double analogRunThreshold, double stickDeadzone, double minGrip) {
        public static final Codec<Ground> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.doubleRange(0.0D, 4.0D).fieldOf("walk_speed").forGetter(Ground::walkSpeed),
                Codec.doubleRange(0.0D, 4.0D).fieldOf("run_speed").forGetter(Ground::runSpeed),
                Codec.doubleRange(1.0D, 200.0D).fieldOf("accel_ticks").forGetter(Ground::accelTicks),
                Codec.doubleRange(1.0D, 200.0D).fieldOf("decel_ticks").forGetter(Ground::decelTicks),
                Codec.doubleRange(0.0D, 180.0D).fieldOf("turn_speed").forGetter(Ground::turnSpeed),
                Codec.doubleRange(0.0D, 1.0D).fieldOf("over_cap_drag").forGetter(Ground::overCapDrag),
                Codec.doubleRange(0.0D, 1.0D).fieldOf("analog_run_threshold").forGetter(Ground::analogRunThreshold),
                Codec.doubleRange(0.0D, 1.0D).fieldOf("stick_deadzone").forGetter(Ground::stickDeadzone),
                Codec.doubleRange(0.0D, 1.0D).fieldOf("min_grip").forGetter(Ground::minGrip)
        ).apply(instance, Ground::new));
    }

    /**
     * @param drag       what horizontal speed keeps each tick in the air; the base value of
     *                   {@code mubble:arcade_air_drag}
     * @param control    how strongly the stick steers in the air; the base value of
     *                   {@code mubble:arcade_air_control}
     * @param accelTicks ticks from standing still to the run speed in the air, at full control
     * @param turnSpeed  how fast the velocity turns towards the stick, in degrees per tick at full control
     * @param brake      speed lost per tick while the stick points against the velocity
     */
    public record Air(double drag, double control, double accelTicks, double turnSpeed, double brake) {
        public static final Codec<Air> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.doubleRange(0.0D, 1.0D).fieldOf("drag").forGetter(Air::drag),
                Codec.doubleRange(0.0D, 4.0D).fieldOf("control").forGetter(Air::control),
                Codec.doubleRange(1.0D, 200.0D).fieldOf("accel_ticks").forGetter(Air::accelTicks),
                Codec.doubleRange(0.0D, 180.0D).fieldOf("turn_speed").forGetter(Air::turnSpeed),
                Codec.doubleRange(0.0D, 4.0D).fieldOf("brake").forGetter(Air::brake)
        ).apply(instance, Air::new));
    }

    /**
     * @param baseGravity          gravity of the states that are not a jump (falling off a ledge,
     *                             walking), at the default value of the vanilla gravity attribute
     * @param fallMultiplier       how much heavier gravity gets once going down
     * @param apexHangMultiplier   what is left of gravity just after the apex, as long as jump is held
     * @param apexHangThreshold    downward speed under which the apex hang applies
     * @param releaseCut           what upward speed keeps when jump is released early
     * @param terminalVelocity     the fastest fall
     * @param jumpHeightMultiplier scale of every jump height; the base value of
     *                             {@code mubble:arcade_jump_height}
     */
    public record Gravity(double baseGravity, double fallMultiplier, double apexHangMultiplier, double apexHangThreshold,
                          double releaseCut, double terminalVelocity, double jumpHeightMultiplier) {
        public static final Codec<Gravity> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.doubleRange(0.0D, 4.0D).fieldOf("base_gravity").forGetter(Gravity::baseGravity),
                Codec.doubleRange(0.0D, 16.0D).fieldOf("fall_multiplier").forGetter(Gravity::fallMultiplier),
                Codec.doubleRange(0.0D, 1.0D).fieldOf("apex_hang_multiplier").forGetter(Gravity::apexHangMultiplier),
                Codec.doubleRange(0.0D, 4.0D).fieldOf("apex_hang_threshold").forGetter(Gravity::apexHangThreshold),
                Codec.doubleRange(0.0D, 1.0D).fieldOf("release_cut").forGetter(Gravity::releaseCut),
                Codec.doubleRange(0.0D, 16.0D).fieldOf("terminal_velocity").forGetter(Gravity::terminalVelocity),
                Codec.doubleRange(0.0D, 16.0D).fieldOf("jump_height_multiplier").forGetter(Gravity::jumpHeightMultiplier)
        ).apply(instance, Gravity::new));
    }

    /**
     * @param windowTicks how many ticks the effective slope is measured over
     * @param snapDown    the deepest step-down the moves that hug the ground stay on, in blocks
     */
    public record Slope(int windowTicks, double snapDown) {
        public static final Codec<Slope> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.intRange(1, 10).fieldOf("window_ticks").forGetter(Slope::windowTicks),
                Codec.doubleRange(0.0D, 1.0D).fieldOf("snap_down").forGetter(Slope::snapDown)
        ).apply(instance, Slope::new));
    }

    /**
     * @param height           height of the bounce off a {@code mubble:bounce} block
     * @param ticksToApex      ticks it takes to reach it
     * @param groundPoundHeight height of the bounce when landing a ground pound on it
     * @param minFallSpeed     landings slower than this do not bounce
     */
    public record Bounce(double height, double ticksToApex, double groundPoundHeight, double minFallSpeed) {
        public static final Codec<Bounce> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.doubleRange(0.0D, 64.0D).fieldOf("height").forGetter(Bounce::height),
                Codec.doubleRange(1.0D, 100.0D).fieldOf("ticks_to_apex").forGetter(Bounce::ticksToApex),
                Codec.doubleRange(0.0D, 64.0D).fieldOf("ground_pound_height").forGetter(Bounce::groundPoundHeight),
                Codec.doubleRange(0.0D, 16.0D).fieldOf("min_fall_speed").forGetter(Bounce::minFallSpeed)
        ).apply(instance, Bounce::new));
    }

    /**
     * @param jumpBoostHeightPerLevel share of height every level of jump boost adds to a jump
     * @param slowFallingMaxFallSpeed the fastest fall under slow falling
     * @param levitationSpeedPerLevel upward speed every level of levitation aims for
     * @param levitationResponse      how fast the vertical speed reaches it, per tick
     */
    public record Effects(double jumpBoostHeightPerLevel, double slowFallingMaxFallSpeed, double levitationSpeedPerLevel, double levitationResponse) {
        public static final Codec<Effects> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.doubleRange(0.0D, 4.0D).fieldOf("jump_boost_height_per_level").forGetter(Effects::jumpBoostHeightPerLevel),
                Codec.doubleRange(0.0D, 4.0D).fieldOf("slow_falling_max_fall_speed").forGetter(Effects::slowFallingMaxFallSpeed),
                Codec.doubleRange(0.0D, 4.0D).fieldOf("levitation_speed_per_level").forGetter(Effects::levitationSpeedPerLevel),
                Codec.doubleRange(0.0D, 1.0D).fieldOf("levitation_response").forGetter(Effects::levitationResponse)
        ).apply(instance, Effects::new));
    }
}
