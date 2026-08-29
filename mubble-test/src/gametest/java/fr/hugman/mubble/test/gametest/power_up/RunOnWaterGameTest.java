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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Running on water, in two halves.
 * <p>
 * The first one is the run itself: a sprint only carries a player over the water if it started on
 * the ground, and it is over the moment they slow to a walk, hit a wall or go under. That is a
 * state the player tick follows from one tick to the next, so those tests play it out with a
 * moving player.
 * <p>
 * The second one is what the water does about it: the collision shape of a block turns solid for a
 * player on such a run, and nothing else. Those tests ask the block state directly, running player
 * in hand, because the answer is the whole of the mixin.
 */
public class RunOnWaterGameTest {
    /** The pond: one water block, with the shore to its north to start a sprint from. */
    private static final BlockPos WATER = new BlockPos(4, Arena.FLOOR_Y + 1, 3);
    private static final BlockPos SHORE = WATER.north();

    /** Enough ticks for a mock player to settle on whatever is under them. */
    private static final int SETTLE_TICKS = 5;
    /** Enough ticks for a jump to bring a player back down on the surface. */
    private static final int FALL_TICKS = 10;
    /** Enough ticks to cross the pond of {@link #hittingAWallEndsTheRun}, wall included. */
    private static final int CROSSING_TICKS = 40;
    private static final double RUNNING_SPEED = 0.25D;

    // -- the run ------------------------------------------------------------------------------

    @GameTest(maxTicks = 100)
    public void aSprintStartedOnTheGroundCarriesOverWater(GameTestHelper helper) {
        var player = onTheShore(helper, PowerUpFixtures.RUNS_ON_WATER);

        helper.startSequence()
                .thenExecuteFor(SETTLE_TICKS, () -> sprintTick(player))
                .thenExecute(() -> {
                    helper.assertTrue(player.onGround(), "the player never landed on the shore, the test proves nothing");
                    helper.assertTrue(player.isRunningOnWater(), "a sprint started on the ground should carry over the water");
                })
                .thenSucceed();
    }

    @GameTest(maxTicks = 100)
    public void walkingIsNotEnough(GameTestHelper helper) {
        var player = onTheShore(helper, PowerUpFixtures.RUNS_ON_WATER);

        helper.startSequence()
                .thenExecuteFor(SETTLE_TICKS, () -> TestPlayers.tick(player))
                .thenExecute(() -> helper.assertFalse(player.isRunningOnWater(), "the power-up should only carry a player while sprinting"))
                .thenSucceed();
    }

    @GameTest(maxTicks = 100)
    public void lettingGoOfTheSprintEndsTheRun(GameTestHelper helper) {
        var player = onTheShore(helper, PowerUpFixtures.RUNS_ON_WATER);

        helper.startSequence()
                .thenExecuteFor(SETTLE_TICKS, () -> sprintTick(player))
                .thenExecute(() -> helper.assertTrue(player.isRunningOnWater(), "the run never started, the test proves nothing"))
                .thenExecute(() -> player.setSprinting(false))
                .thenExecute(() -> TestPlayers.tick(player))
                .thenExecute(() -> helper.assertFalse(player.isRunningOnWater(), "the run should end with the sprint"))
                .thenSucceed();
    }

    @GameTest(maxTicks = 100)
    public void anUntaggedPowerUpNeverStartsARun(GameTestHelper helper) {
        var player = onTheShore(helper, PowerUpFixtures.EMPTY);

        helper.startSequence()
                .thenExecuteFor(SETTLE_TICKS, () -> sprintTick(player))
                .thenExecute(() -> helper.assertFalse(player.isRunningOnWater(), "only power-ups tagged can_run_on_water should carry a player"))
                .thenSucceed();
    }

    @GameTest(maxTicks = 100)
    public void aPlayerWithoutAPowerUpNeverStartsARun(GameTestHelper helper) {
        var player = onTheShore(helper);

        helper.startSequence()
                .thenExecuteFor(SETTLE_TICKS, () -> sprintTick(player))
                .thenExecute(() -> helper.assertFalse(player.isRunningOnWater(), "water should stay water for everyone else"))
                .thenSucceed();
    }

    @GameTest(maxTicks = 100)
    public void sneakingEndsTheRun(GameTestHelper helper) {
        var player = onTheShore(helper, PowerUpFixtures.RUNS_ON_WATER);

        helper.startSequence()
                .thenExecuteFor(SETTLE_TICKS, () -> sprintTick(player))
                .thenExecute(() -> helper.assertTrue(player.isRunningOnWater(), "the run never started, the test proves nothing"))
                // the game lets a player hold both keys down, so the sprint is still on here
                .thenExecute(() -> player.setShiftKeyDown(true))
                .thenExecute(() -> sprintTick(player))
                .thenExecute(() -> {
                    helper.assertTrue(player.isSprinting(), "the sprint was let go of, so the test proves nothing about sneaking");
                    helper.assertFalse(player.isRunningOnWater(), "sneaking should end the run");
                })
                .thenSucceed();
    }

