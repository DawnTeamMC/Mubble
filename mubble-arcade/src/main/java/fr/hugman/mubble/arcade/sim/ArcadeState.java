package fr.hugman.mubble.arcade.sim;

import fr.hugman.mubble.arcade.registries.ArcadeBuiltInRegistries;
import fr.hugman.mubble.arcade.move.ArcadeMove;
import fr.hugman.mubble.arcade.move.ArcadeMoves;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Pose;

/**
 * Everything the simulation remembers from one tick to the next.
 * <p>
 * The state is the whole input of a step besides the input frame, the tuning and the world: two
 * equal states fed the same frame in the same world end up equal, which is what lets the client
 * predict and the server validate with the very same code. {@link #write} covers every field, and
 * comparing two encodings is how the tests and the server tell whether two states agree.
 * <p>
 * Speeds are in blocks per tick, angles in degrees, timers in ticks unless their name says otherwise.
 */
public final class ArcadeState {
    /** How many ticks of slope history are kept at most, whatever a profile asks for. */
    public static final int MAX_SLOPE_WINDOW = 10;

    public static final StreamCodec<ByteBuf, ArcadeState> STREAM_CODEC = StreamCodec.of(
            (buf, state) -> state.write(new FriendlyByteBuf(buf)),
            buf -> ArcadeState.read(new FriendlyByteBuf(buf))
    );

    // the state machine
    public ArcadeMove move = ArcadeMoves.FALL;
    public int moveTicks;
    /** Bumped every time a move is entered, so that entering the same move twice still reads as a new start. */
    public int moveSeq;

    // motion
    public double vx;
    public double vy;
    public double vz;
    /** The direction the body faces, which most moves launch along. */
    public float facing;
    public boolean grounded;
    /** The hitbox pose the last step ended in. */
    public Pose pose = Pose.STANDING;
    public int airTicks;
    /** Gravity of the arc currently climbing, set by the move that launched it. */
    public double arcGravity;

    // input memory
    public boolean jumpHeld;
    public boolean crouchHeld;
    public boolean actionHeld;
    public int jumpBufferMs;
    public int actionBufferMs;
    /** Whether the variable jump height was already cut, or no longer can be, during this airtime. */
    public boolean jumpCut;

    // grace and chains
    public int coyote;
    public int chainIndex;
    public int chainWindow;
    public int wallLeniency;
    public double wallNormalX;
    public double wallNormalZ;
    /** Ticks during which the stick may not steer back towards a wall just kicked off. */
    public int controlLock;
    public int ledgeRegrab;

    // once per airtime
    public boolean divedThisAir;
    public boolean spunThisAir;

    // moves
    public int rollBoosts;
    public int rollBoostCooldown;
    /** Horizontal direction of the ledge being held, as the 2D data value of a direction, or -1. */
    public int ledgeFace = -1;
    public double ledgeY;
    /** Where a move that follows a fixed path started from: a ledge climb, a vault. */
    public double anchorX;
    public double anchorY;
    public double anchorZ;
    public float stickAngle;
    public float spinAccum;
    public int spinWindow;

    // slope history, as a ring buffer of grounded height changes and horizontal distances
    public final double[] slopeDy = new double[MAX_SLOPE_WINDOW];
    public final double[] slopeDxz = new double[MAX_SLOPE_WINDOW];
    public int slopeIndex;

    // landing bookkeeping
    public double lastLandingSpeed;
    /** Whether the landing of this tick takes no fall damage: a ground pound, a roll out of it... */
    public boolean negateFallDamage;

    /** What happened during the last step, for cues, animations, exhaustion and statistics. Not part of the encoding. */
    public final transient List<MoveEvent> events = new ArrayList<>();

    public double horizontalSpeed() {
        return Math.sqrt(this.vx * this.vx + this.vz * this.vz);
    }

    public double slope() {
        double dy = 0.0D;
        double dxz = 0.0D;
        for (int i = 0; i < MAX_SLOPE_WINDOW; i++) {
            dy += this.slopeDy[i];
            dxz += this.slopeDxz[i];
        }
        return dxz < 1.0E-4D ? 0.0D : dy / dxz;
    }

