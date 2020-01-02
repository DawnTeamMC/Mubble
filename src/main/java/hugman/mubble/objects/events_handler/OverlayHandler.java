package hugman.mubble.objects.events_handler;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE, value=Dist.CLIENT)
public class OverlayHandler
{
	@SubscribeEvent
	public static void renderGameOverlayPost(RenderGameOverlayEvent.Post event)
	{
		if(event.getType().equals(ElementType.ALL))
		{
			//GuiUtils.drawGradientRect(5, 5, 5, 50, 100, Color.YELLOW.getRGB(), Color.RED.getRGB());
		}
	}
}