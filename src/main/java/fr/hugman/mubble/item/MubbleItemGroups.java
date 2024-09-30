package fr.hugman.mubble.item;

import fr.hugman.mubble.block.MubbleBlocks;
import fr.hugman.mubble.entity.GoombaEntity;
import fr.hugman.mubble.entity.GoombaVariant;
import fr.hugman.mubble.entity.GoombaVariants;
import fr.hugman.mubble.entity.MubbleEntityTypeKeys;
import fr.hugman.mubble.registry.MubbleRegistryKeys;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;

import java.util.Collections;
import java.util.function.Predicate;

public class MubbleItemGroups {
    public static final ItemGroup SUPER_MARIO = of(MubbleItemGroupKeys.SUPER_MARIO, FabricItemGroup.builder()
            .displayName(Text.translatable("item_group.mubble.super_mario"))
            .icon(() -> new ItemStack(MubbleBlocks.QUESTION_BLOCK))
            .build());
    public static final ItemGroup YOSHI_ISLAND = of(MubbleItemGroupKeys.YOSHI_ISLAND, FabricItemGroup.builder()
            .displayName(Text.translatable("item_group.mubble.yoshi_island"))
            .icon(() -> new ItemStack(MubbleBlocks.GREEN_EGG_BLOCK))
            .build());
    public static final ItemGroup SPLATOON = of(MubbleItemGroupKeys.SPLATOON, FabricItemGroup.builder()
            .displayName(Text.translatable("item_group.mubble.splatoon"))
            .icon(() -> new ItemStack(MubbleItems.SPLATTERSHOT))
            .build());

    private static ItemGroup of(RegistryKey<ItemGroup> key, ItemGroup itemGroup) {
        return Registry.register(Registries.ITEM_GROUP, key, itemGroup);
    }

    public static void appendItemGroups() {
        ItemGroupEvents.modifyEntriesEvent(MubbleItemGroupKeys.SUPER_MARIO).register(entries -> {
            var context = entries.getContext();

            entries.add(MubbleItems.MAKER_GLOVE);
            entries.add(MubbleBlocks.QUESTION_BLOCK);
            entries.add(MubbleBlocks.EMPTY_BLOCK);
            entries.add(MubbleBlocks.BRICK_BLOCK);
            entries.add(MubbleBlocks.CRYSTAL_BLOCK);
            entries.add(MubbleBlocks.GOLD_BLOCK);
            entries.add(MubbleBlocks.BLUE_EXCLAMATION_BLOCK);
            entries.add(MubbleBlocks.GREEN_EXCLAMATION_BLOCK);
            entries.add(MubbleBlocks.YELLOW_EXCLAMATION_BLOCK);
            entries.add(MubbleBlocks.RED_EXCLAMATION_BLOCK);
            entries.add(MubbleBlocks.NOTE_BLOCK);
            entries.add(MubbleBlocks.BLUE_MARIMBA_BLOCK);
            entries.add(MubbleBlocks.GREEN_MARIMBA_BLOCK);
            entries.add(MubbleBlocks.YELLOW_MARIMBA_BLOCK);
            entries.add(MubbleBlocks.RED_MARIMBA_BLOCK);
            entries.add(MubbleBlocks.SNAKE_BLOCK);
            entries.add(MubbleBlocks.FAST_SNAKE_BLOCK);
            entries.add(MubbleBlocks.SLOW_SNAKE_BLOCK);
            entries.add(MubbleBlocks.RED_BEEP_BLOCK);
            entries.add(MubbleBlocks.BLUE_BEEP_BLOCK);
            entries.add(MubbleItems.CAPE_FEATHER);
            entries.add(MubbleItems.SUPER_CAPE_FEATHER);
            entries.add(MubbleItems.GOOMBA_SPAWN_EGG);
            context.lookup()
                    .getOptional(MubbleRegistryKeys.GOOMBA_VARIANT)
                    .ifPresent(registryWrapper -> addGoombaVariantsSpawnEggs(
                            entries,
                            context.lookup(),
                            registryWrapper,
                            registryEntry -> true,
                            ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS
                    ));
        });

        appendSpawnEgg(MubbleItems.GOOMBA_SPAWN_EGG);

        ItemGroupEvents.modifyEntriesEvent(MubbleItemGroupKeys.YOSHI_ISLAND).register(entries -> {
            entries.add(MubbleBlocks.BLUE_EGG_BLOCK);
            entries.add(MubbleBlocks.CYAN_EGG_BLOCK);
            entries.add(MubbleBlocks.GREEN_EGG_BLOCK);
            entries.add(MubbleBlocks.YELLOW_EGG_BLOCK);
            entries.add(MubbleBlocks.ORANGE_EGG_BLOCK);
            entries.add(MubbleBlocks.RED_EGG_BLOCK);
            entries.add(MubbleBlocks.PINK_EGG_BLOCK);
            entries.add(MubbleBlocks.BLACK_EGG_BLOCK);
            entries.add(MubbleBlocks.WHITE_EGG_BLOCK);
        });
        ItemGroupEvents.modifyEntriesEvent(MubbleItemGroupKeys.SPLATOON).register(itemGroup -> {
            itemGroup.add(MubbleItems.TEST_SHOOTER);
            itemGroup.add(MubbleItems.SPLATTERSHOT);
            itemGroup.add(MubbleItems.DOT_96_GAL);
        });
    }