    @GameTest(maxTicks = 100)
    public void sneakingNeverStartsARun(GameTestHelper helper) {
        var player = onTheShore(helper, PowerUpFixtures.RUNS_ON_WATER);
        player.setShiftKeyDown(true);

        helper.startSequence()
                .thenExecuteFor(SETTLE_TICKS, () -> sprintTick(player))
                .thenExecute(() -> helper.assertFalse(player.isRunningOnWater(), "a sneaking player should never be carried by the water"))
                .thenSucceed();
    }

    @GameTest(maxTicks = 100)
    public void usingAnItemEndsTheRun(GameTestHelper helper) {
        var player = onTheShore(helper, PowerUpFixtures.RUNS_ON_WATER);

        helper.startSequence()
                .thenExecuteFor(SETTLE_TICKS, () -> sprintTick(player))
                .thenExecute(() -> helper.assertTrue(player.isRunningOnWater(), "the run never started, the test proves nothing"))
                .thenExecute(() -> startUsingAnItem(player))
                .thenExecute(() -> sprintTick(player))
                .thenExecute(() -> {
                    helper.assertTrue(player.isUsingItem(), "the item never went up, the test proves nothing");
                    helper.assertFalse(player.isRunningOnWater(), "using an item should end the run");
                })
                .thenSucceed();
    }

    /** Neither of the two slows a player down in mid-air, so neither is allowed to end a run there. */
    @GameTest(maxTicks = 100)
    public void slowingDownInMidAirDoesNotEndTheRun(GameTestHelper helper) {
        var player = onTheShore(helper, PowerUpFixtures.RUNS_ON_WATER);

        helper.startSequence()
                .thenExecuteFor(SETTLE_TICKS, () -> sprintTick(player))
                .thenExecute(() -> helper.assertTrue(player.isRunningOnWater(), "the run never started, the test proves nothing"))
                // in the air over the pond, on the way down from a jump. A teleport does not clear
                // the ground flag on its own, so the fall has to be under way before anything else
                .thenExecute(() -> teleport(helper, player, WATER.above(3)))
                .thenExecuteFor(2, () -> sprintTick(player))
                .thenExecute(() -> {
                    helper.assertFalse(player.onGround(), "the player never left the ground, the test proves nothing");
                    player.setShiftKeyDown(true);
                    startUsingAnItem(player);
                })
                .thenExecuteFor(2, () -> sprintTick(player))
                .thenExecute(() -> {
                    helper.assertFalse(player.onGround(), "the player landed already, the test proves nothing");
                    helper.assertTrue(player.isRunningOnWater(), "sneaking or using an item in mid-air should not end the run");
                })
                .thenSucceed();
    }

