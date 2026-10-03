package fr.hugman.mubble.world.arcade;

import fr.hugman.mubble.core.registries.MubbleBuiltInRegistries;
import fr.hugman.mubble.tags.ArcadeMoveTags;
import fr.hugman.mubble.world.arcade.access.ArcadeAccess;
import fr.hugman.mubble.world.arcade.access.ArcadeSources;
import fr.hugman.mubble.world.arcade.access.ResolvedAccess;
import fr.hugman.mubble.world.arcade.cue.CueEvent;
import fr.hugman.mubble.world.arcade.move.ArcMove;
import fr.hugman.mubble.world.arcade.move.ArcadeMove;
import fr.hugman.mubble.world.arcade.move.ArcadeMoves;
import fr.hugman.mubble.world.arcade.move.GroundPoundMove;
import fr.hugman.mubble.world.arcade.move.RollMove;
import fr.hugman.mubble.world.arcade.move.SpinMove;
import fr.hugman.mubble.world.arcade.move.WallSlideMove;
import fr.hugman.mubble.world.arcade.sim.ArcadeInputFrame;
import fr.hugman.mubble.world.arcade.sim.ArcadeState;
import fr.hugman.mubble.world.arcade.sim.ArcadeTuning;
import fr.hugman.mubble.world.arcade.sim.ArcadeWorld;
import fr.hugman.mubble.world.arcade.sim.MoveContext;
import fr.hugman.mubble.world.arcade.sim.MoveResult;
import fr.hugman.mubble.world.entity.ai.attributes.MubbleAttributes;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * The arcade movement layer of one player, on one side.
 * <p>
 * Every tick it works out what the sources of the player allow and whether the layer is on, and
 * keeps the simulation state in between steps. Without an active source it does nothing else: no
 * attribute is touched, no hook takes over, and vanilla movement, rendering and networking stay
 * exactly what they are.
 * <p>
 * While it is on, the layer suspends itself whenever vanilla movement has to take over (swimming,
 * climbing, gliding, riding, flying, sleeping, dying, changing dimension) and comes back from a clean
 * state once that is over.
 */
public final class ArcadeController {
    /** How many past steps a client keeps, to replay them on top of a correction from the server. */
    public static final int HISTORY_SIZE = 64;

    private final Player player;
    private final ArcadeWorld world;
    private ArcadeState state = new ArcadeState();
    private ResolvedAccess access = ResolvedAccess.NONE;
    @Nullable
    private ArcadeProfile profile;
    @Nullable
    private ArcadeProfile appliedProfile;
    private boolean suspended;
    private boolean needsReset = true;

    // the side predicting the movement
    private final Deque<HistoryEntry> history = new ArrayDeque<>();
    private int nextTick;
    private Vec3 writtenVelocity = Vec3.ZERO;

    // the side validating it
    private final Validation validation = new Validation();

    public ArcadeController(Player player) {
        this.player = player;
        this.world = new ArcadeWorld(player.level(), player);
    }

    public static ArcadeController of(Player player) {
        return ((ArcadePlayer) player).mubble$arcade();
    }

    // -- the state of the layer -----------------------------------------------------------------

    /**
     * Brings the layer up to date with the sources of the player. Called at the start of every
     * player tick, on both sides.
     */
    public void tick() {
        var level = this.player.level();
        if (!level.isClientSide()) {
            ArcadeSources.pruneExpired(this.player, level.getGameTime());
        }
        boolean wasDriving = this.isDriving();
        this.access = ArcadeAccess.of(this.player);
        this.profile = this.access.isActive() ? this.access.profile().flatMap(key -> ArcadeProfiles.get(level, key)).orElse(null) : null;
        this.applyAttributeBases();
        this.suspended = this.profile != null && this.shouldSuspend();

        if (wasDriving && !this.isDriving()) {
            this.stop();
        } else if (!wasDriving && this.isDriving()) {
            this.needsReset = true;
        }
    }

    /** Whether vanilla movement has to take over for now. */
    private boolean shouldSuspend() {
        var p = this.player;
        if (p.isDeadOrDying() || p.isSleeping() || p.isPassenger() || p.isSpectator() || p.isFallFlying() || p.getAbilities().flying || p.onClimbable()) {
            return true;
        }
        if (p.isInLava() || (p.isInWater() && p.getFluidHeight(net.minecraft.tags.FluidTags.WATER) > p.getFluidJumpThreshold())) {
            return true;
        }
        return p instanceof ServerPlayer serverPlayer && serverPlayer.isChangingDimension();
    }

    /** Ends the moves cleanly: no state, pose or visual is left behind for vanilla to trip on. */
    private void stop() {
        this.state = new ArcadeState();
        this.needsReset = true;
        this.history.clear();
        this.validation.clear();
        ((ArcadePlayer) this.player).mubble$setArcadeVisual(ArcadeVisual.NONE);
    }

