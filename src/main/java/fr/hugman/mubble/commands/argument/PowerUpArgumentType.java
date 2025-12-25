package fr.hugman.mubble.commands.argument;

import com.mojang.brigadier.context.CommandContext;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.power_up.PowerUp;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceOrIdArgument;
import net.minecraft.core.Holder;

public class PowerUpArgumentType extends ResourceOrIdArgument<PowerUp> {
    protected PowerUpArgumentType(CommandBuildContext registryAccess) {
        super(registryAccess, MubbleRegistries.POWER_UP, PowerUp.CODEC);
    }

    public static PowerUpArgumentType of(CommandBuildContext registryAccess) {
        return new PowerUpArgumentType(registryAccess);
    }

    public static Holder<PowerUp> getPowerUp(CommandContext<CommandSourceStack> context, String argument) {
        return getResource(context, argument);
    }

    private static <T> Holder<T> getResource(CommandContext<CommandSourceStack> context, String argument) {
        return context.getArgument(argument, Holder.class);
    }
}