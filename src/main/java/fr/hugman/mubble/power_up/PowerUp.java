package fr.hugman.mubble.power_up;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.attribute.EntityAttributeEntry;
import fr.hugman.mubble.power_up.action.PowerUpAction;
import fr.hugman.mubble.registry.MubbleRegistryKeys;
import fr.hugman.mubble.sound.MubbleSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryPair;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public record PowerUp(
        Optional<Text> name,
        Optional<RegistryEntry<PowerUpAction>> action,
        Optional<List<EntityAttributeEntry>> attributesModifiers,
        RegistryEntry<SoundEvent> obtainSound,
        RegistryEntry<SoundEvent> looseSound
) {
    //TODO: add icon texture path
    //TODO: add a predicate to determine if you can lose it on damage

    public static final Codec<PowerUp> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TextCodecs.CODEC.optionalFieldOf("name").forGetter(PowerUp::name),
            PowerUpAction.ENTRY_CODEC.optionalFieldOf("action").forGetter(PowerUp::action),
            EntityAttributeEntry.CODEC.listOf().optionalFieldOf("attribute_modifiers").forGetter(PowerUp::attributesModifiers),
            SoundEvent.ENTRY_CODEC.optionalFieldOf("obtain_sound", RegistryEntry.of(MubbleSounds.POWER_UP_OBTAIN)).forGetter(PowerUp::obtainSound),
            SoundEvent.ENTRY_CODEC.optionalFieldOf("loose_sound", RegistryEntry.of(MubbleSounds.POWER_UP_LOOSE)).forGetter(PowerUp::looseSound)
    ).apply(instance, PowerUp::new));

    public static final Codec<RegistryEntry<PowerUp>> ENTRY_CODEC = RegistryElementCodec.of(MubbleRegistryKeys.POWER_UP, CODEC);
    public static final Codec<RegistryPair<PowerUp>> PAIR_CODEC = RegistryPair.createCodec(MubbleRegistryKeys.POWER_UP, ENTRY_CODEC);

    public static final PacketCodec<RegistryByteBuf, PowerUp> PACKET_CODEC = PacketCodec.tuple(
            TextCodecs.OPTIONAL_UNLIMITED_REGISTRY_PACKET_CODEC, PowerUp::name,
            PowerUpAction.OPTIONAL_ENTRY_PACKET_CODEC, PowerUp::action,
            EntityAttributeEntry.OPTIONAL_LIST_PACKET_CODEC, PowerUp::attributesModifiers,
            SoundEvent.ENTRY_PACKET_CODEC, PowerUp::obtainSound,
            SoundEvent.ENTRY_PACKET_CODEC, PowerUp::looseSound,
            PowerUp::new
    );
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<PowerUp>> ENTRY_PACKET_CODEC = PacketCodecs.registryEntry(MubbleRegistryKeys.POWER_UP, PACKET_CODEC);
    public static final PacketCodec<RegistryByteBuf, Optional<RegistryEntry<PowerUp>>> OPTIONAL_ENTRY_PACKET_CODEC = PacketCodecs.optional(ENTRY_PACKET_CODEC);
    public static final PacketCodec<RegistryByteBuf, RegistryPair<PowerUp>> PAIR_PACKET_CODEC = RegistryPair.createPacketCodec(MubbleRegistryKeys.POWER_UP, ENTRY_PACKET_CODEC);

    public void trigger(MinecraftServer server, ServerPlayerEntity player) {
        this.action.ifPresent(entry -> entry.value().onTrigger(server, player));
    }

    public void applyModifiers(BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> attributeConsumer) {
        this.attributesModifiers.ifPresent(entries -> entries.forEach(entry -> attributeConsumer.accept(entry.attribute(), entry.modifier())));
    }

    public static void onChange(LivingEntity entity, Optional<RegistryEntry<PowerUp>> previous, Optional<RegistryEntry<PowerUp>> next) {
        var container = entity.getAttributes();
        previous.ifPresent(e -> e.value().applyModifiers((attribute, modifier) -> {
            EntityAttributeInstance entityAttributeInstance = container.getCustomInstance(attribute);
            if (entityAttributeInstance != null) {
                entityAttributeInstance.removeModifier(modifier);
            }
        }));
        next.ifPresent(e -> e.value().applyModifiers((attribute, modifier) -> {
            EntityAttributeInstance entityAttributeInstance = container.getCustomInstance(attribute);
            if (entityAttributeInstance != null) {
                entityAttributeInstance.removeModifier(modifier);
                entityAttributeInstance.addPersistentModifier(modifier);
            }
        }));

        if (previous.isPresent() && next.isEmpty()) {
            entity.playSound(previous.get().value().looseSound.value(), 1.0F, 1.0F);
        } else
            next.ifPresent(powerUpRegistryEntry -> entity.playSound(powerUpRegistryEntry.value().obtainSound.value(), 1.0F, 1.0F));

        //TODO: particles
    }
}
