package fr.hugman.mubble.entity;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.item.MubbleItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RedKoopaShellEntity extends KoopaShellEntity {
    private static final Identifier TEXTURE = Mubble.id("textures/entity/koopa_shell/red.png");
    private PlayerEntity target;

    public RedKoopaShellEntity(EntityType<? extends RedKoopaShellEntity> entityType, World world) {
        super(entityType, world);
    }

    public RedKoopaShellEntity(World world, double x, double y, double z) {
        super(MubbleEntityTypes.RED_KOOPA_SHELL, world, x, y, z);
    }

    public RedKoopaShellEntity(World world, LivingEntity owner) {
        super(MubbleEntityTypes.RED_KOOPA_SHELL, world, owner);
    }

    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(MubbleItems.RED_KOOPA_SHELL);
    }

    @Override
    public Identifier getTexture() {
        return TEXTURE;
    }

    @Override
    public void tick() {
        if (this.age % 20 == 1) {
            this.expensiveUpdate();
        }

        if (this.target != null && (this.target.isSpectator() || this.target.isDead())) {
            this.target = null;
        }

        if (this.target != null) {
            Vec3d vec3d = new Vec3d(
                    this.target.getX() - this.getX(), this.target.getY() + (double) this.target.getStandingEyeHeight() / 2.0 - this.getY(), this.target.getZ() - this.getZ()
            );
            double d = vec3d.lengthSquared();
            if (d < 64.0) {
                double e = 1.0 - Math.sqrt(d) / 8.0;
                this.setVelocity(this.getVelocity().add(vec3d.normalize().multiply(e * e * 0.1)));
            }
        }

        super.tick();
    }

    private void expensiveUpdate() {
        if (this.target == null || this.target.squaredDistanceTo(this) > 64.0) {
            this.target = this.getWorld().getClosestPlayer(this, 16.0);
        }
    }
}
