package fr.hugman.mubble.test.gametest.power_up;

import fr.hugman.mubble.test.gametest.datapack.PowerUpFixtures;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import fr.hugman.mubble.world.power_up.ability.LaunchAbility;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;

/**
 * The launch on a double tap of the jump key, and the slow fall from its peak, on the {@code launcher.json}
 * fixture: default launch, slow fall at 0.15 blocks per tick.
 */
public class LaunchGameTest {
    private static final BlockPos STAND = new BlockPos(4, Arena.FLOOR_Y + 1, 3);
    private static final double SLOW_FALL_SPEED = 0.15D;
    private static final double EPSILON = 1.0E-4D;

    private static final Input JUMP = TestPlayers.holdingJump();
    private static final Input SNEAK = TestPlayers.holdingSneak();
    private static final Input NOTHING = Input.EMPTY;

    @GameTest
    public void aDoubleTapOnTheGroundLaunchesSevenBlocksUp(GameTestHelper helper) {
        var player = standing(helper);

        double start = player.getY();
        doubleTap(player);
        helper.assertTrue(player.isLaunching(), "a double tap should launch the player");

        double peak = riseToThePeak(player);
        helper.assertTrue(Math.abs(peak - start - LaunchAbility.DEFAULT_HEIGHT) < 0.5D,
                "a launch should carry the player about 7 blocks up, carried " + (peak - start));
        helper.succeed();
    }

    @GameTest
    public void aDoubleTapInTheAirLaunchesToo(GameTestHelper helper) {
        var player = standing(helper);
        player.teleportTo(player.getX(), player.getY() + 1.0D, player.getZ());
        TestPlayers.tick(player, NOTHING);
        helper.assertFalse(player.onGround(), "the player is not in the air, the test proves nothing");

        TestPlayers.tick(player, JUMP);
        TestPlayers.tick(player, NOTHING);
        double start = player.getY();
        TestPlayers.tick(player, JUMP);

        helper.assertTrue(player.isLaunching(), "a double tap in mid-air should launch the player");
        helper.assertTrue(player.getY() > start, "the launch should start on the very tick of the second tap");
        double peak = riseToThePeak(player);
        helper.assertTrue(Math.abs(peak - start - LaunchAbility.DEFAULT_HEIGHT) < 0.5D, "a launch from the air should carry the player about 7 blocks up, carried " + (peak - start));
        helper.succeed();
    }

    @GameTest
    public void aSingleTapDoesNotLaunch(GameTestHelper helper) {
        var player = standing(helper);

        TestPlayers.tick(player, JUMP);
        tick(player, JUMP, 5);
        tick(player, NOTHING, 5);

        helper.assertFalse(player.isLaunching(), "a single tap should not launch");
        helper.assertFalse(player.hasLaunched(), "a single tap should not spend the launch");
        helper.succeed();
    }

    @GameTest
    public void twoSlowTapsDoNotLaunch(GameTestHelper helper) {
        var player = standing(helper);

        TestPlayers.tick(player, JUMP);
        tick(player, NOTHING, LaunchAbility.DEFAULT_DOUBLE_TAP_WINDOW + 1);
        TestPlayers.tick(player, JUMP);

        helper.assertFalse(player.isLaunching(), "two taps further apart than the window should not launch");
        helper.succeed();
    }

    @GameTest
    public void aPlayerWhoMayFlyKeepsTheDoubleTapForFlight(GameTestHelper helper) {
        var player = standing(helper);
        player.getAbilities().mayfly = true;

        doubleTap(player);

        helper.assertFalse(player.isLaunching(), "a player who may fly should not be launched by a double tap");
        helper.succeed();
    }

    @GameTest
    public void thereIsNoSecondLaunchBeforeLanding(GameTestHelper helper) {
        var player = slowFalling(helper);

        doubleTap(player);

        helper.assertFalse(player.isLaunching(), "a second launch before landing should be refused");
        helper.assertTrue(player.hasLaunched(), "the launch should stay spent until the player lands");
        helper.succeed();
    }

    @GameTest
    public void theSlowFallStartsAtThePeakAndHoldsOneSpeed(GameTestHelper helper) {
        var player = slowFalling(helper);

        tick(player, NOTHING, 4);
        for (int tick = 0; tick < 5; tick++) {
            double before = player.getY();
            TestPlayers.tick(player, NOTHING);
            double descent = before - player.getY();
            helper.assertTrue(Math.abs(descent - SLOW_FALL_SPEED) < EPSILON,
                    "tick " + tick + " of a slow fall should cover its speed, covered " + descent);
        }
        helper.assertTrue(player.isSlowFalling(), "the slow fall should last for as long as the player falls");
        helper.succeed();
    }

    @GameTest
    public void sneakingCancelsTheSlowFall(GameTestHelper helper) {
        var player = slowFalling(helper);

        tick(player, SNEAK, 4);
        double before = player.getY();
        TestPlayers.tick(player, SNEAK);

        helper.assertFalse(player.isSlowFalling(), "sneaking should cancel the slow fall");
        helper.assertTrue(before - player.getY() > SLOW_FALL_SPEED, "a player who cancelled the slow fall should drop faster than it");
        helper.succeed();
    }

