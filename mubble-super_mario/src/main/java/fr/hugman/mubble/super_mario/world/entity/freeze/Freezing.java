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
    public static final int DURATION = 260;
    /**
     * How long a {@link FreezeResistance#TOUGH} entity stays trapped, in ticks, before cracking the
     * ice open by itself.
     */
    public static final int TOUGH_DURATION = 80;
    /** How much of the remaining freeze a single struggle from a frozen player melts away, in ticks. */
    public static final int STRUGGLE_RELIEF = 15;
    /**
     * How much of the remaining freeze a single point of damage melts away, in ticks.
     * <p>
     * The ice takes every hit meant for whoever is inside it, so this is what turns it into a shield:
     * beating on it is how one gets a frozen entity out early, and thirteen points of damage is what a
     * whole {@link #DURATION} comes to.
     */
    public static final int MELT_PER_DAMAGE = 20;
    /**
     * How long the ice is left alone after a hit, in ticks.
     * <p>
     * It is what vanilla gives a hurt entity, and for the same reason: without it anything hurting
     * once a tick would grind a whole freeze away in under a second.
     */
    public static final int CRACK_COOLDOWN = 10;
    /**
     * How long before the end the block of ice starts rattling, in ticks.
     * <p>
     * It is the only warning anyone gets that whatever is in there is about to be let out, which is
     * why it lasts long enough to be worth reacting to.
     */
    public static final int RATTLE_DURATION = 40;

    /**
     * Hitbox volume, in cubic blocks, from which an entity counts as big.
     * <p>
     * It sits above a horse and well below an iron golem, which puts the usual mobs a player throws
     * ice balls at — and the players themselves — comfortably on the freezable side.
     */
    public static final double BIG_HITBOX_VOLUME = 2.0D;

    /** Horizontal speed a shoved block of ice sets off at, in blocks per tick. */
    public static final double SLIDE_SPEED = 0.4D;
    /**
     * Horizontal speed, in blocks per tick, from which running into a wall shatters the ice outright.
     * <p>
     * It sits below {@link #SLIDE_SPEED} but above what a slide has left after a few blocks, so a
     * shove straight into a wall breaks the ice open while one that has run its course does not.
     */
    public static final double SHATTER_SPEED = 0.25D;
    /**
     * How much horizontal speed a sliding block of ice keeps every tick while on the ground.
     * <p>
     * Enough friction to bring a full-speed shove to a halt within seven blocks or so: ice that never
     * slowed down would end up wherever the terrain happened to stop it.
     */
    private static final double GROUND_DRAG = 0.94D;
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

    /**
     * How well the entity holds up against being frozen.
     * <p>
     * Bosses are named one by one, since nothing about a hitbox tells them apart from any other
     * oversized mob. Everything else goes by its bulk, so that mobs no data pack ever heard of still
     * behave the way their size suggests.
     */
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

    public static boolean isBig(Entity entity) {
        double width = entity.getBbWidth();
        return width * width * entity.getBbHeight() >= BIG_HITBOX_VOLUME;
    }

    /** @return how long the entity would stay trapped, in ticks, were it frozen right now */
    public static int durationFor(Entity entity) {
        return resistanceOf(entity) == FreezeResistance.TOUGH ? TOUGH_DURATION : DURATION;
    }

    /**
     * Traps an entity in a block of ice, unless it is one of those nothing can hold.
     * <p>
     * Being stuck is the whole of it: the freeze itself costs the entity no health, on the way in or
     * on the way out. Whatever put it in there did its own damage already.
     *
     * @param level  the level both the entity and whatever froze it live in
     * @param entity the entity to freeze
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

    /**
     * Traps an entity in a block of ice for a set number of ticks, whatever it is.
     * <p>
     * Whatever it was doing stops right there: it is put out, brought to a standstill, and left to
     * wait the freeze out.
     */
    public static void freezeFor(ServerLevel level, LivingEntity entity, int ticks) {
        entity.setAttached(SuperMarioAttachmentTypes.FREEZE, FreezeState.lasting(level.getGameTime(), ticks));
        entity.setDeltaMovement(Vec3.ZERO);
        entity.clearFire();
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.GLASS_PLACE, SoundSource.NEUTRAL, 0.8F, 1.2F);
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
        // an entity that has since turned unfreezable — a player switching to creative, say — is let out
        if (state.hasExpired(level.getGameTime()) || !entity.isAlive() || resistanceOf(entity) == FreezeResistance.IMMUNE) {
            thaw(level, entity);
            return;
        }
        shoveAroundBy(level, entity);
    }

    /**
     * Lets the entity out of the ice, at no cost to it.
     *
     * @return whether the entity was frozen in the first place
     */
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
     * Whether the block of ice takes this hit in place of whoever is inside it.
     * <p>
     * It takes very nearly everything: a frozen entity is behind a shield rather than merely stuck,
     * and what gets thrown at it goes into breaking the ice open instead. The two ways past are fire,
     * which melts the ice rather than being stopped by it, and the handful of damage types nothing is
     * ever safe from — the void and {@code /kill} — which would otherwise leave an entity in the ice
     * that no longer has any business being alive.
     */
    public static boolean shields(Entity entity, DamageSource source) {
        return isFrozen(entity)
                && !source.is(SuperMarioDamageTypeTags.MELTS_FREEZE)
                && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
    }

    /**
     * Puts a hit into the block of ice rather than into whoever is inside it.
     * <p>
     * Nothing is thawed on the spot even when the hit is the last one the ice had in it: the entity
     * has to still count as frozen for the rest of this hit to be turned away, so the freeze is only
     * run down to nothing here and {@link #tick} lets it out on the next tick.
     */
    public static void absorb(ServerLevel level, Entity entity, DamageSource source, float amount) {
        var state = getState(entity);
        if (state == null) {
            return;
        }
        if (source.is(SuperMarioDamageTypeTags.MELTS_FREEZE)) {
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
     * Sends the block of ice skidding away from whatever just hit it.
     * <p>
     * Vanilla hands out its knockback only once a hit has landed, and a hit the ice turns away never
     * does, so a shielded entity would take a punch without budging an inch. The shove is dealt out
     * here instead, and it is the same axis-snapped one a player walking into the ice gets rather than
     * whatever angle the blow came in at — which is what lets a well-aimed punch send it into a wall.
     */
    private static void shoveAwayFrom(Entity entity, DamageSource source) {
        var from = source.getSourcePosition();
        if (from == null) {
            return;
        }
        var away = entity.position().subtract(from);
        // a hit landing right on top of the ice says nothing about which way to send it
        if (away.horizontalDistanceSqr() < SLIDE_EPSILON * SLIDE_EPSILON) {
            return;
        }
        shove(entity, Direction.getApproximateNearest(away.x(), 0.0D, away.z()));
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
     * It only falls and slides: an entity in a block of ice has no say in where it goes. A slide runs
     * itself out over a few blocks, and one that meets a wall before it has done so shatters against
     * it.
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
