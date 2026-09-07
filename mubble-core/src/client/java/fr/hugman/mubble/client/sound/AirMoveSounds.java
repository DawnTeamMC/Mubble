package fr.hugman.mubble.client.sound;

import fr.hugman.mubble.world.power_up.ability.FloatAbility;
import fr.hugman.mubble.world.power_up.ability.FlutterAbility;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

/**
 * Keeps one loop going per player on a mid-air move in sight.
 * <p>
 * A sound instance stops itself once the move it belongs to is over, but nothing would stop a second one
 * from being started on the very next tick, so the ones already playing are held onto here. The map is weak
 * on purpose: a player that walked out of range, or left the game, takes their entry with them.
 */
@Environment(EnvType.CLIENT)
public final class AirMoveSounds {
    private static final Map<Player, AirMoveSoundInstance> PLAYING = new WeakHashMap<>();

    private AirMoveSounds() {
    }

    /**
     * @return the loop {@code player} is owed right now, the climb taking precedence over the descent for
     * the tick or two in which a client believes they are on both
     */
    public static Optional<Holder<SoundEvent>> soundFor(Player player) {
        if (player.isFluttering()) {
            return player.getFlutterAbility().flatMap(FlutterAbility::sound);
        }
        if (player.isFloating()) {
            return player.getFloatAbility().flatMap(FloatAbility::sound);
        }
        return Optional.empty();
    }

    public static void tick(Minecraft client) {
        if (client.level == null) {
            PLAYING.clear();
            return;
        }
        for (Player player : client.level.players()) {
            var wanted = soundFor(player);
            if (wanted.isEmpty()) {
                PLAYING.remove(player);
                continue;
            }
            var playing = PLAYING.get(player);
            if (playing != null && !playing.isStopped() && wanted.get().equals(playing.event())) {
                continue;
            }
            var instance = new AirMoveSoundInstance(player, wanted.get());
            PLAYING.put(player, instance);
            client.getSoundManager().play(instance);
        }
    }
}
