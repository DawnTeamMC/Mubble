package fr.hugman.mubble.test.gametest.arcade;

import static fr.hugman.mubble.arcade.sim.ArcadeInputFrame.ACTION;
import static fr.hugman.mubble.arcade.sim.ArcadeInputFrame.CROUCH;
import static fr.hugman.mubble.arcade.sim.ArcadeInputFrame.JUMP;
import static fr.hugman.mubble.arcade.sim.ArcadeInputFrame.SPIN;
import static fr.hugman.mubble.arcade.sim.ArcadeInputFrame.SPRINT;

import fr.hugman.mubble.arcade.references.ArcadeProfileIds;
import fr.hugman.mubble.arcade.ArcadeController;
import fr.hugman.mubble.arcade.move.ArcadeMove;
import fr.hugman.mubble.arcade.move.ArcadeMoves;
import fr.hugman.mubble.arcade.sim.ArcadeInputCollector;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;

/**
 * The moves themselves, played by a stand-in client against real blocks, under {@code mubble:trial}.
 * <p>
 * These check what the moves do with numbers, not how they feel: heights, distances, which state
 * follows which. Feel is for a human to judge.
 */
public class ArcadeMovementGameTest {
    private static final double HEIGHT_TOLERANCE = 0.02D;

    private static Player standing(GameTestHelper helper, BlockPos pos) {
        var player = ArcadeTestKit.client(helper, pos, ArcadeProfileIds.TRIAL);
        var frames = new ArcadeTestKit.Frames();
        for (int i = 0; i < 4; i++) {
            ArcadeTestKit.step(player, frames.next());
        }
        helper.assertTrue(player.onGround(), "the stand-in never landed, the test proves nothing");
        return player;
    }

    private static ArcadeMove move(Player player) {
        return ArcadeController.of(player).state().move;
    }

