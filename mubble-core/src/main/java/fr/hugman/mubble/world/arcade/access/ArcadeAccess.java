package fr.hugman.mubble.world.arcade.access;

import fr.hugman.mubble.core.component.MubbleDataComponents;
import fr.hugman.mubble.world.arcade.ArcadeProfile;
import fr.hugman.mubble.world.arcade.ArcadeUnlocks;
import fr.hugman.mubble.world.arcade.move.ArcadeMove;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

/**
 * Resolves the sources of a player into their {@link ResolvedAccess}.
 * <p>
 * Per move, the strongest mode any source gives wins: {@code deny > force > enable}, {@code enable}
 * only counting for an owned move. The active profile is the one declared by the source with the
 * highest priority, ties going to the source whose id sorts last. The same function runs on both
 * sides, from the same synced data, so that both agree on what the player can do.
 */
public final class ArcadeAccess {
    private ArcadeAccess() {
    }

    /** Everything that counts for {@code player} right now: their stored sources and their equipment. */
    public static ResolvedAccess of(Player player) {
        var sources = new ArrayList<>(ArcadeSources.get(player).sources());
        sources.addAll(equipmentSources(player));
        return resolve(sources, ArcadeUnlocks.get(player), player.level().getGameTime());
    }

    public static ResolvedAccess resolve(List<ArcadeSource> sources, ArcadeUnlocks unlocks, long gameTime) {
        var counting = sources.stream().filter(source -> !source.isExpired(gameTime)).toList();
        if (counting.isEmpty()) {
            return new ResolvedAccess(Optional.empty(), Map.of(), unlocks, List.of());
        }

        Optional<ResourceKey<ArcadeProfile>> profile = counting.stream()
                .filter(source -> source.profile().isPresent())
                .max(Comparator.comparingInt(ArcadeSource::priority).thenComparing(source -> source.id().toString()))
                .flatMap(ArcadeSource::profile);

        Map<ArcadeMove, AccessMode> modes = new HashMap<>();
        for (var source : counting) {
            for (var entry : source.moves().entrySet()) {
                var mode = entry.getValue();
                entry.getKey().forEachMove(move -> modes.merge(move.accessRoot(), mode, AccessMode::strongest));
            }
        }
        return new ResolvedAccess(profile, Map.copyOf(modes), unlocks, List.copyOf(counting));
    }

    /**
     * The sources the equipment of {@code player} makes: one per item carrying the
     * {@code mubble:arcade_movement} component in a slot it accepts.
     */
    public static List<ArcadeSource> equipmentSources(Player player) {
        List<ArcadeSource> sources = null;
        for (EquipmentSlot slot : EquipmentSlot.VALUES) {
            var stack = player.getItemBySlot(slot);
            if (stack.isEmpty()) {
                continue;
            }
            var component = stack.get(MubbleDataComponents.ARCADE_MOVEMENT);
            if (component == null || !component.slots().test(slot)) {
                continue;
            }
            var itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
            var id = Identifier.fromNamespaceAndPath(itemId.getNamespace(), "equipment/" + slot.getName() + "/" + itemId.getPath());
            if (sources == null) {
                sources = new ArrayList<>();
            }
            sources.add(new ArcadeSource(id, component.profile(), component.moves(), 0, Optional.empty()));
        }
        return sources == null ? List.of() : sources;
    }
}
