package fr.hugman.mubble.test.gametest.datapack;

import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.super_mario.core.registries.SuperMarioRegistries;
import fr.hugman.mubble.super_mario.references.GoombaVariantIds;
import fr.hugman.mubble.super_mario.references.SuperMarioDamageTypeIds;
import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpIds;
import fr.hugman.mubble.super_mario.tags.SuperMarioBlockTags;
import fr.hugman.mubble.super_mario.tags.SuperMarioDamageTypeTags;
import fr.hugman.mubble.super_mario.tags.SuperMarioEntityTypeTags;
import fr.hugman.mubble.super_mario.tags.SuperMarioItemTags;
import fr.hugman.mubble.super_mario.tags.SuperMarioPowerUpTags;
import fr.hugman.mubble.tags.MubbleBlockTags;
import fr.hugman.mubble.tags.MubblePowerUpTags;
import fr.hugman.mubble.world.power_up.PowerUp;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * The counterpart of {@code ContentRegistrationTest} for everything a data pack fills in: those
 * registries are empty until a world is loaded, so a unit test cannot see them.
 * <p>
 * This also covers the tags, which are the mod's own extension points: a tag declared in code but
 * never written as a file loads as an empty tag, and every feature keyed on it silently does nothing.
 */
public class DynamicContentGameTest {
    @GameTest
    public void everyDeclaredPowerUpIsLoaded(GameTestHelper helper) {
        assertKeysResolve(helper, SuperMarioPowerUpIds.class, helper.getLevel().registryAccess().lookupOrThrow(MubbleRegistries.POWER_UP));
        helper.succeed();
    }

