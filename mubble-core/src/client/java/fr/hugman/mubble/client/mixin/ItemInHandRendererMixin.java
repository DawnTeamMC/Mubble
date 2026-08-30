package fr.hugman.mubble.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
	@Shadow
	private float offHandHeight;
	@Shadow
	private float oOffHandHeight;

	@Unique
	private boolean displayPowerUpInOtherHand;
	@Unique
    private boolean oDisplayPowerUpInOtherHand;

	@Inject(method="submitArmWithItem", at=@At("TAIL"))
	private void mubble$submitArmWithItem(
			AbstractClientPlayer player,
			float frameInterp,
			float xRot,
			InteractionHand hand,
			float attack, ItemStack itemStack,
			float inverseArmHeight,
			PoseStack poseStack,
			SubmitNodeCollector submitNodeCollector,
			int lightCoords,
			CallbackInfo ci
	) {
        // Render the second hand if the player has a power-up and an item in its first hand
		if (hand == InteractionHand.OFF_HAND && this.displayPowerUpInOtherHand) {
			this.renderPlayerArm(poseStack, submitNodeCollector, lightCoords, inverseArmHeight, attack, player.getMainArm().getOpposite());
		}
	}


	@Inject(method="tick", at=@At("TAIL"))
	private void mubble$tick(CallbackInfo ci) {
		var player = Minecraft.getInstance().player;
        this.displayPowerUpInOtherHand = !player.isScoping()
                && player.getOffhandItem().isEmpty()
                && !player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()
                && player.getItemInHand(InteractionHand.OFF_HAND).isEmpty()
                && player.getPowerUp().isPresent()
                && player.getPowerUp().get().value().shouldDisplayOtherHand(player);
		if(this.displayPowerUpInOtherHand != this.oDisplayPowerUpInOtherHand) {
			this.offHandHeight = 0.0f;
			this.oOffHandHeight = 0.0f;
		}
		this.oDisplayPowerUpInOtherHand = this.displayPowerUpInOtherHand;
	}

	@Shadow
	private void renderPlayerArm(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float inverseArmHeight, float attack, HumanoidArm arm) {
		throw new AssertionError();
	}
}