package fr.hugman.mubble.entity;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.item.MubbleItems;
import fr.hugman.mubble.sound.MubbleSounds;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class GreenKoopaShellEntity extends KoopaShellEntity {
    private static final Identifier TEXTURE = Mubble.id("textures/entity/green_koopa_shell.png");

    public GreenKoopaShellEntity(EntityType<? extends GreenKoopaShellEntity> entityType, Level world) {
        super(entityType, world, 5);
    }

    public GreenKoopaShellEntity(Level world, double x, double y, double z) {
        this(MubbleEntityTypes.GREEN_KOOPA_SHELL, world);
        this.setPos(x, y, z);
    }

    public GreenKoopaShellEntity(Level world, LivingEntity owner) {
        this(world, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ());
        this.setOwner(owner);
    }

    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide() || !this.isStopped()) {
            return;
        }
        var entities = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
        if (!entities.isEmpty()) {
            this.kickShell(entities.getFirst());
        }
    }

    @Override
    public boolean canBeStomped() {
        return super.canBeStomped() && !this.isStopped();
    }

    @Override
    public void onStompedBy(List<Entity> entities) {
        super.onStompedBy(entities);
        if (this.level() instanceof ServerLevel) {
            if (!this.isStopped()) {
                this.setDeltaMovement(Vec3.ZERO);
                this.playSound(MubbleSounds.KOOPA_SHELL_KICK, 0.4F, 1.2F);
                //TODO: add particles
            }
        }
    }

    public void kickShell(Entity kicker) {
        var vec3d = kicker.getKnownMovement();
        if (vec3d.horizontalDistance() == 0.0D) {
            vec3d = this.position().subtract(kicker.position()).normalize();
        }
        //TODO: if still stopped, make it random
        this.setDeltaMovement(vec3d.x, 0.0d, vec3d.z);
        this.targetHorizontalSpeed(TARGET_SPEED, Float.MAX_VALUE);
        this.needsSync = true;
        this.playSound(MubbleSounds.KOOPA_SHELL_KICK, 0.4F, 1.0F);
        this.setOwner(owner);
        //TODO: add particles
        //TODO: reset rebound count? configurable?
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(MubbleItems.GREEN_KOOPA_SHELL);
    }

    @Override
    public Identifier getTexture() {
        return TEXTURE;
    }
}
