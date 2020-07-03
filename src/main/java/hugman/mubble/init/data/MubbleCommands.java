package hugman.mubble.init.data;

import hugman.mubble.object.command.FoodbarCommand;
import hugman.mubble.object.command.HealthCommand;
import hugman.mubble.object.command.MotionCommand;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class MubbleCommands {
	public static void init() {
		// Health
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> HealthCommand.register(dispatcher));
		// Motion
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> MotionCommand.register(dispatcher));
		// Foodbar
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> FoodbarCommand.register(dispatcher));
	}
}
