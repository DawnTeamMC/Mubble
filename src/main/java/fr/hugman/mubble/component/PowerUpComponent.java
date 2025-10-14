package fr.hugman.mubble.component;

import com.mojang.serialization.Codec;
import fr.hugman.mubble.attribute.EntityAttributeEntry;
import fr.hugman.mubble.power_up.PowerUp;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.entry.LazyRegistryEntryReference;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.function.Consumer;

public record PowerUpComponent(LazyRegistryEntryReference<PowerUp> powerUp) implements TooltipAppender {
	public static final Codec<PowerUpComponent> CODEC = PowerUp.LAZY_ENTRY_CODEC.xmap(PowerUpComponent::new, PowerUpComponent::powerUp);
    public static final PacketCodec<RegistryByteBuf, PowerUpComponent> PACKET_CODEC = PowerUp.LAZY_ENTRY_PACKET_CODEC.xmap(PowerUpComponent::new, PowerUpComponent::powerUp);

	@Override
	public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
		var registryLookup = context.getRegistryLookup();
		if (registryLookup != null) {
			this.powerUp.resolveEntry(registryLookup)
					.flatMap(power -> power.value().attributesModifiers())
					.ifPresent(entityAttributeEntries -> buildTooltip(entityAttributeEntries, textConsumer));
		}
	}

    public static void buildTooltip(List<EntityAttributeEntry> attributes, Consumer<Text> textConsumer) {
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
