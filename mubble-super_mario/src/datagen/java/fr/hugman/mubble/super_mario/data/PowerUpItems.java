package fr.hugman.mubble.super_mario.data;

import fr.hugman.mubble.super_mario.references.SuperMarioItemIds;
import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpIds;
import fr.hugman.mubble.world.power_up.PowerUp;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Consumer;

public class PowerUpItems {
    public record Entry(ResourceKey<Item> item, ResourceKey<PowerUp> powerUp) { }

    public static final List<Entry> ENTRIES = List.of(
            new Entry(SuperMarioItemIds.MINI_MUSHROOM, SuperMarioPowerUpIds.MINI),
            new Entry(SuperMarioItemIds.MEGA_MUSHROOM, SuperMarioPowerUpIds.MEGA),
            new Entry(SuperMarioItemIds.FIRE_FLOWER, SuperMarioPowerUpIds.FIRE),
            new Entry(SuperMarioItemIds.ICE_FLOWER, SuperMarioPowerUpIds.ICE),
            new Entry(SuperMarioItemIds.GOLD_FLOWER, SuperMarioPowerUpIds.GOLD),
            new Entry(SuperMarioItemIds.CLOUD_FLOWER, SuperMarioPowerUpIds.CLOUD),
            new Entry(SuperMarioItemIds.BUBBLE_FLOWER, SuperMarioPowerUpIds.BUBBLE),
            new Entry(SuperMarioItemIds.SUPER_FLOWER_POT, SuperMarioPowerUpIds.FLOWER)
    );

    public static ResourceKey<Item> getItem(ResourceKey<PowerUp> powerUp) {
        for (Entry entry : ENTRIES) {
            if (entry.powerUp.equals(powerUp)) {
                return entry.item;
            }
        }
        return null;
    }

    public static ResourceKey<PowerUp> getPowerUp(ResourceKey<Item> item) {
        for (Entry entry : ENTRIES) {
            if (entry.item.equals(item)) {
                return entry.powerUp;
            }
        }
        return null;
    }

    public static void forEach(Consumer<Entry> action) {
        for (Entry entry : ENTRIES) {
            action.accept(entry);
        }
    }
}
