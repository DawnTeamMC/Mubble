package fr.hugman.mubble.test.gametest.power_up;

import fr.hugman.mubble.test.gametest.datapack.PowerUpFixtures;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import fr.hugman.mubble.world.power_up.PowerUp;

/**
 * The flutter: past the top of a jump, a holder still leaning on the jump key rises again instead of
 * falling, once per jump and for as long as the ability lasts.
 * <p>
 * The fixture behind these tests flutters for 10 ticks over a ramp of 4, short enough to play out whole
 * inside an arena. Every one of them drives the player the way a client would, since the jump key and the
 * movement the flutter reads both only ever reach the server as packets.
 */
public class FlutterGameTest {
    private static final BlockPos STAND = new BlockPos(4, Arena.FLOOR_Y + 1, 3);

    /** The upward push a jump is worth, near enough to what {@code jumpFromGround} gives a player. */
    private static final Vec3 JUMP = new Vec3(0.0D, 0.42D, 0.0D);
    /** The duration of the fixture, see {@code flutterer.json}. */
    private static final int FLUTTER_DURATION = 10;
    /** Long enough for a jump to peak and for the flutter to be well under way. */
    private static final int PEAK_TICKS = 12;
    /** Long enough for anything left in the air to have come back down. */
    private static final int LANDING_TICKS = 40;

    private static final Input JUMP_HELD = TestPlayers.holdingJump();
    private static final Input NOTHING_HELD = Input.EMPTY;

    @GameTest
    public void aHeldJumpKeyFluttersPastThePeak(GameTestHelper helper) {
        var player = jumper(helper, PowerUpFixtures.FLUTTERER);

        helper.assertFalse(player.isFluttering(), "the flutter should not start on the way up");
        fall(player, JUMP_HELD, PEAK_TICKS);

        helper.assertTrue(player.isFluttering(), "a jump key held past the peak should start a flutter");
        helper.succeed();
    }

    /** Nothing changes on the way up: a flutter that started there would just be a stronger jump. */
    @GameTest
    public void theFlutterWaitsForTheWayDown(GameTestHelper helper) {
        var player = jumper(helper, PowerUpFixtures.FLUTTERER);

        helper.assertTrue(player.getKnownMovement().y() > 0.0D, "the player is not on the way up, the test proves nothing");
        helper.assertFalse(player.isFluttering(), "a flutter should not start while the player is still climbing");

        helper.succeed();
    }

    @GameTest
    public void aFlutterCarriesThePlayerHigher(GameTestHelper helper) {
        var fluttering = jumper(helper, PowerUpFixtures.FLUTTERER);
        var plain = jumper(helper, PowerUpFixtures.EMPTY);

        fall(fluttering, JUMP_HELD, PEAK_TICKS);
        fall(plain, JUMP_HELD, PEAK_TICKS);

        helper.assertTrue(fluttering.getY() > plain.getY(),
                "a fluttering player should be higher up than one falling plainly, was "
                        + fluttering.getY() + " against " + plain.getY());
        helper.succeed();
    }

    @GameTest
    public void lettingGoOfTheJumpKeyEndsTheFlutter(GameTestHelper helper) {
        var player = flutteringPlayer(helper);

        TestPlayers.tick(player, NOTHING_HELD);

        helper.assertFalse(player.isFluttering(), "letting go of the jump key should end the flutter");
        helper.succeed();
    }

    /** A jump only ever gets one flutter, whether it was spent whole or let go of halfway through. */
    @GameTest
    public void aFlutterLetGoOfCannotBeResumed(GameTestHelper helper) {
        var player = flutteringPlayer(helper);

        TestPlayers.tick(player, NOTHING_HELD);
        fall(player, JUMP_HELD, 5);

        helper.assertFalse(player.isFluttering(), "a flutter let go of should not start again on the same jump");
        helper.succeed();
    }

    /** The tick count is what the lift ramps up over, so it has to count the flutter and nothing else. */
    @GameTest
    public void theFlutterCountsTheTicksItHasRunFor(GameTestHelper helper) {
        var player = flutteringPlayer(helper);
        int started = player.getFlutterTicks();

        fall(player, JUMP_HELD, 3);
        helper.assertValueEqual(player.getFlutterTicks(), started + 3, "the ticks a flutter has run for");

        fall(player, NOTHING_HELD, 1);
        helper.assertValueEqual(player.getFlutterTicks(), 0, "the ticks left on a flutter that is over");

        helper.succeed();
    }

