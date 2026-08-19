package fr.hugman.mubble.test.gametest.power_up;

import fr.hugman.mubble.test.gametest.datapack.PowerUpFixtures;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;

/**
 * The two ways the world takes a power-up away, both driven from the player tick and both keyed on a
 * tag, so a data pack decides which power-ups are fragile.
 * <p>
 * The mock players are not part of the level's tick loop, so the tests tick them by hand, once per
 * game tick: the "in water" flag only settles after the player has moved a little.
 */
public class PowerUpLossGameTest {
    private static final BlockPos STAND = new BlockPos(4, Arena.FLOOR_Y + 1, 3);
    private static final int SETTLE_TICKS = 20;

    @GameTest
    public void aTaggedPowerUpIsLostInWater(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(STAND, Blocks.WATER);

        var player = TestPlayers.at(helper, STAND);
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.LOST_TO_WATER));
        helper.assertTrue(player.getPowerUp().isPresent(), "the player never got the power-up");

        helper.succeedWhen(() -> {
            TestPlayers.tick(player);
            helper.assertTrue(player.isInWater(), "the player is not in the water yet");
            helper.assertTrue(player.getPowerUp().isEmpty(), "a power-up tagged lost_to_water survived a swim");
        });
    }

    @GameTest(maxTicks = 100)
    public void anUntaggedPowerUpSurvivesWater(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(STAND, Blocks.WATER);

        var player = TestPlayers.at(helper, STAND);
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.EMPTY));

        helper.startSequence()
                .thenExecuteFor(SETTLE_TICKS, () -> TestPlayers.tick(player))
                .thenExecute(() -> {
                    helper.assertTrue(player.isInWater(), "the player never made it into the water, the test proves nothing");
                    helper.assertTrue(player.getPowerUp().isPresent(), "an untagged power-up should not be washed away");
                })
                .thenSucceed();
    }

    @GameTest
    public void aTaggedPowerUpIsLostInTheRain(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Arena.openTheSky(helper, STAND);
        helper.getLevel().setRainLevel(1.0F);

        var player = TestPlayers.at(helper, STAND);
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.LOST_TO_RAIN));

        helper.assertTrue(helper.getLevel().isRainingAt(player.blockPosition()),
                "it is not raining on the player, the test cannot tell anything");

        helper.succeedWhen(() -> {
            TestPlayers.tick(player);
            helper.assertTrue(player.getPowerUp().isEmpty(), "a power-up tagged lost_to_rain survived a downpour");
        });
    }

    @GameTest(maxTicks = 100)
    public void aTaggedPowerUpSurvivesUnderARoof(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Arena.openTheSky(helper, STAND);
        helper.getLevel().setRainLevel(1.0F);
        helper.setBlock(STAND.above(2), Blocks.STONE);

        var player = TestPlayers.at(helper, STAND);
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.LOST_TO_RAIN));

        helper.assertFalse(helper.getLevel().isRainingAt(player.blockPosition()), "the roof is not keeping the rain out");

        helper.startSequence()
                .thenExecuteFor(SETTLE_TICKS, () -> TestPlayers.tick(player))
                .thenExecute(() -> helper.assertTrue(player.getPowerUp().isPresent(), "a roof should keep the rain off the power-up"))
                .thenSucceed();
    }

    @GameTest(maxTicks = 100)
    public void anUntaggedPowerUpSurvivesTheRain(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Arena.openTheSky(helper, STAND);
        helper.getLevel().setRainLevel(1.0F);

        var player = TestPlayers.at(helper, STAND);
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.LOST_TO_WATER));

        helper.startSequence()
                .thenExecuteFor(SETTLE_TICKS, () -> TestPlayers.tick(player))
                .thenExecute(() -> helper.assertTrue(player.getPowerUp().isPresent(),
                        "only the power-ups tagged lost_to_rain should mind the weather"))
                .thenSucceed();
    }
}
