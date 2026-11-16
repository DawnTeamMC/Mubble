package fr.hugman.mubble.platformer.network;

import fr.hugman.mubble.platformer.sim.PlatformerState;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * The state the server ended a step in, when it does not match what the client reported.
 * <p>
 * The client takes it as the truth as of that step and replays the steps it took since on top of it,
 * with the inputs and results it remembers: the server does the very same with the inputs on their
 * way, so both end up in step again.
 *
 * @param tick      the tick number of the input frame the state follows, or -1 for a fresh start
 * @param state     the state of the server after that step
 * @param rejected  whether the server also sent the player back, which leaves nothing to replay
 */
public record PlatformerCorrectionPayload(int tick, PlatformerState state, boolean rejected) implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, PlatformerCorrectionPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, PlatformerCorrectionPayload::tick,
            PlatformerState.STREAM_CODEC, PlatformerCorrectionPayload::state,
            ByteBufCodecs.BOOL, PlatformerCorrectionPayload::rejected,
            PlatformerCorrectionPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PlatformerPayloadTypes.PLATFORMER_CORRECTION;
    }
}
