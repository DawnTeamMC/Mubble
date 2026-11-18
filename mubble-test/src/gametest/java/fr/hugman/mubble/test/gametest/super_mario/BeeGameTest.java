package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.tags.MubblePowerUpTags;
import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpIds;
import fr.hugman.mubble.super_mario.tags.SuperMarioPowerUpTags;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.item.SuperMarioItems;
import fr.hugman.mubble.test.gametest.datapack.PowerUpFixtures;
import fr.hugman.mubble.test.gametest.support.AirPlayers;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import fr.hugman.mubble.world.entity.item.collectible.CollectibleEntity;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static fr.hugman.mubble.test.gametest.support.AirPlayers.JUMP_HELD;
import static fr.hugman.mubble.test.gametest.support.AirPlayers.STAND;

/**
 * The Bee Mushroom power-up as shipped: what it grants, what takes it away, and the refills the Super Mario
 * module prices for coins and stomps. The generic flight and cling are covered on a fixture elsewhere.
 */
public class BeeGameTest {
    private static final int COIN_REFILL = 15;
    private static final int STOMP_REFILL = 60;
    private static final int FLIGHT_TICKS = 80;
    private static final BlockPos GROUND = new BlockPos(4, Arena.FLOOR_Y + 1, 3);
    private static final List<Block> HONEY_BLOCKS = List.of(Blocks.HONEY_BLOCK, Blocks.HONEYCOMB_BLOCK, Blocks.BEEHIVE, Blocks.BEE_NEST);

    @GameTest
    public void theMushroomGivesTheBee(GameTestHelper helper) {
        var player = TestPlayers.inLevel(helper);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(SuperMarioItems.BEE_MUSHROOM));

        SuperMarioItems.BEE_MUSHROOM.use(helper.getLevel(), player, InteractionHand.MAIN_HAND);

