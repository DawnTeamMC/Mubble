package fr.hugman.mubble.world.item;

import fr.hugman.mubble.core.component.MubbleDataComponents;
import fr.hugman.mubble.references.MubbleCreativeModeTabKeys;
import fr.hugman.mubble.world.entity.monster.goomba.GoombaVariant;
import fr.hugman.mubble.references.GoombaVariantKeys;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.level.block.MubbleBlocks;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import java.util.Collections;
import java.util.function.Predicate;

public class MubbleCreativeModeTabs {
    public static final CreativeModeTab SUPER_MARIO = register(MubbleCreativeModeTabKeys.SUPER_MARIO, FabricCreativeModeTab.builder()
            .title(Component.translatable("item_group.mubble.super_mario"))
            .icon(() -> new ItemStack(MubbleBlocks.QUESTION_BLOCK))
            .build());
    public static final CreativeModeTab YOSHI_ISLAND = register(MubbleCreativeModeTabKeys.YOSHI_ISLAND, FabricCreativeModeTab.builder()
            .title(Component.translatable("item_group.mubble.yoshi_island"))
            .icon(() -> new ItemStack(MubbleBlocks.GREEN_EGG_BLOCK))
            .build());

    private static CreativeModeTab register(ResourceKey<CreativeModeTab> key, CreativeModeTab itemGroup) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, itemGroup);
    }

    public static void appendItemGroups() {
        CreativeModeTabEvents.modifyOutputEvent(MubbleCreativeModeTabKeys.SUPER_MARIO).register(entries -> {
            var context = entries.getContext();

			entries.accept(MubbleItems.COIN);
			entries.accept(MubbleItems.RED_COIN);
			entries.accept(MubbleItems.BLUE_COIN);
			entries.accept(MubbleItems.FLOWER_COIN);
            entries.accept(MubbleItems.FIRE_FLOWER);
            entries.accept(MubbleItems.ICE_FLOWER);
            entries.accept(MubbleItems.MINI_MUSHROOM);
			entries.accept(MubbleItems.MEGA_MUSHROOM);
			entries.accept(MubbleItems.GOLD_FLOWER);
			entries.accept(MubbleBlocks.QUESTION_BLOCK);
            entries.accept(MubbleBlocks.EMPTY_BLOCK);
            entries.accept(MubbleBlocks.BRICK_BLOCK);
            entries.accept(MubbleBlocks.CRYSTAL_BLOCK);
            entries.accept(MubbleBlocks.GOLD_BLOCK);
            entries.accept(MubbleBlocks.BLUE_EXCLAMATION_BLOCK);
            entries.accept(MubbleBlocks.GREEN_EXCLAMATION_BLOCK);
            entries.accept(MubbleBlocks.YELLOW_EXCLAMATION_BLOCK);
            entries.accept(MubbleBlocks.RED_EXCLAMATION_BLOCK);
            entries.accept(MubbleBlocks.NOTE_BLOCK);
            entries.accept(MubbleBlocks.BLUE_MARIMBA_BLOCK);
            entries.accept(MubbleBlocks.GREEN_MARIMBA_BLOCK);
            entries.accept(MubbleBlocks.YELLOW_MARIMBA_BLOCK);
            entries.accept(MubbleBlocks.RED_MARIMBA_BLOCK);
            entries.accept(MubbleBlocks.SNAKE_BLOCK);
            entries.accept(MubbleBlocks.FAST_SNAKE_BLOCK);
            entries.accept(MubbleBlocks.SLOW_SNAKE_BLOCK);
            entries.accept(MubbleBlocks.RED_BEEP_BLOCK);
            entries.accept(MubbleBlocks.BLUE_BEEP_BLOCK);
            entries.accept(MubbleItems.GREEN_KOOPA_SHELL);
            entries.accept(MubbleItems.RED_KOOPA_SHELL);
            entries.accept(MubbleItems.MAKER_GLOVE);
            entries.accept(MubbleItems.GOOMBA_SPAWN_EGG);
            context.holders()
                    .lookup(MubbleRegistries.GOOMBA_VARIANT)
                    .ifPresent(registryWrapper -> addGoombaVariantsSpawnEggs(
                            entries,
							registryWrapper,
                            registryEntry -> true
                    ));
        });

        appendSpawnEgg(MubbleItems.GOOMBA_SPAWN_EGG);

        CreativeModeTabEvents.modifyOutputEvent(MubbleCreativeModeTabKeys.YOSHI_ISLAND).register(entries -> {
            entries.accept(MubbleBlocks.BLUE_EGG_BLOCK);
            entries.accept(MubbleBlocks.CYAN_EGG_BLOCK);
            entries.accept(MubbleBlocks.GREEN_EGG_BLOCK);
            entries.accept(MubbleBlocks.YELLOW_EGG_BLOCK);
            entries.accept(MubbleBlocks.ORANGE_EGG_BLOCK);
            entries.accept(MubbleBlocks.RED_EGG_BLOCK);
            entries.accept(MubbleBlocks.PINK_EGG_BLOCK);
            entries.accept(MubbleBlocks.BLACK_EGG_BLOCK);
            entries.accept(MubbleBlocks.WHITE_EGG_BLOCK);
        });
    }

    public static void append(ResourceKey<CreativeModeTab> group, CreativeModeTabEvents.ModifyOutput modifier) {
        CreativeModeTabEvents.modifyOutputEvent(group).register(modifier);
    }

    public static void appendSpawnEgg(Item spawnEgg) {
        var itemGroup = BuiltInRegistries.CREATIVE_MODE_TAB.getValue(CreativeModeTabs.SPAWN_EGGS);
        String path = BuiltInRegistries.ITEM.getKey(spawnEgg).getPath();

        if (itemGroup == null) {
            return;
        }

        Predicate<ItemStack> predicate = stack1 -> {
            String path1 = BuiltInRegistries.ITEM.getKey(stack1.getItem()).getPath();
            for (ItemStack stack2 : itemGroup.getDisplayItems()) {
                String path2 = BuiltInRegistries.ITEM.getKey(stack2.getItem()).getPath();
                if (path1.matches(".*_spawn_egg") && path2.matches(".*_spawn_egg")) {
                    // check if path is lexicographically between path1 and path2
                    if (path.compareTo(path1) > 0 && path.compareTo(path2) < 0) {
                        return true;
                    }
                }
            }
            return false;
        };
        append(CreativeModeTabs.SPAWN_EGGS, e -> e.insertAfter(predicate, Collections.singleton(new ItemStack(spawnEgg)), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
    }

    private static void addGoombaVariantsSpawnEggs(
            CreativeModeTab.Output entries,
			HolderLookup.RegistryLookup<GoombaVariant> registryWrapper,
            Predicate<Holder<GoombaVariant>> filter
    ) {
		registryWrapper.listElements()
                .filter(filter)
                .forEach(entry -> {
                            if (GoombaVariantKeys.NORMAL.identifier().equals(entry.key().identifier()) || entry.value().spawnEggInfo().isEmpty()) {
                                return;
                            }
                            var stack = new ItemStack(MubbleItems.GOOMBA_SPAWN_EGG);
							var spawnEgg = entry.value().spawnEggInfo().get();
							stack.set(DataComponents.ITEM_NAME, spawnEgg.name());
                            if (stack.isEmpty()) {
                                return;
                            }
							stack.set(MubbleDataComponents.GOOMBA_VARIANT, entry);
                            entries.accept(stack, spawnEgg.onlyInSearch() ? CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY : CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                        }
                );
    }
}
