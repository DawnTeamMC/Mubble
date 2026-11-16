package fr.hugman.mubble.test.gametest.power_up;

import fr.hugman.mubble.test.gametest.datapack.PowerUpFixtures;
import fr.hugman.mubble.test.gametest.support.AirPlayers;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Tilt;
import fr.hugman.mubble.world.power_up.PowerUp;

/**
 * What a holder of a power-up tagged {@code mubble:light_footed} does not disturb, each against a plain
 * player doing the same thing, and what they still do.
 */
public class LightFootedGameTest {
    private static final BlockPos FLOOR = new BlockPos(4, Arena.FLOOR_Y, 3);
    private static final double HIGH_FALL = 10.0D;
    /** Enough tries for a random one-in-three or one-in-a-hundred chance to have come up. */
    private static final int TRIES = 2000;
    private static final int SETTLE_TICKS = 20;

    @GameTest
    public void aPlainPlayerTramplesFarmland(GameTestHelper helper) {
        var player = standingOn(helper, Blocks.FARMLAND, PowerUpFixtures.EMPTY);

        land(helper, player);

        helper.assertTrue(helper.getBlockState(FLOOR).is(Blocks.DIRT), "the control should have trampled the farmland, the test proves nothing");
        helper.succeed();
    }

    @GameTest
    public void aLightFootedPlayerSparesFarmland(GameTestHelper helper) {
        var player = standingOn(helper, Blocks.FARMLAND, PowerUpFixtures.LIGHT_FOOTED);

        land(helper, player);

        helper.assertTrue(helper.getBlockState(FLOOR).is(Blocks.FARMLAND), "a light footed player trampled the farmland");
        helper.succeed();
    }

    @GameTest
    public void aPlainPlayerCrushesTurtleEggs(GameTestHelper helper) {
        var player = standingOn(helper, Blocks.TURTLE_EGG, PowerUpFixtures.EMPTY);

        stepOnTheEgg(helper, player);

        helper.assertTrue(helper.getBlockState(FLOOR).isAir(), "the control should have crushed the egg, the test proves nothing");
        helper.succeed();
    }

    @GameTest
    public void aLightFootedPlayerSparesTurtleEggs(GameTestHelper helper) {
        var player = standingOn(helper, Blocks.TURTLE_EGG, PowerUpFixtures.LIGHT_FOOTED);

        stepOnTheEgg(helper, player);

        helper.assertTrue(helper.getBlockState(FLOOR).is(Blocks.TURTLE_EGG), "a light footed player crushed a turtle egg");
        helper.succeed();
    }

    @GameTest
    public void aPlainPlayerTiltsABigDripleaf(GameTestHelper helper) {
        var player = standingOnTheLeaf(helper, PowerUpFixtures.EMPTY);

        AirPlayers.tick(player, Input.EMPTY, 3);

        helper.assertTrue(helper.getBlockState(FLOOR).getValue(BlockStateProperties.TILT) != Tilt.NONE,
                "the control should have tilted the leaf, the test proves nothing");
        helper.succeed();
    }

    @GameTest
    public void aLightFootedPlayerLeavesABigDripleafLevel(GameTestHelper helper) {
        var player = standingOnTheLeaf(helper, PowerUpFixtures.LIGHT_FOOTED);

        AirPlayers.tick(player, Input.EMPTY, 3);

        helper.assertTrue(helper.getBlockState(FLOOR).getValue(BlockStateProperties.TILT) == Tilt.NONE, "a light footed player tilted the leaf");
        helper.succeed();
    }

    @GameTest
    public void aPlainPlayerSinksInPowderSnow(GameTestHelper helper) {
        var player = standingOn(helper, Blocks.POWDER_SNOW, PowerUpFixtures.EMPTY);

        AirPlayers.tick(player, Input.EMPTY, SETTLE_TICKS);

        helper.assertTrue(player.getY() < helper.absolutePos(FLOOR).getY() + 1.0D - 0.2D, "the control should have sunk, the test proves nothing");
        helper.succeed();
    }

    @GameTest
    public void aLightFootedPlayerWalksOnPowderSnow(GameTestHelper helper) {
        var player = standingOn(helper, Blocks.POWDER_SNOW, PowerUpFixtures.LIGHT_FOOTED);

        AirPlayers.tick(player, Input.EMPTY, SETTLE_TICKS);

        helper.assertTrue(player.onGround(), "a light footed player should stand on powder snow");
        helper.assertTrue(player.getY() >= helper.absolutePos(FLOOR).getY() + 1.0D - 0.1D, "a light footed player sank into powder snow, was at " + player.getY());
        helper.succeed();
    }

    @GameTest
    public void aLightFootedPlayerStillPressesAPressurePlate(GameTestHelper helper) {
        var player = standingOn(helper, Blocks.AIR, PowerUpFixtures.LIGHT_FOOTED);
        var plate = FLOOR.above();
        helper.setBlock(plate, Blocks.STONE_PRESSURE_PLATE);

        AirPlayers.tick(player, Input.EMPTY, 3);

        helper.assertTrue(helper.getBlockState(plate).getValue(BlockStateProperties.POWERED), "a light footed player should still press a pressure plate");
        helper.succeed();
    }

    /** A player standing on {@code block}, which replaces the floor under their feet. */
    private static ServerPlayer standingOn(GameTestHelper helper, Block block, ResourceKey<PowerUp> powerUp) {
        Arena.buildFloor(helper);
        helper.setBlock(FLOOR, block);
        var player = TestPlayers.at(helper, FLOOR.above());
        player.setPowerUp(PowerUpFixtures.get(helper, powerUp));
        return player;
    }

    private static ServerPlayer standingOnTheLeaf(GameTestHelper helper, ResourceKey<PowerUp> powerUp) {
        var player = standingOn(helper, Blocks.BIG_DRIPLEAF, powerUp);
        player.teleportTo(player.getX(), helper.absolutePos(FLOOR).getY() + 0.9375D, player.getZ());
        player.setOnGround(true);
        return player;
    }

    private static void land(GameTestHelper helper, ServerPlayer player) {
        var pos = helper.absolutePos(FLOOR);
        var state = helper.getBlockState(FLOOR);
        state.getBlock().fallOn(helper.getLevel(), state, pos, player, HIGH_FALL);
    }

    private static void stepOnTheEgg(GameTestHelper helper, ServerPlayer player) {
        var pos = helper.absolutePos(FLOOR);
        for (int attempt = 0; attempt < TRIES && helper.getBlockState(FLOOR).is(Blocks.TURTLE_EGG); attempt++) {
            var state = helper.getBlockState(FLOOR);
            state.getBlock().stepOn(helper.getLevel(), pos, state, player);
        }
    }
}
