package fr.hugman.mubble.test.gametest.arcade;

import static fr.hugman.mubble.arcade.sim.ArcadeInputFrame.ACTION;
import static fr.hugman.mubble.arcade.sim.ArcadeInputFrame.CROUCH;
import static fr.hugman.mubble.arcade.sim.ArcadeInputFrame.JUMP;
import static fr.hugman.mubble.arcade.sim.ArcadeInputFrame.SPRINT;

import fr.hugman.mubble.arcade.references.ArcadeProfileIds;
import fr.hugman.mubble.test.gametest.support.TestFlight;
import fr.hugman.mubble.arcade.ArcadeController;
import fr.hugman.mubble.arcade.ArcadePlayer;
import fr.hugman.mubble.arcade.move.ArcadeMove;
import fr.hugman.mubble.arcade.move.ArcadeMoves;
import fr.hugman.mubble.arcade.replay.ArcadeReplayer;
import fr.hugman.mubble.arcade.sim.ArcadeInputFrame;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

/**
 * The client and the server running the one simulation side by side, over a real connection: the
 * client predicts, the server validates, and vanilla's own move checks stay in the loop.
 * <p>
 * Each test plays one tick of the client per tick of the server, so that what the server does on its
 * own schedule (sending velocity changes, kicking floating players) happens as it would in a game.
 */
public class ArcadeLockstepGameTest {
    private static void drive(GameTestHelper helper, ArcadeTestClient client, int ticks, IntConsumer step, Runnable end) {
        client.play(ticks, step, end);
    }

    private static ArcadeMove move(ArcadeController controller) {
        return controller.state().move;
    }

    /** A lane walled all around, so that a long routine stays on its floor. */
    private static void walledLane(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        var wall = Blocks.STONE.defaultBlockState();
        ArcadeTestKit.fill(helper, new BlockPos(0, 1, 0), new BlockPos(ArcadeTestKit.LANE_WIDTH - 1, 3, 0), wall);
        ArcadeTestKit.fill(helper, new BlockPos(0, 1, ArcadeTestKit.LANE_LENGTH - 1), new BlockPos(ArcadeTestKit.LANE_WIDTH - 1, 3, ArcadeTestKit.LANE_LENGTH - 1), wall);
        ArcadeTestKit.fill(helper, new BlockPos(0, 1, 0), new BlockPos(0, 3, ArcadeTestKit.LANE_LENGTH - 1), wall);
        ArcadeTestKit.fill(helper, new BlockPos(ArcadeTestKit.LANE_WIDTH - 1, 1, 0), new BlockPos(ArcadeTestKit.LANE_WIDTH - 1, 3, ArcadeTestKit.LANE_LENGTH - 1), wall);
        // something to vault, and something to climb
        ArcadeTestKit.fill(helper, new BlockPos(1, 1, 20), new BlockPos(8, 1, 21), wall);
    }

