package hugman.mubble.init.data;

import hugman.mubble.Mubble;
import hugman.mubble.objects.screen.screen_handler.TimeswapTableScreenHandler;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.util.Identifier;

public class MubbleContainerTypes {
	public static final Identifier TIMESWAP_TABLE = new Identifier(Mubble.MOD_ID, "timeswap_table");

	public static void init() {
		ContainerProviderRegistry.INSTANCE.registerFactory(TIMESWAP_TABLE, (syncId, identifier, player, buf) ->
		{
			return new TimeswapTableScreenHandler(syncId, player.inventory);
		});
	}
}