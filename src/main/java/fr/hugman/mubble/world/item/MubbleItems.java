package fr.hugman.mubble.world.item;

import fr.hugman.mubble.core.component.MubbleDataComponents;
import fr.hugman.mubble.references.MubbleItemsKeys;
import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import fr.hugman.mubble.world.item.component.PowerUpComponent;
import fr.hugman.mubble.references.PowerUpsKeys;
import fr.hugman.mubble.power_up.PowerUp;
import java.util.Optional;
import java.util.function.Function;

import fr.hugman.mubble.world.item.helper.ItemFactory;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.UseCooldown;

public class MubbleItems {
    // SUPER MARIO
    public static final Item MAKER_GLOVE = register(MubbleItemsKeys.MAKER_GLOVE, new Item.Properties().stacksTo(1));
    public static final KoopaShellItem GREEN_KOOPA_SHELL = register(MubbleItemsKeys.GREEN_KOOPA_SHELL, s -> new KoopaShellItem(s, false), new Item.Properties().stacksTo(3));
    public static final KoopaShellItem RED_KOOPA_SHELL = register(MubbleItemsKeys.RED_KOOPA_SHELL, s -> new KoopaShellItem(s, true), new Item.Properties().stacksTo(3));

    public static final PowerUpItem MINI_MUSHROOM = registerPowerUp(MubbleItemsKeys.MINI_MUSHROOM, PowerUpsKeys.MINI);
    public static final PowerUpItem MEGA_MUSHROOM = registerPowerUp(MubbleItemsKeys.MEGA_MUSHROOM, PowerUpsKeys.MEGA);
    public static final PowerUpItem FIRE_FLOWER = registerPowerUp(MubbleItemsKeys.FIRE_FLOWER, PowerUpsKeys.FIRE);
    public static final PowerUpItem ICE_FLOWER = registerPowerUp(MubbleItemsKeys.ICE_FLOWER, PowerUpsKeys.ICE);
    public static final CapeFeatherItem CAPE_FEATHER = register(MubbleItemsKeys.CAPE_FEATHER, s -> new CapeFeatherItem(s, false));
    public static final CapeFeatherItem SUPER_CAPE_FEATHER = register(MubbleItemsKeys.SUPER_CAPE_FEATHER, s -> new CapeFeatherItem(s.rarity(Rarity.EPIC), true));

    public static final SpawnEggItem GOOMBA_SPAWN_EGG = register(MubbleItemsKeys.GOOMBA_SPAWN_EGG, ItemFactory.spawnEgg(MubbleEntityTypes.GOOMBA));

    private static <O extends Item> O register(ResourceKey<Item> key, Function<Item.Properties, O> factory, Item.Properties settings) {
        return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(settings.setId(key)));
    }

    private static <O extends Item> O register(ResourceKey<Item> key, Function<Item.Properties, O> factory) {
        return register(key, factory, new Item.Properties());
    }

    private static Item register(ResourceKey<Item> key, Item.Properties settings) {
        return register(key, Item::new, settings.setId(key));
    }

    private static Item register(ResourceKey<Item> key) {
        return register(key, new Item.Properties());
    }

    private static PowerUpItem registerPowerUp(ResourceKey<Item> key, ResourceKey<PowerUp> powerUp) {
        return register(key, PowerUpItem::new, new Item.Properties()
                .component(MubbleDataComponents.POWER_UP, new PowerUpComponent(new EitherHolder<>(powerUp)))
                .component(DataComponents.USE_COOLDOWN, new UseCooldown(1.0f, Optional.of(MubbleCooldownGroups.POWER_UPS))));
    }
}
