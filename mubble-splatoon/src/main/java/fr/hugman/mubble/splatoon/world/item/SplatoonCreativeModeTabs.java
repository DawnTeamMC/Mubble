package fr.hugman.mubble.splatoon.world.item;

import fr.hugman.mubble.splatoon.Splatoon;
import fr.hugman.mubble.splatoon.references.SplatoonCreativeModeTabKeys;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class SplatoonCreativeModeTabs {
    public static final CreativeModeTab SPLATOON = register(SplatoonCreativeModeTabKeys.SPLATOON, FabricCreativeModeTab.builder()
            .title(Component.translatable("item_group." + Splatoon.MOD_ID +".splatoon"))
            .icon(() -> new ItemStack(SplatoonItems.SPLATTERSHOT))
            .build());

    private static CreativeModeTab register(ResourceKey<CreativeModeTab> key, CreativeModeTab itemGroup) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, itemGroup);
    }

    public static void appendItemGroups() {
        CreativeModeTabEvents.modifyOutputEvent(SplatoonCreativeModeTabKeys.SPLATOON).register(entries -> {
			entries.accept(SplatoonItems.TEST_SHOOTER);
			entries.accept(SplatoonItems.SPLATTERSHOT);
			entries.accept(SplatoonItems.DOT_96_GAL);
        });
    }

    public static void append(ResourceKey<CreativeModeTab> group, CreativeModeTabEvents.ModifyOutput modifier) {
        CreativeModeTabEvents.modifyOutputEvent(group).register(modifier);
    }
}
