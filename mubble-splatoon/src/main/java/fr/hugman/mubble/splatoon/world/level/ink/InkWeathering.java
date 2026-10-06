package fr.hugman.mubble.splatoon.world.level.ink;

import fr.hugman.mubble.splatoon.world.attribute.SplatoonEnvironmentAttributes;
import fr.hugman.mubble.splatoon.world.level.attachment.SplatoonAttachmentTypes;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Makes ink go away over time: it dries off once {@link SplatoonEnvironmentAttributes#INK_LIFETIME} has passed since
 * it was last painted, and rain washes it off the surfaces it falls on, unless
 * {@link SplatoonEnvironmentAttributes#RAIN_WASHES_INK} says otherwise.
 *
 * <p>Only loaded chunks holding ink are looked at, each at its own pace so that they do not all get swept on the
 * same tick. Drying is checked rarely, since lifetimes are counted in days; rain is checked every second and takes
 * a share of the cells each time, so puddles of ink fade away over a few seconds instead of popping out.
 *
 * @author Hugman
 * @since v4.0.0
 */
public final class InkWeathering {
    public static final int DRY_INTERVAL = 200;
    public static final int RAIN_INTERVAL = 20;
    public static final float RAIN_WASH_CHANCE = 0.25F;

    private static final Map<ServerLevel, LongSet> INKED_CHUNKS = new IdentityHashMap<>();

    private InkWeathering() {
    }

    static void track(ServerLevel level, ChunkPos pos) {
        INKED_CHUNKS.computeIfAbsent(level, l -> new LongOpenHashSet()).add(pos.pack());
    }

    public static void onChunkLoad(ServerLevel level, LevelChunk chunk) {
        if (chunk.hasAttached(SplatoonAttachmentTypes.CHUNK_INK)) {
            track(level, chunk.getPos());
        }
    }

    public static void onChunkUnload(ServerLevel level, LevelChunk chunk) {
        var chunks = INKED_CHUNKS.get(level);
        if (chunks != null) {
            chunks.remove(chunk.getPos().pack());
        }
    }

    public static void forget(ServerLevel level) {
        INKED_CHUNKS.remove(level);
    }

    public static void tick(ServerLevel level) {
        var chunks = INKED_CHUNKS.get(level);
        if (chunks == null || chunks.isEmpty()) {
            return;
        }
        long time = level.getGameTime();
        boolean raining = level.isRaining();

        for (long key : chunks.toLongArray()) {
            long phase = time + HashCommon.mix(key);
            boolean dry = Math.floorMod(phase, DRY_INTERVAL) == 0;
            boolean rain = raining && Math.floorMod(phase, RAIN_INTERVAL) == 0;
            if (!dry && !rain) {
                continue;
            }

            var chunkPos = ChunkPos.unpack(key);
            var chunk = level.getChunkSource().getChunkNow(chunkPos.x(), chunkPos.z());
            var ink = InkLevel.get(chunk);
            if (ink == null || ink.isEmpty()) {
                chunks.remove(key);
                continue;
            }
            sweep(level, chunk, ink, time, dry, rain);
        }
    }

    private static void sweep(ServerLevel level, LevelChunk chunk, ChunkInk ink, long time, boolean dry, boolean rain) {
        List<Wash> washes = new ArrayList<>();
        ink.forEach((pos, side, face) -> {
            if (dry) {
                int lifetime = level.environmentAttributes().getValue(SplatoonEnvironmentAttributes.INK_LIFETIME, pos);
                if (lifetime > 0 && time - face.paintedAt() >= lifetime) {
                    washes.add(new Wash(pos, side, InkGrid.ALL_CELLS));
                    return;
                }
            }
            if (rain && side == Direction.UP
                    && level.isRainingAt(pos.above())
                    && level.environmentAttributes().getValue(SplatoonEnvironmentAttributes.RAIN_WASHES_INK, pos)) {
                long cells = face.cells();
                long washed = 0L;
                for (int cell = 0; cell < InkGrid.CELLS; cell++) {
                    if (InkGrid.has(cells, cell) && level.getRandom().nextFloat() < RAIN_WASH_CHANCE) {
                        washed |= 1L << cell;
                    }
                }
                if (washed != 0L) {
                    washes.add(new Wash(pos, side, washed));
                }
            }
        });
        List<BlockPos> dried = new ArrayList<>();
        ink.forEachCoat((pos, coat) -> {
            if (dry) {
                int lifetime = level.environmentAttributes().getValue(SplatoonEnvironmentAttributes.INK_LIFETIME, pos);
                if (lifetime > 0 && time - coat.paintedAt() >= lifetime) {
                    dried.add(pos);
                    return;
                }
            }
            if (rain && level.isRainingAt(pos)
                    && level.environmentAttributes().getValue(SplatoonEnvironmentAttributes.RAIN_WASHES_INK, pos)
                    && level.getRandom().nextFloat() < RAIN_WASH_CHANCE) {
                dried.add(pos);
            }
        });

        for (var wash : washes) {
            InkLevel.erase(level, chunk, wash.pos(), wash.side(), wash.cells());
        }
        for (var pos : dried) {
            InkLevel.uncoat(level, chunk, pos);
        }
    }

    private record Wash(BlockPos pos, Direction side, long cells) {
    }
}
