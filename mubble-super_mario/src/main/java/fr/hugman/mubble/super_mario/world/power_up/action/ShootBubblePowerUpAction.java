package fr.hugman.mubble.super_mario.world.power_up.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.keybind.MubbleKeyBindingsKeys;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.entity.projectile.Bubble;
import fr.hugman.mubble.world.power_up.PowerUpProperties;
import fr.hugman.mubble.world.power_up.action.PowerUpAction;
import fr.hugman.mubble.world.power_up.action.PowerUpActionType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;

import java.util.function.Consumer;

/**
 * A power-up action that shoots a {@link Bubble} entity.
 * The player gets {@code maxCharges} charges, and one empty charge is
 * refilled every {@code rechargeInterval} ticks.
 */
public record ShootBubblePowerUpAction(
        int maxCharges,
        int rechargeInterval
) implements PowerUpAction, TooltipProvider {

    public static final int DEFAULT_MAX_CHARGES = 3;
    public static final int DEFAULT_RECHARGE_INTERVAL = 60; // 3 seconds

    public static final MapCodec<ShootBubblePowerUpAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.optionalFieldOf("max_charges", DEFAULT_MAX_CHARGES).forGetter(ShootBubblePowerUpAction::maxCharges),
            Codec.INT.optionalFieldOf("recharge_interval", DEFAULT_RECHARGE_INTERVAL).forGetter(ShootBubblePowerUpAction::rechargeInterval)
    ).apply(instance, ShootBubblePowerUpAction::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ShootBubblePowerUpAction> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ShootBubblePowerUpAction::maxCharges,
            ByteBufCodecs.INT, ShootBubblePowerUpAction::rechargeInterval,
            ShootBubblePowerUpAction::new
    );

    @Override
    public PowerUpActionType<?> getType() {
        return SuperMarioPowerUpActionTypes.SHOOT_BUBBLE;
    }

    @Override
    public boolean canBeRefilled() {
        return true;
    }

    @Override
    public PowerUpProperties setUpProperties() {
        return new PowerUpProperties(PowerUpProperties.ChargeCounting.TIMED_RECHARGE, maxCharges, rechargeInterval);
    }

    @Override
    public boolean canBeTriggered(Player player) {
        var properties = player.getPowerUpProperties();
        if (properties == null) {
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
        if (properties == null) {
            properties = setUpProperties();
            player.setPowerUpProperties(properties);
        }

        var level = player.level();

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        var entity = SuperMarioEntityTypes.BUBBLE.create(level, EntitySpawnReason.TRIGGERED);
        if (entity == null) {
            return InteractionResult.FAIL;
        }

        entity.setOwner(player);
        entity.setPos(player.getX(), player.getEyeY() - 0.1F, player.getZ());
        setVelocity(entity, player, player.getXRot(), player.getYRot(), 0.0F, 0.4F, 1.0F);
        level.addFreshEntity(entity);
        properties.addEntity(entity.getUUID());

        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean shouldSwingOtherHand() {
        return true;
    }

    private void setVelocity(Bubble projectile, Player shooter, float pitch, float yaw, float roll, float speed, float divergence) {
        float f = -Mth.sin(yaw * (float) (Math.PI / 180.0)) * Mth.cos(pitch * (float) (Math.PI / 180.0));
        float g = -Mth.sin((pitch + roll) * (float) (Math.PI / 180.0));
        float h = Mth.cos(yaw * (float) (Math.PI / 180.0)) * Mth.cos(pitch * (float) (Math.PI / 180.0));
        Vec3 vec = new Vec3(f, g, h)
                .normalize()
                .add(
                        projectile.getRandom().triangle(0.0, 0.0172275 * (double) divergence),
                        projectile.getRandom().triangle(0.0, 0.0172275 * (double) divergence),
                        projectile.getRandom().triangle(0.0, 0.0172275 * (double) divergence)
                )
                .scale(speed);
        Vec3 shooterVel = shooter.getKnownMovement();
        projectile.setDeltaMovement(vec.add(shooterVel.x, shooter.onGround() ? 0.0 : shooterVel.y, shooterVel.z));
        projectile.needsSync = true;

        double d = vec.horizontalDistance();
        projectile.setYRot((float) (Mth.atan2(vec.x, vec.z) * 180.0F / (float) Math.PI));
        projectile.setXRot((float) (Mth.atan2(vec.y, d) * 180.0F / (float) Math.PI));
        projectile.yRotO = projectile.getYRot();
        projectile.xRotO = projectile.getXRot();
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        this.getTranslationKey().ifPresent(s -> textConsumer.accept(Component.translatable(
                s + ".description",
                Component.keybind(MubbleKeyBindingsKeys.TRIGGER_POWER_UP),
                this.maxCharges
        ).withStyle(ChatFormatting.GRAY)));
    }
}
