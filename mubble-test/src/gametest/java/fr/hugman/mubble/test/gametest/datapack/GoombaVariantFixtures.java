package fr.hugman.mubble.test.gametest.datapack;

import fr.hugman.mubble.super_mario.core.registries.SuperMarioRegistries;
import fr.hugman.mubble.super_mario.world.entity.monster.goomba.GoombaVariant;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

/**
 * Goomba variants the game test mod defines in its own data pack, under
 * {@code src/gametest/resources/data}, the same way {@link PowerUpFixtures} does for power-ups.
 * <p>
 * The tests aim at these rather than at the shipped variants: {@code super_mario:normal} is the only one
 * the mod itself writes, and a test about carrying a variant through a save file should not depend on
 * whether a second one happens to exist that week.
 */
public class GoombaVariantFixtures {
    /** Namespace of the game test mod itself, see {@code src/gametest/resources/fabric.mod.json}. */
    private static final String NAMESPACE = "mubble-gametest";

    /** Named, and small enough that picking it visibly changes the goomba it is put on. */
    public static final ResourceKey<GoombaVariant> TINY = variant("tiny");

    /** The goomba variant registry of the level the test runs in. */
    public static HolderGetter<GoombaVariant> registry(GameTestHelper helper) {
        return helper.getLevel().registryAccess().lookupOrThrow(SuperMarioRegistries.GOOMBA_VARIANT);
    }

    /** Resolves one of the fixtures above, failing loudly if the data pack did not load. */
    public static Holder<GoombaVariant> get(GameTestHelper helper, ResourceKey<GoombaVariant> key) {
        return registry(helper).get(key)
                .orElseThrow(() -> new AssertionError(key.identifier() + " is missing, the game test data pack did not load"));
    }

    private static ResourceKey<GoombaVariant> variant(String path) {
        return ResourceKey.create(SuperMarioRegistries.GOOMBA_VARIANT, Identifier.fromNamespaceAndPath(NAMESPACE, path));
    }
}
