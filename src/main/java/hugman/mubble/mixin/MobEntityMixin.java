package hugman.mubble.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import hugman.mubble.objects.costume.BlockCostume;
import hugman.mubble.objects.costume.Costume;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(MobEntity.class)
public class MobEntityMixin
{
	@Inject(method = "getPreferredEquipmentSlot", at = @At(value = "TAIL"), cancellable = true)
	private static void getPreferredEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> cir)
	{
		Item item = stack.getItem();
		if (item instanceof Costume)
		{
			Costume costume = (Costume) item;
			cir.setReturnValue(costume.getArmorType());
		} else if (item instanceof BlockCostume)
			{
				BlockCostume costume = (BlockCostume) item;
				cir.setReturnValue(costume.getArmorType());
			}
	}
}
