package fr.hugman.mubble.test.gametest.zelda;

import fr.hugman.mubble.test.gametest.support.LoftwingPlateau;
import fr.hugman.mubble.test.gametest.support.TestEnvironments;
import fr.hugman.mubble.zelda.world.entity.ZeldaEntityTypes;
import fr.hugman.mubble.zelda.world.entity.animal.loftwing.Loftwing;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/**
 * What a loftwing does with its feet on the ground, which is where every absurd thing one did in a
 * live world happened: spinning on the spot, and hauling itself back towards a circle it rode long
 * ago.
 * <p>
 * These run the real AI in a plateau wide enough for the bird to behave: it looks for a perch within
 * twenty-four blocks and rides circles twenty blocks across, so nothing it does fits in the eight
 * blocks a game test is handed by default.
 */
public class LoftwingGroundGameTest {
	/** The middle of the plateau, far enough from its edges that the bird never samples past them. */
	private static final BlockPos MIDDLE = new BlockPos(LoftwingPlateau.SIZE / 2, LoftwingPlateau.FLOOR_Y + 1, LoftwingPlateau.SIZE / 2);

	/** Long enough for a habit to show, short enough not to hold the suite up. */
	private static final int WATCH_TICKS = 400;
	/**
	 * Skipped before anything is measured. A mob's move control is handed its first heading with the
	 * bird pointing wherever it spawned, and coming round to it is one honest snap; everything after
	 * that is the bird steering itself.
	 */
	private static final int SETTLING_TICKS = 20;
	/**
	 * A bird turns its body a few degrees at a time. The move control of a flying mob is allowed a
	 * quarter turn per tick, which is what a bird aimed at a point it is already standing on uses.
	 */
	private static final float MAX_TURN_PER_TICK = 20.0F;
	/** And over a whole watch, settling a heading is worth a lap or two, never dozens. */
	private static final float MAX_TOTAL_TURN = 720.0F;
	/**
	 * Where the bird is carried off to, far enough out that the two cases cannot be confused: a
	 * circle it still rides keeps the point it steers for one radius from where the circle was
	 * picked, while any circle centred near the bird puts that point further out than that.
	 */
	private static final BlockPos CARRIED_OFF = new BlockPos(LoftwingPlateau.SIZE - 6, LoftwingPlateau.FLOOR_Y + 1, LoftwingPlateau.SIZE - 6);
	/** One radius of the circle, plus enough slack for the bird to have drifted before it was picked. */
	private static final double OFF_THE_OLD_CIRCLE = 28.0;

	/** Everything worth knowing when one of these fails, so the message does the diagnosing. */
	private static String describe(Loftwing loftwing) {
		var moveControl = loftwing.getMoveControl();
		return "[y=" + String.format("%.2f", loftwing.getY())
				+ " yRot=" + String.format("%.1f", loftwing.getYRot())
				+ " onGround=" + loftwing.onGround()
				+ " noGravity=" + loftwing.isNoGravity()
				+ " flying=" + loftwing.isFlying()
				+ " sitting=" + loftwing.isOrderedToSit()
				+ " roosting=" + loftwing.isRoosting()
				+ " panicking=" + loftwing.isPanicking()
				+ " free=" + loftwing.isFreeToRoam()
				+ " navDone=" + loftwing.getNavigation().isDone()
				+ " wanted=" + String.format("%.1f/%.1f/%.1f", moveControl.getWantedX(), moveControl.getWantedY(), moveControl.getWantedZ())
				+ " delta=" + loftwing.getDeltaMovement() + "]";
	}

	/**
	 * A loftwing that cannot climb away should look like a bird standing on a rock, not like a
	 * spinning top. The quarter turn a tick is the giveaway: it is what the move control falls back
	 * on when it is asked to aim the bird at a point the bird is already standing under, where the
	 * direction to steer in is nothing but rounding error.
	 */
	@GameTest(structure = LoftwingPlateau.STRUCTURE, environment = TestEnvironments.MIDDAY,
			maxTicks = SETTLING_TICKS + WATCH_TICKS + 40, padding = LoftwingPlateau.PADDING)
	public void aperchedLoftwingDoesNotSpinOnTheSpot(GameTestHelper helper) {
		LoftwingPlateau.buildHollow(helper);
		Loftwing loftwing = helper.spawn(ZeldaEntityTypes.LOFTWING, MIDDLE);

		watchTheTurning(helper, loftwing, "a loftwing under an overhang", () -> {
		});
	}

