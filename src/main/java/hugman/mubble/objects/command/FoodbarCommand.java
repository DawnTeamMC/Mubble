package hugman.mubble.objects.command;

import java.util.Collection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.FoodStats;
import net.minecraft.util.text.TranslationTextComponent;

public class FoodbarCommand implements ICommand
{
	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(
			LiteralArgumentBuilder.<CommandSource>literal("foodbar")
			.requires((source) ->
			{
				return source.hasPermissionLevel(2);
			})
			.then(Commands.literal("add")
				.then(Commands.literal("food")
				.then(Commands.argument("targets", EntityArgument.players())
				.then(Commands.argument("amount", IntegerArgumentType.integer())
				.executes((source) ->
				{
					return setFood(source.getSource(), EntityArgument.getPlayers(source, "targets"), IntegerArgumentType.getInteger(source, "amount"), true);
				})
				)))
				.then(Commands.literal("saturation")
				.then(Commands.argument("targets", EntityArgument.players())
				.then(Commands.argument("amount", FloatArgumentType.floatArg())
				.executes((source) ->
				{
					return setSaturation(source.getSource(), EntityArgument.getPlayers(source, "targets"), FloatArgumentType.getFloat(source, "amount"), true);
				})
				)))
			)
			.then(Commands.literal("set")
				.then(Commands.literal("food")
				.then(Commands.argument("targets", EntityArgument.players())
				.then(Commands.argument("amount", IntegerArgumentType.integer(0, 20))
				.executes((source) ->
				{
					return setFood(source.getSource(), EntityArgument.getPlayers(source, "targets"), IntegerArgumentType.getInteger(source, "amount"), false);
				})
				)))
				.then(Commands.literal("saturation")
				.then(Commands.argument("targets", EntityArgument.players())
				.then(Commands.argument("amount", FloatArgumentType.floatArg(0.0f))
				.executes((source) ->
				{
					return setSaturation(source.getSource(), EntityArgument.getPlayers(source, "targets"), FloatArgumentType.getFloat(source, "amount"), false);
				})
				)))
			)
		);
	}
	
	private static int setFood(CommandSource source, Collection<ServerPlayerEntity> targets, int amount, boolean sum)
	{
		for(ServerPlayerEntity entity : targets)
		{
			FoodStats stats = entity.getFoodStats();
			if(sum == true)
			{
				stats.setFoodLevel(amount + stats.getFoodLevel());
			}
			else
			{
				stats.setFoodLevel(amount);
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
			source.sendFeedback(new TranslationTextComponent("commands.foodbar." + parameter + ".food.success.single", amount, targets.iterator().next().getDisplayName()), true);
		}
		else
		{
			source.sendFeedback(new TranslationTextComponent("commands.foodbar." + parameter + ".food.success.multiple", amount, targets.size()), true);
		}
		
		return targets.size();
	}
	
	private static int setSaturation(CommandSource source, Collection<ServerPlayerEntity> targets, float amount, boolean sum)
	{
		for(ServerPlayerEntity entity : targets)
		{
			FoodStats stats = entity.getFoodStats();
			if(sum == true)
			{
				stats.setFoodSaturationLevel(amount + stats.getFoodLevel());
			}
			else
			{
				stats.setFoodSaturationLevel(amount);
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
			source.sendFeedback(new TranslationTextComponent("commands.foodbar." + parameter + ".saturation.success.single", amount, targets.iterator().next().getDisplayName()), true);
		}
		else
		{
			source.sendFeedback(new TranslationTextComponent("commands.foodbar." + parameter + ".saturation.success.multiple", amount, targets.size()), true);
		}
		
		return targets.size();
	}
}