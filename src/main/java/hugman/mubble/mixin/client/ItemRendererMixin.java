package hugman.mubble.mixin.client;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
	@Inject(method = "renderGuiItemOverlay", at = @At(value = "TAIL"), cancellable = true)
	private void renderGuiItemOverlay(TextRenderer fontRenderer, ItemStack stack, int x, int y, String amountText, CallbackInfoReturnable<List<Text>> info) {
		//bruh just cancel the durability from showing up
	}
}
