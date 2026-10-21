package fr.hugman.mubble.test.gametest.zelda;

import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestClock;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import fr.hugman.mubble.zelda.world.entity.ZeldaEntityTypes;
import fr.hugman.mubble.zelda.world.entity.animal.loftwing.Loftwing;
import fr.hugman.mubble.zelda.world.entity.animal.loftwing.LoftwingCall;
import fr.hugman.mubble.zelda.world.item.ZeldaItems;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;

/**
 * Blowing the whistle. The rescue is the whole point of the item: a fall is over in under two
 * seconds, so a bird that merely set off towards its owner would arrive to find them on the floor.
 * <p>
 * The owner is a mock player, and the level never drives one through its physics, so a fall here is
 * a state held still rather than a real descent: feet off the ground, a drop already behind them,
 * and a downward velocity. That is exactly the state the bird reads, and holding it still is what
 * makes the deadline below worth measuring. Standing still has to be spelled out for the same
 * reason — nothing ever set {@code onGround} on a player the server does not simulate.
 */
public class LoftwingWhistleGameTest {
	private static final BlockPos PERCH = new BlockPos(1, Arena.FLOOR_Y + 1, 1);
	/** Across the arena and well out of reach, so the bird has a distance to close. */
	private static final BlockPos MIDAIR = new BlockPos(6, Arena.FLOOR_Y + 6, 6);
	private static final BlockPos GROUND = new BlockPos(6, Arena.FLOOR_Y + 1, 5);

	/** A drop far past anything a jump could account for. */
	private static final double LETHAL_DROP = 20.0;
	private static final Vec3 FALLING = new Vec3(0.0, -0.8, 0.0);

	/**
	 * What the rescue is held to: its own deadline, plus the two or three ticks the goal selector
	 * takes to pick the call up. It has to stay inside the second a fall gives you.
	 */
	private static final int CATCH_DEADLINE = LoftwingCall.RESCUE_TICKS + 5;
	/** Long enough for a bird to cross the arena under its own wings. */
	private static final int FLIGHT_PATIENCE = 60;
	/** Further than any pair of wings covers inside the deadline, so only the blink explains a catch. */
	private static final double FAR_ABOVE = 100.0;
	/** Long enough for a bird to reach an owner who has only just stepped off. */
	private static final int STEP_OFF_TICKS = 20;
	/**
	 * A step no pair of wings takes in one tick. A loftwing crosses under a block per tick, so
	 * anything near this is the bird blinking rather than flying.
	 */
	private static final double TELEPORT_STEP = 3.0;

	/** The one that matters: whistle on the way down and you end up in the saddle, not on the floor. */
	@GameTest(maxTicks = CATCH_DEADLINE + 40)
	public void afallingOwnerIsCaughtAndEndsUpInTheSaddle(GameTestHelper helper) {
		Loftwing loftwing = beltedLoftwing(helper);
		ServerPlayer owner = fallingOwner(helper, loftwing, MIDAIR);

		helper.assertTrue(whistle(helper, owner) == InteractionResult.SUCCESS, "the whistle should report that a bird is coming");
		helper.assertTrue(loftwing.getCaller() == owner, "the bird should have taken the call");

		helper.startSequence()
				.thenExecuteFor(CATCH_DEADLINE, () -> {
					if (!owner.isPassenger()) {
						keepFalling(owner);
					}
				})
				.thenExecute(() -> {
					helper.assertTrue(owner.getVehicle() == loftwing,
							"a falling owner should be in the saddle within " + CATCH_DEADLINE + " ticks, and is instead "
									+ String.format("%.1f", loftwing.distanceTo(owner)) + " blocks from the bird");
					helper.assertValueEqual((float) owner.fallDistance, 0.0F, "the fall distance of a caught owner");
					helper.assertTrue(loftwing.isFlying(), "a bird that just caught somebody should have its wings out");
				})
				.thenSucceed();
	}

	/**
	 * The distance the blink is there for, and the claim the whole feature rests on: the bird comes
	 * from wherever it was. A hundred blocks is two and a half seconds of diving at its very best,
	 * so a bird that passes this one flew almost none of it — it crossed the gap in one beat, which
	 * is the only way a fall is ever caught in time.
	 */
	@GameTest(maxTicks = CATCH_DEADLINE + 40)
	public void abirdAwholeSkyAwayStillCatchesInTime(GameTestHelper helper) {
		Loftwing loftwing = beltedLoftwing(helper);
		ServerPlayer owner = fallingOwner(helper, loftwing, MIDAIR);
		Vec3 perch = loftwing.position();

		loftwing.snapTo(perch.x(), owner.getY() + FAR_ABOVE, perch.z(), 0.0F, 0.0F);
		helper.assertTrue(loftwing.canHear(owner), "a bird this far up should still be well within earshot");
		helper.assertTrue(whistle(helper, owner) == InteractionResult.SUCCESS, "the whistle should report that a bird is coming");

		helper.startSequence()
				.thenExecuteFor(CATCH_DEADLINE, () -> {
					if (!owner.isPassenger()) {
						keepFalling(owner);
					}
				})
				.thenExecute(() -> helper.assertTrue(owner.getVehicle() == loftwing,
						"a bird " + FAR_ABOVE + " blocks up should still have caught its owner within " + CATCH_DEADLINE
								+ " ticks, and is instead " + String.format("%.1f", loftwing.distanceTo(owner)) + " blocks away"))
				.thenSucceed();
	}

