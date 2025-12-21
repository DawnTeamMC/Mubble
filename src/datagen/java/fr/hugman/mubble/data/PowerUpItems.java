package fr.hugman.mubble.data;

import fr.hugman.mubble.references.PowerUps;
import fr.hugman.mubble.power_up.PowerUp;
import java.util.List;
import java.util.function.Consumer;

import fr.hugman.mubble.world.item.MubbleItemKeys;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class PowerUpItems {
    public record Entry(ResourceKey<Item> item, ResourceKey<PowerUp> powerUp) { }

    public static final List<Entry> ENTRIES = List.of(
            new Entry(MubbleItemKeys.MINI_MUSHROOM, PowerUps.MINI),
            new Entry(MubbleItemKeys.MEGA_MUSHROOM, PowerUps.MEGA),
            new Entry(MubbleItemKeys.FIRE_FLOWER, PowerUps.FIRE),
            new Entry(MubbleItemKeys.ICE_FLOWER, PowerUps.ICE)
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
