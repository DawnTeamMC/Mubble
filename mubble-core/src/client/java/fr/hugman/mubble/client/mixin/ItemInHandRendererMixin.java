package fr.hugman.mubble.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
	@Inject(method="renderArmWithItem", at=@At("TAIL"))
	private void mubble$renderArmWithItem(
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
		if (!player.isScoping()
				&& itemStack.isEmpty()
				&& !player.isInvisible()
				&& hand == InteractionHand.OFF_HAND
				&& player.getPowerUp().isPresent()
				&& player.getPowerUp().get().value().shouldDisplayOtherHand()
				&& !player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()
		) {
			this.renderPlayerArm(poseStack, submitNodeCollector, lightCoords, inverseArmHeight, attack, player.getMainArm().getOpposite());
		}
	}

	@Shadow
	private void renderPlayerArm(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float inverseArmHeight, float attack, HumanoidArm arm) {
		throw new AssertionError();
	}
}