package fr.hugman.mubble.data;

import fr.hugman.mubble.references.PowerUpKeys;
import java.util.List;
import java.util.function.Consumer;

import fr.hugman.mubble.references.MubbleItemKeys;
import fr.hugman.mubble.world.power_up.PowerUp;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class PowerUpItems {
    public record Entry(ResourceKey<Item> item, ResourceKey<PowerUp> powerUp) { }

    public static final List<Entry> ENTRIES = List.of(
            new Entry(MubbleItemKeys.MINI_MUSHROOM, PowerUpKeys.MINI),
            new Entry(MubbleItemKeys.MEGA_MUSHROOM, PowerUpKeys.MEGA),
            new Entry(MubbleItemKeys.FIRE_FLOWER, PowerUpKeys.FIRE),
            new Entry(MubbleItemKeys.ICE_FLOWER, PowerUpKeys.ICE),
            new Entry(MubbleItemKeys.GOLD_FLOWER, PowerUpKeys.GOLD)
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
