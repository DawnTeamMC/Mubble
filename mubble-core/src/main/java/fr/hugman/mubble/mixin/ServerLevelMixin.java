package fr.hugman.mubble.mixin;

import fr.hugman.mubble.world.level.EnvironmentOverridable;
import fr.hugman.mubble.world.level.WeatherOverridable;
import java.util.List;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.level.saveddata.WeatherData;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Server half of the environment override stack, and the one place a level can own its weather.
 *
 * <p>The server needs the attribute half as well as the client: sky light level feeds lighting
 * propagation and mob spawning, which are server state and cannot be faked in the render path.
 *
 * <p>The attribute half needs no injection — {@link ServerLevel} already exposes a setter for its
 * attribute system, so that part of this mixin only carries state. The setter is marked
 * {@code @VisibleForTesting} (vanilla uses it to swap environments per gametest), so if Mojang ever
 * removes it this breaks at compile time rather than at class load, which is the failure we want.
 */
@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin implements EnvironmentOverridable, WeatherOverridable {
    @Unique
    private List<EnvironmentAttributeMap> mubble$overrides = List.of();

    @Unique
    private @Nullable WeatherData mubble$weather;

    @Override
    public void setEnvironmentOverrides(List<EnvironmentAttributeMap> layers) {
        ServerLevel self = (ServerLevel) (Object) this;
        this.mubble$overrides = List.copyOf(layers);

        EnvironmentAttributeSystem.Builder builder = EnvironmentAttributeSystem.builder().addDefaultLayers(self);
        this.mubble$overrides.forEach(builder::addConstantLayer);
        self.setEnvironmentAttributes(builder.build());

        // Sky light feeds spawning and block light updates, so recompute it now rather than whenever
        // the level next happens to touch it.
        self.updateSkyBrightness();
    }

    @Override
    public List<EnvironmentAttributeMap> getEnvironmentOverrides() {
        return this.mubble$overrides;
    }

    @Override
    public void setOwnWeather(@Nullable WeatherData weather) {
        this.mubble$weather = weather;
    }

    @Override
    public @Nullable WeatherData getOwnWeather() {
        return this.mubble$weather;
    }

    /**
     * The one line that makes weather per level.
     *
     * <p>Vanilla's {@code getWeatherData} returns the server's single instance, and everything else
     * — the weather cycle, {@code /weather}, the rain and thunder ramps — reads it through here. A
     * level with its own is therefore isolated in both directions without touching any of that.
     *
     * <p>Not called from {@code ServerLevel}'s constructor, which reads the server's data directly,
     * so there is no window where this could return a half-built object.
     */
    @Inject(method = "getWeatherData", at = @At("HEAD"), cancellable = true)
    private void useOwnWeather(CallbackInfoReturnable<WeatherData> cir) {
        if (this.mubble$weather != null) {
            cir.setReturnValue(this.mubble$weather);
        }
    }
}
