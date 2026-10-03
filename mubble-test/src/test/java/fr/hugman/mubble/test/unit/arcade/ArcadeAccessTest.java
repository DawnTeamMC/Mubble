package fr.hugman.mubble.test.unit.arcade;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.references.ArcadeMoveIds;
import fr.hugman.mubble.references.ArcadeProfileIds;
import fr.hugman.mubble.test.unit.support.Registrations;
import fr.hugman.mubble.world.arcade.ArcadeProfile;
import fr.hugman.mubble.world.arcade.ArcadeUnlocks;
import fr.hugman.mubble.world.arcade.access.AccessMode;
import fr.hugman.mubble.world.arcade.access.ArcadeAccess;
import fr.hugman.mubble.world.arcade.access.ArcadeSource;
import fr.hugman.mubble.world.arcade.access.MoveSelector;
import fr.hugman.mubble.world.arcade.move.ArcadeMoves;
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
 * is covered by {@code ArcadeAccessGameTest}.
 */
public class ArcadeAccessTest {
    private static final long NOW = 1000L;

    @BeforeAll
    static void registerContent() {
        Registrations.registerEverything();
    }

    private static ArcadeSource source(String id, int priority, Optional<ResourceKey<ArcadeProfile>> profile, Map<MoveSelector, AccessMode> moves) {
        return new ArcadeSource(Mubble.id(id), profile, moves, priority, Optional.empty());
    }

    private static Map<MoveSelector, AccessMode> moves(Object... pairs) {
        var map = new java.util.LinkedHashMap<MoveSelector, AccessMode>();
        for (int i = 0; i < pairs.length; i += 2) {
            @SuppressWarnings("unchecked")
            var key = (ResourceKey<fr.hugman.mubble.world.arcade.move.ArcadeMove>) pairs[i];
            map.put(MoveSelector.move(key), (AccessMode) pairs[i + 1]);
        }
        return map;
    }

    private static ArcadeUnlocks owning(ResourceKey<?>... keys) {
        @SuppressWarnings("unchecked")
        var set = Set.of((ResourceKey<fr.hugman.mubble.world.arcade.move.ArcadeMove>[]) keys);
        return new ArcadeUnlocks(set);
    }

    @Test
    @DisplayName("no source, no layer")
    void noSourceMeansNoProfile() {
        var access = ArcadeAccess.resolve(List.of(), ArcadeUnlocks.NONE, NOW);
        assertFalse(access.isActive());
        assertFalse(access.allows(ArcadeMoves.JUMP));
    }

    @Test
    @DisplayName("deny beats an item forcing the same move")
    void denyBeatsForce() {
        var item = source("equipment/feet/boots", 0, Optional.of(ArcadeProfileIds.OVERWORLD), moves(ArcadeMoveIds.JUMP, AccessMode.FORCE));
        var ruleset = source("ruleset", 10, Optional.empty(), moves(ArcadeMoveIds.JUMP, AccessMode.DENY));
        var access = ArcadeAccess.resolve(List.of(item, ruleset), owning(ArcadeMoveIds.JUMP), NOW);
        assertFalse(access.allows(ArcadeMoves.JUMP), "a denied move should stay denied, forced and owned or not");
        assertEquals(AccessMode.DENY, access.mode(ArcadeMoves.JUMP));
    }

    @Test
    @DisplayName("force grants a move the player does not own")
    void forceGrantsUnownedMoves() {
        var access = ArcadeAccess.resolve(List.of(source("trial", 0, Optional.of(ArcadeProfileIds.TRIAL), moves(ArcadeMoveIds.BACKFLIP, AccessMode.FORCE))), ArcadeUnlocks.NONE, NOW);
        assertTrue(access.allows(ArcadeMoves.BACKFLIP));
    }

    @Test
    @DisplayName("enable only grants owned moves")
    void enableNeedsOwnership() {
        var boots = source("boots", 0, Optional.of(ArcadeProfileIds.OVERWORLD), moves(ArcadeMoveIds.JUMP, AccessMode.ENABLE, ArcadeMoveIds.BACKFLIP, AccessMode.ENABLE));
        var access = ArcadeAccess.resolve(List.of(boots), owning(ArcadeMoveIds.JUMP), NOW);
        assertTrue(access.allows(ArcadeMoves.JUMP));
        assertFalse(access.allows(ArcadeMoves.BACKFLIP), "an enabled move the player does not own should not be available");
    }

