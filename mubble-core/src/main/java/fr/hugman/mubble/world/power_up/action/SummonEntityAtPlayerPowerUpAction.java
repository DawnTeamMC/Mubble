package fr.hugman.mubble.world.power_up.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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


public record SummonEntityAtPlayerPowerUpAction(
        EntityType<?> entity,
        int yOffset,
        Optional<Integer> maxProjectiles
) implements PowerUpAction {
    public static final MapCodec<SummonEntityAtPlayerPowerUpAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity").forGetter(SummonEntityAtPlayerPowerUpAction::entity),
            Codec.INT.fieldOf("y_offset").forGetter(SummonEntityAtPlayerPowerUpAction::yOffset),
            Codec.INT.optionalFieldOf("max_projectiles").forGetter(SummonEntityAtPlayerPowerUpAction::maxProjectiles)
    ).apply(instance, SummonEntityAtPlayerPowerUpAction::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SummonEntityAtPlayerPowerUpAction> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(Registries.ENTITY_TYPE), (SummonEntityAtPlayerPowerUpAction::entity),
            ByteBufCodecs.INT, SummonEntityAtPlayerPowerUpAction::yOffset,
            ByteBufCodecs.optional(ByteBufCodecs.INT), (SummonEntityAtPlayerPowerUpAction::maxProjectiles),
            SummonEntityAtPlayerPowerUpAction::new
    );

    @Override
    public PowerUpActionType<?> getType() {
        return PowerUpActionTypes.SUMMON_ENTITY_AT_PLAYER;
    }

    @Override
    public boolean canBeTriggered(Player player) {
        var properties = player.getPowerUpProperties();

        var level = player.level();
        if (!level.isClientSide()) {
            properties.removeInvalidProjectiles(level);
        }
        if(maxProjectiles.isPresent() && properties.getProjectiles().size() >= maxProjectiles.get()) {
            return false;
        }
        return true;
    }

    @Override
    public InteractionResult trigger(Player player) {
        var properties = player.getPowerUpProperties();

        var level = player.level();
        if (!level.isClientSide()) {
            properties.removeInvalidProjectiles(level);
        }
        if(maxProjectiles.isPresent() && properties.getProjectiles().size() >= maxProjectiles.get()) {
            return InteractionResult.FAIL;
        }

        if (player.level().isClientSide()) {
            //TODO once powerup properties are synced, have a check on the client
            return InteractionResult.SUCCESS;
        }

        if (!level.isClientSide()) {
            var entity = this.entity().create(level, EntitySpawnReason.TRIGGERED);
            if (null == entity) {
                return InteractionResult.FAIL;
            }
            entity.setPos(player.getX(), player.getY() + this.yOffset, player.getZ());
            level.addFreshEntity(entity);

            player.setDeltaMovement(player.getDeltaMovement().x, 0.2D, player.getDeltaMovement().z);
            ((ServerPlayer) player).connection.send(new ClientboundSetEntityMotionPacket(player));
            entity.fallDistance = 0.0F;
        }
        return InteractionResult.SUCCESS;
    }
}