package fr.hugman.mubble.item;

import fr.hugman.mubble.block.MubbleBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;

public class MubbleItemGroups {
    public static final ItemGroup SUPER_MARIO = of(MubbleItemGroupKeys.SUPER_MARIO, FabricItemGroup.builder()
            .displayName(Text.translatable("item_group.mubble.super_mario"))
            .icon(() -> new ItemStack(MubbleBlocks.QUESTION_BLOCK))
            .build());
    public static final ItemGroup YOSHI_ISLAND = of(MubbleItemGroupKeys.YOSHI_ISLAND, FabricItemGroup.builder()
            .displayName(Text.translatable("item_group.mubble.yoshi_island"))
            .icon(() -> new ItemStack(MubbleBlocks.GREEN_EGG_BLOCK))
            .build());

    private static ItemGroup of(RegistryKey<ItemGroup> key, ItemGroup itemGroup) {
        return Registry.register(Registries.ITEM_GROUP, key, itemGroup);
    }

    public static void appendItemGroups() {
        ItemGroupEvents.modifyEntriesEvent(MubbleItemGroupKeys.SUPER_MARIO).register(itemGroup -> {
            itemGroup.add(MubbleItems.MAKER_GLOVE);
            itemGroup.add(MubbleBlocks.QUESTION_BLOCK);
            itemGroup.add(MubbleBlocks.EMPTY_BLOCK);
            itemGroup.add(MubbleBlocks.BRICK_BLOCK);
            itemGroup.add(MubbleBlocks.CRYSTAL_BLOCK);
            itemGroup.add(MubbleBlocks.GOLD_BLOCK);
            itemGroup.add(MubbleBlocks.BLUE_EXCLAMATION_BLOCK);
            itemGroup.add(MubbleBlocks.GREEN_EXCLAMATION_BLOCK);
            itemGroup.add(MubbleBlocks.YELLOW_EXCLAMATION_BLOCK);
            itemGroup.add(MubbleBlocks.RED_EXCLAMATION_BLOCK);
            itemGroup.add(MubbleBlocks.NOTE_BLOCK);
            itemGroup.add(MubbleBlocks.BLUE_MARIMBA_BLOCK);
            itemGroup.add(MubbleBlocks.GREEN_MARIMBA_BLOCK);
            itemGroup.add(MubbleBlocks.YELLOW_MARIMBA_BLOCK);
            itemGroup.add(MubbleBlocks.RED_MARIMBA_BLOCK);
            itemGroup.add(MubbleBlocks.SNAKE_BLOCK);
            itemGroup.add(MubbleBlocks.FAST_SNAKE_BLOCK);
            itemGroup.add(MubbleBlocks.SLOW_SNAKE_BLOCK);
            itemGroup.add(MubbleBlocks.RED_BEEP_BLOCK);
            itemGroup.add(MubbleBlocks.BLUE_BEEP_BLOCK);
            itemGroup.add(MubbleItems.CAPE_FEATHER);
            itemGroup.add(MubbleItems.SUPER_CAPE_FEATHER);
        });

        ItemGroupEvents.modifyEntriesEvent(MubbleItemGroupKeys.YOSHI_ISLAND).register(itemGroup -> {
            itemGroup.add(MubbleBlocks.BLUE_EGG_BLOCK);
            itemGroup.add(MubbleBlocks.CYAN_EGG_BLOCK);
            itemGroup.add(MubbleBlocks.GREEN_EGG_BLOCK);
            itemGroup.add(MubbleBlocks.YELLOW_EGG_BLOCK);
            itemGroup.add(MubbleBlocks.ORANGE_EGG_BLOCK);
            itemGroup.add(MubbleBlocks.RED_EGG_BLOCK);
            itemGroup.add(MubbleBlocks.PINK_EGG_BLOCK);
            itemGroup.add(MubbleBlocks.BLACK_EGG_BLOCK);
            itemGroup.add(MubbleBlocks.WHITE_EGG_BLOCK);
        });
    }
}
