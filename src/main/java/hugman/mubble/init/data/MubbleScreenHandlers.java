package hugman.mubble.init.data;

import hugman.mubble.Mubble;
import hugman.mubble.object.screen.screen_handler.TimeswapTableScreenHandler;
import hugman.mubble.util.DataWriter;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class MubbleScreenHandlers {
	public static final ScreenHandlerType<TimeswapTableScreenHandler> TIMESWAP_TABLE = register("timeswap_table", TimeswapTableScreenHandler::new);

	private static <T extends ScreenHandler> ScreenHandlerType<T> register(String name, ScreenHandlerRegistry.SimpleClientHandlerFactory<T> factory) {
		DataWriter.entryNamesData.screen_handlers.add(Mubble.id(name).toString());
		DataWriter.entryCountsData.screen_handlers++;
		DataWriter.save();
		return ScreenHandlerRegistry.registerSimple(new Identifier(Mubble.MOD_ID, name), factory);
	}
}