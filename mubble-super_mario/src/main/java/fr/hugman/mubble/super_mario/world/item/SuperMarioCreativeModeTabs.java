package fr.hugman.mubble.super_mario.world.item;

import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.core.component.SuperMarioDataComponents;
import fr.hugman.mubble.super_mario.core.registries.SuperMarioRegistries;
import fr.hugman.mubble.super_mario.references.GoombaVariantKeys;
import fr.hugman.mubble.super_mario.references.SuperMarioCreativeModeTabKeys;
import fr.hugman.mubble.super_mario.world.entity.monster.goomba.GoombaVariant;
import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
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

public class SuperMarioCreativeModeTabs {
    public static final CreativeModeTab SUPER_MARIO = register(SuperMarioCreativeModeTabKeys.SUPER_MARIO, FabricCreativeModeTab.builder()
            .title(Component.translatable("item_group."+ SuperMario.MOD_ID +".super_mario"))
            .icon(() -> new ItemStack(SuperMarioBlocks.QUESTION_BLOCK))
            .build());
    public static final CreativeModeTab YOSHI_ISLAND = register(SuperMarioCreativeModeTabKeys.YOSHI_ISLAND, FabricCreativeModeTab.builder()
            .title(Component.translatable("item_group." + SuperMario.MOD_ID +".yoshi_island"))
            .icon(() -> new ItemStack(SuperMarioBlocks.GREEN_EGG_BLOCK))
            .build());

    private static CreativeModeTab register(ResourceKey<CreativeModeTab> key, CreativeModeTab itemGroup) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, itemGroup);
    }

    public static void appendItemGroups() {
        CreativeModeTabEvents.modifyOutputEvent(SuperMarioCreativeModeTabKeys.SUPER_MARIO).register(entries -> {
            var context = entries.getContext();

			entries.accept(SuperMarioItems.COIN);
			entries.accept(SuperMarioItems.RED_COIN);
			entries.accept(SuperMarioItems.BLUE_COIN);
			entries.accept(SuperMarioItems.FLOWER_COIN);
            entries.accept(SuperMarioItems.FIRE_FLOWER);
            entries.accept(SuperMarioItems.ICE_FLOWER);
            entries.accept(SuperMarioItems.MINI_MUSHROOM);
            entries.accept(SuperMarioItems.MEGA_MUSHROOM);
            entries.accept(SuperMarioBlocks.QUESTION_BLOCK);
            entries.accept(SuperMarioBlocks.EMPTY_BLOCK);
            entries.accept(SuperMarioBlocks.BRICK_BLOCK);
            entries.accept(SuperMarioBlocks.CRYSTAL_BLOCK);
            entries.accept(SuperMarioBlocks.GOLD_BLOCK);
            entries.accept(SuperMarioBlocks.BLUE_EXCLAMATION_BLOCK);
            entries.accept(SuperMarioBlocks.GREEN_EXCLAMATION_BLOCK);
            entries.accept(SuperMarioBlocks.YELLOW_EXCLAMATION_BLOCK);
            entries.accept(SuperMarioBlocks.RED_EXCLAMATION_BLOCK);
            entries.accept(SuperMarioBlocks.NOTE_BLOCK);
            entries.accept(SuperMarioBlocks.BLUE_MARIMBA_BLOCK);
            entries.accept(SuperMarioBlocks.GREEN_MARIMBA_BLOCK);
            entries.accept(SuperMarioBlocks.YELLOW_MARIMBA_BLOCK);
            entries.accept(SuperMarioBlocks.RED_MARIMBA_BLOCK);
            entries.accept(SuperMarioBlocks.SNAKE_BLOCK);
            entries.accept(SuperMarioBlocks.FAST_SNAKE_BLOCK);
            entries.accept(SuperMarioBlocks.SLOW_SNAKE_BLOCK);
            entries.accept(SuperMarioBlocks.RED_BEEP_BLOCK);
            entries.accept(SuperMarioBlocks.BLUE_BEEP_BLOCK);
            entries.accept(SuperMarioItems.GREEN_KOOPA_SHELL);
            entries.accept(SuperMarioItems.RED_KOOPA_SHELL);
            entries.accept(SuperMarioItems.MAKER_GLOVE);
            entries.accept(SuperMarioItems.GOOMBA_SPAWN_EGG);
            context.holders()
                    .lookup(SuperMarioRegistries.GOOMBA_VARIANT)
                    .ifPresent(registryWrapper -> addGoombaVariantsSpawnEggs(
                            entries,
							registryWrapper,
                            registryEntry -> true
                    ));
        });

        appendSpawnEgg(SuperMarioItems.GOOMBA_SPAWN_EGG);

        CreativeModeTabEvents.modifyOutputEvent(SuperMarioCreativeModeTabKeys.YOSHI_ISLAND).register(entries -> {
            entries.accept(SuperMarioBlocks.BLUE_EGG_BLOCK);
            entries.accept(SuperMarioBlocks.CYAN_EGG_BLOCK);
            entries.accept(SuperMarioBlocks.GREEN_EGG_BLOCK);
            entries.accept(SuperMarioBlocks.YELLOW_EGG_BLOCK);
            entries.accept(SuperMarioBlocks.ORANGE_EGG_BLOCK);
            entries.accept(SuperMarioBlocks.RED_EGG_BLOCK);
            entries.accept(SuperMarioBlocks.PINK_EGG_BLOCK);
            entries.accept(SuperMarioBlocks.BLACK_EGG_BLOCK);
            entries.accept(SuperMarioBlocks.WHITE_EGG_BLOCK);
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
                            var stack = new ItemStack(SuperMarioItems.GOOMBA_SPAWN_EGG);
							var spawnEgg = entry.value().spawnEggInfo().get();
							stack.set(DataComponents.ITEM_NAME, spawnEgg.name());
                            if (stack.isEmpty()) {
                                return;
                            }
							stack.set(SuperMarioDataComponents.GOOMBA_VARIANT, entry);
                            entries.accept(stack, spawnEgg.onlyInSearch() ? CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY : CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                        }
                );
    }
}
