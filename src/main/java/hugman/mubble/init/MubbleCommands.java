package hugman.mubble.init;

import hugman.mubble.objects.command.FoodbarCommand;
import hugman.mubble.objects.command.HealthCommand;
import hugman.mubble.objects.command.MotionCommand;
import net.fabricmc.fabric.api.registry.CommandRegistry;

public class MubbleCommands
{
    public static void init()
    {
    	// Health
    	CommandRegistry.INSTANCE.register(false, HealthCommand::register);
    	
    	// Motion
    	CommandRegistry.INSTANCE.register(false, MotionCommand::register);
    	
    	// Foodbar
    	CommandRegistry.INSTANCE.register(false, FoodbarCommand::register);
    }
}
