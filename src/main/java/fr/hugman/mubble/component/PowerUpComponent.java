package fr.hugman.mubble.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.attribute.EntityAttributeEntry;
import fr.hugman.mubble.power_up.PowerUp;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryPair;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.function.Consumer;

public record PowerUpComponent(RegistryPair<PowerUp> powerUp, boolean showInTooltip) {
    private static final Codec<PowerUpComponent> BASE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PowerUp.PAIR_CODEC.fieldOf("power_up").forGetter(PowerUpComponent::powerUp),
            Codec.BOOL.optionalFieldOf("show_in_tooltip", true).forGetter(PowerUpComponent::showInTooltip)
    ).apply(instance, PowerUpComponent::new));
    public static final Codec<PowerUpComponent> CODEC = Codec.withAlternative(
            BASE_CODEC, PowerUp.PAIR_CODEC, attributeModifiers -> new PowerUpComponent(attributeModifiers, true)
    );
    public static final PacketCodec<RegistryByteBuf, PowerUpComponent> PACKET_CODEC = PacketCodec.tuple(
            PowerUp.PAIR_PACKET_CODEC, PowerUpComponent::powerUp,
            PacketCodecs.BOOL, PowerUpComponent::showInTooltip,
            PowerUpComponent::new
    );

    public PowerUpComponent(RegistryPair<PowerUp> powerUp) {
        this(powerUp, true);
    }

    public void buildTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, float durationMultiplier, float tickRate) {
        var registryLookup = context.getRegistryLookup();
        if (registryLookup != null) {
            this.powerUp.getEntry(registryLookup).flatMap(power -> power.value().attributesModifiers()).ifPresent(entityAttributeEntries -> buildTooltip(entityAttributeEntries, textConsumer, durationMultiplier, tickRate));
        } else {
            this.powerUp.entry().flatMap(power -> power.value().attributesModifiers()).ifPresent(entityAttributeEntries -> buildTooltip(entityAttributeEntries, textConsumer, durationMultiplier, tickRate));

        }
    }

    public static void buildTooltip(List<EntityAttributeEntry> attributes, Consumer<Text> textConsumer, float durationMultiplier, float tickRate) {
        if (!attributes.isEmpty()) {
            textConsumer.accept(ScreenTexts.EMPTY);
            textConsumer.accept(Text.translatable("potion.whenDrank").formatted(Formatting.DARK_PURPLE));

            for (EntityAttributeEntry entry : attributes) {
                EntityAttributeModifier entityAttributeModifier = entry.modifier();
                double d = entityAttributeModifier.value();
                double e;
                if (entityAttributeModifier.operation() != EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                        && entityAttributeModifier.operation() != EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                    e = entityAttributeModifier.value();
                } else {
                    e = entityAttributeModifier.value() * 100.0;
                }

                if (d > 0.0) {
                    textConsumer.accept(
                            Text.translatable(
                                            "attribute.modifier.plus." + entityAttributeModifier.operation().getId(),
                                            AttributeModifiersComponent.DECIMAL_FORMAT.format(e),
                                            Text.translatable(entry.attribute().value().getTranslationKey())
                                    )
                                    .formatted(Formatting.BLUE)
                    );
                } else if (d < 0.0) {
                    e *= -1.0;
                    textConsumer.accept(
                            Text.translatable(
                                            "attribute.modifier.take." + entityAttributeModifier.operation().getId(),
                                            AttributeModifiersComponent.DECIMAL_FORMAT.format(e),
                                            Text.translatable(entry.attribute().value().getTranslationKey())
                                    )
                                    .formatted(Formatting.RED)
                    );
                }
            }
        }
    }
}
