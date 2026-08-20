package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.entity.platform.CloudPlatform;
import fr.hugman.mubble.test.gametest.support.Arena;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;

/**
 * The platform the cloud power-up drops under its holder. It is on a timer, and the timer is the
 * whole point: a platform that never goes away turns the power-up into flight.
 */
public class CloudPlatformGameTest {
    private static final BlockPos SPOT = new BlockPos(4, Arena.FLOOR_Y + 2, 3);

    @GameTest
    public void aplatformStartsOnATimer(GameTestHelper helper) {
        var platform = spawn(helper);

        helper.assertFalse(platform.getDuration() == CloudPlatform.INFINITE_DURATION,
                "a platform with no duration would never disappear");
        helper.assertTrue(platform.alwaysShrinks(), "a fresh platform should be set to shrink");

        helper.succeed();
    }

    @GameTest(maxTicks = 100)
    public void aplatformGoesAwayOnceItsTimeIsUp(GameTestHelper helper) {
        var platform = spawn(helper);
        platform.setDuration(10);

        helper.succeedWhen(() -> helper.assertTrue(platform.isRemoved(), "the platform outlived its duration"));
    }

    @GameTest(maxTicks = 100)
    public void anEndlessPlatformStays(GameTestHelper helper) {
        var platform = spawn(helper);
        platform.setDuration(CloudPlatform.INFINITE_DURATION);

        helper.startSequence()
                .thenIdle(40)
                .thenExecute(() -> {
                    helper.assertFalse(platform.isRemoved(), "a platform with an infinite duration disappeared anyway");
                    helper.assertFalse(platform.isShrinking(), "and it should never start shrinking");
                })
                .thenSucceed();
    }

    @GameTest(maxTicks = 100)
    public void aplatformShrinksBeforeItGoes(GameTestHelper helper) {
        var platform = spawn(helper);
        platform.setDuration(30);

        // Shrinking is the warning that the platform is about to vanish, so it has to come first.
        helper.succeedWhen(() -> helper.assertTrue(platform.isShrinking(), "the platform vanished without shrinking first"));
    }

    @GameTest
    public void anEmptyPlatformIsNotOccupied(GameTestHelper helper) {
        var platform = spawn(helper);

        helper.assertFalse(platform.isOccupied(), "a platform with nobody on it reports being occupied");
        helper.succeed();
    }

    @GameTest
    public void theOwnerIsRemembered(GameTestHelper helper) {
        var platform = spawn(helper);
        var pig = helper.spawnWithNoFreeWill(net.minecraft.world.entity.EntityTypes.PIG, SPOT.below());

        platform.setOwner(pig);

        helper.assertTrue(platform.getOwner() == pig, "the platform forgot who put it there");
        helper.succeed();
    }

    private static CloudPlatform spawn(GameTestHelper helper) {
        Arena.buildFloor(helper);
        return helper.spawn(SuperMarioEntityTypes.CLOUD_PLATFORM, SPOT);
    }
}
