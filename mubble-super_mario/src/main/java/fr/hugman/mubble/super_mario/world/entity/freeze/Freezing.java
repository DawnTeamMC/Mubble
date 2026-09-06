package fr.hugman.mubble.super_mario.world.entity.freeze;

import fr.hugman.mubble.super_mario.core.attachment.SuperMarioAttachmentTypes;
import fr.hugman.mubble.super_mario.tags.SuperMarioDamageTypeTags;
import fr.hugman.mubble.super_mario.tags.SuperMarioEntityTypeTags;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * Everything about entities trapped in a block of ice: freezing them, keeping them there, shoving
 * them around and letting them out.
 * <p>
 * A frozen entity is one carrying a {@link FreezeState} attachment and nothing else, which is what
 * makes any living entity freezable without each of them having to know about it.
 *
 * @see FreezeState
 */
public final class Freezing {
    /** How long a regular entity stays trapped, in ticks. */
    public static final int DURATION = 260;
    /** How long a {@link FreezeResistance#TOUGH} entity stays trapped, in ticks. */
    public static final int TOUGH_DURATION = 80;
    /** How much of the remaining freeze a single struggle from a frozen player melts away, in ticks. */
    public static final int STRUGGLE_RELIEF = 15;
    /** How much of the remaining freeze a single point of damage melts away, in ticks. */
    public static final int MELT_PER_DAMAGE = 20;
    /** How long the ice is left alone after a hit, in ticks, so that nothing grinds it away at once. */
    public static final int CRACK_COOLDOWN = 10;
    /** How long before the end the block of ice starts rattling, in ticks. */
    public static final int RATTLE_DURATION = 40;

    /** Hitbox volume, in cubic blocks, from which an entity counts as big: above a horse, below an iron golem. */
    public static final double BIG_HITBOX_VOLUME = 2.0D;

    /** Horizontal speed a shoved block of ice sets off at, in blocks per tick. */
    public static final double SLIDE_SPEED = 0.4D;
    /** Horizontal speed, in blocks per tick, from which running into a wall shatters the ice outright. */
    public static final double SHATTER_SPEED = 0.25D;
    /** How much horizontal speed a sliding block of ice keeps every tick while on the ground. */
    private static final double GROUND_DRAG = 0.94D;
    /** How much horizontal speed a falling block of ice keeps every tick. */
    private static final double AIR_DRAG = 0.98D;
    /** Horizontal speeds below this are rounded down to a standstill, so that ice does not creep. */
    private static final double SLIDE_EPSILON = 1.0e-3D;
    /** How far around the block of ice a player counts as pushing it: being solid, it is never overlapped. */
    private static final double PUSH_REACH = 0.2D;
    /** How far below the top of the ice a player has to stand to shove it rather than ride it. */
    private static final double PUSH_HEADROOM = 0.1D;
    /** How far below its feet an entity looks for the block of ice it might be standing on. */
    private static final double STANDING_REACH = 1.0e-3D;

    private static final int THAW_PARTICLE_COUNT = 24;
    private static final double THAW_PARTICLE_SPEED = 0.15D;
    private static final int CRACK_PARTICLE_COUNT = 6;
    private static final double CRACK_PARTICLE_SPEED = 0.05D;

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

    /** How well the entity holds up against being frozen. */
    public static FreezeResistance resistanceOf(Entity entity) {
        // a creative player is busy building, and a spectator is not even there to be hit
        if (entity instanceof Player player && (player.isCreative() || player.isSpectator())) {
            return FreezeResistance.IMMUNE;
        }
        if (entity.is(SuperMarioEntityTypeTags.FREEZE_IMMUNE)) {
            return FreezeResistance.IMMUNE;
        }
        return isBig(entity) ? FreezeResistance.TOUGH : FreezeResistance.NONE;
    }

    /** Whether the entity is standing on top of a block of ice someone else is trapped in. */
    public static boolean isStandingOnFrozen(Entity entity) {
        // the sweep is not free, so it is kept behind the cheap tell: on the ground with no block holding it up
        if (!entity.onGround() || entity.mainSupportingBlockPos.isPresent()) {
            return false;
        }
        var feet = entity.getBoundingBox();
        var underfoot = new AABB(feet.minX, feet.minY - STANDING_REACH, feet.minZ, feet.maxX, feet.minY, feet.maxZ);
        return !entity.level().getEntities(entity, underfoot, Freezing::isFrozen).isEmpty();
    }

