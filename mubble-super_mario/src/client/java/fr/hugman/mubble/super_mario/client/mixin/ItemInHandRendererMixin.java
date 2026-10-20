package fr.hugman.mubble.super_mario.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.hugman.mubble.super_mario.client.renderer.entity.ClingingMiniGoombas;
import fr.hugman.mubble.super_mario.world.entity.monster.goomba.MiniGoombaCarrier;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    /**
     * Puts the mini goombas holding onto the player around the edges of their view.
     * <p>
     * Done at the head of the hand pass, which is drawn in camera space and over the world: they stay on
     * the screen wherever the player looks, and no block can get between them and the camera. The hand's
     * own sway is only applied after this, so they do not swing with it. Called with none holding on too,
     * for the last one shaken off to be seen flung off the screen.
     */
    @Inject(method = "submitHandsWithItems", at = @At("HEAD"))
    private void super_mario$submitClingingMiniGoombas(float frameInterp, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, LocalPlayer player, int lightCoords, CallbackInfo ci) {
        int count = ((MiniGoombaCarrier) player).getClingingMiniGoombas();
        ClingingMiniGoombas.submitOnScreen(ClingingMiniGoombas.model(), poseStack, submitNodeCollector, lightCoords, player.tickCount + frameInterp, count);
    }
}
