package fr.hugman.mubble.test.gametest.power_up;

import fr.hugman.mubble.test.gametest.datapack.PowerUpFixtures;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.shapes.CollisionContext;

/**
 * Running on water: the collision shape of a water block becomes solid, but only for a sprinting
 * player holding a power-up tagged {@code mubble:can_run_on_water}.
 * <p>
 * This is asked of the block state directly rather than played out with a moving player, because the
 * mixin answers a question — is there something to stand on — and that answer is the whole feature.
 */
public class RunOnWaterGameTest {
    private static final BlockPos WATER = new BlockPos(4, Arena.FLOOR_Y + 1, 3);

    @GameTest
    public void aSprintingHolderWalksOnWater(GameTestHelper helper) {
        var player = prepare(helper, PowerUpFixtures.RUNS_ON_WATER, true);

        helper.assertFalse(collisionShape(helper, player).isEmpty(),
                "water should hold up a sprinting player with the power-up");
        helper.succeed();
    }

    @GameTest
    public void walkingIsNotEnough(GameTestHelper helper) {
        var player = prepare(helper, PowerUpFixtures.RUNS_ON_WATER, false);

        helper.assertTrue(collisionShape(helper, player).isEmpty(),
                "the power-up should only hold a player up while sprinting");
        helper.succeed();
    }

    @GameTest
    public void anUntaggedPowerUpDoesNotHoldYouUp(GameTestHelper helper) {
        var player = prepare(helper, PowerUpFixtures.EMPTY, true);

        helper.assertTrue(collisionShape(helper, player).isEmpty(),
                "only power-ups tagged can_run_on_water should hold a player up");
        helper.succeed();
    }

    @GameTest
    public void aPlayerWithoutAPowerUpSinks(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(WATER, Blocks.WATER);
        var player = TestPlayers.at(helper, WATER.above());
        player.setSprinting(true);

        helper.assertTrue(collisionShape(helper, player).isEmpty(), "water should stay water for everyone else");
        helper.succeed();
    }

    @GameTest
    public void thePlayerHasToBeOnTopOfTheWater(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(WATER, Blocks.WATER);
        helper.setBlock(WATER.above(), Blocks.WATER);

        // Standing inside the column rather than on it: the surface is not under the player's feet.
        var player = TestPlayers.at(helper, WATER);
        player.setSprinting(true);
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.RUNS_ON_WATER));

        helper.assertTrue(collisionShape(helper, player).isEmpty(),
                "a player under the surface should not be held up by the water above them");
        helper.succeed();
    }

    private static ServerPlayer prepare(GameTestHelper helper, net.minecraft.resources.ResourceKey<fr.hugman.mubble.world.power_up.PowerUp> powerUp, boolean sprinting) {
        Arena.buildFloor(helper);
        helper.setBlock(WATER, Blocks.WATER);

        var player = TestPlayers.at(helper, WATER.above());
        player.setSprinting(sprinting);
        player.setPowerUp(PowerUpFixtures.get(helper, powerUp));
        return player;
    }

    private static net.minecraft.world.phys.shapes.VoxelShape collisionShape(GameTestHelper helper, ServerPlayer player) {
        var absolute = helper.absolutePos(WATER);
        return helper.getLevel().getBlockState(absolute)
                .getCollisionShape(helper.getLevel(), absolute, CollisionContext.of(player));
    }
}
