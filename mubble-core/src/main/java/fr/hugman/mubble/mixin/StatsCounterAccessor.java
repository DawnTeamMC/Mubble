package fr.hugman.mubble.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntMap;

import net.minecraft.stats.Stat;
import net.minecraft.stats.StatsCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Reaches the whole statistic map, which {@link StatsCounter} only exposes one entry at a time.
 *
 * <p>Needed to snapshot a player's statistics when they enter a trial, so an advancement can ask
 * about what happened <em>during</em> it. Asking per statistic would mean knowing which ones to ask
 * about before the advancement that names them has been consulted; copying the map costs one small
 * allocation per trial and needs no such foresight.
 */
@Mixin(StatsCounter.class)
public interface StatsCounterAccessor {
    @Accessor("stats")
    Object2IntMap<Stat<?>> mubble$stats();
}
