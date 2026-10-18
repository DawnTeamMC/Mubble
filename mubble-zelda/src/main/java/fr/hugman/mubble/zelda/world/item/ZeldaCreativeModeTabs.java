package fr.hugman.mubble.zelda.world.item;

import fr.hugman.mubble.zelda.Zelda;
import fr.hugman.mubble.zelda.core.component.ZeldaDataComponents;
import fr.hugman.mubble.zelda.core.registries.ZeldaRegistries;
import fr.hugman.mubble.zelda.references.ZeldaCreativeModeTabIds;
import fr.hugman.mubble.zelda.world.entity.animal.loftwing.LoftwingVariant;
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
import net.minecraft.world.item.ItemStack;

public class ZeldaCreativeModeTabs {
	public static final CreativeModeTab ZELDA = register(ZeldaCreativeModeTabIds.ZELDA, FabricCreativeModeTab.builder()
			.title(Component.translatable("item_group." + Zelda.MOD_ID + ".zelda"))
			.icon(() -> new ItemStack(ZeldaItems.LOFTWING_SPAWN_EGG))
			.build());

	private static CreativeModeTab register(ResourceKey<CreativeModeTab> key, CreativeModeTab tab) {
		return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
	}

	public static void appendItemGroups() {
		CreativeModeTabEvents.modifyOutputEvent(ZeldaCreativeModeTabIds.ZELDA).register(entries -> {
			entries.accept(ZeldaItems.LOFTWING_WHISTLE);
			entries.accept(ZeldaItems.LOFTWING_BELT);
			entries.accept(ZeldaItems.LEATHER_LOFTWING_HELMET);
			entries.accept(ZeldaItems.GOLDEN_LOFTWING_HELMET);
			entries.accept(ZeldaItems.CHAINMAIL_LOFTWING_HELMET);
			entries.accept(ZeldaItems.IRON_LOFTWING_HELMET);
			entries.accept(ZeldaItems.DIAMOND_LOFTWING_HELMET);
			entries.accept(ZeldaItems.NETHERITE_LOFTWING_HELMET);
			entries.accept(ZeldaItems.LOFTWING_SPAWN_EGG);
			entries.getContext().holders()
					.lookup(ZeldaRegistries.LOFTWING_VARIANT)
					.ifPresent(lookup -> addLoftwingVariantSpawnEggs(entries, lookup));
		});

		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS).register(entries -> entries.accept(ZeldaItems.LOFTWING_SPAWN_EGG));
	}

	/** One extra egg per breed that asked for one, so that a crimson loftwing can be summoned on purpose. */
	private static void addLoftwingVariantSpawnEggs(CreativeModeTab.Output entries, HolderLookup.RegistryLookup<LoftwingVariant> lookup) {
		lookup.listElements().forEach(entry -> {
			var spawnEgg = entry.value().spawnEggInfo();
			if (spawnEgg.isEmpty()) {
				return;
			}
			ItemStack stack = new ItemStack(ZeldaItems.LOFTWING_SPAWN_EGG);
			stack.set(DataComponents.ITEM_NAME, spawnEgg.get().name());
			stack.set(ZeldaDataComponents.LOFTWING_VARIANT, entry);
			entries.accept(stack, spawnEgg.get().onlyInSearch() ? CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY : CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
		});
	}
}
