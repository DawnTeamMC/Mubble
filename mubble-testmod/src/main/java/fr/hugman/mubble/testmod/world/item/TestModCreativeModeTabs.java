package fr.hugman.mubble.testmod.world.item;

import fr.hugman.mubble.testmod.MubbleTestMod;
import fr.hugman.mubble.testmod.references.TestModCreativeModeTabIds;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

/**
 * A tab of its own, so that the sandbox content stays out of the tabs the mod ships and is reachable
 * in creative without having to remember any id.
 */
public class TestModCreativeModeTabs {
    public static final CreativeModeTab SANDBOX = register(TestModCreativeModeTabIds.SANDBOX, FabricCreativeModeTab.builder()
            .title(Component.translatable("item_group." + MubbleTestMod.MOD_ID + ".sandbox"))
            .icon(() -> new ItemStack(TestModItems.SNOWBALL_FLOWER))
            .build());

    private static CreativeModeTab register(ResourceKey<CreativeModeTab> key, CreativeModeTab itemGroup) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, itemGroup);
    }

    public static void appendItemGroups() {
        CreativeModeTabEvents.modifyOutputEvent(TestModCreativeModeTabIds.SANDBOX).register(entries -> {
            entries.accept(TestModItems.SNOWBALL_FLOWER);
        });
    }
}
