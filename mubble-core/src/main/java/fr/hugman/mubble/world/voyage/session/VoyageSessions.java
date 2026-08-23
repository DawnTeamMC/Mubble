package fr.hugman.mubble.world.voyage.session;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.world.voyage.VoyageDefinition;
import fr.hugman.mubble.world.voyage.environment.EnvironmentController;
import fr.hugman.mubble.world.voyage.level.VoyageWorldHandle;
import fr.hugman.mubble.world.voyage.level.VoyageWorldProvider;
import fr.hugman.mubble.world.voyage.level.fantasy.FantasyVoyageWorldProvider;
import fr.hugman.mubble.world.voyage.trial.TrialDefinition;
import fr.hugman.mubble.world.voyage.trial.TrialInstance;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * Runs voyages: who is in one, what happens to them, and how they get their life back.
 *
 * <p>Owns the {@link VoyageWorldProvider} as well, which is what phase 0 said would happen once this
 * class existed. Levels are opened per trial and deleted on the way out, so nothing here outlives a
 * run except the {@link PlayerStash}.
 *
 * <h2>Getting out</h2>
 *
 * <p>There are five ways a voyage ends and all of them go through {@link #end}: finishing, failing,
 * abandoning, disconnecting, and the server stopping. A sixth, the server <em>crashing</em>, cannot
 * run code at all, and is the reason the stash is on disk.
 *
 * <p>Disconnecting and stopping restore the player but deliberately <strong>leave the stash</strong>
 * for {@link #recoverOnJoin} to consume. Restoring twice is harmless — the second one sets the same
 * values — whereas consuming the stash before being certain the player's own data was written is how
 * an inventory disappears. The stash is only taken at a moment when the player is definitely there
 * to receive it.
 */
public final class VoyageSessions {
    /** How a voyage ended, which is only ever used to decide what to say about it. */
    public enum Outcome {
        COMPLETED("Voyage complete.", ChatFormatting.GREEN),
        FAILED("Voyage lost.", ChatFormatting.RED),
        ABANDONED("Voyage abandoned.", ChatFormatting.YELLOW),
        DISCONNECTED(null, null),
        SERVER_STOPPING(null, null);

        private final @Nullable String message;
        private final @Nullable ChatFormatting colour;

        Outcome(@Nullable String message, @Nullable ChatFormatting colour) {
            this.message = message;
            this.colour = colour;
        }
    }

    private static @Nullable VoyageSessions current;

    private final MinecraftServer server;
    private final FantasyVoyageWorldProvider worlds;
    private final Map<UUID, VoyageSession> sessions = new HashMap<>();

    private VoyageSessions(MinecraftServer server) {
        this.server = server;
        this.worlds = new FantasyVoyageWorldProvider(server);
    }

    public static void register() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> current = new VoyageSessions(server));

        // Runs at the head of stopServer, before player data and levels are written, so restoring
        // here is what gets saved. Voyage levels are gone by the time vanilla walks the level map.
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            if (current != null) {
                current.endAll(Outcome.SERVER_STOPPING);
                current.worlds.closeAll();
                current = null;
            }
        });

        // All three of these read `current` defensively rather than through get(). They fire around
        // the edges of a server's life, and throwing inside a connection or respawn handler would
        // turn "no session to clean up" into a disconnect.
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            VoyageSessions sessions = current;
            if (sessions != null) {
                sessions.recoverOnJoin(handler.player);
            }
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            VoyageSessions sessions = current;
            if (sessions != null && sessions.sessions.containsKey(handler.player.getUUID())) {
                sessions.end(handler.player, Outcome.DISCONNECTED);
            }
        });

        // Dying in a trial ends the voyage as a loss. Vanilla has already decided where the dead
        // player reappears, so the restore then puts them back where they started the voyage from.
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            VoyageSessions sessions = current;
            if (sessions != null && sessions.sessions.containsKey(newPlayer.getUUID())) {
                sessions.end(newPlayer, Outcome.FAILED);
            }
        });

        VoyageControlItems.register();
    }

    /**
     * {@return the sessions of the running server}
     *
     * @throws IllegalStateException if no server is running
     */
    public static VoyageSessions get(MinecraftServer server) {
        VoyageSessions sessions = current;
        if (sessions == null) {
            throw new IllegalStateException("No voyage sessions; the server is not running");
        }
        return sessions;
    }

    /** {@return where trial levels come from} */
    public VoyageWorldProvider worlds() {
        return this.worlds;
    }

    public @Nullable VoyageSession sessionOf(ServerPlayer player) {
        return this.sessions.get(player.getUUID());
    }

    public boolean isInVoyage(ServerPlayer player) {
        return this.sessions.containsKey(player.getUUID());
    }

    /**
     * Puts a player into a voyage: stash, strip, and drop them into the first trial.
     *
     * @throws IllegalStateException if the player is already in one; callers are expected to have
     *                               checked, and starting a second voyage would strand the first stash
     */
    public VoyageSession start(ServerPlayer player, Identifier voyageId, VoyageDefinition voyage, long seed) {
        if (this.isInVoyage(player)) {
            throw new IllegalStateException(player.getPlainTextName() + " is already in a voyage");
        }

        // Taken before anything is destroyed, so a failure here leaves the player untouched.
        this.data().put(player.getUUID(), PlayerStash.of(player));
        PlayerStash.clear(player);

        VoyageSession session = new VoyageSession(player.getUUID(), voyageId, voyage, seed);
        this.sessions.put(player.getUUID(), session);

        player.sendSystemMessage(Component.empty()
                .append(voyage.displayName())
                .append(Component.literal(" — seed " + seed).withStyle(ChatFormatting.GRAY)));
        this.enterNextTrial(session, player);
        return session;
    }

    /** Acts on a control item. The only path from a player right-clicking to a voyage changing. */
    public void useControl(ServerPlayer player, VoyageControl control) {
        VoyageSession session = this.sessionOf(player);
        if (session == null) {
            // A control item that outlived its voyage. Nothing to act on, and nothing to shout about.
            VoyageControlItems.strip(player);
            return;
        }

        switch (control) {
            case ADVANCE -> this.advance(player, session);
            case FAIL -> this.end(player, Outcome.FAILED);
        }
    }

    /** Ends a voyage from the outside, as a loss. Phase 4's {@code /voyage abandon}. */
    public void abandon(ServerPlayer player) {
        if (this.isInVoyage(player)) {
            this.end(player, Outcome.ABANDONED);
        }
    }

    private void advance(ServerPlayer player, VoyageSession session) {
        if (session.isOnLastTrial()) {
            this.end(player, Outcome.COMPLETED);
            return;
        }
        this.closeCurrentTrial(session);
        this.enterNextTrial(session, player);
    }

    private void enterNextTrial(VoyageSession session, ServerPlayer player) {
        TrialInstance trial = session.advanceToNextTrial();
        TrialDefinition definition = trial.definition();

        VoyageWorldHandle handle = this.worlds.open(trial);
        session.setHandle(handle);

        ServerLevel level = handle.level();
        definition.platform().place(level, 0, 0);
        EnvironmentController.apply(level, definition.environment().unwrapKey().orElseThrow().identifier(),
                trial.nodeSeed(), EnvironmentAttributeMap.EMPTY);

        Vec3 spawn = definition.platform().spawnPos(0, 0);
        player.teleportTo(level, spawn.x(), spawn.y(), spawn.z(), Set.of(), player.getYRot(), player.getXRot(), false);
        // The environment was applied before the player was in the level, so nothing reached them
        // then; this is the send that makes the sky change.
        EnvironmentController.sendTo(player);
        VoyageControlItems.give(player);

        player.sendSystemMessage(Component.literal("Trial " + session.trialNumber() + " of " + session.trialCount() + ": ")
                .append(definition.displayName()));
    }

    private void closeCurrentTrial(VoyageSession session) {
        VoyageWorldHandle handle = session.handle();
        if (handle == null) {
            return;
        }
        session.setHandle(null);
        if (handle.isOpen()) {
            // The controller holds levels until told otherwise; deleting one first would leave an
            // entry for a level nobody can reach.
            EnvironmentController.clear(handle.level());
        }
        this.worlds.close(handle);
    }

    /**
     * Ends a voyage and puts the player back together.
     *
     * <p>The player leaves before the level is destroyed, and the restore is what moves them, so the
     * return position comes from the stash rather than from wherever they happened to be standing.
     */
    private void end(ServerPlayer player, Outcome outcome) {
        VoyageSession session = this.sessions.remove(player.getUUID());
        if (session == null) {
            return;
        }

        VoyageControlItems.strip(player);

        PlayerStash stash = keepStashFor(outcome) ? this.data().peek(player.getUUID()) : this.data().take(player.getUUID());
        if (stash != null) {
            stash.restoreTo(player);
        } else {
            // Should not happen: start() writes the stash before anything else. If it ever does, the
            // player is somewhere that is about to be deleted, so say so loudly and get them out.
            Mubble.LOGGER.error("No stash for {} when their voyage ended ({}); returning them to the overworld",
                    player.getPlainTextName(), outcome);
            ServerLevel overworld = this.server.overworld();
            BlockPos spawn = this.server.getRespawnData().pos();
            player.teleportTo(overworld, spawn.getX() + 0.5D, spawn.getY(), spawn.getZ() + 0.5D,
                    Set.of(), player.getYRot(), player.getXRot(), false);
        }

        this.closeCurrentTrial(session);

        if (outcome.message != null) {
            player.sendSystemMessage(Component.literal(outcome.message).withStyle(outcome.colour));
        }
    }

    /**
     * Whether the stash survives this ending, to be picked up by {@link #recoverOnJoin}.
     *
     * <p>Only for the two endings where the player is on their way out and we cannot watch their
     * data being written. Restoring again on the way back in costs nothing; guessing wrong about
     * whether a save happened costs an inventory.
     */
    private static boolean keepStashFor(Outcome outcome) {
        return outcome == Outcome.DISCONNECTED || outcome == Outcome.SERVER_STOPPING;
    }

    /**
     * Gives a joining player back anything a voyage still owes them.
     *
     * <p>Covers the crash — nothing ran, and their own data has them standing in a dimension that no
     * longer exists — and doubles as the second half of a clean disconnect.
     */
    private void recoverOnJoin(ServerPlayer player) {
        if (this.isInVoyage(player)) {
            return;
        }
        PlayerStash stash = this.data().take(player.getUUID());
        if (stash == null) {
            return;
        }

        Mubble.LOGGER.info("Restoring {} from an unfinished voyage", player.getPlainTextName());
        VoyageControlItems.strip(player);
        stash.restoreTo(player);
        player.sendSystemMessage(Component.literal("Your voyage ended while you were away. You are back where you started.")
                .withStyle(ChatFormatting.YELLOW));
    }

    private void endAll(Outcome outcome) {
        for (UUID playerId : List.copyOf(this.sessions.keySet())) {
            ServerPlayer player = this.server.getPlayerList().getPlayer(playerId);
            if (player != null) {
                this.end(player, outcome);
            } else {
                // Offline with a live session should not happen — disconnecting ends it — but the
                // stash is on disk either way, so their next login puts them right.
                this.sessions.remove(playerId);
            }
        }
    }

    private VoyageSessionData data() {
        return this.server.getDataStorage().computeIfAbsent(VoyageSessionData.TYPE);
    }
}
