package fr.hugman.mubble.super_mario.client.mixin;

import fr.hugman.mubble.super_mario.client.renderer.SuperMarioRenderTypes;
import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpIds;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.hugman.mubble.super_mario.client.renderer.entity.ClingingMiniGoombas;
import fr.hugman.mubble.super_mario.world.entity.monster.goomba.MiniGoombaCarrier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.PlayerModelType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin {
    @Redirect(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;entityTranslucent(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;"))
    private RenderType super_mario$renderHand(Identifier texture) {
        var powerUp = Minecraft.getInstance().player.getPowerUp();
        if (powerUp.isPresent()) {
            if (powerUp.get().is(SuperMarioPowerUpIds.GOLD)) {
                return SuperMarioRenderTypes.getGoldenEntity(texture);
            }
        }
        return RenderTypes.entityTranslucent(texture);
    }

    @Inject(method = "renderRightHand", at = @At("TAIL"))
    private void super_mario$clingToRightHand(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, Identifier skinTexture, boolean hasSleeve, CallbackInfo ci) {
        this.super_mario$clingToHand(poseStack, submitNodeCollector, lightCoords, HumanoidArm.RIGHT);
    }

    @Inject(method = "renderLeftHand", at = @At("TAIL"))
    private void super_mario$clingToLeftHand(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, Identifier skinTexture, boolean hasSleeve, CallbackInfo ci) {
        this.super_mario$clingToHand(poseStack, submitNodeCollector, lightCoords, HumanoidArm.LEFT);
    }

    /**
     * Puts a few of the mini goombas holding onto the player on their first-person arm, as long as the
     * hand at the end of it is empty — an arm holding a map is drawn too, and that one is left alone.
     * <p>
     * The arm has just been posed and drawn here, so it only takes its own transform to be in its space.
     */
    @Unique
    private void super_mario$clingToHand(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, HumanoidArm side) {
        var player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        InteractionHand hand = side == player.getMainArm() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        if (!player.getItemInHand(hand).isEmpty()) {
            return;
        }
        int count = ClingingMiniGoombas.onArm(((MiniGoombaCarrier) player).getClingingMiniGoombas());
        if (count <= 0) {
            return;
        }
        PlayerModel model = ((AvatarRenderer<?>) (Object) this).getModel();
        boolean slim = player.getSkin().model() == PlayerModelType.SLIM;
        float ageInTicks = player.tickCount + Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);

        poseStack.pushPose();
        (side == HumanoidArm.RIGHT ? model.rightArm : model.leftArm).translateAndRotate(poseStack);
        for (int i = 0; i < count; i++) {
            ClingingMiniGoombas.submitOnSurface(ClingingMiniGoombas.model(), poseStack, submitNodeCollector, lightCoords, 0, ageInTicks, ClingingMiniGoombas.armSpot(side, i), slim, i);
        }
        poseStack.popPose();
    }
}
