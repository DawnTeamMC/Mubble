package hugman.mubble.objects.item.costume;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class WingCapCostume extends HeadCostume {
	public WingCapCostume(Item.Settings builder) {
		super(builder, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
	}

	@Override
	public boolean canRepair(ItemStack toRepair, ItemStack repair) {
		return repair.getItem() == Items.FEATHER;
	}

	@Override
	public void usageTick(World world, LivingEntity player, ItemStack stack, int remainingUseTicks) {
		if(isUsable(stack) && player.isSprinting()) {
			stack.damage(1, player, (p_214023_1_) ->
			{
				p_214023_1_.sendEquipmentBreakStatus(EquipmentSlot.HEAD);
			});
		}
		if(!world.isClient && isUsable(stack) && player.isSprinting()) {
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 1, 2));
			player.fallDistance = 0f;
		}
		super.usageTick(world, player, stack, remainingUseTicks);
	}
}