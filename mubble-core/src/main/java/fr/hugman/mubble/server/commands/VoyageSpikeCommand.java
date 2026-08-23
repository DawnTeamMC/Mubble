package fr.hugman.mubble.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.voyage.VoyageDefinition;
import fr.hugman.mubble.world.voyage.VoyageSeeds;
import fr.hugman.mubble.world.voyage.environment.EnvironmentController;
import fr.hugman.mubble.world.voyage.level.VoyageWorldHandle;
import fr.hugman.mubble.world.voyage.level.fantasy.VoyageWorlds;
import fr.hugman.mubble.world.voyage.trial.TrialDefinition;
import fr.hugman.mubble.world.voyage.trial.TrialInstance;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import fr.hugman.mubble.world.voyage.VoyageReward;
import net.minecraft.world.phys.Vec3;

/**
 * Manual test for the voyage spike.
 *
 * <p>{@code /voyagespike open <trial> [seed]} opens a level for a trial definition, builds its
 * platform, applies its environment resolved against the node seed, and teleports the player in.
 * {@code close} brings them back and deletes the level. {@code status} reports what is open, and
 * {@code voyage <id>} reads a voyage definition back out without running it.
 *
 * <p>{@code environment <profile> [seed]} applies a profile to whatever level the player is standing
 * in, which is how a profile gets looked at without opening anything.
 *
 * <p>Deliberately throwaway. Phase 3 owns player state and the session that walks a voyage, phase 4
 * owns the real {@code /voyage} command; this exists only so the pieces can be exercised in-game on
 * their own, and should be deleted once {@code /voyage start} works.
 */
public final class VoyageSpikeCommand {
    private static final SimpleCommandExceptionType ALREADY_OPEN = new SimpleCommandExceptionType(
            Component.literal("You already have a spike level open")
    );
    private static final SimpleCommandExceptionType NOTHING_OPEN = new SimpleCommandExceptionType(
            Component.literal("You have no spike level open")
    );
    private static final Dynamic2CommandExceptionType UNKNOWN_PROFILE = new Dynamic2CommandExceptionType(
            (profile, loaded) -> Component.literal("No environment profile '" + profile + "'. " + loaded)
    );
    private static final Dynamic2CommandExceptionType UNKNOWN_DEFINITION = new Dynamic2CommandExceptionType(
            (what, id) -> Component.literal("No " + what + " '" + id + "' is loaded")
    );

    /**
     * The node path the spike pretends to be at.
     *
     * <p>A real trial gets its path from its position in the voyage tree. The spike is not in one, so
     * it uses a constant: the same trial id and seed always give the same look, which is the property
     * being tested.
     */
    private static final String SPIKE_NODE_PATH = "spike";

    /** Only what the spike needs: where the player was, and which level they went to. */
    private record Session(VoyageWorldHandle handle, Identifier trial, ServerLevel returnLevel, double x, double y, double z, float yRot, float xRot) {
    }

    private static final Map<UUID, Session> SESSIONS = new HashMap<>();

    private VoyageSpikeCommand() {
    }

    /**
     * Forgets every session when the server stops, and when a player dies inside one.
     *
     * <p>{@link #SESSIONS} is static, so without the first of these it survives into the next server
     * in the same JVM — quit to title, load a world again, and the spike still believes you are
     * inside a level that was deleted on shutdown. Phase 3 replaces this with saved data keyed by
     * player UUID, which has to survive a restart rather than be discarded by one.
     */
    public static void register() {
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> SESSIONS.clear());