    public static boolean isBig(Entity entity) {
        double width = entity.getBbWidth();
        return width * width * entity.getBbHeight() >= BIG_HITBOX_VOLUME;
    }

    /** @return how long the entity would stay trapped, in ticks, were it frozen right now */
    public static int durationFor(Entity entity) {
        return resistanceOf(entity) == FreezeResistance.TOUGH ? TOUGH_DURATION : DURATION;
    }

    /**
     * Traps an entity in a block of ice, unless it is one of those nothing can hold. The freeze
     * itself costs no health, on the way in or on the way out.
     *
     * @return how the entity took it, which tells whether it ended up frozen at all
     */
    public static FreezeResistance freeze(ServerLevel level, LivingEntity entity) {
        var resistance = resistanceOf(entity);
        if (resistance == FreezeResistance.IMMUNE) {
            return resistance;
        }
        freezeFor(level, entity, resistance == FreezeResistance.TOUGH ? TOUGH_DURATION : DURATION);
        return resistance;
    }

    /** Traps an entity in a block of ice for a set number of ticks, whatever it is. */
    public static void freezeFor(ServerLevel level, LivingEntity entity, int ticks) {
        entity.setAttached(SuperMarioAttachmentTypes.FREEZE, FreezeState.lasting(level.getGameTime(), ticks));
        entity.setDeltaMovement(Vec3.ZERO);
        entity.clearFire();
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.GLASS_PLACE, SoundSource.NEUTRAL, 0.8F, 1.2F);
    }

    /**
     * Ticks the freeze of a single entity, thawing it once its time is up. Server side only: the
     * clients hold the same {@link FreezeState} and work out where it is at on their own.
     */
    public static void tick(Entity entity) {
        var state = getState(entity);
        if (state == null || !(entity.level() instanceof ServerLevel level)) {
            return;
        }
        // an entity that has since turned unfreezable — a player switching to creative, say — is let out
        if (state.hasExpired(level.getGameTime()) || !entity.isAlive() || resistanceOf(entity) == FreezeResistance.IMMUNE) {
            thaw(level, entity);
            return;
        }
        shoveAroundBy(level, entity);
    }

    /** @return whether the entity was frozen in the first place */
    public static boolean thaw(ServerLevel level, Entity entity) {
        if (entity.removeAttached(SuperMarioAttachmentTypes.FREEZE) == null) {
            return false;
        }
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.GLASS_BREAK, SoundSource.NEUTRAL, 0.8F, 1.2F);
        level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.ICE.defaultBlockState()),
                entity.getX(), entity.getY(0.5D), entity.getZ(),
                THAW_PARTICLE_COUNT,
                entity.getBbWidth() / 2.0D, entity.getBbHeight() / 2.0D, entity.getBbWidth() / 2.0D,
                THAW_PARTICLE_SPEED);
        return true;
    }

    /**
     * Whether the block of ice takes this hit in place of whoever is inside it. It takes everything
     * but fire, which melts it, and what nothing is ever safe from — the void and {@code /kill}.
     */
    public static boolean shields(Entity entity, DamageSource source) {
        return isFrozen(entity)
                && !source.is(SuperMarioDamageTypeTags.MELTS_FROZEN_ENTITIES)
                && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
    }

    /**
     * Puts a hit into the block of ice rather than into whoever is inside it. A hit that empties the
     * ice does not thaw it here — the entity has to still count as frozen for the rest of this hit
     * to be turned away, so {@link #tick} lets it out on the next tick.
     */
    public static void absorb(ServerLevel level, Entity entity, DamageSource source, float amount) {
        var state = getState(entity);
        if (state == null) {
            return;
        }
        if (source.is(SuperMarioDamageTypeTags.MELTS_FROZEN_ENTITIES)) {
            // thawed right away, so that the fire that broke the ice still reaches what was inside it
            thaw(level, entity);
            return;
        }
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || entity.invulnerableTime > CRACK_COOLDOWN) {
            return;
        }
        entity.invulnerableTime = CRACK_COOLDOWN * 2;
        entity.setAttached(SuperMarioAttachmentTypes.FREEZE, state.shortenedBy(Math.max((int) (amount * MELT_PER_DAMAGE), 1)));
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.GLASS_HIT, SoundSource.NEUTRAL, 0.9F, 1.4F);
        level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.ICE.defaultBlockState()),
                entity.getX(), entity.getY(0.5D), entity.getZ(),
                CRACK_PARTICLE_COUNT,
                entity.getBbWidth() / 2.0D, entity.getBbHeight() / 2.0D, entity.getBbWidth() / 2.0D,
                CRACK_PARTICLE_SPEED);
        shoveAwayFrom(entity, source);
    }

    /**
     * Sends the block of ice skidding away from whatever just hit it. Vanilla knocks back only once a
     * hit has landed, and a hit the ice turns away never does, so the shove is dealt out here.
     */
    private static void shoveAwayFrom(Entity entity, DamageSource source) {
        var from = source.getSourcePosition();
        // a hit with nowhere to come from — drowning, starvation — says nothing about where to send it
        if (from != null) {
            shove(entity, entity.position().subtract(from));
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
     * Moves a frozen entity for the tick: it only falls and slides. A slide that meets a wall before
     * it has run itself out shatters against it.
     */
    public static void travelFrozen(LivingEntity entity) {
        var movement = entity.getDeltaMovement();
        double drag = entity.onGround() ? GROUND_DRAG : AIR_DRAG;
        entity.setDeltaMovement(movement.x() * drag, movement.y() - entity.getGravity(), movement.z() * drag);

        double speed = entity.getDeltaMovement().horizontalDistance();
        entity.move(MoverType.SELF, entity.getDeltaMovement());

        if (entity.horizontalCollision && speed >= SHATTER_SPEED && entity.level() instanceof ServerLevel level) {
            thaw(level, entity);
            return;
        }

        // rounding the last of a slide down keeps blocks of ice from drifting forever
        var slowed = entity.getDeltaMovement();
        if (Math.abs(slowed.x()) < SLIDE_EPSILON && Math.abs(slowed.z()) < SLIDE_EPSILON) {
            entity.setDeltaMovement(0.0D, slowed.y(), 0.0D);
        }
    }

    /** Sends the block of ice sliding whenever a player walks into its side. */
    private static void shoveAroundBy(ServerLevel level, Entity entity) {
        var hitBox = entity.getBoundingBox();
        var reach = hitBox.inflate(PUSH_REACH, 0.0D, PUSH_REACH);

        for (Player player : level.getEntitiesOfClass(Player.class, reach, EntitySelector.NO_SPECTATORS)) {
            // whoever stands on top of the ice rides it, they do not push it
            if (player.getBoundingBox().minY >= hitBox.maxY - PUSH_HEADROOM) {
                continue;
            }
            var heading = flatten(player.getKnownMovement());
            if (heading == null) {
                continue;
            }
            // ...and only when they are heading into the ice, rather than away from it
            if (hitBox.getCenter().subtract(player.position()).dot(heading) <= 0.0D) {
                continue;
            }
            // a player chasing the ice they just shoved must not keep resetting its speed
            if (entity.getDeltaMovement().dot(heading) >= SLIDE_SPEED - SLIDE_EPSILON) {
                return;
            }
            shove(entity, heading);
            return;
        }
    }

    /**
     * Sends the block of ice sliding along a heading, keeping whatever vertical motion it had. The
     * heading is taken as it comes, so a shove that lands at an angle sends the ice off at that angle.
     */
    public static void shove(Entity entity, Vec3 heading) {
        var flat = flatten(heading);
        if (flat == null) {
            return;
        }
        var push = flat.scale(SLIDE_SPEED);
        entity.setDeltaMovement(push.x(), entity.getDeltaMovement().y(), push.z());
        // a frozen player moves itself: the server has to tell it where it is being sent
        if (entity instanceof ServerPlayer player) {
            player.connection.send(new ClientboundSetEntityMotionPacket(player));
        }
    }

    /** @see #shove(Entity, Vec3) */
    public static void shove(Entity entity, Direction direction) {
        shove(entity, direction.getUnitVec3());
    }

    /**
     * @return {@code heading} flattened onto the ground and brought down to unit length, or
     *         {@code null} when there is not enough of it left to point anywhere
     */
    @Nullable
    private static Vec3 flatten(Vec3 heading) {
        if (heading.horizontalDistanceSqr() < SLIDE_EPSILON * SLIDE_EPSILON) {
            return null;
        }
        return new Vec3(heading.x(), 0.0D, heading.z()).normalize();
    }
}
