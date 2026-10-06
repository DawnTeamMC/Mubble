package fr.hugman.mubble.test.gametest.collectible;

import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import fr.hugman.mubble.world.entity.item.collectible.CollectibleEntity;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

/**
 * Coins, and everything else picked up by walking into it rather than by touching an item entity.
 */
public class CollectibleEntityGameTest {
    private static final BlockPos SPOT = new BlockPos(4, Arena.FLOOR_Y + 1, 3);

    @GameTest
    public void acollectibleCarriesItsItem(GameTestHelper helper) {
        var collectible = spawn(helper, new ItemStack(Items.GOLD_INGOT, 3));

        helper.assertTrue(collectible.getItem().is(Items.GOLD_INGOT), "the collectible lost the item it was made with");
        helper.assertValueEqual(collectible.getItem().getCount(), 3, "the amount it carries");

        helper.succeed();
    }

    @GameTest
    public void collectingHandsTheItemOver(GameTestHelper helper) {
        var collectible = spawn(helper, new ItemStack(Items.GOLD_INGOT, 3));
        var player = TestPlayers.at(helper, SPOT);

        collectible.collect(player);

        helper.assertTrue(player.getInventory().contains(new ItemStack(Items.GOLD_INGOT)),
                "the player walked away empty handed");
        helper.assertTrue(collectible.isRemoved(), "the collectible stayed behind after being picked up");

        helper.succeed();
    }

    @GameTest
    public void theWholeStackIsCredited(GameTestHelper helper) {
        var collectible = spawn(helper, new ItemStack(Items.GOLD_INGOT, 5));
        var player = TestPlayers.at(helper, SPOT);

        collectible.collect(player);

        int total = 0;
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            var inSlot = player.getInventory().getItem(slot);
            if (inSlot.is(Items.GOLD_INGOT)) {
                total += inSlot.getCount();
            }
        }

        helper.assertValueEqual(total, 5, "the amount that ended up in the inventory");
        helper.succeed();
    }

    /** A player walking into one picks it up through the player tick, without any interaction. */
    @GameTest(maxTicks = 100)
    public void walkingIntoOnePicksItUp(GameTestHelper helper) {
        var collectible = spawn(helper, new ItemStack(Items.GOLD_INGOT));
        var player = TestPlayers.at(helper, SPOT);

        helper.succeedWhen(() -> {
            TestPlayers.tick(player);
            helper.assertTrue(collectible.isRemoved(), "standing on a collectible did not pick it up");
        });
    }

    @GameTest
    public void afixedCollectibleStaysPut(GameTestHelper helper) {
        var collectible = spawn(helper, new ItemStack(Items.GOLD_INGOT));

        collectible.setFixed(true);
        helper.assertTrue(collectible.isFixed(), "the fixed flag did not stick");

        collectible.setFixed(false);
        helper.assertFalse(collectible.isFixed(), "the fixed flag did not come back off");

        helper.succeed();
    }

    /** Where a collectible goes when a block spits it out: nowhere, if something is already there. */
    @GameTest
    public void placePosRefusesAnOccupiedSpot(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var free = helper.absolutePos(SPOT);
        var blocked = helper.absolutePos(SPOT.east());
        helper.setBlock(SPOT.east(), Blocks.STONE);

        helper.assertTrue(CollectibleEntity.placePos(helper.getLevel(), free) != null, "an empty spot should be usable");
        helper.assertTrue(CollectibleEntity.placePos(helper.getLevel(), blocked) == null, "a solid block is not a place for a collectible");

        helper.succeed();
    }

    private static CollectibleEntity spawn(GameTestHelper helper, ItemStack stack) {
        Arena.buildFloor(helper);
        var collectible = helper.spawn(MubbleEntityTypes.COLLECTIBLE, SPOT);
        collectible.setItem(stack);
        return collectible;
    }
}
