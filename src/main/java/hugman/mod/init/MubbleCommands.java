package hugman.mod.init;

import hugman.mod.objects.command.CommandMotion;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
