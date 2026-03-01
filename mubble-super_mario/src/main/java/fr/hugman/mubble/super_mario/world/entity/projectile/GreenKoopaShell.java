package fr.hugman.mubble.super_mario.world.entity.projectile;

import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import java.util.List;

import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.item.SuperMarioItems;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class GreenKoopaShell extends KoopaShell {
    private static final Identifier TEXTURE = SuperMario.id("textures/entity/green_koopa_shell.png");

    public GreenKoopaShell(EntityType<? extends GreenKoopaShell> entityType, Level level) {
        super(entityType, level, 5);
    }

    public GreenKoopaShell(Level level, double x, double y, double z) {
        this(SuperMarioEntityTypes.GREEN_KOOPA_SHELL, level);
        this.setPos(x, y, z);
    }

    public GreenKoopaShell(Level level, LivingEntity owner) {
        this(level, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ());
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
    public void onStompedBy(Entity entity) {
        super.onStompedBy(entity);
        if (this.level() instanceof ServerLevel) {
            if (!this.isStopped()) {
                this.setDeltaMovement(Vec3.ZERO);
                this.playSound(SuperMarioSounds.KOOPA_SHELL_KICK, 0.4F, 1.2F);
                //TODO: add particles
            }
        }
    }

    public void kickShell(Entity kicker) {
        var vec3d = kicker.getKnownMovement();
        if (vec3d.horizontalDistance() == 0.0D) {
            vec3d = this.position().subtract(kicker.position());
            if (vec3d.horizontalDistance() == 0.0D) {
                // Kicker is exactly at the shell's position; pick a random horizontal direction
                double angle = this.random.nextDouble() * 2 * Math.PI;
                vec3d = new Vec3(Math.cos(angle), 0.0D, Math.sin(angle));
            } else {
                vec3d = vec3d.normalize();
            }
        }
        this.setDeltaMovement(vec3d.x, 0.0d, vec3d.z);
        this.targetHorizontalSpeed(KoopaShell.TARGET_SPEED, Float.MAX_VALUE);
        this.needsSync = true;
        this.playSound(SuperMarioSounds.KOOPA_SHELL_KICK, 0.4F, 1.0F);
        this.setOwner(kicker);
        //TODO: add particles
        //TODO: reset rebound count? configurable?
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(SuperMarioItems.GREEN_KOOPA_SHELL);
    }

    @Override
    public Identifier getTexture() {
        return TEXTURE;
    }
}
