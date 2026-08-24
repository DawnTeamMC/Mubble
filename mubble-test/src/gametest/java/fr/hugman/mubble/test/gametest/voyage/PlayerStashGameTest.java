package fr.hugman.mubble.test.gametest.voyage;

import fr.hugman.mubble.test.gametest.datapack.PowerUpFixtures;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import fr.hugman.mubble.world.power_up.PowerUpHolder;
import fr.hugman.mubble.world.voyage.session.PlayerStash;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

/**
 * Taking a player apart and putting them back together.
 *
 * <p>The codec is covered by a unit test; this covers the half that matters more, which is reading
 * state off a real player and restoring it. Item stacks cannot even be constructed without a server,
 * so everything involving them lives here too.
 *
 * <p>Game mode is not asserted anywhere. The framework's mock player hard-codes {@code gameMode()}
 * to creative, so a round trip through it would prove nothing.
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
                "the chestplate did not come back — equipment is not being stashed");
        helper.assertValueEqual(inventory.getSelectedSlot(), 4, "the held slot");
        helper.assertValueEqual(player.getHealth(), 17.5F, "health");
        helper.assertValueEqual(player.getFoodData().getFoodLevel(), 14, "hunger");
        helper.assertValueEqual(player.totalExperience, 394, "total experience");
        helper.assertTrue(player.hasEffect(MobEffects.SPEED), "the speed effect did not come back");
        helper.assertValueEqual(player.position(), where, "the position the player was standing on");

        helper.succeed();
    }

    /**
     * The reason the stash is the player's whole save tag rather than a list of fields.
     *
     * <p>A power-up is Mubble's own state, persisted the ordinary way. An enumerated stash walked
     * straight past it, so it survived into a trial and back out; any other mod's state would have
     * done the same.
     */
    @GameTest
    public void aPowerUpIsTakenAwayAndGivenBack(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        PowerUpHolder holder = (PowerUpHolder) player;
        holder.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.ATTRIBUTES_ONLY));

        PlayerStash stash = PlayerStash.of(player);
        PlayerStash.clear(player);

        helper.assertTrue(holder.getPowerUp().isEmpty(), "the power-up came into the trial with the player");

        stash.restoreTo(player);

        helper.assertTrue(holder.getPowerUp().isPresent(), "the power-up did not come back after the voyage");
        helper.assertValueEqual(holder.getPowerUp().orElseThrow().unwrapKey().orElseThrow(),
                PowerUpFixtures.ATTRIBUTES_ONLY, "the power-up that came back");

        helper.succeed();
    }

    /**
     * The other direction, and the reason restoring clears first instead of only loading.
     *
     * <p>The snapshot of a player with no power-up has no key for one, and the reader ignores absent
     * keys — so loading alone would leave a power-up picked up mid-voyage on the player afterwards.
     */
    @GameTest
    public void aPowerUpGainedInAVoyageDoesNotEscape(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        PowerUpHolder holder = (PowerUpHolder) player;
        helper.assertTrue(holder.getPowerUp().isEmpty(), "the player started with a power-up");

        PlayerStash stash = PlayerStash.of(player);
        PlayerStash.clear(player);
        holder.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.ATTRIBUTES_ONLY));

        stash.restoreTo(player);

        helper.assertTrue(holder.getPowerUp().isEmpty(), "a power-up gained inside a voyage escaped it");

        helper.succeed();
    }

    /** Items are the half the unit test cannot reach, and the half worth losing sleep over. */
    @GameTest
    public void itemsSurviveTheTripToDiskAndBack(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        player.getInventory().setItem(0, new ItemStack(Items.DIAMOND_SWORD));
        player.getInventory().setItem(38, new ItemStack(Items.IRON_CHESTPLATE));

        PlayerStash stash = PlayerStash.of(player);
        var encoded = PlayerStash.CODEC.encodeStart(NbtOps.INSTANCE, stash)
                .getOrThrow(error -> new AssertionError("could not write the stash: " + error));
        PlayerStash decoded = PlayerStash.CODEC.parse(NbtOps.INSTANCE, encoded)
                .getOrThrow(error -> new AssertionError("could not read the stash back: " + error));

        PlayerStash.clear(player);
        decoded.restoreTo(player);

        helper.assertTrue(ItemStack.matches(player.getInventory().getItem(0), new ItemStack(Items.DIAMOND_SWORD)),
                "the sword did not survive the round trip to disk");
        helper.assertTrue(ItemStack.matches(player.getInventory().getItem(38), new ItemStack(Items.IRON_CHESTPLATE)),
                "the chestplate did not survive the round trip to disk");

        helper.succeed();
    }

    /** The ender chest is not a voyage's business, and rolling it back would delete things. */
    @GameTest
    public void theEnderChestIsLeftAlone(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.inLevel(helper);
        player.getEnderChestInventory().setItem(0, new ItemStack(Items.DIAMOND, 5));

        PlayerStash stash = PlayerStash.of(player);
        PlayerStash.clear(player);

        helper.assertTrue(ItemStack.matches(player.getEnderChestInventory().getItem(0), new ItemStack(Items.DIAMOND, 5)),
                "entering a voyage emptied the ender chest");

        // Put something else in mid-voyage: restoring must not roll the ender chest back either.
        player.getEnderChestInventory().setItem(1, new ItemStack(Items.EMERALD, 3));
        stash.restoreTo(player);

        helper.assertTrue(ItemStack.matches(player.getEnderChestInventory().getItem(1), new ItemStack(Items.EMERALD, 3)),
                "leaving a voyage rolled the ender chest back and deleted what was put in it");

        helper.succeed();
    }
}
