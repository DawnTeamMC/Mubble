package fr.hugman.mubble.client.mixin;

import fr.hugman.mubble.client.gui.hud.PowerUpHudRendering;
import fr.hugman.mubble.client.gui.hud.PowerUpPropertiesHudRendering;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
	@Final
	@Shadow
	private Minecraft minecraft;

	@Inject(method="render", at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/Gui;renderBossOverlay(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V"))
	private void mubble$addPowerUpLayer(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {
		PowerUpHudRendering.renderPowerUpLayer(this.minecraft, context);
	}

	@Inject(method="render", at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/Gui;renderCrosshair(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V"))
	private void mubble$addPowerUpPropertiesLayer(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {
		PowerUpPropertiesHudRendering.renderProjectilesLayer(this.minecraft, context);
	}
}