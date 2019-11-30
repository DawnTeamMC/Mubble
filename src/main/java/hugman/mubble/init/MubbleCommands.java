package hugman.mubble.init;

import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;

import hugman.mubble.objects.command.FoodbarCommand;
import hugman.mubble.objects.command.HealthCommand;
import hugman.mubble.objects.command.ICommand;
import hugman.mubble.objects.command.MotionCommand;
import net.minecraft.command.CommandSource;

public class MubbleCommands
{
    public static final List<ICommand> COMMANDS = new ArrayList<ICommand>();

    public static final ICommand HEALTH = register(new HealthCommand());
    public static final ICommand MOTION = register(new MotionCommand());
    public static final ICommand FOODBAR = register(new FoodbarCommand());
    
    private static ICommand register(ICommand command)
    {
    	COMMANDS.add(command);
    	return command;
    }
    
    public static void registerCommands(CommandDispatcher<CommandSource> dispatcher)
    {
    	for(ICommand command : COMMANDS)
    	{
    		command.register(dispatcher);
    	}
	}
}
