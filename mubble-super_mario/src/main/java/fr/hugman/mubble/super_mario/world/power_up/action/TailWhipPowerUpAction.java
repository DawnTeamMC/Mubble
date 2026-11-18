package fr.hugman.mubble.super_mario.world.power_up.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.keybind.MubbleKeyBindingsKeys;
import fr.hugman.mubble.super_mario.core.attachment.SuperMarioAttachmentTypes;
import fr.hugman.mubble.super_mario.world.level.block.HittableBlock;
import fr.hugman.mubble.world.entity.FriendlyFire;
import fr.hugman.mubble.world.power_up.PowerUpCharges;
import fr.hugman.mubble.world.power_up.PowerUpProperties;
import fr.hugman.mubble.world.power_up.action.PowerUpAction;
import fr.hugman.mubble.world.power_up.action.PowerUpActionType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * A spin of the tail that hits everything around the holder at once: the entities are hurt and thrown up, the
 * projectiles sent away and the hittable blocks hit, the ones that break when bumped breaking at once.
 *
 * @param radius        how far around the holder the spin reaches, in blocks
 * @param verticalReach how far above and below the holder's body the spin reaches, in blocks
 * @param damage        the damage dealt to each entity, the attack damage of the holder when absent
 * @param upwardSpeed   the vertical speed the entities hit are thrown up at, in blocks per tick
 * @param ignored       the entity types the spin never hits
 * @param particle      the particle thrown out in a ring around the holder
 */
