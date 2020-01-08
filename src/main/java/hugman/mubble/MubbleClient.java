package hugman.mubble;

import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.client.MubbleColorMaps;
import hugman.mubble.init.client.MubbleRenderLayers;
import hugman.mubble.init.client.MubbleScreens;
import hugman.mubble.objects.entity.render.ChinchoRender;
import hugman.mubble.objects.entity.render.CustomTNTRender;
import hugman.mubble.objects.entity.render.FlyingBlockRender;
import hugman.mubble.objects.entity.render.GoombaRender;
import hugman.mubble.objects.entity.render.ToadRender;
import hugman.mubble.objects.entity.render.ZombieCowmanRender;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class MubbleClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		MubbleScreens.init();
		MubbleColorMaps.registerBlockColors();
		MubbleColorMaps.registerItemColors();
		MubbleRenderLayers.init();
		registerEntityRenders();
	}
	
	private void registerEntityRenders()
	{
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.CHINCHO, (dispatcher, context) -> new ChinchoRender(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.GOOMBA, (dispatcher, context) -> new GoombaRender(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.TOAD, (dispatcher, context) -> new ToadRender(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.ZOMBIE_COWMAN, (dispatcher, context) -> new ZombieCowmanRender(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.CUSTOM_TNT, (dispatcher, context) -> new CustomTNTRender(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.FLYING_BLOCK, (dispatcher, context) -> new FlyingBlockRender(dispatcher));
	}
}
