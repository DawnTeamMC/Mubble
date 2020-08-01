package com.hugman.mubble;

import com.hugman.mubble.init.MubbleEntityPack;
import com.hugman.mubble.init.client.MubbleColorMaps;
import com.hugman.mubble.init.client.MubbleScreens;
import com.hugman.mubble.object.entity.render.*;
import com.hugman.mubble.object.event.LightsaberEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

@Environment(EnvType.CLIENT)
public class MubbleClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MubbleScreens.init();
		MubbleColorMaps.registerColors();
		registerEntityRenders();
		LightsaberEvents.init();
	}

	private void registerEntityRenders() {
		EntityRendererRegistry.INSTANCE.register(MubbleEntityPack.CHINCHO, (dispatcher, context) -> new ChinchoEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntityPack.GOOMBA, (dispatcher, context) -> new GoombaEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntityPack.TOAD, (dispatcher, context) -> new ToadEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntityPack.DUCK, (dispatcher, context) -> new DuckEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntityPack.ZOMBIE_COWMAN, (dispatcher, context) -> new ZombieCowmanEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntityPack.CUSTOM_TNT, (dispatcher, context) -> new CustomTNTEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntityPack.FLYING_BLOCK, (dispatcher, context) -> new FlyingBlockEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(MubbleEntityPack.FIREBALL, (dispatcher, context) -> new FlyingItemEntityRenderer<>(dispatcher, context.getItemRenderer()));
		EntityRendererRegistry.INSTANCE.register(MubbleEntityPack.ICEBALL, (dispatcher, context) -> new FlyingItemEntityRenderer<>(dispatcher, context.getItemRenderer()));
		EntityRendererRegistry.INSTANCE.register(MubbleEntityPack.KIRBY_BALL, (dispatcher, context) -> new FlyingItemEntityRenderer<>(dispatcher, context.getItemRenderer()));
	}
}
