package fr.hugman.mubble.client.sound;

import fr.hugman.mubble.world.power_up.ability.FlutterAbility;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Keeps one flutter loop going per player fluttering in sight.
 * <p>
 * A sound instance stops itself once the flutter it belongs to is over, but nothing would stop a second one
 * from being started on the very next tick, so the ones already playing are held onto here. The map is weak
 * on purpose: a player that walked out of range, or left the game, takes their entry with them.
 */
@Environment(EnvType.CLIENT)
public final class FlutterSounds {
    private static final Map<Player, FlutterSoundInstance> PLAYING = new WeakHashMap<>();

    private FlutterSounds() {
    }

    public static void tick(Minecraft client) {
        if (client.level == null) {
            PLAYING.clear();
            return;
        }
        for (Player player : client.level.players()) {
            if (!player.isFluttering()) {
                PLAYING.remove(player);
                continue;
            }
            var playing = PLAYING.get(player);
            if (playing != null && !playing.isStopped()) {
                continue;
            }
            player.getFlutterAbility().flatMap(FlutterAbility::sound).ifPresent(sound -> {
                var instance = new FlutterSoundInstance(player, sound.value());
                PLAYING.put(player, instance);
                client.getSoundManager().play(instance);
            });
        }
    }
}
