package fr.hugman.mubble.world.power_up.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.keybind.MubbleKeyBindingsKeys;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.phys.Vec3;


public record ShootProjectilePowerUpAction(
        EntityType<?> projectile,
        Holder<SoundEvent> sound,
        float speed,
        Optional<Integer> maxProjectiles,
        Optional<Integer> cooldown
        //TODO: add shooting algorithm
        //TODO: add projectile NBT
) implements PowerUpAction, TooltipProvider {
    public static final MapCodec<ShootProjectilePowerUpAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("projectile").forGetter(ShootProjectilePowerUpAction::projectile),
            SoundEvent.CODEC.fieldOf("sound").forGetter(ShootProjectilePowerUpAction::sound),
            Codec.FLOAT.optionalFieldOf("speed", 1.5F).forGetter(ShootProjectilePowerUpAction::speed),
            Codec.INT.optionalFieldOf("max_projectiles").forGetter(ShootProjectilePowerUpAction::maxProjectiles),
            Codec.INT.optionalFieldOf("cooldown").forGetter(ShootProjectilePowerUpAction::cooldown)
    ).apply(instance, ShootProjectilePowerUpAction::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ShootProjectilePowerUpAction> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(Registries.ENTITY_TYPE), (ShootProjectilePowerUpAction::projectile),
            SoundEvent.STREAM_CODEC, (ShootProjectilePowerUpAction::sound),
            ByteBufCodecs.FLOAT, (ShootProjectilePowerUpAction::speed),
            ByteBufCodecs.optional(ByteBufCodecs.INT), (ShootProjectilePowerUpAction::maxProjectiles),
            ByteBufCodecs.optional(ByteBufCodecs.INT), (ShootProjectilePowerUpAction::cooldown),
            ShootProjectilePowerUpAction::new
    );

    @Override
    public PowerUpActionType<?> getType() {
        return PowerUpActionTypes.SHOOT_PROJECTILE;
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

        player.swing(InteractionHand.MAIN_HAND);

        if (player.level().isClientSide()) {
            //TODO once powerup properties are synced, have a check on the client
            return InteractionResult.SUCCESS;
        }

        if (!level.isClientSide()) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), this.sound, SoundSource.NEUTRAL, 0.5F, 1.0F);
            var entity = this.projectile.create(level, EntitySpawnReason.TRIGGERED);
            if (null == entity) {
                return InteractionResult.FAIL;
            }
            if (entity instanceof Projectile projectileEntity) {
                projectileEntity.setOwner(player);
            }
            entity.setPos(player.getX(), player.getEyeY() - 0.1F, player.getZ());
            setVelocity(entity, player, player.getXRot(), player.getYRot(), 0.0F, this.speed, 1.0F);
            level.addFreshEntity(entity);
            properties.addProjectile(entity.getUUID());
            properties.setCooldown(cooldown.orElse(0));
        }
        return InteractionResult.SUCCESS;
    }


    public void setVelocity(Entity projectile, Entity shooter, float pitch, float yaw, float roll, float speed, float divergence) {
        float f = -Mth.sin(yaw * (float) (Math.PI / 180.0)) * Mth.cos(pitch * (float) (Math.PI / 180.0));
        float g = -Mth.sin((pitch + roll) * (float) (Math.PI / 180.0));
        float h = Mth.cos(yaw * (float) (Math.PI / 180.0)) * Mth.cos(pitch * (float) (Math.PI / 180.0));
        setVelocity(projectile, f, g, h, speed, divergence);
        Vec3 vec3d = shooter.getKnownMovement();
        projectile.setDeltaMovement(projectile.getDeltaMovement().add(vec3d.x, shooter.onGround() ? 0.0 : vec3d.y, vec3d.z));
    }

    public static void setVelocity(Entity projectile, double x, double y, double z, float power, float uncertainty) {
        Vec3 vec3d = calculateVelocity(projectile, x, y, z, power, uncertainty);
        projectile.setDeltaMovement(vec3d);
        projectile.needsSync = true;
        double d = vec3d.horizontalDistance();
        projectile.setYRot((float) (Mth.atan2(vec3d.x, vec3d.z) * 180.0F / (float) Math.PI));
        projectile.setXRot((float) (Mth.atan2(vec3d.y, d) * 180.0F / (float) Math.PI));
        projectile.yRotO = projectile.getYRot();
        projectile.xRotO = projectile.getXRot();
    }

    public static Vec3 calculateVelocity(Entity projectile, double x, double y, double z, float power, float uncertainty) {
        return new Vec3(x, y, z)
                .normalize()
                .add(
                        projectile.getRandom().triangle(0.0, 0.0172275 * (double) uncertainty),
                        projectile.getRandom().triangle(0.0, 0.0172275 * (double) uncertainty),
                        projectile.getRandom().triangle(0.0, 0.0172275 * (double) uncertainty)
                )
                .scale(power);
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        this.getTranslationKey().ifPresent(s -> textConsumer.accept(Component.translatable(
                s + ".description",
                        Component.keybind(MubbleKeyBindingsKeys.TRIGGER_POWER_UP),
                        Component.translatable(projectile.getDescriptionId())
                ).withStyle(ChatFormatting.GRAY)));
    }
}