package fr.hugman.mubble.test.gametest.voyage;

import fr.hugman.mubble.world.level.WeatherOverridable;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.WeatherData;

/**
 * Per-level weather.
 *
 * <p>Weather is one object on the server in 26.2, shared by every level, so a trial asking for a
 * storm used to rain on everyone. Trial levels are handed their own; this checks the seam that makes
 * that work, without opening a runtime dimension.
 *
 * <p>Nothing here writes to the server's weather. Doing so would change it for every other test
 * running alongside this one, which is the same blast radius the fix is about.
 */
public class LevelWeatherGameTest {
    @GameTest
    public void aLevelSharesTheServersWeatherByDefault(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();

        helper.assertTrue(((WeatherOverridable) level).getOwnWeather() == null,
                "an ordinary level should not own its weather");
        helper.assertTrue(level.getWeatherData() == level.getServer().getWeatherData(),
                "an ordinary level should read the server's weather");

        helper.succeed();
    }

    @GameTest
    public void aLevelGivenItsOwnWeatherIsIsolated(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        WeatherData server = level.getServer().getWeatherData();
        boolean serverWasRaining = server.isRaining();
        boolean serverWasThundering = server.isThundering();
        WeatherData own = new WeatherData();

        try {
            ((WeatherOverridable) level).setOwnWeather(own);

            helper.assertTrue(level.getWeatherData() == own, "the level did not pick up its own weather");
            helper.assertTrue(level.getWeatherData() != server, "the level is still reading the server's weather");

            // The point of the whole change: a storm here must not be a storm everywhere.
            level.getWeatherData().setRaining(true);
            level.getWeatherData().setThundering(true);
            helper.assertValueEqual(server.isRaining(), serverWasRaining,
                    "the server's rain after one level started a storm");
            helper.assertValueEqual(server.isThundering(), serverWasThundering,
                    "the server's thunder after one level started a storm");
        } finally {
            ((WeatherOverridable) level).setOwnWeather(null);
        }

        helper.assertTrue(level.getWeatherData() == server, "the level did not go back to the server's weather");

        helper.succeed();
    }
}