    @GameTest
    public void aFlutterRunsOutAfterItsDuration(GameTestHelper helper) {
        var player = flutteringPlayer(helper);

        fall(player, JUMP_HELD, FLUTTER_DURATION + 1);

        helper.assertFalse(player.isFluttering(), "a flutter should be over once its duration has run out");
        helper.assertTrue(player.hasFluttered(), "the jump should be marked as having spent its flutter");
        helper.succeed();
    }

    @GameTest(maxTicks = 200)
    public void landingHandsTheNextJumpItsFlutterBack(GameTestHelper helper) {
        var player = flutteringPlayer(helper);
        fall(player, JUMP_HELD, FLUTTER_DURATION + 1);
        helper.assertTrue(player.hasFluttered(), "the flutter was never spent, the test proves nothing");

        fall(player, NOTHING_HELD, LANDING_TICKS);

        helper.assertTrue(player.onGround(), "the player never landed, the test proves nothing");
        helper.assertFalse(player.hasFluttered(), "landing should hand the next jump its flutter back");
        helper.succeed();
    }

    @GameTest
    public void aPowerUpWithoutAFlutterNeverFlutters(GameTestHelper helper) {
        var player = jumper(helper, PowerUpFixtures.EMPTY);

        fall(player, JUMP_HELD, PEAK_TICKS);

        helper.assertFalse(player.isFluttering(), "a power-up granting no flutter should not flutter");
        helper.succeed();
    }

    /** The form can be lost in mid-air, and the flutter has no business carrying on without it. */
    @GameTest
    public void losingTheFormMidAirEndsTheFlutter(GameTestHelper helper) {
        var player = flutteringPlayer(helper);

        player.clearPowerUp();
        TestPlayers.tick(player, JUMP_HELD);

        helper.assertFalse(player.isFluttering(), "losing the power-up should end the flutter");
        helper.succeed();
    }

    @GameTest
    public void waterCutsTheFlutterShort(GameTestHelper helper) {
        var player = flutteringPlayer(helper);

        for (int y = Arena.FLOOR_Y + 1; y < Arena.SIZE; y++) {
            helper.setBlock(new BlockPos(STAND.getX(), y, STAND.getZ()), Blocks.WATER);
        }
        TestPlayers.tick(player, JUMP_HELD);

        helper.assertTrue(player.isInWater(), "the player is not in the water, the test proves nothing");
        helper.assertFalse(player.isFluttering(), "going into the water should cut the flutter short");
        helper.succeed();
    }

    /** A player standing on the ground with the key held down is jumping, not fluttering. */
    @GameTest
    public void nothingFluttersOnTheGround(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.FLUTTERER));

        fall(player, JUMP_HELD, 5);

        helper.assertTrue(player.onGround(), "the player is not on the ground, the test proves nothing");
        helper.assertFalse(player.isFluttering(), "a player standing on the ground should not flutter");
        helper.succeed();
    }

    /** A player in the middle of a jump, holding the key, with the flutter already going. */
    private static ServerPlayer flutteringPlayer(GameTestHelper helper) {
        var player = jumper(helper, PowerUpFixtures.FLUTTERER);
        fall(player, JUMP_HELD, PEAK_TICKS);
        helper.assertTrue(player.isFluttering(), "the flutter never started, the test proves nothing");
        return player;
    }

    /**
     * A player who has just jumped: off the ground, on the way up, and reporting it.
     */
    private static ServerPlayer jumper(GameTestHelper helper, ResourceKey<PowerUp> powerUp) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);
        player.setPowerUp(PowerUpFixtures.get(helper, powerUp));

        player.setDeltaMovement(JUMP);
        TestPlayers.tick(player, TestPlayers.holdingJump());
        return player;
    }

    private static void fall(ServerPlayer player, Input keys, int ticks) {
        for (int tick = 0; tick < ticks; tick++) {
            TestPlayers.tick(player, keys);
        }
    }
}
