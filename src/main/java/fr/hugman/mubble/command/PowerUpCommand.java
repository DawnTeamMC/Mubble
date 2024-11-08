package fr.hugman.mubble.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import fr.hugman.mubble.command.argument.PowerUpArgumentType;
import fr.hugman.mubble.power_up.PowerUp;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class PowerUpCommand {
    public static final String POWER_UP = "power_up";

    public static final String TARGET_ARG = "target";
    public static final String SET_ARG = "set";
    public static final String REMOVE_ARG = "remove";
    public static final String POWER_UP_ARG = "power_up";

    private static final SimpleCommandExceptionType UNCHANGED_POWER_UP_EXCEPTION = new SimpleCommandExceptionType(
            Text.translatable("commands.mubble.power_up.set.unchanged")
    );
    private static final SimpleCommandExceptionType REMOVE_NO_POWER_UP_EXCEPTION = new SimpleCommandExceptionType(
            Text.translatable("commands.mubble.power_up.remove.no_power_up")
    );

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(CommandManager.literal(POWER_UP)
                .requires(sc -> sc.hasPermissionLevel(2))
                .then(CommandManager.literal(SET_ARG)
                        .then(CommandManager.argument(TARGET_ARG, EntityArgumentType.player())
                                .then(CommandManager.argument(POWER_UP_ARG, PowerUpArgumentType.of(registryAccess)).suggests(PowerUpArgumentType.SUGGESTION_PROVIDER)
                                        .executes(cc -> setPowerUp(cc.getSource(), EntityArgumentType.getPlayer(cc, TARGET_ARG), PowerUpArgumentType.getPowerUp(cc, POWER_UP_ARG))))))
                .then(CommandManager.literal(REMOVE_ARG)
                        .then(CommandManager.argument(TARGET_ARG, EntityArgumentType.player())
                                .executes(cc -> removePowerUp(cc.getSource(), EntityArgumentType.getPlayer(cc, TARGET_ARG))))));
    }

    private static int setPowerUp(ServerCommandSource source, ServerPlayerEntity target, RegistryEntry<PowerUp> powerUp) throws CommandSyntaxException {
        var previousPowerUp = target.getPowerUp();

        if (previousPowerUp.isPresent()) {
            if (previousPowerUp.get().matches(powerUp)) {
                throw UNCHANGED_POWER_UP_EXCEPTION.create();
            }
        }

        target.setPowerUp(powerUp);
        var feedBackText = powerUp.value().name()
                .map(n -> Text.translatable("commands.mubble.power_up.set.success_named", target.getDisplayName(), n))
                .orElseGet(() -> Text.translatable("commands.mubble.power_up.set.success", target.getDisplayName()));
        source.sendFeedback(() -> feedBackText, true);
        return 1;
    }

    private static int removePowerUp(ServerCommandSource source, ServerPlayerEntity target) throws CommandSyntaxException {
        var previousPowerUp = target.getPowerUp();

        if (previousPowerUp.isEmpty()) {
            throw REMOVE_NO_POWER_UP_EXCEPTION.create();
        }

        target.clearPowerUp();
        var feedBackText = previousPowerUp.get().value().name()
                .map(n -> Text.translatable("commands.mubble.power_up.remove.success_named", target.getDisplayName(), n))
                .orElseGet(() -> Text.translatable("commands.mubble.power_up.remove.success", target.getDisplayName()));
        source.sendFeedback(() -> feedBackText, true);
        return 1;
    }
}
