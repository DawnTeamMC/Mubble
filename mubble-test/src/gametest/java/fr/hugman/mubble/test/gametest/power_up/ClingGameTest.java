package fr.hugman.mubble.test.gametest.power_up;

import fr.hugman.mubble.test.gametest.datapack.PowerUpFixtures;
import fr.hugman.mubble.test.gametest.support.AirPlayers;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import fr.hugman.mubble.world.power_up.PowerUp;

import java.util.List;

import static fr.hugman.mubble.test.gametest.support.AirPlayers.JUMP_HELD;

/**
 * Clinging to the blocks of a set like a ladder, on the {@code flier} fixture: the honey blocks and hives of
 * {@code #super_mario:bee_climbable}.
 */
public class ClingGameTest {
    private static final int WALL_X = 5;
    private static final double LEAN = 0.1D;
    /** Enough ticks of leaning to be against the wall, whatever the shape of its block. */
    private static final int SETTLE_TICKS = 4;
    private static final int HEIGHT = Arena.FLOOR_Y + 4;
    private static final int CLINGING_REFILL = 2;
    private static final List<Block> HONEY_BLOCKS = List.of(Blocks.HONEY_BLOCK, Blocks.HONEYCOMB_BLOCK, Blocks.BEEHIVE, Blocks.BEE_NEST);

    @GameTest
    public void everyDefaultHoneyBlockCanBeClung(GameTestHelper helper) {
        Arena.buildFloor(helper);
        for (int index = 0; index < HONEY_BLOCKS.size(); index++) {
            var block = HONEY_BLOCKS.get(index);
            int z = 1 + 2 * index;
            wall(helper, block, z);
            var player = pressed(helper, PowerUpFixtures.FLIER, z);

            lean(player, 3);

            helper.assertTrue(player.isClinging(), "the player should cling to " + block);
            helper.assertTrue(player.onClimbable(), "the player should climb " + block + " like a ladder");
        }
        helper.succeed();
    }

    @GameTest
    public void clingingSlowsTheFallLikeALadder(GameTestHelper helper) {
        Arena.buildFloor(helper);
        wall(helper, Blocks.HONEY_BLOCK, 3);
        var player = pressed(helper, PowerUpFixtures.FLIER, 3);

        lean(player, 8);

        helper.assertTrue(player.getDeltaMovement().y() >= -0.15D - 1.0E-6D,
                "a clinging player should not fall faster than a ladder lets them, was " + player.getDeltaMovement().y());
        helper.assertValueEqual((float) player.fallDistance, 0.0F, "the fall distance of a player clinging to a wall");
        helper.succeed();
    }

    @GameTest
    public void aHeldJumpKeyClimbsTheWall(GameTestHelper helper) {
        Arena.buildFloor(helper);
        wall(helper, Blocks.HONEY_BLOCK, 3);
        var player = pressed(helper, PowerUpFixtures.FLIER, 3);
        lean(player, 1);

        double before = player.getY();
        lean(player, JUMP_HELD, 5);

        helper.assertTrue(player.getY() > before, "a jump key held against the wall should climb it, was " + player.getY() + " from " + before);
        helper.assertFalse(player.isFlying(), "a player clinging to a wall is not flying");
        helper.succeed();
    }

    @GameTest
    public void clingingRefillsTheGauge(GameTestHelper helper) {
        var player = AirPlayers.jumper(helper, PowerUpFixtures.FLIER);
        wall(helper, Blocks.HONEY_BLOCK, 3);
        AirPlayers.tick(player, JUMP_HELD, 20);
        int before = player.getFlightGauge();
        helper.assertTrue(before < 100 - 5 * CLINGING_REFILL, "the gauge never drained, the test proves nothing");

        player.teleportTo(helper.absolutePos(new BlockPos(WALL_X, 0, 0)).getX() - 0.5D, player.getY(), player.getZ());
        lean(player, SETTLE_TICKS);
        before = player.getFlightGauge();
        lean(player, 5);

        helper.assertTrue(player.isClinging(), "the player is not clinging, the test proves nothing");
        helper.assertValueEqual(player.getFlightGauge(), before + 5 * CLINGING_REFILL, "the gauge after five ticks on the wall");
        helper.succeed();
    }

    @GameTest
    public void withoutTheFormNothingCanBeClung(GameTestHelper helper) {
        Arena.buildFloor(helper);
        wall(helper, Blocks.HONEY_BLOCK, 3);
        var player = pressed(helper, PowerUpFixtures.EMPTY, 3);

        lean(player, 3);

        helper.assertFalse(player.isClinging(), "a player without the form should not cling");
        helper.assertFalse(player.onClimbable(), "a player without the form should not climb honey");
        helper.succeed();
    }

    @GameTest
    public void aBlockOutsideTheSetCannotBeClung(GameTestHelper helper) {
        Arena.buildFloor(helper);
        wall(helper, Blocks.STONE, 3);
        var player = pressed(helper, PowerUpFixtures.FLIER, 3);

        lean(player, 3);

        helper.assertFalse(player.isClinging(), "stone is not in the set of blocks to cling to");
        helper.succeed();
    }

    @GameTest
    public void aFloorOfHoneyIsNotAWall(GameTestHelper helper) {
        Arena.buildFloor(helper, Blocks.HONEY_BLOCK.defaultBlockState());
        var player = TestPlayers.at(helper, AirPlayers.STAND);
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.FLIER));

        AirPlayers.tick(player, Input.EMPTY, 3);

        helper.assertFalse(player.isClinging(), "standing on honey is not clinging to it");
        helper.assertFalse(player.onClimbable(), "standing on honey is not climbing it");
        helper.succeed();
    }

    @GameTest
    public void otherPlayersAreToldAboutTheCling(GameTestHelper helper) {
        Arena.buildFloor(helper);
        wall(helper, Blocks.HONEY_BLOCK, 3);
        var player = pressed(helper, PowerUpFixtures.FLIER, 3);
        lean(player, 3);
        var viewer = TestPlayers.mock(helper);

        viewer.getEntityData().assignValues(player.getEntityData().getNonDefaultValues());

        helper.assertTrue(viewer.isClinging(), "a player seeing someone cling should know about it");
        helper.succeed();
    }

    private static void wall(GameTestHelper helper, Block block, int z) {
        for (int y = Arena.FLOOR_Y + 1; y < Arena.SIZE - 1; y++) {
            helper.setBlock(new BlockPos(WALL_X, y, z), block);
        }
    }

    /** Ticks {@code player} while they push towards the wall, which is what keeps them against it. */
    private static void lean(ServerPlayer player, int ticks) {
        lean(player, Input.EMPTY, ticks);
    }

    private static void lean(ServerPlayer player, Input keys, int ticks) {
        for (int tick = 0; tick < ticks; tick++) {
            player.setDeltaMovement(LEAN, player.getDeltaMovement().y(), 0.0D);
            AirPlayers.tick(player, keys, 1);
        }
    }

    /** A player in mid-air a step away from the wall at {@code z}. */
    private static ServerPlayer pressed(GameTestHelper helper, ResourceKey<PowerUp> powerUp, int z) {
        var player = TestPlayers.at(helper, new BlockPos(WALL_X, HEIGHT, z));
        player.teleportTo(helper.absolutePos(new BlockPos(WALL_X, HEIGHT, z)).getX() - 0.5D, player.getY(), player.getZ());
        player.setPowerUp(PowerUpFixtures.get(helper, powerUp));
        player.setOnGround(false);
        lean(player, SETTLE_TICKS);
        return player;
    }
}
