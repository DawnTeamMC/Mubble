package fr.hugman.mubble.test.gametest.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import fr.hugman.mubble.test.gametest.support.TestFlight;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/**
 * The test server allows flight, so it never kicks anyone for floating. A dedicated server does not,
 * and that is the setting the arcade layer has to hold up under: {@link TestFlight} turns it back on
 * for the players of the tests that ask for it, and for them only.
 */
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplFlightMixin {
    @Shadow
    public ServerPlayer player;

    @ModifyExpressionValue(method = "handleMovePlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;allowFlight()Z"))
    private boolean mubbleGametest$disallowFlight(boolean original) {
        return original && !TestFlight.isDisallowed(this.player);
    }
}