public record TailWhipPowerUpAction(
        double radius,
        double verticalReach,
        Optional<Float> damage,
        double upwardSpeed,
        Optional<HolderSet<EntityType<?>>> ignored,
        Optional<Holder<SoundEvent>> sound,
        Optional<ParticleOptions> particle,
        PowerUpCharges charges
) implements PowerUpAction, TooltipProvider {
    public static final double DEFAULT_RADIUS = 3.0D;
    public static final double DEFAULT_VERTICAL_REACH = 1.5D;
    public static final double DEFAULT_UPWARD_SPEED = 0.6D;
    public static final PowerUpCharges DEFAULT_CHARGES = PowerUpCharges.cooldownRecharge(1, 10);

    private static final int RING_PARTICLES = 16;
    private static final double RING_SPEED = 0.35D;

    /** Sends a projectile straight away from whoever deflects it, level and at the speed it had. */
    private static final ProjectileDeflection AWAY = (projectile, deflector, random) -> {
        Vec3 away = projectile.position().subtract(deflector.position()).multiply(1.0D, 0.0D, 1.0D);
        if (away.lengthSqr() < 1.0E-6D) {
            away = deflector.getLookAngle().multiply(1.0D, 0.0D, 1.0D);
        }
        projectile.setDeltaMovement(away.normalize().scale(projectile.getDeltaMovement().length()));
        projectile.needsSync = true;
    };

    public static final MapCodec<TailWhipPowerUpAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.DOUBLE.optionalFieldOf("radius", DEFAULT_RADIUS).forGetter(TailWhipPowerUpAction::radius),
            Codec.DOUBLE.optionalFieldOf("vertical_reach", DEFAULT_VERTICAL_REACH).forGetter(TailWhipPowerUpAction::verticalReach),
            Codec.FLOAT.optionalFieldOf("damage").forGetter(TailWhipPowerUpAction::damage),
            Codec.DOUBLE.optionalFieldOf("upward_speed", DEFAULT_UPWARD_SPEED).forGetter(TailWhipPowerUpAction::upwardSpeed),
            RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).optionalFieldOf("ignored").forGetter(TailWhipPowerUpAction::ignored),
            SoundEvent.CODEC.optionalFieldOf("sound").forGetter(TailWhipPowerUpAction::sound),
            ParticleTypes.CODEC.optionalFieldOf("particle").forGetter(TailWhipPowerUpAction::particle),
            PowerUpCharges.CODEC.optionalFieldOf("charges", DEFAULT_CHARGES).forGetter(TailWhipPowerUpAction::charges)
    ).apply(instance, TailWhipPowerUpAction::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, TailWhipPowerUpAction> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, TailWhipPowerUpAction::radius,
            ByteBufCodecs.DOUBLE, TailWhipPowerUpAction::verticalReach,
            ByteBufCodecs.FLOAT.apply(ByteBufCodecs::optional), TailWhipPowerUpAction::damage,
            ByteBufCodecs.DOUBLE, TailWhipPowerUpAction::upwardSpeed,
            ByteBufCodecs.holderSet(Registries.ENTITY_TYPE).apply(ByteBufCodecs::optional), TailWhipPowerUpAction::ignored,
            SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), TailWhipPowerUpAction::sound,
            ParticleTypes.STREAM_CODEC.apply(ByteBufCodecs::optional), TailWhipPowerUpAction::particle,
            PowerUpCharges.STREAM_CODEC, TailWhipPowerUpAction::charges,
            TailWhipPowerUpAction::new
    );

    @Override
    public PowerUpActionType<?> getType() {
        return SuperMarioPowerUpActionTypes.TAIL_WHIP;
    }

    @Override
    public boolean canBeRefilled() {
        return true;
    }

    @Override
    public PowerUpProperties setUpProperties() {
        return this.charges.createProperties();
    }

    @Override
    public boolean canBeTriggered(Player player) {
        var properties = this.properties(player);
        if (!player.level().isClientSide()) {
            properties.doSoftChecks(player);
        }
        return properties.getChargeCount() > 0;
    }

    @Override
    public InteractionResult trigger(Player player) {
        var properties = this.properties(player);
        if (!(player.level() instanceof ServerLevel level)) {
            return InteractionResult.SUCCESS;
        }
        properties.useCharge();
        player.setAttached(SuperMarioAttachmentTypes.TAIL_WHIP, player.getAttachedOrElse(SuperMarioAttachmentTypes.TAIL_WHIP, 0) + 1);
        this.sound.ifPresent(sound -> level.playSound(null, player.getX(), player.getY(), player.getZ(), sound, SoundSource.PLAYERS, 0.5F, 1.0F));
        this.particle.ifPresent(particle -> this.ring(level, player, particle));

        var area = player.getBoundingBox().inflate(this.radius, this.verticalReach, this.radius);
        this.hitEntities(level, player, area);
        this.deflectProjectiles(level, player, area);
        this.hitBlocks(level, player);
        return InteractionResult.SUCCESS;
    }

    private void hitEntities(ServerLevel level, Player player, AABB area) {
        float damage = this.damage.orElseGet(() -> (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE));
        var source = level.damageSources().playerAttack(player);
        for (var target : level.getEntitiesOfClass(LivingEntity.class, area, entity -> this.canHit(player, entity))) {
            player.setLastHurtMob(target);
            target.hurtServer(level, source, damage);
            if (target.isAlive()) {
                target.setDeltaMovement(0.0D, this.upwardSpeed, 0.0D);
                target.hurtMarked = true;
            }
        }
    }

    private boolean canHit(Player player, LivingEntity target) {
        return target.isAlive() && target.isAttackable() && !target.isSpectator()
                && target.distanceToSqr(player.getX(), target.getY(), player.getZ()) <= this.radius * this.radius
                && !FriendlyFire.spares(player, target)
                && this.ignored.map(types -> !types.contains(target.getType().builtInRegistryHolder())).orElse(true);
    }

    private void deflectProjectiles(ServerLevel level, Player player, AABB area) {
        EntityReference<Entity> owner = EntityReference.of(player);
        for (var projectile : level.getEntitiesOfClass(Projectile.class, area, entity -> this.canDeflect(player, entity))) {
            projectile.deflect(AWAY, player, owner, true);
        }
    }

    private boolean canDeflect(Player player, Projectile projectile) {
        var owner = projectile.getOwner();
        return projectile.distanceToSqr(player.getX(), projectile.getY(), player.getZ()) <= this.radius * this.radius
                && (owner == null || !FriendlyFire.spares(player, owner));
    }

    private void hitBlocks(ServerLevel level, Player player) {
        var box = player.getBoundingBox();
        var from = BlockPos.containing(box.minX - this.radius, box.minY, box.minZ - this.radius);
        var to = BlockPos.containing(box.maxX + this.radius, box.maxY, box.maxZ + this.radius);
        double radiusSqr = this.radius * this.radius;
        for (var pos : BlockPos.betweenClosed(from, to)) {
            var state = level.getBlockState(pos);
            if (!(state.getBlock() instanceof HittableBlock hittable)) {
                continue;
            }
            Vec3 center = Vec3.atCenterOf(pos);
            if (center.distanceToSqr(player.getX(), center.y, player.getZ()) <= radiusSqr) {
                var direction = Direction.getApproximateNearest(player.position().subtract(center));
                hittable.onSmash(level, state, player, new BlockHitResult(center, direction, pos.immutable(), false));
            }
        }
    }

    private void ring(ServerLevel level, Player player, ParticleOptions particle) {
        for (int i = 0; i < RING_PARTICLES; i++) {
            double angle = i * Mth.TWO_PI / RING_PARTICLES;
            level.sendParticles(particle, player.getX(), player.getY() + player.getBbHeight() * 0.4D, player.getZ(),
                    0, Mth.cos((float) angle), 0.0D, Mth.sin((float) angle), RING_SPEED);
        }
    }

    private PowerUpProperties properties(Player player) {
        var properties = player.getPowerUpProperties();
        if (properties == null) {
            properties = this.setUpProperties();
            player.setPowerUpProperties(properties);
        }
        return properties;
    }

    @Override
    public boolean shouldSwingOtherHand() {
        return true;
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        this.getTranslationKey().ifPresent(key -> textConsumer.accept(Component.translatable(
                key + ".description",
                Component.keybind(MubbleKeyBindingsKeys.TRIGGER_POWER_UP)
        ).withStyle(ChatFormatting.GRAY)));
    }
}
