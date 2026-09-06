package fr.hugman.mubble.super_mario.commands;

import fr.hugman.mubble.super_mario.server.commands.FreezeCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class SuperMarioCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> FreezeCommand.register(dispatcher));
    }
}
