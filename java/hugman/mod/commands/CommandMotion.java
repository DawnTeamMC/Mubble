package hugman.mod.commands;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandMotion extends CommandBase
{
	//private final List<String> aliases = Lists.newArrayList(Reference.MODID, "motion");

	@Override
	public String getName()
	{
		return "motion";
	}
	
	/*@Override
	public List<String> getAliases() 
	{
		return aliases;
	}*/

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "commands.motion.usage";
	}
	
	@Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }
	
	@Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
		if (args.length == 1)
		{
			return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
		}
		else if (args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, new String[] {"set", "add"});
        }
        else return Collections.<String>emptyList();
    }

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
        if (args.length < 5)
        {
            throw new WrongUsageException("commands.motion.usage", new Object[0]);
        }
        else
        {
        	Entity entity = getEntity(server, sender, args[0]);
            String a = args[2];
    		String b = args[3];
    		String c = args[4];
    		double dX, dY, dZ;
    		
    		dX = CommandBase.parseDouble(a);
			dY = CommandBase.parseDouble(b);
			dZ = CommandBase.parseDouble(c);
    		
    		if ("set".equals(args[1]))
    		{
    			entity.motionX = dX;
    			entity.motionY = dY;
    			entity.motionZ = dZ;
    			entity.notify();
        		notifyCommandListener(sender, this, "commands.motion.success", new Object[] {entity.getName(), entity.motionX, entity.motionY, entity.motionZ});
    		}
    		else if ("add".equals(args[1]))
    		{
    			entity.motionX = entity.motionX + dX;
    			entity.motionY = entity.motionY + dY;
    			entity.motionZ = entity.motionZ + dZ;
    			entity.notify();
        		notifyCommandListener(sender, this, "commands.motion.success", new Object[] {entity.getName(), entity.motionX, entity.motionY, entity.motionZ});
    		}
        }
	}
	
	@Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}
