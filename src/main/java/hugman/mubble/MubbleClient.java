package hugman.mubble;

import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.client.MubbleColorMaps;
import hugman.mubble.init.client.MubbleRenderLayers;
import hugman.mubble.init.client.MubbleScreens;
import hugman.mubble.objects.entity.render.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

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
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.DUCK, (dispatcher, context) -> new DuckRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.ZOMBIE_COWMAN, (dispatcher, context) -> new ZombieCowmanRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.CUSTOM_TNT, (dispatcher, context) -> new CustomTNTRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.FLYING_BLOCK, (dispatcher, context) -> new FlyingBlockRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.FIREBALL, (dispatcher, context) -> new FlyingItemEntityRenderer<>(dispatcher, context.getItemRenderer()));
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.ICEBALL, (dispatcher, context) -> new FlyingItemEntityRenderer<>(dispatcher, context.getItemRenderer()));
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.KIRBY_BALL, (dispatcher, context) -> new FlyingItemEntityRenderer<>(dispatcher, context.getItemRenderer()));
	}
}
