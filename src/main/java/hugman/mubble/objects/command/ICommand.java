package hugman.mubble.objects.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.CommandSource;

public interface ICommand
{
	void register(CommandDispatcher<CommandSource> dispatcher);
}