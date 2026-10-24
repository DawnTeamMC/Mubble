package fr.hugman.mubble.platformer;

/**
 * A player carrying an platformer movement controller. Every player is one, through a mixin.
 */
public interface PlatformerPlayer {
    default PlatformerController mubble$platformer() {
        throw new UnsupportedOperationException("Implemented by mixin");
    }

    default PlatformerVisual mubble$platformerVisual() {
        return PlatformerVisual.NONE;
    }

    default void mubble$setPlatformerVisual(PlatformerVisual visual) {
    }
}
