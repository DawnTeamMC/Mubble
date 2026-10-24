package fr.hugman.mubble.test.gametest.platformer;

import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.ACTION;
import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.AIMING;
import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.CROUCH;
import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.JUMP;
import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.SPIN;
import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.SPRINT;

import fr.hugman.mubble.platformer.references.PlatformerProfileIds;
import fr.hugman.mubble.platformer.PlatformerController;
import fr.hugman.mubble.platformer.PlatformerPreview;
import fr.hugman.mubble.platformer.move.PlatformerMove;
import fr.hugman.mubble.platformer.move.PlatformerMoves;
import fr.hugman.mubble.platformer.sim.PlatformerInputCollector;
import fr.hugman.mubble.platformer.sim.PlatformerInputFrame;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.phys.Vec3;

/**
 * The moves themselves, played by a stand-in client against real blocks, under {@code mubble:trial}.
 * <p>
 * These check what the moves do with numbers, not how they feel: heights, distances, which state
 * follows which. Feel is for a human to judge.
 */
public class PlatformerMovementGameTest {
    private static final double HEIGHT_TOLERANCE = 0.02D;

    private static Player standing(GameTestHelper helper, BlockPos pos) {
        var player = PlatformerTestKit.client(helper, pos, PlatformerProfileIds.TRIAL);
        var frames = new PlatformerTestKit.Frames();
        for (int i = 0; i < 4; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        helper.assertTrue(player.onGround(), "the stand-in never landed, the test proves nothing");
        return player;
    }

    private static PlatformerMove move(Player player) {
        return PlatformerController.of(player).state().move;
    }

    /** Steps until the player lands, and returns the highest the feet went, relative to {@code fromY}. */
    private static double peakUntilLanding(GameTestHelper helper, Player player, PlatformerTestKit.Frames frames, double fromY, int maxTicks) {
        double peak = 0.0D;
        boolean left = false;
        for (int i = 0; i < maxTicks; i++) {
            PlatformerTestKit.step(player, frames.next());
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

    @GameTest(structure = PlatformerTestKit.LANE)
    public void jumpReachesItsAuthoredHeight(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 5));
        var frames = new PlatformerTestKit.Frames();
        double startY = player.getY();
        PlatformerTestKit.step(player, frames.press(JUMP));
        helper.assertValueEqual(move(player), PlatformerMoves.JUMP, "move after pressing jump");
        double peak = peakUntilLanding(helper, player, frames, startY, 60);
        helper.assertTrue(Math.abs(peak - 2.2D) < HEIGHT_TOLERANCE, "a held jump should reach 2.2 blocks, got " + peak);
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void releasingJumpEarlyCutsTheJump(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 5));
        var frames = new PlatformerTestKit.Frames();
        double startY = player.getY();
        PlatformerTestKit.step(player, frames.press(JUMP));
        PlatformerTestKit.step(player, frames.next());
        frames.letGo(JUMP);
        double peak = peakUntilLanding(helper, player, frames, startY, 60);
        helper.assertTrue(peak < 1.6D, "a jump released after two ticks should stay low, got " + peak);
        helper.assertTrue(peak > 0.3D, "a released jump should still leave the ground, got " + peak);
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void jumpsChainIntoDoubleAndTripleJumps(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 2));
        var frames = new PlatformerTestKit.Frames().forward().hold(SPRINT);
        for (int i = 0; i < 4; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        double[] expected = {2.2D, 3.2D, 4.6D};
        PlatformerMove[] moves = {PlatformerMoves.JUMP, PlatformerMoves.DOUBLE_JUMP, PlatformerMoves.TRIPLE_JUMP};
        for (int jump = 0; jump < 3; jump++) {
            double startY = player.getY();
            PlatformerTestKit.step(player, frames.press(JUMP));
            helper.assertValueEqual(move(player), moves[jump], "move of jump " + (jump + 1));
            double peak = peakUntilLanding(helper, player, frames, startY, 80);
            helper.assertTrue(Math.abs(peak - expected[jump]) < HEIGHT_TOLERANCE, "jump " + (jump + 1) + " should reach " + expected[jump] + ", got " + peak);
            frames.letGo(JUMP);
        }
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void standingStillDoesNotChain(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 5));
        var frames = new PlatformerTestKit.Frames();
        double startY = player.getY();
        PlatformerTestKit.step(player, frames.press(JUMP));
        peakUntilLanding(helper, player, frames, startY, 60);
        frames.letGo(JUMP);
        PlatformerTestKit.step(player, frames.press(JUMP));
        helper.assertValueEqual(move(player), PlatformerMoves.JUMP, "a second jump without speed should be a plain jump");
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void backflipGoesHighAndBackwards(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 20));
        var frames = new PlatformerTestKit.Frames().hold(CROUCH);
        PlatformerTestKit.step(player, frames.next());
        double startY = player.getY();
        double startZ = player.getZ();
        PlatformerTestKit.step(player, frames.press(JUMP));
        helper.assertValueEqual(move(player), PlatformerMoves.BACKFLIP, "crouching and jumping while standing");
        frames.letGo(CROUCH);
        double peak = peakUntilLanding(helper, player, frames, startY, 80);
        helper.assertTrue(Math.abs(peak - 5.2D) < HEIGHT_TOLERANCE, "a backflip should reach 5.2 blocks, got " + peak);
        helper.assertTrue(player.getZ() < startZ - 0.5D, "a backflip should drift backwards, from " + startZ + " to " + player.getZ());
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void longJumpGoesFartherThanAJump(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        double jumpDistance = this.runAndJump(helper, new BlockPos(2, 1, 2), false);
        double longJumpDistance = this.runAndJump(helper, new BlockPos(7, 1, 2), true);
        helper.assertTrue(longJumpDistance > jumpDistance + 1.0D, "a long jump should go farther than a running jump: " + longJumpDistance + " against " + jumpDistance);
        helper.succeed();
    }

    private double runAndJump(GameTestHelper helper, BlockPos start, boolean longJump) {
        var player = standing(helper, start);
        var frames = new PlatformerTestKit.Frames().forward().hold(SPRINT);
        for (int i = 0; i < 6; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        if (longJump) {
            PlatformerTestKit.step(player, frames.press(CROUCH));
        }
        double startZ = player.getZ();
        PlatformerTestKit.step(player, frames.press(JUMP));
        helper.assertValueEqual(move(player), longJump ? PlatformerMoves.LONG_JUMP : PlatformerMoves.JUMP, "move of the running jump");
        frames.letGo(CROUCH);
        peakUntilLanding(helper, player, frames.release(), player.getY(), 80);
        return player.getZ() - startZ;
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void coyoteTimeKeepsAJumpAfterTheEdge(GameTestHelper helper) {
        // a raised platform ending at z = 8
        PlatformerTestKit.fill(helper, new BlockPos(0, 0, 0), new BlockPos(9, 3, 7), Blocks.STONE.defaultBlockState());
        PlatformerTestKit.fill(helper, new BlockPos(0, 0, 8), new BlockPos(9, 0, 47), Blocks.STONE.defaultBlockState());
        var player = standing(helper, new BlockPos(5, 4, 5));
        var frames = new PlatformerTestKit.Frames().forward().hold(SPRINT);
        int airborne = 0;
        for (int i = 0; i < 40 && airborne < 2; i++) {
            PlatformerTestKit.step(player, frames.next());
            airborne = player.onGround() ? 0 : airborne + 1;
        }
        helper.assertValueEqual(airborne, 2, "ticks spent in the air before pressing jump");
        PlatformerTestKit.step(player, frames.press(JUMP));
        helper.assertValueEqual(move(player), PlatformerMoves.JUMP, "a jump pressed two ticks after running off the edge");
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void aJumpPressedOneFrameBeforeLandingStillHappens(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var player = PlatformerTestKit.client(helper, new BlockPos(5, 4, 5), PlatformerProfileIds.TRIAL);
        var collector = new PlatformerInputCollector();
        long ms = 1_000_000L;
        long now = 0L;
        int tick = 0;
        // fall until the next tick lands
        while (!player.onGround() && tick < 60) {
            var state = PlatformerController.of(player).state();
            double gap = player.getY() - (helper.absolutePos(BlockPos.ZERO).getY() + 1.0D);
            boolean landsNext = gap + state.vy - 0.2D < 0.0D;
            if (landsNext) {
                // one frame before the tick, jump is pressed and already released by the tick
                collector.sampleFrame(now + 33 * ms, 0, JUMP);
            }
            now += 50 * ms;
            PlatformerTestKit.step(player, collector.tick(tick++, now, 0.0F, 0.0F, 0.0F, true));
            if (landsNext) {
                break;
            }
        }
        helper.assertTrue(player.onGround(), "the player should have landed with the press in the buffer");
        now += 50 * ms;
        PlatformerTestKit.step(player, collector.tick(tick, now, 0.0F, 0.0F, 0.0F, true));
        helper.assertValueEqual(move(player), PlatformerMoves.JUMP, "the buffered jump should go off right after landing");
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void groundPoundDropsStraightAndNegatesFallDamage(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var pounder = PlatformerTestKit.client(helper, new BlockPos(2, 10, 5), PlatformerProfileIds.OVERWORLD);
        var faller = PlatformerTestKit.client(helper, new BlockPos(7, 10, 5), PlatformerProfileIds.OVERWORLD);
        var frames = new PlatformerTestKit.Frames();
        var fallerFrames = new PlatformerTestKit.Frames();
        PlatformerTestKit.step(pounder, frames.next());
        PlatformerTestKit.step(faller, fallerFrames.next());
        PlatformerTestKit.step(pounder, frames.press(CROUCH));
        helper.assertValueEqual(move(pounder), PlatformerMoves.GROUND_POUND, "crouch in the air");
        double x = pounder.getX();
        for (int i = 0; i < 40 && !pounder.onGround(); i++) {
            PlatformerTestKit.step(pounder, frames.next());
        }
        for (int i = 0; i < 60 && !faller.onGround(); i++) {
            PlatformerTestKit.step(faller, fallerFrames.next());
        }
        helper.assertValueEqual(move(pounder), PlatformerMoves.GROUND_POUND_LAND, "the landing of a ground pound");
        helper.assertTrue(Math.abs(pounder.getX() - x) < 1.0E-6D, "a ground pound drops straight down");
        helper.assertValueEqual(pounder.getHealth(), pounder.getMaxHealth(), "health after a ground pound landing");
        helper.assertTrue(faller.getHealth() < faller.getMaxHealth(), "the same fall without a ground pound should hurt, the test proves nothing otherwise");
        // and right after, a jump goes very high
        double startY = pounder.getY();
        PlatformerTestKit.step(pounder, frames.press(JUMP));
        helper.assertValueEqual(move(pounder), PlatformerMoves.GROUND_POUND_JUMP, "a jump right after the landing");
        double peak = peakUntilLanding(helper, pounder, frames, startY, 80);
        helper.assertTrue(Math.abs(peak - 5.0D * 0.9D) < HEIGHT_TOLERANCE, "a ground pound jump under the overworld profile should reach 4.5, got " + peak);
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void rollingOutOfAGroundPoundIsFasterThanARoll(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        // a plain roll from standing, for comparison
        var roller = standing(helper, new BlockPos(2, 1, 5));
        var plain = new PlatformerTestKit.Frames().forward().hold(CROUCH);
        PlatformerTestKit.step(roller, plain.press(ACTION));
        helper.assertValueEqual(move(roller), PlatformerMoves.ROLL, "crouch and action on the ground");
        double plainSpeed = PlatformerController.of(roller).state().horizontalSpeed();

        var pounder = standing(helper, new BlockPos(7, 1, 5));
        var frames = new PlatformerTestKit.Frames().forward();
        PlatformerTestKit.step(pounder, frames.press(JUMP));
        for (int i = 0; i < 6; i++) {
            PlatformerTestKit.step(pounder, frames.next());
        }
        frames.letGo(JUMP);
        PlatformerTestKit.step(pounder, frames.press(CROUCH));
        helper.assertValueEqual(move(pounder), PlatformerMoves.GROUND_POUND, "crouch in the air");
        // crouch stays held through the pound, as it does when one rolls out of it
        for (int i = 0; i < 40 && move(pounder) != PlatformerMoves.GROUND_POUND_LAND; i++) {
            PlatformerTestKit.step(pounder, frames.next());
        }
        helper.assertValueEqual(move(pounder), PlatformerMoves.GROUND_POUND_LAND, "the landing of the ground pound");
        PlatformerTestKit.step(pounder, frames.press(ACTION));
        helper.assertValueEqual(move(pounder), PlatformerMoves.ROLL, "action right after a ground pound lands, crouch held");
        double poundSpeed = PlatformerController.of(pounder).state().horizontalSpeed();
        helper.assertTrue(poundSpeed > plainSpeed + 0.15D, "a ground pound roll should start faster than a roll: " + poundSpeed + " against " + plainSpeed);
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void aGroundPoundLandingTooLongAgoRollsLikeAnyRoll(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var pounder = standing(helper, new BlockPos(5, 1, 5));
        var frames = new PlatformerTestKit.Frames();
        PlatformerTestKit.step(pounder, frames.press(JUMP));
        for (int i = 0; i < 6; i++) {
            PlatformerTestKit.step(pounder, frames.next());
        }
        frames.letGo(JUMP);
        PlatformerTestKit.step(pounder, frames.press(CROUCH));
        for (int i = 0; i < 40 && move(pounder) != PlatformerMoves.GROUND_POUND_LAND; i++) {
            PlatformerTestKit.step(pounder, frames.next());
        }
        // the impact plays out, then the player rolls from a crouch
        for (int i = 0; i < 20 && move(pounder) != PlatformerMoves.CROUCH; i++) {
            PlatformerTestKit.step(pounder, frames.next());
        }
        helper.assertValueEqual(move(pounder), PlatformerMoves.CROUCH, "crouch held once the impact is over");
        PlatformerTestKit.step(pounder, frames.forward().press(ACTION));
        helper.assertValueEqual(move(pounder), PlatformerMoves.ROLL, "crouch and action");
        helper.assertTrue(PlatformerController.of(pounder).state().horizontalSpeed() < 0.7D, "a roll long after a ground pound is a plain roll");
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void ledgeGrabHangsThenClimbs(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        // a wall 4 blocks high in front of the player, its top at y = 5
        PlatformerTestKit.fill(helper, new BlockPos(0, 1, 7), new BlockPos(9, 4, 9), Blocks.STONE.defaultBlockState());
        var player = PlatformerTestKit.client(helper, new BlockPos(5, 5, 6), PlatformerProfileIds.TRIAL);
        var frames = new PlatformerTestKit.Frames();
        for (int i = 0; i < 30 && move(player) != PlatformerMoves.LEDGE_GRAB; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        helper.assertValueEqual(move(player), PlatformerMoves.LEDGE_GRAB, "falling along a wall facing it");
        double hangY = player.getY();
        for (int i = 0; i < 100; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        helper.assertValueEqual(move(player), PlatformerMoves.LEDGE_GRAB, "still hanging five seconds later");
        helper.assertTrue(Math.abs(player.getY() - hangY) < 1.0E-6D, "the hang should not sink");
        PlatformerTestKit.step(player, frames.press(JUMP));
        helper.assertValueEqual(move(player), PlatformerMoves.LEDGE_CLIMB, "jump while hanging");
        var trace = new StringBuilder();
        for (int i = 0; i < 20 && move(player) == PlatformerMoves.LEDGE_CLIMB; i++) {
            PlatformerTestKit.step(player, frames.next());
            trace.append(' ').append(move(player).id().getPath()).append('@').append(player.getY());
        }
        PlatformerTestKit.step(player, frames.next());
        double top = helper.absolutePos(BlockPos.ZERO).getY() + 5.0D;
        helper.assertTrue(player.onGround() && Math.abs(player.getY() - top) < 1.0E-3D, "the climb should end standing on the ledge, at " + player.getY() + " for a top at " + top + ", hang at " + hangY + ":" + trace);
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void ledgeDropLetsGo(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        PlatformerTestKit.fill(helper, new BlockPos(0, 1, 7), new BlockPos(9, 4, 9), Blocks.STONE.defaultBlockState());
        var player = PlatformerTestKit.client(helper, new BlockPos(5, 5, 6), PlatformerProfileIds.TRIAL);
        var frames = new PlatformerTestKit.Frames();
        for (int i = 0; i < 30 && move(player) != PlatformerMoves.LEDGE_GRAB; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        PlatformerTestKit.step(player, frames.next());
        PlatformerTestKit.step(player, frames.press(CROUCH));
        helper.assertValueEqual(move(player), PlatformerMoves.FALL, "crouch while hanging");
        PlatformerTestKit.step(player, frames.next());
        helper.assertFalse(move(player) == PlatformerMoves.LEDGE_GRAB, "a ledge just let go of should not be grabbed again right away");
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void wallSlideCapsTheFallAndKicksOff(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        PlatformerTestKit.fill(helper, new BlockPos(0, 1, 7), new BlockPos(9, 11, 7), Blocks.STONE.defaultBlockState());
        var player = PlatformerTestKit.client(helper, new BlockPos(5, 9, 6), PlatformerProfileIds.TRIAL);
        // a ledge must not be in reach: the wall goes up to the ceiling
        var frames = new PlatformerTestKit.Frames().forward();
        for (int i = 0; i < 20 && move(player) != PlatformerMoves.WALL_SLIDE; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        helper.assertValueEqual(move(player), PlatformerMoves.WALL_SLIDE, "falling while holding towards a wall");
        for (int i = 0; i < 5; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        double vy = PlatformerController.of(player).state().vy;
        helper.assertTrue(vy >= -0.15D - 1.0E-9D, "a wall slide caps the fall at 0.15 b/t, got " + vy);
        double z = player.getZ();
        PlatformerTestKit.step(player, frames.press(JUMP));
        helper.assertValueEqual(move(player), PlatformerMoves.WALL_JUMP, "jump while sliding");
        PlatformerTestKit.step(player, frames.next());
        helper.assertTrue(player.getZ() < z, "a wall jump pushes away from the wall");
        helper.assertTrue(PlatformerController.of(player).state().vy > 0.0D, "a wall jump goes up");
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void aRollClimbsAHillOfFullBlocks(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        // a hill going up one full block every 3 blocks, from z = 8
        for (int step = 0; step < 4; step++) {
            PlatformerTestKit.fill(helper, new BlockPos(0, 1, 8 + step * 3), new BlockPos(9, 1 + step, 47), Blocks.STONE.defaultBlockState());
        }
        var player = standing(helper, new BlockPos(5, 1, 2));
        var frames = new PlatformerTestKit.Frames().forward().hold(SPRINT);
        for (int i = 0; i < 4; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        PlatformerTestKit.step(player, frames.press(CROUCH));
        PlatformerTestKit.step(player, frames.press(ACTION));
        helper.assertValueEqual(move(player), PlatformerMoves.ROLL, "crouch and action while running");
        double startY = player.getY();
        for (int i = 0; i < 40 && move(player) == PlatformerMoves.ROLL; i++) {
            PlatformerTestKit.step(player, i % 6 == 5 ? frames.tap(ACTION) : frames.next());
        }
        helper.assertTrue(player.getY() >= startY + 4.0D - 1.0E-6D, "the roll should have climbed the four steps, it rose " + (player.getY() - startY) + " and is now " + move(player));
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void rollingDownStairsGainsSpeed(GameTestHelper helper) {
        // a flat run up to z = 6, then stairs going down one block per block
        PlatformerTestKit.fill(helper, new BlockPos(0, 0, 0), new BlockPos(9, 9, 6), Blocks.STONE.defaultBlockState());
        var stairs = Blocks.STONE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.NORTH);
        for (int step = 0; step < 9; step++) {
            int z = 7 + step;
            int top = 9 - step;
            PlatformerTestKit.fill(helper, new BlockPos(0, 0, z), new BlockPos(9, top - 1, z), Blocks.STONE.defaultBlockState());
            PlatformerTestKit.fill(helper, new BlockPos(0, top, z), new BlockPos(9, top, z), stairs);
        }
        PlatformerTestKit.fill(helper, new BlockPos(0, 0, 16), new BlockPos(9, 0, 47), Blocks.STONE.defaultBlockState());
        var player = standing(helper, new BlockPos(5, 10, 2));
        var frames = new PlatformerTestKit.Frames().forward().hold(CROUCH);
        PlatformerTestKit.step(player, frames.press(ACTION));
        helper.assertValueEqual(move(player), PlatformerMoves.ROLL, "action while crouching on the ground");
        double startSpeed = PlatformerController.of(player).state().horizontalSpeed();
        double bestSpeed = startSpeed;
        for (int i = 0; i < 30 && player.getZ() < helper.absolutePos(new BlockPos(0, 0, 15)).getZ(); i++) {
            PlatformerTestKit.step(player, frames.next());
            bestSpeed = Math.max(bestSpeed, PlatformerController.of(player).state().horizontalSpeed());
        }
        helper.assertValueEqual(move(player), PlatformerMoves.ROLL, "still rolling at the bottom of the stairs");
        helper.assertTrue(bestSpeed > startSpeed + 0.2D, "rolling down nine steps should gain speed: from " + startSpeed + " to " + bestSpeed);
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void crouchingWhileRunningDownStairsSlides(GameTestHelper helper) {
        PlatformerTestKit.fill(helper, new BlockPos(0, 0, 0), new BlockPos(9, 9, 6), Blocks.STONE.defaultBlockState());
        var stairs = Blocks.STONE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.NORTH);
        for (int step = 0; step < 9; step++) {
            int z = 7 + step;
            int top = 9 - step;
            PlatformerTestKit.fill(helper, new BlockPos(0, 0, z), new BlockPos(9, top - 1, z), Blocks.STONE.defaultBlockState());
            PlatformerTestKit.fill(helper, new BlockPos(0, top, z), new BlockPos(9, top, z), stairs);
        }
        PlatformerTestKit.fill(helper, new BlockPos(0, 0, 16), new BlockPos(9, 0, 47), Blocks.STONE.defaultBlockState());
        var player = standing(helper, new BlockPos(5, 10, 2));
        var frames = new PlatformerTestKit.Frames().forward().hold(SPRINT);
        boolean slid = false;
        for (int i = 0; i < 40 && !slid; i++) {
            boolean onTheStairs = player.getZ() > helper.absolutePos(new BlockPos(0, 0, 9)).getZ();
            PlatformerTestKit.step(player, onTheStairs ? frames.tap(CROUCH) : frames.next());
            slid = move(player) == PlatformerMoves.SLIDE;
        }
        helper.assertTrue(slid, "crouch while running down the stairs");
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void aSpinSlowsTheFallOncePerAir(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var player = PlatformerTestKit.client(helper, new BlockPos(5, 11, 5), PlatformerProfileIds.TRIAL);
        var frames = new PlatformerTestKit.Frames();
        for (int i = 0; i < 8; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        PlatformerTestKit.step(player, frames.tap(SPIN));
        helper.assertValueEqual(move(player), PlatformerMoves.SPIN, "spin in the air");
        // the lift, then a slow fall
        double slowest = 0.0D;
        for (int i = 0; i < 12; i++) {
            PlatformerTestKit.step(player, frames.next());
            slowest = Math.min(slowest, PlatformerController.of(player).state().vy);
        }
        helper.assertTrue(slowest >= -0.08D - 1.0E-9D, "a spin falls at most 0.08 b/t, fell at " + -slowest);
        PlatformerTestKit.step(player, frames.tap(SPIN));
        helper.assertFalse(move(player) == PlatformerMoves.SPIN && PlatformerController.of(player).state().moveTicks == 0, "a second spin in the same air");
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void turningTheStickAroundSpins(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var player = PlatformerTestKit.client(helper, new BlockPos(5, 11, 5), PlatformerProfileIds.TRIAL);
        var frames = new PlatformerTestKit.Frames();
        PlatformerTestKit.step(player, frames.next());
        boolean spun = false;
        // a little more than a full turn, before the fall reaches the floor
        for (int i = 0; i <= 9 && !spun; i++) {
            helper.assertFalse(player.onGround(), "the player landed before the stick went around, the test proves nothing");
            double angle = Math.toRadians(i * 45.0D);
            frames.stick((float) Math.sin(angle), (float) Math.cos(angle));
            PlatformerTestKit.step(player, frames.next());
            spun = move(player) == PlatformerMoves.SPIN;
        }
        helper.assertTrue(spun, "a full turn of the stick in the air should spin");
        helper.succeed();
    }

    /** Turns the stick of {@code frames} by {@code step} degrees a tick, {@code ticks} times, the view turned by {@code view} degrees a tick along. */
    private static void turnStick(Player player, PlatformerTestKit.Frames frames, float step, int ticks, float view) {
        for (int i = 1; i <= ticks; i++) {
            float yaw = view * i;
            double angle = Math.toRadians(step * i + yaw);
            frames.yaw(yaw).stick((float) -Math.sin(angle), (float) Math.cos(angle));
            PlatformerTestKit.step(player, frames.next());
        }
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void aQuickTurnOfTheStickThenJumpSpinJumps(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 5));
        var frames = new PlatformerTestKit.Frames();
        turnStick(player, frames, 45.0F, 9, 0.0F);
        PlatformerTestKit.step(player, frames.tap(JUMP));
        helper.assertTrue(move(player) == PlatformerMoves.SPIN, "a full turn of the stick in 8 ticks, then jump, should spin jump, got " + move(player));
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void aSlowSweepOfTheStickDoesNotSpin(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 5));
        var frames = new PlatformerTestKit.Frames();
        // a turn and a quarter, steering round over more than a second
        turnStick(player, frames, 18.0F, 25, 0.0F);
        PlatformerTestKit.step(player, frames.tap(JUMP));
        helper.assertTrue(move(player) == PlatformerMoves.JUMP, "a slow sweep of the stick is steering, not spinning: should jump, got " + move(player));
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void turningTheCameraWithTheStickHeldDoesNotSpin(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 5));
        var frames = new PlatformerTestKit.Frames();
        // the stick held forward while the camera goes all the way round: in the world, the stick turns
        turnStick(player, frames, 0.0F, 9, 45.0F);
        PlatformerTestKit.step(player, frames.tap(JUMP));
        helper.assertTrue(move(player) == PlatformerMoves.JUMP, "turning the camera is not turning the stick: should jump, got " + move(player));
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void reversingAtSpeedSkidsIntoASideSomersault(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 4));
        var frames = new PlatformerTestKit.Frames().forward().hold(SPRINT);
        for (int i = 0; i < 6; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        frames.stick(0.0F, -1.0F);
        PlatformerTestKit.step(player, frames.next());
        helper.assertValueEqual(move(player), PlatformerMoves.SKID, "reversing the stick at run speed");
        PlatformerTestKit.step(player, frames.press(JUMP));
        helper.assertValueEqual(move(player), PlatformerMoves.SIDE_SOMERSAULT, "jumping out of a skid");
        PlatformerTestKit.step(player, frames.next());
        helper.assertTrue(PlatformerController.of(player).state().vz < 0.0D, "the somersault heads the new way");
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void diveLandsIntoARollout(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        // a ledge four blocks up to run off, high enough for a ground pound
        PlatformerTestKit.fill(helper, new BlockPos(3, 1, 1), new BlockPos(7, 3, 4), Blocks.STONE.defaultBlockState());
        var player = standing(helper, new BlockPos(5, 4, 2));
        var frames = new PlatformerTestKit.Frames().forward().hold(SPRINT);
        for (int i = 0; i < 20 && player.onGround(); i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        helper.assertFalse(player.onGround(), "the stand-in never ran off the ledge, the test proves nothing");

        // action alone in the air is for the hands: no dive
        PlatformerTestKit.step(player, frames.tap(ACTION));
        PlatformerTestKit.step(player, frames.next());
        helper.assertFalse(move(player) == PlatformerMoves.DIVE, "action in the air, without a ground pound");

        // as in Super Mario Odyssey: ground pound, then action
        PlatformerTestKit.step(player, frames.press(CROUCH));
        helper.assertValueEqual(move(player), PlatformerMoves.GROUND_POUND, "crouch in the air");
        PlatformerTestKit.step(player, frames.tap(ACTION));
        helper.assertValueEqual(move(player), PlatformerMoves.DIVE, "action during a ground pound");
        frames.letGo(CROUCH);
        // the lunge, less the air drag of the trial profile on the tick it starts
        double lunge = PlatformerController.of(player).state().horizontalSpeed();
        helper.assertTrue(lunge >= 0.5D * 0.99D - 1.0E-9D, "a dive lunges at least at 0.5 b/t, got " + lunge);
        for (int i = 0; i < 40 && !player.onGround(); i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        helper.assertValueEqual(move(player), PlatformerMoves.ROLLOUT, "the landing of a dive");
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void thePreviewOfTheGuideTellsWhatEachButtonWouldStart(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        PlatformerTestKit.fill(helper, new BlockPos(3, 1, 1), new BlockPos(7, 3, 4), Blocks.STONE.defaultBlockState());
        var player = standing(helper, new BlockPos(5, 4, 2));
        var controller = PlatformerController.of(player);
        var frames = new PlatformerTestKit.Frames();
        var before = move(player);
        helper.assertValueEqual(PlatformerPreview.ifPressed(controller, frames.next(), JUMP), PlatformerMoves.JUMP, "jump, standing");
        helper.assertValueEqual(PlatformerPreview.ifPressed(controller, frames.next(), CROUCH), PlatformerMoves.CROUCH, "crouch, standing");
        // previews change nothing
        helper.assertValueEqual(move(player), before, "the move after the previews");

        PlatformerTestKit.step(player, frames.press(CROUCH));
        helper.assertValueEqual(PlatformerPreview.ifPressed(controller, frames.next(), JUMP), PlatformerMoves.BACKFLIP, "jump, crouching still");
        helper.assertValueEqual(PlatformerPreview.ifPressed(controller, frames.next(), ACTION), PlatformerMoves.ROLL, "action, crouching");
        helper.assertTrue(PlatformerPreview.ifPressed(controller, frames.next(), CROUCH) == null, "crouch, already crouching");
        frames.letGo(CROUCH);
        PlatformerTestKit.step(player, frames.next());

        // off the ledge: crouch pounds, and action dives once it does
        frames.forward().hold(SPRINT);
        for (int i = 0; i < 20 && player.onGround(); i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        helper.assertValueEqual(PlatformerPreview.ifPressed(controller, frames.next(), CROUCH), PlatformerMoves.GROUND_POUND, "crouch, in the air");
        helper.assertTrue(PlatformerPreview.ifPressed(controller, frames.next(), ACTION) == null, "action, in the air without a ground pound");
        PlatformerTestKit.step(player, frames.press(CROUCH));
        helper.assertValueEqual(PlatformerPreview.ifPressed(controller, frames.next(), ACTION), PlatformerMoves.DIVE, "action, during a ground pound");
        helper.succeed();
    }

    /** A pool four blocks deep, walled with stone, its surface a little below y = 5. */
    static void pool(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        PlatformerTestKit.fill(helper, new BlockPos(1, 1, 1), new BlockPos(9, 4, 12), Blocks.STONE.defaultBlockState());
        PlatformerTestKit.fill(helper, new BlockPos(2, 1, 2), new BlockPos(8, 4, 11), Blocks.WATER.defaultBlockState());
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void waterIsSwumOnTheNumbersOfVanilla(GameTestHelper helper) {
        pool(helper);
        var player = PlatformerTestKit.client(helper, new BlockPos(5, 2, 3), PlatformerProfileIds.TRIAL);
        var controller = PlatformerController.of(player);
        var frames = new PlatformerTestKit.Frames();
        PlatformerTestKit.step(player, frames.next());
        helper.assertValueEqual(move(player), PlatformerMoves.SWIM, "deep in water");

        // left alone, the player sinks slowly, as in vanilla
        for (int i = 0; i < 15; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        double vy = controller.state().vy;
        helper.assertTrue(vy < 0.0D && vy > -0.05D, "a slow sink, got " + vy);

        // swimming forward settles on vanilla's pace: a tenth of a block per tick
        frames.forward();
        double pace = 0.0D;
        for (int i = 0; i < 30; i++) {
            var before = player.position();
            PlatformerTestKit.step(player, frames.next());
            pace = player.position().subtract(before).horizontalDistance();
        }
        helper.assertValueEqual(move(player), PlatformerMoves.SWIM, "still swimming");
        helper.assertTrue(Math.abs(pace - 0.1D) < 0.01D, "vanilla swims at 0.1 b/t, got " + pace);

        // under the surface, a press of jump strokes upward
        frames.release();
        PlatformerTestKit.step(player, frames.tap(JUMP));
        helper.assertValueEqual(move(player), PlatformerMoves.SWIM, "a stroke, deep in water");
        helper.assertTrue(controller.state().vy > 0.1D, "the stroke goes up, got " + controller.state().vy);
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void inWaterGroundPoundsAreSlowedAndDivesDash(GameTestHelper helper) {
        pool(helper);
        var player = PlatformerTestKit.client(helper, new BlockPos(5, 3, 4), PlatformerProfileIds.TRIAL);
        var controller = PlatformerController.of(player);
        var frames = new PlatformerTestKit.Frames();
        PlatformerTestKit.step(player, frames.next());
        helper.assertValueEqual(move(player), PlatformerMoves.SWIM, "deep in water");

        PlatformerTestKit.step(player, frames.press(CROUCH));
        helper.assertValueEqual(move(player), PlatformerMoves.GROUND_POUND, "crouch in water");
        for (int i = 0; i < 7 && controller.state().vy == 0.0D; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        double drop = -controller.state().vy;
        double dryDrop = controller.tuning().groundPoundSpeed();
        helper.assertTrue(drop > 0.0D && drop < dryDrop * 0.5D, "the water slows the pound down: " + drop + " against " + dryDrop + " out of it");

        // ground pound and action: the dive of the water, a dash forward
        PlatformerTestKit.step(player, frames.tap(ACTION));
        helper.assertValueEqual(move(player), PlatformerMoves.SWIM_DASH, "action during a ground pound, in water");
        double dash = controller.state().horizontalSpeed();
        helper.assertTrue(dash >= 0.4D, "the dash goes fast, got " + dash);
        frames.letGo(CROUCH);
        for (int i = 0; i < 20 && move(player) == PlatformerMoves.SWIM_DASH; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        helper.assertValueEqual(move(player), PlatformerMoves.SWIM, "the dash winds down into a swim");
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void jumpLeavesTheWaterFromTheSurface(GameTestHelper helper) {
        pool(helper);
        var player = PlatformerTestKit.client(helper, new BlockPos(5, 4, 5), PlatformerProfileIds.TRIAL);
        var frames = new PlatformerTestKit.Frames();
        // holding jump at the surface keeps the player bobbing there, swimming all along
        frames.hold(JUMP);
        for (int i = 0; i < 40; i++) {
            PlatformerTestKit.step(player, frames.next());
            helper.assertValueEqual(move(player), PlatformerMoves.SWIM, "bobbing at the surface, tick " + i);
        }
        frames.letGo(JUMP);
        PlatformerTestKit.step(player, frames.next());
        PlatformerTestKit.step(player, frames.press(JUMP));
        helper.assertValueEqual(move(player), PlatformerMoves.JUMP, "a press of jump at the surface");
        double y = player.getY();
        for (int i = 0; i < 4; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        helper.assertTrue(player.getY() > y + 0.5D, "the jump rises out of the water");
        helper.succeed();
    }

    /** A stone wall across the lane at z = 6, six blocks high, a ladder up its north face at x = 5. */
    static void ladder(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        PlatformerTestKit.fill(helper, new BlockPos(1, 1, 6), new BlockPos(9, 6, 6), Blocks.STONE.defaultBlockState());
        PlatformerTestKit.fill(helper, new BlockPos(5, 1, 5), new BlockPos(5, 6, 5), Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.NORTH));
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void laddersAreClimbedOnTheNumbersOfVanilla(GameTestHelper helper) {
        ladder(helper);
        var player = PlatformerTestKit.client(helper, new BlockPos(5, 1, 5), PlatformerProfileIds.TRIAL);
        var controller = PlatformerController.of(player);
        var frames = new PlatformerTestKit.Frames();
        PlatformerTestKit.step(player, frames.next());
        helper.assertTrue(controller.isDriving(), "a ladder does not hand the player over to vanilla");
        helper.assertValueEqual(move(player), PlatformerMoves.CLIMB, "on a ladder");

        // pushing into the wall climbs, at the pace of vanilla
        frames.forward();
        double rise = 0.0D;
        for (int i = 0; i < 12; i++) {
            double y = player.getY();
            PlatformerTestKit.step(player, frames.next());
            rise = player.getY() - y;
        }
        helper.assertValueEqual(move(player), PlatformerMoves.CLIMB, "climbing");
        helper.assertTrue(Math.abs(rise - 0.1176D) < 0.005D, "vanilla climbs 0.1176 b/t, got " + rise);

        // let go of the stick, and the player slides down, no faster than in vanilla
        frames.release();
        double slide = 0.0D;
        for (int i = 0; i < 8; i++) {
            double y = player.getY();
            PlatformerTestKit.step(player, frames.next());
            slide = player.getY() - y;
        }
        helper.assertTrue(slide < -0.1D && slide > -0.15D - 1.0E-6D, "vanilla slides down at 0.15 b/t, got " + slide);

        // crouch holds on
        frames.hold(CROUCH);
        PlatformerTestKit.step(player, frames.next());
        double held = player.getY();
        for (int i = 0; i < 5; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        helper.assertValueEqual(move(player), PlatformerMoves.CLIMB, "holding on");
        helper.assertTrue(Math.abs(player.getY() - held) < 1.0E-6D, "crouch holds on, moved by " + (player.getY() - held));

        // up to the top, and onto the wall
        frames.letGo(CROUCH).forward();
        for (int i = 0; i < 100 && (move(player) == PlatformerMoves.CLIMB || !player.onGround()); i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        helper.assertTrue(move(player) != PlatformerMoves.CLIMB && player.onGround(), "the player never got off the ladder, still " + move(player));
        double top = helper.absoluteVec(Vec3.atLowerCornerOf(new BlockPos(5, 7, 5))).y;
        helper.assertTrue(Math.abs(player.getY() - top) < 1.0E-3D, "standing on top of the wall, at " + player.getY() + " for " + top);
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void usingAnItemSlowsDownAsInVanilla(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 3));
        var controller = PlatformerController.of(player);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
        player.startUsingItem(InteractionHand.MAIN_HAND);
        helper.assertTrue(player.isUsingItem(), "the bow is not drawn, the test proves nothing");

        // drawing a bow, the player walks at a fifth of their pace, sprint or not
        var frames = new PlatformerTestKit.Frames().forward().hold(SPRINT);
        double pace = 0.0D;
        for (int i = 0; i < 20; i++) {
            var before = player.position();
            PlatformerTestKit.step(player, frames.next());
            pace = player.position().subtract(before).horizontalDistance();
        }
        double walk = controller.tuning().walkSpeed();
        helper.assertTrue(Math.abs(pace - walk * 0.2D) < 0.005D, "a fifth of the walking pace " + walk + ", got " + pace);
        helper.assertTrue(controller.state().move.kind() == PlatformerMove.Kind.GROUND, "still on the ground, " + move(player));

        // the bow let go, the player runs again
        player.releaseUsingItem();
        for (int i = 0; i < 20; i++) {
            var before = player.position();
            PlatformerTestKit.step(player, frames.next());
            pace = player.position().subtract(before).horizontalDistance();
        }
        helper.assertTrue(pace > walk, "running again, got " + pace);
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void theServerHoldsTheClientToTheItemItUses(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 3));
        var full = new PlatformerTestKit.Frames().forward().next();
        helper.assertTrue(full.slowedFor(player) == full, "no item in use, nothing to slow down");

        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
        player.startUsingItem(InteractionHand.MAIN_HAND);
        // a client that did not slow down is slowed down
        var slowed = full.slowedFor(player);
        helper.assertTrue(slowed.isHeld(PlatformerInputFrame.USING_ITEM), "the frame says the hands use an item");
        helper.assertTrue(Math.abs(slowed.stickMagnitude() - 0.2D) < 1.0E-6D, "the stick scaled to the use speed of the bow, got " + slowed.stickMagnitude());
        // one that slowed down keeps its stick, slowing down once is all
        helper.assertTrue(slowed.slowedFor(player).equals(slowed), "slowing down a slowed frame again changes nothing");
        // one that claims to have slowed down but did not is held to the item
        var cheating = new PlatformerInputFrame(full.tick(), full.stickX(), full.stickZ(), full.viewYaw(), full.coupled(),
                (byte) (full.held() | PlatformerInputFrame.USING_ITEM), full.pressed(), full.jumpAgeMs(), full.actionAgeMs());
        helper.assertTrue(Math.abs(cheating.slowedFor(player).stickMagnitude() - 0.2D) < 1.0E-6D, "the stick clamped to the use speed of the bow");
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void runningIntoAOneBlockStepVaultsOverIt(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        PlatformerTestKit.fill(helper, new BlockPos(0, 1, 12), new BlockPos(9, 1, 14), Blocks.STONE.defaultBlockState());
        var player = standing(helper, new BlockPos(5, 1, 2));
        var frames = new PlatformerTestKit.Frames().forward().hold(SPRINT);
        boolean vaulted = false;
        for (int i = 0; i < 40; i++) {
            PlatformerTestKit.step(player, frames.next());
            vaulted |= move(player) == PlatformerMoves.VAULT;
        }
        helper.assertTrue(vaulted, "running into a block high step should vault it");
        helper.assertTrue(player.getZ() > helper.absolutePos(new BlockPos(0, 0, 15)).getZ(), "the vault should carry the player past the step");
        helper.succeed();
    }

    /** A step a block high along the whole lane, its face at z = 6, right in front of a player standing at z = 5. */
    private static void stepAhead(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        PlatformerTestKit.fill(helper, new BlockPos(0, 1, 6), new BlockPos(9, 1, 8), Blocks.STONE.defaultBlockState());
    }

    /** Steps with {@code frames} until the feet reach {@code top}, and returns the moves gone through. */
    private static String climb(GameTestHelper helper, Player player, PlatformerTestKit.Frames frames, int jumpAt, double top, int maxTicks) {
        var trace = new StringBuilder();
        for (int i = 0; i < maxTicks && !(player.onGround() && player.getY() > top - 1.0E-3D); i++) {
            PlatformerTestKit.step(player, i == jumpAt ? frames.press(JUMP) : frames.next());
            trace.append(' ').append(move(player).id().getPath());
        }
        return trace.toString();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void pressedAgainstAOneBlockStepVaultsOverIt(GameTestHelper helper) {
        stepAhead(helper);
        var player = standing(helper, new BlockPos(5, 1, 5));
        // right against the face of the step, where any push is stopped dead
        player.setPos(player.getX(), player.getY(), helper.absolutePos(BlockPos.ZERO).getZ() + 6.0D - player.getBbWidth() * 0.5D);
        var frames = new PlatformerTestKit.Frames().forward();
        double top = helper.absolutePos(BlockPos.ZERO).getY() + 2.0D;
        var trace = climb(helper, player, frames, -1, top, 15);
        helper.assertTrue(trace.contains("vault"), "pushing into a block high step from right against it should vault it:" + trace);
        helper.assertTrue(player.onGround() && Math.abs(player.getY() - top) < 1.0E-2D, "the vault should end standing on the step, at " + player.getY() + ":" + trace);
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void walkingIntoAOneBlockStepDoesNotClimbIt(GameTestHelper helper) {
        stepAhead(helper);
        var player = standing(helper, new BlockPos(5, 1, 5));
        // a light push of the stick is a walk, and Mario does not walk up a whole block
        var frames = new PlatformerTestKit.Frames().stick(0.0F, 0.3F);
        double floor = player.getY();
        for (int i = 0; i < 20; i++) {
            PlatformerTestKit.step(player, frames.next());
            helper.assertFalse(move(player) == PlatformerMoves.VAULT, "a walk should not vault a block high step");
        }
        helper.assertTrue(Math.abs(player.getY() - floor) < 1.0E-3D, "a walk should stay below the step");
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void runningAlongAOneBlockStepDoesNotClimbIt(GameTestHelper helper) {
        stepAhead(helper);
        var player = standing(helper, new BlockPos(1, 1, 5));
        // along the step, leaning into it a little
        var frames = new PlatformerTestKit.Frames().stick(0.95F, 0.3F).hold(SPRINT);
        double floor = player.getY();
        for (int i = 0; i < 20; i++) {
            PlatformerTestKit.step(player, frames.next());
            helper.assertFalse(move(player) == PlatformerMoves.VAULT, "a run along a step should not vault it");
        }
        helper.assertTrue(Math.abs(player.getY() - floor) < 1.0E-3D, "a run along a step should stay below it");
        helper.succeed();
    }

    /**
     * An inside corner of steps for a player standing at (5, 1, 5): a step a block high to the east and
     * to the south, and two blocks high diagonally between them, as far as the lane goes.
     */
    static void corner(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var stone = Blocks.STONE.defaultBlockState();
        PlatformerTestKit.fill(helper, new BlockPos(6, 1, 5), new BlockPos(9, 1, 5), stone);
        PlatformerTestKit.fill(helper, new BlockPos(5, 1, 6), new BlockPos(5, 1, 15), stone);
        PlatformerTestKit.fill(helper, new BlockPos(6, 1, 6), new BlockPos(9, 2, 15), stone);
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void runningIntoACornerOfStepsClimbsBoth(GameTestHelper helper) {
        corner(helper);
        var player = standing(helper, new BlockPos(5, 1, 5));
        var frames = new PlatformerTestKit.Frames().stick(0.7071F, 0.7071F).hold(SPRINT);
        double top = helper.absolutePos(BlockPos.ZERO).getY() + 3.0D;
        var trace = climb(helper, player, frames, -1, top, 40);
        helper.assertTrue(Math.abs(player.getY() - top) < 1.0E-2D, "pushing into a corner of steps should climb both, ended at " + player.getY() + ":" + trace);
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void jumpingIntoACornerOfStepsEndsOnTop(GameTestHelper helper) {
        corner(helper);
        var player = standing(helper, new BlockPos(5, 1, 5));
        var frames = new PlatformerTestKit.Frames().stick(0.7071F, 0.7071F);
        double top = helper.absolutePos(BlockPos.ZERO).getY() + 3.0D;
        var trace = climb(helper, player, frames, 3, top, 40);
        helper.assertTrue(Math.abs(player.getY() - top) < 1.0E-2D, "jumping into a corner of steps should end on top of it, ended at " + player.getY() + ":" + trace);
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void iceKeepsMomentum(GameTestHelper helper) {
        PlatformerTestKit.floor(helper, Blocks.ICE.defaultBlockState());
        var player = standing(helper, new BlockPos(5, 1, 2));
        var frames = new PlatformerTestKit.Frames().forward().hold(SPRINT);
        for (int i = 0; i < 20; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        double speed = PlatformerController.of(player).state().horizontalSpeed();
        frames.release().letGo(SPRINT);
        for (int i = 0; i < 10; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        double after = PlatformerController.of(player).state().horizontalSpeed();
        helper.assertTrue(speed > 0.1D, "the run on ice never picked up speed, the test proves nothing: " + speed);
        helper.assertTrue(after > speed * 0.9D, "letting go of the stick on ice should keep the speed: from " + speed + " to " + after);
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void bounceBlocksLaunchBackUp(GameTestHelper helper) {
        PlatformerTestKit.floor(helper, Blocks.SLIME_BLOCK.defaultBlockState());
        var player = PlatformerTestKit.client(helper, new BlockPos(5, 4, 5), PlatformerProfileIds.TRIAL);
        var frames = new PlatformerTestKit.Frames();
        for (int i = 0; i < 30 && player.getY() > helper.absolutePos(BlockPos.ZERO).getY() + 1.05D; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        PlatformerTestKit.step(player, frames.next());
        double startY = helper.absolutePos(BlockPos.ZERO).getY() + 1.0D;
        double peak = 0.0D;
        for (int i = 0; i < 40; i++) {
            PlatformerTestKit.step(player, frames.next());
            peak = Math.max(peak, player.getY() - startY);
        }
        helper.assertTrue(peak > 3.5D, "a landing on a bounce block should send the player about 4 blocks up, got " + peak);
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void aimingOverTheShoulderARollFacesTheWayItGoes(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 20));
        var controller = PlatformerController.of(player);
        // the view looks south, a yaw of 0, and the stick points back, north
        var frames = new PlatformerTestKit.Frames().hold(AIMING).stick(0.0F, -1.0F).hold(CROUCH);
        PlatformerTestKit.step(player, frames.press(ACTION));
        helper.assertValueEqual(move(player), PlatformerMoves.ROLL, "crouch and action on the ground");
        for (int i = 0; i < 3; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        var state = controller.state();
        helper.assertTrue(state.vz < 0.0D, "the roll goes north, vz=" + state.vz);
        helper.assertTrue(Math.abs(Mth.degreesDifference(state.facing, 180.0F)) < 1.0F, "the body faces the way it rolls, north, got " + state.facing);
        helper.assertTrue(Math.abs(Mth.degreesDifference(player.getYRot(), 180.0F)) < 1.0F, "and so does the player, got " + player.getYRot());

        // once the roll is over, the body faces the view again
        frames.letGo(CROUCH).release();
        for (int i = 0; i < 4; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        helper.assertTrue(move(player) != PlatformerMoves.ROLL, "the roll should be over");
        helper.assertTrue(Math.abs(Mth.degreesDifference(controller.state().facing, 0.0F)) < 1.0F, "the body faces the view again, got " + controller.state().facing);

        // walking backwards meanwhile keeps facing the view, as in first person
        frames.stick(0.0F, -1.0F);
        for (int i = 0; i < 4; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        helper.assertTrue(Math.abs(Mth.degreesDifference(controller.state().facing, 0.0F)) < 1.0F, "walking backwards faces the view, got " + controller.state().facing);
        helper.succeed();
    }
}
