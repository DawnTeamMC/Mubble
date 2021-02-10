package com.hugman.mubble;

import com.hugman.mubble.init.MubbleEntities;
import com.hugman.mubble.init.client.MubbleColorMaps;
import com.hugman.mubble.init.client.MubbleScreens;
import com.hugman.mubble.object.entity.render.*;
import com.hugman.mubble.object.event.LightsaberEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

@Environment(EnvType.CLIENT)
public class MubbleClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MubbleScreens.init();
		MubbleColorMaps.registerColors();
		registerEntityRenders();
		LightsaberEvents.init();

		FabricModelPredicateProviderRegistry.register(Mubble.MOD_DATA.id("trinket"), (stack, world, entity) -> {
			if(entity == null && stack.getOrCreateTag().contains("Trinket")) return stack.getOrCreateTag().getInt("Trinket");
			return 0;
		});
	}

	private void registerEntityRenders() {
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.CHINCHO, (dispatcher, context) -> new ChinchoEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.GOOMBA, (dispatcher, context) -> new GoombaEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.TOAD, (dispatcher, context) -> new ToadEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.CUSTOM_TNT, (dispatcher, context) -> new CustomTNTEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.FLYING_BLOCK, (dispatcher, context) -> new FlyingBlockEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.FIREBALL, (dispatcher, context) -> new FlyingItemEntityRenderer<>(dispatcher, context.getItemRenderer()));
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.ICEBALL, (dispatcher, context) -> new FlyingItemEntityRenderer<>(dispatcher, context.getItemRenderer()));
		EntityRendererRegistry.INSTANCE.register(MubbleEntities.KIRBY_BALL, (dispatcher, context) -> new FlyingItemEntityRenderer<>(dispatcher, context.getItemRenderer()));
	}
}
