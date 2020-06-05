package hugman.mubble.mixin.client;

import com.google.common.collect.Lists;
import com.sun.istack.internal.Nullable;
import hugman.mubble.init.MubbleEnchantments;
import hugman.mubble.util.EnchantmentUtil;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin
{
	@Inject(method = "renderGuiItemOverlay", at = @At(value = "TAIL"), cancellable = true)
	private void renderGuiItemOverlay(TextRenderer fontRenderer, ItemStack stack, int x, int y, @Nullable String amountText, CallbackInfoReturnable<List<Text>> info)
	{
		//bruh just cancel the durability from showing up
	}
}
