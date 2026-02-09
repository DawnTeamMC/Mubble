package fr.hugman.mubble.world.entity.ai.attributes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record EntityAttributeEntry(Holder<Attribute> attribute, AttributeModifier modifier) {
    public static final Codec<EntityAttributeEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Attribute.CODEC.fieldOf("type").forGetter(EntityAttributeEntry::attribute),
                    AttributeModifier.MAP_CODEC.forGetter(EntityAttributeEntry::modifier)
            ).apply(instance, EntityAttributeEntry::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, EntityAttributeEntry> STREAM_CODEC = StreamCodec.composite(
            Attribute.STREAM_CODEC,
            EntityAttributeEntry::attribute,
            AttributeModifier.STREAM_CODEC,
            EntityAttributeEntry::modifier,
            EntityAttributeEntry::new
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, List<EntityAttributeEntry>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());
    public static final StreamCodec<RegistryFriendlyByteBuf, Optional<List<EntityAttributeEntry>>> OPTIONAL_LIST_STREAM_CODEC = LIST_STREAM_CODEC.apply(ByteBufCodecs::optional);

    public boolean matches(Holder<Attribute> attribute, Identifier modifierId) {
        return attribute.equals(this.attribute) && this.modifier.is(modifierId);
    }
}
