package fr.hugman.mubble.super_mario.world.entity.freeze;

/**
 * How well an entity holds up against being frozen, which is what tells apart the three outcomes an
 * ice ball can have on it.
 */
public enum FreezeResistance {
    /** Trapped for the full duration, and none the worse for it once it thaws. */
    NONE,
    /**
     * Big enough to crack the ice open well before it melts, but not without hurting itself doing so.
     */
    TOUGH,
    /** Far too big to be trapped at all: the ice shatters on impact and only stings. */
    IMMUNE
}
