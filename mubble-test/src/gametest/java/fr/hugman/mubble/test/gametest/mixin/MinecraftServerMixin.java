package fr.hugman.mubble.test.gametest.mixin;

import fr.hugman.mubble.test.gametest.support.FlightPolicy;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "allowFlight", at = @At("HEAD"), cancellable = true)
    private void mubble$gametest$denyFlight(CallbackInfoReturnable<Boolean> cir) {
        if (FlightPolicy.denied) {
            cir.setReturnValue(false);
        }
    }
}
