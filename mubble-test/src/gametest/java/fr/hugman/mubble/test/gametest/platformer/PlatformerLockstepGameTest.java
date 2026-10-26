package fr.hugman.mubble.test.gametest.platformer;

import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.ACTION;
import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.CROUCH;
import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.JUMP;
import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.SPRINT;

import fr.hugman.mubble.platformer.references.PlatformerProfileIds;
import fr.hugman.mubble.test.gametest.support.TestFlight;
import fr.hugman.mubble.platformer.PlatformerController;
import fr.hugman.mubble.platformer.PlatformerPlayer;
import fr.hugman.mubble.platformer.move.PlatformerMove;
import fr.hugman.mubble.platformer.move.PlatformerMoves;
import fr.hugman.mubble.platformer.replay.PlatformerReplayer;
import fr.hugman.mubble.platformer.sim.PlatformerInputFrame;
import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import fr.hugman.mubble.super_mario.world.level.block.entity.BumpableBlockEntity;
import fr.hugman.mubble.super_mario.world.level.block.entity.SuperMarioBlockEntityTypes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

/**
 * The client and the server running the one simulation side by side, over a real connection: the
 * client predicts, the server validates, and vanilla's own move checks stay in the loop.
 * <p>
 * Each test plays one tick of the client per tick of the server, so that what the server does on its
 * own schedule (sending velocity changes, kicking floating players) happens as it would in a game.
 */
public class PlatformerLockstepGameTest {
    private static void drive(GameTestHelper helper, PlatformerTestClient client, int ticks, IntConsumer step, Runnable end) {
        client.play(ticks, step, end);
    }

    private static PlatformerMove move(PlatformerController controller) {
        return controller.state().move;
    }

    /** A lane walled all around, so that a long routine stays on its floor. */
    private static void walledLane(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var wall = Blocks.STONE.defaultBlockState();
        PlatformerTestKit.fill(helper, new BlockPos(0, 1, 0), new BlockPos(PlatformerTestKit.LANE_WIDTH - 1, 3, 0), wall);
        PlatformerTestKit.fill(helper, new BlockPos(0, 1, PlatformerTestKit.LANE_LENGTH - 1), new BlockPos(PlatformerTestKit.LANE_WIDTH - 1, 3, PlatformerTestKit.LANE_LENGTH - 1), wall);
        PlatformerTestKit.fill(helper, new BlockPos(0, 1, 0), new BlockPos(0, 3, PlatformerTestKit.LANE_LENGTH - 1), wall);
        PlatformerTestKit.fill(helper, new BlockPos(PlatformerTestKit.LANE_WIDTH - 1, 1, 0), new BlockPos(PlatformerTestKit.LANE_WIDTH - 1, 3, PlatformerTestKit.LANE_LENGTH - 1), wall);
        // something to vault, and something to climb
        PlatformerTestKit.fill(helper, new BlockPos(1, 1, 20), new BlockPos(8, 1, 21), wall);
    }

    /** A bit of everything: runs, jump chains, long jumps, rolls, skids, backflips, ground pounds, dives. */
    private static List<PlatformerInputFrame> routine() {
        var f = new PlatformerTestKit.Frames();
        var out = new ArrayList<PlatformerInputFrame>();
        repeat(out, 4, f::next);
        f.forward().hold(SPRINT);
        repeat(out, 12, f::next);
        for (int j = 0; j < 3; j++) {
            out.add(f.press(JUMP));
            repeat(out, 5, f::next);
            f.letGo(JUMP);
            repeat(out, 12, f::next);
        }
        out.add(f.press(CROUCH));
        out.add(f.press(JUMP));
        f.letGo(CROUCH | JUMP);
        repeat(out, 20, f::next);
        out.add(f.press(CROUCH));
        out.add(f.press(ACTION));
        f.letGo(CROUCH | ACTION);
        repeat(out, 10, f::next);
        // back the other way: a skid, then a side somersault out of it
        f.stick(0.0F, -1.0F);
        repeat(out, 3, f::next);
        out.add(f.press(JUMP));
        f.letGo(JUMP);
        repeat(out, 20, f::next);
        f.release();
        repeat(out, 6, f::next);
        out.add(f.press(CROUCH));
        repeat(out, 3, f::next);
        out.add(f.press(JUMP));
        f.letGo(CROUCH | JUMP);
        repeat(out, 25, f::next);
        out.add(f.press(JUMP));
        f.letGo(JUMP);
        repeat(out, 6, f::next);
        out.add(f.press(CROUCH));
        f.letGo(CROUCH);
        repeat(out, 25, f::next);
        f.stick(0.0F, -1.0F);
        out.add(f.press(JUMP));
        repeat(out, 4, f::next);
        out.add(f.press(ACTION));
        f.letGo(JUMP | ACTION);
        repeat(out, 25, f::next);
        // and into the walls, to slide down and kick off them
        f.stick(1.0F, 0.0F);
        repeat(out, 8, f::next);
        out.add(f.press(JUMP));
        repeat(out, 10, f::next);
        out.add(f.press(JUMP));
        f.letGo(JUMP);
        repeat(out, 25, f::next);
        return out;
    }

