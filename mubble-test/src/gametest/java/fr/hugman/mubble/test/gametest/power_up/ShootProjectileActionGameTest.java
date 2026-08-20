package fr.hugman.mubble.test.gametest.power_up;

import fr.hugman.mubble.test.gametest.datapack.PowerUpFixtures;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.phys.AABB;

/**
 * The one power-up action of the core module. The fixture it uses shoots two snowballs, so running
 * out of charges takes two triggers rather than a dozen.
 */
public class ShootProjectileActionGameTest {
    private static final BlockPos STAND = new BlockPos(4, Arena.FLOOR_Y + 1, 3);

    @GameTest
    public void triggeringShootsTheProjectile(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.SHOOTER));

        var result = trigger(helper, player);

        helper.assertTrue(result == InteractionResult.SUCCESS, "the trigger did not report a success");
        helper.assertValueEqual(snowballsAround(helper, player), 1, "snowballs in the air after one trigger");

        helper.succeed();
    }

    @GameTest
    public void theShooterOwnsWhatItShoots(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.SHOOTER));

        trigger(helper, player);

        var snowball = helper.getLevel().getEntitiesOfClass(Snowball.class, around(player)).stream().findFirst()
                .orElseThrow(() -> new AssertionError("nothing was shot at all"));
        helper.assertTrue(snowball.getOwner() == player, "the projectile is not owned by whoever shot it");

        helper.succeed();
    }

    @GameTest
    public void everyTriggerCostsACharge(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.SHOOTER));

        helper.assertValueEqual(player.getPowerUpProperties().getChargeCount(), 2, "charges before shooting");

        trigger(helper, player);
        helper.assertValueEqual(player.getPowerUpProperties().getChargeCount(), 1, "charges after one shot");

        trigger(helper, player);
        helper.assertValueEqual(player.getPowerUpProperties().getChargeCount(), 0, "charges after two shots");

        helper.succeed();
    }

    @GameTest
    public void aSpentPowerUpShootsNothingMore(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);
        var shooter = PowerUpFixtures.get(helper, PowerUpFixtures.SHOOTER);
        player.setPowerUp(shooter);

        trigger(helper, player);
        trigger(helper, player);
        int afterSpending = snowballsAround(helper, player);

        var result = trigger(helper, player);

        helper.assertTrue(result == InteractionResult.PASS, "a spent power-up should pass rather than shoot");
        helper.assertFalse(shooter.value().canBeTriggered(player), "a spent power-up should not report itself as triggerable");
        helper.assertValueEqual(snowballsAround(helper, player), afterSpending, "a third snowball came out of an empty power-up");

        helper.succeed();
    }

    /**
     * The charges of this action are counted from the projectiles still flying, so they come back on
     * their own once those are gone.
     */
    @GameTest(maxTicks = 200)
    public void chargesComeBackAsTheProjectilesDie(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.SHOOTER));

        trigger(helper, player);
        trigger(helper, player);
        helper.assertValueEqual(player.getPowerUpProperties().getChargeCount(), 0, "charges right after shooting twice");

        // The player tick is what refreshes the charges, so it has to be driven here too.
        helper.succeedWhen(() -> {
            TestPlayers.tick(player);
            helper.assertValueEqual(player.getPowerUpProperties().getChargeCount(), 2, "the charges never came back");
        });
    }

    @GameTest
    public void aPowerUpWithoutAnActionDoesNothing(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);
        var empty = PowerUpFixtures.get(helper, PowerUpFixtures.EMPTY);
        player.setPowerUp(empty);

        helper.assertFalse(empty.value().canBeTriggered(player), "a power-up without an action cannot be triggered");
        helper.assertTrue(empty.value().trigger(player) == InteractionResult.PASS, "and triggering it should simply pass");

        helper.succeed();
    }

    private static InteractionResult trigger(GameTestHelper helper, net.minecraft.server.level.ServerPlayer player) {
        return player.getPowerUp().orElseThrow(() -> new AssertionError("the player holds no power-up")).value().trigger(player);
    }

    private static int snowballsAround(GameTestHelper helper, net.minecraft.world.entity.Entity player) {
        return helper.getLevel().getEntitiesOfClass(Snowball.class, around(player)).size();
    }

    private static AABB around(net.minecraft.world.entity.Entity player) {
        return player.getBoundingBox().inflate(8.0D);
    }
}