	/**
	 * Whistling at the top of the fall rather than halfway down it. Two ticks in, nothing has
	 * dropped far enough to be worth catching, and a bird already standing right there would
	 * otherwise count itself arrived and go back to its own business before the fall became real.
	 */
	@GameTest(maxTicks = STEP_OFF_TICKS + CATCH_DEADLINE + 40)
	public void abirdWaitsOutTheStartOfAfall(GameTestHelper helper) {
		Loftwing loftwing = beltedLoftwing(helper);
		// Right beside the bird, so it has nothing to fly and every excuse to stand down.
		ServerPlayer owner = fallingOwner(helper, loftwing, PERCH.above());
		owner.fallDistance = 0.0;

		helper.assertFalse(loftwing.canCatch(owner), "a fall this young is not yet one worth catching");
		helper.assertTrue(whistle(helper, owner) == InteractionResult.SUCCESS, "the whistle should report that a bird is coming");

		helper.startSequence()
				.thenExecuteFor(STEP_OFF_TICKS, () -> {
					owner.setOnGround(false);
					owner.setDeltaMovement(FALLING);
				})
				.thenExecute(() -> {
					helper.assertTrue(loftwing.getCaller() == owner,
							"the bird should still be on the call: its owner's feet never touched the ground");
					// The fall becomes one worth catching.
					owner.fallDistance = LETHAL_DROP;
				})
				.thenExecuteFor(CATCH_DEADLINE, () -> {
					if (!owner.isPassenger()) {
						keepFalling(owner);
					}
				})
				.thenExecute(() -> helper.assertTrue(owner.getVehicle() == loftwing,
						"the bird stood down before the fall became one worth catching"))
				.thenSucceed();
	}

	/**
	 * The limit, the way the horse call has one in Breath of the Wild. The bird is sent straight up
	 * rather than off across the world on purpose: the chunk stays loaded, so the whistle still
	 * finds the bird and the range is the only thing that can have turned it down.
	 */
	@GameTest
	public void abirdOutOfEarshotNeverComes(GameTestHelper helper) {
		Loftwing loftwing = beltedLoftwing(helper);
		ServerPlayer owner = fallingOwner(helper, loftwing, MIDAIR);
		Vec3 perch = loftwing.position();

		loftwing.snapTo(perch.x(), owner.getY() + LoftwingCall.RANGE + 16.0, perch.z(), 0.0F, 0.0F);

		helper.assertFalse(loftwing.canHear(owner), "a bird that far up should be out of earshot");
		helper.assertTrue(whistle(helper, owner) == InteractionResult.CONSUME, "a whistle nothing answers should not report a success");
		helper.assertTrue(loftwing.getCaller() == null, "a bird out of earshot should never have been called");
		helper.assertTrue(owner.getVehicle() == null, "an owner out of range should be left to fall");

		// The control: the same bird and the same whistle, back within earshot.
		loftwing.snapTo(perch.x(), perch.y(), perch.z(), 0.0F, 0.0F);
		helper.assertTrue(loftwing.canHear(owner), "a bird across the arena should be well within earshot");
		helper.assertTrue(whistle(helper, owner) == InteractionResult.SUCCESS, "back in range, the same bird should answer");
		helper.assertTrue(loftwing.getCaller() == owner, "back in range, the same bird should take the call");

		helper.succeed();
	}

	/**
	 * Nothing changes for an owner standing safely on the ground: the bird flies the whole way.
	 * Blinking in beside somebody who is in no danger would be free, and a far poorer sight than a
	 * loftwing gliding down.
	 */
	@GameTest(maxTicks = FLIGHT_PATIENCE + 40)
	public void anownerOnTheirFeetGetsAbirdThatFlies(GameTestHelper helper) {
		Loftwing loftwing = beltedLoftwing(helper);
		ServerPlayer owner = standingOwner(helper, loftwing);

		double startDistance = loftwing.distanceTo(owner);
		helper.assertTrue(whistle(helper, owner) == InteractionResult.SUCCESS, "the whistle should report that a bird is coming");

		Vec3[] previous = {loftwing.position()};
		double[] longestStep = {0.0};
		double[] closest = {startDistance};

		helper.startSequence()
				.thenExecuteFor(FLIGHT_PATIENCE, () -> {
					owner.setOnGround(true);
					longestStep[0] = Math.max(longestStep[0], loftwing.position().distanceTo(previous[0]));
					previous[0] = loftwing.position();
					closest[0] = Math.min(closest[0], loftwing.distanceTo(owner));
				})
				.thenExecute(() -> {
					helper.assertTrue(owner.getVehicle() == null, "an owner standing on the ground should not be put in the saddle");
					helper.assertTrue(longestStep[0] < TELEPORT_STEP,
							"the bird blinked instead of flying: " + String.format("%.1f", longestStep[0]) + " blocks in one tick");
					helper.assertTrue(closest[0] < startDistance, "the bird never set off towards its owner at all");
				})
				.thenSucceed();
	}

