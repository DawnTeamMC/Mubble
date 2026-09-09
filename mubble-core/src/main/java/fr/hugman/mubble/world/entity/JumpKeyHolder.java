package fr.hugman.mubble.world.entity;

/**
 * An entity whose jump key this side can read.
 * <p>
 * Injected onto {@code Player}, and the one thing the mid-air abilities all hang off. The server is told the
 * key of every player it runs by their input packets; a client only ever knows the one under the keyboard in
 * front of it, which is enough, since a client only ever simulates the player it controls.
 */
public interface JumpKeyHolder {
    /**
     * @return whether the jump key is being held down right now, as far as this side can tell
     */
    default boolean isJumpKeyHeld() {
        return false;
    }
}
