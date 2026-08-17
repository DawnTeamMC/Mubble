package fr.hugman.mubble.test.unit;

import com.google.common.reflect.Reflection;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.references.MubbleEntityTypeKeys;
import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.references.SuperMarioEntityTypeIds;
import fr.hugman.mubble.super_mario.references.SuperMarioItemIds;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.item.SuperMarioItems;
import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import net.minecraft.SharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.entity.EntityType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks that the {@code references} classes and the classes actually registering content agree with
 * each other. A key declared but never registered would only blow up at runtime, usually far away
 * from the mistake.
 * <p>
 * Minecraft is bootstrapped through {@code fabric-loader-junit}, but no level is created.
 */
public class ContentRegistrationTest {
    @BeforeAll
    static void bootstrapMinecraft() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();

        // Registration happens in the static initialisers, exactly like the mod initialisers do it.
        Reflection.initialize(MubbleEntityTypes.class);
        Reflection.initialize(SuperMarioEntityTypes.class);
        Reflection.initialize(SuperMarioItems.class);
    }

    @Test
    @DisplayName("every declared entity type key is registered")
    void entityTypeKeysAreRegistered() {
        assertKeysAreRegistered(MubbleEntityTypeKeys.class, BuiltInRegistries.ENTITY_TYPE, Mubble.MOD_ID);
        assertKeysAreRegistered(SuperMarioEntityTypeIds.class, BuiltInRegistries.ENTITY_TYPE, SuperMario.MOD_ID);
    }

    @Test
    @DisplayName("every declared item key is registered")
    void itemKeysAreRegistered() {
        assertKeysAreRegistered(SuperMarioItemIds.class, BuiltInRegistries.ITEM, SuperMario.MOD_ID);
    }

    @Test
    @DisplayName("projectile entity types are the ones their key points at")
    void projectileEntityTypesMatchTheirKey() {
        assertRegisteredAs(SuperMarioEntityTypes.FIREBALL, SuperMarioEntityTypeIds.FIREBALL);
        assertRegisteredAs(SuperMarioEntityTypes.ICEBALL, SuperMarioEntityTypeIds.ICEBALL);
        assertRegisteredAs(SuperMarioEntityTypes.GOLD_FIREBALL, SuperMarioEntityTypeIds.GOLD_FIREBALL);
        assertRegisteredAs(SuperMarioEntityTypes.GREEN_KOOPA_SHELL, SuperMarioEntityTypeIds.GREEN_KOOPA_SHELL);
        assertRegisteredAs(SuperMarioEntityTypes.RED_KOOPA_SHELL, SuperMarioEntityTypeIds.RED_KOOPA_SHELL);
        assertRegisteredAs(MubbleEntityTypes.COLLECTIBLE, MubbleEntityTypeKeys.COLLECTIBLE);
    }

    private static void assertRegisteredAs(EntityType<?> type, ResourceKey<EntityType<?>> key) {
        assertSame(type, BuiltInRegistries.ENTITY_TYPE.getValue(key), () -> key.identifier() + " is not registered to the expected entity type");
        assertEquals(key.identifier(), EntityType.getKey(type));
    }

    /**
     * Asserts that every {@link ResourceKey} constant of {@code holder} is present in {@code registry}
     * under the {@code expectedNamespace} namespace.
     */
    private static void assertKeysAreRegistered(Class<?> holder, Registry<?> registry, String expectedNamespace) {
        var keys = declaredKeys(holder);
        assertFalse(keys.isEmpty(), () -> holder.getSimpleName() + " declares no key, the test is looking at the wrong class");

        for (ResourceKey<?> key : keys) {
            assertEquals(expectedNamespace, key.identifier().getNamespace(), () -> key + " is not in the expected namespace");
            assertEquals(registry.key(), key.registryKey(), () -> key + " does not belong to " + registry.key());
            assertTrue(registry.containsKey(key.identifier()), () -> key.identifier() + " is declared in " + holder.getSimpleName() + " but never registered");
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
}
