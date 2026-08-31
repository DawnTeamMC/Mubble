package fr.hugman.mubble.test.gametest.datapack;

import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.power_up.PowerUp;
import fr.hugman.mubble.world.power_up.action.PowerUpAction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

/**
 * Keys of the entries the game test mod defines in its own data pack, under
 * {@code src/gametest/resources/data}. They are written by hand rather than generated, so that they
 * go through the very same loading path as a third-party data pack would.
 * <p>
 * The tests aim at these rather than at the shipped power-ups on purpose: a test about losing a
 * power-up in water should not start failing because someone rebalanced the cloud flower.
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

    /** Tagged {@code mubble:lost_to_water}. */
    public static final ResourceKey<PowerUp> LOST_TO_WATER = powerUp("lost_to_water");

    /** Tagged {@code mubble:lost_to_rain}. */
    public static final ResourceKey<PowerUp> LOST_TO_RAIN = powerUp("lost_to_rain");

    /** Tagged {@code mubble:can_run_on_water}. */
    public static final ResourceKey<PowerUp> RUNS_ON_WATER = powerUp("runs_on_water");

    /** Tagged {@code super_mario:disables_stomping}. */
    public static final ResourceKey<PowerUp> STOMP_PROOF = powerUp("stomp_proof");

    /** Shoots two snowballs, so that running out of charges takes two triggers and not a dozen. */
    public static final ResourceKey<PowerUp> SHOOTER = powerUp("shooter");

    /** Grants nothing but a flutter, on numbers of its own rather than on the defaults. */
    public static final ResourceKey<PowerUp> FLUTTERER = powerUp("flutterer");

    /** The power-up registry of the level the test runs in. */
    public static HolderGetter<PowerUp> registry(GameTestHelper helper) {
        return helper.getLevel().registryAccess().lookupOrThrow(MubbleRegistries.POWER_UP);
    }

    /** Resolves one of the fixtures above, failing loudly if the data pack did not load. */
    public static Holder<PowerUp> get(GameTestHelper helper, ResourceKey<PowerUp> key) {
        return registry(helper).get(key)
                .orElseThrow(() -> new AssertionError(key.identifier() + " is missing, the game test data pack did not load"));
    }

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
