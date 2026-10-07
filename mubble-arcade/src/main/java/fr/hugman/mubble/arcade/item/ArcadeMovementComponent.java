package fr.hugman.mubble.arcade.item;

import fr.hugman.mubble.arcade.registries.ArcadeRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.arcade.ArcadeProfile;
import fr.hugman.mubble.arcade.access.AccessMode;
import fr.hugman.mubble.arcade.access.MoveSelector;
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
 * Makes an item a source of arcade movement while it sits in a matching slot, the same way attribute
 * modifiers work.
 * <pre>{@code
 * "mubble:arcade_movement": {
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
public record ArcadeMovementComponent(
        Optional<ResourceKey<ArcadeProfile>> profile,
        EquipmentSlotGroup slots,
        Map<MoveSelector, AccessMode> moves
) implements TooltipProvider {
    public static final Codec<ArcadeMovementComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(ArcadeRegistries.ARCADE_PROFILE).optionalFieldOf("profile").forGetter(ArcadeMovementComponent::profile),
            EquipmentSlotGroup.CODEC.fieldOf("slots").forGetter(ArcadeMovementComponent::slots),
            Codec.unboundedMap(MoveSelector.CODEC, AccessMode.ITEM_CODEC).optionalFieldOf("moves", Map.of()).forGetter(ArcadeMovementComponent::moves)
    ).apply(instance, ArcadeMovementComponent::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ArcadeMovementComponent> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(ArcadeRegistries.ARCADE_PROFILE).apply(ByteBufCodecs::optional), ArcadeMovementComponent::profile,
            EquipmentSlotGroup.STREAM_CODEC, ArcadeMovementComponent::slots,
            ByteBufCodecs.map(HashMap::new, MoveSelector.STREAM_CODEC, AccessMode.STREAM_CODEC), ArcadeMovementComponent::moves,
            ArcadeMovementComponent::new
    );

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag flag, DataComponentGetter components) {
        textConsumer.accept(Component.translatable("item." + Mubble.MOD_ID + ".arcade_movement.tooltip").withStyle(ChatFormatting.GRAY));
        if (flag.isAdvanced()) {
            this.profile.ifPresent(key -> textConsumer.accept(Component.translatable("item." + Mubble.MOD_ID + ".arcade_movement.profile", key.identifier().toString()).withStyle(ChatFormatting.DARK_GRAY)));
        }
    }
}