    @GameTest(maxTicks = 200)
    public void landingHandsTheLaunchBack(GameTestHelper helper) {
        var player = slowFalling(helper);

        for (int tick = 0; tick < 150 && !player.onGround(); tick++) {
            TestPlayers.tick(player, NOTHING);
        }
        TestPlayers.tick(player, NOTHING);

        helper.assertTrue(player.onGround(), "the player never landed, the test proves nothing");
        helper.assertFalse(player.isSlowFalling(), "landing should end the slow fall");
        helper.assertFalse(player.hasLaunched(), "landing should hand the launch back");
        helper.succeed();
    }

    @GameTest
    public void waterHandsTheLaunchBack(GameTestHelper helper) {
        var player = slowFalling(helper);
        var level = helper.getLevel();
        var feet = player.blockPosition();
        level.setBlockAndUpdate(feet, Blocks.WATER.defaultBlockState());
        level.setBlockAndUpdate(feet.above(), Blocks.WATER.defaultBlockState());

        TestPlayers.tick(player, NOTHING);

        helper.assertTrue(player.isInWater(), "the player is not in the water, the test proves nothing");
        helper.assertFalse(player.hasLaunched(), "water should hand the launch back");
        helper.assertFalse(player.isSlowFalling(), "water should end the slow fall");
        helper.succeed();
    }

    @GameTest
    public void aLadderHandsTheLaunchBack(GameTestHelper helper) {
        var player = slowFalling(helper);
        var level = helper.getLevel();
        var feet = player.blockPosition();
        level.setBlockAndUpdate(feet.south(), Blocks.STONE.defaultBlockState());
        level.setBlockAndUpdate(feet, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.NORTH));

        TestPlayers.tick(player, NOTHING);

        helper.assertTrue(player.onClimbable(), "the player is not on the ladder, the test proves nothing");
        helper.assertFalse(player.hasLaunched(), "a ladder should hand the launch back");
        helper.succeed();
    }

    @GameTest
    public void losingTheFormMidAirEndsEverything(GameTestHelper helper) {
        var player = standing(helper);
        doubleTap(player);

        player.clearPowerUp();
        TestPlayers.tick(player, NOTHING);

        helper.assertFalse(player.isLaunching(), "losing the form should end the launch");
        helper.assertFalse(player.isSlowFalling(), "losing the form should leave no slow fall");
        helper.succeed();
    }

    @GameTest
    public void theLaunchStateIsSaved(GameTestHelper helper) {
        var player = slowFalling(helper);
        var registries = helper.getLevel().registryAccess();
        var output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registries);
        player.saveWithoutId(output);

        Player reloaded = TestPlayers.mock(helper);
        reloaded.load(TagValueInput.create(ProblemReporter.DISCARDING, registries, output.buildResult()));

        helper.assertTrue(reloaded.hasLaunched(), "a spent launch should stay spent through a save");
        helper.assertTrue(reloaded.isSlowFalling(), "a slow fall should carry on through a save");
        helper.succeed();
    }

    /** What a client watching someone else does with the entity data the server sends it. */
    @GameTest
    public void theOtherPlayersAreToldAboutTheLaunch(GameTestHelper helper) {
        var player = standing(helper);
        doubleTap(player);

        Player watched = TestPlayers.mock(helper);
        watched.getEntityData().assignValues(player.getEntityData().getNonDefaultValues());
        helper.assertTrue(watched.isLaunching(), "the launch flag should reach the other players");

        riseToThePeak(player);
        watched.getEntityData().assignValues(player.getEntityData().getNonDefaultValues());
        helper.assertTrue(watched.isSlowFalling(), "the slow fall flag should reach the other players");
        helper.succeed();
    }

    private static ServerPlayer standing(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Arena.openTheSky(helper, STAND);
        var player = TestPlayers.survival(TestPlayers.at(helper, STAND));
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.LAUNCHER));
        TestPlayers.tick(player, NOTHING);
        return player;
    }

    private static ServerPlayer slowFalling(GameTestHelper helper) {
        var player = standing(helper);
        doubleTap(player);
        riseToThePeak(player);
        helper.assertTrue(player.isSlowFalling(), "the slow fall never started, the test proves nothing");
        return player;
    }

    private static void doubleTap(ServerPlayer player) {
        TestPlayers.tick(player, JUMP);
        TestPlayers.tick(player, NOTHING);
        TestPlayers.tick(player, JUMP);
    }

    /** Ticks with no key held until the launch is over, and returns the highest the feet got. */
    private static double riseToThePeak(ServerPlayer player) {
        double peak = player.getY();
        for (int tick = 0; tick < 40 && player.isLaunching(); tick++) {
            TestPlayers.tick(player, NOTHING);
            peak = Math.max(peak, player.getY());
        }
        return peak;
    }

    private static void tick(ServerPlayer player, Input keys, int ticks) {
        for (int tick = 0; tick < ticks; tick++) {
            TestPlayers.tick(player, keys);
        }
    }
}
