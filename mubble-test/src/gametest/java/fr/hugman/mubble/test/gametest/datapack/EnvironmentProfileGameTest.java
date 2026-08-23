package fr.hugman.mubble.test.gametest.datapack;

import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.level.EnvironmentOverridable;
import fr.hugman.mubble.world.voyage.environment.EnvironmentProfile;
import java.util.List;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;

/**
 * Loading and application of environment profiles written by hand in the data pack of this module.
 *
 * <p>Profiles reach the game through the dynamic registry, which only exists on a running server, so
 * the codec unit tests cannot cover any of this.
 */
public class EnvironmentProfileGameTest {
    private static final Identifier ORANGE_SKY = Identifier.fromNamespaceAndPath("mubble-gametest", "orange_sky");
    private static final Identifier BARE = Identifier.fromNamespaceAndPath("mubble-gametest", "bare");

    @GameTest
    public void dataPackEnvironmentProfilesAreLoaded(GameTestHelper helper) {
        Registry<EnvironmentProfile> registry = profiles(helper);

        helper.assertTrue(!registry.keySet().isEmpty(), "the environment profile registry is empty");
        for (Identifier id : List.of(ORANGE_SKY, BARE)) {
            helper.assertTrue(registry.getValue(id) != null, id + " was not loaded from the data pack");
        }

        helper.succeed();
    }

    @GameTest
    public void aProfileCanLeaveEveryFieldOut(GameTestHelper helper) {
        EnvironmentProfile bare = profiles(helper).getValue(BARE);

        helper.assertTrue(bare != null, "the bare profile was not loaded");
        helper.assertValueEqual(bare, EnvironmentProfile.EMPTY, "a profile with an empty body");

        helper.succeed();
    }

    /** The override stack is what makes a trial look different, and it only exists on a live level. */
    @GameTest
    public void applyingAProfileChangesTheResolvedAttributes(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        EnvironmentProfile profile = profiles(helper).getValue(ORANGE_SKY);
        helper.assertTrue(profile != null, "the orange sky profile was not loaded");

        int skyBefore = level.environmentAttributes().getDimensionValue(EnvironmentAttributes.SKY_COLOR);
        int fogBefore = level.environmentAttributes().getDimensionValue(EnvironmentAttributes.FOG_COLOR);
        var overridable = (EnvironmentOverridable) level;

        try {
            overridable.setEnvironmentOverrides(List.of(profile.attributes().fixed(), EnvironmentAttributeMap.EMPTY));

            helper.assertValueEqual(
                    level.environmentAttributes().getDimensionValue(EnvironmentAttributes.SKY_COLOR),
                    0xFFFFA120, "the sky colour while the profile is applied");

            // Fall-through is per field: the profile never names fog, so fog must be untouched.
            helper.assertValueEqual(
                    level.environmentAttributes().getDimensionValue(EnvironmentAttributes.FOG_COLOR),
                    fogBefore, "the fog colour, which this profile does not name");
        } finally {
            overridable.setEnvironmentOverrides(List.of());
        }

        helper.assertValueEqual(
                level.environmentAttributes().getDimensionValue(EnvironmentAttributes.SKY_COLOR),
                skyBefore, "the sky colour after clearing the override");

        helper.succeed();
    }

    private static Registry<EnvironmentProfile> profiles(GameTestHelper helper) {
        return helper.getLevel().registryAccess().lookupOrThrow(MubbleRegistries.ENVIRONMENT_PROFILE);
    }
}
