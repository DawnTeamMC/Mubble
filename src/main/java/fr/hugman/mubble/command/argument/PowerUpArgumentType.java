package fr.hugman.mubble.command.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import fr.hugman.mubble.power_up.PowerUp;
import fr.hugman.mubble.registry.MubbleRegistryKeys;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.registry.ReloadableRegistries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.ServerCommandSource;

public class PowerUpArgumentType extends RegistryEntryArgumentType<PowerUp> {
    public static final SuggestionProvider<ServerCommandSource> SUGGESTION_PROVIDER = (context, builder) -> {
        ReloadableRegistries.Lookup lookup = context.getSource().getServer().getReloadableRegistries();
        return CommandSource.suggestIdentifiers(lookup.getIds(MubbleRegistryKeys.POWER_UP), builder);
    };

    protected PowerUpArgumentType(CommandRegistryAccess registryAccess) {
        super(registryAccess, MubbleRegistryKeys.POWER_UP, PowerUp.ENTRY_CODEC);
    }

    public static PowerUpArgumentType of(CommandRegistryAccess registryAccess) {
        return new PowerUpArgumentType(registryAccess);
    }

    public static RegistryEntry<PowerUp> getPowerUp(CommandContext<ServerCommandSource> context, String argument) {
        return getArgument(context, argument);
    }

    private static <T> RegistryEntry<T> getArgument(CommandContext<ServerCommandSource> context, String argument) {
        return context.getArgument(argument, RegistryEntry.class);
    }
}