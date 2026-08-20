package fr.hugman.mubble.test.gametest.power_up;

import fr.hugman.mubble.test.gametest.datapack.PowerUpFixtures;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;

/**
 * A power-up has to survive logging out. It is written by the player mixin into the entity data, so
 * a change to either the codec or the keys silently loses whatever players were carrying.
 */
public class PowerUpPersistenceGameTest {
    @GameTest
    public void aPowerUpSurvivesSaveAndLoad(GameTestHelper helper) {
        var saved = TestPlayers.mock(helper);
        saved.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.SHOOTER));

        var loaded = reload(helper, saved);

        helper.assertTrue(loaded.getPowerUp().isPresent(), "the power-up was lost on the way through the save file");
        helper.assertTrue(loaded.getPowerUp().orElseThrow().is(PowerUpFixtures.SHOOTER), "a different power-up came back");

        helper.succeed();
    }

    @GameTest
    public void theChargesSurviveSaveAndLoad(GameTestHelper helper) {
        var saved = TestPlayers.mock(helper);
        saved.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.SHOOTER));
        // Spend one of the two charges, so that a reset to full would show.
        saved.getPowerUpProperties().addEntity(java.util.UUID.randomUUID());
        int spent = saved.getPowerUpProperties().getChargeCount();

        var loaded = reload(helper, saved);

        var properties = loaded.getPowerUpProperties();
        helper.assertTrue(properties != null, "the charge properties were lost on the way");
        helper.assertValueEqual(properties.getChargeCount(), spent, "the charge count came back changed");

        helper.succeed();
    }

    @GameTest
    public void aPlayerWithoutAPowerUpComesBackWithoutOne(GameTestHelper helper) {
        var loaded = reload(helper, TestPlayers.mock(helper));

        helper.assertTrue(loaded.getPowerUp().isEmpty(), "a power-up appeared out of an empty save");
        helper.succeed();
    }

    /** Writes {@code player} out the way the game does at logout, and reads it back into a new one. */
    private static Player reload(GameTestHelper helper, Player player) {
        var registries = helper.getLevel().registryAccess();

        var output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registries);
        player.saveWithoutId(output);

        var reloaded = TestPlayers.mock(helper);
        reloaded.load(TagValueInput.create(ProblemReporter.DISCARDING, registries, output.buildResult()));
        return reloaded;
    }
}
