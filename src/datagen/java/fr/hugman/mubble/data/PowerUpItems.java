package fr.hugman.mubble.data;

import fr.hugman.mubble.item.MubbleItemKeys;
import fr.hugman.mubble.power_up.PowerUps;
import fr.hugman.mubble.power_up.PowerUp;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;

import java.util.List;
import java.util.function.Consumer;

public class PowerUpItems {
    public record Entry(RegistryKey<Item> item, RegistryKey<PowerUp> powerUp) { }

    public static final List<Entry> ENTRIES = List.of(
            new Entry(MubbleItemKeys.MINI_MUSHROOM, PowerUps.MINI),
            new Entry(MubbleItemKeys.MEGA_MUSHROOM, PowerUps.MEGA),
            new Entry(MubbleItemKeys.FIRE_FLOWER, PowerUps.FIRE),
            new Entry(MubbleItemKeys.ICE_FLOWER, PowerUps.ICE)
    );

    public static RegistryKey<Item> getItem(RegistryKey<PowerUp> powerUp) {
        for (Entry entry : ENTRIES) {
            if (entry.powerUp.equals(powerUp)) {
                return entry.item;
            }
        }
        return null;
    }

    public static RegistryKey<PowerUp> getPowerUp(RegistryKey<Item> item) {
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
