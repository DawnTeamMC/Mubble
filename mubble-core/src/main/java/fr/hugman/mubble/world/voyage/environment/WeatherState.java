package fr.hugman.mubble.world.voyage.environment;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

/**
 * The weather a trial asks for.
 *
 * <p>Not an environment attribute. Weather in 26.x is server state driven by
 * {@link net.minecraft.world.level.saveddata.WeatherData}, and it contributes its own layers to the
 * attribute system rather than being one — so a profile cannot express it the way it expresses a sky
 * colour. See {@code docs/environment-profiles.md}.
 */
public enum WeatherState implements StringRepresentable {
    CLEAR("clear"),
    RAIN("rain"),
    THUNDER("thunder");

    public static final Codec<WeatherState> CODEC = StringRepresentable.fromEnum(WeatherState::values);

    private final String name;

    WeatherState(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
