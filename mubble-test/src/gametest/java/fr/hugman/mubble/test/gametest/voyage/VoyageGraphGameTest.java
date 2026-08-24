package fr.hugman.mubble.test.gametest.voyage;

import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import fr.hugman.mubble.world.voyage.VoyageDefinition;
import fr.hugman.mubble.world.voyage.VoyageNode;
import java.util.List;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

/**
 * The shape of a voyage graph, and the graphs that are refused.
 *
 * <p>Validation is the point of this file. A dangling route strands a player in a level with no way
 * out and a cycle walks them round it forever — both are invisible until somebody is inside one, so
 * they have to be caught while the data pack loads.
 *
 * <p>A game test rather than a unit test because nodes reference trials and waystations by id, and
 * resolving one needs the registries a running server has.
 */
public class VoyageGraphGameTest {
    private static final String WAY = "mubble-gametest:fork";
    private static final String TRIAL = "mubble-gametest:seeded";

    @GameTest
    public void aNodeMustBeExactlyOneKind(GameTestHelper helper) {
        helper.assertTrue(parseNode(helper, "{}").isError(), "a node naming neither kind was accepted");
        helper.assertTrue(parseNode(helper, """
                { "trial": "%s", "waystation": "%s" }
                """.formatted(TRIAL, WAY)).isError(), "a node naming both kinds was accepted");
        helper.assertTrue(parseNode(helper, """
                { "waystation": "%s" }
                """.formatted(WAY)).isSuccess(), "a plain waystation node was rejected");

        helper.succeed();
    }

    @GameTest
    public void aDanglingRouteIsRefused(GameTestHelper helper) {
        DataResult<VoyageDefinition> result = parse(helper, voyage("a", """
                "a": { "waystation": "%s", "next": ["nowhere"] }
                """.formatted(WAY)));

        helper.assertTrue(result.isError(), "a dangling route was accepted");
        helper.assertTrue(error(result).contains("nowhere"), "the error should name the missing node: " + error(result));

        helper.succeed();
    }

    @GameTest
    public void anUnknownStartIsRefused(GameTestHelper helper) {
        DataResult<VoyageDefinition> result = parse(helper, voyage("elsewhere", """
                "a": { "waystation": "%s" }
                """.formatted(WAY)));

        helper.assertTrue(result.isError(), "a start naming no node was accepted");
        helper.assertTrue(error(result).contains("elsewhere"), "the error should name the missing start: " + error(result));

        helper.succeed();
    }

    @GameTest
    public void aLoopIsRefused(GameTestHelper helper) {
        DataResult<VoyageDefinition> result = parse(helper, voyage("a", """
                "a": { "waystation": "%s", "next": ["b"] },
                "b": { "waystation": "%s", "next": ["c"] },
                "c": { "waystation": "%s", "next": ["a"] }
                """.formatted(WAY, WAY, WAY)));

        helper.assertTrue(result.isError(), "a cycle was accepted; a player would walk it forever");
        helper.assertTrue(error(result).contains("a -> b -> c -> a"),
                "the error should show the loop, it said: " + error(result));

        helper.succeed();
    }

    /**
     * The case the whole feature rests on: two ways round that rejoin.
     *
     * <p>A depth-first search that confused "seen before" with "on the current path" would call this
     * a cycle and refuse every branching voyage there is.
     */
    @GameTest
    public void convergingRoutesAreNotALoop(GameTestHelper helper) {
        DataResult<VoyageDefinition> result = parse(helper, voyage("start", """
                "start":  { "waystation": "%s", "next": ["left", "right"] },
                "left":   { "trial": "%s", "next": ["finish"] },
                "right":  { "trial": "%s", "next": ["finish"] },
                "finish": { "trial": "%s" }
                """.formatted(WAY, TRIAL, TRIAL, TRIAL)));

        helper.assertTrue(result.isSuccess(), "converging routes were rejected: " + error(result));

        VoyageDefinition voyage = result.getOrThrow();
        helper.assertValueEqual(voyage.node("start").next(), List.of("left", "right"), "the routes offered");
        // Two trials on either route, plus the waystation which does not count.
        helper.assertValueEqual(voyage.longestTrialCount(), 2, "the trials on the longest route");

        helper.succeed();
    }

    @GameTest
    public void waystationsDoNotCountAsTrials(GameTestHelper helper) {
        VoyageDefinition voyage = parse(helper, voyage("a", """
                "a": { "waystation": "%s", "next": ["b"] },
                "b": { "waystation": "%s" }
                """.formatted(WAY, WAY))).getOrThrow();

        helper.assertValueEqual(voyage.longestTrialCount(), 0, "the trials in a voyage made only of waystations");
        helper.assertTrue(!voyage.node("a").isTrial(), "a waystation reported itself as a trial");

        helper.succeed();
    }

    private static String voyage(String start, String nodes) {
        return """
                { "display_name": "Test", "start": "%s", "nodes": { %s } }
                """.formatted(start, nodes);
    }

    private static DataResult<VoyageDefinition> parse(GameTestHelper helper, String json) {
        var ops = helper.getLevel().registryAccess().createSerializationContext(JsonOps.INSTANCE);
        return VoyageDefinition.DIRECT_CODEC.parse(ops, JsonParser.parseString(json));
    }

    private static DataResult<VoyageNode> parseNode(GameTestHelper helper, String json) {
        var ops = helper.getLevel().registryAccess().createSerializationContext(JsonOps.INSTANCE);
        return VoyageNode.CODEC.parse(ops, JsonParser.parseString(json));
    }

    private static String error(DataResult<?> result) {
        return result.error().map(Object::toString).orElse("(no error)");
    }
}
