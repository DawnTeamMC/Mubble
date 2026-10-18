package fr.hugman.mubble.super_mario.world.entity.freeze;

/**
 * How well an entity holds up against being frozen, which is what tells apart the three outcomes an
 * ice ball can have on it.
 */
public enum FreezeResistance {
    /** Trapped for the full duration. */
    NONE,
    /** Big enough to crack the ice open well before it melts. */
    TOUGH,
    /** Not to be trapped at all: the ice shatters on impact and leaves nothing behind. */
    IMMUNE
}
