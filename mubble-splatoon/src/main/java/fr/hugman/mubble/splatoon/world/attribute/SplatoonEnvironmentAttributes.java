package fr.hugman.mubble.splatoon.world.attribute;

import fr.hugman.mubble.splatoon.Splatoon;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.attribute.AttributeTypes;
import net.minecraft.world.attribute.EnvironmentAttribute;

/**
 * Environment attributes of the Splatoon module. Like any environment attribute, they can be set per dimension type or
 * per biome by data packs, so an arena biome can keep its ink forever while the overworld lets it dry off.
 */
public interface SplatoonEnvironmentAttributes {
    int TICKS_PER_DAY = 24000;

    /**
     * How long ink stays on a surface after it was last painted, in ticks. Zero or less keeps it forever.
     * Defaults to four in-game days.
     */
    EnvironmentAttribute<Integer> INK_LIFETIME = register("gameplay/ink_lifetime", EnvironmentAttribute.builder(AttributeTypes.INTEGER).defaultValue(4 * TICKS_PER_DAY));
    /**
     * Whether rain washes away the ink on the surfaces it falls on.
     */
    EnvironmentAttribute<Boolean> RAIN_WASHES_INK = register("gameplay/rain_washes_ink", EnvironmentAttribute.builder(AttributeTypes.BOOLEAN).defaultValue(true));
    /**
     * Whether liquids wash away the ink they touch, and keep ink from being painted under them.
     */
    EnvironmentAttribute<Boolean> LIQUIDS_WASH_INK = register("gameplay/liquids_wash_ink", EnvironmentAttribute.builder(AttributeTypes.BOOLEAN).defaultValue(true));

    private static <Value> EnvironmentAttribute<Value> register(String path, EnvironmentAttribute.Builder<Value> builder) {
        var environmentAttribute = builder.build();
        Registry.register(BuiltInRegistries.ENVIRONMENT_ATTRIBUTE, Splatoon.id(path), environmentAttribute);
        return environmentAttribute;
    }
}
