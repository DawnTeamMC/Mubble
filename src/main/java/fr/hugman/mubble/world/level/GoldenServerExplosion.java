package fr.hugman.mubble.world.level;

import fr.hugman.mubble.sounds.MubbleSounds;
import fr.hugman.mubble.sounds.SoundConfig;
import fr.hugman.mubble.world.entity.item.collectible.CollectibleEntity;
import fr.hugman.mubble.world.item.MubbleItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ExplosionParticleInfo;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class GoldenServerExplosion implements Explosion {
    private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new ExplosionDamageCalculator();
    private static final WeightedList<ExplosionParticleInfo> DEFAULT_EXPLOSION_BLOCK_PARTICLES = WeightedList.<ExplosionParticleInfo>builder()
            .add(new ExplosionParticleInfo(ParticleTypes.POOF, 0.5F, 1.0F))
            .add(new ExplosionParticleInfo(ParticleTypes.SMOKE, 1.0F, 1.0F))
            .build();

    private final ServerLevel level;
    private final @Nullable Entity source;
    private final float radius;
    private final Vec3 center;
    private final BlockInteraction blockInteraction;
    private final DamageSource damageSource;
    private final ExplosionDamageCalculator damageCalculator;
    private final Map<Player, Vec3> hitPlayers = new HashMap();

    public GoldenServerExplosion(
            ServerLevel level,
            @Nullable Entity source,
            @Nullable DamageSource damageSource,
            @Nullable ExplosionDamageCalculator damageCalculator,
            Vec3 center,
            float radius,
            Explosion.BlockInteraction blockInteraction
    ) {
        this.level = level;
        this.source = source;
        this.radius = radius;
        this.center = center;
        this.blockInteraction = blockInteraction;
        this.damageSource = damageSource == null ? level.damageSources().explosion(this) : damageSource;
        this.damageCalculator = damageCalculator == null ? this.makeDamageCalculator(source) : damageCalculator;
    }

    private ExplosionDamageCalculator makeDamageCalculator(@Nullable final Entity source) {
        return source == null ? EXPLOSION_DAMAGE_CALCULATOR : new EntityBasedExplosionDamageCalculator(source);
    }

    @Override
    public ServerLevel level() {
        return this.level;
    }

    @Override
    public BlockInteraction getBlockInteraction() {
        return this.blockInteraction;
    }

    @Override
    public @Nullable LivingEntity getIndirectSourceEntity() {
        return Explosion.getIndirectSourceEntity(this.source);
    }

    @Override
    public @Nullable Entity getDirectSourceEntity() {
        return this.source;
    }

    @Override
    public float radius() {
        return this.radius;
    }

    @Override
    public Vec3 center() {
        return this.center;
    }

    @Override
    public boolean canTriggerBlocks() {
        if (this.blockInteraction != Explosion.BlockInteraction.TRIGGER_BLOCK) {
            return false;
        } else {
            return this.source != null && this.source.is(EntityType.BREEZE_WIND_CHARGE) ? this.level.getGameRules().get(GameRules.MOB_GRIEFING) : true;
        }
    }

    @Override
    public boolean shouldAffectBlocklikeEntities() {
        boolean mobGriefingEnabled = this.level.getGameRules().get(GameRules.MOB_GRIEFING);
        boolean isNotWindCharge = this.source == null || !this.source.is(EntityType.BREEZE_WIND_CHARGE) && !this.source.is(EntityType.WIND_CHARGE);
        return mobGriefingEnabled ? isNotWindCharge : this.blockInteraction.shouldAffectBlocklikeEntities() && isNotWindCharge;
    }

    public Map<Player, Vec3> getHitPlayers() {
        return hitPlayers;
    }

    private boolean interactsWithBlocks() {
        return this.blockInteraction != Explosion.BlockInteraction.KEEP;
    }

    public boolean isSmall() {
        return this.radius < 2.0F || !this.interactsWithBlocks();
    }

    private List<BlockPos> calculateAffectedPositions() {
        Set<BlockPos> toAffectSet = new HashSet();
        int size = 16;

        for (int xx = 0; xx < size; xx++) {
            for (int yy = 0; yy < size; yy++) {
                for (int zz = 0; zz < size; zz++) {
                    if (xx == 0 || xx == size - 1 || yy == 0 || yy == size - 1 || zz == 0 || zz == size - 1) {
                        double xd = xx / (size - 1.0F) * 2.0F - 1.0F;
                        double yd = yy / (size - 1.0F) * 2.0F - 1.0F;
                        double zd = zz / (size - 1.0F) * 2.0F - 1.0F;
                        double d = Math.sqrt(xd * xd + yd * yd + zd * zd);
                        xd /= d;
                        yd /= d;
                        zd /= d;
                        float remainingPower = this.radius * (0.7F + this.level.getRandom().nextFloat() * 0.6F);
                        double xp = this.center.x;
                        double yp = this.center.y;
                        double zp = this.center.z;

                        for (float stepSize = 0.3F; remainingPower > 0.0F; remainingPower -= 0.22500001F) {
                            BlockPos pos = BlockPos.containing(xp, yp, zp);
                            BlockState block = this.level.getBlockState(pos);
                            FluidState fluid = this.level.getFluidState(pos);
                            if (!this.level.isInWorldBounds(pos)) {
                                break;
                            }

                            Optional<Float> resistance = this.damageCalculator.getBlockExplosionResistance(this, this.level, pos, block, fluid);
                            if (resistance.isPresent()) {
                                remainingPower -= (resistance.get() + 0.3F) * 0.3F;
                            }

                            if (remainingPower > 0.0F && this.damageCalculator.shouldBlockExplode(this, this.level, pos, block, remainingPower)) {
                                toAffectSet.add(pos);
                            }

                            xp += xd * 0.3F;
                            yp += yd * 0.3F;
                            zp += zd * 0.3F;
                        }
                    }
                }
            }
        }

        return new ObjectArrayList<>(toAffectSet);
    }

    public int explode() {
        this.level.gameEvent(this.source, GameEvent.EXPLODE, this.center);
        List<BlockPos> toAffect = this.calculateAffectedPositions();
        this.hurtEntities();
        if (this.interactsWithBlocks()) {
            ProfilerFiller profiler = Profiler.get();
            profiler.push("golden_explosion_blocks");
            this.interactWithBlocks(toAffect);
            profiler.pop();
        }

        return toAffect.size();
    }

    private void interactWithBlocks(final List<BlockPos> targetBlocks) {
        Util.shuffle(targetBlocks, this.level.getRandom());

        for (BlockPos blockPos : targetBlocks) {
            var state = this.level.getBlockState(blockPos);
            if (!state.isAir() && this.getBlockInteraction() != BlockInteraction.TRIGGER_BLOCK) {
                // TODO add custom explosion block interaction. Loot parameters and stuff
                this.level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                Vec3 pos = CollectibleEntity.placePos(level, blockPos);
                if (pos != null) {
                    var itemStack = new ItemStack(MubbleItems.COIN);
                    CollectibleEntity entity = new CollectibleEntity(level, pos.x(), pos.y(), pos.z(), itemStack.copyWithCount(1));
                    entity.setCollectSound(new SoundConfig(MubbleSounds.COIN_COLLECT, 0.2f, 1.0f));
                    entity.setBounceSound(new SoundConfig(MubbleSounds.COIN_BOUNCE, 1.0f, 1.0f));
                    EntityType.createDefaultStackConfig(level, itemStack, null).accept(entity);
                    if (entity != null) {
                        entity.snapTo(entity.getX(), entity.getY(), entity.getZ(), 0.0f, 0.0F);
                        level.addFreshEntityWithPassengers(entity);
                    }
                }
            }
        }
    }

    private void hurtEntities() {
        if (!(this.radius < 1.0E-5F)) {
            float doubleRadius = this.radius * 2.0F;
            int x0 = Mth.floor(this.center.x - doubleRadius - 1.0);
            int x1 = Mth.floor(this.center.x + doubleRadius + 1.0);
            int y0 = Mth.floor(this.center.y - doubleRadius - 1.0);
            int y1 = Mth.floor(this.center.y + doubleRadius + 1.0);
            int z0 = Mth.floor(this.center.z - doubleRadius - 1.0);
            int z1 = Mth.floor(this.center.z + doubleRadius + 1.0);

            for (Entity entity : this.level.getEntities(this.source, new AABB(x0, y0, z0, x1, y1, z1))) {
                if (!entity.ignoreExplosion(this)) {
                    double dist = Math.sqrt(entity.distanceToSqr(this.center)) / doubleRadius;
                    if (!(dist > 1.0)) {
                        Vec3 entityOrigin = entity instanceof PrimedTnt ? entity.position() : entity.getEyePosition();
                        Vec3 direction = entityOrigin.subtract(this.center).normalize();
                        boolean shouldDamageEntity = this.damageCalculator.shouldDamageEntity(this, entity);
                        float knockbackMultiplier = this.damageCalculator.getKnockbackMultiplier(entity);
                        float exposure = !shouldDamageEntity && knockbackMultiplier == 0.0F ? 0.0F : getExposure(this.center, entity);

                        if (shouldDamageEntity) {
                            entity.hurtServer(this.level, this.damageSource, this.damageCalculator.getEntityDamageAmount(this, entity, exposure));
                        }

                        double knockbackResistance = entity instanceof LivingEntity livingEntity
                                ? livingEntity.getAttributeValue(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE)
                                : 0.0;
                        double knockbackPower = (1.0 - dist) * exposure * knockbackMultiplier * (1.0 - knockbackResistance);
                        Vec3 knockback = direction.scale(knockbackPower);
                        entity.push(knockback);
                        if (entity.is(EntityTypeTags.REDIRECTABLE_PROJECTILE) && entity instanceof Projectile projectile) {
                            projectile.setOwner(this.damageSource.getEntity());
                        } else if (entity instanceof Player player && !player.isSpectator() && (!player.isCreative() || !player.getAbilities().flying)) {
                            this.hitPlayers.put(player, knockback);
                        }

                        entity.onExplosionHit(this.source);
                    }
                }
            }
        }
    }

    public static float getExposure(final Vec3 center, final Entity entity) {
        AABB bb = entity.getBoundingBox();
        double xs = 1.0 / ((bb.maxX - bb.minX) * 2.0 + 1.0);
        double ys = 1.0 / ((bb.maxY - bb.minY) * 2.0 + 1.0);
        double zs = 1.0 / ((bb.maxZ - bb.minZ) * 2.0 + 1.0);
        double xOffset = (1.0 - Math.floor(1.0 / xs) * xs) / 2.0;
        double zOffset = (1.0 - Math.floor(1.0 / zs) * zs) / 2.0;
        if (!(xs < 0.0) && !(ys < 0.0) && !(zs < 0.0)) {
            int hits = 0;
            int count = 0;

            for (double xx = 0.0; xx <= 1.0; xx += xs) {
                for (double yy = 0.0; yy <= 1.0; yy += ys) {
                    for (double zz = 0.0; zz <= 1.0; zz += zs) {
                        double x = Mth.lerp(xx, bb.minX, bb.maxX);
                        double y = Mth.lerp(yy, bb.minY, bb.maxY);
                        double z = Mth.lerp(zz, bb.minZ, bb.maxZ);
                        Vec3 from = new Vec3(x + xOffset, y, z + zOffset);
                        if (entity.level().clip(new ClipContext(from, center, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getType() == HitResult.Type.MISS) {
                            hits++;
                        }

                        count++;
                    }
                }
            }

            return (float) hits / count;
        } else {
            return 0.0F;
        }
    }

    public static void create(
            ServerLevel level,
            @Nullable Entity source,
            double x,
            double y,
            double z,
            float r,
            Level.ExplosionInteraction blockInteraction
    ) {
        create(level,
                source,
                Explosion.getDefaultDamageSource(level, source),
                null,
                x,
                y,
                z,
                r,
                blockInteraction,
                ParticleTypes.EXPLOSION,
                ParticleTypes.EXPLOSION_EMITTER,
                DEFAULT_EXPLOSION_BLOCK_PARTICLES,
                SoundEvents.GENERIC_EXPLODE
        );
    }

    public static void create(
            ServerLevel level,
            @Nullable Entity source,
            @Nullable DamageSource damageSource,
            @Nullable ExplosionDamageCalculator damageCalculator,
            Vec3 boomPos,
            float r,
            Level.ExplosionInteraction blockInteraction
    ) {
        create(
                level,
                source,
                damageSource,
                damageCalculator,
                boomPos.x(),
                boomPos.y(),
                boomPos.z(),
                r,
                blockInteraction,
                ParticleTypes.EXPLOSION,
                ParticleTypes.EXPLOSION_EMITTER,
                DEFAULT_EXPLOSION_BLOCK_PARTICLES,
                MubbleSounds.GOLDEN_EXPLOSION
        );
    }

    public static void create(
            ServerLevel level,
            @Nullable Entity source,
            @Nullable DamageSource damageSource,
            @Nullable ExplosionDamageCalculator damageCalculator,
            double x,
            double y,
            double z,
            float r,
            Level.ExplosionInteraction interactionType
    ) {
        create(
                level,
                source,
                damageSource,
                damageCalculator,
                x,
                y,
                z,
                r,
                interactionType,
                ParticleTypes.EXPLOSION,
                ParticleTypes.EXPLOSION_EMITTER,
                DEFAULT_EXPLOSION_BLOCK_PARTICLES,
                MubbleSounds.GOLDEN_EXPLOSION
        );
    }

    public static void create(
            ServerLevel level,
            @Nullable Entity source,
            @Nullable DamageSource damageSource,
            @Nullable ExplosionDamageCalculator damageCalculator,
            double x,
            double y,
            double z,
            float r,
            Level.ExplosionInteraction interactionType,
            ParticleOptions smallExplosionParticles,
            ParticleOptions largeExplosionParticles,
            WeightedList<ExplosionParticleInfo> blockParticles,
            Holder<SoundEvent> explosionSound
    ) {
        Explosion.BlockInteraction blockInteraction = switch (interactionType) {
            case NONE -> Explosion.BlockInteraction.KEEP;
            case BLOCK -> level.getDestroyType(GameRules.BLOCK_EXPLOSION_DROP_DECAY);
            case MOB ->
                    level.getGameRules().get(GameRules.MOB_GRIEFING) ? level.getDestroyType(GameRules.MOB_EXPLOSION_DROP_DECAY) : Explosion.BlockInteraction.KEEP;
            case TNT -> level.getDestroyType(GameRules.TNT_EXPLOSION_DROP_DECAY);
            case TRIGGER -> Explosion.BlockInteraction.TRIGGER_BLOCK;
        };
        Vec3 center = new Vec3(x, y, z);
        GoldenServerExplosion explosion = new GoldenServerExplosion(level, source, damageSource, damageCalculator, center, r, blockInteraction);
        int blockCount = explosion.explode();
        ParticleOptions explosionParticle = explosion.isSmall() ? smallExplosionParticles : largeExplosionParticles;

        for (ServerPlayer player : level.players()) {
            if (player.distanceToSqr(center) < (double) 4096.0F) {
                Optional<Vec3> playerKnockback = Optional.ofNullable(explosion.getHitPlayers().get(player));
                player.connection.send(new ClientboundExplodePacket(center, r, blockCount, playerKnockback, explosionParticle, explosionSound, blockParticles));
            }
        }
    }
}
