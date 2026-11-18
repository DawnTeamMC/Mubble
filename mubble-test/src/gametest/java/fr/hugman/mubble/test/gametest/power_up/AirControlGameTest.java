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

/**
 * The steering of a glide, see {@code glider.json}: the fall of the floater with 0.05 of acceleration up to 0.4
 * blocks per tick on top of it.
 */
public class AirControlGameTest {
    private static final BlockPos STAND = new BlockPos(4, Arena.FLOOR_Y + 1, 3);
    private static final double TOP_SPEED = 0.4D;
    private static final double EPSILON = 1.0E-4D;

    private static final Input GLIDING_FORWARD = TestPlayers.holdingJumpAndForward();
    private static final Input FORWARD = new Input(true, false, false, false, false, false, false);

    @GameTest
    public void aGliderGoesFurtherThanAFloaterOnTheSameKeys(GameTestHelper helper) {
        var glider = falling(helper, PowerUpFixtures.GLIDER);
        var floater = falling(helper, PowerUpFixtures.FLOATER, -2);
        double gliderFrom = glider.getZ();
        double floaterFrom = floater.getZ();

        for (int tick = 0; tick < 12; tick++) {
            TestPlayers.tick(glider, GLIDING_FORWARD);
            TestPlayers.tick(floater, GLIDING_FORWARD);
        }

        helper.assertTrue(glider.isFloating(), "the glider never floated, the test proves nothing");
        helper.assertTrue(glider.getZ() - gliderFrom > floater.getZ() - floaterFrom + 0.5D,
                "a glider should cover more ground than a floater, " + (glider.getZ() - gliderFrom) + " against " + (floater.getZ() - floaterFrom));
        helper.succeed();
    }

    @GameTest
    public void steeringStopsAddingAtTheTopSpeed(GameTestHelper helper) {
        var glider = falling(helper, PowerUpFixtures.GLIDER);

        for (int tick = 0; tick < 25; tick++) {
            TestPlayers.tick(glider, GLIDING_FORWARD);
            helper.assertTrue(glider.getDeltaMovement().horizontalDistance() <= TOP_SPEED + EPSILON,
                    "a glider should not steer past the top speed, moving " + glider.getDeltaMovement().horizontalDistance());
        }
        helper.succeed();
    }

    @GameTest
    public void noSteeringWithoutTheJumpKey(GameTestHelper helper) {
        var glider = falling(helper, PowerUpFixtures.GLIDER);
        var plain = falling(helper, PowerUpFixtures.EMPTY, -2);
        double gliderFrom = glider.getZ();
        double plainFrom = plain.getZ();

        for (int tick = 0; tick < 6; tick++) {
            TestPlayers.tick(glider, FORWARD);
            TestPlayers.tick(plain, FORWARD);
        }

        helper.assertFalse(glider.isFloating(), "a glider that is not holding jump should not float");
        helper.assertTrue(Math.abs((glider.getZ() - gliderFrom) - (plain.getZ() - plainFrom)) < 0.02D, "a fall is a fall without the jump key, " + (glider.getZ() - gliderFrom) + " against " + (plain.getZ() - plainFrom));
        helper.succeed();
    }

    private static ServerPlayer falling(GameTestHelper helper, ResourceKey<PowerUp> powerUp) {
        return falling(helper, powerUp, 0);
    }

    private static ServerPlayer falling(GameTestHelper helper, ResourceKey<PowerUp> powerUp, int eastOffset) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND.above(Arena.SIZE - 2).east(eastOffset));
        player.setPowerUp(PowerUpFixtures.get(helper, powerUp));
        for (int tick = 0; tick < 8; tick++) {
            TestPlayers.tick(player, Input.EMPTY);
        }
        return player;
    }
}