    public static void append(RegistryKey<ItemGroup> group, ItemGroupEvents.ModifyEntries modifier) {
        ItemGroupEvents.modifyEntriesEvent(group).register(modifier);
    }

    public static void appendSpawnEgg(Item spawnEgg) {
        var itemGroup = Registries.ITEM_GROUP.get(ItemGroups.SPAWN_EGGS);
        String path = Registries.ITEM.getId(spawnEgg).getPath();

        if (itemGroup == null) {
            return;
        }

        Predicate<ItemStack> predicate = stack1 -> {
            String path1 = Registries.ITEM.getId(stack1.getItem()).getPath();
            for (ItemStack stack2 : itemGroup.getDisplayStacks()) {
                String path2 = Registries.ITEM.getId(stack2.getItem()).getPath();
                if (path1.matches(".*_spawn_egg") && path2.matches(".*_spawn_egg")) {
                    // check if path is lexicographically between path1 and path2
                    if (path.compareTo(path1) > 0 && path.compareTo(path2) < 0) {
                        return true;
                    }
                }
            }
            return false;
        };
        append(ItemGroups.SPAWN_EGGS, e -> e.addAfter(predicate, Collections.singleton(new ItemStack(spawnEgg)), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS));
    }

    private static void addGoombaVariantsSpawnEggs(
            ItemGroup.Entries entries,
            RegistryWrapper.WrapperLookup registries,
            RegistryWrapper.Impl<GoombaVariant> registryWrapper,
            Predicate<RegistryEntry<GoombaVariant>> filter,
            ItemGroup.StackVisibility stackVisibility
    ) {
        RegistryOps<NbtElement> registryOps = registries.getOps(NbtOps.INSTANCE);
        registryWrapper.streamEntries()
                .filter(filter)
                //TODO: sort?
                .forEach(
                        entry -> {
                            if (GoombaVariants.NORMAL.getValue().equals(entry.registryKey().getValue())) {
                                return;
                            }
                            var stack = new ItemStack(MubbleItems.GOOMBA_SPAWN_EGG);
                            entry.value().spawnEggName().ifPresent(name -> stack.set(DataComponentTypes.ITEM_NAME, name));
                            if (stack.isEmpty()) {
                                return;
                            }
                            stack.set(DataComponentTypes.ENTITY_DATA, NbtComponent.DEFAULT
                                    .apply(nbt -> nbt.putString("id", MubbleEntityTypeKeys.GOOMBA.getValue().toString()))
                                    .with(registryOps, GoombaEntity.VARIANT_MAP_CODEC, entry)
                                    .getOrThrow());
                            entries.add(stack, stackVisibility);
                        }
                );
    }

}
