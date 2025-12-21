package fr.hugman.mubble.power_up;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.world.entity.ai.attributes.EntityAttributeEntry;
import fr.hugman.mubble.power_up.action.PowerUpAction;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.sounds.MubbleSounds;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EitherHolder;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public record PowerUp(
        Optional<Component> name,
        Optional<Identifier> spriteId,
        Optional<Holder<PowerUpAction>> action,
        Optional<List<EntityAttributeEntry>> attributesModifiers,
        Holder<SoundEvent> obtainSound,
        Holder<SoundEvent> looseSound,
        boolean canSprintOnWater
) {
    //TODO: add a predicate/damage tag to determine if you can lose it to damage
    //TODO: add custom music

    public static final Holder<SoundEvent> DEFAULT_OBTAIN_SOUND = MubbleSounds.POWER_UP_OBTAIN;
    public static final Holder<SoundEvent> DEFAULT_LOOSE_SOUND = MubbleSounds.POWER_UP_LOOSE;

    public static final Codec<PowerUp> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ComponentSerialization.CODEC.optionalFieldOf("name").forGetter(PowerUp::name),
            Identifier.CODEC.optionalFieldOf("sprite_id").forGetter(PowerUp::spriteId),
            PowerUpAction.ENTRY_CODEC.optionalFieldOf("action").forGetter(PowerUp::action),
            EntityAttributeEntry.CODEC.listOf().optionalFieldOf("attribute_modifiers").forGetter(PowerUp::attributesModifiers),
            SoundEvent.CODEC.optionalFieldOf("obtain_sound", DEFAULT_OBTAIN_SOUND).forGetter(PowerUp::obtainSound),
            SoundEvent.CODEC.optionalFieldOf("loose_sound", DEFAULT_LOOSE_SOUND).forGetter(PowerUp::looseSound),
            Codec.BOOL.optionalFieldOf("can_sprint_on_water", false).forGetter(PowerUp::canSprintOnWater)
    ).apply(instance, PowerUp::new));

    public static final Codec<Holder<PowerUp>> ENTRY_CODEC = RegistryFileCodec.create(MubbleRegistries.POWER_UP, CODEC);
    public static final Codec<EitherHolder<PowerUp>> LAZY_ENTRY_CODEC = EitherHolder.codec(MubbleRegistries.POWER_UP, ENTRY_CODEC);

    public static final StreamCodec<RegistryFriendlyByteBuf, PowerUp> PACKET_CODEC = StreamCodec.composite(
            ComponentSerialization.TRUSTED_OPTIONAL_STREAM_CODEC, PowerUp::name,
            Identifier.STREAM_CODEC.apply(ByteBufCodecs::optional), PowerUp::spriteId,
            PowerUpAction.OPTIONAL_ENTRY_PACKET_CODEC, PowerUp::action,
            EntityAttributeEntry.OPTIONAL_LIST_PACKET_CODEC, PowerUp::attributesModifiers,
            SoundEvent.STREAM_CODEC, PowerUp::obtainSound,
            SoundEvent.STREAM_CODEC, PowerUp::looseSound,
            ByteBufCodecs.BOOL, PowerUp::canSprintOnWater,
            PowerUp::new
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<PowerUp>> ENTRY_PACKET_CODEC = ByteBufCodecs.holder(MubbleRegistries.POWER_UP, PACKET_CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, Optional<Holder<PowerUp>>> OPTIONAL_ENTRY_PACKET_CODEC = ByteBufCodecs.optional(ENTRY_PACKET_CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, EitherHolder<PowerUp>> LAZY_ENTRY_PACKET_CODEC = EitherHolder.streamCodec(MubbleRegistries.POWER_UP, ENTRY_PACKET_CODEC);

    public InteractionResult trigger(Player player) {
        return this.action.map(entry -> entry.value().trigger(player)).orElse(InteractionResult.PASS);
    }

    public void applyModifiers(BiConsumer<Holder<Attribute>, AttributeModifier> attributeConsumer) {
        this.attributesModifiers.ifPresent(entries -> entries.forEach(entry -> attributeConsumer.accept(entry.attribute(), entry.modifier())));
    }

    /**
     * @return whether the power-up can be triggered with the "power-up trigger" key.
     */
    public boolean canBeTriggered() {
        return this.action.isPresent();
    }

    public static void onChange(LivingEntity entity, Optional<Holder<PowerUp>> previous, Optional<Holder<PowerUp>> next) {
        if(!entity.level().isClientSide()) {
            var container = entity.getAttributes();
            previous.ifPresent(e -> e.value().applyModifiers((attribute, modifier) -> {
                AttributeInstance entityAttributeInstance = container.getInstance(attribute);
                if (entityAttributeInstance != null) {
                    entityAttributeInstance.removeModifier(modifier);
                }
            }));
            next.ifPresent(e -> e.value().applyModifiers((attribute, modifier) -> {
                AttributeInstance entityAttributeInstance = container.getInstance(attribute);
                if (entityAttributeInstance != null) {
                    entityAttributeInstance.removeModifier(modifier);
                    entityAttributeInstance.addPermanentModifier(modifier);
                }
            }));
        }

        if (previous.isPresent() && next.isEmpty()) {
            entity.playSound(previous.get().value().looseSound.value(), 1.0F, 1.0F);
        } else
            next.ifPresent(powerUpRegistryEntry -> {
                entity.playSound(powerUpRegistryEntry.value().obtainSound.value(), 1.0F, 1.0F);
                if (entity instanceof PowerUpHolder powerUpHolder) {
                    powerUpHolder.getPowerUpProperties().reset();
                }
            });

        //TODO: create event?
        //TODO: particles
    }

    public static boolean canChange(LivingEntity entity, Holder<PowerUp> entry) {
        if (entity instanceof Player player) {
            return player.getPowerUp().map(power -> !power.is(entry)).orElse(true);
        }
        return false;
    }

    public static Optional<Identifier> getSpriteId(Holder<PowerUp> entry) {
        return entry.value().spriteId().or(() -> entry.unwrapKey().flatMap(key -> Optional.of(key.identifier().withPrefix("power_up/"))));
    }
}
