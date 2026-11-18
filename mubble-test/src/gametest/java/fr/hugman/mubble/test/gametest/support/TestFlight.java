package fr.hugman.mubble.test.gametest.support;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.world.entity.player.Player;

/**
 * Players the test server treats as a dedicated server with {@code allow-flight=false} does: staying
 * in the air too long gets them kicked. Scoped to players so that tests running side by side do not
 * see each other's setting.
 */
public final class TestFlight {
    private static final Set<UUID> DISALLOWED = ConcurrentHashMap.newKeySet();

    private TestFlight() {
    }

    public static void disallow(Player player) {
        DISALLOWED.add(player.getUUID());
    }

    public static void reset(Player player) {
        DISALLOWED.remove(player.getUUID());
    }

    public static boolean isDisallowed(Player player) {
        return DISALLOWED.contains(player.getUUID());
    }
}
