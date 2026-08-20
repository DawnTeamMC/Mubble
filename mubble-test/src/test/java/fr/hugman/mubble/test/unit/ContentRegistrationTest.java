package fr.hugman.mubble.test.unit;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.core.registries.MubbleBuiltInRegistries;
import fr.hugman.mubble.references.MubbleConsumeEffectTypeKeys;
import fr.hugman.mubble.references.MubbleEntityTypeKeys;
import fr.hugman.mubble.references.PowerUpActionTypesKeys;
import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.references.SuperMarioBlockItemIds;
import fr.hugman.mubble.super_mario.references.SuperMarioEntityTypeIds;
import fr.hugman.mubble.super_mario.references.SuperMarioItemIds;
import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpActionTypesIds;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.item.SuperMarioItems;
import fr.hugman.mubble.super_mario.world.level.block.BumpableBlock;
import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import fr.hugman.mubble.super_mario.world.level.block.entity.SuperMarioBlockEntityTypes;
import fr.hugman.mubble.test.unit.support.Registrations;
import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
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
 * Minecraft is bootstrapped through {@code fabric-loader-junit}, but no level is created, so only the
 * built-in registries are visible here. Their dynamic counterparts — power-ups, damage types, goomba
 * variants — belong to {@code DynamicContentGameTest} instead.
 */
public class ContentRegistrationTest {
    @BeforeAll
    static void registerContent() {
        Registrations.registerEverything();
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
    @DisplayName("every declared block key is registered, along with its block item")
    void blockKeysAreRegistered() {
        var ids = declaredBlockItemIds(SuperMarioBlockItemIds.class);
        assertFalse(ids.isEmpty(), "SuperMarioBlockItemIds declares no key, the test is looking at the wrong class");

        for (BlockItemId id : ids) {
            assertEquals(SuperMario.MOD_ID, id.block().identifier().getNamespace(), () -> id + " is not in the expected namespace");
            assertTrue(BuiltInRegistries.BLOCK.containsKey(id.block()), () -> id.block().identifier() + " is declared but never registered");
        }
    }

    @Test
    @DisplayName("every declared power-up action type key is registered")
    void powerUpActionTypeKeysAreRegistered() {
        assertKeysAreRegistered(PowerUpActionTypesKeys.class, MubbleBuiltInRegistries.POWER_UP_ACTION_TYPE, Mubble.MOD_ID);
        assertKeysAreRegistered(SuperMarioPowerUpActionTypesIds.class, MubbleBuiltInRegistries.POWER_UP_ACTION_TYPE, SuperMario.MOD_ID);
    }

    @Test
    @DisplayName("every declared consume effect type key is registered")
    void consumeEffectTypeKeysAreRegistered() {
        assertKeysAreRegistered(MubbleConsumeEffectTypeKeys.class, BuiltInRegistries.CONSUME_EFFECT_TYPE, Mubble.MOD_ID);
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

    @Test
    @DisplayName("blocks are the ones their key points at")
    void blocksMatchTheirKey() {
        assertBlockRegisteredAs(SuperMarioBlocks.QUESTION_BLOCK, SuperMarioBlockItemIds.QUESTION_BLOCK);
        assertBlockRegisteredAs(SuperMarioBlocks.EMPTY_BLOCK, SuperMarioBlockItemIds.EMPTY_BLOCK);
        assertBlockRegisteredAs(SuperMarioBlocks.BRICK_BLOCK, SuperMarioBlockItemIds.BRICK_BLOCK);
        assertBlockRegisteredAs(SuperMarioBlocks.NOTE_BLOCK, SuperMarioBlockItemIds.NOTE_BLOCK);
    }

    @Test
    @DisplayName("the bumpable block entity accepts every bumpable block")
    void bumpableBlockEntityCoversItsBlocks() {
        var type = SuperMarioBlockEntityTypes.BUMPABLE_BLOCK;

        for (Block block : BuiltInRegistries.BLOCK) {
            if (!(block instanceof BumpableBlock)) {
                continue;
            }
            var id = BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow().identifier();
            if (!id.getNamespace().equals(SuperMario.MOD_ID)) {
                continue;
            }
            assertTrue(type.isValid(block.defaultBlockState()),
                    () -> id + " is a bumpable block, but the bumpable block entity does not accept it");
        }
    }

    @Test
    @DisplayName("registered items are the ones their key points at")
    void itemsMatchTheirKey() {
        assertSame(SuperMarioItems.FIRE_FLOWER, BuiltInRegistries.ITEM.getValue(SuperMarioItemIds.FIRE_FLOWER), "the fire flower");
        assertSame(SuperMarioItems.MINI_MUSHROOM, BuiltInRegistries.ITEM.getValue(SuperMarioItemIds.MINI_MUSHROOM), "the mini mushroom");
        assertSame(SuperMarioItems.GOOMBA_SPAWN_EGG, BuiltInRegistries.ITEM.getValue(SuperMarioItemIds.GOOMBA_SPAWN_EGG), "the goomba spawn egg");
    }

    private static void assertRegisteredAs(EntityType<?> type, ResourceKey<EntityType<?>> key) {
        assertSame(type, BuiltInRegistries.ENTITY_TYPE.getValue(key), () -> key.identifier() + " is not registered to the expected entity type");
        assertEquals(key.identifier(), EntityType.getKey(type));
    }

    private static void assertBlockRegisteredAs(Block block, BlockItemId id) {
        assertSame(block, BuiltInRegistries.BLOCK.getValue(id.block()), () -> id.block().identifier() + " is not registered to the expected block");
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

    static List<ResourceKey<?>> declaredKeys(Class<?> holder) {
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

    static List<BlockItemId> declaredBlockItemIds(Class<?> holder) {
        var ids = new ArrayList<BlockItemId>();
        for (Field field : holder.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) || !BlockItemId.class.isAssignableFrom(field.getType())) {
                continue;
            }
            try {
                ids.add((BlockItemId) field.get(null));
            } catch (IllegalAccessException e) {
                throw new AssertionError("Could not read " + holder.getSimpleName() + "." + field.getName(), e);
            }
        }
        return ids;
    }
}
