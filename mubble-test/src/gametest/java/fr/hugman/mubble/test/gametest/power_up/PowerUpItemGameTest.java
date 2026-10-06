package fr.hugman.mubble.test.gametest.power_up;

import fr.hugman.mubble.core.component.MubbleDataComponents;
import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpIds;
import fr.hugman.mubble.super_mario.world.item.SuperMarioItems;
import fr.hugman.mubble.test.gametest.datapack.PowerUpFixtures;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import fr.hugman.mubble.world.item.MubbleCooldownGroups;
import fr.hugman.mubble.world.item.consume_effects.ChangePowerUpConsumeEffect;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;

/**
 * Getting a power-up from an item, both ways the mod offers: the flowers, which hand it over on use,
 * and the mushrooms, which do it through a consume effect.
 */
public class PowerUpItemGameTest {
    @GameTest
    public void aFlowerCarriesThePowerUpItGives(GameTestHelper helper) {
        var stack = new ItemStack(SuperMarioItems.FIRE_FLOWER);

        var component = stack.get(MubbleDataComponents.POWER_UP);
        helper.assertTrue(component != null, "the fire flower carries no power-up at all");
        helper.assertTrue(component.powerUp().is(SuperMarioPowerUpIds.FIRE), "the fire flower points at the wrong power-up");

        helper.succeed();
    }

    @GameTest
    public void usingAFlowerGivesThePowerUpAndSpendsIt(GameTestHelper helper) {
        var player = survivalPlayer(helper);
        var stack = new ItemStack(SuperMarioItems.FIRE_FLOWER, 2);
        player.setItemInHand(InteractionHand.MAIN_HAND, stack);

        var result = SuperMarioItems.FIRE_FLOWER.use(helper.getLevel(), player, InteractionHand.MAIN_HAND);

        helper.assertTrue(result == InteractionResult.SUCCESS, "using the flower did not report a success");
        helper.assertTrue(player.getPowerUp().orElseThrow().is(SuperMarioPowerUpIds.FIRE), "using the flower gave nothing");
        helper.assertValueEqual(player.getItemInHand(InteractionHand.MAIN_HAND).getCount(), 1, "the stack after using one flower");

        helper.succeed();
    }

    @GameTest
    public void usingTheSameFlowerTwiceIsRefused(GameTestHelper helper) {
        var player = survivalPlayer(helper);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(SuperMarioItems.FIRE_FLOWER, 2));

        SuperMarioItems.FIRE_FLOWER.use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        var second = SuperMarioItems.FIRE_FLOWER.use(helper.getLevel(), player, InteractionHand.MAIN_HAND);

        // The fire power-up can be refilled, but only once a projectile has been spent.
        helper.assertTrue(second == InteractionResult.FAIL, "a full power-up should refuse a second flower");
        helper.assertValueEqual(player.getItemInHand(InteractionHand.MAIN_HAND).getCount(), 1, "and the refused flower should not be eaten");

        helper.succeed();
    }

    @GameTest
    public void aSpentPowerUpAcceptsAnotherFlower(GameTestHelper helper) {
        var player = survivalPlayer(helper);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(SuperMarioItems.FIRE_FLOWER, 2));

        SuperMarioItems.FIRE_FLOWER.use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        player.getPowerUpProperties().useCharge();

        var refill = SuperMarioItems.FIRE_FLOWER.use(helper.getLevel(), player, InteractionHand.MAIN_HAND);

        helper.assertTrue(refill == InteractionResult.SUCCESS, "a power-up short of a charge should accept a refill");
        helper.assertValueEqual(player.getItemInHand(InteractionHand.MAIN_HAND).getCount(), 0, "and the refill should cost a flower");

        helper.succeed();
    }

    /** The flowers are only spent outside creative, so the tests asking about the stack need survival. */
    private static net.minecraft.server.level.ServerPlayer survivalPlayer(GameTestHelper helper) {
        var player = TestPlayers.inLevel(helper);
        player.setGameMode(net.minecraft.world.level.GameType.SURVIVAL);
        return player;
    }

    @GameTest
    public void everyPowerUpItemSharesOneCooldownGroup(GameTestHelper helper) {
        var stack = new ItemStack(SuperMarioItems.FIRE_FLOWER);

        var cooldown = stack.get(net.minecraft.core.component.DataComponents.USE_COOLDOWN);
        helper.assertTrue(cooldown != null, "the flower has no use cooldown");
        helper.assertTrue(cooldown.cooldownGroup().orElseThrow().equals(MubbleCooldownGroups.POWER_UPS),
                "power-up items must share one cooldown group, or they can be chained");

        helper.succeed();
    }

    @GameTest
    public void aConsumeEffectHandsOverThePowerUp(GameTestHelper helper) {
        var player = TestPlayers.inLevel(helper);
        var effect = new ChangePowerUpConsumeEffect(PowerUpFixtures.get(helper, PowerUpFixtures.EMPTY));

        var applied = effect.apply(helper.getLevel(), ItemStack.EMPTY, player);

        helper.assertTrue(applied, "the consume effect reported doing nothing");
        helper.assertTrue(player.getPowerUp().orElseThrow().is(PowerUpFixtures.EMPTY), "the consume effect gave the wrong power-up");

        helper.succeed();
    }

    @GameTest
    public void aConsumeEffectIsRefusedWhenThePowerUpIsAlreadyHeld(GameTestHelper helper) {
        var player = TestPlayers.inLevel(helper);
        var effect = new ChangePowerUpConsumeEffect(PowerUpFixtures.get(helper, PowerUpFixtures.EMPTY));
        effect.apply(helper.getLevel(), ItemStack.EMPTY, player);

        var again = effect.apply(helper.getLevel(), ItemStack.EMPTY, player);

        helper.assertFalse(again, "applying the same power-up twice should report that nothing happened");
        helper.succeed();
    }

    @GameTest
    public void theSuperMushroomCarriesAConsumeEffect(GameTestHelper helper) {
        var stack = new ItemStack(SuperMarioItems.SUPER_MUSHROOM);

        var consumable = stack.get(net.minecraft.core.component.DataComponents.CONSUMABLE);
        helper.assertTrue(consumable != null, "the super mushroom is not consumable any more");
        helper.assertFalse(consumable.onConsumeEffects().isEmpty(), "the super mushroom does nothing when eaten");

        helper.succeed();
    }
}
