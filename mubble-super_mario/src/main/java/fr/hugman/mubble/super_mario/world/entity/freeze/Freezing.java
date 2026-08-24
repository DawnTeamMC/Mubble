package fr.hugman.mubble.super_mario.world.entity.freeze;

import fr.hugman.mubble.super_mario.core.attachment.SuperMarioAttachmentTypes;
import fr.hugman.mubble.super_mario.references.SuperMarioDamageTypeIds;
import fr.hugman.mubble.super_mario.tags.SuperMarioEntityTypeTags;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * Everything about entities trapped in a block of ice: freezing them, keeping them there, shoving
 * them around and letting them out.
 * <p>
 * A frozen entity is one carrying a {@link FreezeState} attachment. Nothing else marks it, which is
 * what makes any living entity freezable without each of them having to know about it: the mixins on
 * {@code Entity} and {@code LivingEntity} read that attachment back and hand over to the methods
 * here.
 *
 * @see FreezeState
 */
public final class Freezing {
    /** How long a regular entity stays trapped, in ticks. */
    public static final int DURATION = 200;
    /**
     * How long a {@link FreezeResistance#TOUGH} entity stays trapped, in ticks, before cracking the
     * ice open by itself.
     */
    public static final int TOUGH_DURATION = 60;
    /** What breaking out of the ice by force costs a {@link FreezeResistance#TOUGH} entity. */
    public static final float TOUGH_THAW_DAMAGE = 4.0F;
    /** What an ice ball does to a {@link FreezeResistance#IMMUNE} entity, having nothing else to do. */
    public static final float IMMUNE_DAMAGE = 4.0F;
    /** How much of the remaining freeze a single struggle from a frozen player melts away, in ticks. */
    public static final int STRUGGLE_RELIEF = 15;

    /**
     * Hitbox volume, in cubic blocks, from which an entity counts as big.
     * <p>
     * It sits above a horse and well below an iron golem, which puts the usual mobs a player throws
     * ice balls at — and the players themselves — comfortably on the freezable side.
     */
    public static final double BIG_HITBOX_VOLUME = 2.0D;

    /** Horizontal speed a shoved block of ice sets off at, in blocks per tick. */
    public static final double SLIDE_SPEED = 0.4D;
    /** How much horizontal speed a sliding block of ice keeps every tick while on the ground. */
    private static final double GROUND_DRAG = 0.995D;
    /** How much horizontal speed a falling block of ice keeps every tick. */
    private static final double AIR_DRAG = 0.98D;
    /** Horizontal speeds below this are rounded down to a standstill, so that ice does not creep. */
    private static final double SLIDE_EPSILON = 1.0e-3D;
    /**
     * How far around the block of ice a player is still counted as pushing it.
     * <p>
     * Frozen entities are solid, so a player walking into one never overlaps it: without this margin
     * there would be nothing to find.
     */
    private static final double PUSH_REACH = 0.2D;
    /** How far below the top of the ice a player has to stand to shove it rather than ride it. */
    private static final double PUSH_HEADROOM = 0.1D;

    private static final int THAW_PARTICLE_COUNT = 24;
    private static final double THAW_PARTICLE_SPEED = 0.15D;

    private Freezing() {
    }

    @Nullable
    public static FreezeState getState(Entity entity) {
        return entity.getAttached(SuperMarioAttachmentTypes.FREEZE);
    }

    public static boolean isFrozen(Entity entity) {
        return getState(entity) != null;
    }

    /**
     * @return how much longer the entity stays frozen, in ticks, or {@code 0} when it is not frozen
     */
    public static int getRemainingTicks(Entity entity) {
        var state = getState(entity);
        return state == null ? 0 : state.remaining(entity.level().getGameTime());
    }

    /**
     * How well the entity holds up against being frozen.
     * <p>
     * Bosses are named one by one, since nothing about a hitbox tells them apart from any other
     * oversized mob. Everything else goes by its bulk, so that mobs no data pack ever heard of still
     * behave the way their size suggests.
     */
    public static FreezeResistance resistanceOf(Entity entity) {
        if (entity.is(SuperMarioEntityTypeTags.FREEZE_IMMUNE)) {
            return FreezeResistance.IMMUNE;
        }
        return isBig(entity) ? FreezeResistance.TOUGH : FreezeResistance.NONE;
    }

    public static boolean isBig(Entity entity) {
        double width = entity.getBbWidth();
        return width * width * entity.getBbHeight() >= BIG_HITBOX_VOLUME;
    }

