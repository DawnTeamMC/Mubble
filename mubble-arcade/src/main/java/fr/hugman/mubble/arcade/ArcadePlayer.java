package fr.hugman.mubble.arcade;

/**
 * A player carrying an arcade movement controller. Every player is one, through a mixin.
 */
public interface ArcadePlayer {
    default ArcadeController mubble$arcade() {
        throw new UnsupportedOperationException("Implemented by mixin");
    }

    default ArcadeVisual mubble$arcadeVisual() {
        return ArcadeVisual.NONE;
    }

    default void mubble$setArcadeVisual(ArcadeVisual visual) {
    }
}