        helper.assertTrue(player.getPowerUp().orElseThrow().is(SuperMarioPowerUpIds.BEE), "using the mushroom gave nothing");
        helper.succeed();
    }

    @GameTest
    public void theBeeIsPureMobility(GameTestHelper helper) {
        var bee = PowerUpFixtures.get(helper, SuperMarioPowerUpIds.BEE);

        helper.assertTrue(bee.value().action().isEmpty(), "the bee should have no action");
        helper.assertTrue(bee.value().abilities().flight().isPresent(), "the bee should fly");
        helper.assertTrue(bee.value().abilities().cling().isPresent(), "the bee should cling");
        helper.assertTrue(bee.value().abilities().flight().orElseThrow().gauge() == 100, "the bee should fly for five seconds");
        helper.succeed();
    }

    @GameTest
    public void theBeeIsLightLostToWaterAndRainAndHasAStinger(GameTestHelper helper) {
        var bee = PowerUpFixtures.get(helper, SuperMarioPowerUpIds.BEE);

        helper.assertTrue(bee.is(MubblePowerUpTags.LIGHT_FOOTED), "the bee should be light footed");
        helper.assertTrue(bee.is(MubblePowerUpTags.LOST_TO_WATER), "the bee should be lost to water");
        helper.assertTrue(bee.is(MubblePowerUpTags.LOST_TO_RAIN), "the bee should be lost to rain");
        helper.assertTrue(bee.is(SuperMarioPowerUpTags.HAS_STINGER), "the bee should wear a stinger");
        helper.succeed();
    }

    @GameTest
    public void theBeeClingsToEveryHoneyBlock(GameTestHelper helper) {
        var cling = PowerUpFixtures.get(helper, SuperMarioPowerUpIds.BEE).value().abilities().cling().orElseThrow();

        for (var block : HONEY_BLOCKS) {
            helper.assertTrue(cling.blocks().contains(block.builtInRegistryHolder()), "the bee should cling to " + block);
        }
        helper.assertFalse(cling.blocks().contains(Blocks.STONE.builtInRegistryHolder()), "the bee should not cling to stone");
        helper.succeed();
    }

    @GameTest
    public void theBeeFlies(GameTestHelper helper) {
        var player = AirPlayers.jumper(helper, SuperMarioPowerUpIds.BEE);

        AirPlayers.tick(player, JUMP_HELD, 3);

        helper.assertTrue(player.isFlying(), "a bee holding the jump key should fly");
        helper.succeed();
    }

    @GameTest
    public void theBeeIsLostInWater(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(STAND, Blocks.WATER);
        var player = TestPlayers.at(helper, STAND);
        player.setPowerUp(PowerUpFixtures.get(helper, SuperMarioPowerUpIds.BEE));

        helper.succeedWhen(() -> {
            TestPlayers.tick(player);
            helper.assertTrue(player.getPowerUp().isEmpty(), "the bee survived a swim");
        });
    }

    @GameTest
    public void theBeeIsLostInTheRain(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Arena.openTheSky(helper, STAND);
        helper.getLevel().setRainLevel(1.0F);
        var player = TestPlayers.at(helper, STAND);
        player.setPowerUp(PowerUpFixtures.get(helper, SuperMarioPowerUpIds.BEE));

        helper.succeedWhen(() -> {
            TestPlayers.tick(player);
            helper.assertTrue(player.getPowerUp().isEmpty(), "the bee survived a downpour");
        });
    }

    @GameTest
    public void aCoinRefillsTheGauge(GameTestHelper helper) {
        var player = tiredBee(helper);
        int before = player.getFlightGauge();

        collectible(helper, new ItemStack(SuperMarioItems.COIN)).collect(player);

        helper.assertValueEqual(player.getFlightGauge(), before + COIN_REFILL, "the gauge after a coin");
        helper.succeed();
    }

    @GameTest
    public void anythingButACoinRefillsNothing(GameTestHelper helper) {
        var player = tiredBee(helper);
        int before = player.getFlightGauge();

        collectible(helper, new ItemStack(Items.GOLD_INGOT)).collect(player);

        helper.assertValueEqual(player.getFlightGauge(), before, "the gauge after something that is not a coin");
        helper.succeed();
    }

    @GameTest
    public void aStompRefillsTheGauge(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var goomba = helper.spawnWithNoFreeWill(SuperMarioEntityTypes.GOOMBA, GROUND);
        var player = tiredBee(helper);
        int before = player.getFlightGauge();
        player.teleportTo(helper.absolutePos(GROUND).getX() + 0.5D, helper.absolutePos(GROUND).getY() + 1.0D, helper.absolutePos(GROUND).getZ() + 0.5D);
        player.setOnGround(false);
        player.setDeltaMovement(new Vec3(0.0D, -0.2D, 0.0D));

        goomba.tick();

        helper.assertValueEqual(player.getFlightGauge(), before + STOMP_REFILL, "the gauge after a stomp");
        helper.succeed();
    }

    @GameTest
    public void aLadderRefillsNothing(GameTestHelper helper) {
        Arena.buildFloor(helper);
        for (int y = Arena.FLOOR_Y + 1; y < Arena.FLOOR_Y + 7; y++) {
            helper.setBlock(new BlockPos(1, y, 3), Blocks.STONE);
            helper.setBlock(new BlockPos(2, y, 3), Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.EAST));
        }
        var player = tiredBee(helper);
        int before = player.getFlightGauge();
        Vec3 onTheLadder = helper.absoluteVec(new Vec3(2.5D, Arena.FLOOR_Y + 5.0D, 3.5D));
        player.teleportTo(onTheLadder.x(), onTheLadder.y(), onTheLadder.z());
        player.setDeltaMovement(Vec3.ZERO);
        player.setOnGround(false);

        AirPlayers.tick(player, Input.EMPTY, 5);

        helper.assertTrue(player.onClimbable() && !player.onGround(), "the bee is not on the ladder, the test proves nothing");
        helper.assertValueEqual(player.getFlightGauge(), before, "the gauge after five ticks on a ladder");
        helper.succeed();
    }

    /** A bee that has flown for a while, so that every refill has room to show. */
    private static ServerPlayer tiredBee(GameTestHelper helper) {
        var player = AirPlayers.jumper(helper, SuperMarioPowerUpIds.BEE);
        AirPlayers.tick(player, JUMP_HELD, FLIGHT_TICKS);
        return player;
    }

    private static CollectibleEntity collectible(GameTestHelper helper, ItemStack stack) {
        var collectible = helper.spawn(MubbleEntityTypes.COLLECTIBLE, GROUND);
        collectible.setItem(stack);
        collectible.setFixed(true);
        return collectible;
    }
}
