package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.entity.platform.CloudPlatform;
import fr.hugman.mubble.super_mario.world.power_up.action.SpawnCloudPlatformPowerUpAction;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Optional;

/**
 * The action behind the cloud power-up. The platform it drops belongs under its holder, but never
 * under the floor they stand on: a platform spawned inside blocks is one nobody can stand on.
 */
public class SpawnCloudPlatformActionGameTest {
    private static final BlockPos STAND = new BlockPos(4, Arena.FLOOR_Y + 1, 3);
    private static final double EPSILON = 1.0E-4D;

    /** The same action the cloud power-up is built with, see {@code SuperMarioPowerUpProvider}. */
    private static final SpawnCloudPlatformPowerUpAction ACTION =
            new SpawnCloudPlatformPowerUpAction(SuperMarioEntityTypes.CLOUD_PLATFORM, Optional.of(3));

    @GameTest
    public void aPlatformDroppedInTheAirStaysUnderItsHolder(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND.above(4));
        double feet = player.getY();

        helper.assertTrue(ACTION.trigger(player) == InteractionResult.SUCCESS, "the trigger did not report a success");

        var platform = platformOf(helper, player);
        helper.assertTrue(top(platform) < feet, "the platform should appear under the player, not around them");
        helper.assertValueEqual(player.getY(), feet, "the player moved even though the platform fit right under them");

        helper.succeed();
    }

    /**
     * The bug this guards against: used while standing on the ground, the action dropped the platform
     * a block and a half down, which is under the floor.
     */
    @GameTest
    public void aPlatformDroppedOnTheGroundStaysOutOfIt(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);

        helper.assertTrue(ACTION.trigger(player) == InteractionResult.SUCCESS, "the trigger did not report a success");

        var platform = platformOf(helper, player);
        helper.assertTrue(helper.getLevel().noCollision(null, platform.getBoundingBox().deflate(EPSILON)),
                "the platform was spawned inside blocks");

        helper.succeed();
    }

    @GameTest
    public void aPlatformRisingOutOfTheGroundTakesItsHolderWithIt(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);
        double feet = player.getY();

        ACTION.trigger(player);

        var platform = platformOf(helper, player);
        helper.assertTrue(player.getY() > feet, "the player was left standing on the floor next to the platform");
        helper.assertTrue(Math.abs(player.getY() - top(platform)) < EPSILON,
                "the player should end up right on top of the platform, and not somewhere else");

        helper.succeed();
    }

    /** With blocks all the way up, there is nowhere the platform could go without ending up in one. */
    @GameTest
    public void aPlatformWithNoRoomIsNotDroppedAtAll(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);
        helper.setBlock(STAND, Blocks.STONE);
        helper.setBlock(STAND.above(), Blocks.STONE);

        helper.assertTrue(ACTION.trigger(player) == InteractionResult.FAIL, "a platform with nowhere to go should fail");
        helper.assertTrue(platformsAround(helper, player).isEmpty(), "a platform was spawned anyway");

        helper.succeed();
    }

    private static double top(CloudPlatform platform) {
        return platform.getY() + platform.getBbHeight();
    }

    private static List<CloudPlatform> platformsAround(GameTestHelper helper, ServerPlayer player) {
        return helper.getLevel().getEntitiesOfClass(CloudPlatform.class, player.getBoundingBox().inflate(8.0D));
    }

    private static CloudPlatform platformOf(GameTestHelper helper, ServerPlayer player) {
        return platformsAround(helper, player).stream().findFirst()
                .orElseThrow(() -> new AssertionError("no platform was spawned at all"));
    }
}
