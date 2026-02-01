package fr.hugman.mubble.super_mario.core.component;

import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.world.entity.monster.goomba.GoombaVariant;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.UnaryOperator;

public class SuperMarioDataComponents {
    public static final DataComponentType<Holder<GoombaVariant>> GOOMBA_VARIANT = register(
            "goomba/variant", builder -> builder.persistent(GoombaVariant.ENTRY_CODEC).networkSynchronized(GoombaVariant.ENTRY_PACKET_CODEC)
    );

    private static <T> DataComponentType<T> register(String path, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, SuperMario.id(path), builderOperator.apply(DataComponentType.builder()).build());
    }
}
