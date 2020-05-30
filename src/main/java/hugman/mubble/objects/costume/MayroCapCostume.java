package hugman.mubble.objects.costume;

import hugman.mubble.init.data.MubbleTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class MayroCapCostume extends HeadCostume
{
	public MayroCapCostume(Item.Settings builder)
	{
		super(builder, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
	}

	@Override
	public void usageTick(World world, LivingEntity entity, ItemStack stack, int remainingUseTicks)
	{
		if (entity instanceof PlayerEntity)
		{
			((PlayerEntity) entity).inventory.method_29280(item -> MubbleTags.Items.COINS.contains(item.getItem()), 1, ((PlayerEntity) entity).playerScreenHandler.method_29281());
		}
		super.usageTick(world, entity, stack, remainingUseTicks);
	}
}