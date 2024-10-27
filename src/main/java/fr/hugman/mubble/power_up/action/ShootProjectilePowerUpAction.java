package fr.hugman.mubble.power_up.action;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;

public record ShootProjectilePowerUpAction(
        RegistryEntry<EntityType<?>> projectile,
        RegistryEntry<SoundEvent> sound
        //TODO: add shooting algorithm
        //TODO: add projectile NBT
) implements PowerUpAction {
    public static final MapCodec<ShootProjectilePowerUpAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Registries.ENTITY_TYPE.getEntryCodec().fieldOf("projectile").forGetter(ShootProjectilePowerUpAction::projectile),
            SoundEvent.ENTRY_CODEC.fieldOf("sound").forGetter(ShootProjectilePowerUpAction::sound)
    ).apply(instance, ShootProjectilePowerUpAction::new));

    public static final PacketCodec<RegistryByteBuf, ShootProjectilePowerUpAction> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.registryEntry(RegistryKeys.ENTITY_TYPE), (ShootProjectilePowerUpAction::projectile),
            SoundEvent.ENTRY_PACKET_CODEC, (ShootProjectilePowerUpAction::sound),
            ShootProjectilePowerUpAction::new
    );


    @Override
    public PowerUpActionType<?> getType() {
        return PowerUpActionTypes.SHOOT_PROJECTILE;
    }

    @Override
    public void onTrigger(MinecraftServer server, ServerPlayerEntity player) {

    }
}
