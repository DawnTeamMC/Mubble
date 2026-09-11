package fr.hugman.mubble.super_mario.world.entity.monster.goomba;

/**
 * An entity mini goombas can latch onto.
 * <p>
 * A clinging mini goomba is not an entity any more, the way a parrot on a shoulder is not: it is a number
 * the carrier keeps, and it only becomes a {@link MiniGoomba} again once it is shaken off. Players are the
 * only carriers for now, which is also the only thing a mini goomba targets on its own.
 *
 * @author Hugman
 * @since v4.0.0
 */
public interface MiniGoombaCarrier {
    /** How many mini goombas are currently clinging on. */
    default int getClingingMiniGoombas() {
        return 0;
    }

    default void setClingingMiniGoombas(int count) {
    }

    /** The most that can hold on at once, past which further mini goombas bounce off. */
    default int getMaxClingingMiniGoombas() {
        return MiniGoomba.MAX_CLINGING;
    }

    /**
     * Takes one more mini goomba on, if there is still room for it.
     *
     * @return whether it found a hold
     */
    default boolean addClingingMiniGoomba() {
        int count = this.getClingingMiniGoombas();
        if (count >= this.getMaxClingingMiniGoombas()) {
            return false;
        }
        this.setClingingMiniGoombas(count + 1);
        return true;
    }
}
