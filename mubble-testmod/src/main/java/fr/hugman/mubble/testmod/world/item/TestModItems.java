package fr.hugman.mubble.testmod.world.item;

import fr.hugman.mubble.core.component.MubbleDataComponents;
import fr.hugman.mubble.testmod.references.TestModItemIds;
import fr.hugman.mubble.testmod.references.TestModPowerUpIds;
import fr.hugman.mubble.world.item.MubbleCooldownGroups;
import fr.hugman.mubble.world.item.PowerUpItem;
import fr.hugman.mubble.world.item.component.PowerUpComponent;
import fr.hugman.mubble.world.power_up.PowerUp;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.UseCooldown;

import java.util.Optional;
import java.util.function.Function;

public class TestModItems {
    public static final PowerUpItem SNOWBALL_FLOWER = registerPowerUp(TestModItemIds.SNOWBALL_FLOWER, TestModPowerUpIds.SNOWBALL);

    private static <O extends Item> O register(ResourceKey<Item> key, Function<Item.Properties, O> factory, Item.Properties settings) {
        return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(settings.setId(key)));
    }

    private static PowerUpItem registerPowerUp(ResourceKey<Item> key, ResourceKey<PowerUp> powerUp) {
        return register(key, PowerUpItem::new, new Item.Properties()
                .delayedComponent(MubbleDataComponents.POWER_UP, (context) -> new PowerUpComponent(context.getOrThrow(powerUp)))
                .component(DataComponents.USE_COOLDOWN, new UseCooldown(1.0f, Optional.of(MubbleCooldownGroups.POWER_UPS))));
    }
}
