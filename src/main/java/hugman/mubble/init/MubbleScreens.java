package hugman.mubble.init;

import hugman.mubble.objects.screen.TimeswapTableScreen;
import net.minecraft.client.gui.ScreenManager;

public class MubbleScreens
{
	public static void registerScreens()
	{
		ScreenManager.registerFactory(MubbleContainerTypes.TIMESWAP_TABLE, TimeswapTableScreen::new);
	}
}