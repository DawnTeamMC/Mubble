package fr.hugman.mubble.mixin.client;

import fr.hugman.mubble.client.gui.hud.PowerUpHudRendering;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.LayeredDrawer;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Final
    @Shadow
    private LayeredDrawer layeredDrawer;
    @Final
    @Shadow
    private MinecraftClient client;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/LayeredDrawer;addSubDrawer(Lnet/minecraft/client/gui/LayeredDrawer;Ljava/util/function/BooleanSupplier;)Lnet/minecraft/client/gui/LayeredDrawer;", ordinal = 0))
    private void addCustomLayer(MinecraftClient client, CallbackInfo ci) {
        this.layeredDrawer.addLayer((context, tickCounter) -> PowerUpHudRendering.renderPowerUpLayer(this.client, context, tickCounter));
    }
}