	/**
	 * Without the belt there is nothing to carry anyone with, so the bird answers and arrives and
	 * that is all it can do. Letting it come is what tells an owner which of the two is missing.
	 */
	@GameTest(maxTicks = CATCH_DEADLINE + 60)
	public void abareBackedBirdComesButCatchesNobody(GameTestHelper helper) {
		Loftwing loftwing = loftwing(helper);
		ServerPlayer owner = fallingOwner(helper, loftwing, MIDAIR);

		helper.assertFalse(loftwing.hasBelt(), "this bird is meant to be bare-backed");
		helper.assertTrue(whistle(helper, owner) == InteractionResult.SUCCESS, "a bird without its belt should still answer");
		helper.assertFalse(loftwing.canCatch(owner), "a bird without its belt has nothing to catch anyone with");

		Vec3[] previous = {loftwing.position()};
		double[] longestStep = {0.0};

		helper.startSequence()
				.thenExecuteFor(CATCH_DEADLINE + 20, () -> {
					longestStep[0] = Math.max(longestStep[0], loftwing.position().distanceTo(previous[0]));
					previous[0] = loftwing.position();
					keepFalling(owner);
				})
				.thenExecute(() -> {
					helper.assertTrue(owner.getVehicle() == null, "an owner was carried by a bird wearing no belt");
					helper.assertTrue(longestStep[0] < TELEPORT_STEP,
							"a bird that cannot catch anyone has no business blinking: "
									+ String.format("%.1f", longestStep[0]) + " blocks in one tick");
				})
				.thenSucceed();
	}

	/** The guards on the call: a bird answers its own owner, and only once it is old enough to fly them. */
	@GameTest
	public void achickAndSomebodyElsesBirdBothStayPut(GameTestHelper helper) {
		Loftwing chick = beltedLoftwing(helper);
		ServerPlayer owner = fallingOwner(helper, chick, MIDAIR);
		chick.setBaby(true);

		helper.assertFalse(chick.answerCall(owner), "a chick is too young to fly to anyone");
		helper.assertTrue(whistle(helper, owner) == InteractionResult.CONSUME, "a whistle only a chick could hear should not report a success");

		Loftwing stranger = beltedLoftwing(helper);
		helper.assertFalse(stranger.answerCall(owner), "a loftwing that never picked you should ignore your whistle");

		helper.succeed();
	}

	/** Holds the owner in the state of someone a second into a fall they will not walk away from. */
	private static void keepFalling(ServerPlayer owner) {
		owner.setOnGround(false);
		owner.setDeltaMovement(FALLING);
		owner.fallDistance = LETHAL_DROP;
	}

	private static InteractionResult whistle(GameTestHelper helper, ServerPlayer player) {
		player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ZeldaItems.LOFTWING_WHISTLE));
		return ZeldaItems.LOFTWING_WHISTLE.use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
	}

	/** An owner of {@code loftwing}, hanging at {@code pos} on their way down. */
	private static ServerPlayer fallingOwner(GameTestHelper helper, Loftwing loftwing, BlockPos pos) {
		ServerPlayer owner = owner(helper, loftwing, pos);
		keepFalling(owner);
		return owner;
	}

	/** An owner of {@code loftwing}, on their feet and in no danger at all. */
	private static ServerPlayer standingOwner(GameTestHelper helper, Loftwing loftwing) {
		ServerPlayer owner = owner(helper, loftwing, GROUND);
		owner.setOnGround(true);
		owner.fallDistance = 0.0;
		return owner;
	}

	private static ServerPlayer owner(GameTestHelper helper, Loftwing loftwing, BlockPos pos) {
		ServerPlayer owner = TestPlayers.at(helper, pos);
		owner.setGameMode(GameType.SURVIVAL);
		loftwing.bondWith(owner);
		return owner;
	}

	private static Loftwing beltedLoftwing(GameTestHelper helper) {
		Loftwing loftwing = loftwing(helper);
		loftwing.setItemSlot(EquipmentSlot.SADDLE, new ItemStack(ZeldaItems.LOFTWING_BELT));
		return loftwing;
	}

	private static Loftwing loftwing(GameTestHelper helper) {
		Arena.buildFloor(helper);
		TestClock.midday(helper);
		return helper.spawn(ZeldaEntityTypes.LOFTWING, PERCH);
	}
}
