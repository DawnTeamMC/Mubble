package fr.hugman.mubble.world.attribute;

import fr.hugman.mubble.Mubble;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.attribute.EnvironmentAttribute;

import java.util.List;

public interface MubbleEnvironmentAttributes {
    EnvironmentAttribute<List<BlockTransform>> FIREBALL_MELTS = register("gameplay/fireball_melts", EnvironmentAttribute.builder(MubbleEnvironmentAttributeTypes.BLOCK_TRANSFORMS).defaultValue(List.of()));
    EnvironmentAttribute<List<BlockTransform>> ICEBALL_FREEZES = register("gameplay/iceball_freezes", EnvironmentAttribute.builder(MubbleEnvironmentAttributeTypes.BLOCK_TRANSFORMS).defaultValue(List.of()));

    private static <Value> EnvironmentAttribute<Value> register(String path, EnvironmentAttribute.Builder<Value> builder) {
        var environmentAttribute = builder.build();
        Registry.register(Registries.ENVIRONMENTAL_ATTRIBUTE, Mubble.id(path), environmentAttribute);
        return environmentAttribute;
    }
}
