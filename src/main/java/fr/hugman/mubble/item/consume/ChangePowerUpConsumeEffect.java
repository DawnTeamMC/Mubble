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
import net.minecraft.registry.entry.LazyRegistryEntryReference;
import net.minecraft.world.World;

public record ChangePowerUpConsumeEffect(LazyRegistryEntryReference<PowerUp> powerUp) implements ConsumeEffect {
    public static final MapCodec<ChangePowerUpConsumeEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            PowerUp.LAZY_ENTRY_CODEC.fieldOf("power_up").forGetter(ChangePowerUpConsumeEffect::powerUp)
    ).apply(instance, ChangePowerUpConsumeEffect::new));
    public static final PacketCodec<RegistryByteBuf, ChangePowerUpConsumeEffect> PACKET_CODEC = PacketCodec.tuple(
            PowerUp.LAZY_ENTRY_PACKET_CODEC, ChangePowerUpConsumeEffect::powerUp,
            ChangePowerUpConsumeEffect::new
    );

    @Override
    public Type<ChangePowerUpConsumeEffect> getType() {
        return MubbleConsumeEffectTypes.CHANGE_POWER_UP;
    }

    @Override
    public boolean onConsume(World world, ItemStack stack, LivingEntity user) {
        if (user instanceof PlayerEntity player) {
            var entry = powerUp.resolveEntry(world.getRegistryManager());
            if (entry.isPresent()) {
                if (PowerUp.canChange(user, entry.get())) {
                    player.setPowerUp(entry.get());
                    return true;
                }
            }
        }
        return false;
    }
}
