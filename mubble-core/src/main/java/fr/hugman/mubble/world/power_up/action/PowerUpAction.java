package fr.hugman.mubble.world.power_up.action;

import com.mojang.serialization.Codec;
import fr.hugman.mubble.core.registries.MubbleBuiltInRegistries;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.power_up.PowerUpProperties;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public interface PowerUpAction {
    Codec<PowerUpAction> TYPE_CODEC = MubbleBuiltInRegistries.POWER_UP_ACTION_TYPE.byNameCodec().dispatch(PowerUpAction::getType, PowerUpActionType::codec);
    StreamCodec<RegistryFriendlyByteBuf, PowerUpAction> TYPE_STREAM_CODEC = ByteBufCodecs.registry(MubbleRegistries.POWER_UP_ACTION_TYPE).dispatch(PowerUpAction::getType, PowerUpActionType::streamCodec);

    Codec<Holder<PowerUpAction>> CODEC = RegistryFileCodec.create(MubbleRegistries.POWER_UP_ACTION, TYPE_CODEC);
    StreamCodec<RegistryFriendlyByteBuf, Holder<PowerUpAction>> STREAM_CODEC = ByteBufCodecs.holder(MubbleRegistries.POWER_UP_ACTION, TYPE_STREAM_CODEC);
    StreamCodec<RegistryFriendlyByteBuf, Optional<Holder<PowerUpAction>>> OPTIONAL_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs::optional);

    PowerUpActionType<?> getType();

    @Nullable
    default PowerUpProperties setUpProperties() {
        return null;
    }

    default boolean supportsProperties() {
        return false;
    }

    default boolean canBeTriggered(Player player) {
        return true;
    }

    InteractionResult trigger(Player player);

    default boolean shouldSwingOtherHand() {
        return false;
    }

    default Optional<String> getTranslationKey() {
        return MubbleBuiltInRegistries.POWER_UP_ACTION_TYPE
                .getResourceKey(this.getType())
                .map(k -> Util.makeDescriptionId("power_up_action_type", k.identifier()));
    }
}
