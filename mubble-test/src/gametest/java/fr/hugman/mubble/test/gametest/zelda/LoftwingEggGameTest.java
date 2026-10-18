package fr.hugman.mubble.test.gametest.zelda;

import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.zelda.world.entity.animal.loftwing.Loftwing;
import fr.hugman.mubble.zelda.world.level.block.ZeldaBlocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;

/**
 * The three stages of a loftwing egg. The roll is left alone here: what the tests check is that an
 * egg schedules itself, that each stage knows the one after it, and that the last one leaves a
 * chick behind rather than another egg.
 */
public class LoftwingEggGameTest {
	private static final BlockPos NEST = new BlockPos(4, Arena.FLOOR_Y + 1, 3);

	/** Every stage is its own block, in the order the issue asks for. */
	@GameTest
	public void thethreeStagesAreThreeBlocks(GameTestHelper helper) {
		helper.assertTrue(ZeldaBlocks.LOFTWING_EGG != ZeldaBlocks.CRACKED_LOFTWING_EGG, "the first two stages should be different blocks");
		helper.assertTrue(ZeldaBlocks.CRACKED_LOFTWING_EGG != ZeldaBlocks.HATCHING_LOFTWING_EGG, "the last two stages should be different blocks");
		helper.succeed();
	}

	/** Placing an egg has to put a tick on the calendar, or it would sit there for ever. */
	@GameTest
	public void aplacedEggWaitsForNoon(GameTestHelper helper) {
		Arena.buildFloor(helper);
		helper.setBlock(NEST, ZeldaBlocks.LOFTWING_EGG);

		var absolute = helper.absolutePos(NEST);
		helper.assertTrue(helper.getLevel().getBlockTicks().hasScheduledTick(absolute, ZeldaBlocks.LOFTWING_EGG),
				"a freshly laid egg should be waiting for the next midday");

		helper.succeed();
	}

	/** Forced through every stage, an egg ends as a chick and not as a block. */
	@GameTest(maxTicks = 400)
	public void aforcedEggHatchesIntoAChick(GameTestHelper helper) {
		Arena.buildFloor(helper);
		helper.setBlock(NEST, ZeldaBlocks.LOFTWING_EGG);

		var absolute = helper.absolutePos(NEST);
		var level = helper.getLevel();
		// Keep rolling the scheduled tick by hand until the egg gives in; the odds make the wait,
		// not the rule, and the test has no business waiting twenty in-game days for it.
		for (int attempt = 0; attempt < 2000 && !level.getBlockState(absolute).isAir(); attempt++) {
			level.getBlockState(absolute).tick(level, absolute, level.getRandom());
		}

		helper.assertBlockPresent(Blocks.AIR, NEST);
		helper.assertEntityPresent(fr.hugman.mubble.zelda.world.entity.ZeldaEntityTypes.LOFTWING);
		helper.assertTrue(helper.getLevel().getEntitiesOfClass(Loftwing.class, helper.getBounds()).getFirst().isBaby(),
				"an egg should hatch into a chick, not a grown bird");

		helper.succeed();
	}
}
