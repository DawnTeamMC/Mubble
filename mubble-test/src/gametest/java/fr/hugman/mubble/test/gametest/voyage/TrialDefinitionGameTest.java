package fr.hugman.mubble.test.gametest.voyage;

import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.voyage.VoyageDefinition;
import fr.hugman.mubble.world.voyage.VoyageSeeds;
import fr.hugman.mubble.world.voyage.trial.TrialDefinition;
import fr.hugman.mubble.world.voyage.trial.TrialInstance;
import fr.hugman.mubble.world.voyage.trial.TrialPlatform;
import java.util.HashSet;
import java.util.Set;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.block.Blocks;

/**
 * Trials and voyages as data packs actually load them.
 *
 * <p>Both live in dynamic registries and reference each other by id, so nothing here can be reached
 * from a unit test: resolving {@code mubble-gametest:two_trials} into two {@link TrialDefinition}s
 * needs a server that has finished loading its data packs.
 */
public class TrialDefinitionGameTest {
    private static final Identifier SEEDED = Identifier.fromNamespaceAndPath("mubble-gametest", "seeded");
    private static final Identifier DEFAULTED = Identifier.fromNamespaceAndPath("mubble-gametest", "defaulted");
    private static final Identifier TWO_TRIALS = Identifier.fromNamespaceAndPath("mubble-gametest", "two_trials");

    @GameTest
    public void dataPackTrialsAreLoaded(GameTestHelper helper) {
        Registry<TrialDefinition> registry = trials(helper);

        TrialDefinition seeded = registry.getValue(SEEDED);
        helper.assertTrue(seeded != null, SEEDED + " was not loaded from the data pack");
        helper.assertValueEqual(seeded.displayName().getString(), "Seeded Trial", "the trial display name");
        helper.assertValueEqual(seeded.platform().block(), Blocks.DEEPSLATE_TILES, "the platform block");

        helper.succeed();
    }

    /** The acceptance criterion is that a new trial needs no Java, which means every field defaults. */
    @GameTest
    public void aTrialMayLeaveThePlatformOut(GameTestHelper helper) {
        TrialDefinition defaulted = trials(helper).getValue(DEFAULTED);

        helper.assertTrue(defaulted != null, DEFAULTED + " was not loaded from the data pack");
        helper.assertValueEqual(defaulted.platform(), TrialPlatform.DEFAULT, "the platform of a trial that omits it");

        helper.succeed();
    }

    @GameTest
    public void aVoyageResolvesItsTrialsInOrder(GameTestHelper helper) {
        VoyageDefinition voyage = voyages(helper).getValue(TWO_TRIALS);
        helper.assertTrue(voyage != null, TWO_TRIALS + " was not loaded from the data pack");

        helper.assertValueEqual(voyage.trials().size(), 2, "the number of trials");
        helper.assertValueEqual(voyage.trials().getFirst().unwrapKey().orElseThrow().identifier(), SEEDED, "the first trial");
        helper.assertValueEqual(voyage.trials().get(1).unwrapKey().orElseThrow().identifier(), DEFAULTED, "the second trial");
        helper.assertValueEqual(voyage.completionRewards().size(), 1, "the number of rewards");

        helper.succeed();
    }

    /**
     * The whole seed path, from a voyage seed to a resolved sky colour.
     *
     * <p>Same seed, same colour; different seeds, eventually every colour. A resolver that quietly
     * always picked the first candidate would pass every codec test and fail only this one.
     */
    @GameTest
    public void aTrialResolvesItsEnvironmentFromTheSeed(GameTestHelper helper) {
        TrialDefinition seeded = trials(helper).getValue(SEEDED);
        helper.assertTrue(seeded != null, SEEDED + " was not loaded from the data pack");

        helper.assertValueEqual(skyFor(seeded, 7L), skyFor(seeded, 7L), "the sky colour for one voyage seed");

        Set<Integer> seen = new HashSet<>();
        for (long voyageSeed = 0; voyageSeed < 100; voyageSeed++) {
            seen.add(skyFor(seeded, voyageSeed));
        }
        helper.assertValueEqual(seen.size(), 4, "the number of distinct skies over 100 voyage seeds");

        // The fixed half of the same profile must not move with the seed.
        helper.assertValueEqual(
                seeded.environment().value().attributes().fixed().applyModifier(EnvironmentAttributes.FOG_COLOR, 0),
                0xFF123456, "the fog colour, which the profile fixes");

        helper.succeed();
    }

    /** A node keeps its seed wherever it sits, which is what makes a run code mean one voyage. */
    @GameTest
    public void trialsAtDifferentNodesResolveDifferently(GameTestHelper helper) {
        TrialDefinition seeded = trials(helper).getValue(SEEDED);
        helper.assertTrue(seeded != null, SEEDED + " was not loaded from the data pack");

        long first = TrialInstance.of(SEEDED, VoyageDefinition.nodePath(0), seeded, 99L).nodeSeed();
        long second = TrialInstance.of(SEEDED, VoyageDefinition.nodePath(1), seeded, 99L).nodeSeed();

        helper.assertTrue(first != second, "the same trial at two nodes of one voyage got the same seed");
        helper.assertValueEqual(first, VoyageSeeds.node(99L, "0"), "the node seed of the first trial");

        helper.succeed();
    }

    @GameTest
    public void aPlatformIsActuallyBuilt(GameTestHelper helper) {
        // Built inside this test's own region rather than in a voyage level: what is under test is
        // the block placement, and opening a runtime dimension from a game test would be testing
        // Fantasy instead.
        BlockPos origin = helper.absolutePos(new BlockPos(2, 2, 2));
        TrialPlatform platform = new TrialPlatform(Blocks.DEEPSLATE_TILES, 1, origin.getY());

        platform.place(helper.getLevel(), origin.getX(), origin.getZ());

        int y = platform.spawnY() - 1;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos pos = new BlockPos(origin.getX() + x, y, origin.getZ() + z);
                helper.assertTrue(helper.getLevel().getBlockState(pos).is(Blocks.DEEPSLATE_TILES),
                        "the platform is missing a block at " + pos);
            }
        }
        // A radius of one is a three-by-three, so the ring outside it must be untouched.
        helper.assertTrue(!helper.getLevel().getBlockState(new BlockPos(origin.getX() + 2, y, origin.getZ())).is(Blocks.DEEPSLATE_TILES),
                "the platform is wider than its radius");

        helper.succeed();
    }

    private static int skyFor(TrialDefinition trial, long voyageSeed) {
        TrialInstance instance = TrialInstance.of(SEEDED, VoyageDefinition.nodePath(0), trial, voyageSeed);
        return trial.environment().value().attributes()
                .resolveCandidates(instance.nodeSeed())
                .applyModifier(EnvironmentAttributes.SKY_COLOR, 0);
    }

    private static Registry<TrialDefinition> trials(GameTestHelper helper) {
        return helper.getLevel().registryAccess().lookupOrThrow(MubbleRegistries.TRIAL);
    }

    private static Registry<VoyageDefinition> voyages(GameTestHelper helper) {
        return helper.getLevel().registryAccess().lookupOrThrow(MubbleRegistries.VOYAGE);
    }
}
