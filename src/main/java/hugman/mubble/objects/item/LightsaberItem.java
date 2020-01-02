package hugman.mubble.objects.item;

import hugman.mubble.init.MubbleSounds;
import hugman.mubble.util.MubbleItemTier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.world.World;

public class LightsaberItem extends SwordItem
{
	public static int idleTimer = 0;
	
	public LightsaberItem(Properties builder)
	{
		super(MubbleItemTier.KYBER, 3, -2.4F, builder);
	}
	
	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity entity)
	{
		entity.playSound(MubbleSounds.ITEM_LIGHTSABER_SWIPE, 1.0F, 1.0F);
		return super.onEntitySwing(stack, entity);
	}
	
	@Override
	public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker)
	{
		return true;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
    	if(isSelected && worldIn.isRemote && idleTimer == 0 && entityIn instanceof PlayerEntity)
    	{
    		Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(MubbleSounds.ITEM_LIGHTSABER_IDLE, 1.0F, 0.15F));
    	}
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
	}
}