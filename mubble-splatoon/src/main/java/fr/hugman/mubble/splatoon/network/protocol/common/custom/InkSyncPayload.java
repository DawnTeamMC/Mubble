package fr.hugman.mubble.splatoon.network.protocol.common.custom;

import fr.hugman.mubble.splatoon.world.level.ink.ChunkInk;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.ChunkPos;

import java.util.List;

/**
 * Tells a client about the ink of one chunk.
 *
 * @param chunk   the chunk the faces belong to
 * @param replace whether these faces are all the ink of the chunk, which is the case when the chunk is first sent,
 *                rather than the faces that changed since the last update
 * @param faces   the faces, each with its ink or with none when it was cleaned
 * @param coats   the coated blocks, each with its coat or with none when it was cleaned
 */
public record InkSyncPayload(ChunkPos chunk, boolean replace, List<ChunkInk.Entry> faces, List<ChunkInk.CoatEntry> coats) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, InkSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ChunkPos.STREAM_CODEC, InkSyncPayload::chunk,
            ByteBufCodecs.BOOL, InkSyncPayload::replace,
            ChunkInk.Entry.STREAM_CODEC.apply(ByteBufCodecs.list()), InkSyncPayload::faces,
            ChunkInk.CoatEntry.STREAM_CODEC.apply(ByteBufCodecs.list()), InkSyncPayload::coats,
            InkSyncPayload::new
    );

    @Override
    public Type<InkSyncPayload> type() {
        return SplatoonPayloadTypes.INK_SYNC;
    }
}
