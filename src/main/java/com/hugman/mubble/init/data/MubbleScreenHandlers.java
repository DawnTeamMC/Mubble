package com.hugman.mubble.init.data;

import com.hugman.mubble.Mubble;
import com.hugman.mubble.object.screen.screen_handler.TimeswapTableScreenHandler;
import com.hugman.mubble.util.DataWriter;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class MubbleScreenHandlers {
	public static final ScreenHandlerType<TimeswapTableScreenHandler> TIMESWAP_TABLE = register("timeswap_table", TimeswapTableScreenHandler::new);

	private static <T extends ScreenHandler> ScreenHandlerType<T> register(String name, ScreenHandlerRegistry.SimpleClientHandlerFactory<T> factory) {
		DataWriter.screenHandlersEntries.add(Mubble.id(name));
		return ScreenHandlerRegistry.registerSimple(new Identifier(Mubble.MOD_ID, name), factory);
	}
}