    private static void repeat(List<PlatformerInputFrame> out, int count, Supplier<PlatformerInputFrame> frame) {
        for (int i = 0; i < count; i++) {
            out.add(frame.get());
        }
    }

    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 600)
    public void aWholeRoutineStaysInLockstep(GameTestHelper helper) {
        walledLane(helper);
        var client = PlatformerTestClient.join(helper, new BlockPos(5, 1, 3), PlatformerProfileIds.TRIAL);
        var routine = routine();
        Set<PlatformerMove> played = new HashSet<>();
        Set<PlatformerMove> seenRemotely = new HashSet<>();
        drive(helper, client, routine.size(), t -> {
            client.tick(routine.get(t));
            played.add(move(client.controller()));
            var visual = ((PlatformerPlayer) client.server()).mubble$platformerVisual();
            if (visual.move() != null) {
                seenRemotely.add(visual.move());
            }
        }, () -> {
            client.assertUndisturbed("a routine played by an honest client");
            helper.assertValueEqual(client.corrections(), 0, "corrections, which an honest client never needs (" + client.events() + ")");
            helper.assertValueEqual(client.serverController().validation().accepted, routine.size(), "steps the server accepted");
            helper.assertValueEqual(client.serverController().state().fingerprint(), client.controller().state().fingerprint(), "fingerprint of the state on both sides");
            helper.assertTrue(played.size() >= 8, "the routine should go through many moves, it went through " + played);
            // what other players are told: the move of the player, as the server validated it
            helper.assertTrue(seenRemotely.containsAll(played), "every move played should be visible to other players, missing " + difference(played, seenRemotely));
        });
    }

    private static Set<PlatformerMove> difference(Set<PlatformerMove> a, Set<PlatformerMove> b) {
        var result = new HashSet<>(a);
        result.removeAll(b);
        return result;
    }

    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 400)
    public void swimmingStaysInLockstep(GameTestHelper helper) {
        PlatformerMovementGameTest.pool(helper);
        var client = PlatformerTestClient.join(helper, new BlockPos(5, 3, 3), PlatformerProfileIds.TRIAL);
        var f = new PlatformerTestKit.Frames();
        var swim = new ArrayList<PlatformerInputFrame>();
        for (int i = 0; i < 10; i++) {
            swim.add(f.next());
        }
        f.forward();
        for (int i = 0; i < 25; i++) {
            swim.add(i % 8 == 4 ? f.tap(JUMP) : f.next());
        }
        swim.add(f.press(CROUCH));
        for (int i = 0; i < 8; i++) {
            swim.add(f.next());
        }
        swim.add(f.tap(ACTION));
        f.letGo(CROUCH);
        for (int i = 0; i < 20; i++) {
            swim.add(f.next());
        }
        f.release().hold(SPRINT | JUMP);
        for (int i = 0; i < 40; i++) {
            swim.add(f.next());
        }
        f.letGo(SPRINT | JUMP);
        swim.add(f.next());
        swim.add(f.press(JUMP));
        f.letGo(JUMP);
        for (int i = 0; i < 30; i++) {
            swim.add(f.next());
        }
        Set<PlatformerMove> played = new HashSet<>();
        drive(helper, client, swim.size(), t -> {
            client.tick(swim.get(t));
            played.add(move(client.controller()));
        }, () -> {
            client.assertUndisturbed("swimming");
            helper.assertValueEqual(client.corrections(), 0, "corrections (" + client.events() + ")");
            helper.assertValueEqual(client.serverController().validation().accepted, swim.size(), "steps the server accepted");
            helper.assertTrue(played.containsAll(Set.of(PlatformerMoves.SWIM, PlatformerMoves.GROUND_POUND, PlatformerMoves.SWIM_DASH)), "the swim should go through every water move, it went through " + played);
        });
    }

    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 300)
    public void climbingStaysInLockstep(GameTestHelper helper) {
        PlatformerMovementGameTest.ladder(helper);
        var client = PlatformerTestClient.join(helper, new BlockPos(5, 1, 4), PlatformerProfileIds.TRIAL);
        var f = new PlatformerTestKit.Frames();
        var climb = new ArrayList<PlatformerInputFrame>();
        // onto the ladder and up, down a little, holding on, then up to the top and onto the wall
        f.forward();
        for (int i = 0; i < 30; i++) {
            climb.add(i == 12 ? f.tap(JUMP) : f.next());
        }
        f.release();
        for (int i = 0; i < 10; i++) {
            climb.add(f.next());
        }
        f.hold(CROUCH);
        for (int i = 0; i < 10; i++) {
            climb.add(f.next());
        }
        f.letGo(CROUCH).forward();
        for (int i = 0; i < 70; i++) {
            climb.add(f.next());
        }
        Set<PlatformerMove> played = new HashSet<>();
        drive(helper, client, climb.size(), t -> {
            client.tick(climb.get(t));
            played.add(move(client.controller()));
        }, () -> {
            client.assertUndisturbed("climbing");
            helper.assertValueEqual(client.corrections(), 0, "corrections (" + client.events() + ")");
            helper.assertValueEqual(client.serverController().validation().accepted, climb.size(), "steps the server accepted");
            helper.assertTrue(played.contains(PlatformerMoves.CLIMB), "the player never climbed, went through " + played);
            helper.assertTrue(move(client.controller()) != PlatformerMoves.CLIMB, "the player never got off the ladder");
        });
    }

    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 200)
    public void usingAnItemStaysInLockstep(GameTestHelper helper) {
        walledLane(helper);
        var client = PlatformerTestClient.join(helper, new BlockPos(5, 1, 3), PlatformerProfileIds.TRIAL);
        var frames = new PlatformerTestKit.Frames().forward().hold(SPRINT);
        drive(helper, client, 60, t -> {
            if (t == 10) {
                // the bow drawn on both sides, as the use packet of the client starts it on the server
                for (var player : List.of(client.self(), client.server())) {
                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
                    player.startUsingItem(InteractionHand.MAIN_HAND);
                }
            } else if (t == 40) {
                client.self().releaseUsingItem();
                client.server().releaseUsingItem();
            }
            client.tick(frames.next());
        }, () -> {
            client.assertUndisturbed("drawing a bow");
            helper.assertValueEqual(client.corrections(), 0, "corrections (" + client.events() + ")");
        });
    }

    /**
     * A jump cut short by a ceiling goes through: the server moves its player up into the block as
     * the client did, which is what bumps the blocks of Super Mario from below.
     */
    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 200)
    public void jumpingIntoABrickBlockBumpsIt(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var brick = new BlockPos(5, 3, 3);
        helper.setBlock(brick, SuperMarioBlocks.BRICK_BLOCK.defaultBlockState());
        var client = PlatformerTestClient.join(helper, new BlockPos(5, 1, 3), PlatformerProfileIds.TRIAL);
        var frames = new PlatformerTestKit.Frames();
        double floor = helper.absoluteVec(Vec3.atLowerCornerOf(new BlockPos(5, 1, 3))).y;
        double[] highest = {Double.NEGATIVE_INFINITY};
        boolean[] bumped = {false};
        drive(helper, client, 30, t -> {
            client.tick(t == 10 ? frames.press(JUMP) : frames.next());
            highest[0] = Math.max(highest[0], client.server().getY());
            bumped[0] |= helper.getLevel().getBlockEntity(helper.absolutePos(brick), SuperMarioBlockEntityTypes.BUMPABLE_BLOCK)
                    .map(BumpableBlockEntity::isBumping).orElse(true);
        }, () -> {
            client.assertUndisturbed("jumping into a block");
            helper.assertTrue(highest[0] > floor + 0.1D, "the server's player never rose into the block, highest at " + (highest[0] - floor));
            helper.assertTrue(bumped[0], "the brick block was never bumped");
        });
    }

    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 200)
    public void groundPoundingABrickBlockBreaksIt(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var brick = new BlockPos(5, 1, 3);
        helper.setBlock(brick, SuperMarioBlocks.BRICK_BLOCK.defaultBlockState());
        var client = PlatformerTestClient.join(helper, new BlockPos(5, 5, 3), PlatformerProfileIds.TRIAL);
        var frames = new PlatformerTestKit.Frames();
        Set<PlatformerMove> played = new HashSet<>();
        drive(helper, client, 60, t -> {
            client.tick(t == 2 ? frames.press(CROUCH) : frames.next());
            played.add(move(client.controller()));
        }, () -> {
            helper.assertTrue(played.contains(PlatformerMoves.GROUND_POUND_LAND), "the ground pound never landed, went through " + played);
            helper.assertBlockPresent(Blocks.AIR, brick);
        });
    }

    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 200)
    public void aLaunchAtThreeTimesTheRunSpeedIsNotPulledBack(GameTestHelper helper) {
        walledLane(helper);
        var client = PlatformerTestClient.join(helper, new BlockPos(5, 1, 3), PlatformerProfileIds.TRIAL);
        double launch = 3.0D * client.controller().tuning().effectiveRunSpeed();
        var frames = new PlatformerTestKit.Frames().forward().hold(SPRINT);
        double[] fastest = {0.0D};
        drive(helper, client, 60, t -> {
            var before = client.self().position();
            client.tick(frames.next());
            var moved = client.self().position().subtract(before);
            fastest[0] = Math.max(fastest[0], moved.horizontalDistance());
            if (t == 10) {
                // the server pushes the player, the way a launcher block or an explosion would: during
                // its own tick, after the packets of the player were handled
                client.serverCommand("mubble platformer impulse @self 0 0.5 " + launch);
            }
        }, () -> {
            helper.assertTrue(client.motions() > 0, "the server never sent the push");
            helper.assertTrue(fastest[0] >= launch - 0.05D, "the push should have launched the player at " + launch + " b/t, the fastest step was " + fastest[0]);
            client.assertUndisturbed("a launch at three times the run speed");
        });
    }

    /** Up an inside corner of steps from right against it, one vault after the other, with nothing sent back. */
    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 200)
    public void climbingACornerOfStepsIsNotPulledBack(GameTestHelper helper) {
        PlatformerMovementGameTest.corner(helper);
        var client = PlatformerTestClient.join(helper, new BlockPos(5, 1, 5), PlatformerProfileIds.TRIAL);
        var frames = new PlatformerTestKit.Frames();
        double top = helper.absoluteVec(Vec3.atLowerCornerOf(new BlockPos(6, 3, 6))).y;
        Set<PlatformerMove> played = new HashSet<>();
        drive(helper, client, 40, t -> {
            client.tick(t < 4 ? frames.next() : frames.stick(0.7071F, 0.7071F).next());
            played.add(move(client.controller()));
        }, () -> {
            client.assertUndisturbed("climbing a corner of steps");
            helper.assertTrue(played.contains(PlatformerMoves.VAULT), "the corner was never vaulted, went through " + played);
            helper.assertTrue(Math.abs(client.server().getY() - top) < 1.0E-2D, "the climb should end on top of the corner, the server has the player at " + client.server().getY());
        });
    }

    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 300)
    public void aFiveSecondLedgeHangIsNeitherPulledBackNorKicked(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        PlatformerTestKit.fill(helper, new BlockPos(0, 1, 7), new BlockPos(9, 4, 9), Blocks.STONE.defaultBlockState());
        var client = PlatformerTestClient.join(helper, new BlockPos(5, 5, 6), PlatformerProfileIds.TRIAL);
        TestFlight.disallow(client.server());
        var frames = new PlatformerTestKit.Frames();
        int[] hanging = {0};
        int ticks = 30 + 120 + 20;
        drive(helper, client, ticks, t -> {
            boolean hangsLongEnough = hanging[0] >= 120;
            client.tick(hangsLongEnough && move(client.controller()) == PlatformerMoves.LEDGE_GRAB ? frames.press(JUMP) : frames.next());
            if (move(client.controller()) == PlatformerMoves.LEDGE_GRAB) {
                hanging[0]++;
            }
        }, () -> {
            helper.assertTrue(hanging[0] >= 100, "the player should have hung for 5 seconds, it hung for " + hanging[0] + " ticks");
            client.assertUndisturbed("a five second ledge hang, then a climb");
            double top = helper.absolutePos(BlockPos.ZERO).getY() + 5.0D;
            helper.assertTrue(Math.abs(client.server().getY() - top) < 1.0E-3D, "the climb should end on the ledge, the server has the player at " + client.server().getY());
        });
    }

    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 200)
    public void aClientFloatingWithoutTheLayerIsKicked(GameTestHelper helper) {
        // the control of the hang above: the same server setting does kick a player that floats
        PlatformerTestKit.floor(helper);
        var client = PlatformerTestClient.join(helper, new BlockPos(5, 6, 20), null);
        TestFlight.disallow(client.server());
        var hover = client.self().position();
        // the first report after the setting changed comes with the reminder a second later, then 4 seconds of floating
        drive(helper, client, 130, t -> client.tickVanilla(hover, false), () ->
                helper.assertTrue(client.kicked(), "a vanilla player floating for 5 seconds should be kicked, which makes the hang test meaningful"));
    }

    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 100)
    public void aClientLyingAboutItsStepIsPulledBack(GameTestHelper helper) {
        walledLane(helper);
        var client = PlatformerTestClient.join(helper, new BlockPos(5, 1, 3), PlatformerProfileIds.TRIAL);
        var frames = new PlatformerTestKit.Frames().forward();
        Vec3[] beforeLie = new Vec3[1];
        drive(helper, client, 40, t -> {
            if (t == 10) {
                beforeLie[0] = client.server().position();
                client.tickTampered(frames.next(), 1.5D);
                helper.assertTrue(client.server().position().distanceTo(beforeLie[0]) < 1.0E-6D, "the server should not take the lie, it moved the player to " + client.server().position());
            } else {
                client.tick(frames.next());
            }
        }, () -> {
            client.receive();
            helper.assertValueEqual(client.rejections(), 1, "rejected steps (" + client.events() + ")");
            helper.assertFalse(client.kicked(), "a rejected step sends the player back, it does not kick them");
            helper.assertTrue(client.server().position().distanceTo(client.self().position()) < 1.0E-6D,
                    "both sides should agree again, the server has the player at " + client.server().position() + ", the client at " + client.self().position());
        });
    }

    /** The step a player on the ground may take up a block is not a lift a player in the air can claim every tick. */
    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 100)
    public void anAirborneClientLyingUpwardsIsPulledBack(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var client = PlatformerTestClient.join(helper, new BlockPos(5, 8, 20), PlatformerProfileIds.TRIAL);
        var frames = new PlatformerTestKit.Frames();
        double startY = client.server().getY();
        double[] highest = {startY};
        drive(helper, client, 40, t -> {
            client.tickTampered(frames.next(), new Vec3(0.0D, client.controller().tuning().stepHeight(), 0.0D));
            highest[0] = Math.max(highest[0], client.server().getY());
        }, () -> {
            client.receive();
            helper.assertTrue(client.rejections() > 0, "the server should not take the lift, it never rejected a step (" + client.events() + ")");
            helper.assertTrue(highest[0] <= startY + 1.0E-6D, "the server should never have the player rise, it had them " + (highest[0] - startY) + " above the start");
            helper.assertFalse(client.kicked(), "a rejected step sends the player back, it does not kick them");
        });
    }

    /** Pushes small enough to come from the world are taken in, but not one every tick for ever. */
    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 100)
    public void aClientStackingSmallPushesIsPulledBack(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var client = PlatformerTestClient.join(helper, new BlockPos(5, 1, 20), PlatformerProfileIds.TRIAL);
        var frames = new PlatformerTestKit.Frames();
        double push = 0.9D * client.controller().profile().validation().freeImpulse();
        double startY = client.server().getY();
        double[] highest = {startY};
        drive(helper, client, 60, t -> {
            // what a tampered client does: a push of its own making, under the free impulse, every tick
            client.self().push(0.0D, push, 0.0D);
            client.tick(frames.next());
            highest[0] = Math.max(highest[0], client.server().getY());
        }, () -> {
            client.receive();
            helper.assertTrue(client.rejections() > 0, "the server should not take a push every tick, it never rejected a step (" + client.events() + ")");
            helper.assertTrue(highest[0] < startY + 1.5D, "the pushes should not have lifted the player far, the server had them " + (highest[0] - startY) + " above the start");
            helper.assertFalse(client.kicked(), "a rejected step sends the player back, it does not kick them");
        });
    }

    /** A column of bubbles up through water, out of soul sand, walled in. */
    private static void bubbleColumn(GameTestHelper helper, BlockPos bottom, int height) {
        PlatformerTestKit.floor(helper);
        var stone = Blocks.STONE.defaultBlockState();
        PlatformerTestKit.fill(helper, bottom.offset(-1, 0, -1), bottom.offset(1, height, 1), stone);
        helper.setBlock(bottom.below(), Blocks.SOUL_SAND);
        PlatformerTestKit.fill(helper, bottom, bottom.above(height - 1), Blocks.BUBBLE_COLUMN.defaultBlockState().setValue(BubbleColumnBlock.DRAG_DOWN, false));
        helper.setBlock(bottom.above(height), Blocks.AIR);
    }

    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 200)
    public void ridingABubbleColumnIsNotPulledBack(GameTestHelper helper) {
        var bottom = new BlockPos(5, 1, 20);
        bubbleColumn(helper, bottom, 8);
        var client = PlatformerTestClient.join(helper, bottom, PlatformerProfileIds.TRIAL);
        var frames = new PlatformerTestKit.Frames();
        double startY = client.self().getY();
        int[] impulses = {0};
        drive(helper, client, 80, t -> {
            client.tick(frames.next());
            if (client.controller().history().getLast().impulse().isPresent()) {
                impulses[0]++;
            }
        }, () -> {
            client.assertUndisturbed("riding a bubble column");
            helper.assertTrue(impulses[0] > 20, "the bubbles should have pushed the player every tick, they did " + impulses[0] + " times");
            helper.assertTrue(client.self().getY() > startY + 3.0D, "the bubbles should have carried the player up, they rose " + (client.self().getY() - startY));
        });
    }

    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 200)
    public void slidingDownHoneyIsNotPulledBack(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        PlatformerTestKit.fill(helper, new BlockPos(4, 1, 21), new BlockPos(6, 10, 21), Blocks.HONEY_BLOCK.defaultBlockState());
        // against the honey, where vanilla has it slow the fall down: the hitbox inside its cell, past the shape of the block
        var client = PlatformerTestClient.join(helper, Vec3.atBottomCenterOf(new BlockPos(5, 10, 20)).add(0.0D, 0.0D, 0.26D), PlatformerProfileIds.TRIAL);
        var frames = new PlatformerTestKit.Frames();
        int[] impulses = {0};
        drive(helper, client, 60, t -> {
            client.tick(frames.next());
            if (client.controller().history().getLast().impulse().isPresent()) {
                impulses[0]++;
            }
        }, () -> {
            client.assertUndisturbed("sliding down honey");
            helper.assertTrue(impulses[0] > 5, "the honey should have slowed the slide down, it did " + impulses[0] + " times");
        });
    }

    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 200)
    public void rollingUpAHillStaysInLockstep(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        for (int step = 0; step < 4; step++) {
            PlatformerTestKit.fill(helper, new BlockPos(0, 1, 8 + step * 3), new BlockPos(9, 1 + step, 47), Blocks.STONE.defaultBlockState());
        }
        var client = PlatformerTestClient.join(helper, new BlockPos(5, 1, 2), PlatformerProfileIds.TRIAL);
        var frames = new PlatformerTestKit.Frames().forward().hold(SPRINT);
        double startY = client.self().getY();
        drive(helper, client, 50, t -> client.tick(switch (t) {
            case 6 -> frames.press(CROUCH);
            case 7, 13, 19, 25 -> frames.press(ACTION);
            default -> frames.next();
        }), () -> {
            client.assertUndisturbed("rolling up a hill of full blocks");
            helper.assertValueEqual(client.corrections(), 0, "corrections (" + client.events() + ")");
            helper.assertTrue(client.self().getY() >= startY + 4.0D - 1.0E-6D, "the roll should have climbed the hill, it rose " + (client.self().getY() - startY));
        });
    }

    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 200)
    public void theLayerKeepsWorkingInAnotherDimension(GameTestHelper helper) {
        walledLane(helper);
        var nether = helper.getLevel().getServer().getLevel(Level.NETHER);
        helper.assertTrue(nether != null, "the test server has no nether");
        // a floor above the roof of the nether, where nothing else is
        var origin = helper.absolutePos(BlockPos.ZERO);
        var floor = new BlockPos(origin.getX() / 8, 200, origin.getZ() / 8);
        for (var pos : BlockPos.betweenClosed(floor.offset(-3, 0, -3), floor.offset(3, 0, 40))) {
            nether.setBlockAndUpdate(pos, Blocks.STONE.defaultBlockState());
        }
        var client = PlatformerTestClient.join(helper, new BlockPos(5, 1, 3), PlatformerProfileIds.TRIAL);
        var frames = new PlatformerTestKit.Frames().forward().hold(SPRINT);
        double[] netherStart = {Double.NaN};
        drive(helper, client, 80, t -> {
            if (t == 20) {
                // what a portal does
                client.server().teleport(new TeleportTransition(nether, Vec3.atBottomCenterOf(floor.above()), Vec3.ZERO, 0.0F, 0.0F, TeleportTransition.DO_NOTHING));
            }
            client.tick(t % 20 == 15 ? frames.tap(JUMP) : frames.next());
            if (Double.isNaN(netherStart[0]) && client.self().level() == nether) {
                netherStart[0] = client.self().getZ();
            }
        }, () -> {
            client.receive();
            helper.assertTrue(client.self().level() == nether && client.server().level() == nether, "both sides should be in the nether");
            helper.assertValueEqual(client.rejections(), 0, "rejected steps (" + client.events() + ")");
            helper.assertFalse(client.kicked(), "kicked (" + client.events() + ")");
            helper.assertTrue(client.self().getZ() > netherStart[0] + 10.0D, "the player should run on in the nether, it went from " + netherStart[0] + " to " + client.self().getZ());
            helper.assertTrue(client.server().position().distanceTo(client.self().position()) < 1.0E-6D,
                    "the server has the player at " + client.server().position() + ", the client at " + client.self().position());
        });
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void replayingARecordingAsTheServerGivesTheSameTrajectory(GameTestHelper helper) {
        walledLane(helper);
        var player = PlatformerTestKit.client(helper, new BlockPos(5, 1, 3), PlatformerProfileIds.TRIAL);
        var controller = PlatformerController.of(player);
        controller.resetFromEntity();
        var start = player.position();
        var initial = controller.state().copy();
        var frames = routine();
        var client = PlatformerReplayer.playAsClient(player, controller.state(), frames, controller::tuning);
        var server = PlatformerReplayer.playAsServer(player, initial, frames, client, start, controller::tuning);
        var divergence = PlatformerReplayer.firstDivergence(client, server);
        helper.assertTrue(divergence.isEmpty(), "the replay as the server diverged at step " + divergence.orElse(-1));
        helper.succeed();
    }
}