    /**
     * Traps an entity in a block of ice, or hurts it when it is too big to be trapped.
     *
     * @param level    the level both the entity and whatever froze it live in
     * @param entity   the entity to freeze
     * @param cause    the damage the entity takes should it turn out to be unfreezable
     * @return how the entity took it, which tells whether it ended up frozen at all
     */
    public static FreezeResistance freeze(ServerLevel level, LivingEntity entity, DamageSource cause) {
        var resistance = resistanceOf(entity);
        if (resistance == FreezeResistance.IMMUNE) {
            entity.hurtServer(level, cause, IMMUNE_DAMAGE);
            return resistance;
        }

        int duration = resistance == FreezeResistance.TOUGH ? TOUGH_DURATION : DURATION;
        entity.setAttached(SuperMarioAttachmentTypes.FREEZE, FreezeState.lasting(level.getGameTime(), duration));
        entity.setDeltaMovement(Vec3.ZERO);
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.GLASS_PLACE, SoundSource.NEUTRAL, 0.8F, 1.2F);
        return resistance;
    }

    /**
     * Ticks the freeze of a single entity, thawing it once its time is up.
     * <p>
     * Only the server counts: the clients hold the very same {@link FreezeState} and work out where
     * it is at on their own, and letting them thaw an entity themselves would only make them guess
     * ahead of the removal the server is about to send them anyway.
     */
    public static void tick(Entity entity) {
        var state = getState(entity);
        if (state == null || !(entity.level() instanceof ServerLevel level)) {
            return;
        }
        if (state.hasExpired(level.getGameTime()) || !entity.isAlive()) {
            thaw(level, entity);
            return;
        }
        shoveAroundBy(level, entity);
    }

    /**
     * Lets the entity out of the ice, hurting it when it had to break out by force.
     */
    public static void thaw(ServerLevel level, Entity entity) {
        if (entity.removeAttached(SuperMarioAttachmentTypes.FREEZE) == null) {
            return;
        }
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.GLASS_BREAK, SoundSource.NEUTRAL, 0.8F, 1.2F);
        level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.ICE.defaultBlockState()),
                entity.getX(), entity.getY(0.5D), entity.getZ(),
                THAW_PARTICLE_COUNT,
                entity.getBbWidth() / 2.0D, entity.getBbHeight() / 2.0D, entity.getBbWidth() / 2.0D,
                THAW_PARTICLE_SPEED);

        if (entity instanceof LivingEntity living && resistanceOf(entity) == FreezeResistance.TOUGH) {
            living.hurtServer(level, level.damageSources().source(SuperMarioDamageTypeIds.ICEBALL), TOUGH_THAW_DAMAGE);
        }
    }

    /**
     * Melts a slice off the remaining freeze, which is what a frozen player smashing their movement
     * keys buys them.
     *
     * @return whether the entity was frozen in the first place
     */
    public static boolean struggle(Entity entity) {
        var state = getState(entity);
        if (state == null) {
            return false;
        }
        entity.setAttached(SuperMarioAttachmentTypes.FREEZE, state.shortenedBy(STRUGGLE_RELIEF));
        return true;
    }

    /**
     * Moves a frozen entity for the tick, in place of whatever it would have done on its own.
     * <p>
     * It only falls and slides: an entity in a block of ice has no say in where it goes, and the
     * little drag the ice has is what makes a shove carry it several blocks away.
     */
    public static void travelFrozen(LivingEntity entity) {
        var movement = entity.getDeltaMovement();
        double drag = entity.onGround() ? GROUND_DRAG : AIR_DRAG;
        entity.setDeltaMovement(movement.x() * drag, movement.y() - entity.getGravity(), movement.z() * drag);
        entity.move(MoverType.SELF, entity.getDeltaMovement());

        // rounding the last of a slide down keeps blocks of ice from drifting forever
        var slowed = entity.getDeltaMovement();
        if (Math.abs(slowed.x()) < SLIDE_EPSILON && Math.abs(slowed.z()) < SLIDE_EPSILON) {
            entity.setDeltaMovement(0.0D, slowed.y(), 0.0D);
        }
    }

    /**
     * Sends the block of ice sliding whenever a player walks into its side.
     * <p>
     * The shove is snapped to the axis the player is heading along, so that the ice always sets off
     * straight ahead rather than at whatever angle it was walked into.
     */
    private static void shoveAroundBy(ServerLevel level, Entity entity) {
        var hitBox = entity.getBoundingBox();
        var reach = hitBox.inflate(PUSH_REACH, 0.0D, PUSH_REACH);

        for (Player player : level.getEntitiesOfClass(Player.class, reach, EntitySelector.NO_SPECTATORS)) {
            // whoever stands on top of the ice rides it, they do not push it
            if (player.getBoundingBox().minY >= hitBox.maxY - PUSH_HEADROOM) {
                continue;
            }
            var heading = player.getKnownMovement();
            if (heading.horizontalDistanceSqr() < SLIDE_EPSILON * SLIDE_EPSILON) {
                continue;
            }
            var direction = Direction.getApproximateNearest(heading.x(), 0.0D, heading.z());
            // ...and only when they are heading into the ice, rather than away from it
            if (hitBox.getCenter().subtract(player.position()).dot(direction.getUnitVec3()) <= 0.0D) {
                continue;
            }
            // a player chasing the ice they just shoved must not keep resetting its speed
            if (entity.getDeltaMovement().dot(direction.getUnitVec3()) >= SLIDE_SPEED - SLIDE_EPSILON) {
                return;
            }
            shove(entity, direction);
            return;
        }
    }

    /**
     * Sends the block of ice sliding along a direction, keeping whatever vertical motion it had.
     */
    public static void shove(Entity entity, Direction direction) {
        entity.setDeltaMovement(direction.getStepX() * SLIDE_SPEED, entity.getDeltaMovement().y(), direction.getStepZ() * SLIDE_SPEED);
        // a frozen player moves itself: the server has to tell it where it is being sent
        if (entity instanceof ServerPlayer player) {
            player.connection.send(new ClientboundSetEntityMotionPacket(player));
        }
    }
}