    /**
     * Makes the values of the active profile the base values of the arcade attributes, and puts them
     * back to their defaults once no profile is active.
     */
    private void applyAttributeBases() {
        if (this.profile == this.appliedProfile) {
            return;
        }
        this.appliedProfile = this.profile;
        for (var attribute : MubbleAttributes.ARCADE) {
            var instance = this.player.getAttribute(attribute);
            if (instance != null) {
                instance.setBaseValue(this.profile == null ? attribute.value().getDefaultValue() : baseValue(attribute, this.profile));
            }
        }
    }

    /** What {@code profile} says the base value of {@code attribute} is. */
    public static double baseValue(Holder<Attribute> attribute, ArcadeProfile profile) {
        var physics = profile.physics();
        if (attribute == MubbleAttributes.ARCADE_RUN_SPEED) {
            return physics.ground().runSpeed();
        } else if (attribute == MubbleAttributes.ARCADE_JUMP_HEIGHT) {
            return physics.gravity().jumpHeightMultiplier();
        } else if (attribute == MubbleAttributes.ARCADE_ROLL_BOOST) {
            return profile.settingsOrDefault(ArcadeMoves.ROLL).get(RollMove.BOOST);
        } else if (attribute == MubbleAttributes.ARCADE_AIR_DRAG) {
            return physics.air().drag();
        } else if (attribute == MubbleAttributes.ARCADE_AIR_CONTROL) {
            return physics.air().control();
        } else if (attribute == MubbleAttributes.ARCADE_COYOTE_TICKS) {
            return profile.grace().coyoteTicks();
        } else if (attribute == MubbleAttributes.ARCADE_WALL_SLIDE_SPEED) {
            return profile.settingsOrDefault(ArcadeMoves.WALL_SLIDE).get(WallSlideMove.MAX_FALL_SPEED);
        } else if (attribute == MubbleAttributes.ARCADE_GROUND_POUND_SPEED) {
            return profile.settingsOrDefault(ArcadeMoves.GROUND_POUND).get(GroundPoundMove.DROP_SPEED);
        }
        return attribute.value().getDefaultValue();
    }

    /** Whether a profile is active, suspended or not. */
    public boolean isActive() {
        return this.profile != null;
    }

    public boolean isSuspended() {
        return this.suspended;
    }

    /** Whether the layer is driving the movement right now: a profile is active and nothing suspends it. */
    public boolean isDriving() {
        return this.profile != null && !this.suspended;
    }

    @Nullable
    public ArcadeProfile profile() {
        return this.profile;
    }

    public ResolvedAccess access() {
        return this.access;
    }

    public ArcadeState state() {
        return this.state;
    }

    public void setState(ArcadeState state) {
        this.state = state;
    }

    public ArcadeWorld world() {
        return this.world;
    }

    public Player player() {
        return this.player;
    }

    /**
     * Whether {@code move} may be entered: allowed by the sources, supported by the profile, and, for
     * the moves tagged {@code mubble:speed}, affordable when the profile makes food count.
     */
    public boolean allows(ArcadeMove move) {
        if (move.isBase()) {
            return true;
        }
        var profile = this.profile;
        if (profile == null || !profile.supports(move.accessRoot()) || !this.access.allows(move)) {
            return false;
        }
        if (profile.costs().hungerGatesSpeedMoves() && !this.hasEnoughFood() && isTagged(move, ArcadeMoveTags.SPEED)) {
            return false;
        }
        return true;
    }

    private boolean hasEnoughFood() {
        return this.player.getFoodData().hasEnoughFood() || this.player.getAbilities().mayfly;
    }

    private static boolean isTagged(ArcadeMove move, net.minecraft.tags.TagKey<ArcadeMove> tag) {
        return MubbleBuiltInRegistries.ARCADE_MOVE.getResourceKey(move.accessRoot())
                .flatMap(MubbleBuiltInRegistries.ARCADE_MOVE::get)
                .map(holder -> holder.is(tag))
                .orElse(false);
    }

    public ArcadeTuning tuning() {
        return ArcadeTuning.of(this.player, Objects.requireNonNull(this.profile, "No active profile"), this::allows);
    }

    /** Whether the hands are kept from attacking and using items by the current move. */
    public boolean handsBusy() {
        return this.isDriving() && this.profile.settingsOrDefault(this.state.move).handsBusy(this.state.move);
    }

    public InteractionPolicy interaction() {
        return this.isDriving() ? this.profile.interaction() : InteractionPolicy.FULL;
    }

    // -- starting over --------------------------------------------------------------------------

    public boolean needsReset() {
        return this.needsReset;
    }

    public void markReset() {
        this.needsReset = true;
    }

