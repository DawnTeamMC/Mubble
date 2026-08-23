package fr.hugman.mubble.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.voyage.environment.EnvironmentController;
import fr.hugman.mubble.world.voyage.level.TrialInstance;
import fr.hugman.mubble.world.voyage.level.VoyageWorldHandle;
import fr.hugman.mubble.world.voyage.level.fantasy.VoyageWorlds;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.level.block.Blocks;

/**
 * Manual test for the phase 0 runtime level spike.
 *
 * <p>{@code /voyagespike open [seed]} builds a level, drops a stone platform under the player and
 * teleports them onto it. {@code /voyagespike close} brings them back to where they were and
 * deletes the level. {@code /voyagespike status} reports what is open.
 *
 * <p>{@code /voyagespike environment <profile>} applies an environment profile to whatever level the
 * player is standing in, and {@code /voyagespike environment clear} takes it away. Phase 2 attaches
 * profiles to trials properly; until then this is how the profile stack gets exercised in-game.
 *
 * <p>Deliberately throwaway. Phase 3 owns player state and phase 4 owns the real {@code /voyage}
 * command; this exists only so the level lifecycle can be exercised in-game on its own, and should
 * be deleted once {@code /voyage start} works.
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

    private static final int PLATFORM_RADIUS = 4;
    private static final int PLATFORM_Y = 64;

    /** Only what phase 0 needs: where the player was, and which level they went to. */
    private record Session(VoyageWorldHandle handle, ServerLevel returnLevel, double x, double y, double z, float yRot, float xRot) {
    }

    private static final Map<UUID, Session> SESSIONS = new HashMap<>();

    private VoyageSpikeCommand() {
    }

    /**
     * Forgets every session when the server stops.
     *
     * <p>{@link #SESSIONS} is static, so without this it survives into the next server in the same
     * JVM — quit to title, load a world again, and the spike still believes you are inside a level
     * that was deleted on shutdown. Phase 3 replaces this with saved data keyed by player UUID,
     * which has to survive a restart rather than be discarded by one.
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
                VoyageWorlds.get(newPlayer.level().getServer()).close(session.handle());
            }
        });
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("voyagespike")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("open")
                        .executes(cc -> open(cc.getSource().getPlayerOrException(), 0L))
                        .then(Commands.argument("seed", LongArgumentType.longArg())
                                .executes(cc -> open(cc.getSource().getPlayerOrException(), LongArgumentType.getLong(cc, "seed")))))
                .then(Commands.literal("close")
                        .executes(cc -> close(cc.getSource().getPlayerOrException())))
                .then(Commands.literal("status")
                        .executes(cc -> status(cc.getSource())))
                .then(Commands.literal("environment")
                        .then(Commands.literal("clear")
                                .executes(cc -> clearEnvironment(cc.getSource().getPlayerOrException())))
                        .then(Commands.argument("profile", IdentifierArgument.id())
                                .suggests((cc, builder) -> SharedSuggestionProvider.suggestResource(
                                        cc.getSource().registryAccess().lookupOrThrow(MubbleRegistries.ENVIRONMENT_PROFILE).keySet(), builder))
                                .executes(cc -> setEnvironment(
                                        cc.getSource().getPlayerOrException(),
                                        IdentifierArgument.getId(cc, "profile"))))));
    }

    private static int open(ServerPlayer player, long seed) throws CommandSyntaxException {
        if (SESSIONS.containsKey(player.getUUID())) {
            throw ALREADY_OPEN.create();
        }

        TrialInstance trial = new TrialInstance(fr.hugman.mubble.Mubble.id("spike"), "spike");
        VoyageWorldHandle handle = VoyageWorlds.get(player.level().getServer()).open(trial, seed);
        ServerLevel level = handle.level();

        for (int x = -PLATFORM_RADIUS; x <= PLATFORM_RADIUS; x++) {
            for (int z = -PLATFORM_RADIUS; z <= PLATFORM_RADIUS; z++) {
                level.setBlockAndUpdate(new BlockPos(x, PLATFORM_Y, z), Blocks.STONE.defaultBlockState());
            }
        }

        SESSIONS.put(player.getUUID(), new Session(
                handle, player.level(), player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot()
        ));

        player.teleportTo(level, 0.5D, PLATFORM_Y + 1, 0.5D, Set.of(), player.getYRot(), player.getXRot(), false);
        player.sendSystemMessage(Component.literal("Opened " + handle.dimension().identifier() + " (seed " + seed + ")"));
        return 1;
    }

    private static int close(ServerPlayer player) throws CommandSyntaxException {
        Session session = SESSIONS.remove(player.getUUID());
        if (session == null) {
            throw NOTHING_OPEN.create();
        }

        // Order matters: the player leaves first, then the level is destroyed.
        player.teleportTo(session.returnLevel(), session.x(), session.y(), session.z(), Set.of(), session.yRot(), session.xRot(), false);
        VoyageWorlds.get(player.level().getServer()).close(session.handle());

        player.sendSystemMessage(Component.literal("Closed " + session.handle().dimension().identifier()));
        return 1;
    }

    private static int setEnvironment(ServerPlayer player, Identifier profile) throws CommandSyntaxException {
        if (!EnvironmentController.apply(player.level(), profile, EnvironmentAttributeMap.EMPTY)) {
            throw UNKNOWN_PROFILE.create(profile, EnvironmentController.describeLoaded(player.level().getServer()));
        }
        player.sendSystemMessage(Component.literal("Applied environment " + profile));
        return 1;
    }

    private static int clearEnvironment(ServerPlayer player) {
        EnvironmentController.clear(player.level());
        player.sendSystemMessage(Component.literal("Cleared environment"));
        return 1;
    }

    private static int status(CommandSourceStack source) {
        if (SESSIONS.isEmpty()) {
            source.sendSuccess(() -> Component.literal("No spike levels open"), false);
            return 0;
        }
        for (Map.Entry<UUID, Session> entry : SESSIONS.entrySet()) {
            Component line = Component.literal(entry.getKey() + " -> " + entry.getValue().handle().dimension().identifier());
            source.sendSuccess(() -> line, false);
        }
        return SESSIONS.size();
    }
}