    @Test
    @DisplayName("a source silent about a move has no say in it")
    void silentSourcesHaveNoSay() {
        var access = ArcadeAccess.resolve(List.of(source("boots", 0, Optional.of(ArcadeProfileIds.OVERWORLD), moves(ArcadeMoveIds.JUMP, AccessMode.FORCE))), ArcadeUnlocks.NONE, NOW);
        assertFalse(access.allows(ArcadeMoves.DIVE));
        assertEquals(null, access.mode(ArcadeMoves.DIVE));
    }

    @Test
    @DisplayName("the parts of a move follow its access")
    void subStatesFollowTheirParent() {
        var forced = ArcadeAccess.resolve(List.of(source("a", 0, Optional.of(ArcadeProfileIds.TRIAL), moves(ArcadeMoveIds.LEDGE_GRAB, AccessMode.FORCE))), ArcadeUnlocks.NONE, NOW);
        assertTrue(forced.allows(ArcadeMoves.LEDGE_CLIMB));
        var denied = ArcadeAccess.resolve(List.of(
                source("a", 0, Optional.of(ArcadeProfileIds.TRIAL), moves(ArcadeMoveIds.DIVE, AccessMode.FORCE)),
                source("b", 0, Optional.empty(), moves(ArcadeMoveIds.DIVE, AccessMode.DENY))), ArcadeUnlocks.NONE, NOW);
        assertFalse(denied.allows(ArcadeMoves.ROLLOUT));
    }

    @Test
    @DisplayName("the states that are not moves are always available")
    void baseStatesAreAlwaysAvailable() {
        var access = ArcadeAccess.resolve(List.of(source("a", 0, Optional.of(ArcadeProfileIds.TRIAL), Map.of())), ArcadeUnlocks.NONE, NOW);
        assertTrue(access.allows(ArcadeMoves.FALL));
        assertTrue(access.allows(ArcadeMoves.WALK));
        assertTrue(access.allows(ArcadeMoves.LAND));
    }

    @Test
    @DisplayName("the profile of the source with the highest priority wins, ties going to the id sorting last")
    void highestPriorityProfileWins() {
        var low = source("a", 0, Optional.of(ArcadeProfileIds.OVERWORLD), Map.of());
        var high = source("b", 5, Optional.of(ArcadeProfileIds.TRIAL), Map.of());
        assertEquals(Optional.of(ArcadeProfileIds.TRIAL), ArcadeAccess.resolve(List.of(low, high), ArcadeUnlocks.NONE, NOW).profile());
        var tieFirst = source("a", 3, Optional.of(ArcadeProfileIds.OVERWORLD), Map.of());
        var tieLast = source("z", 3, Optional.of(ArcadeProfileIds.TRIAL), Map.of());
        assertEquals(Optional.of(ArcadeProfileIds.TRIAL), ArcadeAccess.resolve(List.of(tieLast, tieFirst), ArcadeUnlocks.NONE, NOW).profile());
        var noProfile = source("c", 100, Optional.empty(), Map.of());
        assertEquals(Optional.of(ArcadeProfileIds.TRIAL), ArcadeAccess.resolve(List.of(low, high, noProfile), ArcadeUnlocks.NONE, NOW).profile(),
                "a source without a profile should not take part in choosing one");
    }

    @Test
    @DisplayName("boots enabling moves the player owns none of leave the layer off")
    void enablingNothingOwnedIsInactive() {
        var boots = source("equipment/feet/boots", 0, Optional.of(ArcadeProfileIds.OVERWORLD), moves(ArcadeMoveIds.JUMP, AccessMode.ENABLE));
        assertFalse(ArcadeAccess.resolve(List.of(boots), ArcadeUnlocks.NONE, NOW).isActive(), "nothing owned, nothing to do");
        assertTrue(ArcadeAccess.resolve(List.of(boots), owning(ArcadeMoveIds.JUMP), NOW).isActive(), "one move owned is enough");
        var denied = source("ruleset", 10, Optional.empty(), moves(ArcadeMoveIds.JUMP, AccessMode.DENY));
        assertFalse(ArcadeAccess.resolve(List.of(boots, denied), owning(ArcadeMoveIds.JUMP), NOW).isActive(), "every move denied, nothing to do");
    }

    @Test
    @DisplayName("an expired source no longer counts")
    void expiredSourcesDoNotCount() {
        var expiring = new ArcadeSource(Mubble.id("timed"), Optional.of(ArcadeProfileIds.TRIAL), moves(ArcadeMoveIds.JUMP, AccessMode.FORCE), 0, Optional.of(NOW));
        assertFalse(ArcadeAccess.resolve(List.of(expiring), ArcadeUnlocks.NONE, NOW).isActive());
        assertTrue(ArcadeAccess.resolve(List.of(expiring), ArcadeUnlocks.NONE, NOW - 1).isActive());
    }
}
