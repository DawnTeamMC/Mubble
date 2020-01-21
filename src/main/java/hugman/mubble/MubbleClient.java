package hugman.mubble;

import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.client.MubbleColorMaps;
import hugman.mubble.init.client.MubbleRenderLayers;
import hugman.mubble.init.client.MubbleScreens;
import hugman.mubble.objects.entity.render.ChinchoRenderer;
import hugman.mubble.objects.entity.render.CustomTNTRenderer;
import hugman.mubble.objects.entity.render.FlyingBlockRenderer;
import hugman.mubble.objects.entity.render.GoombaRenderer;
import hugman.mubble.objects.entity.render.ToadRenderer;
import hugman.mubble.objects.entity.render.ZombieCowmanRenderer;
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
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.CHINCHO, (dispatcher, context) -> new ChinchoRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.GOOMBA, (dispatcher, context) -> new GoombaRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.TOAD, (dispatcher, context) -> new ToadRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.ZOMBIE_COWMAN, (dispatcher, context) -> new ZombieCowmanRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.CUSTOM_TNT, (dispatcher, context) -> new CustomTNTRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.FLYING_BLOCK, (dispatcher, context) -> new FlyingBlockRenderer(dispatcher));
	}
}
