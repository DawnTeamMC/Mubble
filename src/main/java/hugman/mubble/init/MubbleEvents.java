/*package hugman.mubble.init;

import java.util.Random;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;

import hugman.mubble.init.data.MubbleTags;
import hugman.mubble.init.world.MubbleDimensions;
import hugman.mubble.objects.block.PermafrostPortalBlock;
import hugman.mubble.objects.costume.BlockCostume;
import hugman.mubble.objects.costume.Costume;
import hugman.mubble.util.CalendarEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class MubbleEvents
{
	public static void onJump(LivingJumpEvent event)
	{
		LivingEntity entity = event.getEntityLiving();
		if(entity.hasStatusEffect(MubbleEffects.HEAVINESS))
		{
			Vec3d vec3d = entity.getVelocity();
			entity.setVelocity(vec3d.x, vec3d.y - (float)(entity.getStatusEffect(MubbleEffects.HEAVINESS).getAmplifier() + 1) * 0.05F, vec3d.z);
		}
	}
	
	public static void onTick(LivingUpdateEvent event)
	{
		LivingEntity entity = event.getEntityLiving();
		World world = entity.getEntityWorld();
		ItemStack headItem = entity.getEquippedStack(EquipmentSlot.HEAD);
		if(!world.isClient)
		{
			if(MubbleTags.Items.WEIGHT_HEAVY.contains(headItem.getItem()))
			{
				entity.addStatusEffect(new StatusEffectInstance(MubbleEffects.HEAVINESS, 25, 0));
			}
		}
	}
	
	public static void onArmorChange(LivingUpdateEvent event)
	{
		LivingEntity entity = event.getEntityLiving();
		World world = entity.getEntityWorld();
		ItemStack headItem = entity.getEquippedStack(EquipmentSlot.HEAD);	
		if(entity instanceof PlayerEntity && world.isClient)
		{
			GameRenderer renderer = MinecraftClient.getInstance().gameRenderer;
			ShaderEffect shaderGroup = renderer.getShader();
			if(!(headItem.getItem() instanceof Costume) && !(headItem.getItem() instanceof BlockCostume))
			{
				if(shaderGroup != null)
				{
					renderer.disableShader();
				}
			}
		}
	}
	
	public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event)
	{
		World worldIn = event.getWorld().getWorld();
		BlockPos pos = event.getPos();
		BlockState state = event.getPlacedAgainst();
		Block block = event.getPlacedBlock().getBlock();
		
		if(block instanceof FireBlock)
		{
			if(worldIn.dimension.getType() != DimensionType.OVERWORLD && worldIn.getDimension().getType() != MubbleDimensions.PERMAFROST || !((PermafrostPortalBlock)MubbleBlocks.PERMAFROST_PORTAL).trySpawnPortal(worldIn, pos))
			{
				if (!state.canPlaceAt(worldIn, pos))
				{
					worldIn.removeBlock(pos, false);
				}
				else
				{
					worldIn.getBlockTickScheduler().schedule(pos, block, block.getTickRate(worldIn) + worldIn.random.nextInt(10));
				}
			}
		}
	}
	
	public static void onSpawn(LivingSpawnEvent.CheckSpawn event)
	{
		Entity fEntity = event.getEntity();
		Random rand = new Random();
		if(fEntity instanceof MobEntity)
		{
			MobEntity entity = (MobEntity) fEntity;
			if(MubbleTags.EntityTypes.CAN_WEAR_HELMET.contains(entity.getType()))
			{
				if(entity.getEquippedStack(EquipmentSlot.HEAD).isEmpty() && CalendarEvents.isChristmasSeason)
				{
					if(rand.nextFloat() < (float)CalendarEvents.getDayToday() / 25.0f)
					{
						entity.equipStack(EquipmentSlot.HEAD, new ItemStack(MubbleCostumes.CHRISTMAS_HAT));
						entity.setEquipmentDropChance(EquipmentSlot.HEAD, 0.0F);
					}
				}
			}
		}
	}
}*/