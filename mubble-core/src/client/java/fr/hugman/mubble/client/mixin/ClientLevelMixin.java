package fr.hugman.mubble.client.mixin;

import fr.hugman.mubble.world.level.EnvironmentOverridable;
import java.util.List;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Client half of the environment override stack.
 *
 * <p>This is the hook that makes a trial look different. Everything on the client that draws sky,
 * fog, cloud or light colour reads them back through {@code environmentAttributes()}, so overriding
 * there covers the whole render path at once — no per-renderer patching, and anything vanilla adds
 * later comes along for free.
 *
 * <p>Rebuilding reuses {@code addEnvironmentAttributeLayers} rather than
 * {@code addDefaultLayers}, so the two layers {@link ClientLevel} adds for lightning flashes survive
 * inside a voyage.
 */
@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin implements EnvironmentOverridable {
    @Shadow
    private EnvironmentAttributeSystem.Builder addEnvironmentAttributeLayers(EnvironmentAttributeSystem.Builder builder) {
        throw new AssertionError("shadow");
    }

    @Unique
    private List<EnvironmentAttributeMap> mubble$overrides = List.of();
    @Unique
    private @Nullable EnvironmentAttributeSystem mubble$overriddenAttributes;

    @Override
    public void setEnvironmentOverrides(List<EnvironmentAttributeMap> layers) {
        this.mubble$overrides = List.copyOf(layers);

        if (this.mubble$overrides.isEmpty()) {
            this.mubble$overriddenAttributes = null;
        } else {
            EnvironmentAttributeSystem.Builder builder = this.addEnvironmentAttributeLayers(EnvironmentAttributeSystem.builder());
            this.mubble$overrides.forEach(builder::addConstantLayer);
            this.mubble$overriddenAttributes = builder.build();
        }

        ((Level) (Object) this).updateSkyBrightness();
    }

    @Override
    public List<EnvironmentAttributeMap> getEnvironmentOverrides() {
        return this.mubble$overrides;
    }

    // Fully qualified on purpose: environmentAttributes() has a covariant bridge overload returning
    // EnvironmentAttributeReader, and injecting into both would run this twice.
    @Inject(
            method = "environmentAttributes()Lnet/minecraft/world/attribute/EnvironmentAttributeSystem;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void mubble$applyOverrides(CallbackInfoReturnable<EnvironmentAttributeSystem> cir) {
        if (this.mubble$overriddenAttributes != null) {
            cir.setReturnValue(this.mubble$overriddenAttributes);
        }
    }
}
