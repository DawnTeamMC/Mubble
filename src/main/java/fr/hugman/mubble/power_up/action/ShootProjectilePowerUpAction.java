package fr.hugman.mubble.power_up.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.keybind.MubbleKeyBindingsKeys;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;
import java.util.function.Consumer;


public record ShootProjectilePowerUpAction(
        EntityType<?> projectile,
        RegistryEntry<SoundEvent> sound,
        float speed,
        Optional<Integer> maxProjectiles,
        Optional<Integer> cooldown
        //TODO: add shooting algorithm
        //TODO: add projectile NBT
) implements PowerUpAction, TooltipAppender {
    public static final MapCodec<ShootProjectilePowerUpAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Registries.ENTITY_TYPE.getCodec().fieldOf("projectile").forGetter(ShootProjectilePowerUpAction::projectile),
            SoundEvent.ENTRY_CODEC.fieldOf("sound").forGetter(ShootProjectilePowerUpAction::sound),
            Codec.FLOAT.optionalFieldOf("speed", 1.5F).forGetter(ShootProjectilePowerUpAction::speed),
            Codec.INT.optionalFieldOf("max_projectiles").forGetter(ShootProjectilePowerUpAction::maxProjectiles),
            Codec.INT.optionalFieldOf("cooldown").forGetter(ShootProjectilePowerUpAction::cooldown)
    ).apply(instance, ShootProjectilePowerUpAction::new));

    public static final PacketCodec<RegistryByteBuf, ShootProjectilePowerUpAction> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.registryValue(RegistryKeys.ENTITY_TYPE), (ShootProjectilePowerUpAction::projectile),
            SoundEvent.ENTRY_PACKET_CODEC, (ShootProjectilePowerUpAction::sound),
            PacketCodecs.FLOAT, (ShootProjectilePowerUpAction::speed),
            PacketCodecs.optional(PacketCodecs.INTEGER), (ShootProjectilePowerUpAction::maxProjectiles),
            PacketCodecs.optional(PacketCodecs.INTEGER), (ShootProjectilePowerUpAction::cooldown),
            ShootProjectilePowerUpAction::new
    );

    @Override
    public PowerUpActionType<?> getType() {
        return PowerUpActionTypes.SHOOT_PROJECTILE;
    }

    @Override
    public ActionResult trigger(PlayerEntity player) {
        var properties = player.getPowerUpProperties();

        var world = player.getEntityWorld();
        if (!world.isClient()) {
            properties.removeInvalidProjectiles(world);
        }
        if(maxProjectiles.isPresent() && properties.projectiles.size() >= maxProjectiles.get()) {
            return ActionResult.FAIL;
        }

        player.swingHand(Hand.MAIN_HAND);

        if (player.getEntityWorld().isClient()) {
            //TODO once powerup properties are synced, have a check on the client
            return ActionResult.SUCCESS;
        }

        if (!world.isClient()) {
            world.playSound(null, player.getX(), player.getY(), player.getZ(), this.sound, SoundCategory.NEUTRAL, 0.5F, 1.0F);
            var entity = this.projectile.create(world, SpawnReason.TRIGGERED);
            if (null == entity) {
                return ActionResult.FAIL;
            }
            if (entity instanceof ProjectileEntity projectileEntity) {
                projectileEntity.setOwner(player);
            }
            entity.setPosition(player.getX(), player.getEyeY() - 0.1F, player.getZ());
            setVelocity(entity, player, player.getPitch(), player.getYaw(), 0.0F, this.speed, 1.0F);
            world.spawnEntity(entity);
            properties.projectiles.add(entity.getUuid());
            properties.cooldown = cooldown.orElse(0);
        }
        return ActionResult.SUCCESS;
    }


    public void setVelocity(Entity projectile, Entity shooter, float pitch, float yaw, float roll, float speed, float divergence) {
        float f = -MathHelper.sin(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
        float g = -MathHelper.sin((pitch + roll) * (float) (Math.PI / 180.0));
        float h = MathHelper.cos(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
        setVelocity(projectile, f, g, h, speed, divergence);
        Vec3d vec3d = shooter.getMovement();
        projectile.setVelocity(projectile.getVelocity().add(vec3d.x, shooter.isOnGround() ? 0.0 : vec3d.y, vec3d.z));
    }

    public static void setVelocity(Entity projectile, double x, double y, double z, float power, float uncertainty) {
        Vec3d vec3d = calculateVelocity(projectile, x, y, z, power, uncertainty);
        projectile.setVelocity(vec3d);
        projectile.velocityDirty = true;
        double d = vec3d.horizontalLength();
        projectile.setYaw((float) (MathHelper.atan2(vec3d.x, vec3d.z) * 180.0F / (float) Math.PI));
        projectile.setPitch((float) (MathHelper.atan2(vec3d.y, d) * 180.0F / (float) Math.PI));
        projectile.lastYaw = projectile.getYaw();
        projectile.lastPitch = projectile.getPitch();
    }

    public static Vec3d calculateVelocity(Entity projectile, double x, double y, double z, float power, float uncertainty) {
        return new Vec3d(x, y, z)
                .normalize()
                .add(
                        projectile.getRandom().nextTriangular(0.0, 0.0172275 * (double) uncertainty),
                        projectile.getRandom().nextTriangular(0.0, 0.0172275 * (double) uncertainty),
                        projectile.getRandom().nextTriangular(0.0, 0.0172275 * (double) uncertainty)
                )
                .multiply(power);
    }

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        this.getTranslationKey().ifPresent(s -> textConsumer.accept(Text.translatable(
                s + ".description",
                        Text.keybind(MubbleKeyBindingsKeys.TRIGGER_POWER_UP),
                        Text.translatable(projectile.getTranslationKey())
                ).formatted(Formatting.GRAY)));
    }
}