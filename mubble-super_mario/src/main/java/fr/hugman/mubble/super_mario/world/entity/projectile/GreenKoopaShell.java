package fr.hugman.mubble.super_mario.world.entity.projectile;

import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
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

        if (this.level().isClientSide() || this.isRemoved() || !this.isStopped()) {
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
        if (this.level() instanceof ServerLevel && !this.isStopped()) {
            this.setDeltaMovement(Vec3.ZERO);
            this.needsSync = true;
            this.playSound(SuperMarioSounds.KOOPA_SHELL_KICK, 0.4F, 1.2F);
            //TODO: add particles
        }
    }

    /**
     * Sends a shell at a standstill sliding again.
     *
     * @param kicker the entity that kicked the shell, which becomes its new owner
     */
    public void kickShell(Entity kicker) {
        if (!this.isStopped()) {
            return;
        }
        var direction = this.kickDirection(kicker).scale(TARGET_SPEED);
        this.setDeltaMovement(direction.x(), 0.0d, direction.z());
        this.grantStompImmunity();
        this.needsSync = true;
        this.playSound(SuperMarioSounds.KOOPA_SHELL_KICK, 0.4F, 1.0F);
        this.setOwner(kicker);
        //TODO: add particles
        //TODO: reset rebound count? configurable?
    }

    /**
     * Picks the direction a kick sends the shell towards. The kicker's own movement is preferred, so that
     * running into a shell sends it ahead; a kicker standing still pushes it away from itself instead.
     *
     * @return a horizontal unit vector, never zero, so that a kick can never leave the shell where it is
     */
    private Vec3 kickDirection(Entity kicker) {
        var movement = kicker.getKnownMovement();
        if (hasHorizontalDirection(movement)) {
            return horizontalDirection(movement);
        }
        var away = this.position().subtract(kicker.position());
        if (hasHorizontalDirection(away)) {
            return horizontalDirection(away);
        }
        // the kicker stands exactly on the shell: no direction can be derived from it, so any will do
        return this.randomHorizontalDirection();
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
