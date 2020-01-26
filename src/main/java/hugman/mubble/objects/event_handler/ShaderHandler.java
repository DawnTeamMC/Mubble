package hugman.mubble.objects.event_handler;

import hugman.mubble.objects.costume.BlockCostume;
import hugman.mubble.objects.costume.Costume;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE, value=Dist.CLIENT)
public class ShaderHandler
{
	@SubscribeEvent
	public static void onArmorChange(LivingUpdateEvent event)
	{
		LivingEntity entity = event.getEntityLiving();
		ItemStack headItem = entity.getItemStackFromSlot(EquipmentSlotType.HEAD);	
		if(entity instanceof PlayerEntity)
		{
			GameRenderer renderer = Minecraft.getInstance().gameRenderer;
			ShaderGroup shaderGroup = renderer.getShaderGroup();
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