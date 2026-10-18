package fr.hugman.mubble.zelda.world.item;

import fr.hugman.mubble.world.item.helper.ItemFactory;
import fr.hugman.mubble.zelda.references.ZeldaItemIds;
import fr.hugman.mubble.zelda.world.entity.ZeldaEntityTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

import java.util.function.Function;

public class ZeldaItems {
	public static final SpawnEggItem LOFTWING_SPAWN_EGG = register(ZeldaItemIds.LOFTWING_SPAWN_EGG, ItemFactory.spawnEgg(ZeldaEntityTypes.LOFTWING));

	private static <O extends Item> O register(ResourceKey<Item> key, Function<Item.Properties, O> factory, Item.Properties settings) {
		return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(settings.setId(key)));
	}

	private static <O extends Item> O register(ResourceKey<Item> key, Function<Item.Properties, O> factory) {
		return register(key, factory, new Item.Properties());
	}
}
