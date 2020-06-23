package hugman.mubble.init.client;

import hugman.mubble.Mubble;
import hugman.mubble.init.data.MubbleScreenHandlers;
import hugman.mubble.objects.screen.TimeswapTableScreen;
import hugman.mubble.objects.screen.screen_handler.TimeswapTableScreenHandler;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class MubbleScreens {
	public static void init() {
		ScreenRegistry.register(MubbleScreenHandlers.TIMESWAP_TABLE, TimeswapTableScreen::new);
	}
}