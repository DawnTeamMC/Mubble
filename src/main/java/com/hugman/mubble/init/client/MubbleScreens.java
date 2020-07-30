package com.hugman.mubble.init.client;

import com.hugman.mubble.init.data.MubbleScreenHandlers;
import com.hugman.mubble.object.screen.TimeswapTableScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class MubbleScreens {
	public static void init() {
		ScreenRegistry.register(MubbleScreenHandlers.TIMESWAP_TABLE, TimeswapTableScreen::new);
	}
}