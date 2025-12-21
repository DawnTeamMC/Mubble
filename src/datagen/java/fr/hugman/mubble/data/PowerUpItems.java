package fr.hugman.mubble.data;

import fr.hugman.mubble.references.PowerUpsKeys;
import fr.hugman.mubble.power_up.PowerUp;
import java.util.List;
import java.util.function.Consumer;

import fr.hugman.mubble.references.MubbleItemsKeys;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class PowerUpItems {
    public record Entry(ResourceKey<Item> item, ResourceKey<PowerUp> powerUp) { }

    public static final List<Entry> ENTRIES = List.of(
            new Entry(MubbleItemsKeys.MINI_MUSHROOM, PowerUpsKeys.MINI),
            new Entry(MubbleItemsKeys.MEGA_MUSHROOM, PowerUpsKeys.MEGA),
            new Entry(MubbleItemsKeys.FIRE_FLOWER, PowerUpsKeys.FIRE),
            new Entry(MubbleItemsKeys.ICE_FLOWER, PowerUpsKeys.ICE)
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
