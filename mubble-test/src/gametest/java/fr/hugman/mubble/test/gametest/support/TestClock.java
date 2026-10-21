package fr.hugman.mubble.test.gametest.support;

import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.clock.WorldClocks;

/**
 * Winding the world clock, for the tests that care what time of day it is.
 * <p>
 * Game tests start at night, which is the right time for anything that sleeps and the wrong one for
 * anything that has to be awake: a loftwing left to itself at night goes and roosts.
 */
public final class TestClock {
    /** Noon, as the overworld counts it. */
    private static final long MIDDAY = 6000L;

    private TestClock() {
    }

    public static void midday(GameTestHelper helper) {
        var level = helper.getLevel();
        var clock = level.registryAccess().lookupOrThrow(Registries.WORLD_CLOCK).getOrThrow(WorldClocks.OVERWORLD);
        level.clockManager().setTotalTicks(clock, MIDDAY);
    }
}