    /** Steps until the player lands, and returns the highest the feet went, relative to {@code fromY}. */
    private static double peakUntilLanding(GameTestHelper helper, Player player, ArcadeTestKit.Frames frames, double fromY, int maxTicks) {
        double peak = 0.0D;
        boolean left = false;
        for (int i = 0; i < maxTicks; i++) {
            ArcadeTestKit.step(player, frames.next());
            peak = Math.max(peak, player.getY() - fromY);
            if (!player.onGround()) {
                left = true;
            } else if (left) {
                return peak;
            }
        }
        helper.fail("the player never came back down");
        return peak;
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void jumpReachesItsAuthoredHeight(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 5));
        var frames = new ArcadeTestKit.Frames();
        double startY = player.getY();
        ArcadeTestKit.step(player, frames.press(JUMP));
        helper.assertValueEqual(move(player), ArcadeMoves.JUMP, "move after pressing jump");
        double peak = peakUntilLanding(helper, player, frames, startY, 60);
        helper.assertTrue(Math.abs(peak - 2.2D) < HEIGHT_TOLERANCE, "a held jump should reach 2.2 blocks, got " + peak);
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void releasingJumpEarlyCutsTheJump(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 5));
        var frames = new ArcadeTestKit.Frames();
        double startY = player.getY();
        ArcadeTestKit.step(player, frames.press(JUMP));
        ArcadeTestKit.step(player, frames.next());
        frames.letGo(JUMP);
        double peak = peakUntilLanding(helper, player, frames, startY, 60);
        helper.assertTrue(peak < 1.6D, "a jump released after two ticks should stay low, got " + peak);
        helper.assertTrue(peak > 0.3D, "a released jump should still leave the ground, got " + peak);
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void jumpsChainIntoDoubleAndTripleJumps(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 2));
        var frames = new ArcadeTestKit.Frames().forward().hold(SPRINT);
        for (int i = 0; i < 4; i++) {
            ArcadeTestKit.step(player, frames.next());
        }
        double[] expected = {2.2D, 3.2D, 4.6D};
        ArcadeMove[] moves = {ArcadeMoves.JUMP, ArcadeMoves.DOUBLE_JUMP, ArcadeMoves.TRIPLE_JUMP};
        for (int jump = 0; jump < 3; jump++) {
            double startY = player.getY();
            ArcadeTestKit.step(player, frames.press(JUMP));
            helper.assertValueEqual(move(player), moves[jump], "move of jump " + (jump + 1));
            double peak = peakUntilLanding(helper, player, frames, startY, 80);
            helper.assertTrue(Math.abs(peak - expected[jump]) < HEIGHT_TOLERANCE, "jump " + (jump + 1) + " should reach " + expected[jump] + ", got " + peak);
            frames.letGo(JUMP);
        }
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void standingStillDoesNotChain(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 5));
        var frames = new ArcadeTestKit.Frames();
        double startY = player.getY();
        ArcadeTestKit.step(player, frames.press(JUMP));
        peakUntilLanding(helper, player, frames, startY, 60);
        frames.letGo(JUMP);
        ArcadeTestKit.step(player, frames.press(JUMP));
        helper.assertValueEqual(move(player), ArcadeMoves.JUMP, "a second jump without speed should be a plain jump");
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void backflipGoesHighAndBackwards(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 20));
        var frames = new ArcadeTestKit.Frames().hold(CROUCH);
        ArcadeTestKit.step(player, frames.next());
        double startY = player.getY();
        double startZ = player.getZ();
        ArcadeTestKit.step(player, frames.press(JUMP));
        helper.assertValueEqual(move(player), ArcadeMoves.BACKFLIP, "crouching and jumping while standing");
        frames.letGo(CROUCH);
        double peak = peakUntilLanding(helper, player, frames, startY, 80);
        helper.assertTrue(Math.abs(peak - 5.2D) < HEIGHT_TOLERANCE, "a backflip should reach 5.2 blocks, got " + peak);
        helper.assertTrue(player.getZ() < startZ - 0.5D, "a backflip should drift backwards, from " + startZ + " to " + player.getZ());
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void longJumpGoesFartherThanAJump(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        double jumpDistance = this.runAndJump(helper, new BlockPos(2, 1, 2), false);
        double longJumpDistance = this.runAndJump(helper, new BlockPos(7, 1, 2), true);
        helper.assertTrue(longJumpDistance > jumpDistance + 1.0D, "a long jump should go farther than a running jump: " + longJumpDistance + " against " + jumpDistance);
        helper.succeed();
    }

    private double runAndJump(GameTestHelper helper, BlockPos start, boolean longJump) {
        var player = standing(helper, start);
        var frames = new ArcadeTestKit.Frames().forward().hold(SPRINT);
        for (int i = 0; i < 6; i++) {
            ArcadeTestKit.step(player, frames.next());
        }
        if (longJump) {
            ArcadeTestKit.step(player, frames.press(CROUCH));
        }
        double startZ = player.getZ();
        ArcadeTestKit.step(player, frames.press(JUMP));
        helper.assertValueEqual(move(player), longJump ? ArcadeMoves.LONG_JUMP : ArcadeMoves.JUMP, "move of the running jump");
        frames.letGo(CROUCH);
        peakUntilLanding(helper, player, frames.release(), player.getY(), 80);
        return player.getZ() - startZ;
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void coyoteTimeKeepsAJumpAfterTheEdge(GameTestHelper helper) {
        // a raised platform ending at z = 8
        ArcadeTestKit.fill(helper, new BlockPos(0, 0, 0), new BlockPos(9, 3, 7), Blocks.STONE.defaultBlockState());
        ArcadeTestKit.fill(helper, new BlockPos(0, 0, 8), new BlockPos(9, 0, 47), Blocks.STONE.defaultBlockState());
        var player = standing(helper, new BlockPos(5, 4, 5));
        var frames = new ArcadeTestKit.Frames().forward().hold(SPRINT);
        int airborne = 0;
        for (int i = 0; i < 40 && airborne < 2; i++) {
            ArcadeTestKit.step(player, frames.next());
            airborne = player.onGround() ? 0 : airborne + 1;
        }
        helper.assertValueEqual(airborne, 2, "ticks spent in the air before pressing jump");
        ArcadeTestKit.step(player, frames.press(JUMP));
        helper.assertValueEqual(move(player), ArcadeMoves.JUMP, "a jump pressed two ticks after running off the edge");
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void aJumpPressedOneFrameBeforeLandingStillHappens(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        var player = ArcadeTestKit.client(helper, new BlockPos(5, 4, 5), ArcadeProfileIds.TRIAL);
        var collector = new ArcadeInputCollector();
        long ms = 1_000_000L;
        long now = 0L;
        int tick = 0;
        // fall until the next tick lands
        while (!player.onGround() && tick < 60) {
            var state = ArcadeController.of(player).state();
            double gap = player.getY() - (helper.absolutePos(BlockPos.ZERO).getY() + 1.0D);
            boolean landsNext = gap + state.vy - 0.2D < 0.0D;
            if (landsNext) {
                // one frame before the tick, jump is pressed and already released by the tick
                collector.sampleFrame(now + 33 * ms, 0, JUMP);
            }
            now += 50 * ms;
            ArcadeTestKit.step(player, collector.tick(tick++, now, 0.0F, 0.0F, 0.0F, true));
            if (landsNext) {
                break;
            }
        }
        helper.assertTrue(player.onGround(), "the player should have landed with the press in the buffer");
        now += 50 * ms;
        ArcadeTestKit.step(player, collector.tick(tick, now, 0.0F, 0.0F, 0.0F, true));
        helper.assertValueEqual(move(player), ArcadeMoves.JUMP, "the buffered jump should go off right after landing");
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void groundPoundDropsStraightAndNegatesFallDamage(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        var pounder = ArcadeTestKit.client(helper, new BlockPos(2, 10, 5), ArcadeProfileIds.OVERWORLD);
        var faller = ArcadeTestKit.client(helper, new BlockPos(7, 10, 5), ArcadeProfileIds.OVERWORLD);
        var frames = new ArcadeTestKit.Frames();
        var fallerFrames = new ArcadeTestKit.Frames();
        ArcadeTestKit.step(pounder, frames.next());
        ArcadeTestKit.step(faller, fallerFrames.next());
        ArcadeTestKit.step(pounder, frames.press(CROUCH));
        helper.assertValueEqual(move(pounder), ArcadeMoves.GROUND_POUND, "crouch in the air");
        double x = pounder.getX();
        for (int i = 0; i < 40 && !pounder.onGround(); i++) {
            ArcadeTestKit.step(pounder, frames.next());
        }
        for (int i = 0; i < 60 && !faller.onGround(); i++) {
            ArcadeTestKit.step(faller, fallerFrames.next());
        }
        helper.assertValueEqual(move(pounder), ArcadeMoves.GROUND_POUND_LAND, "the landing of a ground pound");
        helper.assertTrue(Math.abs(pounder.getX() - x) < 1.0E-6D, "a ground pound drops straight down");
        helper.assertValueEqual(pounder.getHealth(), pounder.getMaxHealth(), "health after a ground pound landing");
        helper.assertTrue(faller.getHealth() < faller.getMaxHealth(), "the same fall without a ground pound should hurt, the test proves nothing otherwise");
        // and right after, a jump goes very high
        double startY = pounder.getY();
        ArcadeTestKit.step(pounder, frames.press(JUMP));
        helper.assertValueEqual(move(pounder), ArcadeMoves.GROUND_POUND_JUMP, "a jump right after the landing");
        double peak = peakUntilLanding(helper, pounder, frames, startY, 80);
        helper.assertTrue(Math.abs(peak - 5.0D * 0.9D) < HEIGHT_TOLERANCE, "a ground pound jump under the overworld profile should reach 4.5, got " + peak);
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void rollingOutOfAGroundPoundIsFasterThanARoll(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        // a plain roll from standing, for comparison
        var roller = standing(helper, new BlockPos(2, 1, 5));
        var plain = new ArcadeTestKit.Frames().forward().hold(CROUCH);
        ArcadeTestKit.step(roller, plain.press(ACTION));
        helper.assertValueEqual(move(roller), ArcadeMoves.ROLL, "crouch and action on the ground");
        double plainSpeed = ArcadeController.of(roller).state().horizontalSpeed();

        var pounder = standing(helper, new BlockPos(7, 1, 5));
        var frames = new ArcadeTestKit.Frames().forward();
        ArcadeTestKit.step(pounder, frames.press(JUMP));
        for (int i = 0; i < 6; i++) {
            ArcadeTestKit.step(pounder, frames.next());
        }
        frames.letGo(JUMP);
        ArcadeTestKit.step(pounder, frames.press(CROUCH));
        helper.assertValueEqual(move(pounder), ArcadeMoves.GROUND_POUND, "crouch in the air");
        // crouch stays held through the pound, as it does when one rolls out of it
        for (int i = 0; i < 40 && move(pounder) != ArcadeMoves.GROUND_POUND_LAND; i++) {
            ArcadeTestKit.step(pounder, frames.next());
        }
        helper.assertValueEqual(move(pounder), ArcadeMoves.GROUND_POUND_LAND, "the landing of the ground pound");
        ArcadeTestKit.step(pounder, frames.press(ACTION));
        helper.assertValueEqual(move(pounder), ArcadeMoves.ROLL, "action right after a ground pound lands, crouch held");
        double poundSpeed = ArcadeController.of(pounder).state().horizontalSpeed();
        helper.assertTrue(poundSpeed > plainSpeed + 0.15D, "a ground pound roll should start faster than a roll: " + poundSpeed + " against " + plainSpeed);
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void aGroundPoundLandingTooLongAgoRollsLikeAnyRoll(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        var pounder = standing(helper, new BlockPos(5, 1, 5));
        var frames = new ArcadeTestKit.Frames();
        ArcadeTestKit.step(pounder, frames.press(JUMP));
        for (int i = 0; i < 6; i++) {
            ArcadeTestKit.step(pounder, frames.next());
        }
        frames.letGo(JUMP);
        ArcadeTestKit.step(pounder, frames.press(CROUCH));
        for (int i = 0; i < 40 && move(pounder) != ArcadeMoves.GROUND_POUND_LAND; i++) {
            ArcadeTestKit.step(pounder, frames.next());
        }
        // the impact plays out, then the player rolls from a crouch
        for (int i = 0; i < 20 && move(pounder) != ArcadeMoves.CROUCH; i++) {
            ArcadeTestKit.step(pounder, frames.next());
        }
        helper.assertValueEqual(move(pounder), ArcadeMoves.CROUCH, "crouch held once the impact is over");
        ArcadeTestKit.step(pounder, frames.forward().press(ACTION));
        helper.assertValueEqual(move(pounder), ArcadeMoves.ROLL, "crouch and action");
        helper.assertTrue(ArcadeController.of(pounder).state().horizontalSpeed() < 0.7D, "a roll long after a ground pound is a plain roll");
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void ledgeGrabHangsThenClimbs(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        // a wall 4 blocks high in front of the player, its top at y = 5
        ArcadeTestKit.fill(helper, new BlockPos(0, 1, 7), new BlockPos(9, 4, 9), Blocks.STONE.defaultBlockState());
        var player = ArcadeTestKit.client(helper, new BlockPos(5, 5, 6), ArcadeProfileIds.TRIAL);
        var frames = new ArcadeTestKit.Frames();
        for (int i = 0; i < 30 && move(player) != ArcadeMoves.LEDGE_GRAB; i++) {
            ArcadeTestKit.step(player, frames.next());
        }
        helper.assertValueEqual(move(player), ArcadeMoves.LEDGE_GRAB, "falling along a wall facing it");
        double hangY = player.getY();
        for (int i = 0; i < 100; i++) {
            ArcadeTestKit.step(player, frames.next());
        }
        helper.assertValueEqual(move(player), ArcadeMoves.LEDGE_GRAB, "still hanging five seconds later");
        helper.assertTrue(Math.abs(player.getY() - hangY) < 1.0E-6D, "the hang should not sink");
        ArcadeTestKit.step(player, frames.press(JUMP));
        helper.assertValueEqual(move(player), ArcadeMoves.LEDGE_CLIMB, "jump while hanging");
        var trace = new StringBuilder();
        for (int i = 0; i < 20 && move(player) == ArcadeMoves.LEDGE_CLIMB; i++) {
            ArcadeTestKit.step(player, frames.next());
            trace.append(' ').append(move(player).id().getPath()).append('@').append(player.getY());
        }
        ArcadeTestKit.step(player, frames.next());
        double top = helper.absolutePos(BlockPos.ZERO).getY() + 5.0D;
        helper.assertTrue(player.onGround() && Math.abs(player.getY() - top) < 1.0E-3D, "the climb should end standing on the ledge, at " + player.getY() + " for a top at " + top + ", hang at " + hangY + ":" + trace);
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void ledgeDropLetsGo(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        ArcadeTestKit.fill(helper, new BlockPos(0, 1, 7), new BlockPos(9, 4, 9), Blocks.STONE.defaultBlockState());
        var player = ArcadeTestKit.client(helper, new BlockPos(5, 5, 6), ArcadeProfileIds.TRIAL);
        var frames = new ArcadeTestKit.Frames();
        for (int i = 0; i < 30 && move(player) != ArcadeMoves.LEDGE_GRAB; i++) {
            ArcadeTestKit.step(player, frames.next());
        }
        ArcadeTestKit.step(player, frames.next());
        ArcadeTestKit.step(player, frames.press(CROUCH));
        helper.assertValueEqual(move(player), ArcadeMoves.FALL, "crouch while hanging");
        ArcadeTestKit.step(player, frames.next());
        helper.assertFalse(move(player) == ArcadeMoves.LEDGE_GRAB, "a ledge just let go of should not be grabbed again right away");
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void wallSlideCapsTheFallAndKicksOff(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        ArcadeTestKit.fill(helper, new BlockPos(0, 1, 7), new BlockPos(9, 11, 7), Blocks.STONE.defaultBlockState());
        var player = ArcadeTestKit.client(helper, new BlockPos(5, 9, 6), ArcadeProfileIds.TRIAL);
        // a ledge must not be in reach: the wall goes up to the ceiling
        var frames = new ArcadeTestKit.Frames().forward();
        for (int i = 0; i < 20 && move(player) != ArcadeMoves.WALL_SLIDE; i++) {
            ArcadeTestKit.step(player, frames.next());
        }
        helper.assertValueEqual(move(player), ArcadeMoves.WALL_SLIDE, "falling while holding towards a wall");
        for (int i = 0; i < 5; i++) {
            ArcadeTestKit.step(player, frames.next());
        }
        double vy = ArcadeController.of(player).state().vy;
        helper.assertTrue(vy >= -0.15D - 1.0E-9D, "a wall slide caps the fall at 0.15 b/t, got " + vy);
        double z = player.getZ();
        ArcadeTestKit.step(player, frames.press(JUMP));
        helper.assertValueEqual(move(player), ArcadeMoves.WALL_JUMP, "jump while sliding");
        ArcadeTestKit.step(player, frames.next());
        helper.assertTrue(player.getZ() < z, "a wall jump pushes away from the wall");
        helper.assertTrue(ArcadeController.of(player).state().vy > 0.0D, "a wall jump goes up");
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void aRollClimbsAHillOfFullBlocks(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        // a hill going up one full block every 3 blocks, from z = 8
        for (int step = 0; step < 4; step++) {
            ArcadeTestKit.fill(helper, new BlockPos(0, 1, 8 + step * 3), new BlockPos(9, 1 + step, 47), Blocks.STONE.defaultBlockState());
        }
        var player = standing(helper, new BlockPos(5, 1, 2));
        var frames = new ArcadeTestKit.Frames().forward().hold(SPRINT);
        for (int i = 0; i < 4; i++) {
            ArcadeTestKit.step(player, frames.next());
        }
        ArcadeTestKit.step(player, frames.press(CROUCH));
        ArcadeTestKit.step(player, frames.press(ACTION));
        helper.assertValueEqual(move(player), ArcadeMoves.ROLL, "crouch and action while running");
        double startY = player.getY();
        for (int i = 0; i < 40 && move(player) == ArcadeMoves.ROLL; i++) {
            ArcadeTestKit.step(player, i % 6 == 5 ? frames.tap(ACTION) : frames.next());
        }
        helper.assertTrue(player.getY() >= startY + 4.0D - 1.0E-6D, "the roll should have climbed the four steps, it rose " + (player.getY() - startY) + " and is now " + move(player));
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void rollingDownStairsGainsSpeed(GameTestHelper helper) {
        // a flat run up to z = 6, then stairs going down one block per block
        ArcadeTestKit.fill(helper, new BlockPos(0, 0, 0), new BlockPos(9, 9, 6), Blocks.STONE.defaultBlockState());
        var stairs = Blocks.STONE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.NORTH);
        for (int step = 0; step < 9; step++) {
            int z = 7 + step;
            int top = 9 - step;
            ArcadeTestKit.fill(helper, new BlockPos(0, 0, z), new BlockPos(9, top - 1, z), Blocks.STONE.defaultBlockState());
            ArcadeTestKit.fill(helper, new BlockPos(0, top, z), new BlockPos(9, top, z), stairs);
        }
        ArcadeTestKit.fill(helper, new BlockPos(0, 0, 16), new BlockPos(9, 0, 47), Blocks.STONE.defaultBlockState());
        var player = standing(helper, new BlockPos(5, 10, 2));
        var frames = new ArcadeTestKit.Frames().forward().hold(CROUCH);
        ArcadeTestKit.step(player, frames.press(ACTION));
        helper.assertValueEqual(move(player), ArcadeMoves.ROLL, "action while crouching on the ground");
        double startSpeed = ArcadeController.of(player).state().horizontalSpeed();
        double bestSpeed = startSpeed;
        for (int i = 0; i < 30 && player.getZ() < helper.absolutePos(new BlockPos(0, 0, 15)).getZ(); i++) {
            ArcadeTestKit.step(player, frames.next());
            bestSpeed = Math.max(bestSpeed, ArcadeController.of(player).state().horizontalSpeed());
        }
        helper.assertValueEqual(move(player), ArcadeMoves.ROLL, "still rolling at the bottom of the stairs");
        helper.assertTrue(bestSpeed > startSpeed + 0.2D, "rolling down nine steps should gain speed: from " + startSpeed + " to " + bestSpeed);
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void crouchingWhileRunningDownStairsSlides(GameTestHelper helper) {
        ArcadeTestKit.fill(helper, new BlockPos(0, 0, 0), new BlockPos(9, 9, 6), Blocks.STONE.defaultBlockState());
        var stairs = Blocks.STONE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.NORTH);
        for (int step = 0; step < 9; step++) {
            int z = 7 + step;
            int top = 9 - step;
            ArcadeTestKit.fill(helper, new BlockPos(0, 0, z), new BlockPos(9, top - 1, z), Blocks.STONE.defaultBlockState());
            ArcadeTestKit.fill(helper, new BlockPos(0, top, z), new BlockPos(9, top, z), stairs);
        }
        ArcadeTestKit.fill(helper, new BlockPos(0, 0, 16), new BlockPos(9, 0, 47), Blocks.STONE.defaultBlockState());
        var player = standing(helper, new BlockPos(5, 10, 2));
        var frames = new ArcadeTestKit.Frames().forward().hold(SPRINT);
        boolean slid = false;
        for (int i = 0; i < 40 && !slid; i++) {
            boolean onTheStairs = player.getZ() > helper.absolutePos(new BlockPos(0, 0, 9)).getZ();
            ArcadeTestKit.step(player, onTheStairs ? frames.tap(CROUCH) : frames.next());
            slid = move(player) == ArcadeMoves.SLIDE;
        }
        helper.assertTrue(slid, "crouch while running down the stairs");
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void aSpinSlowsTheFallOncePerAir(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        var player = ArcadeTestKit.client(helper, new BlockPos(5, 11, 5), ArcadeProfileIds.TRIAL);
        var frames = new ArcadeTestKit.Frames();
        for (int i = 0; i < 8; i++) {
            ArcadeTestKit.step(player, frames.next());
        }
        ArcadeTestKit.step(player, frames.tap(SPIN));
        helper.assertValueEqual(move(player), ArcadeMoves.SPIN, "spin in the air");
        // the lift, then a slow fall
        double slowest = 0.0D;
        for (int i = 0; i < 12; i++) {
            ArcadeTestKit.step(player, frames.next());
            slowest = Math.min(slowest, ArcadeController.of(player).state().vy);
        }
        helper.assertTrue(slowest >= -0.08D - 1.0E-9D, "a spin falls at most 0.08 b/t, fell at " + -slowest);
        ArcadeTestKit.step(player, frames.tap(SPIN));
        helper.assertFalse(move(player) == ArcadeMoves.SPIN && ArcadeController.of(player).state().moveTicks == 0, "a second spin in the same air");
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void turningTheStickAroundSpins(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        var player = ArcadeTestKit.client(helper, new BlockPos(5, 11, 5), ArcadeProfileIds.TRIAL);
        var frames = new ArcadeTestKit.Frames();
        ArcadeTestKit.step(player, frames.next());
        boolean spun = false;
        // a little more than a full turn, before the fall reaches the floor
        for (int i = 0; i <= 9 && !spun; i++) {
            helper.assertFalse(player.onGround(), "the player landed before the stick went around, the test proves nothing");
            double angle = Math.toRadians(i * 45.0D);
            frames.stick((float) Math.sin(angle), (float) Math.cos(angle));
            ArcadeTestKit.step(player, frames.next());
            spun = move(player) == ArcadeMoves.SPIN;
        }
        helper.assertTrue(spun, "a full turn of the stick in the air should spin");
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void reversingAtSpeedSkidsIntoASideSomersault(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 4));
        var frames = new ArcadeTestKit.Frames().forward().hold(SPRINT);
        for (int i = 0; i < 6; i++) {
            ArcadeTestKit.step(player, frames.next());
        }
        frames.stick(0.0F, -1.0F);
        ArcadeTestKit.step(player, frames.next());
        helper.assertValueEqual(move(player), ArcadeMoves.SKID, "reversing the stick at run speed");
        ArcadeTestKit.step(player, frames.press(JUMP));
        helper.assertValueEqual(move(player), ArcadeMoves.SIDE_SOMERSAULT, "jumping out of a skid");
        ArcadeTestKit.step(player, frames.next());
        helper.assertTrue(ArcadeController.of(player).state().vz < 0.0D, "the somersault heads the new way");
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void diveLandsIntoARollout(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 4));
        var frames = new ArcadeTestKit.Frames().forward().hold(SPRINT);
        for (int i = 0; i < 4; i++) {
            ArcadeTestKit.step(player, frames.next());
        }
        ArcadeTestKit.step(player, frames.press(JUMP));
        ArcadeTestKit.step(player, frames.next());
        ArcadeTestKit.step(player, frames.press(ACTION));
        // pressed within the cancel window of the jump, the buffered press dives as soon as it ends
        for (int i = 0; i < 3 && move(player) != ArcadeMoves.DIVE; i++) {
            ArcadeTestKit.step(player, frames.next());
        }
        helper.assertValueEqual(move(player), ArcadeMoves.DIVE, "action in the air");
        // the lunge, less the air drag of the trial profile on the tick it starts
        double lunge = ArcadeController.of(player).state().horizontalSpeed();
        helper.assertTrue(lunge >= 0.5D * 0.99D - 1.0E-9D, "a dive lunges at least at 0.5 b/t, got " + lunge);
        for (int i = 0; i < 40 && !player.onGround(); i++) {
            ArcadeTestKit.step(player, frames.next());
        }
        helper.assertValueEqual(move(player), ArcadeMoves.ROLLOUT, "the landing of a dive");
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void runningIntoAOneBlockStepVaultsOverIt(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        ArcadeTestKit.fill(helper, new BlockPos(0, 1, 12), new BlockPos(9, 1, 14), Blocks.STONE.defaultBlockState());
        var player = standing(helper, new BlockPos(5, 1, 2));
        var frames = new ArcadeTestKit.Frames().forward().hold(SPRINT);
        boolean vaulted = false;
        for (int i = 0; i < 40; i++) {
            ArcadeTestKit.step(player, frames.next());
            vaulted |= move(player) == ArcadeMoves.VAULT;
        }
        helper.assertTrue(vaulted, "running into a block high step should vault it");
        helper.assertTrue(player.getZ() > helper.absolutePos(new BlockPos(0, 0, 15)).getZ(), "the vault should carry the player past the step");
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void iceKeepsMomentum(GameTestHelper helper) {
        ArcadeTestKit.floor(helper, Blocks.ICE.defaultBlockState());
        var player = standing(helper, new BlockPos(5, 1, 2));
        var frames = new ArcadeTestKit.Frames().forward().hold(SPRINT);
        for (int i = 0; i < 20; i++) {
            ArcadeTestKit.step(player, frames.next());
        }
        double speed = ArcadeController.of(player).state().horizontalSpeed();
        frames.release().letGo(SPRINT);
        for (int i = 0; i < 10; i++) {
            ArcadeTestKit.step(player, frames.next());
        }
        double after = ArcadeController.of(player).state().horizontalSpeed();
        helper.assertTrue(speed > 0.1D, "the run on ice never picked up speed, the test proves nothing: " + speed);
        helper.assertTrue(after > speed * 0.9D, "letting go of the stick on ice should keep the speed: from " + speed + " to " + after);
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void bounceBlocksLaunchBackUp(GameTestHelper helper) {
        ArcadeTestKit.floor(helper, Blocks.SLIME_BLOCK.defaultBlockState());
        var player = ArcadeTestKit.client(helper, new BlockPos(5, 4, 5), ArcadeProfileIds.TRIAL);
        var frames = new ArcadeTestKit.Frames();
        for (int i = 0; i < 30 && player.getY() > helper.absolutePos(BlockPos.ZERO).getY() + 1.05D; i++) {
            ArcadeTestKit.step(player, frames.next());
        }
        ArcadeTestKit.step(player, frames.next());
        double startY = helper.absolutePos(BlockPos.ZERO).getY() + 1.0D;
        double peak = 0.0D;
        for (int i = 0; i < 40; i++) {
            ArcadeTestKit.step(player, frames.next());
            peak = Math.max(peak, player.getY() - startY);
        }
        helper.assertTrue(peak > 3.5D, "a landing on a bounce block should send the player about 4 blocks up, got " + peak);
        helper.succeed();
    }
}
