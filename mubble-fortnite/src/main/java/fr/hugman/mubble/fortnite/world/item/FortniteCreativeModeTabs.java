package fr.hugman.mubble.fortnite.world.item;

import fr.hugman.mubble.fortnite.Fortnite;
import fr.hugman.mubble.fortnite.references.FortniteCreativeModeTabIds;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class FortniteCreativeModeTabs {
    public static final CreativeModeTab FORTNITE = register(FortniteCreativeModeTabIds.FORTNITE, FabricCreativeModeTab.builder()
            .title(Component.translatable("item_group." + Fortnite.MOD_ID + ".fortnite"))
            .icon(() -> new ItemStack(FortniteItems.IMPULSE_GRENADE))
            .build());

    private static CreativeModeTab register(ResourceKey<CreativeModeTab> key, CreativeModeTab itemGroup) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, itemGroup);
    }

    public static void appendItemGroups() {
        CreativeModeTabEvents.modifyOutputEvent(FortniteCreativeModeTabIds.FORTNITE).register(entries -> {
            entries.accept(FortniteItems.IMPULSE_GRENADE);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(entries -> {
            entries.insertAfter(Items.WIND_CHARGE, FortniteItems.IMPULSE_GRENADE);
        });
    }
}
