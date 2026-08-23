package fr.hugman.mubble.test.gametest.voyage;

import com.mojang.serialization.JsonOps;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import fr.hugman.mubble.world.voyage.session.PlayerStash;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

/**
 * Taking a player's belongings away and giving them back.
 *
 * <p>The codec is covered by a unit test, but not the half that matters most: reading state off a
 * real player and putting it back. Item stacks cannot even be constructed without a server, so
 * everything involving them lives here.
 *
 * <p>Game mode is not asserted anywhere. The framework's mock player hard-codes
 * {@code gameMode()} to creative, so a round trip through it would prove nothing.
 */
public class PlayerStashGameTest {
    @GameTest
    public void aStashRoundTripsThroughAPlayer(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        Inventory inventory = player.getInventory();

        inventory.setItem(0, new ItemStack(Items.DIAMOND_SWORD));
        inventory.setItem(17, new ItemStack(Items.COOKED_BEEF, 32));
        inventory.setItem(38, new ItemStack(Items.IRON_CHESTPLATE));
        inventory.setSelectedSlot(4);
        player.addEffect(new MobEffectInstance(MobEffects.SPEED, 600, 1));
        player.setHealth(17.5F);
        player.getFoodData().setFoodLevel(14);
        player.giveExperiencePoints(394);
        Vec3 where = player.position();

        PlayerStash stash = PlayerStash.of(player);
        PlayerStash.clear(player);

        helper.assertTrue(inventory.getItem(0).isEmpty(), "the inventory was not emptied on entry");
        helper.assertTrue(inventory.getItem(38).isEmpty(), "armour was left on the player on entry");
        helper.assertTrue(player.getActiveEffects().isEmpty(), "effects were left on the player on entry");
        helper.assertValueEqual(player.totalExperience, 0, "experience after clearing");

        stash.restoreTo(player);

        helper.assertTrue(ItemStack.matches(inventory.getItem(0), new ItemStack(Items.DIAMOND_SWORD)),
                "the sword did not come back");
        helper.assertTrue(ItemStack.matches(inventory.getItem(17), new ItemStack(Items.COOKED_BEEF, 32)),
                "the stack of beef did not come back whole");
        helper.assertTrue(ItemStack.matches(inventory.getItem(38), new ItemStack(Items.IRON_CHESTPLATE)),
                "the chestplate did not come back — equipment slots are not being stashed");
        helper.assertValueEqual(inventory.getSelectedSlot(), 4, "the held slot");
        helper.assertValueEqual(player.getHealth(), 17.5F, "health");
        helper.assertValueEqual(player.getFoodData().getFoodLevel(), 14, "hunger");
        helper.assertValueEqual(player.totalExperience, 394, "total experience");
        helper.assertTrue(player.hasEffect(MobEffects.SPEED), "the speed effect did not come back");
        helper.assertValueEqual(player.position(), where, "the position the player was standing on");

        helper.succeed();
    }

    /** Items are the half the unit test cannot reach, and the half worth losing sleep over. */
    @GameTest
    public void itemsSurviveTheTripToDiskAndBack(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        player.getInventory().setItem(0, new ItemStack(Items.DIAMOND_SWORD));
        player.getInventory().setItem(38, new ItemStack(Items.IRON_CHESTPLATE));

        PlayerStash stash = PlayerStash.of(player);
        var ops = helper.getLevel().registryAccess().createSerializationContext(JsonOps.INSTANCE);

        var encoded = PlayerStash.CODEC.encodeStart(ops, stash)
                .getOrThrow(error -> new AssertionError("could not write the stash: " + error));
        PlayerStash decoded = PlayerStash.CODEC.parse(ops, encoded)
                .getOrThrow(error -> new AssertionError("could not read the stash back: " + error));

        helper.assertValueEqual(decoded.inventory().size(), 2, "the number of stashed stacks");
        // Restoring the decoded copy rather than comparing records: ItemStack keeps identity
        // equality, so a record comparison would pass or fail for the wrong reason.
        PlayerStash.clear(player);
        decoded.restoreTo(player);

        helper.assertTrue(ItemStack.matches(player.getInventory().getItem(0), new ItemStack(Items.DIAMOND_SWORD)),
                "the sword did not survive the round trip to disk");
        helper.assertTrue(ItemStack.matches(player.getInventory().getItem(38), new ItemStack(Items.IRON_CHESTPLATE)),
                "the chestplate did not survive the round trip to disk");

        helper.succeed();
    }

    /** The ender chest is not a voyage's business, and stashing it would only add a way to lose it. */
    @GameTest
    public void theEnderChestIsLeftAlone(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        player.getEnderChestInventory().setItem(0, new ItemStack(Items.DIAMOND, 5));

        PlayerStash.clear(player);

        helper.assertTrue(ItemStack.matches(player.getEnderChestInventory().getItem(0), new ItemStack(Items.DIAMOND, 5)),
                "entering a voyage emptied the ender chest");

        helper.succeed();
    }
}
