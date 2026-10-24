package fr.hugman.mubble.test.unit.platformer;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.platformer.references.PlatformerMoveIds;
import fr.hugman.mubble.platformer.references.PlatformerProfileIds;
import fr.hugman.mubble.test.unit.support.Registrations;
import fr.hugman.mubble.platformer.PlatformerProfile;
import fr.hugman.mubble.platformer.PlatformerUnlocks;
import fr.hugman.mubble.platformer.access.AccessMode;
import fr.hugman.mubble.platformer.access.PlatformerAccess;
import fr.hugman.mubble.platformer.access.PlatformerSource;
import fr.hugman.mubble.platformer.access.MoveSelector;
import fr.hugman.mubble.platformer.move.PlatformerMoves;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.resources.ResourceKey;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The resolution of move access: {@code deny > force > enable}, {@code enable} only counting for an
 * owned move, and the profile of the source with the highest priority.
 * <p>
 * Only plain move ids here: tags are bound by a data pack, which unit tests do not load. The tag side
 * is covered by {@code PlatformerAccessGameTest}.
 */
public class PlatformerAccessTest {
    private static final long NOW = 1000L;

    @BeforeAll
    static void registerContent() {
        Registrations.registerEverything();
    }

    private static PlatformerSource source(String id, int priority, Optional<ResourceKey<PlatformerProfile>> profile, Map<MoveSelector, AccessMode> moves) {
        return new PlatformerSource(Mubble.id(id), profile, moves, priority, Optional.empty());
    }

    private static Map<MoveSelector, AccessMode> moves(Object... pairs) {
        var map = new java.util.LinkedHashMap<MoveSelector, AccessMode>();
        for (int i = 0; i < pairs.length; i += 2) {
            @SuppressWarnings("unchecked")
            var key = (ResourceKey<fr.hugman.mubble.platformer.move.PlatformerMove>) pairs[i];
            map.put(MoveSelector.move(key), (AccessMode) pairs[i + 1]);
        }
        return map;
    }

    private static PlatformerUnlocks owning(ResourceKey<?>... keys) {
        @SuppressWarnings("unchecked")
        var set = Set.of((ResourceKey<fr.hugman.mubble.platformer.move.PlatformerMove>[]) keys);
        return new PlatformerUnlocks(set);
    }

    @Test
    @DisplayName("no source, no layer")
    void noSourceMeansNoProfile() {
        var access = PlatformerAccess.resolve(List.of(), PlatformerUnlocks.NONE, NOW);
        assertFalse(access.isActive());
        assertFalse(access.allows(PlatformerMoves.JUMP));
    }

    @Test
    @DisplayName("deny beats an item forcing the same move")
    void denyBeatsForce() {
        var item = source("equipment/feet/boots", 0, Optional.of(PlatformerProfileIds.OVERWORLD), moves(PlatformerMoveIds.JUMP, AccessMode.FORCE));
        var ruleset = source("ruleset", 10, Optional.empty(), moves(PlatformerMoveIds.JUMP, AccessMode.DENY));
        var access = PlatformerAccess.resolve(List.of(item, ruleset), owning(PlatformerMoveIds.JUMP), NOW);
        assertFalse(access.allows(PlatformerMoves.JUMP), "a denied move should stay denied, forced and owned or not");
        assertEquals(AccessMode.DENY, access.mode(PlatformerMoves.JUMP));
    }

    @Test
    @DisplayName("force grants a move the player does not own")
    void forceGrantsUnownedMoves() {
        var access = PlatformerAccess.resolve(List.of(source("trial", 0, Optional.of(PlatformerProfileIds.TRIAL), moves(PlatformerMoveIds.BACKFLIP, AccessMode.FORCE))), PlatformerUnlocks.NONE, NOW);
        assertTrue(access.allows(PlatformerMoves.BACKFLIP));
    }

    @Test
    @DisplayName("enable only grants owned moves")
    void enableNeedsOwnership() {
        var boots = source("boots", 0, Optional.of(PlatformerProfileIds.OVERWORLD), moves(PlatformerMoveIds.JUMP, AccessMode.ENABLE, PlatformerMoveIds.BACKFLIP, AccessMode.ENABLE));
        var access = PlatformerAccess.resolve(List.of(boots), owning(PlatformerMoveIds.JUMP), NOW);
        assertTrue(access.allows(PlatformerMoves.JUMP));
        assertFalse(access.allows(PlatformerMoves.BACKFLIP), "an enabled move the player does not own should not be available");
    }

