package hugman.mubble.mixin.client;

import com.google.common.collect.Lists;
import com.sun.istack.internal.Nullable;
import hugman.mubble.init.MubbleEnchantments;
import hugman.mubble.util.EnchantmentUtil;
import net.minecraft.client.item.TooltipContext;
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

@Mixin(ItemStack.class)
public class ItemStackMixin
{
	@Inject(method = "getTooltip", at = @At(value = "TAIL"), cancellable = true)
	private void getTooltip(@Nullable PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> info)
	{
		ItemStack itemStack = (ItemStack) (Object) this;
		if (player != null)
		{
			if (!player.isCreative() & EnchantmentUtil.hasEnchantment(MubbleEnchantments.IGNORANCE_CURSE, itemStack))
			{
				List<Text> tooltip = Lists.newArrayList();
				MutableText mutableText = (new LiteralText("")).append(itemStack.getName()).formatted(itemStack.getRarity().formatting);
				if (itemStack.hasCustomName())
				{
					mutableText.formatted(Formatting.ITALIC);
				}
				tooltip.add(mutableText);
				tooltip.add(MubbleEnchantments.IGNORANCE_CURSE.getName(EnchantmentHelper.getLevel(MubbleEnchantments.IGNORANCE_CURSE, itemStack)));
				info.setReturnValue(tooltip);
			}
		}
	}
}
