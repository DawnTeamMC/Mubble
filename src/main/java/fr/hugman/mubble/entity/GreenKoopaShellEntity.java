package fr.hugman.mubble.entity;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.item.MubbleItems;
import fr.hugman.mubble.sound.MubbleSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class GreenKoopaShellEntity extends KoopaShellEntity {
    private static final Identifier TEXTURE = Mubble.id("textures/entity/green_koopa_shell.png");

    public GreenKoopaShellEntity(EntityType<? extends GreenKoopaShellEntity> entityType, World world) {
        super(entityType, world, 5);
    }

    public GreenKoopaShellEntity(World world, double x, double y, double z) {
        this(MubbleEntityTypes.GREEN_KOOPA_SHELL, world);
        this.setPosition(x, y, z);
    }

    public GreenKoopaShellEntity(World world, LivingEntity owner) {
        this(world, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ());
        this.setOwner(owner);
    }

    @Override
    public void onStompedBy(List<Entity> entities) {
        super.onStompedBy(entities);
        if (this.getEntityWorld() instanceof ServerWorld) {
            if (this.isStopped()) {
                var vec3d = entities.getFirst().getVelocity();
                if (vec3d.horizontalLength() == 0.0D) {
                    vec3d = this.getEntityPos().subtract(entities.getFirst().getEntityPos()).normalize();
                }
                //TODO: if still stopped, make it random
                this.setVelocity(vec3d.x, 0.0d, vec3d.z);
                this.targetHorizontalSpeed(TARGET_SPEED, Float.MAX_VALUE);
                this.velocityDirty = true;
                this.playSound(MubbleSounds.KOOPA_SHELL_KICK, 0.4F, 1.0F);
                if (entities.getFirst() instanceof Entity owner) {
                    this.setOwner(owner);
                }
                //TODO: add particles
                //TODO: reset rebound count? configurable?
            } else {
                this.setVelocity(Vec3d.ZERO);
                this.playSound(MubbleSounds.KOOPA_SHELL_KICK, 0.4F, 1.2F);
                //TODO: add particles
            }
        }
    }

    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(MubbleItems.GREEN_KOOPA_SHELL);
    }

    @Override
    public Identifier getTexture() {
        return TEXTURE;
    }
}
