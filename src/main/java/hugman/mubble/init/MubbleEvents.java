package hugman.mubble.init;

import hugman.mubble.init.data.MubbleTags;
import hugman.mubble.init.world.MubbleDimensions;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.DyeItem;
import net.minecraft.item.EggItem;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.item.SnowballItem;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class MubbleEvents
{
	@SubscribeEvent
	public static void onJump(LivingJumpEvent event)
	{
		LivingEntity entity = event.getEntityLiving();
		if(entity.isPotionActive(MubbleEffects.HEAVINESS))
		{
			Vec3d vec3d = entity.getMotion();
			entity.setMotion(vec3d.x, vec3d.y - (float)(entity.getActivePotionEffect(MubbleEffects.HEAVINESS).getAmplifier() + 1) * 0.05F, vec3d.z);
		}
	}
	
	@SubscribeEvent
	public static void onTick(LivingUpdateEvent event)
	{
		LivingEntity entity = event.getEntityLiving();
		World world = entity.getEntityWorld();
		ItemStack itemHead = entity.getItemStackFromSlot(EquipmentSlotType.HEAD);
		ItemStack itemMainHand = entity.getHeldItemMainhand();
		ItemStack itemOffHand = entity.getHeldItemOffhand();
		if(world.getDimension().getType() == MubbleDimensions.PERMAFROST_TYPE && entity instanceof PlayerEntity)
		{
			if(itemMainHand.getItem() != Items.TORCH && itemOffHand.getItem() != Items.TORCH)
			{
				entity.addPotionEffect(new EffectInstance(MubbleEffects.HEAVINESS, 5, 0));
				entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 5, 0));
				entity.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 5, 0));
			}
		}
		if(!world.isRemote)
		{
			if(MubbleTags.Items.WEIGHT_HEAVY.contains(itemHead.getItem())) entity.addPotionEffect(new EffectInstance(MubbleEffects.HEAVINESS, 25, 0));
		}
	}
}