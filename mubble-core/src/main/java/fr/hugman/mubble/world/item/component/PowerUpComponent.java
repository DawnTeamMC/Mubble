package fr.hugman.mubble.world.item.component;

import com.mojang.serialization.Codec;
import fr.hugman.mubble.world.entity.ai.attributes.EntityAttributeEntry;
import java.util.function.Consumer;

import fr.hugman.mubble.world.power_up.PowerUp;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipProvider;

public record PowerUpComponent(Holder<PowerUp> powerUp) implements TooltipProvider {
    public static final Codec<PowerUpComponent> CODEC = PowerUp.CODEC.xmap(PowerUpComponent::new, PowerUpComponent::powerUp);
    public static final StreamCodec<RegistryFriendlyByteBuf, PowerUpComponent> STREAM_CODEC = PowerUp.STREAM_CODEC.map(PowerUpComponent::new, PowerUpComponent::powerUp);

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        var registryLookup = context.registries();
        if (registryLookup != null) {
            buildAutomaticTooltip(this.powerUp.value(), context, textConsumer, type, components);
        }
    }

    /**
     * Writes what the power-up granted by the item does.
     * <p>
     * A power-up describing itself says it in its own words, and its attribute modifiers are then
     * only spelled out one by one under the advanced tooltips: a form as thorough as the mini or the
     * mega one moves a dozen attributes at once, and listing them all is a wall of text no player
     * asked for. One that says nothing keeps showing them, since it is all it has to show — that is
     * what a data pack adding a power-up gets until it writes a description of its own.
     */
    public static void buildAutomaticTooltip(PowerUp powerUp, Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        powerUp.description().forEach(line -> textConsumer.accept(line.copy().withStyle(ChatFormatting.GRAY)));
        if (powerUp.action().isPresent()) {
            var action = powerUp.action().get().value();
            if(action instanceof TooltipProvider tooltipAppender) {
                tooltipAppender.addToTooltip(context, textConsumer, type, components);
            }
        }
        boolean detailed = powerUp.description().isEmpty() || type.isAdvanced();
        if (detailed && powerUp.attributesModifiers().isPresent() && !powerUp.attributesModifiers().get().isEmpty()) {
            textConsumer.accept(CommonComponents.EMPTY);
            textConsumer.accept(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));

            for (EntityAttributeEntry entry : powerUp.attributesModifiers().get()) {
                AttributeModifier entityAttributeModifier = entry.modifier();
                double d = entityAttributeModifier.amount();
                double e;
                if (entityAttributeModifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                        && entityAttributeModifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                    e = entityAttributeModifier.amount();
                } else {
                    e = entityAttributeModifier.amount() * 100.0;
                }

                if (d > 0.0) {
                    textConsumer.accept(
                            Component.translatable(
                                            "attribute.modifier.plus." + entityAttributeModifier.operation().id(),
                                            ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(e),
                                            Component.translatable(entry.attribute().value().getDescriptionId())
                                    )
                                    .withStyle(ChatFormatting.BLUE)
                    );
                } else if (d < 0.0) {
                    e *= -1.0;
                    textConsumer.accept(
                            Component.translatable(
                                            "attribute.modifier.take." + entityAttributeModifier.operation().id(),
                                            ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(e),
                                            Component.translatable(entry.attribute().value().getDescriptionId())
                                    )
                                    .withStyle(ChatFormatting.RED)
                    );
                }
            }
        }
    }
}
