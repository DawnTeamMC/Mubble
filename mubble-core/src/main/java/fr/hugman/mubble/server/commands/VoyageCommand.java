package fr.hugman.mubble.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.voyage.VoyageDefinition;
import fr.hugman.mubble.world.voyage.VoyageSeeds;
import fr.hugman.mubble.world.voyage.session.VoyageSession;
import fr.hugman.mubble.world.voyage.session.VoyageSessions;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

/**
 * {@code /voyage}: the whole player-facing surface of a voyage.
 *
 * <pre>{@code
 * /voyage start <voyage_id> [seed]
 * /voyage abandon
 * /voyage status
 * }</pre>
 *
 * <p>Thin on purpose. Everything it does is one call into {@link VoyageSessions}, which owns what a
 * voyage <em>is</em>; this only turns arguments into that call and turns a refusal into a sentence
 * somebody can act on.
 *
 * <p><strong>No permission level.</strong> Starting a voyage is a player-facing action rather than
 * an operator one, so the POC lets anyone run it. If that ever needs gating it goes on the root
 * literal below, as {@code .requires(Commands.hasPermission(...))} — but note that gating the root
 * would also hide {@code status} and {@code abandon} from a player already inside a voyage, so it
 * more likely belongs on {@code start} alone.
 */
public final class VoyageCommand {
    private static final DynamicCommandExceptionType UNKNOWN_VOYAGE = new DynamicCommandExceptionType(
            id -> Component.literal("No voyage '" + id + "' is loaded. Tab-completion lists the ones that are.")
    );
    private static final SimpleCommandExceptionType ALREADY_IN_VOYAGE = new SimpleCommandExceptionType(
            Component.literal("You are already in a voyage. Finish it, or use /voyage abandon.")
    );
    private static final SimpleCommandExceptionType NOT_IN_VOYAGE = new SimpleCommandExceptionType(
            Component.literal("You are not in a voyage.")
    );

    private VoyageCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("voyage")
                .then(Commands.literal("start")
                        .then(Commands.argument("voyage", IdentifierArgument.id())
                                .suggests((cc, builder) -> SharedSuggestionProvider.suggestResource(
                                        cc.getSource().registryAccess().lookupOrThrow(MubbleRegistries.VOYAGE).keySet(), builder))
                                .executes(cc -> start(cc.getSource().getPlayerOrException(),
                                        IdentifierArgument.getId(cc, "voyage"), VoyageSeeds.random()))
                                .then(Commands.argument("seed", LongArgumentType.longArg())
                                        .executes(cc -> start(cc.getSource().getPlayerOrException(),
                                                IdentifierArgument.getId(cc, "voyage"), LongArgumentType.getLong(cc, "seed"))))))
                .then(Commands.literal("abandon")
                        .executes(cc -> abandon(cc.getSource().getPlayerOrException())))
                .then(Commands.literal("status")
                        .executes(cc -> status(cc.getSource(), cc.getSource().getPlayerOrException()))));
    }

    private static int start(ServerPlayer player, Identifier voyageId, long seed) throws CommandSyntaxException {
        VoyageSessions sessions = VoyageSessions.get(player.level().getServer());
        // Checked before the lookup so that a player already inside a voyage gets told that, rather
        // than being told the id they fat-fingered does not exist.
        if (sessions.isInVoyage(player)) {
            throw ALREADY_IN_VOYAGE.create();
        }

        VoyageDefinition voyage = player.level().getServer().registryAccess()
                .lookupOrThrow(MubbleRegistries.VOYAGE).getValue(voyageId);
        if (voyage == null) {
            throw UNKNOWN_VOYAGE.create(voyageId);
        }

        // The session reports the seed on the way in, which is what makes a generated one usable:
        // there is no other way for a player to learn the number they would need to share.
        sessions.start(player, voyageId, voyage, seed);
        return 1;
    }

    private static int abandon(ServerPlayer player) throws CommandSyntaxException {
        VoyageSessions sessions = VoyageSessions.get(player.level().getServer());
        if (!sessions.isInVoyage(player)) {
            throw NOT_IN_VOYAGE.create();
        }
        sessions.abandon(player);
        return 1;
    }

    private static int status(CommandSourceStack source, ServerPlayer player) throws CommandSyntaxException {
        VoyageSession session = VoyageSessions.get(player.level().getServer()).sessionOf(player);
        if (session == null) {
            throw NOT_IN_VOYAGE.create();
        }

        source.sendSuccess(() -> Component.empty()
                .append(session.voyage().displayName())
                .append(Component.literal(" — trial " + session.trialNumber() + " of " + session.trialCount()))
                // The node key as well as the trial number: with branching routes the number no
                // longer says where you are, only how far.
                .append(Component.literal(" — at '" + session.nodeKey() + "'"))
                .append(Component.literal(" — seed " + session.seed()).withStyle(ChatFormatting.GRAY)), false);
        return session.trialNumber();
    }
}
