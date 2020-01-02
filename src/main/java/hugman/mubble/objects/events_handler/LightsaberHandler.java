package hugman.mubble.objects.events_handler;

import hugman.mubble.init.MubbleSounds;
import hugman.mubble.objects.item.LightsaberItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SSpawnMovingSoundEffectPacket;
import net.minecraft.network.play.server.SStopSoundPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class LightsaberHandler
{
	@SubscribeEvent
	public static void onLightsaberPull(LivingEquipmentChangeEvent event)
	{
		LivingEntity entityIn = event.getEntityLiving();
		World worldIn = entityIn.getEntityWorld();
		ItemStack from = event.getFrom();
		ItemStack to = event.getTo();
		if(from.getItem() instanceof LightsaberItem)
		{
			worldIn.playMovingSound((PlayerEntity)null, entityIn, MubbleSounds.ITEM_LIGHTSABER_PULL_IN, SoundCategory.PLAYERS, 1.0F, 1.0F);
			if(entityIn instanceof ServerPlayerEntity)
			{
	    		SStopSoundPacket sstopsoundpacket = new SStopSoundPacket(MubbleSounds.ITEM_LIGHTSABER_IDLE.getName(), SoundCategory.MASTER);
	    		((ServerPlayerEntity)entityIn).connection.sendPacket(sstopsoundpacket);
			}
		}
		if(to.getItem() instanceof LightsaberItem)
		{
			worldIn.playMovingSound((PlayerEntity)null, entityIn, MubbleSounds.ITEM_LIGHTSABER_PULL_OUT, SoundCategory.PLAYERS, 1.0F, 1.0F);
			if(entityIn instanceof ServerPlayerEntity)
			{
	    		SSpawnMovingSoundEffectPacket sstopsoundpacket = new SSpawnMovingSoundEffectPacket(MubbleSounds.ITEM_LIGHTSABER_IDLE, SoundCategory.MASTER, entityIn, 0.15F, 1.0F);
	    		((ServerPlayerEntity)entityIn).connection.sendPacket(sstopsoundpacket);
			}
		}
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void clientTick(ClientTickEvent event)
	{
		if(event.phase == Phase.START)
		{
			if(LightsaberItem.idleTimer <= 95)
			{
				LightsaberItem.idleTimer++;
			}
			else
			{
				LightsaberItem.idleTimer = 0;
			}
		}
	}
}