    @Test
    @DisplayName("a source silent about a move has no say in it")
    void silentSourcesHaveNoSay() {
        var access = PlatformerAccess.resolve(List.of(source("boots", 0, Optional.of(PlatformerProfileIds.OVERWORLD), moves(PlatformerMoveIds.JUMP, AccessMode.FORCE))), PlatformerUnlocks.NONE, NOW);
        assertFalse(access.allows(PlatformerMoves.DIVE));
        assertEquals(null, access.mode(PlatformerMoves.DIVE));
    }

    @Test
    @DisplayName("the parts of a move follow its access")
    void subStatesFollowTheirParent() {
        var forced = PlatformerAccess.resolve(List.of(source("a", 0, Optional.of(PlatformerProfileIds.TRIAL), moves(PlatformerMoveIds.LEDGE_GRAB, AccessMode.FORCE))), PlatformerUnlocks.NONE, NOW);
        assertTrue(forced.allows(PlatformerMoves.LEDGE_CLIMB));
        var denied = PlatformerAccess.resolve(List.of(
                source("a", 0, Optional.of(PlatformerProfileIds.TRIAL), moves(PlatformerMoveIds.DIVE, AccessMode.FORCE)),
                source("b", 0, Optional.empty(), moves(PlatformerMoveIds.DIVE, AccessMode.DENY))), PlatformerUnlocks.NONE, NOW);
        assertFalse(denied.allows(PlatformerMoves.ROLLOUT));
    }

    @Test
    @DisplayName("the states that are not moves are always available")
    void baseStatesAreAlwaysAvailable() {
        var access = PlatformerAccess.resolve(List.of(source("a", 0, Optional.of(PlatformerProfileIds.TRIAL), Map.of())), PlatformerUnlocks.NONE, NOW);
        assertTrue(access.allows(PlatformerMoves.FALL));
        assertTrue(access.allows(PlatformerMoves.WALK));
        assertTrue(access.allows(PlatformerMoves.LAND));
    }

    @Test
    @DisplayName("the profile of the source with the highest priority wins, ties going to the id sorting last")
    void highestPriorityProfileWins() {
        var low = source("a", 0, Optional.of(PlatformerProfileIds.OVERWORLD), Map.of());
        var high = source("b", 5, Optional.of(PlatformerProfileIds.TRIAL), Map.of());
        assertEquals(Optional.of(PlatformerProfileIds.TRIAL), PlatformerAccess.resolve(List.of(low, high), PlatformerUnlocks.NONE, NOW).profile());
        var tieFirst = source("a", 3, Optional.of(PlatformerProfileIds.OVERWORLD), Map.of());
        var tieLast = source("z", 3, Optional.of(PlatformerProfileIds.TRIAL), Map.of());
        assertEquals(Optional.of(PlatformerProfileIds.TRIAL), PlatformerAccess.resolve(List.of(tieLast, tieFirst), PlatformerUnlocks.NONE, NOW).profile());
        var noProfile = source("c", 100, Optional.empty(), Map.of());
        assertEquals(Optional.of(PlatformerProfileIds.TRIAL), PlatformerAccess.resolve(List.of(low, high, noProfile), PlatformerUnlocks.NONE, NOW).profile(),
                "a source without a profile should not take part in choosing one");
    }

    @Test
    @DisplayName("boots enabling moves the player owns none of leave the layer off")
    void enablingNothingOwnedIsInactive() {
        var boots = source("equipment/feet/boots", 0, Optional.of(PlatformerProfileIds.OVERWORLD), moves(PlatformerMoveIds.JUMP, AccessMode.ENABLE));
        assertFalse(PlatformerAccess.resolve(List.of(boots), PlatformerUnlocks.NONE, NOW).isActive(), "nothing owned, nothing to do");
        assertTrue(PlatformerAccess.resolve(List.of(boots), owning(PlatformerMoveIds.JUMP), NOW).isActive(), "one move owned is enough");
        var denied = source("ruleset", 10, Optional.empty(), moves(PlatformerMoveIds.JUMP, AccessMode.DENY));
        assertFalse(PlatformerAccess.resolve(List.of(boots, denied), owning(PlatformerMoveIds.JUMP), NOW).isActive(), "every move denied, nothing to do");
    }

    @Test
    @DisplayName("an expired source no longer counts")
    void expiredSourcesDoNotCount() {
        var expiring = new PlatformerSource(Mubble.id("timed"), Optional.of(PlatformerProfileIds.TRIAL), moves(PlatformerMoveIds.JUMP, AccessMode.FORCE), 0, Optional.of(NOW));
        assertFalse(PlatformerAccess.resolve(List.of(expiring), PlatformerUnlocks.NONE, NOW).isActive());
        assertTrue(PlatformerAccess.resolve(List.of(expiring), PlatformerUnlocks.NONE, NOW - 1).isActive());
    }
}
