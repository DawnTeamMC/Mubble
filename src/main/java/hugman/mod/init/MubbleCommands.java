package hugman.mod.init;

import hugman.mod.objects.command.CommandMotion;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/** 
 * Init class - used to initialize commands.
 */
public class MubbleCommands
{
	public static void addCommands(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandMotion());
	}
}
