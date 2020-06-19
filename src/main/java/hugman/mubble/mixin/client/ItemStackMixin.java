package hugman.mubble.mixin.client;

import com.google.common.collect.Lists;
import hugman.mubble.init.MubbleEnchantments;
import hugman.mubble.util.EnchantmentUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@Redirect(method = "getTooltip", at = @At(value = "INVOKE", target = "appendEnchantments"))
	public void hideEnchantments(List<Text> tooltip, ListTag enchantments, PlayerEntity playerEntity, TooltipContext context) {
		ItemStack stack = (ItemStack) (Object) this;
		if(!playerEntity.isCreative() && EnchantmentUtil.hasEnchantment(MubbleEnchantments.IGNORANCE_CURSE, stack)) {
			tooltip.add(MubbleEnchantments.IGNORANCE_CURSE.getName(EnchantmentHelper.getLevel(MubbleEnchantments.IGNORANCE_CURSE, stack)));
		}
		else {
			stack.appendEnchantments(tooltip, enchantments);
		}
	}
}
