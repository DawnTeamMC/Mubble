package fr.hugman.mubble.super_mario.world.entity.freeze;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * The block of ice an entity is trapped in, attached to it for as long as it lasts.
 * <p>
 * Both ends are stored as absolute game times rather than as a countdown, so that the whole freeze
 * only has to be sent to the clients once: they hold the very same clock and can work out on their
 * own how far along it is on any given frame. A countdown would have to be synced every single tick.
 *
 * @param startedAt the game time the entity was frozen at
 * @param endsAt    the game time the entity thaws at, unless it breaks free sooner
 */
public record FreezeState(long startedAt, long endsAt) {
    public static final Codec<FreezeState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.LONG.fieldOf("started_at").forGetter(FreezeState::startedAt),
            Codec.LONG.fieldOf("ends_at").forGetter(FreezeState::endsAt)
    ).apply(instance, FreezeState::new));

    public static final StreamCodec<ByteBuf, FreezeState> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, FreezeState::startedAt,
            ByteBufCodecs.VAR_LONG, FreezeState::endsAt,
            FreezeState::new);

    public static FreezeState lasting(long gameTime, int ticks) {
        return new FreezeState(gameTime, gameTime + ticks);
    }

    /** @return how long the entity has been frozen for, in ticks, never below zero */
    public int elapsed(long gameTime) {
        return (int) Math.max(gameTime - this.startedAt, 0L);
    }

    /** @return how much longer the entity stays frozen, in ticks, never below zero */
    public int remaining(long gameTime) {
        return (int) Math.max(this.endsAt - gameTime, 0L);
    }

    public boolean hasExpired(long gameTime) {
        return gameTime >= this.endsAt;
    }

    /**
     * @return the same freeze, cut short by {@code ticks}, never ending before it started
     */
    public FreezeState shortenedBy(int ticks) {
        return new FreezeState(this.startedAt, Math.max(this.endsAt - ticks, this.startedAt));
    }
}
