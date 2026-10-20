package fr.hugman.mubble.test.gametest.zelda;

import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import fr.hugman.mubble.zelda.world.entity.ZeldaEntityTypes;
import fr.hugman.mubble.zelda.world.entity.animal.loftwing.Loftwing;
import fr.hugman.mubble.zelda.world.item.ZeldaItems;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.clock.WorldClocks;
import net.minecraft.world.item.ItemStack;

/**
 * A loftwing left alone has to actually fly. These run the real AI rather than poking at flags,
 * because the first thing the bird did in a live world was hop on the spot: its move control was
 * being handed one tick of lift at a time and having gravity switched back on underneath it.
 */
public class LoftwingFlightGameTest {
	private static final BlockPos GROUND = new BlockPos(4, Arena.FLOOR_Y + 1, 3);
	/** Long enough to tell flight from a hop, short enough not to hold the suite up. */
	private static final int PATIENCE = 120;
	/** A hopping loftwing lands every few ticks, so this many in a row can only be real flight. */
	private static final int SUSTAINED_FLIGHT_TICKS = 40;

	/**
	 * Game tests start at night, and a loftwing is right to go to sleep at night rather than soar.
	 * These tests are about flying, so put the sun up first.
	 */
	private static void makeItMidday(GameTestHelper helper) {
		var level = helper.getLevel();
		var clock = level.registryAccess().lookupOrThrow(Registries.WORLD_CLOCK).getOrThrow(WorldClocks.OVERWORLD);
		level.clockManager().setTotalTicks(clock, MIDDAY);
	}

	private static final long MIDDAY = 6000L;

	/** Everything worth knowing when one of these fails, so the message does the diagnosing. */
	private static String describe(Loftwing loftwing, double startY) {
		return "[y=" + String.format("%.2f", loftwing.getY()) + " from " + String.format("%.2f", startY)
				+ " onGround=" + loftwing.onGround()
				+ " noGravity=" + loftwing.isNoGravity()
				+ " flying=" + loftwing.isFlying()
				+ " baby=" + loftwing.isBaby()
				+ " free=" + loftwing.isFreeToRoam()
				+ " wantedY=" + String.format("%.1f", loftwing.getMoveControl().getWantedY())
				+ " delta=" + loftwing.getDeltaMovement() + "]";
	}

	/** The one that was broken: a wild loftwing climbs away instead of bouncing on the floor. */
	@GameTest(maxTicks = PATIENCE + 20)
	public void awildLoftwingTakesToTheAir(GameTestHelper helper) {
		Arena.buildFloor(helper);
		makeItMidday(helper);
		Loftwing loftwing = helper.spawn(ZeldaEntityTypes.LOFTWING, GROUND);
		double startY = loftwing.getY();

		helper.succeedWhen(() -> {
			makeItMidday(helper);
			helper.assertTrue(loftwing.getY() > startY + 2.0,
					"a wild loftwing should climb away, not hop on the spot " + describe(loftwing, startY));
			helper.assertTrue(loftwing.isFlying(), "a loftwing off the ground should have its wings out " + describe(loftwing, startY));
		});
	}

	/**
	 * The difference between flying and hopping is not how high the bird gets — in an arena eight
	 * blocks tall that is a coin toss — but whether it keeps touching back down. A hopping loftwing
	 * lands every few ticks; a flying one does not land at all.
	 */
	@GameTest(maxTicks = PATIENCE + 60)
	public void awildLoftwingStopsTouchingTheGround(GameTestHelper helper) {
		Arena.buildFloor(helper);
		makeItMidday(helper);
		Loftwing loftwing = helper.spawn(ZeldaEntityTypes.LOFTWING, GROUND);
		int[] airborneTicks = {0};

		helper.succeedWhen(() -> {
			makeItMidday(helper);
			airborneTicks[0] = loftwing.onGround() ? 0 : airborneTicks[0] + 1;
			helper.assertTrue(airborneTicks[0] >= SUSTAINED_FLIGHT_TICKS,
					"the loftwing keeps landing: it is hopping, not flying (longest run off the ground so far: " + airborneTicks[0] + " ticks)");
		});
	}

	/**
	 * Take-off has to read the rider's jump from {@link net.minecraft.world.entity.LivingEntity#isJumping()},
	 * which both sides keep, and not from the server's copy of the input packet: a mount carrying a
	 * player is simulated by that player's client, so a {@code ServerPlayer} lookup comes back empty
	 * precisely where it is needed, and the bird never leaves the ground.
	 * <p>
	 * How far it then climbs cannot be checked here for that same reason — with no client in a game
	 * test, vanilla zeroes a ridden mount's movement server side. That part needs a real client.
	 */
	@GameTest(maxTicks = PATIENCE)
	public void ariddenLoftwingTakesOffOnTheSecondJump(GameTestHelper helper) {
		Arena.buildFloor(helper);
		makeItMidday(helper);
		Loftwing loftwing = helper.spawn(ZeldaEntityTypes.LOFTWING, GROUND);
		ServerPlayer rider = TestPlayers.at(helper, GROUND);

		loftwing.bondWith(rider);
		loftwing.setItemSlot(EquipmentSlot.SADDLE, new ItemStack(ZeldaItems.LOFTWING_BELT));
		helper.assertTrue(rider.startRiding(loftwing), "the owner should be able to climb onto a belted loftwing");
		helper.assertTrue(loftwing.getControllingPassenger() == rider, "the owner should be the one steering");
		helper.assertFalse(loftwing.isFlying(), "the loftwing should start on its feet");

		helper.succeedWhen(() -> {
			makeItMidday(helper);
			rider.setJumping(true);
			helper.assertTrue(loftwing.isFlying(), "holding jump never threw the loftwing into flight");
		});
	}
}