    public void recordSlope(double dy, double dxz, int window) {
        int size = Math.max(1, Math.min(window, MAX_SLOPE_WINDOW));
        this.slopeIndex = (this.slopeIndex + 1) % size;
        this.slopeDy[this.slopeIndex] = dy;
        this.slopeDxz[this.slopeIndex] = dxz;
        // shrinking the window leaves stale samples behind it
        for (int i = size; i < MAX_SLOPE_WINDOW; i++) {
            this.slopeDy[i] = 0.0D;
            this.slopeDxz[i] = 0.0D;
        }
    }

    public void clearSlope() {
        Arrays.fill(this.slopeDy, 0.0D);
        Arrays.fill(this.slopeDxz, 0.0D);
        this.slopeIndex = 0;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(ArcadeBuiltInRegistries.ARCADE_MOVE.getId(this.move));
        buf.writeVarInt(this.moveTicks);
        buf.writeVarInt(this.moveSeq);
        buf.writeDouble(this.vx);
        buf.writeDouble(this.vy);
        buf.writeDouble(this.vz);
        buf.writeFloat(this.facing);
        buf.writeBoolean(this.grounded);
        buf.writeEnum(this.pose);
        buf.writeVarInt(this.airTicks);
        buf.writeDouble(this.arcGravity);
        buf.writeBoolean(this.jumpHeld);
        buf.writeBoolean(this.crouchHeld);
        buf.writeBoolean(this.actionHeld);
        buf.writeVarInt(this.jumpBufferMs);
        buf.writeVarInt(this.actionBufferMs);
        buf.writeBoolean(this.jumpCut);
        buf.writeVarInt(this.coyote);
        buf.writeVarInt(this.chainIndex);
        buf.writeVarInt(this.chainWindow);
        buf.writeVarInt(this.wallLeniency);
        buf.writeDouble(this.wallNormalX);
        buf.writeDouble(this.wallNormalZ);
        buf.writeVarInt(this.controlLock);
        buf.writeVarInt(this.ledgeRegrab);
        buf.writeBoolean(this.divedThisAir);
        buf.writeBoolean(this.spunThisAir);
        buf.writeVarInt(this.rollBoosts);
        buf.writeVarInt(this.rollBoostCooldown);
        buf.writeVarInt(this.ledgeFace + 1);
        buf.writeDouble(this.ledgeY);
        buf.writeDouble(this.anchorX);
        buf.writeDouble(this.anchorY);
        buf.writeDouble(this.anchorZ);
        buf.writeFloat(this.stickAngle);
        buf.writeFloat(this.spinAccum);
        buf.writeVarInt(this.spinWindow);
        for (int i = 0; i < MAX_SLOPE_WINDOW; i++) {
            buf.writeDouble(this.slopeDy[i]);
            buf.writeDouble(this.slopeDxz[i]);
        }
        buf.writeVarInt(this.slopeIndex);
        buf.writeDouble(this.lastLandingSpeed);
        buf.writeBoolean(this.negateFallDamage);
    }

    public static ArcadeState read(FriendlyByteBuf buf) {
        var state = new ArcadeState();
        var move = ArcadeBuiltInRegistries.ARCADE_MOVE.byId(buf.readVarInt());
        state.move = move == null ? ArcadeMoves.FALL : move;
        state.moveTicks = buf.readVarInt();
        state.moveSeq = buf.readVarInt();
        state.vx = buf.readDouble();
        state.vy = buf.readDouble();
        state.vz = buf.readDouble();
        state.facing = buf.readFloat();
        state.grounded = buf.readBoolean();
        state.pose = buf.readEnum(Pose.class);
        state.airTicks = buf.readVarInt();
        state.arcGravity = buf.readDouble();
        state.jumpHeld = buf.readBoolean();
        state.crouchHeld = buf.readBoolean();
        state.actionHeld = buf.readBoolean();
        state.jumpBufferMs = buf.readVarInt();
        state.actionBufferMs = buf.readVarInt();
        state.jumpCut = buf.readBoolean();
        state.coyote = buf.readVarInt();
        state.chainIndex = buf.readVarInt();
        state.chainWindow = buf.readVarInt();
        state.wallLeniency = buf.readVarInt();
        state.wallNormalX = buf.readDouble();
        state.wallNormalZ = buf.readDouble();
        state.controlLock = buf.readVarInt();
        state.ledgeRegrab = buf.readVarInt();
        state.divedThisAir = buf.readBoolean();
        state.spunThisAir = buf.readBoolean();
        state.rollBoosts = buf.readVarInt();
        state.rollBoostCooldown = buf.readVarInt();
        state.ledgeFace = buf.readVarInt() - 1;
        state.ledgeY = buf.readDouble();
        state.anchorX = buf.readDouble();
        state.anchorY = buf.readDouble();
        state.anchorZ = buf.readDouble();
        state.stickAngle = buf.readFloat();
        state.spinAccum = buf.readFloat();
        state.spinWindow = buf.readVarInt();
        for (int i = 0; i < MAX_SLOPE_WINDOW; i++) {
            state.slopeDy[i] = buf.readDouble();
            state.slopeDxz[i] = buf.readDouble();
        }
        state.slopeIndex = buf.readVarInt();
        state.lastLandingSpeed = buf.readDouble();
        state.negateFallDamage = buf.readBoolean();
        return state;
    }