        // Dying takes you out of the level without going through close(), which used to leave the
        // session open: the level stayed alive, /voyagespike open refused to start another, and a
        // later close() teleported you from wherever you respawned back to where you had been
        // standing before the spike. Vanilla has already chosen where a dead player goes, so this
        // only destroys the level and forgets the session — it does not move anyone.
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            Session session = SESSIONS.remove(newPlayer.getUUID());
            if (session != null) {
                destroy(newPlayer.level().getServer(), session.handle());
            }
        });
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("voyagespike")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("open")
                        .then(Commands.argument("trial", IdentifierArgument.id())
                                .suggests((cc, builder) -> suggest(cc.getSource(), MubbleRegistries.TRIAL, builder))
                                .executes(cc -> open(cc.getSource().getPlayerOrException(), IdentifierArgument.getId(cc, "trial"), 0L))
                                .then(Commands.argument("seed", LongArgumentType.longArg())
                                        .executes(cc -> open(cc.getSource().getPlayerOrException(),
                                                IdentifierArgument.getId(cc, "trial"), LongArgumentType.getLong(cc, "seed"))))))
                .then(Commands.literal("close")
                        .executes(cc -> close(cc.getSource().getPlayerOrException())))
                .then(Commands.literal("status")
                        .executes(cc -> status(cc.getSource())))
                .then(Commands.literal("voyage")
                        .then(Commands.argument("voyage", IdentifierArgument.id())
                                .suggests((cc, builder) -> suggest(cc.getSource(), MubbleRegistries.VOYAGE, builder))
                                .executes(cc -> describeVoyage(cc.getSource(), IdentifierArgument.getId(cc, "voyage")))))
                .then(Commands.literal("environment")
                        .then(Commands.literal("clear")
                                .executes(cc -> clearEnvironment(cc.getSource().getPlayerOrException())))
                        .then(Commands.argument("profile", IdentifierArgument.id())
                                .suggests((cc, builder) -> suggest(cc.getSource(), MubbleRegistries.ENVIRONMENT_PROFILE, builder))
                                .executes(cc -> setEnvironment(cc.getSource().getPlayerOrException(),
                                        IdentifierArgument.getId(cc, "profile"), 0L))
                                .then(Commands.argument("seed", LongArgumentType.longArg())
                                        .executes(cc -> setEnvironment(cc.getSource().getPlayerOrException(),
                                                IdentifierArgument.getId(cc, "profile"), LongArgumentType.getLong(cc, "seed")))))));
    }

    private static int open(ServerPlayer player, Identifier trialId, long seed) throws CommandSyntaxException {
        if (SESSIONS.containsKey(player.getUUID())) {
            throw ALREADY_OPEN.create();
        }

        TrialDefinition definition = lookup(player.level().getServer(), MubbleRegistries.TRIAL, trialId, "trial");
        TrialInstance trial = TrialInstance.of(trialId, SPIKE_NODE_PATH, definition, seed);

        VoyageWorldHandle handle = VoyageWorlds.get(player.level().getServer()).open(trial);
        ServerLevel level = handle.level();
        definition.platform().place(level, 0, 0);

        // Always present: a trial may only reference a registered profile, never inline one, because
        // the client is told which profile to apply by id. See EnvironmentProfile.CODEC.
        Identifier profileId = definition.environment().unwrapKey().orElseThrow().identifier();
        EnvironmentController.apply(level, profileId, trial.nodeSeed(), EnvironmentAttributeMap.EMPTY);

        SESSIONS.put(player.getUUID(), new Session(
                handle, trialId, player.level(), player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot()
        ));

        Vec3 spawn = definition.platform().spawnPos(0, 0);
        player.teleportTo(level, spawn.x(), spawn.y(), spawn.z(), Set.of(), player.getYRot(), player.getXRot(), false);
        // The environment was applied before the player was in the level, so nothing was sent to them
        // then. This is the same call phase 3's session will make on every trial entry.
        EnvironmentController.sendTo(player);

        player.sendSystemMessage(Component.literal("Opened " + handle.dimension().identifier() + " for ")
                .append(definition.displayName())
                .append(Component.literal(" (seed " + seed + ", node seed " + trial.nodeSeed() + ")")));
        return 1;
    }

    private static int close(ServerPlayer player) throws CommandSyntaxException {
        Session session = SESSIONS.remove(player.getUUID());
        if (session == null) {
            throw NOTHING_OPEN.create();
        }

        // Order matters: the player leaves first, then the level is destroyed.
        player.teleportTo(session.returnLevel(), session.x(), session.y(), session.z(), Set.of(), session.yRot(), session.xRot(), false);
        destroy(player.level().getServer(), session.handle());
        EnvironmentController.sendTo(player);

        player.sendSystemMessage(Component.literal("Closed " + session.handle().dimension().identifier()));
        return 1;
    }

    /**
     * Ends a trial level: drops its environment, then deletes it.
     *
     * <p>Both in that order and both every time. The controller keeps a level until told otherwise,
     * and deleting one out from under it would leave an entry for a level nobody can reach. Phase 3's
     * session inherits this pairing.
     */
    private static void destroy(MinecraftServer server, VoyageWorldHandle handle) {
        if (handle.isOpen()) {
            EnvironmentController.clear(handle.level());
        }
        VoyageWorlds.get(server).close(handle);
    }

    private static int setEnvironment(ServerPlayer player, Identifier profile, long seed) throws CommandSyntaxException {
        if (!EnvironmentController.apply(player.level(), profile, VoyageSeeds.node(seed, SPIKE_NODE_PATH), EnvironmentAttributeMap.EMPTY)) {
            throw UNKNOWN_PROFILE.create(profile, EnvironmentController.describeLoaded(player.level().getServer()));
        }
        player.sendSystemMessage(Component.literal("Applied environment " + profile + " (seed " + seed + ")"));
        return 1;
    }

    private static int clearEnvironment(ServerPlayer player) {
        EnvironmentController.clear(player.level());
        player.sendSystemMessage(Component.literal("Cleared environment"));
        return 1;
    }

    /** Reads a voyage back out. Running one is phase 3; this only proves the file parsed. */
    private static int describeVoyage(CommandSourceStack source, Identifier voyageId) throws CommandSyntaxException {
        VoyageDefinition voyage = lookup(source.getServer(), MubbleRegistries.VOYAGE, voyageId, "voyage");

        source.sendSuccess(() -> Component.literal("").append(voyage.displayName())
                .append(Component.literal(" — " + voyage.trials().size() + " trial(s)")), false);
        for (int i = 0; i < voyage.trials().size(); i++) {
            int index = i;
            Identifier id = voyage.trials().get(i).unwrapKey().orElseThrow().identifier();
            source.sendSuccess(() -> Component.literal("  " + VoyageDefinition.nodePath(index) + ": " + id), false);
        }
        for (VoyageReward reward : voyage.completionRewards()) {
            source.sendSuccess(() -> Component.literal("  reward: " + reward.count() + "x ")
                    .append(reward.toStack().getHoverName()), false);
        }
        return voyage.trials().size();
    }

    private static int status(CommandSourceStack source) {
        if (SESSIONS.isEmpty()) {
            source.sendSuccess(() -> Component.literal("No spike levels open"), false);
            return 0;
        }
        for (Map.Entry<UUID, Session> entry : SESSIONS.entrySet()) {
            Session session = entry.getValue();
            Component line = Component.literal(entry.getKey() + " -> " + session.handle().dimension().identifier()
                    + " (" + session.trial() + ")");
            source.sendSuccess(() -> line, false);
        }
        return SESSIONS.size();
    }

    private static <T> T lookup(MinecraftServer server, ResourceKey<Registry<T>> registryKey, Identifier id, String what)
            throws CommandSyntaxException {
        T value = server.registryAccess().lookupOrThrow(registryKey).getValue(id);
        if (value == null) {
            throw UNKNOWN_DEFINITION.create(what, id);
        }
        return value;
    }

    private static <T> CompletableFuture<Suggestions> suggest(
            CommandSourceStack source, ResourceKey<Registry<T>> registryKey, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggestResource(source.registryAccess().lookupOrThrow(registryKey).keySet(), builder);
    }
}
