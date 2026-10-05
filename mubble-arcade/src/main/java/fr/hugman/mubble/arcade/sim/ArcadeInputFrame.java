package fr.hugman.mubble.arcade.sim;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;

/**
 * What the player asked for during one tick, as the simulation consumes it.
 * <p>
 * The client samples its inputs every frame and folds them into one of these per tick, see the
 * client input buffer. A press that happened between two ticks is not lost: it shows up in
 * {@link #pressed()} even if the key is already released, along with how long ago it happened, so
 * that jump buffering and coyote time keep their full length despite the 50 ms tick.
 *
 * @param tick        sequence number of the tick on the client, to keep the server in lockstep
 * @param stickX      world-space x of the movement stick, already turned by the camera
 * @param stickZ      world-space z of the movement stick
 * @param viewYaw     yaw of the view, in degrees
 * @param coupled     whether the body follows the view (first person) rather than the stick
 * @param held        actions held down at the end of the tick, see the {@code JUMP}... bits
 * @param pressed     actions pressed at least once since the previous tick
 * @param jumpAgeMs   how long before the tick the last jump press happened, in milliseconds
 * @param actionAgeMs how long before the tick the last action press happened, in milliseconds
 */
public record ArcadeInputFrame(
        int tick,
        float stickX,
        float stickZ,
        float viewYaw,
        boolean coupled,
        byte held,
        byte pressed,
        short jumpAgeMs,
        short actionAgeMs
) {
    public static final int JUMP = 1;
    public static final int CROUCH = 1 << 1;
    public static final int ACTION = 1 << 2;
    public static final int SPRINT = 1 << 3;
    public static final int SPIN = 1 << 4;

    public static final ArcadeInputFrame IDLE = new ArcadeInputFrame(0, 0.0F, 0.0F, 0.0F, true, (byte) 0, (byte) 0, (short) 0, (short) 0);

    public static final StreamCodec<ByteBuf, ArcadeInputFrame> STREAM_CODEC = StreamCodec.of(
            (buf, frame) -> frame.write(new FriendlyByteBuf(buf)),
            buf -> read(new FriendlyByteBuf(buf))
    );

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(this.tick);
        buf.writeFloat(this.stickX);
        buf.writeFloat(this.stickZ);
        buf.writeFloat(this.viewYaw);
        buf.writeBoolean(this.coupled);
        buf.writeByte(this.held);
        buf.writeByte(this.pressed);
        buf.writeShort(this.jumpAgeMs);
        buf.writeShort(this.actionAgeMs);
    }

    public static ArcadeInputFrame read(FriendlyByteBuf buf) {
        var frame = new ArcadeInputFrame(buf.readVarInt(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readBoolean(), buf.readByte(), buf.readByte(), buf.readShort(), buf.readShort());
        return frame.sanitized();
    }

    /** The same frame, with whatever a hostile client could send clamped back into range. */
    public ArcadeInputFrame sanitized() {
        float x = Float.isFinite(this.stickX) ? this.stickX : 0.0F;
        float z = Float.isFinite(this.stickZ) ? this.stickZ : 0.0F;
        float length = Mth.sqrt(x * x + z * z);
        if (length > 1.0F) {
            x /= length;
            z /= length;
        }
        float yaw = Float.isFinite(this.viewYaw) ? Mth.wrapDegrees(this.viewYaw) : 0.0F;
        short jumpAge = (short) Mth.clamp(this.jumpAgeMs, 0, 1000);
        short actionAge = (short) Mth.clamp(this.actionAgeMs, 0, 1000);
        return new ArcadeInputFrame(this.tick, x, z, yaw, this.coupled, this.held, this.pressed, jumpAge, actionAge);
    }

    public boolean isHeld(int action) {
        return (this.held & action) != 0;
    }

    public boolean wasPressed(int action) {
        return (this.pressed & action) != 0;
    }

    public double stickMagnitude() {
        return Math.sqrt((double) this.stickX * this.stickX + (double) this.stickZ * this.stickZ);
    }

    public ArcadeInputFrame withTick(int tick) {
        return new ArcadeInputFrame(tick, this.stickX, this.stickZ, this.viewYaw, this.coupled, this.held, this.pressed, this.jumpAgeMs, this.actionAgeMs);
    }
}
