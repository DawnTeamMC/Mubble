package fr.hugman.mubble.super_mario.client.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.Map;

/**
 * One looping sound per running snake, kept alive by its head as it goes.
 * <p>
 * The head is a block entity that is thrown away and built again in the next cell on every step, so
 * the sound cannot simply hang off it: it is keyed on the snake instead, and the head reports where
 * it is while it exists.
 *
 * @author Hugman
 * @since v4.0.0
 */
@Environment(EnvType.CLIENT)
public final class SnakeSounds {
    private static final Map<Integer, SnakeMoveSoundInstance> ACTIVE = new HashMap<>();

    private SnakeSounds() {
    }

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(SnakeSounds::tick);
    }

    /** Tells the sound of snake {@code snakeId} where its head is now, starting it if need be. */
    public static void heardAt(int snakeId, double x, double y, double z) {
        SnakeMoveSoundInstance instance = ACTIVE.get(snakeId);
        if (instance == null || instance.isStopped()) {
            instance = new SnakeMoveSoundInstance(x, y, z);
            ACTIVE.put(snakeId, instance);
            Minecraft.getInstance().getSoundManager().play(instance);
            return;
        }
        instance.moveTo(x, y, z);
    }

    private static void tick(Minecraft client) {
        if (client.level == null) {
            ACTIVE.values().forEach(SnakeMoveSoundInstance::silence);
            ACTIVE.clear();
            return;
        }
        ACTIVE.values().removeIf(SnakeMoveSoundInstance::isStopped);
    }
}