    @GameTest
    public void everyDeclaredDamageTypeIsLoaded(GameTestHelper helper) {
        assertKeysResolve(helper, SuperMarioDamageTypeIds.class, helper.getLevel().registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE));
        helper.succeed();
    }

    @GameTest
    public void everyDeclaredGoombaVariantIsLoaded(GameTestHelper helper) {
        assertKeysResolve(helper, GoombaVariantIds.class, helper.getLevel().registryAccess().lookupOrThrow(SuperMarioRegistries.GOOMBA_VARIANT));
        helper.succeed();
    }

    @GameTest
    public void everyDeclaredPowerUpTagExists(GameTestHelper helper) {
        var powerUps = helper.getLevel().registryAccess().lookupOrThrow(MubbleRegistries.POWER_UP);

        for (TagKey<?> tag : declaredTags(MubblePowerUpTags.class)) {
            assertTagIsNotEmpty(helper, powerUps, tag);
        }
        for (TagKey<?> tag : declaredTags(SuperMarioPowerUpTags.class)) {
            assertTagIsNotEmpty(helper, powerUps, tag);
        }

        helper.succeed();
    }

    @GameTest
    public void everyDeclaredBlockTagExists(GameTestHelper helper) {
        for (TagKey<?> tag : declaredTags(MubbleBlockTags.class)) {
            assertTagIsNotEmpty(helper, BuiltInRegistries.BLOCK, tag);
        }
        for (TagKey<?> tag : declaredTags(SuperMarioBlockTags.class)) {
            assertTagIsNotEmpty(helper, BuiltInRegistries.BLOCK, tag);
        }

        helper.succeed();
    }

    @GameTest
    public void everyDeclaredEntityAndItemTagExists(GameTestHelper helper) {
        for (TagKey<?> tag : declaredTags(SuperMarioEntityTypeTags.class)) {
            assertTagIsNotEmpty(helper, BuiltInRegistries.ENTITY_TYPE, tag);
        }
        for (TagKey<?> tag : declaredTags(SuperMarioItemTags.class)) {
            assertTagIsNotEmpty(helper, BuiltInRegistries.ITEM, tag);
        }

        helper.succeed();
    }

    @GameTest
    public void everyDeclaredDamageTypeTagExists(GameTestHelper helper) {
        var damageTypes = helper.getLevel().registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE);

        for (TagKey<?> tag : declaredTags(SuperMarioDamageTypeTags.class)) {
            assertTagIsNotEmpty(helper, damageTypes, tag);
        }

        helper.succeed();
    }

    /** Every power-up the mod ships must be reachable through its sprite id, or the HUD shows nothing. */
    @GameTest
    public void everyShippedPowerUpHasASprite(GameTestHelper helper) {
        var powerUps = helper.getLevel().registryAccess().lookupOrThrow(MubbleRegistries.POWER_UP);

        for (ResourceKey<?> key : declaredKeys(SuperMarioPowerUpIds.class)) {
            @SuppressWarnings("unchecked")
            var powerUpKey = (ResourceKey<PowerUp>) key;
            var holder = powerUps.get(powerUpKey).orElseThrow();

            helper.assertTrue(PowerUp.getSpriteId(holder).isPresent(), key.identifier() + " resolves to no HUD sprite at all");
        }

        helper.succeed();
    }

    /**
     * Tags that exist so that data packs can put something in them, and that ship empty on purpose. They still
     * have to be written out, but the emptiness check does not apply to them.
     */
    private static final java.util.Set<TagKey<?>> DATA_PACK_HOOKS = java.util.Set.of(
            SuperMarioEntityTypeTags.BUBBLE_CAN_TRAP
    );

    private static void assertTagIsNotEmpty(GameTestHelper helper, net.minecraft.core.HolderLookup<?> registry, TagKey<?> tag) {
        @SuppressWarnings({"unchecked", "rawtypes"})
        var entries = ((net.minecraft.core.HolderLookup) registry).get((TagKey) tag);

        helper.assertTrue(entries.isPresent(), tag.location() + " is declared in code but no data pack defines it");
        if (DATA_PACK_HOOKS.contains(tag)) {
            return;
        }
        helper.assertFalse(((net.minecraft.core.HolderSet.Named<?>) entries.get()).size() == 0,
                tag.location() + " is defined but empty, everything keyed on it does nothing");
    }

    private static void assertKeysResolve(GameTestHelper helper, Class<?> holder, Registry<?> registry) {
        var keys = declaredKeys(holder);
        helper.assertFalse(keys.isEmpty(), holder.getSimpleName() + " declares no key, the test is looking at the wrong class");

        for (ResourceKey<?> key : keys) {
            helper.assertTrue(registry.containsKey(key.identifier()),
                    key.identifier() + " is declared in " + holder.getSimpleName() + " but no data pack defines it");
        }
    }

    private static void assertKeysResolve(GameTestHelper helper, Class<?> holder, net.minecraft.core.HolderLookup.RegistryLookup<?> registry) {
        var keys = declaredKeys(holder);
        helper.assertFalse(keys.isEmpty(), holder.getSimpleName() + " declares no key, the test is looking at the wrong class");

        for (ResourceKey<?> key : keys) {
            @SuppressWarnings({"unchecked", "rawtypes"})
            var found = ((net.minecraft.core.HolderGetter) registry).get((ResourceKey) key);
            helper.assertTrue(found.isPresent(),
                    key.identifier() + " is declared in " + holder.getSimpleName() + " but no data pack defines it");
        }
    }

    private static List<ResourceKey<?>> declaredKeys(Class<?> holder) {
        var keys = new ArrayList<ResourceKey<?>>();
        for (Field field : holder.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) || !ResourceKey.class.isAssignableFrom(field.getType())) {
                continue;
            }
            try {
                keys.add((ResourceKey<?>) field.get(null));
            } catch (IllegalAccessException e) {
                throw new AssertionError("Could not read " + holder.getSimpleName() + "." + field.getName(), e);
            }
        }
        return keys;
    }

    private static List<TagKey<?>> declaredTags(Class<?> holder) {
        var tags = new ArrayList<TagKey<?>>();
        for (Field field : holder.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) || !TagKey.class.isAssignableFrom(field.getType())) {
                continue;
            }
            try {
                tags.add((TagKey<?>) field.get(null));
            } catch (IllegalAccessException e) {
                throw new AssertionError("Could not read " + holder.getSimpleName() + "." + field.getName(), e);
            }
        }
        return tags;
    }
}
