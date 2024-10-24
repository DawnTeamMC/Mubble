package fr.hugman.mubble.component;

import fr.hugman.mubble.Mubble;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.UnaryOperator;

public class MubbleComponentTypes {
    public static final ComponentType<PowerUpComponent> POWER_UP = of("power_up", builder -> builder.codec(PowerUpComponent.CODEC).packetCodec(PowerUpComponent.PACKET_CODEC).cache());

    private static <T> ComponentType<T> of(String path, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Mubble.id(path), builderOperator.apply(ComponentType.builder()).build());
    }
}
