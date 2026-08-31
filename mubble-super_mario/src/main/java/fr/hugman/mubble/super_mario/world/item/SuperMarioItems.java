package fr.hugman.mubble.super_mario.world.item;

import fr.hugman.mubble.core.component.MubbleDataComponents;
import fr.hugman.mubble.super_mario.references.SuperMarioItemIds;
import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpIds;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.item.component.SuperMarioConsumables;
import fr.hugman.mubble.world.item.MubbleCooldownGroups;
import fr.hugman.mubble.world.item.PowerUpItem;
import fr.hugman.mubble.world.item.component.PowerUpComponent;
import java.util.Optional;
import java.util.function.Function;

import fr.hugman.mubble.world.item.helper.ItemFactory;
import fr.hugman.mubble.world.power_up.PowerUp;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.UseCooldown;

public class SuperMarioItems {
    // SUPER MARIO
	public static final Item COIN = register(SuperMarioItemIds.COIN, CollectibleItem::new);
	public static final Item RED_COIN = register(SuperMarioItemIds.RED_COIN, CollectibleItem::new);
	public static final Item BLUE_COIN = register(SuperMarioItemIds.BLUE_COIN, CollectibleItem::new);
	public static final Item FLOWER_COIN = register(SuperMarioItemIds.FLOWER_COIN, CollectibleItem::new);
    public static final KoopaShellItem GREEN_KOOPA_SHELL = register(SuperMarioItemIds.GREEN_KOOPA_SHELL, s -> new KoopaShellItem(s, false), new Item.Properties().stacksTo(3));
    public static final KoopaShellItem RED_KOOPA_SHELL = register(SuperMarioItemIds.RED_KOOPA_SHELL, s -> new KoopaShellItem(s, true), new Item.Properties().stacksTo(3));

    public static final Item SUPER_MUSHROOM = register(SuperMarioItemIds.SUPER_MUSHROOM, new Item.Properties().component(DataComponents.CONSUMABLE, SuperMarioConsumables.SUPER_MUSHROOM));
    public static final PowerUpItem MINI_MUSHROOM = registerPowerUp(SuperMarioItemIds.MINI_MUSHROOM, SuperMarioPowerUpIds.MINI);
    public static final PowerUpItem MEGA_MUSHROOM = registerPowerUp(SuperMarioItemIds.MEGA_MUSHROOM, SuperMarioPowerUpIds.MEGA);
    public static final PowerUpItem FIRE_FLOWER = registerPowerUp(SuperMarioItemIds.FIRE_FLOWER, SuperMarioPowerUpIds.FIRE);
    public static final PowerUpItem ICE_FLOWER = registerPowerUp(SuperMarioItemIds.ICE_FLOWER, SuperMarioPowerUpIds.ICE);
    public static final PowerUpItem GOLD_FLOWER = registerPowerUp(SuperMarioItemIds.GOLD_FLOWER, SuperMarioPowerUpIds.GOLD);
    public static final PowerUpItem CLOUD_FLOWER = registerPowerUp(SuperMarioItemIds.CLOUD_FLOWER, SuperMarioPowerUpIds.CLOUD);
    public static final PowerUpItem BUBBLE_FLOWER = registerPowerUp(SuperMarioItemIds.BUBBLE_FLOWER, SuperMarioPowerUpIds.BUBBLE);
    public static final CapeFeatherItem CAPE_FEATHER = register(SuperMarioItemIds.CAPE_FEATHER, s -> new CapeFeatherItem(s, false));
    public static final CapeFeatherItem SUPER_CAPE_FEATHER = register(SuperMarioItemIds.SUPER_CAPE_FEATHER, s -> new CapeFeatherItem(s.rarity(Rarity.EPIC), true));

	public static final Item MAKER_GLOVE = register(SuperMarioItemIds.MAKER_GLOVE, new Item.Properties().stacksTo(1));

	public static final SpawnEggItem GOOMBA_SPAWN_EGG = register(SuperMarioItemIds.GOOMBA_SPAWN_EGG, ItemFactory.spawnEgg(SuperMarioEntityTypes.GOOMBA));

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
                .delayedComponent(MubbleDataComponents.POWER_UP, (context) -> new PowerUpComponent(context.getOrThrow(powerUp)))
                .component(DataComponents.USE_COOLDOWN, new UseCooldown(1.0f, Optional.of(MubbleCooldownGroups.POWER_UPS))));
    }
}
