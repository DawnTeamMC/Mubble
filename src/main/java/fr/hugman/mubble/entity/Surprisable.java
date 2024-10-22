package fr.hugman.mubble.entity;

/**
 * Represents an entity that can be surprised.
 *
 * @author Hugman
 * @since v4.0.0
 */
public interface Surprisable {
    boolean isSurprised();

    void setSurprised(boolean b);

    default void onSurprised() {
    }

    ;
}
