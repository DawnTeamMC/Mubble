package hugman.mubble.objects.command;

import java.util.Collection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.util.text.TranslationTextComponent;

public class MotionCommand
{
	public MotionCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(
			LiteralArgumentBuilder.<CommandSource>literal("motion")
			.requires((source) ->
			{
				return source.hasPermissionLevel(2);
			})
			.then(Commands.literal("add")
			.then(Commands.argument("targets", EntityArgument.entities())
			.then(Commands.argument("x", DoubleArgumentType.doubleArg())
			.then(Commands.argument("y", DoubleArgumentType.doubleArg())
			.then(Commands.argument("z", DoubleArgumentType.doubleArg())
			.executes((source) ->
			{
				return setMotion(source.getSource(), EntityArgument.getEntities(source, "targets"), DoubleArgumentType.getDouble(source, "x"), DoubleArgumentType.getDouble(source, "y"), DoubleArgumentType.getDouble(source, "z"), true);
			})
			)))))
			.then(Commands.literal("set")
			.then(Commands.argument("targets", EntityArgument.entities())
			.then(Commands.argument("x", DoubleArgumentType.doubleArg())
			.then(Commands.argument("y", DoubleArgumentType.doubleArg())
			.then(Commands.argument("z", DoubleArgumentType.doubleArg())
			.executes((source) ->
			{
				return setMotion(source.getSource(), EntityArgument.getEntities(source, "targets"), DoubleArgumentType.getDouble(source, "x"), DoubleArgumentType.getDouble(source, "y"), DoubleArgumentType.getDouble(source, "z"), false);
			})
			)))))
		);
	}
	
	private static int setMotion(CommandSource source, Collection<? extends Entity> targets, double x, double y, double z, boolean sum)
	{
		for(Entity entity : targets)
		{
			if(sum == true)
			{
				entity.setMotion(entity.getMotion().add(x, y, z));
			}
			else
			{
				entity.setMotion(x, y, z);
			}

			//entity.velocityChanged = true;
			if(entity instanceof ServerPlayerEntity)
			{
				ServerPlayerEntity player = (ServerPlayerEntity)entity;
				player.connection.sendPacket(new SEntityVelocityPacket(entity));
			}
		}
		
		final String parameter;
		if(sum == true)
		{
			parameter = "add";
		}
		else
		{
			parameter = "set";
		}
		if(targets.size() == 1)
		{
			source.sendFeedback(new TranslationTextComponent("commands.motion." + parameter + ".success.single", x, y, z, targets.iterator().next().getDisplayName()), true);
		}
		else
		{
			source.sendFeedback(new TranslationTextComponent("commands.motion." + parameter + ".success.multiple", x, y, z, targets.size()), true);
		}
		
		return targets.size();
	}
}