package fr.hugman.mubble.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;

import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;

/**
 * How much a statistic was allowed to move while the player was inside a trial.
 *
 * <pre>{@code
 * { "type": "minecraft:custom", "stat": "minecraft:jump",   "value": { "max": 0 } }
 * { "type": "minecraft:killed", "stat": "minecraft:zombie", "value": { "max": 0 } }
 * }</pre>
 *
 * <p><strong>A delta, not a total.</strong> Vanilla can already match a total: any advancement's
 * {@code player} predicate takes a {@code stats} list. That answers "has never jumped", which is not
 * what "completed this trial without jumping" means. So the session records the player's statistics
 * on entry and this compares the difference on completion.
 *
 * <p>The vocabulary is vanilla's whole statistic registry rather than anything invented here, so
 * "without jumping", "without killing anything", "without breaking a block", "without taking damage"
 * and several hundred others are all the same two lines of JSON. Anything vanilla counts, a trial
 * can be completed without.
 */
public record StatDelta(Stat<?> stat, MinMaxBounds.Ints value) {
    public static final Codec<StatDelta> CODEC = BuiltInRegistries.STAT_TYPE.byNameCodec()
            .dispatch("type", delta -> delta.stat().getType(), StatDelta::typedCodec);

    /** {@return whether this statistic moved by an allowed amount} */
    public boolean matches(ServerPlayer player, Object2IntMap<Stat<?>> onEntry) {
        return this.value.matches(player.getStats().getValue(this.stat) - onEntry.getInt(this.stat));
    }

    private static <T> MapCodec<StatDelta> typedCodec(StatType<T> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                type.getRegistry().holderByNameCodec().fieldOf("stat").forGetter(delta -> holderOf(type, delta)),
                MinMaxBounds.Ints.CODEC.optionalFieldOf("value", MinMaxBounds.Ints.ANY).forGetter(StatDelta::value)
        ).apply(instance, (holder, value) -> new StatDelta(type.get(holder.value()), value)));
    }

    @SuppressWarnings("unchecked")
    private static <T> Holder<T> holderOf(StatType<T> type, StatDelta delta) {
        // The dispatch guarantees the stat belongs to this type, which is the thing the compiler
        // cannot see through the wildcard on Stat<?>.
        return type.getRegistry().wrapAsHolder((T) delta.stat().getValue());
    }
}