    /** A bit of everything: runs, jump chains, long jumps, rolls, skids, backflips, ground pounds, dives. */
    private static List<ArcadeInputFrame> routine() {
        var f = new ArcadeTestKit.Frames();
        var out = new ArrayList<ArcadeInputFrame>();
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

    private static void repeat(List<ArcadeInputFrame> out, int count, Supplier<ArcadeInputFrame> frame) {
        for (int i = 0; i < count; i++) {
            out.add(frame.get());
        }
    }

    @GameTest(structure = ArcadeTestKit.LANE, maxTicks = 600)
    public void aWholeRoutineStaysInLockstep(GameTestHelper helper) {
        walledLane(helper);
        var client = ArcadeTestClient.join(helper, new BlockPos(5, 1, 3), ArcadeProfileIds.TRIAL);
        var routine = routine();
        Set<ArcadeMove> played = new HashSet<>();
        Set<ArcadeMove> seenRemotely = new HashSet<>();
        drive(helper, client, routine.size(), t -> {
            client.tick(routine.get(t));
            played.add(move(client.controller()));
            var visual = ((ArcadePlayer) client.server()).mubble$arcadeVisual();
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

    private static Set<ArcadeMove> difference(Set<ArcadeMove> a, Set<ArcadeMove> b) {
        var result = new HashSet<>(a);
        result.removeAll(b);
        return result;
    }

    @GameTest(structure = ArcadeTestKit.LANE, maxTicks = 400)
    public void swimmingStaysInLockstep(GameTestHelper helper) {
        ArcadeMovementGameTest.pool(helper);
        var client = ArcadeTestClient.join(helper, new BlockPos(5, 3, 3), ArcadeProfileIds.TRIAL);
        var f = new ArcadeTestKit.Frames();
        var swim = new ArrayList<ArcadeInputFrame>();
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
        Set<ArcadeMove> played = new HashSet<>();
        drive(helper, client, swim.size(), t -> {
            client.tick(swim.get(t));
            played.add(move(client.controller()));
        }, () -> {
            client.assertUndisturbed("swimming");
            helper.assertValueEqual(client.corrections(), 0, "corrections (" + client.events() + ")");
            helper.assertValueEqual(client.serverController().validation().accepted, swim.size(), "steps the server accepted");
            helper.assertTrue(played.containsAll(Set.of(ArcadeMoves.SWIM, ArcadeMoves.GROUND_POUND, ArcadeMoves.SWIM_DASH)), "the swim should go through every water move, it went through " + played);
        });
    }

    @GameTest(structure = ArcadeTestKit.LANE, maxTicks = 200)
    public void aLaunchAtThreeTimesTheRunSpeedIsNotPulledBack(GameTestHelper helper) {
        walledLane(helper);
        var client = ArcadeTestClient.join(helper, new BlockPos(5, 1, 3), ArcadeProfileIds.TRIAL);
        double launch = 3.0D * client.controller().tuning().effectiveRunSpeed();
        var frames = new ArcadeTestKit.Frames().forward().hold(SPRINT);
        double[] fastest = {0.0D};
        drive(helper, client, 60, t -> {
            var before = client.self().position();
            client.tick(frames.next());
            var moved = client.self().position().subtract(before);
            fastest[0] = Math.max(fastest[0], moved.horizontalDistance());
            if (t == 10) {
                // the server pushes the player, the way a launcher block or an explosion would: during
                // its own tick, after the packets of the player were handled
                client.serverCommand("mubble arcade impulse @self 0 0.5 " + launch);
            }
        }, () -> {
            helper.assertTrue(client.motions() > 0, "the server never sent the push");
            helper.assertTrue(fastest[0] >= launch - 0.05D, "the push should have launched the player at " + launch + " b/t, the fastest step was " + fastest[0]);
            client.assertUndisturbed("a launch at three times the run speed");
        });
    }

    @GameTest(structure = ArcadeTestKit.LANE, maxTicks = 300)
    public void aFiveSecondLedgeHangIsNeitherPulledBackNorKicked(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        ArcadeTestKit.fill(helper, new BlockPos(0, 1, 7), new BlockPos(9, 4, 9), Blocks.STONE.defaultBlockState());
        var client = ArcadeTestClient.join(helper, new BlockPos(5, 5, 6), ArcadeProfileIds.TRIAL);
        TestFlight.disallow(client.server());
        var frames = new ArcadeTestKit.Frames();
        int[] hanging = {0};
        int ticks = 30 + 120 + 20;
        drive(helper, client, ticks, t -> {
            boolean hangsLongEnough = hanging[0] >= 120;
            client.tick(hangsLongEnough && move(client.controller()) == ArcadeMoves.LEDGE_GRAB ? frames.press(JUMP) : frames.next());
            if (move(client.controller()) == ArcadeMoves.LEDGE_GRAB) {
                hanging[0]++;
            }
        }, () -> {
            helper.assertTrue(hanging[0] >= 100, "the player should have hung for 5 seconds, it hung for " + hanging[0] + " ticks");
            client.assertUndisturbed("a five second ledge hang, then a climb");
            double top = helper.absolutePos(BlockPos.ZERO).getY() + 5.0D;
            helper.assertTrue(Math.abs(client.server().getY() - top) < 1.0E-3D, "the climb should end on the ledge, the server has the player at " + client.server().getY());
        });
    }

    @GameTest(structure = ArcadeTestKit.LANE, maxTicks = 200)
    public void aClientFloatingWithoutTheLayerIsKicked(GameTestHelper helper) {
        // the control of the hang above: the same server setting does kick a player that floats
        ArcadeTestKit.floor(helper);
        var client = ArcadeTestClient.join(helper, new BlockPos(5, 6, 20), null);
        TestFlight.disallow(client.server());
        var hover = client.self().position();
        // the first report after the setting changed comes with the reminder a second later, then 4 seconds of floating
        drive(helper, client, 130, t -> client.tickVanilla(hover, false), () ->
                helper.assertTrue(client.kicked(), "a vanilla player floating for 5 seconds should be kicked, which makes the hang test meaningful"));
    }

    @GameTest(structure = ArcadeTestKit.LANE, maxTicks = 100)
    public void aClientLyingAboutItsStepIsPulledBack(GameTestHelper helper) {
        walledLane(helper);
        var client = ArcadeTestClient.join(helper, new BlockPos(5, 1, 3), ArcadeProfileIds.TRIAL);
        var frames = new ArcadeTestKit.Frames().forward();
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

    @GameTest(structure = ArcadeTestKit.LANE, maxTicks = 200)
    public void rollingUpAHillStaysInLockstep(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        for (int step = 0; step < 4; step++) {
            ArcadeTestKit.fill(helper, new BlockPos(0, 1, 8 + step * 3), new BlockPos(9, 1 + step, 47), Blocks.STONE.defaultBlockState());
        }
        var client = ArcadeTestClient.join(helper, new BlockPos(5, 1, 2), ArcadeProfileIds.TRIAL);
        var frames = new ArcadeTestKit.Frames().forward().hold(SPRINT);
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

    @GameTest(structure = ArcadeTestKit.LANE, maxTicks = 200)
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
        var client = ArcadeTestClient.join(helper, new BlockPos(5, 1, 3), ArcadeProfileIds.TRIAL);
        var frames = new ArcadeTestKit.Frames().forward().hold(SPRINT);
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

    @GameTest(structure = ArcadeTestKit.LANE)
    public void replayingARecordingAsTheServerGivesTheSameTrajectory(GameTestHelper helper) {
        walledLane(helper);
        var player = ArcadeTestKit.client(helper, new BlockPos(5, 1, 3), ArcadeProfileIds.TRIAL);
        var controller = ArcadeController.of(player);
        controller.resetFromEntity();
        var start = player.position();
        var initial = controller.state().copy();
        var frames = routine();
        var client = ArcadeReplayer.playAsClient(player, controller.state(), frames, controller::tuning);
        var server = ArcadeReplayer.playAsServer(player, initial, frames, client, start, controller::tuning);
        var divergence = ArcadeReplayer.firstDivergence(client, server);
        helper.assertTrue(divergence.isEmpty(), "the replay as the server diverged at step " + divergence.orElse(-1));
        helper.succeed();
    }
}
