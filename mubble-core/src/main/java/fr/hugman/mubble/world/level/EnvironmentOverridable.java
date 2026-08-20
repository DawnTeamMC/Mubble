package fr.hugman.mubble.world.level;

import java.util.List;

import net.minecraft.world.attribute.EnvironmentAttributeMap;

/**
 * A level whose environment attributes can be overridden at runtime.
 *
 * <p>Injected onto {@link net.minecraft.world.level.Level} and implemented on both sides, so a
 * voyage trial can change how a level looks and behaves without owning a dimension type or a biome.
 *
 * <p>Vanilla resolves environment attributes through a layer stack baked once per level:
 *
 * <pre>
 * dimension type defaults
 *   &larr; biome effects
 *   &larr; timelines and weather
 *   &larr; environment profile      &lt;- these are ours
 *   &larr; per-instance override
 * </pre>
 *
 * <p>The layers passed here are appended in order after everything vanilla contributes, so later
 * layers win. Fall-through is per field, not per layer, because a layer only touches the attributes
 * it actually names.
 */
public interface EnvironmentOverridable {
    /**
     * Replaces the override layers on this level and rebakes its attribute stack.
     *
     * <p>Passing an empty list restores stock vanilla resolution. Cheap enough to call on trial
     * entry, trial exit and {@code /reload}; not something to call every tick.
     */
    default void setEnvironmentOverrides(List<EnvironmentAttributeMap> layers) {
    }

    /** {@return the override layers currently applied to this level, outermost last} */
    default List<EnvironmentAttributeMap> getEnvironmentOverrides() {
        return List.of();
    }
}
