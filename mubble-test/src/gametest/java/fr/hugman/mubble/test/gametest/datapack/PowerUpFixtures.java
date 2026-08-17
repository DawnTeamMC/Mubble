package fr.hugman.mubble.test.gametest.datapack;

import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.power_up.PowerUp;
import fr.hugman.mubble.world.power_up.action.PowerUpAction;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

/**
 * Keys of the entries the game test mod defines in its own data pack, under
 * {@code src/gametest/resources/data}. They are written by hand rather than generated, so that they
 * go through the very same loading path as a third-party data pack would.
 * <p>
 * They cover the shapes a data pack may use but no shipped power-up happens to use. Anything the
 * players actually get must be tested against the modules shipping it instead.
 */
public class PowerUpFixtures {
    /** Namespace of the game test mod itself, see {@code src/gametest/resources/fabric.mod.json}. */
    private static final String NAMESPACE = "mubble-gametest";

    /** Every optional field left out, to make sure a power-up can be that bare. */
    public static final ResourceKey<PowerUp> EMPTY = powerUp("empty");

    /** Only attribute modifiers, without a name, an action or any cosmetic. */
    public static final ResourceKey<PowerUp> ATTRIBUTES_ONLY = powerUp("attributes_only");

    /** Points at {@link #SNOWBALL_BARRAGE} by id instead of inlining its action. */
    public static final ResourceKey<PowerUp> REFERENCED_ACTION = powerUp("referenced_action");

    /** A stand-alone action, which the mod never writes: it inlines all of its own. */
    public static final ResourceKey<PowerUpAction> SNOWBALL_BARRAGE = powerUpAction("snowball_barrage");

    private static ResourceKey<PowerUp> powerUp(String path) {
        return ResourceKey.create(MubbleRegistries.POWER_UP, id(path));
    }

    private static ResourceKey<PowerUpAction> powerUpAction(String path) {
        return ResourceKey.create(MubbleRegistries.POWER_UP_ACTION, id(path));
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(NAMESPACE, path);
    }
}
