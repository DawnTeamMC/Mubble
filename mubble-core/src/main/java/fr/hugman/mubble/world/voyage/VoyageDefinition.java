package fr.hugman.mubble.world.voyage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.RegistryFileCodec;
import org.jspecify.annotations.Nullable;

/**
 * One voyage: a graph of nodes, as written in a data pack.
 *
 * <pre>{@code
 * // data/<namespace>/mubble/voyage/<id>.json
 * {
 *   "display_name": "POC Voyage",
 *   "start": "dawn",
 *   "nodes": {
 *     "dawn":       { "trial": "mubble-testmod:trial_dawn",       "next": ["crossroads"] },
 *     "crossroads": { "waystation": "mubble-testmod:way_crossroads", "next": ["toxic", "shifting"] },
 *     "toxic":      { "trial": "mubble-testmod:trial_toxic",      "next": ["finish"] },
 *     "shifting":   { "trial": "mubble-testmod:trial_shifting",   "next": ["finish"] },
 *     "finish":     { "trial": "mubble-testmod:trial_plain" }
 *   },
 *   "completion_rewards": [{ "item": "minecraft:carrot", "count": 1 }]
 * }
 * }</pre>
 *
 * <p><strong>A node's key is its address, and its seed comes from that key alone.</strong> That is
 * the whole reason routes can branch and rejoin without breaking a shared seed: {@code toxic} looks
 * the same whether the player reached it first or second, and whichever way they went, the route
 * they did not take was already decided. Player choice cannot desync the stream because there is no
 * stream — every node is derived independently from {@code hash(voyage_seed, key)}.
 *
 * <p>Still a stand-in for the real thing, which has acts, bosses and three-way Waystation choices,
 * but the shape is no longer a lie: this is a graph, and growing one adds nodes rather than changing
 * how anything reads them.
 *
 * @param displayName       what the player is told they are starting
 * @param start             the key of the node the voyage begins at
 * @param nodes             every node, by key
 * @param completionRewards what finishing hands over
 */
public record VoyageDefinition(
        Component displayName,
        String start,
        Map<String, VoyageNode> nodes,
        List<VoyageReward> completionRewards
) {
    public static final Codec<VoyageDefinition> DIRECT_CODEC = RecordCodecBuilder.<VoyageDefinition>create(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("display_name").forGetter(VoyageDefinition::displayName),
            Codec.STRING.fieldOf("start").forGetter(VoyageDefinition::start),
            Codec.unboundedMap(Codec.STRING, VoyageNode.CODEC).fieldOf("nodes").forGetter(VoyageDefinition::nodes),
            VoyageReward.CODEC.listOf().optionalFieldOf("completion_rewards", List.of()).forGetter(VoyageDefinition::completionRewards)
    ).apply(instance, VoyageDefinition::new)).validate(VoyageDefinition::validate);

    /** For referencing a voyage by id. */
    public static final Codec<Holder<VoyageDefinition>> CODEC =
            RegistryFileCodec.create(MubbleRegistries.VOYAGE, DIRECT_CODEC);

    public @Nullable VoyageNode node(String key) {
        return this.nodes.get(key);
    }

    /**
     * {@return how many trials the longest route through this voyage runs}
     *
     * <p>Used only for "trial 2 of 3". Exact when every route is the same length, which is what
     * branches that rejoin usually are; an upper bound otherwise, which is the honest thing to show
     * a player who has not chosen yet.
     */
    public int longestTrialCount() {
        return trialsFrom(this.start, this.nodes, new HashMap<>());
    }

    private static int trialsFrom(String key, Map<String, VoyageNode> nodes, Map<String, Integer> memo) {
        Integer known = memo.get(key);
        if (known != null) {
            return known;
        }
        VoyageNode node = nodes.get(key);
        int longestTail = 0;
        for (String next : node.next()) {
            longestTail = Math.max(longestTail, trialsFrom(next, nodes, memo));
        }
        int total = longestTail + (node.isTrial() ? 1 : 0);
        memo.put(key, total);
        return total;
    }

    /**
     * Rejects a graph nobody could finish.
     *
     * <p>Worth doing at load rather than discovering in play: a dangling key strands a player in a
     * level with no way out, and a cycle walks them round it forever. Both are silent until somebody
     * is inside one.
     */
    private static DataResult<VoyageDefinition> validate(VoyageDefinition voyage) {
        if (voyage.nodes.isEmpty()) {
            return DataResult.error(() -> "A voyage must have at least one node");
        }
        if (!voyage.nodes.containsKey(voyage.start)) {
            return DataResult.error(() -> "A voyage starts at '" + voyage.start + "', which is not one of its nodes");
        }

        for (Map.Entry<String, VoyageNode> entry : voyage.nodes.entrySet()) {
            for (String next : entry.getValue().next()) {
                if (!voyage.nodes.containsKey(next)) {
                    return DataResult.error(() -> "Node '" + entry.getKey() + "' leads to '" + next + "', which does not exist");
                }
            }
        }

        List<String> cycle = findCycle(voyage.start, voyage.nodes);
        if (cycle != null) {
            return DataResult.error(() -> "A voyage cannot loop: " + String.join(" -> ", cycle));
        }
        return DataResult.success(voyage);
    }

    /** {@return the path of a cycle reachable from {@code start}}, or {@code null} if there is none. */
    private static @Nullable List<String> findCycle(String start, Map<String, VoyageNode> nodes) {
        Set<String> finished = new HashSet<>();
        Set<String> onPath = new HashSet<>();
        Deque<String> path = new ArrayDeque<>();
        return findCycle(start, nodes, finished, onPath, path);
    }

    private static @Nullable List<String> findCycle(
            String key, Map<String, VoyageNode> nodes, Set<String> finished, Set<String> onPath, Deque<String> path) {
        if (finished.contains(key)) {
            return null;
        }
        if (!onPath.add(key)) {
            // Report the loop itself, not the walk that led into it. `path` is a stack, so it reads
            // most-recent-first; everything down to the earlier sighting of this node is the loop.
            List<String> walked = List.copyOf(path);
            List<String> loop = new ArrayList<>(walked.subList(0, walked.indexOf(key) + 1).reversed());
            loop.add(key);
            return List.copyOf(loop);
        }

        path.push(key);
        for (String next : nodes.get(key).next()) {
            List<String> cycle = findCycle(next, nodes, finished, onPath, path);
            if (cycle != null) {
                return cycle;
            }
        }
        path.pop();

        onPath.remove(key);
        finished.add(key);
        return null;
    }
}
