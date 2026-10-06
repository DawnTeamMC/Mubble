package fr.hugman.mubble.test.gametest.power_up;

import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpIds;
import fr.hugman.mubble.test.gametest.datapack.PowerUpFixtures;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import fr.hugman.mubble.world.power_up.PowerUp;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * Putting a power-up on a player and taking it away again: the attribute modifiers it carries, the
 * charge state it sets up, and the rules deciding whether a change is allowed at all.
 */
public class PowerUpHolderGameTest {
    @GameTest
    public void aPlayerStartsWithoutAPowerUp(GameTestHelper helper) {
        var player = TestPlayers.mock(helper);

        helper.assertTrue(player.getPowerUp().isEmpty(), "a fresh player already holds a power-up");
        helper.assertTrue(player.getPowerUpProperties() == null, "a fresh player already has charge properties");

        helper.succeed();
    }

    @GameTest
    public void settingAPowerUpKeepsIt(GameTestHelper helper) {
        var player = TestPlayers.mock(helper);
        var fire = PowerUpFixtures.registry(helper).getOrThrow(SuperMarioPowerUpIds.FIRE);

        player.setPowerUp(fire);

        helper.assertTrue(player.getPowerUp().isPresent(), "the power-up was not kept");
        helper.assertTrue(player.getPowerUp().orElseThrow().is(SuperMarioPowerUpIds.FIRE), "the wrong power-up was kept");

        helper.succeed();
    }

    @GameTest
    public void clearingAPowerUpTakesItAway(GameTestHelper helper) {
        var player = TestPlayers.mock(helper);
        player.setPowerUp(PowerUpFixtures.registry(helper).getOrThrow(SuperMarioPowerUpIds.FIRE));

        player.clearPowerUp();

        helper.assertTrue(player.getPowerUp().isEmpty(), "the power-up stayed after being cleared");
        helper.assertTrue(player.getPowerUpProperties() == null, "the charge properties outlived the power-up");

        helper.succeed();
    }

    @GameTest
    public void attributeModifiersFollowThePowerUp(GameTestHelper helper) {
        var player = TestPlayers.mock(helper);
        double baseHealth = player.getAttributeValue(Attributes.MAX_HEALTH);

        // The fixture adds a flat 10 to max health and nothing else, so the arithmetic is unambiguous.
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.ATTRIBUTES_ONLY));
        helper.assertValueEqual(player.getAttributeValue(Attributes.MAX_HEALTH), baseHealth + 10.0D, "max health while holding the power-up");

        player.clearPowerUp();
        helper.assertValueEqual(player.getAttributeValue(Attributes.MAX_HEALTH), baseHealth, "max health after losing the power-up");

        helper.succeed();
    }

    @GameTest
    public void swappingPowerUpsDoesNotStackTheirModifiers(GameTestHelper helper) {
        var player = TestPlayers.mock(helper);
        double baseHealth = player.getAttributeValue(Attributes.MAX_HEALTH);
        var attributesOnly = PowerUpFixtures.get(helper, PowerUpFixtures.ATTRIBUTES_ONLY);

        player.setPowerUp(attributesOnly);
        player.setPowerUp(attributesOnly);

        helper.assertValueEqual(player.getAttributeValue(Attributes.MAX_HEALTH), baseHealth + 10.0D,
                "max health after taking the same power-up twice");

        helper.succeed();
    }

    @GameTest
    public void aPowerUpWithAnActionSetsUpItsCharges(GameTestHelper helper) {
        var player = TestPlayers.mock(helper);

        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.SHOOTER));

        var properties = player.getPowerUpProperties();
        helper.assertTrue(properties != null, "a power-up with an action must set up its charges");
        helper.assertValueEqual(properties.getChargeCount(), 2, "the fixture shoots twice before running out");

        helper.succeed();
    }

    @GameTest
    public void aPowerUpWithoutAnActionHasNoCharges(GameTestHelper helper) {
        var player = TestPlayers.mock(helper);

        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.EMPTY));

        helper.assertTrue(player.getPowerUpProperties() == null, "a power-up without an action should have nothing to charge");
        helper.succeed();
    }

    @GameTest
    public void takingTheSamePowerUpAgainIsRefusedUnlessItCanBeRefilled(GameTestHelper helper) {
        var player = TestPlayers.mock(helper);
        var empty = PowerUpFixtures.get(helper, PowerUpFixtures.EMPTY);

        helper.assertTrue(PowerUp.canChange(player, empty), "a player without a power-up should accept any");

        player.setPowerUp(empty);
        helper.assertFalse(PowerUp.canChange(player, empty), "a power-up that cannot be refilled should be refused twice over");

        helper.succeed();
    }

    @GameTest
    public void aSpentPowerUpCanBeRefilledButAFullOneCannot(GameTestHelper helper) {
        var player = TestPlayers.mock(helper);
        var shooter = PowerUpFixtures.get(helper, PowerUpFixtures.SHOOTER);

        player.setPowerUp(shooter);
        helper.assertFalse(PowerUp.canRefill(player, shooter), "a power-up at full charge has nothing to refill");

        // Spending a charge is what a trigger does; here it is done directly to keep the test about refilling.
        player.getPowerUpProperties().useCharge();
        helper.assertTrue(PowerUp.canRefill(player, shooter), "a power-up that spent a charge should be refillable");
        helper.assertTrue(PowerUp.canChange(player, shooter), "and taking it again should therefore be allowed");

        helper.succeed();
    }

    @GameTest
    public void changingPowerUpSwapsTheModifiersOver(GameTestHelper helper) {
        var player = TestPlayers.mock(helper);
        double baseHealth = player.getAttributeValue(Attributes.MAX_HEALTH);

        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.ATTRIBUTES_ONLY));
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.EMPTY));

        helper.assertValueEqual(player.getAttributeValue(Attributes.MAX_HEALTH), baseHealth,
                "the modifiers of the previous power-up were left behind");

        helper.succeed();
    }
}
