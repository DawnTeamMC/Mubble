package hugman.mubble.mixin.client;

import com.sun.istack.internal.Nullable;
import hugman.mubble.init.MubbleEnchantments;
import hugman.mubble.util.EnchantmentUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@Redirect(method = "getTooltip", at = @At(value = "INVOKE", target = "appendEnchantments"))
	public void mubble_appendEnchantments(List<Text> tooltip, ListTag enchantments, @Nullable PlayerEntity playerEntity, TooltipContext context) {
		ItemStack stack = (ItemStack) (Object) this;
		if(EnchantmentUtil.hasEnchantment(MubbleEnchantments.IGNORANCE_CURSE, stack)) {
			if(playerEntity != null) {
				if(playerEntity.isCreative()) {
					ItemStack.appendEnchantments(tooltip, enchantments);
					return;
				}
			}
			tooltip.add(MubbleEnchantments.IGNORANCE_CURSE.getName(EnchantmentHelper.getLevel(MubbleEnchantments.IGNORANCE_CURSE, stack)));
		}
		else {
			ItemStack.appendEnchantments(tooltip, enchantments);
		}
	}

	@Redirect(method = "getTooltip", at = @At(value = "INVOKE",target = "isDamaged"))
	public boolean mubble_isDamaged(ItemStack stack) {
		ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
		if(EnchantmentUtil.hasEnchantment(MubbleEnchantments.IGNORANCE_CURSE, stack) && !clientPlayerEntity.isCreative()) {
			return false;
		}
		return stack.isDamaged();
	}
}
