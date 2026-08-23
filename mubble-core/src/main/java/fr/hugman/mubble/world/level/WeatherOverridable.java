package fr.hugman.mubble.world.level;

import net.minecraft.world.level.saveddata.WeatherData;
import org.jspecify.annotations.Nullable;

/**
 * A level that keeps its own weather instead of the server's.
 *
 * <p>Injected onto {@link net.minecraft.server.level.ServerLevel}. Weather in 26.2 is one
 * {@link WeatherData} owned by {@link net.minecraft.server.MinecraftServer}, which every level reads
 * through {@code ServerLevel#getWeatherData}. Levels already keep their own rain and thunder
 * <em>levels</em>, and Fantasy already scopes the rain packets to the dimension they came from — so
 * the single shared object is the only thing making weather global, and handing a level its own is
 * the whole fix.
 *
 * <p>A level with its own weather is fully isolated in both directions: a storm in a trial does not
 * rain on someone's overworld build, and {@code /weather} in the overworld does not reach into a
 * trial.
 *
 * <p>The data is deliberately not saved. It is created for a level that is itself temporary, and
 * writing it to disk would leave weather files behind for dimensions that no longer exist.
 */
public interface WeatherOverridable {
    /**
     * Gives this level its own weather, or {@code null} to put it back on the server's.
     *
     * <p>Call this before anything reads the level's weather. A fresh {@link WeatherData} is clear,
     * which is the right thing for a level that has just been created.
     */
    default void setOwnWeather(@Nullable WeatherData weather) {
    }

    /** {@return this level's own weather, or {@code null} if it shares the server's} */
    default @Nullable WeatherData getOwnWeather() {
        return null;
    }
}
