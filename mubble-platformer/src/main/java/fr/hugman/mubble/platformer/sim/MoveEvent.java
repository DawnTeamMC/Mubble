package fr.hugman.mubble.platformer.sim;

import fr.hugman.mubble.platformer.cue.CueEvent;
import fr.hugman.mubble.platformer.move.PlatformerMove;

/**
 * Something that happened during a step, which the sides react to in their own way: the client plays
 * cues, the server charges exhaustion and counts statistics.
 *
 * @param type      the kind of event, which is also the cue it plays
 * @param move      the move the event belongs to
 * @param intensity how strong it was: the landing speed for a landing, the horizontal speed otherwise
 */
public record MoveEvent(CueEvent type, PlatformerMove move, double intensity) {
}
