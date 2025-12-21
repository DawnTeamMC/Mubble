package fr.hugman.mubble.world.level.biome;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.sound.MubbleSounds;
import fr.hugman.mubble.tags.MubbleBlockTags;
import fr.hugman.mubble.world.attribute.BlockTransform;
import fr.hugman.mubble.world.attribute.MubbleEnvironmentAttributes;
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

public class MubbleBiomeModifications {
    private static final Identifier FIREBALL_STUFF = Mubble.id("fireball_stuff");

    public static void register() {
        BiomeModifications.create(FIREBALL_STUFF).add(ModificationPhase.ADDITIONS, BiomeSelectors.all(), (selectionContext, modificationContext) -> {
            if (!hasAttributeValue(selectionContext, MubbleEnvironmentAttributes.FIREBALL_MELTS)) {
                modificationContext.getAttributes().set(MubbleEnvironmentAttributes.FIREBALL_MELTS, List.of(
                        new BlockTransform(TagPredicate.is(MubbleBlockTags.MELTABLE_TO_WATER), Blocks.WATER.defaultBlockState(), Optional.of(MubbleSounds.FIREBALL_MELT_BLOCK)),
                        new BlockTransform(TagPredicate.is(MubbleBlockTags.MELTABLE_TO_ICE), Blocks.ICE.defaultBlockState(), Optional.of(MubbleSounds.FIREBALL_MELT_BLOCK))
                ));
            }
            if (!hasAttributeValue(selectionContext, MubbleEnvironmentAttributes.ICEBALL_FREEZES)) {
                modificationContext.getAttributes().set(MubbleEnvironmentAttributes.ICEBALL_FREEZES, List.of(
                        new BlockTransform(TagPredicate.is(MubbleBlockTags.FREEZABLE_TO_PACKED_ICE), Blocks.PACKED_ICE.defaultBlockState(), Optional.of(MubbleSounds.ICEBALL_HIT_BLOCK))
                ));
            }
        });
    }

    private static boolean hasAttributeValue(BiomeSelectionContext context, EnvironmentAttribute<?> key) {
        return context.getBiome().getAttributes().get(key) != null;
    }
}
