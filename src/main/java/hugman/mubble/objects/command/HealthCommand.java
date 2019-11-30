package hugman.mubble.objects.command;

import java.util.Collection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TranslationTextComponent;

public class HealthCommand implements ICommand
{
	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(
			LiteralArgumentBuilder.<CommandSource>literal("health")
			.requires((source) ->
			{
				return source.hasPermissionLevel(2);
			})
			.then(Commands.literal("add")
			.then(Commands.argument("targets", EntityArgument.entities())
			.then(Commands.argument("amount", FloatArgumentType.floatArg())
			.executes((source) ->
			{
				return setHealth(source.getSource(), EntityArgument.getEntities(source, "targets"), FloatArgumentType.getFloat(source, "amount"), true);
			})
			)))
			.then(Commands.literal("set")
			.then(Commands.argument("targets", EntityArgument.entities())
			.then(Commands.argument("amount", FloatArgumentType.floatArg(0.0f))
			.executes((source) ->
			{
				return setHealth(source.getSource(), EntityArgument.getEntities(source, "targets"), FloatArgumentType.getFloat(source, "amount"), false);
			})
			)))
		);
	}
	
	private static int setHealth(CommandSource source, Collection<? extends Entity> targets, float amount, boolean sum)
	{
		int finalTargetAmount = 0;
		for(Entity entity : targets)
		{
			if(entity instanceof LivingEntity)
			{
				finalTargetAmount++;
				LivingEntity livingEntity = (LivingEntity) entity;
				if(sum == true)
				{
					if(amount > 0.0F)
					{
						livingEntity.heal(amount);
					}
					else if(amount < 0.0F)
					{
						livingEntity.attackEntityFrom(DamageSource.OUT_OF_WORLD, amount * -1.0f);
					}
				}
				else
				{
					if(livingEntity.getHealth() > 0.0F)
					{
						if(amount == 0.0F)
						{
							livingEntity.onKillCommand();
						}
						else
						{
							livingEntity.setHealth(amount);
						}
					}
				}
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
			source.sendFeedback(new TranslationTextComponent("commands.health." + parameter + ".success.single", amount, targets.iterator().next().getDisplayName()), true);
		}
		else
		{
			source.sendFeedback(new TranslationTextComponent("commands.health." + parameter + ".success.multiple", amount, finalTargetAmount), true);
		}
		
		return finalTargetAmount;
	}
}