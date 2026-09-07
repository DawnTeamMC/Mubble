package fr.hugman.mubble.test.gametest.power_up;

import fr.hugman.mubble.test.gametest.datapack.PowerUpFixtures;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import fr.hugman.mubble.world.power_up.PowerUp;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.level.block.Blocks;

/**
 * The descent half of a jump held on: a holder leaning on the jump key comes down at a walking pace and is
 * spared most of what the fall would otherwise be worth.
 * <p>
 * The fixture behind these tests grants the float and no climb at all, which is the shape the Tanooki form
 * will take: it has to work on its own, without a flutter first.
 *
 * @see FlutterGameTest for the other half
 */
public class FloatGameTest {
    private static final BlockPos STAND = new BlockPos(4, Arena.FLOOR_Y + 1, 3);

    /** The speed of the fixture, see {@code floater.json}. */
    private static final double FLOAT_SPEED = 0.1D;
    /** The share of a floated fall the fixture still counts. */
    private static final double FLOAT_FALL_DAMAGE = 0.25D;
    /** Enough ticks for a dropped player to be falling well past the float's pace. */
    private static final int DROP_TICKS = 8;
    private static final double EPSILON = 1.0E-4D;

    private static final Input JUMP_HELD = TestPlayers.holdingJump();
    private static final Input NOTHING_HELD = Input.EMPTY;

    /** The Tanooki case: no climb to come first, just a jump that ends gently. */
    @GameTest
    public void aHeldJumpKeySlowsTheFall(GameTestHelper helper) {
        var player = falling(helper, PowerUpFixtures.FLOATER);

        fall(player, JUMP_HELD, 3);
        double descent = descentOverOneTick(player, JUMP_HELD);

        helper.assertTrue(player.isFloating(), "a jump key held on the way down should start a float");
        helper.assertTrue(descent <= FLOAT_SPEED + EPSILON,
                "a floating player should come down no faster than the float, dropped " + descent + " in a tick");
        helper.succeed();
    }

    /**
     * The descent is a pace rather than a slowing down: every tick of it covers the same ground, however long
     * the float has been going on for.
     */
    @GameTest
    public void aFloatComesDownAtOneSteadySpeed(GameTestHelper helper) {
        var player = floatingPlayer(helper);

        for (int tick = 0; tick < 5; tick++) {
            // Not an exact comparison: the ability keeps its speed as a float, and the descent is a double.
            double descent = descentOverOneTick(player, JUMP_HELD);
            helper.assertTrue(Math.abs(descent - FLOAT_SPEED) < EPSILON,
                    "tick " + tick + " of a descent should cover the float's speed, covered " + descent);
        }
        helper.succeed();
    }

    @GameTest
    public void aFloatIsSlowerThanAPlainFall(GameTestHelper helper) {
        var floating = falling(helper, PowerUpFixtures.FLOATER);
        var plain = falling(helper, PowerUpFixtures.EMPTY);

        fall(floating, JUMP_HELD, 6);
        fall(plain, JUMP_HELD, 6);

        helper.assertTrue(floating.getY() > plain.getY(),
                "a floating player should be higher up than one dropping plainly, was "
                        + floating.getY() + " against " + plain.getY());
        helper.succeed();
    }

    @GameTest
    public void lettingGoOfTheJumpKeyEndsTheFloat(GameTestHelper helper) {
        var player = floatingPlayer(helper);

        fall(player, NOTHING_HELD, 3);
        double descent = descentOverOneTick(player, NOTHING_HELD);

        helper.assertFalse(player.isFloating(), "letting go of the jump key should end the float");
        helper.assertTrue(descent > FLOAT_SPEED,
                "a player who let go should be dropping faster than the float again, dropped " + descent + " in a tick");
        helper.succeed();
    }

    /** Unlike the flutter, which a jump only ever gets one of, the float comes back on the next press. */
    @GameTest
    public void aFloatCanBeStartedAgainOnTheSameFall(GameTestHelper helper) {
        var player = floatingPlayer(helper);

        fall(player, NOTHING_HELD, 3);
        helper.assertFalse(player.isFloating(), "the float never ended, the test proves nothing");
        fall(player, JUMP_HELD, 2);

        helper.assertTrue(player.isFloating(), "pressing the jump key again should start the float back up");
        helper.succeed();
    }

