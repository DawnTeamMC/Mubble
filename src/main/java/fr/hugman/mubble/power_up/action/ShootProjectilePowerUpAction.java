package fr.hugman.mubble.power_up.action;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

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
        var world = player.getServerWorld();
        world.playSound(null, player.getX(), player.getY(), player.getZ(), this.sound, SoundCategory.NEUTRAL, 0.5F, 1.0F);
        var entity = this.projectile.value().create(world, SpawnReason.TRIGGERED);
        switch (entity) {
            case ThrownItemEntity thrownItemEntity -> ProjectileEntity.spawnWithVelocity((world2, shooter, stack) -> {
                thrownItemEntity.setOwner(shooter);
                thrownItemEntity.setItem(stack);
                thrownItemEntity.setPosition(shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ());
                return thrownItemEntity;
            }, world, new ItemStack(Items.SNOWBALL), player, 0.0F, 1.5F, 1.0F);
            case ProjectileEntity projectile -> ProjectileEntity.spawnWithVelocity((world2, shooter, stack) -> {
                projectile.setOwner(shooter);
                projectile.setPosition(shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ());
                return projectile;
            }, world, new ItemStack(Items.SNOWBALL), player, 0.0F, 1.5F, 1.0F);
            case null -> {
                //idk
            }
            default -> {
                //idk
                entity.setPosition(player.getX(), player.getEyeY() - 0.1F, player.getZ());
                setVelocity(entity, player, player.getPitch(), player.getYaw(), 0.0F, 1.5F, 1.0F);
                world.spawnEntity(entity);
            }
        }
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
        projectile.prevYaw = projectile.getYaw();
        projectile.prevPitch = projectile.getPitch();
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
}
