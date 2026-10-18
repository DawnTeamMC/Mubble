package fr.hugman.mubble.zelda.core.component;

import fr.hugman.mubble.zelda.Zelda;
import fr.hugman.mubble.zelda.world.entity.animal.loftwing.LoftwingVariant;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.UnaryOperator;

public class ZeldaDataComponents {
    public static final DataComponentType<Holder<LoftwingVariant>> LOFTWING_VARIANT = register(
            "loftwing/variant", builder -> builder.persistent(LoftwingVariant.CODEC).networkSynchronized(LoftwingVariant.STREAM_CODEC)
    );

    private static <T> DataComponentType<T> register(String path, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Zelda.id(path), builderOperator.apply(DataComponentType.builder()).build());
    }
}
