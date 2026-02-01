package fr.hugman.mubble.world.item.consume_effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.world.power_up.PowerUp;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;

public record ChangePowerUpConsumeEffect(Holder<PowerUp> powerUp) implements ConsumeEffect {
    public static final MapCodec<ChangePowerUpConsumeEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            PowerUp.CODEC.fieldOf("power_up").forGetter(ChangePowerUpConsumeEffect::powerUp)
    ).apply(instance, ChangePowerUpConsumeEffect::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ChangePowerUpConsumeEffect> STREAM_CODEC = StreamCodec.composite(
            PowerUp.STREAM_CODEC, ChangePowerUpConsumeEffect::powerUp,
            ChangePowerUpConsumeEffect::new
    );

    @Override
    public Type<ChangePowerUpConsumeEffect> getType() {
        return MubbleConsumeEffectTypes.CHANGE_POWER_UP;
    }

    @Override
    public boolean apply(Level level, ItemStack stack, LivingEntity user) {
        if (user instanceof Player player) {
            if (PowerUp.canChange(user, powerUp)) {
                player.setPowerUp(powerUp);
                return true;
            }
        }
        return false;
    }
}
