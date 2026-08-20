package fr.hugman.mubble.test.unit.support;

import net.minecraft.SharedConstants;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.Bootstrap;

/**
 * Brings Minecraft up once for the whole unit test run. {@code fabric-loader-junit} boots Knot around
 * JUnit, but the game itself still has to be bootstrapped by hand before any registry can be read.
 * <p>
 * Only the built-in registries exist here: anything living in a dynamic registry (power-ups, damage
 * types, goomba variants) needs a server, so it belongs in a game test instead.
 */
public final class TestBootstrap {
    private static RegistryAccess.Frozen registries;

    private TestBootstrap() {
    }

    public static synchronized void bootstrap() {
        if (registries != null) {
            return;
        }
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        registries = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
    }

    /** The built-in registries, for the codecs needing a lookup (sounds, attributes, blocks...). */
    public static RegistryAccess.Frozen registries() {
        bootstrap();
        return registries;
    }
}
