package fr.hugman.mubble.commands;

import fr.hugman.mubble.server.commands.PowerUpCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class MubbleCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> PowerUpCommand.register(dispatcher, registryAccess));
    }
}
