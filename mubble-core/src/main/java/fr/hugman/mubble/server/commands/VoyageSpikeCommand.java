package fr.hugman.mubble.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import fr.hugman.mubble.world.voyage.level.TrialInstance;
import fr.hugman.mubble.world.voyage.level.VoyageWorldHandle;
import fr.hugman.mubble.world.voyage.level.fantasy.VoyageWorlds;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;

/**
 * Manual test for the phase 0 runtime level spike.
 *
 * <p>{@code /voyagespike open [seed]} builds a level, drops a stone platform under the player and
 * teleports them onto it. {@code /voyagespike close} brings them back to where they were and
 * deletes the level. {@code /voyagespike status} reports what is open.
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

    private static final int PLATFORM_RADIUS = 4;
    private static final int PLATFORM_Y = 64;

    /** Only what phase 0 needs: where the player was, and which level they went to. */
    private record Session(VoyageWorldHandle handle, ServerLevel returnLevel, double x, double y, double z, float yRot, float xRot) {
    }

    private static final Map<UUID, Session> SESSIONS = new HashMap<>();

    private VoyageSpikeCommand() {
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
                        .executes(cc -> status(cc.getSource()))));
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
