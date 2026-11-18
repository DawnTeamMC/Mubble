package fr.hugman.mubble.test.gametest.support;

/**
 * The game test server lets everyone fly, which leaves the kick for floating unreachable. A test needing it
 * asks for the policy of a dedicated server through here.
 *
 * @see fr.hugman.mubble.test.gametest.mixin.MinecraftServerMixin
 */
public final class FlightPolicy {
    public static volatile boolean denied;

    private FlightPolicy() {
    }

    public static void denyDuring(Runnable test) {
        denied = true;
        try {
            test.run();
        } finally {
            denied = false;
        }
    }
}