	/**
	 * The circle a loftwing rides belongs to the stretch of sky it rides it in. Carry the bird away
	 * and that circle is a place it used to be; still steering for it is how a bird that has come
	 * down ends up hauling itself back towards where it fell from.
	 */
	@GameTest(structure = LoftwingPlateau.STRUCTURE, environment = TestEnvironments.MIDDAY, maxTicks = 160, padding = LoftwingPlateau.PADDING)
	public void acarriedOffLoftwingRidesANewCircle(GameTestHelper helper) {
		LoftwingPlateau.buildPlateau(helper);
		Loftwing loftwing = helper.spawn(ZeldaEntityTypes.LOFTWING, MIDDLE);
		// Where the circle gets picked: on its first tick, with the bird still where it spawned.
		Vec3 circledHere = helper.absoluteVec(new Vec3(MIDDLE.getX() + 0.5, MIDDLE.getY(), MIDDLE.getZ() + 0.5));

		helper.startSequence()
				// Long enough to have settled on a circle and started riding it.
				.thenExecuteFor(40, () -> {
				})
				.thenExecute(() -> {
					// Carried flat across the plateau: the height it was flying at is not the point.
					Vec3 carriedOff = helper.absoluteVec(new Vec3(CARRIED_OFF.getX() + 0.5, 0.0, CARRIED_OFF.getZ() + 0.5));
					loftwing.snapTo(carriedOff.x(), loftwing.getY(), carriedOff.z(), loftwing.getYRot(), loftwing.getXRot());
					loftwing.setDeltaMovement(Vec3.ZERO);
				})
				.thenExecuteAfter(10, () -> {
					double steersFromOldCircle = Math.hypot(loftwing.getMoveControl().getWantedX() - circledHere.x(),
							loftwing.getMoveControl().getWantedZ() - circledHere.z());
					helper.assertTrue(steersFromOldCircle > OFF_THE_OLD_CIRCLE,
							"the loftwing was carried right across the plateau and still steers for a point "
									+ String.format("%.1f", steersFromOldCircle) + " blocks from where it picked its circle: "
									+ "it is going back to where it was " + describe(loftwing));
				})
				.thenSucceed();
	}

	/**
	 * Watches how much the bird turns once it has had time to settle, and fails on the two shapes a
	 * spin takes: one violent snap, or a steady churn that adds up to far more than a bird settling
	 * its heading would ever need.
	 */
	private void watchTheTurning(GameTestHelper helper, Loftwing loftwing, String what, Runnable eachTick) {
		float[] lastYaw = {loftwing.getYRot()};
		float[] worstTurn = new float[1];
		float[] totalTurn = new float[1];
		int[] watched = new int[1];

		helper.startSequence()
				.thenExecuteFor(SETTLING_TICKS, () -> {
					eachTick.run();
					lastYaw[0] = loftwing.getYRot();
				})
				.thenExecuteFor(WATCH_TICKS, () -> {
					eachTick.run();
					float turn = Math.abs(Mth.degreesDifference(lastYaw[0], loftwing.getYRot()));
					lastYaw[0] = loftwing.getYRot();
					worstTurn[0] = Math.max(worstTurn[0], turn);
					totalTurn[0] += turn;
					watched[0]++;
				})
				.thenExecute(() -> {
					helper.assertTrue(worstTurn[0] <= MAX_TURN_PER_TICK,
							what + " whipped round " + String.format("%.1f", worstTurn[0]) + " degrees in a single tick " + describe(loftwing));
					helper.assertTrue(totalTurn[0] <= MAX_TOTAL_TURN,
							what + " turned " + String.format("%.0f", totalTurn[0]) + " degrees over " + watched[0]
									+ " ticks, which is " + String.format("%.1f", totalTurn[0] / 360.0F) + " full turns: it is spinning " + describe(loftwing));
				})
				.thenSucceed();
	}
}