    /** It slows a fall down; it is not a second jump, and has no business lifting anyone. */
    @GameTest
    public void aFloatNeverLiftsAClimbingPlayer(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND.above(2));
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.FLOATER));
        player.setDeltaMovement(0.0D, 0.42D, 0.0D);
        TestPlayers.tick(player, JUMP_HELD);

        double climbing = player.getDeltaMovement().y();
        TestPlayers.tick(player, JUMP_HELD);

        helper.assertTrue(climbing > 0.0D, "the player is not on the way up, the test proves nothing");
        helper.assertFalse(player.isFloating(), "a climbing player should not be floating");
        helper.assertTrue(player.getDeltaMovement().y() < climbing,
                "gravity should still be taking its share of a climb, even with a float in hand");
        helper.succeed();
    }

    @GameTest
    public void aFloatSparesMostOfTheFall(GameTestHelper helper) {
        var floating = floatingPlayer(helper);
        double before = floating.fallDistance;

        int ticks = 10;
        fall(floating, JUMP_HELD, ticks);

        // Each floated tick is worth its speed, of which only the fixture's share is kept.
        double covered = FLOAT_SPEED * ticks;
        double expected = before + covered * FLOAT_FALL_DAMAGE;
        helper.assertTrue(floating.fallDistance <= expected + 0.5D,
                "a floated fall should mostly be written off, was " + floating.fallDistance + " against about " + expected);
        helper.assertTrue(floating.fallDistance < before + covered,
                "a floated fall should count for less than the distance actually covered");
        helper.succeed();
    }

    @GameTest
    public void aPowerUpWithoutAFloatNeverFloats(GameTestHelper helper) {
        var player = falling(helper, PowerUpFixtures.EMPTY);

        fall(player, JUMP_HELD, 4);

        helper.assertFalse(player.isFloating(), "a power-up granting no float should not float");
        helper.succeed();
    }

    @GameTest
    public void waterCutsTheFloatShort(GameTestHelper helper) {
        var player = floatingPlayer(helper);

        for (int y = Arena.FLOOR_Y + 1; y < Arena.SIZE; y++) {
            helper.setBlock(new BlockPos(STAND.getX(), y, STAND.getZ()), Blocks.WATER);
        }
        TestPlayers.tick(player, JUMP_HELD);

        helper.assertTrue(player.isInWater(), "the player is not in the water, the test proves nothing");
        helper.assertFalse(player.isFloating(), "going into the water should cut the float short");
        helper.succeed();
    }

    @GameTest(maxTicks = 200)
    public void landingEndsTheFloat(GameTestHelper helper) {
        var player = floatingPlayer(helper);

        fall(player, JUMP_HELD, 120);

        helper.assertTrue(player.onGround(), "the player never landed, the test proves nothing");
        helper.assertFalse(player.isFloating(), "a player back on the ground should not still be floating");
        helper.succeed();
    }

    /** A player already floating, well into their descent. */
    private static ServerPlayer floatingPlayer(GameTestHelper helper) {
        var player = falling(helper, PowerUpFixtures.FLOATER);
        fall(player, JUMP_HELD, 3);
        helper.assertTrue(player.isFloating(), "the float never started, the test proves nothing");
        return player;
    }

    /** A player dropped from the top of the arena, falling fast enough for a float to be worth something. */
    private static ServerPlayer falling(GameTestHelper helper, ResourceKey<PowerUp> powerUp) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND.above(Arena.SIZE - 2));
        player.setPowerUp(PowerUpFixtures.get(helper, powerUp));

        fall(player, NOTHING_HELD, DROP_TICKS);
        return player;
    }

    /**
     * How far the player actually drops over one tick.
     * <p>
     * Not the same thing as their movement, which gravity is added back to once the tick has been spent: what
     * a float holds down is the ground covered, and that is what has to be measured.
     */
    private static double descentOverOneTick(ServerPlayer player, Input keys) {
        double before = player.getY();
        TestPlayers.tick(player, keys);
        return before - player.getY();
    }

    private static void fall(ServerPlayer player, Input keys, int ticks) {
        for (int tick = 0; tick < ticks; tick++) {
            TestPlayers.tick(player, keys);
        }
    }
}
