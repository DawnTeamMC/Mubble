package hugman.mubble.objects.item;

import hugman.mubble.init.MubbleSounds;
import hugman.mubble.init.data.MubbleItemTiers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.s2c.play.PlaySoundFromEntityS2CPacket;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;

public class LightsaberItem extends SwordItem
{
	public static int idleTimer = 0;
	
	public LightsaberItem(Settings builder)
	{
		super(MubbleItemTiers.KYBER, 3, -2.4F, builder);
	}
	
	/*@Override
	public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker)
	{
		return true;
	}*/
	
	public void onSwing(LivingEntity entity, boolean hitsEntity)
	{
		entity.playSound(MubbleSounds.ITEM_LIGHTSABER_SWIPE, 1.0F, 1.0F);
		if(hitsEntity)
		{
			entity.playSound(MubbleSounds.ITEM_LIGHTSABER_HIT, 1.0F, 1.0F);
		}
	}
	
	public void onPullOut(Entity entity, World world)
	{
		world.playSoundFromEntity((PlayerEntity) null, entity, MubbleSounds.ITEM_LIGHTSABER_PULL_OUT, SoundCategory.PLAYERS, 1.0F, 1.0F);
		if(entity instanceof ServerPlayerEntity)
		{
			PlaySoundFromEntityS2CPacket packet = new PlaySoundFromEntityS2CPacket(MubbleSounds.ITEM_LIGHTSABER_IDLE, SoundCategory.MASTER, entity, 0.15F, 1.0F);
    		((ServerPlayerEntity) entity).networkHandler.sendPacket(packet);
		}
	}
	
	public void onPullIn(Entity entity, World world)
	{
		world.playSoundFromEntity((PlayerEntity) null, entity, MubbleSounds.ITEM_LIGHTSABER_PULL_IN, SoundCategory.PLAYERS, 1.0F, 1.0F);
		if(entity instanceof ServerPlayerEntity)
		{
			StopSoundS2CPacket packet = new StopSoundS2CPacket(MubbleSounds.ITEM_LIGHTSABER_IDLE.getId(), SoundCategory.MASTER);
    		((ServerPlayerEntity) entity).networkHandler.sendPacket(packet);
		}
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
    	if(isSelected && worldIn.isClient && idleTimer == 0 && entityIn instanceof PlayerEntity)
    	{
    		MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(MubbleSounds.ITEM_LIGHTSABER_IDLE, 1.0F, 0.15F));
    	}
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
	}
}