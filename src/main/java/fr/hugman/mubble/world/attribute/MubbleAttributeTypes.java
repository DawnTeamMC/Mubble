package fr.hugman.mubble.world.attribute;

import fr.hugman.mubble.Mubble;
import java.util.List;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.attribute.AttributeType;

public class MubbleAttributeTypes {
    public static final AttributeType<List<BlockTransform>> BLOCK_TRANSFORMS = register("block_transforms", AttributeType.ofNotInterpolated(ExtraCodecs.compactListCodec(BlockTransform.CODEC)));

    private static <Value> AttributeType<Value> register(String path, AttributeType<Value> type) {
        Registry.register(BuiltInRegistries.ATTRIBUTE_TYPE, Mubble.id(path), type);
        return type;
    }
}
