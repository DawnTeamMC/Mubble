package fr.hugman.mubble.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class MubbleCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> PowerUpCommand.register(dispatcher, registryAccess));
    }
}
