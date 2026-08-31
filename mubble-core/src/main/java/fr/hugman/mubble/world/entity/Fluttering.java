package fr.hugman.mubble.world.entity;

import fr.hugman.mubble.world.power_up.ability.FlutterAbility;

import java.util.Optional;

/**
 * An entity that can extend its jump by fluttering, granted by whichever power-up it holds.
 * <p>
 * Injected onto {@code Player}. Both sides run the same flutter tick for tick: the client so that the
 * movement it predicts for itself actually rises, the server so that it knows what the movement it is being
 * sent is supposed to look like.
 *
 * @see FlutterAbility
 */
public interface Fluttering {
    /**
     * @return the flutter the currently held power-up grants, if it grants one at all
     */
    default Optional<FlutterAbility> getFlutterAbility() {
        return Optional.empty();
    }

    /**
     * @return whether the jump key is being held down right now, as far as this side can tell
     */
    default boolean isJumpKeyHeld() {
        return false;
    }

    /**
     * @return whether a flutter is going on right now
     */
    default boolean isFluttering() {
        return false;
    }

    /**
     * @return how many ticks the flutter under way has already run, 0 outside of one
     */
    default int getFlutterTicks() {
        return 0;
    }

    /**
     * @return whether the jump the holder is on has already spent its flutter
     */
    default boolean hasFluttered() {
        return false;
    }
}
