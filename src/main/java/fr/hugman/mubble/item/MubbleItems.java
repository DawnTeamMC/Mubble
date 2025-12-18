package fr.hugman.mubble.item;

import fr.hugman.mubble.component.MubbleDataComponentTypes;
import fr.hugman.mubble.component.PowerUpComponent;
import fr.hugman.mubble.entity.MubbleEntityTypes;
import fr.hugman.mubble.power_up.PowerUps;
import fr.hugman.mubble.power_up.PowerUp;
import fr.hugman.mubble.item.helper.ItemFactory;
import java.util.Optional;
import java.util.function.Function;
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
    public static final Item MAKER_GLOVE = of(MubbleItemKeys.MAKER_GLOVE, new Item.Properties().stacksTo(1));
    public static final KoopaShellItem GREEN_KOOPA_SHELL = of(MubbleItemKeys.GREEN_KOOPA_SHELL, s -> new KoopaShellItem(s, false), new Item.Properties().stacksTo(3));
    public static final KoopaShellItem RED_KOOPA_SHELL = of(MubbleItemKeys.RED_KOOPA_SHELL, s -> new KoopaShellItem(s, true), new Item.Properties().stacksTo(3));

    public static final PowerUpItem MINI_MUSHROOM = powerUp(MubbleItemKeys.MINI_MUSHROOM, PowerUps.MINI);
    public static final PowerUpItem MEGA_MUSHROOM = powerUp(MubbleItemKeys.MEGA_MUSHROOM, PowerUps.MEGA);
    public static final PowerUpItem FIRE_FLOWER = powerUp(MubbleItemKeys.FIRE_FLOWER, PowerUps.FIRE);
    public static final PowerUpItem ICE_FLOWER = powerUp(MubbleItemKeys.ICE_FLOWER, PowerUps.ICE);
    public static final CapeFeatherItem CAPE_FEATHER = of(MubbleItemKeys.CAPE_FEATHER, s -> new CapeFeatherItem(s, false));
    public static final CapeFeatherItem SUPER_CAPE_FEATHER = of(MubbleItemKeys.SUPER_CAPE_FEATHER, s -> new CapeFeatherItem(s.rarity(Rarity.EPIC), true));

    public static final SpawnEggItem GOOMBA_SPAWN_EGG = of(MubbleItemKeys.GOOMBA_SPAWN_EGG, ItemFactory.spawnEgg(MubbleEntityTypes.GOOMBA));

    private static <O extends Item> O of(ResourceKey<Item> key, Function<Item.Properties, O> factory, Item.Properties settings) {
        return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(settings.setId(key)));
    }

    private static <O extends Item> O of(ResourceKey<Item> key, Function<Item.Properties, O> factory) {
        return of(key, factory, new Item.Properties());
    }

    private static Item of(ResourceKey<Item> key, Item.Properties settings) {
        return of(key, Item::new, settings.setId(key));
    }

    private static Item of(ResourceKey<Item> key) {
        return of(key, new Item.Properties());
    }

    private static PowerUpItem powerUp(ResourceKey<Item> key, ResourceKey<PowerUp> powerUp) {
        return of(key, PowerUpItem::new, new Item.Properties()
                .component(MubbleDataComponentTypes.POWER_UP, new PowerUpComponent(new EitherHolder<>(powerUp)))
                .component(DataComponents.USE_COOLDOWN, new UseCooldown(1.0f, Optional.of(MubbleCooldownGroups.POWER_UPS))));
    }
}
