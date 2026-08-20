package fr.hugman.mubble.mixin;

import fr.hugman.mubble.world.level.EnvironmentOverridable;
import java.util.List;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * Server half of the environment override stack.
 *
 * <p>The server needs this as well as the client: sky light level feeds lighting propagation and mob
 * spawning, which are server state and cannot be faked in the render path.
 *
 * <p>No injection here — {@link ServerLevel} already exposes a setter for its attribute system, so
 * this mixin exists only to carry the state and satisfy the injected interface. The setter is marked
 * {@code @VisibleForTesting} (vanilla uses it to swap environments per gametest), so if Mojang ever
 * removes it this breaks at compile time rather than at class load, which is the failure we want.
 */
@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin implements EnvironmentOverridable {
    @Unique
    private List<EnvironmentAttributeMap> mubble$overrides = List.of();

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
}
