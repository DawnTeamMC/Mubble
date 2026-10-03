package fr.hugman.mubble.world.arcade.sim;

import fr.hugman.mubble.tags.MubbleBlockTags;
import fr.hugman.mubble.world.arcade.ArcadeGrace;
import fr.hugman.mubble.world.arcade.ArcadePhysics;
import fr.hugman.mubble.world.arcade.ArcadeProfile;
import fr.hugman.mubble.world.arcade.cue.CueEvent;
import fr.hugman.mubble.world.arcade.move.ArcadeMove;
import fr.hugman.mubble.world.arcade.move.ArcadeMoves;
import fr.hugman.mubble.world.arcade.move.MoveParam;
import fr.hugman.mubble.world.arcade.move.MoveSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * One step of the simulation, as the moves see it: the state, the input, the tuning and the world,
 * along with the physics every move shares.
 * <p>
 * A step is planned first ({@link ArcadeSimulation#plan}), which decides the move and the
 * displacement, then settled ({@link ArcadeSimulation#settle}) once the world reported where the
 * player actually ended up. Everything here is a pure function of what the context was built from:
 * angles go through the lookup tables of {@link Mth}, never through {@link Math#sin}, whose result is
 * allowed to differ from one JVM, or one JIT tier, to another.
 */
public final class MoveContext {
    private static final float DEGREES_TO_RADIANS = Mth.DEG_TO_RAD;
    private static final double EPSILON = 1.0E-7D;
    /** Friction of a plain block, against which the grip of every other block is measured. */
    private static final double PLAIN_FRICTION = 0.6D;
    /** How long one tick lasts, which is what an input buffer loses every tick. */
    public static final int TICK_MS = 50;

    private final ArcadeState state;
    private final ArcadeInputFrame input;
    private final ArcadeTuning tuning;
    private final ArcadeWorld world;
    private final Vec3 start;
    private final ArcadePhysics physics;
    private final ArcadeGrace grace;

    private final double stickX;
    private final double stickZ;
    private final double stickMagnitude;
    private final boolean jumpPressed;
    private final boolean jumpReleased;
    private final boolean crouchPressed;
    private final boolean actionPressed;
    private final boolean spinPressed;

    private double firstX;
    private double firstY;
    private double firstZ;
    private double dx;
    private double dy;
    private double dz;
    private boolean verticalDecided;
    private boolean horizontalDecided;
    /** Whether the move decided the displacement itself, rather than leaving it to the velocity. */
    private boolean displacementFixed;
    private Pose pose = Pose.STANDING;

    @Nullable
    private MoveResult result;

    MoveContext(ArcadeState state, ArcadeInputFrame input, ArcadeTuning tuning, ArcadeWorld world, Vec3 start) {
        this.state = state;
        this.input = input;
        this.tuning = tuning;
        this.world = world;
        this.start = start;
        this.physics = tuning.profile().physics();
        this.grace = tuning.profile().grace();

        double magnitude = input.stickMagnitude();
        if (magnitude < this.physics.ground().stickDeadzone()) {
            this.stickX = 0.0D;
            this.stickZ = 0.0D;
            this.stickMagnitude = 0.0D;
        } else {
            this.stickX = input.stickX();
            this.stickZ = input.stickZ();
            this.stickMagnitude = Math.min(1.0D, magnitude);
        }

        boolean jumpHeld = input.isHeld(ArcadeInputFrame.JUMP);
        this.jumpPressed = input.wasPressed(ArcadeInputFrame.JUMP);
        // a press and release within the same tick is a release too
        this.jumpReleased = (state.jumpHeld || this.jumpPressed) && !jumpHeld;
        this.crouchPressed = input.wasPressed(ArcadeInputFrame.CROUCH);
        this.actionPressed = input.wasPressed(ArcadeInputFrame.ACTION);
        this.spinPressed = input.wasPressed(ArcadeInputFrame.SPIN);
    }

    // -- accessors ------------------------------------------------------------------------------

    public ArcadeState state() {
        return this.state;
    }

    public ArcadeInputFrame input() {
        return this.input;
    }

    public ArcadeTuning tuning() {
        return this.tuning;
    }

    public ArcadeWorld world() {
        return this.world;
    }

    public ArcadeProfile profile() {
        return this.tuning.profile();
    }

    public ArcadePhysics physics() {
        return this.physics;
    }

    public ArcadeGrace grace() {
        return this.grace;
    }

    /** Where the player stood when the step began. */
    public Vec3 start() {
        return this.start;
    }

    public MoveSettings settings(ArcadeMove move) {
        return this.profile().settingsOrDefault(move);
    }

    public double param(ArcadeMove move, MoveParam param) {
        return this.settings(move).get(param);
    }

    public boolean allowed(ArcadeMove move) {
        return move.isBase() || this.tuning.allowed().test(move);
    }

    /** Ticks an airborne move lasts before the aerial follow-ups may cut it short. */
    public int cancelWindow(ArcadeMove move) {
        return move.param(ArcadeMoves.CANCEL_WINDOW.name()).map(param -> this.settings(move).ticks(param)).orElse(0);
    }

    /** What the world did with the planned displacement. Only available while settling. */
    public MoveResult result() {
        if (this.result == null) {
            throw new IllegalStateException("The step is not settled yet");
        }
        return this.result;
    }

    void setResult(MoveResult result) {
        this.result = result;
    }

    // -- input ----------------------------------------------------------------------------------

    public boolean hasStick() {
        return this.stickMagnitude > 0.0D;
    }

    public double stickMagnitude() {
        return this.stickMagnitude;
    }

    public double stickX() {
        return this.stickX;
    }

    public double stickZ() {
        return this.stickZ;
    }

    public float stickYaw() {
        return yawOf(this.stickX, this.stickZ);
    }

    /** How much the stick points along the horizontal direction ({@code x}, {@code z}), from -1 to 1. */
    public double stickAlong(double x, double z) {
        if (!this.hasStick()) {
            return 0.0D;
        }
        double length = Math.sqrt(x * x + z * z);
        return length < EPSILON ? 0.0D : (this.stickX * x + this.stickZ * z) / (this.stickMagnitude * length);
    }

    public boolean jumpPressed() {
        return this.jumpPressed;
    }

    public boolean jumpReleased() {
        return this.jumpReleased;
    }

    public boolean jumpHeld() {
        return this.state.jumpHeld;
    }

    public boolean crouchPressed() {
        return this.crouchPressed;
    }

    public boolean crouchHeld() {
        return this.state.crouchHeld;
    }

    public boolean actionPressed() {
        return this.actionPressed;
    }

    public boolean actionHeld() {
        return this.state.actionHeld;
    }

    public boolean spinPressed() {
        return this.spinPressed;
    }

    public boolean sprintHeld() {
        return this.input.isHeld(ArcadeInputFrame.SPRINT);
    }

    /** Whether a jump press is still waiting to be used, see {@link ArcadeGrace#jumpBufferMs()}. */
    public boolean jumpBuffered() {
        return this.state.jumpBufferMs > 0;
    }

    public void consumeJump() {
        this.state.jumpBufferMs = 0;
    }

    public boolean actionBuffered() {
        return this.state.actionBufferMs > 0;
    }

    public void consumeAction() {
        this.state.actionBufferMs = 0;
    }

    /** Whether a jump may start from here: on the ground, or still within coyote time. */
    public boolean canJumpFromHere() {
        return this.state.grounded || this.state.coyote > 0;
    }

    /** Whether the stick was spun around fast enough to ask for a spin, and has not asked for one since. */
    public boolean spinGesture() {
        return this.state.spinWindow > 0 && Math.abs(this.state.spinAccum) >= 360.0F;
    }

    public void consumeSpinGesture() {
        this.state.spinAccum = 0.0F;
        this.state.spinWindow = 0;
    }

    // -- the start of the tick ------------------------------------------------------------------

    /** Reads the input of the tick into the state: held keys, buffered presses, the spin gesture. */
    void beginTick(int spinWindowTicks) {
        var s = this.state;
        if (this.jumpPressed) {
            s.jumpBufferMs = Math.max(s.jumpBufferMs - TICK_MS, this.grace.jumpBufferMs() - this.input.jumpAgeMs());
        } else {
            s.jumpBufferMs = Math.max(0, s.jumpBufferMs - TICK_MS);
        }
        if (this.actionPressed) {
            s.actionBufferMs = Math.max(s.actionBufferMs - TICK_MS, this.grace.actionBufferMs() - this.input.actionAgeMs());
        } else {
            s.actionBufferMs = Math.max(0, s.actionBufferMs - TICK_MS);
        }
        s.jumpHeld = this.input.isHeld(ArcadeInputFrame.JUMP);
        s.crouchHeld = this.input.isHeld(ArcadeInputFrame.CROUCH);
        s.actionHeld = this.input.isHeld(ArcadeInputFrame.ACTION);

        // the spin gesture: the stick turned all the way around within a short window
        if (this.hasStick() && this.stickMagnitude > 0.5D) {
            float angle = this.stickYaw();
            if (s.spinWindow > 0) {
                s.spinAccum += Mth.degreesDifference(s.stickAngle, angle);
            } else {
                s.spinAccum = 0.0F;
            }
            s.stickAngle = angle;
            s.spinWindow = spinWindowTicks;
        } else if (s.spinWindow > 0) {
            s.spinWindow--;
            if (s.spinWindow == 0) {
                s.spinAccum = 0.0F;
            }
        }

        if (this.input.coupled()) {
            s.facing = this.input.viewYaw();
        }
    }

    /** Counts the timers down, once the move of the tick is decided. */
    void endPlan() {
        var s = this.state;
        if (s.coyote > 0) {
            s.coyote--;
        }
        if (s.chainWindow > 0) {
            s.chainWindow--;
            if (s.chainWindow == 0) {
                s.chainIndex = 0;
            }
        }
        if (s.wallLeniency > 0) {
            s.wallLeniency--;
        }
        if (s.controlLock > 0) {
            s.controlLock--;
        }
        if (s.ledgeRegrab > 0) {
            s.ledgeRegrab--;
        }
        if (s.rollBoostCooldown > 0) {
            s.rollBoostCooldown--;
        }
    }

    // -- transitions ----------------------------------------------------------------------------

    /** Leaves the current move for {@code next}. */
    public void switchTo(ArcadeMove next) {
        var s = this.state;
        s.move = next;
        s.moveTicks = 0;
        s.moveSeq++;
        this.verticalDecided = false;
        this.horizontalDecided = false;
        next.enter(this);
        this.emit(CueEvent.START, s.horizontalSpeed());
    }

    public void emit(CueEvent type, double intensity) {
        this.state.events.add(new MoveEvent(type, this.state.move, intensity));
    }

    /** The move a landing goes into: a roll when one is asked for, the landing state otherwise. */
    public ArcadeMove landingMove() {
        if (this.state.crouchHeld && this.actionBuffered() && this.allowed(ArcadeMoves.ROLL)) {
            this.consumeAction();
            this.state.negateFallDamage = true;
            return ArcadeMoves.ROLL;
        }
        return ArcadeMoves.LAND;
    }

    /** The plain move of the ground: running when it is available, walking otherwise. */
    public ArcadeMove baseGroundMove() {
        if (this.state.crouchHeld && this.allowed(ArcadeMoves.CROUCH)) {
            return ArcadeMoves.CROUCH;
        }
        return this.allowed(ArcadeMoves.RUN) ? ArcadeMoves.RUN : ArcadeMoves.WALK;
    }

    // -- motion ---------------------------------------------------------------------------------

    public double horizontalSpeed() {
        return this.state.horizontalSpeed();
    }

    /** The direction of the horizontal velocity, or the facing when standing still. */
    public float velocityYaw() {
        return this.state.horizontalSpeed() < EPSILON ? this.state.facing : yawOf(this.state.vx, this.state.vz);
    }

    public void setHorizontal(float yaw, double speed) {
        this.state.vx = directionX(yaw) * speed;
        this.state.vz = directionZ(yaw) * speed;
        this.horizontalDecided = true;
    }

    public void setVelocity(double vx, double vy, double vz) {
        this.state.vx = vx;
        this.state.vy = vy;
        this.state.vz = vz;
    }

    /** Turns the facing towards {@code yaw} by at most {@code maxTurn} degrees. Ignored while the body follows the view. */
    public void turnFacing(float yaw, double maxTurn) {
        if (!this.input.coupled()) {
            this.state.facing = Mth.wrapDegrees(Mth.approachDegrees(this.state.facing, yaw, (float) maxTurn));
        }
    }

    public void faceYaw(float yaw) {
        if (!this.input.coupled()) {
            this.state.facing = Mth.wrapDegrees(yaw);
        }
    }

    /** How well the block underfoot lets the player grip it: 1 on a plain block, close to 0 on ice. */
    public double grip() {
        double friction = this.world.friction(this.start);
        double grip = (1.0D - friction) / (1.0D - PLAIN_FRICTION);
        return Mth.clamp(grip, this.physics.ground().minGrip(), 1.0D);
    }

    public boolean onMomentumBlock() {
        return this.world.is(this.world.supportingPos(this.start), MubbleBlockTags.KEEPS_MOMENTUM);
    }

    /** The speed a ground move aims for under the stick: walking when it is barely tilted, running at full tilt. */
    public double stickTargetSpeed(boolean canRun) {
        if (!this.hasStick()) {
            return 0.0D;
        }
        double walk = this.tuning.walkSpeed();
        if (!canRun) {
            return walk * this.stickMagnitude;
        }
        double threshold = this.physics.ground().analogRunThreshold();
        if (this.stickMagnitude < threshold) {
            return walk * (this.stickMagnitude / Math.max(threshold, EPSILON));
        }
        double t = threshold >= 1.0D ? 1.0D : (this.stickMagnitude - threshold) / (1.0D - threshold);
        return Mth.lerp(t, walk, this.tuning.effectiveRunSpeed());
    }

    /**
     * Ground locomotion: accelerates towards {@code targetSpeed} along the stick, brakes when it is
     * released, and lets speed above the run speed decay on its own rather than cutting it.
     * <p>
     * Everything is scaled by how well the block grips (ice stays ice) and by its speed factor (soul
     * sand stays slow), and blocks tagged {@code mubble:keeps_momentum} neither brake nor drag.
     */
    public void groundMotion(double targetSpeed) {
        var ground = this.physics.ground();
        double grip = this.grip();
        boolean keepsMomentum = this.onMomentumBlock();
        double factor = this.world.speedFactor(this.start);
        double runSpeed = this.tuning.effectiveRunSpeed();
        double cap = runSpeed * factor;
        double target = Math.min(targetSpeed * factor, cap);
        double accel = runSpeed / ground.accelTicks() * grip;
        double decel = runSpeed / ground.decelTicks() * grip;
        double overCap = 1.0D - (1.0D - ground.overCapDrag()) * grip;

        double speed = this.state.horizontalSpeed();
        float yaw = this.velocityYaw();
        if (this.hasStick()) {
            float stickYaw = this.stickYaw();
            if (speed <= accel) {
                yaw = stickYaw;
            } else {
                double t = Mth.clamp(speed / Math.max(runSpeed, EPSILON), 0.0D, 1.0D);
                double maxTurn = Mth.lerp(t, 180.0D, ground.turnSpeed()) * grip;
                yaw = Mth.approachDegrees(yaw, stickYaw, (float) maxTurn);
            }
            if (speed > cap) {
                speed = keepsMomentum ? speed : Math.max(cap, speed * overCap);
            } else if (this.sprintHeld() && target >= cap - EPSILON) {
                // the sprint key is the keyboard's way of being at full speed right away
                speed = target;
            } else if (speed < target) {
                speed = Math.min(target, speed + accel);
            } else {
                speed = Math.max(target, speed - decel);
            }
        } else if (!keepsMomentum) {
            speed = speed > cap ? Math.max(0.0D, speed * overCap - decel) : Math.max(0.0D, speed - decel);
        }
        this.setHorizontal(yaw, speed);
        if (speed > EPSILON) {
            this.turnFacing(yaw, 180.0D);
        }
    }

    /**
     * Air control: the stick pulls the velocity towards itself without ever pushing it past the run
     * speed, and steers whatever speed is above it without taking any of it away. Drag applies all
     * along.
     */
    public void airMotion(double controlScale) {
        var air = this.physics.air();
        double control = this.tuning.airControl() * controlScale;
        double vx = this.state.vx * this.tuning.airDrag();
        double vz = this.state.vz * this.tuning.airDrag();
        if (this.hasStick() && control > 0.0D) {
            double runSpeed = this.tuning.effectiveRunSpeed();
            double cap = runSpeed * this.stickMagnitude;
            double speed = Math.sqrt(vx * vx + vz * vz);
            double sx = this.stickX / this.stickMagnitude;
            double sz = this.stickZ / this.stickMagnitude;
            if (speed > cap && speed > EPSILON) {
                // momentum above the cap is only steered, never fed
                float yaw = yawOf(vx, vz);
                float turned = Mth.approachDegrees(yaw, this.stickYaw(), (float) (air.turnSpeed() * control * this.stickMagnitude));
                if ((vx * sx + vz * sz) / speed < -0.5D) {
                    speed = Math.max(0.0D, speed - air.brake() * control * this.stickMagnitude);
                }
                vx = directionX(turned) * speed;
                vz = directionZ(turned) * speed;
            } else {
                double accel = runSpeed / air.accelTicks() * control * this.stickMagnitude;
                vx += sx * accel;
                vz += sz * accel;
                double next = Math.sqrt(vx * vx + vz * vz);
                double limit = Math.max(cap, speed);
                if (next > limit && next > EPSILON) {
                    vx *= limit / next;
                    vz *= limit / next;
                }
            }
            this.turnFacing(this.stickYaw(), air.turnSpeed() * control);
        }
        this.state.vx = vx;
        this.state.vz = vz;
        this.horizontalDecided = true;
    }

    /**
     * Vertical motion under gravity, levitation and slow falling.
     * <p>
     * The displacement is integrated with the trapezoidal rule ({@code dy = vy - g / 2}), which lands a
     * jump authored as a height and a number of ticks exactly on that height. Gravity is the one of
     * the current arc while climbing, a fraction of it just past the apex as long as jump is held, and
     * a multiple of it on the way down.
     */
    public void gravity() {
        var gravity = this.physics.gravity();
        double vy = this.state.vy;
        if (this.jumpReleased && vy > 0.0D && !this.state.jumpCut) {
            vy *= gravity.releaseCut();
            this.state.jumpCut = true;
        }
        double displacement;
        if (this.tuning.levitation() >= 0) {
            var effects = this.physics.effects();
            double target = effects.levitationSpeedPerLevel() * (this.tuning.levitation() + 1);
            double next = vy + (target - vy) * effects.levitationResponse();
            displacement = (vy + next) * 0.5D;
            vy = next;
        } else {
            double arc = this.state.arcGravity > 0.0D ? this.state.arcGravity : this.tuning.baseGravity();
            double g;
            if (vy > 0.0D) {
                g = arc;
            } else if (this.state.jumpHeld && !this.state.jumpCut && vy > -gravity.apexHangThreshold() && !this.state.grounded) {
                g = arc * gravity.apexHangMultiplier();
            } else {
                g = arc * gravity.fallMultiplier();
            }
            g *= this.tuning.gravityScale();
            displacement = vy - g * 0.5D;
            vy -= g;
            double terminal = gravity.terminalVelocity();
            if (this.tuning.slowFalling()) {
                terminal = Math.min(terminal, this.physics.effects().slowFallingMaxFallSpeed());
            }
            vy = Math.max(vy, -terminal);
            displacement = Math.max(displacement, -terminal);
        }
        this.state.vy = vy;
        this.dy = displacement;
        this.verticalDecided = true;
    }

    /** Like {@link #gravity()}, with the fall capped at {@code maxFallSpeed}: wall slides, spins. */
    public void cappedGravity(double maxFallSpeed) {
        this.gravity();
        if (this.state.vy < -maxFallSpeed) {
            this.state.vy = -maxFallSpeed;
        }
        if (this.dy < -maxFallSpeed) {
            this.dy = -maxFallSpeed;
        }
    }

    /**
     * Starts an arc reaching {@code height} blocks in {@code ticksToApex} ticks.
     * <p>
     * The gravity and the take-off speed are derived from both ({@code g = 2h/t²}, {@code v = 2h/t}),
     * the height being scaled by {@code mubble:arcade_jump_height} (jump boost included) and by the
     * jump factor of the block underfoot. A variable arc is cut short when jump is released early.
     */
    public void launch(double height, double ticksToApex, boolean variable) {
        double h = height * this.tuning.jumpHeight() * this.world.jumpFactor(this.start);
        double t = Math.max(1.0D, ticksToApex);
        this.state.arcGravity = 2.0D * h / (t * t);
        this.state.vy = 2.0D * h / t;
        this.state.jumpCut = !variable;
        this.state.coyote = 0;
        this.consumeJump();
    }

    /** Holds the player in place vertically, for this tick. */
    public void holdVertical() {
        this.state.vy = 0.0D;
        this.dy = 0.0D;
        this.verticalDecided = true;
    }

    /** Overrides the displacement of this tick with a fixed one, for moves following a path. */
    public void setDisplacement(double x, double y, double z) {
        this.dx = x;
        this.dy = y;
        this.dz = z;
        this.verticalDecided = true;
        this.horizontalDecided = true;
        this.displacementFixed = true;
    }

    /** Adds to the first leg of the displacement, carried out before the main one. */
    public void nudge(double x, double y, double z) {
        this.firstX += x;
        this.firstY += y;
        this.firstZ += z;
    }

    // -- probes ---------------------------------------------------------------------------------

    /** The hitbox at the start of the step, in the pose the previous step ended in. */
    public AABB box() {
        return this.world.box(this.start, this.state.pose);
    }

    public AABB box(Pose pose) {
        return this.world.box(this.start, pose);
    }

    /** How high above the ground the player is, up to {@code max}. */
    public double heightAboveGround(double max) {
        double distance = this.world.distanceToGround(this.box(), max);
        return distance < 0.0D ? max : distance;
    }

    /** The horizontal direction closest to ({@code x}, {@code z}). */
    public static Direction horizontalDirection(double x, double z) {
        return Math.abs(x) > Math.abs(z) ? (x > 0.0D ? Direction.EAST : Direction.WEST) : (z > 0.0D ? Direction.SOUTH : Direction.NORTH);
    }

    /**
     * A wall right next to the player in {@code direction}, covering at least the upper half of the
     * body.
     *
     * @param reach how far the wall may be, in blocks
     * @return the wall, or {@code null} when there is none, or when it is tagged {@code mubble:no_wall_jump}
     */
    @Nullable
    public Wall findWall(Direction direction, double reach) {
        var box = this.box();
        var axis = direction.getAxis();
        double step = direction.getAxisDirection().getStep() * reach;
        double allowed = this.world.sweep(box, axis, step);
        if (Math.abs(allowed) >= reach - EPSILON) {
            return null;
        }
        var upper = new AABB(box.minX, (box.minY + box.maxY) * 0.5D, box.minZ, box.maxX, box.maxY, box.maxZ);
        if (Math.abs(this.world.sweep(upper, axis, step)) >= reach - EPSILON) {
            return null;
        }
        double distance = Math.abs(allowed);
        var contact = this.contactPos(box, direction, distance, (box.minY + box.maxY) * 0.5D);
        if (this.world.is(contact, MubbleBlockTags.NO_WALL_JUMP)) {
            return null;
        }
        return new Wall(direction, distance, contact);
    }

    /**
     * An edge the player can hang from in {@code direction}.
     *
     * @param hangDepth how far below the edge the top of the hitbox hangs
     * @param grabBand  how far below the hanging height an edge still gets caught
     * @param minHeight how far above the feet the edge has to be, so that a step is not grabbed
     * @return the ledge, or {@code null} when there is none within reach
     */
    @Nullable
    public Ledge findLedge(Direction direction, double hangDepth, double grabBand, double minHeight) {
        var axis = direction.getAxis();
        double towards = direction.getAxisDirection().getStep() * (axis == Direction.Axis.X ? this.state.vx : this.state.vz);
        return this.findLedge(this.start, direction, hangDepth, grabBand, minHeight, this.grace.ledgeMagnetism() + Math.max(0.0D, towards), Math.max(0.0D, -this.state.vy));
    }

    /**
     * An edge the player standing at {@code from} could hang from, see {@link #findLedge(Direction, double, double, double)}.
     *
     * @param reach how far the wall under the edge may be
     * @param fall  how far the player falls this tick, which widens the band edges get caught in
     */
    @Nullable
    public Ledge findLedge(Vec3 from, Direction direction, double hangDepth, double grabBand, double minHeight, double reach, double fall) {
        var box = this.world.box(from, Pose.STANDING);
        var axis = direction.getAxis();
        int sign = direction.getAxisDirection().getStep();
        double magnetism = this.grace.ledgeMagnetism();
        double allowed = this.world.sweep(box, axis, sign * reach);
        if (Math.abs(allowed) >= reach - EPSILON) {
            return null;
        }
        var atWall = box.move(axis == Direction.Axis.X ? allowed : 0.0D, 0.0D, axis == Direction.Axis.Z ? allowed : 0.0D);

        // the surface right beyond the face of the wall, searched from above the reach of the hands
        double searchTop = atWall.maxY + magnetism;
        double searchBottom = atWall.maxY - hangDepth - grabBand - fall;
        double inset = 0.1D;
        double faceMin = sign > 0 ? (axis == Direction.Axis.X ? atWall.maxX : atWall.maxZ) : (axis == Direction.Axis.X ? atWall.minX : atWall.minZ) - 0.25D;
        double faceMax = faceMin + 0.25D;
        AABB column = axis == Direction.Axis.X
                ? new AABB(faceMin, searchTop, atWall.minZ + inset, faceMax, searchTop + 0.05D, atWall.maxZ - inset)
                : new AABB(atWall.minX + inset, searchTop, faceMin, atWall.maxX - inset, searchTop + 0.05D, faceMax);
        if (this.world.collides(column)) {
            // the wall goes on above the reach of the hands
            return null;
        }
        double drop = this.world.distanceToGround(column, searchTop - searchBottom);
        if (drop < 0.0D) {
            return null;
        }
        double top = searchTop - drop;
        if (top < atWall.minY + minHeight) {
            return null;
        }
        var edgePos = BlockPos.containing((column.minX + column.maxX) * 0.5D, top - 0.01D, (column.minZ + column.maxZ) * 0.5D);
        if (this.world.is(edgePos, MubbleBlockTags.NO_LEDGE_GRAB)) {
            return null;
        }
        // there has to be room to climb up, at least crouching
        var crouching = this.world.dimensions(Pose.CROUCHING);
        double width = crouching.width();
        double centerX = axis == Direction.Axis.X ? faceMin + (sign > 0 ? width * 0.5D + 0.01D : 0.25D - width * 0.5D - 0.01D) : (atWall.minX + atWall.maxX) * 0.5D;
        double centerZ = axis == Direction.Axis.Z ? faceMin + (sign > 0 ? width * 0.5D + 0.01D : 0.25D - width * 0.5D - 0.01D) : (atWall.minZ + atWall.maxZ) * 0.5D;
        var onTop = crouching.makeBoundingBox(centerX, top + 0.001D, centerZ);
        if (this.world.collides(onTop)) {
            return null;
        }
        // where the player hangs: against the wall, the top of the hitbox a little under the edge
        double height = atWall.getYsize();
        var hang = new Vec3((atWall.minX + atWall.maxX) * 0.5D, top - hangDepth - height, (atWall.minZ + atWall.maxZ) * 0.5D);
        var hangBox = this.world.box(hang, Pose.STANDING);
        if (this.world.collides(hangBox.deflate(1.0E-4D))) {
            return null;
        }
        return new Ledge(direction, top, hang);
    }

    private BlockPos contactPos(AABB box, Direction direction, double distance, double y) {
        double x = (box.minX + box.maxX) * 0.5D;
        double z = (box.minZ + box.maxZ) * 0.5D;
        double reach = distance + 0.01D;
        switch (direction) {
            case EAST -> x = box.maxX + reach;
            case WEST -> x = box.minX - reach;
            case SOUTH -> z = box.maxZ + reach;
            case NORTH -> z = box.minZ - reach;
            default -> {
            }
        }
        return BlockPos.containing(x, y, z);
    }

    // -- the end of the plan --------------------------------------------------------------------

    /**
     * Finishes the displacement: the grace nudges of the air, the speed limits and the hitbox pose.
     */
    void finishPlan() {
        var s = this.state;
        if (!this.verticalDecided) {
            this.gravity();
        }

        // the safety ceiling, which nothing goes past
        double ceiling = this.physics.safetyCeiling();
        double speed = s.horizontalSpeed();
        if (speed > ceiling) {
            s.vx *= ceiling / speed;
            s.vz *= ceiling / speed;
        }
        s.vy = Mth.clamp(s.vy, -ceiling, ceiling);
        if (!this.displacementFixed) {
            this.dx = s.vx;
            this.dz = s.vz;
            this.dy = Mth.clamp(this.dy, -ceiling, ceiling);
        }

        this.pose = this.resolvePose(s.move.pose(this));
        if (!s.grounded && !this.displacementFixed) {
            this.cornerCorrection();
        }
    }

    /**
     * The grace of corners: a jump that would bonk its head on the very edge of a ceiling slides
     * around it, and a fall or a jump clipping the top edge of a wall with the feet is lifted over.
     */
    private void cornerCorrection() {
        double reach = this.grace.cornerCorrection();
        if (reach <= 0.0D) {
            return;
        }
        var box = this.world.box(this.start, this.pose);
        if (this.dy > 0.0D && this.world.collides(box.move(0.0D, this.dy, 0.0D))) {
            for (double offset = 0.05D; offset <= reach + EPSILON; offset += 0.05D) {
                for (var direction : CORNER_DIRECTIONS) {
                    double ox = direction.getStepX() * offset;
                    double oz = direction.getStepZ() * offset;
                    var shifted = box.move(ox, 0.0D, oz);
                    if (!this.world.collides(shifted) && !this.world.collides(shifted.move(0.0D, this.dy, 0.0D))) {
                        this.nudge(ox, 0.0D, oz);
                        return;
                    }
                }
            }
        }
        if ((this.dx != 0.0D || this.dz != 0.0D) && this.world.collides(box.move(this.dx, 0.0D, this.dz)) && this.dy > -reach) {
            for (double lift = 0.05D; lift <= reach + EPSILON; lift += 0.05D) {
                var lifted = box.move(0.0D, lift, 0.0D);
                if (!this.world.collides(lifted) && !this.world.collides(lifted.move(this.dx, 0.0D, this.dz))) {
                    this.nudge(0.0D, lift, 0.0D);
                    return;
                }
            }
        }
    }

    private static final Direction[] CORNER_DIRECTIONS = {Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH};

    /** The pose the move asks for if it fits, a lower one otherwise: nobody stands up into a ceiling. */
    private Pose resolvePose(Pose requested) {
        if (this.fits(requested)) {
            return requested;
        }
        if (requested != Pose.SWIMMING && this.fits(Pose.CROUCHING)) {
            return Pose.CROUCHING;
        }
        return Pose.SWIMMING;
    }

    private boolean fits(Pose pose) {
        return !this.world.collides(this.world.box(this.start, pose).deflate(1.0E-7D));
    }

    public Pose pose() {
        return this.pose;
    }

    /** The first leg of the displacement. */
    public Vec3 first() {
        return new Vec3(this.firstX, this.firstY, this.firstZ);
    }

    /** The main leg of the displacement. */
    public Vec3 second() {
        return new Vec3(this.dx, this.dy, this.dz);
    }

    /** Both legs together. */
    public Vec3 total() {
        return new Vec3(this.firstX + this.dx, this.firstY + this.dy, this.firstZ + this.dz);
    }

    /**
     * Keeps a ground move on the ground when the floor steps down under it: stairs, slabs, the edge
     * of a block. Within {@link ArcadePhysics.Slope#snapDown()}, the player goes sideways first, then
     * straight down onto the lower step, instead of flying off it; and the height given up that way
     * turns into speed, {@code gainPerBlock} for every block of descent.
     *
     * @return how far the player went down, 0 when the floor did not step down
     */
    public double hugGround(double gainPerBlock) {
        var s = this.state;
        if (!s.grounded || s.vy > 0.0D) {
            return 0.0D;
        }
        var box = this.box();
        var moved = box.move(s.vx, 0.0D, s.vz);
        if (this.world.collides(moved) || this.world.collides(moved.move(0.0D, -SUPPORT_PROBE, 0.0D))) {
            // blocked (vanilla steps up on its own), or still supported
            return 0.0D;
        }
        double drop = this.world.distanceToGround(moved, this.physics.slope().snapDown() + SUPPORT_PROBE);
        if (drop < 0.0D) {
            // a real edge: the player leaves the ground
            return 0.0D;
        }
        this.firstX += s.vx;
        this.firstZ += s.vz;
        this.dx = 0.0D;
        this.dz = 0.0D;
        this.dy = -(drop + 1.0E-4D);
        this.verticalDecided = true;
        this.displacementFixed = true;
        s.vy = 0.0D;
        double speed = s.horizontalSpeed();
        if (gainPerBlock > 0.0D && speed > EPSILON) {
            double gained = speed + gainPerBlock * drop;
            s.vx *= gained / speed;
            s.vz *= gained / speed;
        }
        return drop;
    }

    /** How far below the feet the next floor may be and still carry the player without a snap. */
    private static final double SUPPORT_PROBE = 0.1D;

    public void setVerticalDisplacement(double dy) {
        this.dy = dy;
        this.verticalDecided = true;
    }

    public double plannedVertical() {
        return this.dy;
    }

    // -- angles ---------------------------------------------------------------------------------

    public static float yawOf(double x, double z) {
        return (float) (Mth.atan2(-x, z) * Mth.RAD_TO_DEG);
    }

    public static double directionX(float yaw) {
        return -Mth.sin(yaw * DEGREES_TO_RADIANS);
    }

    public static double directionZ(float yaw) {
        return Mth.cos(yaw * DEGREES_TO_RADIANS);
    }

    /** A wall found by {@link #findWall}. */
    public record Wall(Direction direction, double distance, BlockPos contact) {
        public double normalX() {
            return -this.direction.getStepX();
        }

        public double normalZ() {
            return -this.direction.getStepZ();
        }
    }

    /** A ledge found by {@link #findLedge}. */
    public record Ledge(Direction direction, double top, Vec3 hangPosition) {
    }
}
