package fr.hugman.mubble.platformer.network;

import fr.hugman.mubble.platformer.sim.PlatformerInputFrame;
import fr.hugman.mubble.platformer.sim.MoveResult;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;

/**
 * One step of platformer movement as a client took it, for the server to check and follow.
 * <p>
 * It goes out right before the vanilla move packet of the same tick, which the server handles after
 * it: by then the server knows where the step is allowed to end.
 *
 * @param frame       what the player asked for
 * @param from        where the step started
 * @param result      what the world did with the displacement on the client
 * @param fingerprint the fingerprint of the state of the client after the step
 * @param impulse     the velocity something outside the simulation set right before the step, if any
 */
public record PlatformerInputPayload(PlatformerInputFrame frame, Vec3 from, MoveResult result, long fingerprint, Optional<Vec3> impulse) implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, PlatformerInputPayload> STREAM_CODEC = StreamCodec.composite(
            PlatformerInputFrame.STREAM_CODEC, PlatformerInputPayload::frame,
            Vec3.STREAM_CODEC, PlatformerInputPayload::from,
            MoveResult.STREAM_CODEC, PlatformerInputPayload::result,
            ByteBufCodecs.LONG, PlatformerInputPayload::fingerprint,
            Vec3.STREAM_CODEC.apply(ByteBufCodecs::optional), PlatformerInputPayload::impulse,
            PlatformerInputPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PlatformerPayloadTypes.PLATFORMER_INPUT;
    }
}