    /**
     * Starts the simulation over from what the entity looks like right now: on the ground or not,
     * moving as fast as it does (within the safety ceiling), facing where it faces.
     */
    public void resetFromEntity() {
        var fresh = new ArcadeState();
        fresh.grounded = this.player.onGround();
        fresh.move = fresh.grounded ? (this.allows(ArcadeMoves.RUN) ? ArcadeMoves.RUN : ArcadeMoves.WALK) : ArcadeMoves.FALL;
        var velocity = this.player.getDeltaMovement();
        double ceiling = this.profile == null ? 0.0D : this.profile.physics().safetyCeiling();
        double speed = velocity.length();
        if (speed > ceiling && speed > 0.0D) {
            velocity = velocity.scale(ceiling / speed);
        }
        fresh.vx = velocity.x;
        fresh.vy = fresh.grounded ? 0.0D : velocity.y;
        fresh.vz = velocity.z;
        fresh.facing = this.player.getYRot();
        fresh.pose = this.player.getPose();
        this.state = fresh;
        this.needsReset = false;
        this.history.clear();
        this.writtenVelocity = velocity;
    }

    // -- effects of a step ----------------------------------------------------------------------

    /**
     * What a step costs and counts on the server: exhaustion when the profile charges for its moves,
     * the jump statistic for every jump.
     */
    public void applyServerEffects(MoveContext ctx) {
        var profile = ctx.profile();
        for (var event : this.state.events) {
            if (event.type() != CueEvent.START) {
                continue;
            }
            if (profile.costs().exhaustion()) {
                float exhaustion = ctx.settings(event.move()).exhaustion();
                if (exhaustion > 0.0F) {
                    this.player.causeFoodExhaustion(exhaustion);
                }
            }
            if (event.move() instanceof ArcMove || event.move() instanceof SpinMove) {
                this.player.awardStat(Stats.JUMP);
            }
        }
        if (profile.costs().exhaustion() && this.state.grounded) {
            double perBlock = this.state.move.exhaustionPerBlock(ctx);
            if (perBlock > 0.0D) {
                this.player.causeFoodExhaustion((float) (perBlock * ctx.result().horizontalDistance()));
            }
        }
    }

    /** Publishes the move to the other players, when it changed. */
    public void updateVisual() {
        var arcadePlayer = (ArcadePlayer) this.player;
        var current = arcadePlayer.mubble$arcadeVisual();
        if (current.move() != this.state.move || current.seq() != this.state.moveSeq) {
            float intensity = this.state.events.stream()
                    .filter(event -> event.move() == this.state.move)
                    .map(event -> (float) event.intensity())
                    .reduce((first, second) -> second)
                    .orElse((float) this.state.horizontalSpeed());
            arcadePlayer.mubble$setArcadeVisual(new ArcadeVisual(this.state.move, this.state.moveSeq, intensity, this.access.profile().orElse(null)));
        }
    }

    // -- the predicting side --------------------------------------------------------------------

    public int nextTick() {
        return this.nextTick++;
    }

    public void remember(HistoryEntry entry) {
        this.history.addLast(entry);
        while (this.history.size() > HISTORY_SIZE) {
            this.history.removeFirst();
        }
    }

    public Deque<HistoryEntry> history() {
        return this.history;
    }

    public Vec3 writtenVelocity() {
        return this.writtenVelocity;
    }

    public void setWrittenVelocity(Vec3 velocity) {
        this.writtenVelocity = velocity;
    }

    /**
     * One step as the predicting side took it, kept to be replayed.
     *
     * @param frame   the input of the step
     * @param from    where the step started
     * @param result  what the world did with it
     * @param impulse the velocity something outside the simulation set right before the step, if any
     */
    public record HistoryEntry(ArcadeInputFrame frame, Vec3 from, MoveResult result, Optional<Vec3> impulse) {
    }

    // -- the validating side --------------------------------------------------------------------

    public Validation validation() {
        return this.validation;
    }

    /** What the server remembers about the movement of a player it validates. */
    public static final class Validation {
        /** Where the last validated step left the player, which the next vanilla move packet has to match. */
        @Nullable
        public Vec3 expectedPosition;
        /** Game time of the last push the server gave the player: knockback, explosion... */
        public long lastImpulseTime = Long.MIN_VALUE;
        /** How strong that push was, in blocks per tick. */
        public double lastImpulseStrength;
        /** The tick number of the last input frame processed. */
        public int lastTick = -1;
        /** Whether the last vanilla move packet was announced by a validated step, which keeps the floating kick off. */
        public boolean vouched;
        /** Whether the teleport being sent is a rejection from the validator itself. */
        public boolean ownTeleport;
        public int accepted;
        public int rejected;
        public int corrected;

        public void clear() {
            this.expectedPosition = null;
            this.lastTick = -1;
            this.vouched = false;
        }

        public void recordImpulse(long gameTime, double strength) {
            // two pushes on the same tick add up, at worst
            this.lastImpulseStrength = gameTime == this.lastImpulseTime ? this.lastImpulseStrength + strength : strength;
            this.lastImpulseTime = gameTime;
        }
    }
}
