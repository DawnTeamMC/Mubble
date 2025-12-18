package fr.hugman.mubble.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import fr.hugman.mubble.command.argument.PowerUpArgumentType;
import fr.hugman.mubble.power_up.PowerUp;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class PowerUpCommand {
    public static final String POWER_UP = "power_up";

    public static final String TARGET_ARG = "target";
    public static final String SET_ARG = "set";
    public static final String REMOVE_ARG = "remove";
    public static final String POWER_UP_ARG = "power_up";

    private static final SimpleCommandExceptionType UNCHANGED_POWER_UP_EXCEPTION = new SimpleCommandExceptionType(
            Component.translatable("commands.mubble.power_up.set.unchanged")
    );
    private static final SimpleCommandExceptionType REMOVE_NO_POWER_UP_EXCEPTION = new SimpleCommandExceptionType(
            Component.translatable("commands.mubble.power_up.remove.no_power_up")
    );

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess) {
        dispatcher.register(Commands.literal(POWER_UP)
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal(SET_ARG)
                        .then(Commands.argument(TARGET_ARG, EntityArgument.player())
                                .then(Commands.argument(POWER_UP_ARG, PowerUpArgumentType.of(registryAccess))
                                        .executes(cc -> setPowerUp(cc.getSource(), EntityArgument.getPlayer(cc, TARGET_ARG), PowerUpArgumentType.getPowerUp(cc, POWER_UP_ARG))))))
                .then(Commands.literal(REMOVE_ARG)
                        .then(Commands.argument(TARGET_ARG, EntityArgument.player())
                                .executes(cc -> removePowerUp(cc.getSource(), EntityArgument.getPlayer(cc, TARGET_ARG))))));
    }

    private static int setPowerUp(CommandSourceStack source, ServerPlayer target, Holder<PowerUp> powerUp) throws CommandSyntaxException {
        var previousPowerUp = target.getPowerUp();

        if (previousPowerUp.isPresent()) {
            if (previousPowerUp.get().is(powerUp)) {
                throw UNCHANGED_POWER_UP_EXCEPTION.create();
            }
        }

        target.setPowerUp(powerUp);
        var feedBackText = powerUp.value().name()
                .map(n -> Component.translatable("commands.mubble.power_up.set.success_named", target.getDisplayName(), n))
                .orElseGet(() -> Component.translatable("commands.mubble.power_up.set.success", target.getDisplayName()));
        source.sendSuccess(() -> feedBackText, true);
        return 1;
    }

    private static int removePowerUp(CommandSourceStack source, ServerPlayer target) throws CommandSyntaxException {
        var previousPowerUp = target.getPowerUp();

        if (previousPowerUp.isEmpty()) {
            throw REMOVE_NO_POWER_UP_EXCEPTION.create();
        }

        target.clearPowerUp();
        var feedBackText = previousPowerUp.get().value().name()
                .map(n -> Component.translatable("commands.mubble.power_up.remove.success_named", target.getDisplayName(), n))
                .orElseGet(() -> Component.translatable("commands.mubble.power_up.remove.success", target.getDisplayName()));
        source.sendSuccess(() -> feedBackText, true);
        return 1;
    }
}
