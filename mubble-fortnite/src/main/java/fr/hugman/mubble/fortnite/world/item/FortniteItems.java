package fr.hugman.mubble.fortnite.world.item;

import fr.hugman.mubble.fortnite.references.FortniteItemIds;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class FortniteItems {
    public static final ImpulseGrenadeItem IMPULSE_GRENADE = register(FortniteItemIds.IMPULSE_GRENADE, ImpulseGrenadeItem::new, new Item.Properties().stacksTo(ImpulseGrenadeItem.MAX_STACK_SIZE));

    private static <O extends Item> O register(ResourceKey<Item> key, Function<Item.Properties, O> factory, Item.Properties settings) {
        return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(settings.setId(key)));
    }
}
