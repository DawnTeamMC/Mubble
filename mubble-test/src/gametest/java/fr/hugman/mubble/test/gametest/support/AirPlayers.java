package fr.hugman.mubble.test.gametest.support;

import fr.hugman.mubble.test.gametest.datapack.PowerUpFixtures;
import fr.hugman.mubble.world.power_up.PowerUp;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec3;

/**
 * Players for the tests of the moves done with the jump key: off the ground, holding a power-up, and
 * driven tick after tick the way a client would.
 */
public final class AirPlayers {
    public static final BlockPos STAND = new BlockPos(4, Arena.FLOOR_Y + 1, 3);
    public static final Input JUMP_HELD = TestPlayers.holdingJump();

    private static final Vec3 JUMP = new Vec3(0.0D, 0.42D, 0.0D);

    private AirPlayers() {
    }

    /** A player who has just jumped on a fresh floor, holding the jump key. */
    public static ServerPlayer jumper(GameTestHelper helper, ResourceKey<PowerUp> powerUp) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);
        player.setPowerUp(PowerUpFixtures.get(helper, powerUp));

        player.setDeltaMovement(JUMP);
        TestPlayers.tick(player, JUMP_HELD);
        return player;
    }

    public static void tick(ServerPlayer player, Input keys, int ticks) {
        for (int tick = 0; tick < ticks; tick++) {
            TestPlayers.tick(player, keys);
        }
    }
}
