package fr.hugman.mubble.platformer.item;

import fr.hugman.mubble.platformer.registries.PlatformerRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.platformer.PlatformerProfile;
import fr.hugman.mubble.platformer.access.AccessMode;
import fr.hugman.mubble.platformer.access.MoveSelector;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

/**
 * Makes an item a source of platformer movement while it sits in a matching slot, the same way attribute
 * modifiers work.
 * <pre>{@code
 * "mubble:platformer_movement": {
 *   "profile": "mubble:overworld",
 *   "slots": "feet",
 *   "moves": { "#mubble:all": "enable" }
 * }
 * }</pre>
 * An item can only {@code force} or {@code enable} a move, never {@code deny} one: that is kept for
 * rulesets and commands, so that a trial can always lock a move whatever the player wears.
 *
 * @param profile the profile the item asks for
 * @param slots   the slots the item has to be in to count
 * @param moves   what the item says about each move or tag of moves
 */
public record PlatformerMovementComponent(
        Optional<ResourceKey<PlatformerProfile>> profile,
        EquipmentSlotGroup slots,
        Map<MoveSelector, AccessMode> moves
) implements TooltipProvider {
    public static final Codec<PlatformerMovementComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(PlatformerRegistries.PLATFORMER_PROFILE).optionalFieldOf("profile").forGetter(PlatformerMovementComponent::profile),
            EquipmentSlotGroup.CODEC.fieldOf("slots").forGetter(PlatformerMovementComponent::slots),
            Codec.unboundedMap(MoveSelector.CODEC, AccessMode.ITEM_CODEC).optionalFieldOf("moves", Map.of()).forGetter(PlatformerMovementComponent::moves)
    ).apply(instance, PlatformerMovementComponent::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlatformerMovementComponent> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(PlatformerRegistries.PLATFORMER_PROFILE).apply(ByteBufCodecs::optional), PlatformerMovementComponent::profile,
            EquipmentSlotGroup.STREAM_CODEC, PlatformerMovementComponent::slots,
            ByteBufCodecs.map(HashMap::new, MoveSelector.STREAM_CODEC, AccessMode.STREAM_CODEC), PlatformerMovementComponent::moves,
            PlatformerMovementComponent::new
    );

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag flag, DataComponentGetter components) {
        textConsumer.accept(Component.translatable("item." + Mubble.MOD_ID + ".platformer_movement.tooltip").withStyle(ChatFormatting.GRAY));
        if (flag.isAdvanced()) {
            this.profile.ifPresent(key -> textConsumer.accept(Component.translatable("item." + Mubble.MOD_ID + ".platformer_movement.profile", key.identifier().toString()).withStyle(ChatFormatting.DARK_GRAY)));
        }
    }
}
