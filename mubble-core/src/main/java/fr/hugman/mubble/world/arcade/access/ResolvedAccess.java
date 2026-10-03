package fr.hugman.mubble.world.arcade.access;

import fr.hugman.mubble.world.arcade.ArcadeProfile;
import fr.hugman.mubble.world.arcade.ArcadeUnlocks;
import fr.hugman.mubble.world.arcade.move.ArcadeMove;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;

/**
 * Where the sources of a player leave them: the active profile and what each move resolves to.
 *
 * @param profile the profile of the source with the highest priority declaring one, if any
 * @param modes   the strongest mode any source gave each move
 * @param unlocks the moves the player owns
 * @param sources every source that counted, equipment included, for inspection
 */
public record ResolvedAccess(
        Optional<ResourceKey<ArcadeProfile>> profile,
        Map<ArcadeMove, AccessMode> modes,
        ArcadeUnlocks unlocks,
        List<ArcadeSource> sources
) {
    public static final ResolvedAccess NONE = new ResolvedAccess(Optional.empty(), Map.of(), ArcadeUnlocks.NONE, List.of());

    /**
     * Whether the arcade movement layer is on at all: a profile is chosen and at least one move is
     * available. Boots that only enable moves do nothing for a player who owns none of them, rather
     * than take over the movement with nothing to do it with.
     */
    public boolean isActive() {
        return this.profile.isPresent() && this.modes.keySet().stream().anyMatch(this::allows);
    }

    @Nullable
    public AccessMode mode(ArcadeMove move) {
        return this.modes.get(move.accessRoot());
    }

    /**
     * Whether {@code move} is available: {@code deny} beats {@code force}, which beats {@code enable},
     * which only counts for an owned move. A move no source speaks about is not available. The states
     * that are not moves of their own always are.
     */
    public boolean allows(ArcadeMove move) {
        if (move.isBase()) {
            return true;
        }
        var mode = this.mode(move);
        if (mode == null) {
            return false;
        }
        return switch (mode) {
            case DENY -> false;
            case FORCE -> true;
            case ENABLE -> this.unlocks.owns(move);
        };
    }
}
