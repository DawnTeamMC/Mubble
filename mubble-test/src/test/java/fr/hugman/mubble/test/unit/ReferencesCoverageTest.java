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
import fr.hugman.mubble.test.unit.support.Registrations;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The other direction of {@link ContentRegistrationTest}: that one checks nothing is declared without
 * being registered, this one checks nothing is registered without being declared.
 * <p>
 * Content registered straight to a registry without a key in {@code references} still works, but it
 * cannot be referred to from anywhere else without hard-coding an identifier — which is precisely
 * what the {@code references} classes exist to avoid.
 */
public class ReferencesCoverageTest {
    @BeforeAll
    static void registerContent() {
        Registrations.registerEverything();
    }

    @Test
    @DisplayName("every registered entity type is declared")
    void entityTypesAreDeclared() {
        assertEveryEntryIsDeclared(BuiltInRegistries.ENTITY_TYPE, Mubble.MOD_ID, MubbleEntityTypeKeys.class);
        assertEveryEntryIsDeclared(BuiltInRegistries.ENTITY_TYPE, SuperMario.MOD_ID, SuperMarioEntityTypeIds.class);
    }

    @Test
    @DisplayName("every registered item is declared, as an item or as a block item")
    void itemsAreDeclared() {
        var declared = declaredIdentifiers(SuperMarioItemIds.class);
        declared.addAll(ContentRegistrationTest.declaredBlockItemIds(SuperMarioBlockItemIds.class).stream()
                .map(id -> id.item().identifier())
                .collect(Collectors.toSet()));

        assertEveryEntryIsDeclared(BuiltInRegistries.ITEM, SuperMario.MOD_ID, declared, "SuperMarioItemIds or SuperMarioBlockItemIds");
    }

    @Test
    @DisplayName("every registered block is declared")
    void blocksAreDeclared() {
        var declared = ContentRegistrationTest.declaredBlockItemIds(SuperMarioBlockItemIds.class).stream()
                .map(id -> id.block().identifier())
                .collect(Collectors.toCollection(TreeSet::new));

        assertEveryEntryIsDeclared(BuiltInRegistries.BLOCK, SuperMario.MOD_ID, declared, "SuperMarioBlockItemIds");
    }

    @Test
    @DisplayName("every registered power-up action type is declared")
    void powerUpActionTypesAreDeclared() {
        assertEveryEntryIsDeclared(MubbleBuiltInRegistries.POWER_UP_ACTION_TYPE, Mubble.MOD_ID, PowerUpActionTypesKeys.class);
        assertEveryEntryIsDeclared(MubbleBuiltInRegistries.POWER_UP_ACTION_TYPE, SuperMario.MOD_ID, SuperMarioPowerUpActionTypesIds.class);
    }

    @Test
    @DisplayName("every registered consume effect type is declared")
    void consumeEffectTypesAreDeclared() {
        assertEveryEntryIsDeclared(BuiltInRegistries.CONSUME_EFFECT_TYPE, Mubble.MOD_ID, MubbleConsumeEffectTypeKeys.class);
    }

    private static void assertEveryEntryIsDeclared(Registry<?> registry, String namespace, Class<?> holder) {
        assertEveryEntryIsDeclared(registry, namespace, declaredIdentifiers(holder), holder.getSimpleName());
    }

    private static void assertEveryEntryIsDeclared(Registry<?> registry, String namespace, Set<Identifier> declared, String holderName) {
        var missing = registry.registryKeySet().stream()
                .map(ResourceKey::identifier)
                .filter(id -> id.getNamespace().equals(namespace))
                .filter(id -> !declared.contains(id))
                .collect(Collectors.toCollection(TreeSet::new));

        assertTrue(missing.isEmpty(),
                () -> missing.size() + " entries of " + registry.key().identifier() + " are registered under "
                        + namespace + " without a key in " + holderName + ": " + missing);
    }

    private static Set<Identifier> declaredIdentifiers(Class<?> holder) {
        return ContentRegistrationTest.declaredKeys(holder).stream()
                .map(ResourceKey::identifier)
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
