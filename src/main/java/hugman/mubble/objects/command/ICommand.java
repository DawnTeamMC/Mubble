package hugman.mubble.objects.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;

public interface ICommand
{
	void register(CommandDispatcher<CommandSource> dispatcher);
}