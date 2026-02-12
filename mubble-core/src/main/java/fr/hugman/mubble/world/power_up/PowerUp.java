package fr.hugman.mubble.world.power_up;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.entity.ai.attributes.EntityAttributeEntry;
import fr.hugman.mubble.world.power_up.action.PowerUpAction;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public record PowerUp(
        Optional<Component> name,
        Optional<Identifier> spriteId,
        Optional<Holder<PowerUpAction>> action,
        Optional<List<EntityAttributeEntry>> attributesModifiers,
        PowerUpCosmectics cosmectics,
        boolean canSprintOnWater
) {
    //TODO: add a predicate/damage tag to determine if you can lose it to damage
    //TODO: add custom music

    public static final Codec<PowerUp> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ComponentSerialization.CODEC.optionalFieldOf("name").forGetter(PowerUp::name),
            Identifier.CODEC.optionalFieldOf("sprite_id").forGetter(PowerUp::spriteId),
            PowerUpAction.CODEC.optionalFieldOf("action").forGetter(PowerUp::action),
            EntityAttributeEntry.CODEC.listOf().optionalFieldOf("attribute_modifiers").forGetter(PowerUp::attributesModifiers),
            PowerUpCosmectics.CODEC.optionalFieldOf("cosmetics", PowerUpCosmectics.EMPTY).forGetter(PowerUp::cosmectics),
            Codec.BOOL.optionalFieldOf("can_sprint_on_water", false).forGetter(PowerUp::canSprintOnWater)
    ).apply(instance, PowerUp::new));

    public static final Codec<Holder<PowerUp>> CODEC = RegistryFileCodec.create(MubbleRegistries.POWER_UP, DIRECT_CODEC);

    public static final StreamCodec<RegistryFriendlyByteBuf, PowerUp> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ComponentSerialization.TRUSTED_OPTIONAL_STREAM_CODEC, PowerUp::name,
            Identifier.STREAM_CODEC.apply(ByteBufCodecs::optional), PowerUp::spriteId,
            PowerUpAction.OPTIONAL_STREAM_CODEC, PowerUp::action,
            EntityAttributeEntry.OPTIONAL_LIST_STREAM_CODEC, PowerUp::attributesModifiers,
            PowerUpCosmectics.STREAM_CODEC, PowerUp::cosmectics,
            ByteBufCodecs.BOOL, PowerUp::canSprintOnWater,
            PowerUp::new
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<PowerUp>> STREAM_CODEC = ByteBufCodecs.holder(MubbleRegistries.POWER_UP, DIRECT_STREAM_CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, Optional<Holder<PowerUp>>> OPTIONAL_STREAM_CODEC = ByteBufCodecs.optional(STREAM_CODEC);

    public InteractionResult trigger(Player player) {
        if(this.action.isEmpty()) {
            return InteractionResult.PASS;
        }
        var result = this.action.get().value().trigger(player);
        if(result == InteractionResult.SUCCESS && this.action.get().value().shouldSwingOtherHand()) {
            // swing the empty hand or main hand if both are occupied
            player.swing(!player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && player.getItemInHand(InteractionHand.OFF_HAND).isEmpty() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        }
        return result;
    }

    /**
     * @return true if the power-up will swing the other hand when used.
     */
    public boolean shouldDisplayOtherHand() {
        return this.canBeTriggered() && this.action.map(action -> action.value().shouldSwingOtherHand()).orElse(false);
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
        if (!entity.level().isClientSide()) {
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
            previous.get().value().cosmectics().looseSound().ifPresent(sound -> entity.playSound(sound.value(), 1.0F, 1.0F));
        } else {
            next.ifPresent(powerUpRegistryEntry -> {
                powerUpRegistryEntry.value().cosmectics().obtainSound().ifPresent(sound -> entity.playSound(sound.value(), 1.0F, 1.0F));
                if (entity instanceof PowerUpHolder powerUpHolder) {
                    powerUpHolder.getPowerUpProperties().reset();
                }
            });
        }
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
