package fr.hugman.mubble.component;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.item.weapon.SplatoonWeapon;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.function.UnaryOperator;

public class MubbleDataComponentsTypes {
    public static final ComponentType<RegistryEntry<SplatoonWeapon>> SPLATOON_WEAPON = of("splatoon_weapon", builder ->
            builder.codec(SplatoonWeapon.ENTRY_CODEC).packetCodec(SplatoonWeapon.ENTRY_PACKET_CODEC).cache());

    private static <T> ComponentType<T> of(String path, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Mubble.id(path), ((ComponentType.Builder) builderOperator.apply(ComponentType.builder())).build());
    }
}
