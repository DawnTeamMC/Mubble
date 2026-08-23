package fr.hugman.mubble.world.voyage.session;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.Mubble;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.minecraft.core.UUIDUtil;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.jspecify.annotations.Nullable;

/**
 * The stashes of everyone currently in a voyage, on disk.
 *
 * <p>A running session lives in memory and is not saved: the trial levels are temporary and are
 * gone after a restart, so there is nothing to resume. What must survive is the {@link PlayerStash},
 * because the alternative is a player whose inventory is nowhere.
 *
 * <p>So the contract is deliberately lopsided. A restart <em>ends</em> a voyage in progress; it does
 * not continue one. Everyone gets their belongings and their position back the next time they log
 * in, which is what the acceptance criterion asks for.
 *
 * <p><strong>Not expected to survive a Minecraft version upgrade.</strong> The stash contains item
 * stacks and effects, whose stored form changes between versions, and no data fixer knows this
 * layout. Fixes only run when a world is opened on a newer version than it was saved with, so the
 * only exposure is upgrading with a voyage in progress. Finish or abandon voyages before upgrading;
 * for a POC that is a fair trade against inventing a fixer schema.
 */
public class VoyageSessionData extends SavedData {
    public static final Codec<VoyageSessionData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(UUIDUtil.STRING_CODEC, PlayerStash.CODEC).optionalFieldOf("stashes", Map.of())
                    .forGetter(data -> data.stashes)
    ).apply(instance, VoyageSessionData::new));

    public static final SavedDataType<VoyageSessionData> TYPE = new SavedDataType<>(
            Mubble.id("voyage_sessions"), VoyageSessionData::new, CODEC, DataFixTypes.PLAYER
    );

    private final Map<UUID, PlayerStash> stashes;

    public VoyageSessionData() {
        this(Map.of());
    }

    private VoyageSessionData(Map<UUID, PlayerStash> stashes) {
        this.stashes = new HashMap<>(stashes);
    }

    public void put(UUID playerId, PlayerStash stash) {
        this.stashes.put(playerId, stash);
        this.setDirty();
    }

    /** {@return the stash for {@code playerId}, removing it}, or {@code null} if there was none */
    public @Nullable PlayerStash take(UUID playerId) {
        PlayerStash stash = this.stashes.remove(playerId);
        if (stash != null) {
            this.setDirty();
        }
        return stash;
    }

    /** {@return the stash for {@code playerId}, leaving it in place}, or {@code null} if there is none */
    public @Nullable PlayerStash peek(UUID playerId) {
        return this.stashes.get(playerId);
    }

    public boolean has(UUID playerId) {
        return this.stashes.containsKey(playerId);
    }

    /** {@return everyone with something still stashed} — the recovery list after a restart. */
    public Set<UUID> stashedPlayers() {
        return Set.copyOf(this.stashes.keySet());
    }
}