    /** Sprinting out of the water is the case the run is not meant to survive. */
    @GameTest(maxTicks = 100)
    public void aSprintStartedInTheWaterDoesNotCarry(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(WATER, Blocks.WATER);
        helper.setBlock(WATER.above(), Blocks.WATER);

        var player = TestPlayers.at(helper, WATER);
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.RUNS_ON_WATER));

        helper.startSequence()
                .thenExecuteFor(SETTLE_TICKS, () -> sprintTick(player))
                .thenExecute(() -> {
                    helper.assertTrue(player.isInWater(), "the player never made it into the water, the test proves nothing");
                    helper.assertFalse(player.isRunningOnWater(), "a sprint started in the water should not carry");
                })
                // jumping out of the water, on top of the surface, is not a start either
                .thenExecute(() -> teleport(helper, player, WATER.above(2)))
                .thenExecuteFor(2, () -> sprintTick(player))
                .thenExecute(() -> {
                    helper.assertFalse(player.isRunningOnWater(), "jumping out of the water should not start a run");
                    helper.assertTrue(collisionShape(helper, player, WATER.above()).isEmpty(), "the surface should not hold up a swimmer");
                })
                .thenSucceed();
    }

    /** The other side of it: what leaving the ground must not break. */
    @GameTest(maxTicks = 100)
    public void theRunSurvivesAJump(GameTestHelper helper) {
        var player = onTheShore(helper, PowerUpFixtures.RUNS_ON_WATER);

        helper.startSequence()
                .thenExecuteFor(SETTLE_TICKS, () -> sprintTick(player))
                .thenExecute(() -> helper.assertTrue(player.isRunningOnWater(), "the run never started, the test proves nothing"))
                // in the air over the pond, on the way down from a jump
                .thenExecute(() -> teleport(helper, player, WATER.above(2)))
                .thenExecuteFor(FALL_TICKS, () -> sprintTick(player))
                .thenExecute(() -> {
                    helper.assertTrue(player.isRunningOnWater(), "a jump should not end the run");
                    helper.assertTrue(player.onGround(), "the player should have landed back on the surface of the water");
                    helper.assertTrue(player.getY() > helper.absolutePos(WATER).getY() + 0.5D,
                            "the player fell through the water instead of landing on it");
                })
                .thenSucceed();
    }

    @GameTest(maxTicks = 200)
    public void hittingAWallEndsTheRun(GameTestHelper helper) {
        // shore, three blocks of water to run across, then a wall too tall to step over
        Arena.buildFloor(helper);
        helper.setBlock(SHORE, Blocks.STONE);
        for (int i = 0; i < 3; i++) {
            helper.setBlock(WATER.south(i), Blocks.WATER);
        }
        var wall = WATER.south(3);
        for (int dy = 0; dy < 3; dy++) {
            helper.setBlock(wall.above(dy), Blocks.STONE);
        }

        var player = TestPlayers.at(helper, SHORE.above());
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.RUNS_ON_WATER));

        helper.startSequence()
                .thenExecuteFor(SETTLE_TICKS, () -> sprintTick(player))
                .thenExecute(() -> helper.assertTrue(player.isRunningOnWater(), "the run never started, the test proves nothing"))
                .thenExecuteFor(CROSSING_TICKS, () -> {
                    player.setDeltaMovement(new Vec3(0.0D, player.getDeltaMovement().y, RUNNING_SPEED));
                    sprintTick(player);
                })
                .thenExecute(() -> {
                    helper.assertTrue(player.horizontalCollision, "the player never reached the wall, the test proves nothing");
                    helper.assertFalse(player.isRunningOnWater(), "hitting a wall should end the run");
                    helper.assertTrue(player.isInWater(), "the player should have fallen in once the run ended");
                })
                .thenSucceed();
    }

    // -- the water ----------------------------------------------------------------------------

    @GameTest
    public void waterHoldsUpARunningPlayer(GameTestHelper helper) {
        var player = onTheWater(helper, WATER.above());

        helper.assertFalse(collisionShape(helper, player, WATER).isEmpty(), "water should hold up a player running on it");
        helper.succeed();
    }

    @GameTest
    public void waterStaysWaterForEveryoneElse(GameTestHelper helper) {
        var player = onTheWater(helper, WATER.above());
        player.setRunningOnWater(false);

        helper.assertTrue(collisionShape(helper, player, WATER).isEmpty(), "water should only turn solid for a player running on it");
        helper.succeed();
    }

    @GameTest
    public void thePlayerHasToBeOnTopOfTheWater(GameTestHelper helper) {
        // standing inside the column rather than on it: the surface is not under the player's feet
        var player = onTheWater(helper, WATER);
        helper.setBlock(WATER.above(), Blocks.WATER);

        helper.assertTrue(collisionShape(helper, player, WATER.above()).isEmpty(),
                "a player under the surface should not be held up by the water above them");
        helper.succeed();
    }

    // -- support ------------------------------------------------------------------------------

    /** A pond and its shore, with a bare player standing on the shore. */
    private static ServerPlayer onTheShore(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(WATER, Blocks.WATER);
        helper.setBlock(SHORE, Blocks.STONE);

        return TestPlayers.at(helper, SHORE.above());
    }

    /** The same, with {@code powerUp} in hand. */
    private static ServerPlayer onTheShore(GameTestHelper helper, ResourceKey<PowerUp> powerUp) {
        var player = onTheShore(helper);
        player.setPowerUp(PowerUpFixtures.get(helper, powerUp));
        return player;
    }

    /** A player already on a run, standing on {@code pos}, for the tests about the water itself. */
    private static ServerPlayer onTheWater(GameTestHelper helper, BlockPos pos) {
        Arena.buildFloor(helper);
        helper.setBlock(WATER, Blocks.WATER);

        var player = TestPlayers.at(helper, pos);
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.RUNS_ON_WATER));
        player.setRunningOnWater(true);
        return player;
    }

    /** Raises a shield, the way a player blocking mid-sprint would. */
    private static void startUsingAnItem(ServerPlayer player) {
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.SHIELD));
        player.startUsingItem(InteractionHand.MAIN_HAND);
    }

    /** Ticks the player with the sprint key held down, the way a client would keep it on. */
    private static void sprintTick(ServerPlayer player) {
        player.setSprinting(true);
        TestPlayers.tick(player);
    }

    private static void teleport(GameTestHelper helper, ServerPlayer player, BlockPos pos) {
        var absolute = helper.absolutePos(pos);
        player.teleportTo(absolute.getX() + 0.5D, absolute.getY(), absolute.getZ() + 0.5D);
    }

    private static VoxelShape collisionShape(GameTestHelper helper, ServerPlayer player, BlockPos pos) {
        var absolute = helper.absolutePos(pos);
        return helper.getLevel().getBlockState(absolute)
                .getCollisionShape(helper.getLevel(), absolute, CollisionContext.of(player));
    }
}
