package fr.hugman.mubble.super_mario.world.power_up.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.world.power_up.PowerUpProperties;
import fr.hugman.mubble.world.power_up.action.PowerUpAction;
import fr.hugman.mubble.world.power_up.action.PowerUpActionType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;


public record SpawnCloudPlatformPowerUpAction(
        EntityType<?> entity,
        Optional<Integer> max
) implements PowerUpAction {
    public static final MapCodec<SpawnCloudPlatformPowerUpAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity").forGetter(SpawnCloudPlatformPowerUpAction::entity),
            Codec.INT.optionalFieldOf("max").forGetter(SpawnCloudPlatformPowerUpAction::max)
    ).apply(instance, SpawnCloudPlatformPowerUpAction::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpawnCloudPlatformPowerUpAction> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(Registries.ENTITY_TYPE), (SpawnCloudPlatformPowerUpAction::entity),
            ByteBufCodecs.optional(ByteBufCodecs.INT), (SpawnCloudPlatformPowerUpAction::max),
            SpawnCloudPlatformPowerUpAction::new
    );

    @Override
    public PowerUpActionType<?> getType() {
        return SuperMarioPowerUpActionTypes.SPAWN_CLOUD_PLATFORM;
    }

    @Override
    public PowerUpProperties setUpProperties() {
        return new PowerUpProperties(PowerUpProperties.ChargeCounting.FROM_ACTIVE_ENTITIES, max.orElse(Integer.MAX_VALUE));
    }

    @Override
    public boolean canBeTriggered(Player player) {
        var properties = player.getPowerUpProperties();

        if(properties == null) {
            properties = setUpProperties();
            player.setPowerUpProperties(properties);
        }

        var level = player.level();
        if (!level.isClientSide()) {
            properties.doSoftChecks(player);
        }
        return properties.getChargeCount() > 0;
    }

    @Override
    public InteractionResult trigger(Player player) {
        var properties = player.getPowerUpProperties();
        var level = player.level();

        if(properties == null) {
            properties = setUpProperties();
            player.setPowerUpProperties(properties);
        }

        if (level.isClientSide()) {
            //TODO once powerup properties are synced, have a check on the client
            return InteractionResult.SUCCESS;
        }

        if (!level.isClientSide()) {
            var entity = this.entity().create(level, EntitySpawnReason.TRIGGERED);
            if (null == entity) {
                return InteractionResult.FAIL;
            }
            entity.setPos(player.getX(), player.getY() - 0.5f - entity.getBbHeight(), player.getZ());
            level.addFreshEntity(entity);
            properties.addEntity(entity.getUUID());

            player.setDeltaMovement(player.getDeltaMovement().x, 0.2D, player.getDeltaMovement().z);
            ((ServerPlayer) player).connection.send(new ClientboundSetEntityMotionPacket(player));
            entity.fallDistance = 0.0F;
        }
        return InteractionResult.SUCCESS;
    }
}