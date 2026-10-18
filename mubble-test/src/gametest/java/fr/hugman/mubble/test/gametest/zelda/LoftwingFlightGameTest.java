package fr.hugman.mubble.test.gametest.zelda;

import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.zelda.world.entity.ZeldaEntityTypes;
import fr.hugman.mubble.zelda.world.entity.animal.loftwing.Loftwing;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;

/**
 * A loftwing left alone has to actually fly. These run the real AI rather than poking at flags,
 * because the first thing the bird did in a live world was hop on the spot: its move control was
 * being handed one tick of lift at a time and having gravity switched back on underneath it.
 */
public class LoftwingFlightGameTest {
	private static final BlockPos GROUND = new BlockPos(4, Arena.FLOOR_Y + 1, 3);
	/** Long enough to tell flight from a hop, short enough not to hold the suite up. */
	private static final int PATIENCE = 120;

	/** The one that was broken: a wild loftwing climbs away instead of bouncing on the floor. */
	@GameTest(maxTicks = PATIENCE + 20)
	public void awildLoftwingTakesToTheAir(GameTestHelper helper) {
		Arena.buildFloor(helper);
		Loftwing loftwing = helper.spawn(ZeldaEntityTypes.LOFTWING, GROUND);
		double startY = loftwing.getY();

		helper.succeedWhen(() -> {
			helper.assertTrue(loftwing.getY() > startY + 2.0,
					"a wild loftwing should climb away, not hop on the spot (it is at " + loftwing.getY() + ", started at " + startY + ")");
			helper.assertTrue(loftwing.isFlying(), "a loftwing off the ground should have its wings out");
		});
	}

	/** Hopping looks like flight for a tick at a time, so check it stays up rather than bounces. */
	@GameTest(maxTicks = PATIENCE + 40)
	public void awildLoftwingStaysUpOnceItIsUp(GameTestHelper helper) {
		Arena.buildFloor(helper);
		Loftwing loftwing = helper.spawn(ZeldaEntityTypes.LOFTWING, GROUND);
		double startY = loftwing.getY();

		helper.succeedWhen(() -> {
			helper.assertTrue(loftwing.getY() > startY + 3.0, "the loftwing never got far enough off the floor");
			helper.assertFalse(loftwing.onGround(), "a flying loftwing should not be touching the ground");
			// The flag belongs to the move control: a bird it is actually steering is held up by it.
			helper.assertTrue(loftwing.isNoGravity(), "a flying loftwing should not be falling between wingbeats");
		});
	}
}
