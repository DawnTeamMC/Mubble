package fr.hugman.mubble.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.List;
import java.util.Optional;

import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;

/**
 * Fires when a player completes a trial.
 *
 * <pre>{@code
 * // in an advancement's criteria
 * {
 *   "trigger": "mubble:trial_completed",
 *   "conditions": {
 *     "trial": "mubble-testmod:trial_toxic",
 *     "stats": [{ "type": "minecraft:custom", "stat": "minecraft:jump", "value": { "max": 0 } }]
 *   }
 * }
 * }</pre>
 *
 * <p>Every field is optional, so the bare trigger means "completed any trial". The advancement says
 * which trial it wants, which is the way round the request asked for: content decides, and adding an
 * advancement for a new trial needs no Java.
 *
 * <p><strong>Trials only.</strong> Walking through a waystation does not fire this, or "complete
 * every trial" would be quietly satisfiable by strolling.
 *
 * @param trial the trial definition that must have been the one completed
 * @param voyage the voyage it must have been completed inside
 * @param stats how much each statistic was allowed to move during the trial — see {@link StatDelta}
 */
public class TrialCompletedTrigger extends SimpleCriterionTrigger<TrialCompletedTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    /**
     * @param statsOnEntry the player's statistics as they were when the trial started, so that a
     *                     condition can ask about the trial rather than about the whole save
     */
    public void trigger(ServerPlayer player, Identifier trial, Identifier voyage, Object2IntMap<Stat<?>> statsOnEntry) {
        this.trigger(player, instance -> instance.matches(player, trial, voyage, statsOnEntry));
    }

    public record TriggerInstance(
            Optional<ContextAwarePredicate> player,
            Optional<Identifier> trial,
            Optional<Identifier> voyage,
            List<StatDelta> stats
    ) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                Identifier.CODEC.optionalFieldOf("trial").forGetter(TriggerInstance::trial),
                Identifier.CODEC.optionalFieldOf("voyage").forGetter(TriggerInstance::voyage),
                StatDelta.CODEC.listOf().optionalFieldOf("stats", List.of()).forGetter(TriggerInstance::stats)
        ).apply(instance, TriggerInstance::new));

        boolean matches(ServerPlayer player, Identifier trial, Identifier voyage, Object2IntMap<Stat<?>> statsOnEntry) {
            if (this.trial.isPresent() && !this.trial.get().equals(trial)) {
                return false;
            }
            if (this.voyage.isPresent() && !this.voyage.get().equals(voyage)) {
                return false;
            }
            return this.stats.stream().allMatch(delta -> delta.matches(player, statsOnEntry));
        }
    }
}
