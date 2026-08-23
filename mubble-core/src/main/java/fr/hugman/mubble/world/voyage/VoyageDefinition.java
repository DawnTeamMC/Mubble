package fr.hugman.mubble.world.voyage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.voyage.trial.TrialDefinition;
import java.util.List;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.RegistryFileCodec;

/**
 * One voyage, as written in a data pack.
 *
 * <pre>{@code
 * // data/<namespace>/voyage/<id>.json
 * {
 *   "display_name": "POC Voyage",
 *   "trials": ["mubble-testmod:trial_dawn", "mubble-testmod:trial_shifting"],
 *   "completion_rewards": [{ "item": "minecraft:carrot", "count": 1 }]
 * }
 * }</pre>
 *
 * <p><strong>A flat ordered list is a POC stand-in.</strong> The real structure is a tree: three-way
 * Waystation choices, branches that rejoin, acts ending in a boss. What matters is that nothing
 * downstream assumes the list — trials are reached by {@linkplain #nodePath(int) node path}, which is
 * already the addressing scheme a tree needs, so growing one changes how paths are produced and not
 * how anything consumes them.
 *
 * @param displayName       what the player is told they are starting
 * @param trials            the trials to run, in order; at least one
 * @param completionRewards what finishing the whole voyage hands over
 */
public record VoyageDefinition(
        Component displayName,
        List<Holder<TrialDefinition>> trials,
        List<VoyageReward> completionRewards
) {
    public static final Codec<VoyageDefinition> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("display_name").forGetter(VoyageDefinition::displayName),
            TrialDefinition.CODEC.listOf().validate(VoyageDefinition::requireTrials).fieldOf("trials").forGetter(VoyageDefinition::trials),
            // Rewards are phase 5. Carrying the list now costs nothing and means the testmod content
            // written for this phase does not have to be rewritten when phase 5 starts handing it out.
            VoyageReward.CODEC.listOf().optionalFieldOf("completion_rewards", List.of()).forGetter(VoyageDefinition::completionRewards)
    ).apply(instance, VoyageDefinition::new));

    /** For referencing a voyage by id. */
    public static final Codec<Holder<VoyageDefinition>> CODEC =
            RegistryFileCodec.create(MubbleRegistries.VOYAGE, DIRECT_CODEC);

    /**
     * {@return the node path of the trial at {@code index}}
     *
     * <p>The node path is the address a trial has inside the voyage tree, and the only thing besides
     * the voyage seed that its {@linkplain VoyageSeeds#node node seed} is derived from. Flat for now;
     * branching will extend a path rather than replace the scheme, so a trial keeps its seed however
     * the player got to it.
     */
    public static String nodePath(int index) {
        return Integer.toString(index);
    }

    private static DataResult<List<Holder<TrialDefinition>>> requireTrials(List<Holder<TrialDefinition>> trials) {
        return trials.isEmpty()
                ? DataResult.error(() -> "A voyage must have at least one trial")
                : DataResult.success(trials);
    }
}
