package hugman.mubble.init.client;

import hugman.mubble.init.data.MubbleScreenHandlers;
import hugman.mubble.objects.screen.TimeswapTableScreen;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class MubbleScreens {
	public static void init() {
		ScreenRegistry.register(MubbleScreenHandlers.TIMESWAP_TABLE, TimeswapTableScreen::new);
	}
}