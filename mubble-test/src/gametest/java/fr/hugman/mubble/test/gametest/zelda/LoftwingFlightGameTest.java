package fr.hugman.mubble.test.gametest.zelda;

import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import fr.hugman.mubble.zelda.world.entity.ZeldaEntityTypes;
import fr.hugman.mubble.zelda.world.entity.animal.loftwing.Loftwing;
import fr.hugman.mubble.zelda.world.entity.animal.loftwing.LoftwingFlight;
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
 * A loftwing left alone has to actually fly. Those tests run the real AI rather than poking at
 * flags, because the first thing the bird did in a live world was hop on the spot: its move control
 * was being handed one tick of lift at a time and having gravity switched back on underneath it.
 * <p>
 * The ridden half of the file tests the flight model rather than the flying. A mount carrying a
 * player is simulated by that player's client, and vanilla zeroes such a mount's movement on the
 * server, so there is no displacement here to measure: {@code tickRidden} still runs, and what it
 * decides — the speed it builds up, the velocity it asks for, where it points the bird, what it
 * spends — is what these assert. {@code isEffectiveAi} is false under a rider too, so the goals
 * stand down and nothing but the model moves those numbers.
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

	// -- under a rider --------------------------------------------------------------------------

	/** Holding the jump key this long gets a bird off the ground; one beat is all it takes. */
	private static final int TAKEOFF_TICKS = 2;
	/** Long enough for a wingbeat's lift to have bled away, so that a glide is a glide. */
	private static final int SETTLE_TICKS = 25;
	/** Degrees of pitch. The game counts nose-down as positive, so a climb is a negative one. */
	private static final float CLIMB = -30.0F;
	private static final float LEVEL = 0.0F;
	private static final float DIVE = 70.0F;
	/** Long enough to build a dive's worth of speed, the swing into the dive included. */
	private static final int DIVE_TICKS = 40;
	/** How many ticks of each bird's climb are counted, once it is at the climb attitude. */
	private static final int CLIMB_WINDOW = 40;
	/** How much more a dive has to be worth than level flight for the point of diving to be made. */
	private static final double CLIMB_MARGIN = 5.0;
	/** How long holding the jump key takes to run a full reserve down: fifty beats, eight ticks apart. */
	private static final int RESERVE_TICKS = 400;

	/** A belted loftwing with its owner in the saddle, which is the only way one ever flies. */
	private record Ridden(Loftwing loftwing, ServerPlayer rider) {
		/** Where the rider is looking, which is the only steering a loftwing in the air answers to. */
		void pitch(float degrees) {
			this.rider.setXRot(degrees);
		}

		void yaw(float degrees) {
			this.rider.setYRot(degrees);
		}

		void jump(boolean held) {
			this.rider.setJumping(held);
		}

		void sprint(boolean held) {
			this.rider.setSprinting(held);
		}

		void crouch(boolean held) {
			this.rider.setShiftKeyDown(held);
		}

		/** The height the flight model is asking for this tick, in blocks. */
		double climbRate() {
			return this.loftwing.getFlightVelocity().y();
		}

		String describe() {
			return "[flying=" + this.loftwing.isFlying()
					+ " speed=" + String.format("%.3f", this.loftwing.getFlightSpeed())
					+ " pitch=" + String.format("%.1f", this.loftwing.getXRot())
					+ " yaw=" + String.format("%.1f", this.loftwing.getYRot())
					+ " climbRate=" + String.format("%.3f", this.climbRate())
					+ " stamina=" + this.loftwing.getStamina()
					+ " dashing=" + this.loftwing.isDashing() + "]";
		}
	}

	/**
	 * Puts a bird on the ground with its owner aboard, facing due south and looking level.
	 * <p>
	 * The sun goes up once rather than on every tick: a ridden loftwing has no effective AI, so
	 * there is no roost goal left to talk out of going to sleep.
	 */
	private static Ridden saddleUp(GameTestHelper helper, BlockPos pos) {
		makeItMidday(helper);
		Loftwing loftwing = helper.spawn(ZeldaEntityTypes.LOFTWING, pos);
		ServerPlayer rider = TestPlayers.at(helper, pos);
		rider.setYRot(0.0F);
		rider.setXRot(LEVEL);

		loftwing.bondWith(rider);
		loftwing.setItemSlot(EquipmentSlot.SADDLE, new ItemStack(ZeldaItems.LOFTWING_BELT));
		helper.assertTrue(rider.startRiding(loftwing), "the owner should be able to climb onto a belted loftwing");
		helper.assertTrue(loftwing.getControllingPassenger() == rider, "the owner should be the one steering");
		helper.assertFalse(loftwing.isFlying(), "the loftwing should start on its feet");
		return new Ridden(loftwing, rider);
	}

	/**
	 * Take-off has to read the rider's jump from {@link net.minecraft.world.entity.LivingEntity#isJumping()},
	 * which both sides keep, and not from the server's copy of the input packet: a mount carrying a
	 * player is simulated by that player's client, so a {@code ServerPlayer} lookup comes back empty
	 * precisely where it is needed, and the bird never leaves the ground.
	 * <p>
	 * And it has to leave the ground upwards. Take-off used to throw the bird along whatever the
	 * rider was looking at, which with a level gaze was straight into the floor it stood on; it is a
	 * beat of the wings now, paid for out of the reserve like every other beat.
	 */
	@GameTest(maxTicks = PATIENCE)
	public void ariddenLoftwingTakesOffUpwards(GameTestHelper helper) {
		Arena.buildFloor(helper);
		Ridden flier = saddleUp(helper, GROUND);

		helper.startSequence()
				.thenExecuteFor(TAKEOFF_TICKS, () -> flier.jump(true))
				.thenExecute(() -> {
					helper.assertTrue(flier.loftwing().isFlying(), "holding jump never threw the loftwing into flight " + flier.describe());
					helper.assertTrue(flier.climbRate() > 0.0,
							"a loftwing leaving the ground should be going up, not along the rider's level gaze " + flier.describe());
					helper.assertTrue(flier.loftwing().getStamina() < LoftwingFlight.MAX_STAMINA,
							"take-off is a wingbeat, so it should have come out of the reserve " + flier.describe());
				})
				.thenSucceed();
	}

	/**
	 * The turn has inertia now. The bird used to be put on the rider's line of sight at over eleven
	 * degrees a tick, which read as a bird spinning on a pin; it leans into the turn in its own time
	 * instead, and still gets there.
	 */
	@GameTest(maxTicks = PATIENCE + 60)
	public void ariddenLoftwingComesRoundInItsOwnTime(GameTestHelper helper) {
		Arena.buildFloor(helper);
		Ridden flier = saddleUp(helper, GROUND);
		// A quarter turn is far enough that snapping onto it would be unmistakable.
		float target = 90.0F;

		helper.startSequence()
				.thenExecuteFor(TAKEOFF_TICKS, () -> flier.jump(true))
				.thenExecute(() -> {
					flier.jump(false);
					flier.yaw(target);
				})
				.thenExecuteAfter(5, () -> {
					float turned = flier.loftwing().getYRot();
					helper.assertTrue(turned > 1.0F, "the loftwing is not coming round to the rider at all " + flier.describe());
					helper.assertTrue(turned < target / 3.0F,
							"the loftwing snapped onto the rider's heading instead of leaning into it " + flier.describe());
				})
				.thenExecuteAfter(60, () -> helper.assertTrue(flier.loftwing().getYRot() > target - 1.0F,
						"the loftwing never finished the turn " + flier.describe()))
				.thenSucceed();
	}

	/**
	 * A wingbeat is what buys height, and the only thing that does. A gliding bird is sinking; one
	 * beat of its wings and it is going up, out of the reserve.
	 */
	@GameTest(maxTicks = PATIENCE + 40)
	public void awingbeatBuysHeightOutOfTheReserve(GameTestHelper helper) {
		Arena.buildFloor(helper);
		Ridden flier = saddleUp(helper, GROUND);
		int[] reserveBeforeTheBeat = {0};

		helper.startSequence()
				.thenExecuteFor(TAKEOFF_TICKS, () -> flier.jump(true))
				.thenExecute(() -> flier.jump(false))
				.thenExecuteAfter(SETTLE_TICKS, () -> {
					helper.assertTrue(flier.loftwing().isFlying(), "the loftwing came down before the test got going " + flier.describe());
					helper.assertTrue(flier.climbRate() < 0.0, "a loftwing that is not beating its wings should be sinking " + flier.describe());
					reserveBeforeTheBeat[0] = flier.loftwing().getStamina();
					flier.jump(true);
				})
				.thenExecuteAfter(2, () -> {
					helper.assertTrue(flier.climbRate() > 0.0, "a wingbeat should turn a sink into a climb " + flier.describe());
					helper.assertTrue(flier.loftwing().getStamina() < reserveBeforeTheBeat[0],
							"a wingbeat should cost wingbeats (the reserve stood at " + reserveBeforeTheBeat[0] + ") " + flier.describe());
				})
				.thenSucceed();
	}

	/**
	 * Landing takes nothing but letting go, even with the nose up. The bird used to hold whatever
	 * height it was given until the rider climbed off it, because powered flight carried no gravity
	 * of its own.
	 */
	@GameTest(maxTicks = PATIENCE + 80)
	public void lettingGoBringsALoftwingDown(GameTestHelper helper) {
		Arena.buildFloor(helper);
		Ridden flier = saddleUp(helper, GROUND);
		int[] sinkingTicks = {0};
		int[] climbingTicks = {0};

		helper.startSequence()
				.thenExecuteFor(TAKEOFF_TICKS, () -> flier.jump(true))
				.thenExecute(() -> {
					flier.jump(false);
					// Nose up and still coming down: without a wingbeat there is nothing to climb on.
					flier.pitch(CLIMB);
				})
				.thenExecuteAfter(SETTLE_TICKS, () -> helper.assertTrue(flier.loftwing().isFlying(),
						"the loftwing stopped flying before the glide was measured " + flier.describe()))
				.thenExecuteFor(80, () -> {
					if (flier.climbRate() < 0.0) {
						sinkingTicks[0]++;
					} else {
						climbingTicks[0]++;
					}
				})
				.thenExecute(() -> helper.assertTrue(climbingTicks[0] == 0,
						"a loftwing nobody is beating the wings of should lose height on every tick, and it gained on "
								+ climbingTicks[0] + " of " + (sinkingTicks[0] + climbingTicks[0]) + " " + flier.describe()))
				.thenSucceed();
	}

	/**
	 * An empty reserve grounds a loftwing, it does not stall one. The nose stays exactly where the
	 * rider put it: the old model pushed the pitch back to level once the wingbeats ran out, which
	 * took the bird off the rider at the worst possible moment.
	 */
	@GameTest(maxTicks = RESERVE_TICKS + PATIENCE)
	public void anemptyReserveNeverDropsTheNose(GameTestHelper helper) {
		Arena.buildFloor(helper);
		Ridden flier = saddleUp(helper, GROUND);

		helper.startSequence()
				.thenExecuteFor(RESERVE_TICKS, () -> flier.jump(true))
				.thenExecute(() -> {
					helper.assertTrue(flier.loftwing().getStamina() < LoftwingFlight.FLAP_COST,
							"the reserve should be spent by now, or this proves nothing about an empty one " + flier.describe());
					flier.jump(false);
					flier.pitch(CLIMB);
				})
				.thenExecuteAfter(20, () -> helper.assertTrue(Math.abs(flier.loftwing().getXRot() - CLIMB) < 0.5F,
						"an empty reserve should still let the rider hold the nose up " + flier.describe()))
				.thenSucceed();
	}

	/**
	 * The one the whole model is for: speed is height in the bank. Two birds are told to climb at
	 * the very same angle, and the one that spent the last two seconds diving gains far more out of
	 * it than the one that spent them flying level, because it brings a dive's speed into the climb.
	 * <p>
	 * Only the ticks each bird is actually at the climb attitude are counted. Both get the same
	 * order at the same moment, but the diving one has a hundred degrees of pull-up to get through
	 * first, and that part of the arc is not the climb.
	 */
	@GameTest(maxTicks = 220)
	public void adiveIsWhatPaysForAClimb(GameTestHelper helper) {
		Arena.buildFloor(helper);
		Ridden diver = saddleUp(helper, new BlockPos(1, Arena.FLOOR_Y + 1, 1));
		Ridden glider = saddleUp(helper, new BlockPos(6, Arena.FLOOR_Y + 1, 6));
		double[] climbed = {0.0, 0.0};
		int[] counted = {0, 0};
		double[] entrySpeed = {0.0, 0.0};

		helper.startSequence()
				.thenExecuteFor(TAKEOFF_TICKS, () -> {
					diver.jump(true);
					glider.jump(true);
				})
				// From here on neither bird beats a wing: all either of them has is what it carries.
				.thenExecute(() -> {
					diver.jump(false);
					glider.jump(false);
					diver.pitch(DIVE);
					glider.pitch(LEVEL);
				})
				.thenExecuteAfter(DIVE_TICKS, () -> {
					entrySpeed[0] = diver.loftwing().getFlightSpeed();
					entrySpeed[1] = glider.loftwing().getFlightSpeed();
					helper.assertTrue(entrySpeed[0] > entrySpeed[1],
							"the dive bought no speed, so there is nothing here to tell apart " + diver.describe() + " against " + glider.describe());
					diver.pitch(CLIMB);
					glider.pitch(CLIMB);
				})
				.thenExecuteFor(80, () -> {
					if (atTheClimbAttitude(diver) && counted[0] < CLIMB_WINDOW) {
						climbed[0] += diver.climbRate();
						counted[0]++;
					}
					if (atTheClimbAttitude(glider) && counted[1] < CLIMB_WINDOW) {
						climbed[1] += glider.climbRate();
						counted[1]++;
					}
				})
				.thenExecute(() -> {
					String tally = "dived " + String.format("%.2f", climbed[0]) + " blocks over " + counted[0] + " ticks from "
							+ String.format("%.3f", entrySpeed[0]) + " b/t, level " + String.format("%.2f", climbed[1])
							+ " over " + counted[1] + " ticks from " + String.format("%.3f", entrySpeed[1]) + " b/t";
					helper.assertTrue(counted[0] == CLIMB_WINDOW && counted[1] == CLIMB_WINDOW,
							"the two climbs were not measured over the same window, so the comparison is worthless (" + tally + ")");
					helper.assertTrue(climbed[0] > 0.0, "the climb out of a dive should gain height (" + tally + ")");
					helper.assertTrue(climbed[0] > climbed[1] + CLIMB_MARGIN,
							"diving first should be worth far more height than the same climb flown out of level flight (" + tally + ")");
				})
				.thenSucceed();
	}

	private static boolean atTheClimbAttitude(Ridden flier) {
		return Math.abs(flier.loftwing().getXRot() - CLIMB) < 0.5F;
	}

	/**
	 * Speed widens a turn. A bird that has just dived out of the clouds is not meant to pivot the
	 * way one drifting along at its slowest can, so the two are given the same quarter turn to make
	 * and the fast one gets much less of the way through it.
	 */
	@GameTest(maxTicks = 220)
	public void speedWidensALoftwingsTurn(GameTestHelper helper) {
		Arena.buildFloor(helper);
		Ridden fast = saddleUp(helper, new BlockPos(1, Arena.FLOOR_Y + 1, 1));
		Ridden slow = saddleUp(helper, new BlockPos(6, Arena.FLOOR_Y + 1, 6));
		float target = 90.0F;
		double[] speeds = {0.0, 0.0};

		helper.startSequence()
				.thenExecuteFor(TAKEOFF_TICKS, () -> {
					fast.jump(true);
					slow.jump(true);
				})
				.thenExecute(() -> {
					fast.jump(false);
					slow.jump(false);
					fast.pitch(DIVE);
					slow.pitch(LEVEL);
				})
				// Both hold the pitch they are on through the turn, so neither one's speed moves.
				.thenExecuteAfter(DIVE_TICKS, () -> {
					speeds[0] = fast.loftwing().getFlightSpeed();
					speeds[1] = slow.loftwing().getFlightSpeed();
					helper.assertTrue(speeds[0] > speeds[1] * 2.0,
							"the dive bought no speed, so there is nothing here to tell apart " + fast.describe() + " against " + slow.describe());
					fast.yaw(target);
					slow.yaw(target);
				})
				.thenExecuteAfter(10, () -> {
					float fastTurn = fast.loftwing().getYRot();
					float slowTurn = slow.loftwing().getYRot();
					String tally = "fast came round " + String.format("%.1f", fastTurn) + " degrees at "
							+ String.format("%.3f", speeds[0]) + " b/t, slow " + String.format("%.1f", slowTurn)
							+ " at " + String.format("%.3f", speeds[1]) + " b/t";
					helper.assertTrue(fastTurn > 1.0F, "the fast loftwing is not coming round at all (" + tally + ")");
					helper.assertTrue(slowTurn > fastTurn * 1.5F, "speed should widen the turn, and here it barely did (" + tally + ")");
				})
				.thenSucceed();
	}

	/**
	 * The dash is on the sprint key, which is the one key a rider in the saddle has left: jumping is
	 * the wingbeat and the gaze is the steering.
	 */
	@GameTest(maxTicks = PATIENCE)
	public void thedashIsOnTheSprintKey(GameTestHelper helper) {
		Arena.buildFloor(helper);
		Ridden flier = saddleUp(helper, GROUND);

		helper.startSequence()
				.thenExecuteFor(TAKEOFF_TICKS, () -> flier.jump(true))
				.thenExecute(() -> {
					flier.jump(false);
					flier.sprint(true);
				})
				.thenExecuteAfter(5, () -> {
					helper.assertTrue(flier.rider().isSprinting(), "the rider let go of the sprint, so this proves nothing " + flier.describe());
					helper.assertTrue(flier.loftwing().isDashing(), "the sprint key should ask the loftwing for a dash " + flier.describe());
				})
				.thenSucceed();
	}

	/**
	 * And never on crouch. Crouching is how vanilla gets a rider off a mount, so a dash bound to it
	 * threw the rider off and left the belt to catch them — which is also why this one has to watch
	 * every tick rather than look once: the bird spends some of them with nobody steering it.
	 */
	@GameTest(maxTicks = PATIENCE)
	public void crouchingNeverDashesALoftwing(GameTestHelper helper) {
		Arena.buildFloor(helper);
		Ridden flier = saddleUp(helper, GROUND);

		helper.startSequence()
				.thenExecuteFor(TAKEOFF_TICKS, () -> flier.jump(true))
				.thenExecute(() -> flier.jump(false))
				.thenExecuteFor(20, () -> {
					flier.crouch(true);
					helper.assertFalse(flier.loftwing().isDashing(), "crouching should get a rider off the bird, never dash it " + flier.describe());
				})
				.thenSucceed();
	}
}
