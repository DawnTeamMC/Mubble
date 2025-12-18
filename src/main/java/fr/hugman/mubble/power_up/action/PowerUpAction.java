package fr.hugman.mubble.power_up.action;

import com.mojang.serialization.Codec;
import fr.hugman.mubble.registry.MubbleRegistries;
import fr.hugman.mubble.registry.MubbleRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import java.util.Optional;

public interface PowerUpAction {
    Codec<PowerUpAction> TYPE_CODEC = MubbleRegistries.POWER_UP_ACTION_TYPE.byNameCodec().dispatch(PowerUpAction::getType, PowerUpActionType::codec);
    StreamCodec<RegistryFriendlyByteBuf, PowerUpAction> TYPE_PACKET_CODEC = ByteBufCodecs.registry(MubbleRegistryKeys.POWER_UP_ACTION_TYPE).dispatch(PowerUpAction::getType, PowerUpActionType::packetCodec);

    Codec<Holder<PowerUpAction>> ENTRY_CODEC = RegistryFileCodec.create(MubbleRegistryKeys.POWER_UP_ACTION, TYPE_CODEC);
    StreamCodec<RegistryFriendlyByteBuf, Holder<PowerUpAction>> ENTRY_PACKET_CODEC = ByteBufCodecs.holder(MubbleRegistryKeys.POWER_UP_ACTION, TYPE_PACKET_CODEC);
    StreamCodec<RegistryFriendlyByteBuf, Optional<Holder<PowerUpAction>>> OPTIONAL_ENTRY_PACKET_CODEC = ENTRY_PACKET_CODEC.apply(ByteBufCodecs::optional);

    PowerUpActionType<?> getType();

    InteractionResult trigger(Player player);

    default Optional<String> getTranslationKey() {
        return MubbleRegistries.POWER_UP_ACTION_TYPE
                .getResourceKey(this.getType())
                .map(k -> Util.makeDescriptionId("power_up_action_type", k.identifier()));
    }
}
