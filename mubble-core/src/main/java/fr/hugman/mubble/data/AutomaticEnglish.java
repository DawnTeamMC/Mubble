package fr.hugman.mubble.data;

import fr.hugman.mubble.core.registries.MubbleRegistries;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.DyeColor;

import java.util.List;
import java.util.Set;

public class AutomaticEnglish {
    private static final Set<String> DO_NOT_TITLE_CASE = Set.of(
            "of", "the", "and", "a", "an", "in", "on", "for", "to", "at", "by", "from", "with"
    );

    public static void generateAutomaticTranslations(String modId, HolderLookup.Provider lookup, FabricLanguageProvider.TranslationBuilder builder) {
        for (var block : getRegistryEntries(modId, lookup, Registries.BLOCK)) {
            var path = block.key().identifier().getPath()
                    .replaceAll("exclamation", "!")
                    .replaceAll("question", "?");
            try {
                builder.add(block.value(), snakeToTitleCase(path));
            } catch (RuntimeException ignored) {}
        }

        for (var item : getRegistryEntries(modId, lookup, Registries.ITEM)) {
            if (item.value().getDescriptionId().startsWith("block.")) {
                continue;
            }
            var path = item.key().identifier().getPath();
            if (path.endsWith("_chest_boat")) {
                path = path.replace("_chest_boat", "_boat_with_chest");
            }
            try {
                builder.add(item.value(), snakeToTitleCase(path));
            } catch (RuntimeException ignored) {}
        }

        for (var entity : getRegistryEntries(modId, lookup, Registries.ENTITY_TYPE)) {
            var path = entity.key().identifier().getPath();
            if (path.endsWith("_chest_boat")) {
                path = path.replace("_chest_boat", "_boat_with_chest");
            }
            builder.add(entity.value(), snakeToTitleCase(path));
        }

        for (var biome : getRegistryEntries(modId, lookup, Registries.BIOME)) {
            var id = biome.key().identifier();
            builder.add(Util.makeDescriptionId("biome", id), snakeToTitleCase(id.getPath()));
        }

        for (var bannerPattern : getRegistryEntries(modId, lookup, Registries.BANNER_PATTERN)) {
            var id = bannerPattern.key().identifier();
            builder.add(
                    Util.makeDescriptionId("item", id.withPath(s -> s + "_banner_pattern.desc")),
                    snakeToTitleCase(id.getPath())
            );
            for (DyeColor color : DyeColor.values()) {
                builder.add(
                        Util.makeDescriptionId("block", id.withPath(s -> "banner." + s + "." + color.getName())),
                        snakeToTitleCase(color.getName() + "_" + id.getPath())
                );
            }
        }

        for (var paintingVariant : getRegistryEntries(modId, lookup, Registries.PAINTING_VARIANT)) {
            var id = paintingVariant.key().identifier();
            builder.add(Util.makeDescriptionId("painting", id) + ".title", snakeToTitleCase(id.getPath()));
        }

        for (var itemGroup : getRegistryEntries(modId, lookup, Registries.CREATIVE_MODE_TAB)) {
            var id = itemGroup.key().identifier();
            try {
                builder.add(Util.makeDescriptionId("item_group", id), snakeToTitleCase(id.getPath()));
            }
            catch (RuntimeException ignored) {}
        }

        for (var powerUp : getRegistryEntries(modId, lookup, MubbleRegistries.POWER_UP)) {
            var id = powerUp.key().identifier();
            try {
                builder.add(Util.makeDescriptionId("power_up", id), snakeToTitleCase(id.getPath()));
            }
            catch (RuntimeException ignored) {}
        }
    }

    private static <O> List<Holder.Reference<O>> getRegistryEntries(String modId, HolderLookup.Provider lookup, ResourceKey<? extends Registry<O>> registryKey) {
        return lookup.lookupOrThrow(registryKey).listElements()
                .filter(entry -> entry.key().identifier().getNamespace().equals(modId))
                .toList();
    }

    private static String snakeToTitleCase(String str) {
        String[] words = str.split("_");
        StringBuilder titleCase = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (word.isEmpty()) {
                continue;
            }
            if (i != 0 && DO_NOT_TITLE_CASE.contains(word.toLowerCase())) {
                titleCase.append(word.toLowerCase());
            } else {
                titleCase.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase());
            }
            if (i < words.length - 1) {
                titleCase.append(" ");
            }
        }
        return titleCase.toString();
    }
}