    /** The encoding of this state, which two states agreeing on every field share byte for byte. */
    public byte[] toBytes() {
        var buf = new FriendlyByteBuf(Unpooled.buffer());
        this.write(buf);
        var bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        return bytes;
    }

    public ArcadeState copy() {
        var buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(this.toBytes()));
        return read(buf);
    }

    public void copyFrom(ArcadeState other) {
        var buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(other.toBytes()));
        var copy = read(buf);
        this.move = copy.move;
        this.moveTicks = copy.moveTicks;
        this.moveSeq = copy.moveSeq;
        this.vx = copy.vx;
        this.vy = copy.vy;
        this.vz = copy.vz;
        this.facing = copy.facing;
        this.grounded = copy.grounded;
        this.pose = copy.pose;
        this.airTicks = copy.airTicks;
        this.arcGravity = copy.arcGravity;
        this.jumpHeld = copy.jumpHeld;
        this.crouchHeld = copy.crouchHeld;
        this.actionHeld = copy.actionHeld;
        this.jumpBufferMs = copy.jumpBufferMs;
        this.actionBufferMs = copy.actionBufferMs;
        this.jumpCut = copy.jumpCut;
        this.coyote = copy.coyote;
        this.chainIndex = copy.chainIndex;
        this.chainWindow = copy.chainWindow;
        this.wallLeniency = copy.wallLeniency;
        this.wallNormalX = copy.wallNormalX;
        this.wallNormalZ = copy.wallNormalZ;
        this.controlLock = copy.controlLock;
        this.ledgeRegrab = copy.ledgeRegrab;
        this.divedThisAir = copy.divedThisAir;
        this.spunThisAir = copy.spunThisAir;
        this.rollBoosts = copy.rollBoosts;
        this.rollBoostCooldown = copy.rollBoostCooldown;
        this.ledgeFace = copy.ledgeFace;
        this.ledgeY = copy.ledgeY;
        this.anchorX = copy.anchorX;
        this.anchorY = copy.anchorY;
        this.anchorZ = copy.anchorZ;
        this.stickAngle = copy.stickAngle;
        this.spinAccum = copy.spinAccum;
        this.spinWindow = copy.spinWindow;
        System.arraycopy(copy.slopeDy, 0, this.slopeDy, 0, MAX_SLOPE_WINDOW);
        System.arraycopy(copy.slopeDxz, 0, this.slopeDxz, 0, MAX_SLOPE_WINDOW);
        this.slopeIndex = copy.slopeIndex;
        this.lastLandingSpeed = copy.lastLandingSpeed;
        this.negateFallDamage = copy.negateFallDamage;
    }

    public boolean sameAs(ArcadeState other) {
        return Arrays.equals(this.toBytes(), other.toBytes());
    }

    /**
     * A 64-bit fingerprint of the encoding (FNV-1a), which the client sends instead of the whole
     * state so that the server can tell whether both still agree.
     */
    public long fingerprint() {
        long hash = 0xcbf29ce484222325L;
        for (byte b : this.toBytes()) {
            hash ^= b & 0xFF;
            hash *= 0x100000001b3L;
        }
        return hash;
    }

    @Override
    public String toString() {
        return String.format("%s#%d t=%d v=(%.4f, %.4f, %.4f) %s", this.move, this.moveSeq, this.moveTicks, this.vx, this.vy, this.vz, this.grounded ? "grounded" : "airborne");
    }
}
