package fr.hugman.mubble.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
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
    private MinecraftClient client;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/LayeredDrawer;addLayer(Lnet/minecraft/client/gui/LayeredDrawer$Layer;)Lnet/minecraft/client/gui/LayeredDrawer;", ordinal = 6))
    private void mubble$addPowerUpLayer(MinecraftClient client, CallbackInfo ci, @Local(ordinal = 0) LocalRef<LayeredDrawer> layeredDrawer) {
        //TODO: create event and add layer with it
        layeredDrawer.set(layeredDrawer.get().addLayer((context, tickCounter) -> PowerUpHudRendering.renderPowerUpLayer(this.client, context, tickCounter)));
    }
}