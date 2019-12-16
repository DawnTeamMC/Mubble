package hugman.mubble.init;

import java.util.Random;

import com.mojang.blaze3d.platform.GLX;

import hugman.mubble.init.data.MubbleTags;
import hugman.mubble.init.world.MubbleDimensions;
import hugman.mubble.objects.block.PermafrostPortalBlock;
import hugman.mubble.objects.costume.BlockCostume;
import hugman.mubble.objects.costume.Costume;
import hugman.mubble.util.CalendarEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BlockEvent;
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
		ItemStack headItem = entity.getItemStackFromSlot(EquipmentSlotType.HEAD);
		if(!world.isRemote)
		{
			if(MubbleTags.Items.WEIGHT_HEAVY.contains(headItem.getItem()))
			{
				entity.addPotionEffect(new EffectInstance(MubbleEffects.HEAVINESS, 25, 0));
			}
		}
	}
	
	@SubscribeEvent
	public static void onArmorChange(LivingUpdateEvent event)
	{
		LivingEntity entity = event.getEntityLiving();
		World world = entity.getEntityWorld();
		ItemStack headItem = entity.getItemStackFromSlot(EquipmentSlotType.HEAD);	
		if(entity instanceof PlayerEntity && world.isRemote)
		{
			GameRenderer renderer = Minecraft.getInstance().gameRenderer;
			ShaderGroup shaderGroup = renderer.getShaderGroup();
			if(GLX.usePostProcess)
			{
				if(!(headItem.getItem() instanceof Costume) && !(headItem.getItem() instanceof BlockCostume))
				{
					if(shaderGroup != null)
					{
						renderer.stopUseShader();
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event)
	{
		World worldIn = event.getWorld().getWorld();
		BlockPos pos = event.getPos();
		BlockState state = event.getPlacedAgainst();
		Block block = event.getPlacedBlock().getBlock();
		
		if(block instanceof FireBlock)
		{
			if(worldIn.dimension.getType() != DimensionType.OVERWORLD && worldIn.getDimension().getType().getModType() != MubbleDimensions.PERMAFROST || !((PermafrostPortalBlock)MubbleBlocks.PERMAFROST_PORTAL).trySpawnPortal(worldIn, pos))
			{
				if (!state.isValidPosition(worldIn, pos))
				{
					worldIn.removeBlock(pos, false);
				}
				else
				{
					worldIn.getPendingBlockTicks().scheduleTick(pos, block, block.tickRate(worldIn) + worldIn.rand.nextInt(10));
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onSpawn(LivingSpawnEvent.CheckSpawn event)
	{
		Entity fEntity = event.getEntity();
		Random rand = new Random();
		if(fEntity instanceof MobEntity)
		{
			MobEntity entity = (MobEntity)fEntity;
			if(entity instanceof ZombieEntity || entity instanceof AbstractSkeletonEntity)
			{
				if(entity.getItemStackFromSlot(EquipmentSlotType.HEAD).isEmpty() && CalendarEvents.isDecember && rand.nextFloat() < (CalendarEvents.getDayToday() / 25))
				{
					entity.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(MubbleCostumes.CHRISTMAS_HAT));
					entity.setDropChance(EquipmentSlotType.HEAD, 0.0F);
				}
			}
		}
	}
}