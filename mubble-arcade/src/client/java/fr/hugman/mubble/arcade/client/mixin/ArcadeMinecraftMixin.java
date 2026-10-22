package fr.hugman.mubble.arcade.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fr.hugman.mubble.arcade.client.ArcadeClientInput;
import fr.hugman.mubble.arcade.client.ArcadeHands;
import fr.hugman.mubble.arcade.client.camera.ArcadeCamera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class ArcadeMinecraftMixin {
    /** Every frame starts here, right after the window events: the arcade actions are sampled before any tick runs. */
    @Inject(method = "runTick", at = @At("HEAD"))
    private void mubble$sampleArcadeInput(boolean advanceGameTime, CallbackInfo ci) {
        ArcadeClientInput.onFrame((Minecraft) (Object) this);
    }

    /**
     * While the arcade layer drives, the hands answer to the arcade attack and use rather than the
     * vanilla ones, and the clicks the moves take never reach them, see {@link ArcadeHands}.
     */
    @WrapOperation(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;consumeClick()Z"))
    private boolean mubble$arcadeHands(KeyMapping key, Operation<Boolean> original) {
        var minecraft = (Minecraft) (Object) this;
        var standIn = ArcadeHands.standIn(minecraft, key);
        if (standIn == null) {
            return original.call(key);
        }
        while (original.call(key)) {
            // the vanilla key stands aside
        }
        while (original.call(standIn)) {
            if (!ArcadeHands.takeForMoves(minecraft, standIn)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Aiming over the shoulder, the body turns to the crosshair right before the hands use what they
     * hold, and what they reach out to is picked again from there: a loaded crossbow shoots on the click.
     */
    @Inject(method = "startUseItem", at = @At("HEAD"))
    private void mubble$faceTheCrosshair(CallbackInfo ci) {
        var minecraft = (Minecraft) (Object) this;
        if (ArcadeCamera.onUse(minecraft)) {
            ((MinecraftInvoker) minecraft).mubble$pick(1.0F);
        }
    }

    /** Nor does holding them, until they are let go. */
    @WrapOperation(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z"))
    private boolean mubble$arcadeHandsHeld(KeyMapping key, Operation<Boolean> original) {
        var standIn = ArcadeHands.standIn((Minecraft) (Object) this, key);
        return standIn == null ? original.call(key) : original.call(standIn) && !ArcadeHands.kept(standIn);
    }
}
