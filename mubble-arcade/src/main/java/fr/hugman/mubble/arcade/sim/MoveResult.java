package fr.hugman.mubble.arcade.sim;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * What the world did with the displacement a step asked for.
 *
 * @param dx                 how far the player actually moved along x
 * @param dy                 how far the player actually moved along y
 * @param dz                 how far the player actually moved along z
 * @param onGround           whether the player stands on something afterwards
 * @param horizontalCollision whether a wall stopped part of the horizontal motion
 * @param verticalCollision  whether a floor or a ceiling stopped part of the vertical motion
 */
public record MoveResult(double dx, double dy, double dz, boolean onGround, boolean horizontalCollision, boolean verticalCollision) {
    public static final StreamCodec<ByteBuf, MoveResult> STREAM_CODEC = StreamCodec.of(
            (buf, result) -> {
                var out = new FriendlyByteBuf(buf);
                out.writeDouble(result.dx);
                out.writeDouble(result.dy);
                out.writeDouble(result.dz);
                out.writeBoolean(result.onGround);
                out.writeBoolean(result.horizontalCollision);
                out.writeBoolean(result.verticalCollision);
            },
            buf -> {
                var in = new FriendlyByteBuf(buf);
                return new MoveResult(in.readDouble(), in.readDouble(), in.readDouble(), in.readBoolean(), in.readBoolean(), in.readBoolean());
            }
    );

    public double horizontalDistance() {
        return Math.sqrt(this.dx * this.dx + this.dz * this.dz);
    }

    public boolean isFinite() {
        return Double.isFinite(this.dx) && Double.isFinite(this.dy) && Double.isFinite(this.dz);
    }
}
