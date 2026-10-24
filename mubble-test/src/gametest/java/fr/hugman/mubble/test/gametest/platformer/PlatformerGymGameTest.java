package fr.hugman.mubble.test.gametest.platformer;

import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.ACTION;
import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.CROUCH;
import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.JUMP;
import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.SPIN;
import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.SPRINT;

import fr.hugman.mubble.platformer.references.PlatformerProfileIds;
import fr.hugman.mubble.platformer.gym.PlatformerGym;
import fr.hugman.mubble.platformer.move.PlatformerMove;
import fr.hugman.mubble.platformer.sim.PlatformerInputFrame;
import java.util.HashSet;
import java.util.Set;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;

/**
 * Every lane of the movement gym, as the gym command builds it, run flat out by a client over a real
 * connection: whatever the moves do against those blocks, the server has to agree with every step.
 */
public class PlatformerGymGameTest {
    private static final String STRUCTURE = "mubble-gametest:platformer_gym_lane";
    private static final int TICKS = 160;

    /**
     * Runs east along the lane at full speed, mixing in everything the lane is about: jumps every so
     * often, rolls, long jumps, ground pounds, spins, and turning back now and then.
     */
    private static PlatformerInputFrame flatOut(PlatformerTestKit.Frames frames, int t) {
        frames.stick(t % 50 < 42 ? 1.0F : -1.0F, 0.0F).hold(SPRINT);
        return switch (t % 25) {
            case 3 -> frames.press(JUMP);
            case 6 -> {
                frames.letGo(JUMP);
                yield frames.tap(t % 75 == 6 ? CROUCH : t % 75 == 31 ? ACTION : 0);
            }
            case 12 -> frames.press(CROUCH);
            case 13 -> frames.press(t % 50 == 13 ? ACTION : JUMP);
            case 15 -> {
                frames.letGo(CROUCH | JUMP | ACTION);
                yield frames.next();
            }
            case 20 -> frames.tap(SPIN);
            default -> frames.next();
        };
    }

    private static void runLane(GameTestHelper helper, PlatformerGym.Lane lane, int startHeight) {
        var origin = helper.absolutePos(new BlockPos(3, 1, 2)).offset(0, 0, -lane.ordinal() * PlatformerGym.LANE_SPACING);
        PlatformerGym.build(helper.getLevel(), origin, lane);
        var client = PlatformerTestClient.join(helper, new BlockPos(4, 1 + startHeight, 3), PlatformerProfileIds.TRIAL);
        double startX = client.self().getX();
        var frames = new PlatformerTestKit.Frames();
        Set<PlatformerMove> played = new HashSet<>();
        double[] furthest = {0.0D};
        long start = helper.getTick();
        for (int i = 0; i < TICKS; i++) {
            int t = i;
            helper.runAtTickTime(start + 1 + t, () -> {
                try {
                    client.tick(flatOut(frames, t));
                    played.add(client.controller().state().move);
                    furthest[0] = Math.max(furthest[0], client.self().getX() - startX);
                } catch (RuntimeException e) {
                    client.leave();
                    throw e;
                }
            });
        }
        helper.runAtTickTime(start + 1 + TICKS, () -> {
            try {
                client.assertUndisturbed("the " + lane.name().toLowerCase() + " lane, flat out");
                helper.assertValueEqual(client.corrections(), 0, "corrections on the " + lane.name().toLowerCase() + " lane (" + client.events() + ")");
                helper.assertTrue(furthest[0] > 3.0D, "the run should have gone some way down the lane, it went " + furthest[0]);
                helper.assertTrue(played.size() >= 5, "the run should have gone through a few moves, it went through " + played);
            } finally {
                client.leave();
            }
            helper.succeed();
        });
    }

    @GameTest(structure = STRUCTURE, maxTicks = 300)
    public void gaps(GameTestHelper helper) {
        runLane(helper, PlatformerGym.Lane.GAPS, 0);
    }

    @GameTest(structure = STRUCTURE, maxTicks = 300)
    public void walls(GameTestHelper helper) {
        runLane(helper, PlatformerGym.Lane.WALLS, 0);
    }

    @GameTest(structure = STRUCTURE, maxTicks = 300)
    public void ledges(GameTestHelper helper) {
        runLane(helper, PlatformerGym.Lane.LEDGES, 0);
    }

    @GameTest(structure = STRUCTURE, maxTicks = 300)
    public void stairs(GameTestHelper helper) {
        runLane(helper, PlatformerGym.Lane.STAIRS, 10);
    }

    @GameTest(structure = STRUCTURE, maxTicks = 300)
    public void wallJumpShaft(GameTestHelper helper) {
        runLane(helper, PlatformerGym.Lane.WALL_JUMP, 0);
    }

    @GameTest(structure = STRUCTURE, maxTicks = 300)
    public void noWallJumpShaft(GameTestHelper helper) {
        runLane(helper, PlatformerGym.Lane.NO_WALL_JUMP, 0);
    }

    @GameTest(structure = STRUCTURE, maxTicks = 300)
    public void bounce(GameTestHelper helper) {
        runLane(helper, PlatformerGym.Lane.BOUNCE, 0);
    }

    @GameTest(structure = STRUCTURE, maxTicks = 300)
    public void momentum(GameTestHelper helper) {
        runLane(helper, PlatformerGym.Lane.MOMENTUM, 0);
    }

    @GameTest(structure = STRUCTURE, maxTicks = 300)
    public void vault(GameTestHelper helper) {
        runLane(helper, PlatformerGym.Lane.VAULT, 0);
    }
}
