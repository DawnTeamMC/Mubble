package fr.hugman.mubble.world.voyage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.world.voyage.trial.TrialDefinition;
import fr.hugman.mubble.world.voyage.waystation.WaystationDefinition;
import java.util.List;
import java.util.Optional;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;

/**
 * One step of a voyage, and where it can go next.
 *
 * <pre>{@code
 * "crossroads": { "waystation": "mubble-testmod:way_crossroads", "next": ["left", "right"] },
 * "left":       { "trial": "mubble-testmod:trial_toxic",         "next": ["finish"] },
 * "finish":     { "trial": "mubble-testmod:trial_shifting" }
 * }</pre>
 *
 * <p>Exactly one of {@code trial} or {@code waystation}. An empty or absent {@code next} ends the
 * voyage; one entry moves straight on; several make the player choose.
 *
 * @param trial      the trial this node runs, if it is one
 * @param waystation the waystation this node is, if it is one
 * @param next       the keys of the nodes this one can lead to, in the order they are offered
 */
public record VoyageNode(
        Optional<Holder<TrialDefinition>> trial,
        Optional<Holder<WaystationDefinition>> waystation,
        List<String> next
) {
    public static final Codec<VoyageNode> CODEC = RecordCodecBuilder.<VoyageNode>create(instance -> instance.group(
            TrialDefinition.CODEC.optionalFieldOf("trial").forGetter(VoyageNode::trial),
            WaystationDefinition.CODEC.optionalFieldOf("waystation").forGetter(VoyageNode::waystation),
            Codec.STRING.listOf().optionalFieldOf("next", List.of()).forGetter(VoyageNode::next)
    ).apply(instance, VoyageNode::new)).validate(VoyageNode::requireExactlyOneKind);

    /** {@return what this node puts the player in}, whichever kind it is. */
    public VoyageNodeContent content() {
        return this.trial.<VoyageNodeContent>map(Holder::value)
                .orElseGet(() -> this.waystation.orElseThrow().value());
    }

    /**
     * {@return whether finishing this node is something an advancement can be earned for}
     *
     * <p>Only trials. Passing through a waystation is not an accomplishment, and letting one fire
     * the trigger would make "complete every trial" quietly satisfiable by walking.
     */
    public boolean isTrial() {
        return this.trial.isPresent();
    }

    /** {@return the id of this node's definition}, for logging and for advancement matching. */
    public Identifier contentId() {
        return this.trial.map(holder -> holder.unwrapKey().orElseThrow().identifier())
                .orElseGet(() -> this.waystation.orElseThrow().unwrapKey().orElseThrow().identifier());
    }

    private static DataResult<VoyageNode> requireExactlyOneKind(VoyageNode node) {
        if (node.trial.isPresent() == node.waystation.isPresent()) {
            return DataResult.error(() -> "A voyage node must name exactly one of 'trial' or 'waystation'");
        }
        return DataResult.success(node);
    }
}
