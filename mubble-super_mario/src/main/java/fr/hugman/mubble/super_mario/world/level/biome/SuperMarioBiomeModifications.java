package fr.hugman.mubble.super_mario.world.level.biome;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.world.attribute.SuperMarioEnvironmentAttributes;
import fr.hugman.mubble.tags.MubbleBlockTags;
import fr.hugman.mubble.world.attribute.BlockTransform;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.advancements.criterion.TagPredicate;
import net.minecraft.resources.Identifier;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Optional;

public class SuperMarioBiomeModifications {
    private static final Identifier FIREBALL_STUFF = SuperMario.id("fireball_stuff");

    public static void register() {
        BiomeModifications.create(FIREBALL_STUFF).add(ModificationPhase.ADDITIONS, BiomeSelectors.all(), (selectionContext, modificationContext) -> {
            if (!hasAttributeValue(selectionContext, SuperMarioEnvironmentAttributes.FIREBALL_MELTS)) {
                modificationContext.getAttributes().set(SuperMarioEnvironmentAttributes.FIREBALL_MELTS, List.of(
                        new BlockTransform(TagPredicate.is(MubbleBlockTags.MELTABLE_TO_WATER), Blocks.WATER.defaultBlockState(), Optional.of(SuperMarioSounds.FIREBALL_MELT_BLOCK)),
                        new BlockTransform(TagPredicate.is(MubbleBlockTags.MELTABLE_TO_ICE), Blocks.ICE.defaultBlockState(), Optional.of(SuperMarioSounds.FIREBALL_MELT_BLOCK))
                ));
            }
            if (!hasAttributeValue(selectionContext, SuperMarioEnvironmentAttributes.ICEBALL_FREEZES)) {
                modificationContext.getAttributes().set(SuperMarioEnvironmentAttributes.ICEBALL_FREEZES, List.of(
                        new BlockTransform(TagPredicate.is(MubbleBlockTags.FREEZABLE_TO_PACKED_ICE), Blocks.PACKED_ICE.defaultBlockState(), Optional.of(SuperMarioSounds.ICEBALL_HIT_BLOCK))
                ));
            }
        });
    }

    private static boolean hasAttributeValue(BiomeSelectionContext context, EnvironmentAttribute<?> key) {
        return context.getBiome().getAttributes().get(key) != null;
    }
}
