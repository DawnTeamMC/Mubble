/*package hugman.mubble.objects.events_handler;

import hugman.mubble.objects.costume.BlockCostume;
import hugman.mubble.objects.costume.Costume;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class ShaderHandler
{
	public static void onArmorChange(LivingUpdateEvent event)
	{
		LivingEntity entity = event.getEntityLiving();
		ItemStack headItem = entity.getEquippedStack(EquipmentSlot.HEAD);
		if(entity instanceof PlayerEntity)
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
			if(headItem.getItem() instanceof Costume)
			{
				ResourceLocation shader = ((Costume) headItem.getItem()).getShader();
				if(shaderGroup != null && shader == null)
				{
					renderer.stopUseShader();
				}
			}
			if(headItem.getItem() instanceof BlockCostume)
			{
				ResourceLocation shader = ((BlockCostume) headItem.getItem()).getShader();
				if(shaderGroup != null && shader == null)
				{
					renderer.stopUseShader();
				}
			}
		}
	}
}*/