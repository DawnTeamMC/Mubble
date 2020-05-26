package hugman.mubble.init.client;

import hugman.mubble.init.data.MubbleContainerTypes;
import hugman.mubble.objects.container.TimeswapTableContainer;
import hugman.mubble.objects.screen.TimeswapTableScreen;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;

public class MubbleScreens
{
	public static void init()
	{
		ScreenProviderRegistry.INSTANCE.registerFactory(MubbleContainerTypes.TIMESWAP_TABLE, (syncId, identifier, player, buf) ->
		{
			return new TimeswapTableScreen(new TimeswapTableContainer(syncId, player.inventory), player.inventory, null);
		});
	}
}