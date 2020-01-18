package hugman.mubble.objects.item;

import hugman.mubble.init.MubbleSounds;
import hugman.mubble.util.MubbleItemTier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.network.play.server.SSpawnMovingSoundEffectPacket;
import net.minecraft.network.play.server.SStopSoundPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class LightsaberItem extends SwordItem
{
	public static int idleTimer = 0;
	
	public LightsaberItem(Properties builder)
	{
		super(MubbleItemTier.KYBER, 3, -2.4F, builder);
	}
	
	@Override
	public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker)
	{
		return true;
	}
	
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
		world.playMovingSound((PlayerEntity)null, entity, MubbleSounds.ITEM_LIGHTSABER_PULL_OUT, SoundCategory.PLAYERS, 1.0F, 1.0F);
		if(entity instanceof ServerPlayerEntity)
		{
    		SSpawnMovingSoundEffectPacket packet = new SSpawnMovingSoundEffectPacket(MubbleSounds.ITEM_LIGHTSABER_IDLE, SoundCategory.MASTER, entity, 0.15F, 1.0F);
    		((ServerPlayerEntity)entity).connection.sendPacket(packet);
		}
	}
	
	public void onPullIn(Entity entity, World world)
	{
		world.playMovingSound((PlayerEntity)null, entity, MubbleSounds.ITEM_LIGHTSABER_PULL_IN, SoundCategory.PLAYERS, 1.0F, 1.0F);
		if(entity instanceof ServerPlayerEntity)
		{
    		SStopSoundPacket packet = new SStopSoundPacket(MubbleSounds.ITEM_LIGHTSABER_IDLE.getName(), SoundCategory.MASTER);
    		((ServerPlayerEntity)entity).connection.sendPacket(packet);
		}
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