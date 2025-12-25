package fr.hugman.mubble.world.entity.projectile;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.sounds.MubbleSounds;
import java.util.List;

import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import fr.hugman.mubble.world.item.MubbleItems;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class GreenKoopaShell extends KoopaShell {
    private static final Identifier TEXTURE = Mubble.id("textures/entity/green_koopa_shell.png");

    public GreenKoopaShell(EntityType<? extends GreenKoopaShell> entityType, Level level) {
        super(entityType, level, 5);
    }

    public GreenKoopaShell(Level level, double x, double y, double z) {
        this(MubbleEntityTypes.GREEN_KOOPA_SHELL, level);
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
