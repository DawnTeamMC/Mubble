package fr.hugman.mubble.super_mario.data;

import fr.hugman.mubble.super_mario.references.SuperMarioItemKeys;
import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpKeys;
import fr.hugman.mubble.world.power_up.PowerUp;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Consumer;

public class PowerUpItems {
    public record Entry(ResourceKey<Item> item, ResourceKey<PowerUp> powerUp) { }

    public static final List<Entry> ENTRIES = List.of(
            new Entry(SuperMarioItemKeys.MINI_MUSHROOM, SuperMarioPowerUpKeys.MINI),
            new Entry(SuperMarioItemKeys.MEGA_MUSHROOM, SuperMarioPowerUpKeys.MEGA),
            new Entry(SuperMarioItemKeys.FIRE_FLOWER, SuperMarioPowerUpKeys.FIRE),
            new Entry(SuperMarioItemKeys.ICE_FLOWER, SuperMarioPowerUpKeys.ICE),
            new Entry(SuperMarioItemKeys.GOLD_FLOWER, SuperMarioPowerUpKeys.GOLD),
            new Entry(SuperMarioItemKeys.CLOUD_FLOWER, SuperMarioPowerUpKeys.CLOUD),
            new Entry(SuperMarioItemKeys.BUBBLE_FLOWER, SuperMarioPowerUpKeys.BUBBLE)
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
