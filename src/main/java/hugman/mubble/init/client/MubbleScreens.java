package hugman.mubble.init.client;

import hugman.mubble.init.data.MubbleContainerTypes;
import hugman.mubble.objects.screen.TimeswapTableScreen;
import hugman.mubble.objects.screen.screen_handler.TimeswapTableScreenHandler;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;

public class MubbleScreens {
	public static void init() {
		ScreenProviderRegistry.INSTANCE.registerFactory(MubbleContainerTypes.TIMESWAP_TABLE, (syncId, identifier, player, buf) ->
		{
			return new TimeswapTableScreen(new TimeswapTableScreenHandler(syncId, player.inventory), player.inventory, null);
		});
	}
}