package fr.hugman.mubble.world.attribute;

import fr.hugman.mubble.Mubble;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.attribute.EnvironmentAttributeType;

import java.util.List;
import java.util.Optional;

public class MubbleEnvironmentAttributeTypes {
    public static final EnvironmentAttributeType<List<BlockTransform>> BLOCK_TRANSFORMS = register("block_transforms", EnvironmentAttributeType.discrete(Codecs.listOrSingle(BlockTransform.CODEC)));

    private static <Value> EnvironmentAttributeType<Value> register(String path, EnvironmentAttributeType<Value> type) {
        Registry.register(Registries.ATTRIBUTE_TYPE, Mubble.id(path), type);
        return type;
    }
}
