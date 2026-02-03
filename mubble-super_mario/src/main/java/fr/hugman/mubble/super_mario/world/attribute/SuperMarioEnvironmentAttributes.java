package fr.hugman.mubble.super_mario.world.attribute;

import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.world.attribute.BlockTransform;
import fr.hugman.mubble.world.attribute.MubbleAttributeTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.attribute.EnvironmentAttribute;

import java.util.List;

public interface SuperMarioEnvironmentAttributes {
    EnvironmentAttribute<List<BlockTransform>> FIREBALL_MELTS = register("gameplay/fireball_melts", EnvironmentAttribute.builder(MubbleAttributeTypes.BLOCK_TRANSFORMS).defaultValue(List.of()));
    EnvironmentAttribute<List<BlockTransform>> ICEBALL_FREEZES = register("gameplay/iceball_freezes", EnvironmentAttribute.builder(MubbleAttributeTypes.BLOCK_TRANSFORMS).defaultValue(List.of()));

    private static <Value> EnvironmentAttribute<Value> register(String path, EnvironmentAttribute.Builder<Value> builder) {
        var environmentAttribute = builder.build();
        Registry.register(BuiltInRegistries.ENVIRONMENT_ATTRIBUTE, SuperMario.id(path), environmentAttribute);
        return environmentAttribute;
    }
}
