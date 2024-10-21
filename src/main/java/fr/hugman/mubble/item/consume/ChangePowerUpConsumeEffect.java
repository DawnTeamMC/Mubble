package fr.hugman.mubble.item.consume;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.power_up.PowerUp;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.RegistryPair;
import net.minecraft.world.World;

public record ChangePowerUpConsumeEffect(RegistryPair<PowerUp> powerUp) implements ConsumeEffect {
    public static final MapCodec<ChangePowerUpConsumeEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            PowerUp.PAIR_CODEC.fieldOf("power_up").forGetter(ChangePowerUpConsumeEffect::powerUp)
    ).apply(instance, ChangePowerUpConsumeEffect::new));
    public static final PacketCodec<RegistryByteBuf, ChangePowerUpConsumeEffect> PACKET_CODEC = PacketCodec.tuple(
            PowerUp.PAIR_PACKET_CODEC, ChangePowerUpConsumeEffect::powerUp,
            ChangePowerUpConsumeEffect::new
    );

    @Override
    public Type<ChangePowerUpConsumeEffect> getType() {
        return MubbleConsumeEffectTypes.CHANGE_POWER_UP;
    }

    @Override
    public boolean onConsume(World world, ItemStack stack, LivingEntity user) {
        if (user instanceof PlayerEntity player) {
            var previousPowerUp = player.getPowerUp();
            var entry = this.powerUp.getEntry(world.getRegistryManager());

            if (entry.isEmpty()) {
                return false;
            }

            if (previousPowerUp.isPresent()) {
                if (previousPowerUp.get().matches(entry.get())) {
                    return false;
                }
            }
            player.setPowerUp(entry.get());
            return true;
        }
        return false;
    }
}
