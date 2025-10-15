package fr.hugman.mubble.attribute;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;

public record EntityAttributeEntry(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier) {
    public static final Codec<EntityAttributeEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    EntityAttribute.CODEC.fieldOf("type").forGetter(EntityAttributeEntry::attribute),
                    EntityAttributeModifier.MAP_CODEC.forGetter(EntityAttributeEntry::modifier)
            ).apply(instance, EntityAttributeEntry::new)
    );

    public static final PacketCodec<RegistryByteBuf, EntityAttributeEntry> PACKET_CODEC = PacketCodec.tuple(
            EntityAttribute.PACKET_CODEC,
            EntityAttributeEntry::attribute,
            EntityAttributeModifier.PACKET_CODEC,
            EntityAttributeEntry::modifier,
            EntityAttributeEntry::new
    );

    public static final PacketCodec<RegistryByteBuf, List<EntityAttributeEntry>> LIST_PACKET_CODEC = PACKET_CODEC.collect(PacketCodecs.toList());
    public static final PacketCodec<RegistryByteBuf, Optional<List<EntityAttributeEntry>>> OPTIONAL_LIST_PACKET_CODEC = LIST_PACKET_CODEC.collect(PacketCodecs::optional);

    public boolean matches(RegistryEntry<EntityAttribute> attribute, Identifier modifierId) {
        return attribute.equals(this.attribute) && this.modifier.idMatches(modifierId);
    }
}
