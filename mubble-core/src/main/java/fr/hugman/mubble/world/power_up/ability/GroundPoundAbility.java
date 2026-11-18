package fr.hugman.mubble.world.power_up.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.world.entity.FriendlyFire;
import fr.hugman.mubble.world.level.block.PoundableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

/**
 * A pound straight down started by a fresh press of sneak in the air, hanging a moment before it drops.
 *
 * @param hangTicks     how long the holder hangs before dropping
 * @param dropSpeed     the speed of the drop, in blocks per tick
 * @param minHeight     how high above the ground the holder has to be to start a pound, in blocks
 * @param reach         how far around the feet the impact reaches, in blocks
 * @param kills         the entity types the impact defeats outright, none when absent
 * @param damagePerBlock the damage the impact deals to the others for each block fallen
 * @param maxDamage     the most damage the impact deals to the others
 * @param particle      the particle thrown out in a ring from the feet on landing
 * @param landSound     the sound played on landing
 * @param statue        the statue the holder turns into while pounding, if the form has one
 */
public record GroundPoundAbility(
        int hangTicks,
        float dropSpeed,
        float minHeight,
        float reach,
        Optional<HolderSet<EntityType<?>>> kills,
        float damagePerBlock,
        float maxDamage,
        Optional<ParticleOptions> particle,
        Optional<Holder<SoundEvent>> landSound,
        Optional<Statue> statue
) {
    public GroundPoundAbility {
        hangTicks = Math.max(0, hangTicks);
        dropSpeed = Math.max(0.0F, dropSpeed);
        minHeight = Math.max(0.0F, minHeight);
        reach = Math.max(0.0F, reach);
        damagePerBlock = Math.max(0.0F, damagePerBlock);
        maxDamage = Math.max(0.0F, maxDamage);
    }


    public static final int DEFAULT_HANG_TICKS = 5;
    public static final float DEFAULT_DROP_SPEED = 1.2F;
    public static final float DEFAULT_MIN_HEIGHT = 1.0F;
    public static final float DEFAULT_REACH = 1.5F;
    public static final float DEFAULT_DAMAGE_PER_BLOCK = 2.0F;
    public static final float DEFAULT_MAX_DAMAGE = 40.0F;

    public static final Codec<GroundPoundAbility> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("hang_ticks", DEFAULT_HANG_TICKS).forGetter(GroundPoundAbility::hangTicks),
            Codec.FLOAT.optionalFieldOf("drop_speed", DEFAULT_DROP_SPEED).forGetter(GroundPoundAbility::dropSpeed),
            Codec.FLOAT.optionalFieldOf("min_height", DEFAULT_MIN_HEIGHT).forGetter(GroundPoundAbility::minHeight),
            Codec.FLOAT.optionalFieldOf("reach", DEFAULT_REACH).forGetter(GroundPoundAbility::reach),
            RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).optionalFieldOf("kills").forGetter(GroundPoundAbility::kills),
            Codec.FLOAT.optionalFieldOf("damage_per_block", DEFAULT_DAMAGE_PER_BLOCK).forGetter(GroundPoundAbility::damagePerBlock),
            Codec.FLOAT.optionalFieldOf("max_damage", DEFAULT_MAX_DAMAGE).forGetter(GroundPoundAbility::maxDamage),
            ParticleTypes.CODEC.optionalFieldOf("particle").forGetter(GroundPoundAbility::particle),
            SoundEvent.CODEC.optionalFieldOf("land_sound").forGetter(GroundPoundAbility::landSound),
            Statue.CODEC.optionalFieldOf("statue").forGetter(GroundPoundAbility::statue)
    ).apply(instance, GroundPoundAbility::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, GroundPoundAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, GroundPoundAbility::hangTicks,
            ByteBufCodecs.FLOAT, GroundPoundAbility::dropSpeed,
            ByteBufCodecs.FLOAT, GroundPoundAbility::minHeight,
            ByteBufCodecs.FLOAT, GroundPoundAbility::reach,
            ByteBufCodecs.holderSet(Registries.ENTITY_TYPE).apply(ByteBufCodecs::optional), GroundPoundAbility::kills,
            ByteBufCodecs.FLOAT, GroundPoundAbility::damagePerBlock,
            ByteBufCodecs.FLOAT, GroundPoundAbility::maxDamage,
            ParticleTypes.STREAM_CODEC.apply(ByteBufCodecs::optional), GroundPoundAbility::particle,
            SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), GroundPoundAbility::landSound,
            Statue.STREAM_CODEC.apply(ByteBufCodecs::optional), GroundPoundAbility::statue,
            GroundPoundAbility::new
    );

    public static GroundPoundAbility of(Optional<Statue> statue) {
        return new GroundPoundAbility(DEFAULT_HANG_TICKS, DEFAULT_DROP_SPEED, DEFAULT_MIN_HEIGHT, DEFAULT_REACH, Optional.empty(),
                DEFAULT_DAMAGE_PER_BLOCK, DEFAULT_MAX_DAMAGE, Optional.empty(), Optional.empty(), statue);
    }

    public boolean defeatsOutright(Entity entity) {
        return this.kills
                .map(types -> types.contains(entity.getType().builtInRegistryHolder()))
                .orElse(false);
    }

    /** Like a falling anvil: so much per block fallen, up to a cap. */
    public float damageFor(double blocksFallen) {
        return Mth.clamp((float) Math.floor(blocksFallen * this.damagePerBlock), 0.0F, this.maxDamage);
    }

    public void hurtAround(ServerLevel level, Player pounder, Vec3 feet, double blocksFallen) {
        float damage = this.damageFor(blocksFallen);
        var area = new AABB(feet.x - this.reach, feet.y - 1.0D, feet.z - this.reach, feet.x + this.reach, feet.y + pounder.getBbHeight(), feet.z + this.reach);
        var source = level.damageSources().playerAttack(pounder);
        for (var target : level.getEntitiesOfClass(LivingEntity.class, area, entity -> mayHurt(pounder, entity))) {
            float amount = this.defeatsOutright(target) ? Float.MAX_VALUE : damage;
            if (amount > 0.0F) {
                pounder.setLastHurtMob(target);
                target.hurtServer(level, source, amount);
            }
        }
    }

    private static boolean mayHurt(Player pounder, LivingEntity target) {
        return target != pounder && target.isAlive() && target.isAttackable() && !target.isSpectator() && !FriendlyFire.spares(pounder, target);
    }

    /** Pounds the blocks under the feet and around them, see {@link PoundableBlock}. */
    public void poundBlocks(ServerLevel level, Player pounder, Vec3 feet) {
        int y = Mth.floor(feet.y - 0.1D);
        int reachBlocks = Mth.ceil(this.reach);
        var from = BlockPos.containing(feet.x - reachBlocks, y, feet.z - reachBlocks);
        var to = BlockPos.containing(feet.x + reachBlocks, y, feet.z + reachBlocks);
        double reachSqr = this.reach * this.reach;
        for (var pos : BlockPos.betweenClosed(from, to)) {
            double dx = pos.getX() + 0.5D - feet.x;
            double dz = pos.getZ() + 0.5D - feet.z;
            var state = level.getBlockState(pos);
            if (dx * dx + dz * dz <= reachSqr && state.getBlock() instanceof PoundableBlock poundable) {
                poundable.onPounded(level, state, pos.immutable(), pounder);
            }
        }
    }

    /**
     * The stone a pounding holder turns into.
     *
     * @param maxTicks      how long the statue lasts at most
     * @param invulnerable  whether the holder is invulnerable as a statue
     * @param vulnerableTo  the damage types that still hurt an invulnerable statue
     */
    public record Statue(
            int maxTicks,
            boolean invulnerable,
            TagKey<DamageType> vulnerableTo,
            Optional<ParticleOptions> enterParticle,
            Optional<ParticleOptions> leaveParticle,
            Optional<Holder<SoundEvent>> enterSound,
            Optional<Holder<SoundEvent>> leaveSound
    ) {
        public Statue {
            maxTicks = Math.max(0, maxTicks);
        }

        public static final int DEFAULT_MAX_TICKS = 240;

        public static final Codec<Statue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.optionalFieldOf("max_ticks", DEFAULT_MAX_TICKS).forGetter(Statue::maxTicks),
                Codec.BOOL.optionalFieldOf("invulnerable", true).forGetter(Statue::invulnerable),
                TagKey.hashedCodec(Registries.DAMAGE_TYPE).optionalFieldOf("vulnerable_to", DamageTypeTags.BYPASSES_INVULNERABILITY).forGetter(Statue::vulnerableTo),
                ParticleTypes.CODEC.optionalFieldOf("enter_particle").forGetter(Statue::enterParticle),
                ParticleTypes.CODEC.optionalFieldOf("leave_particle").forGetter(Statue::leaveParticle),
                SoundEvent.CODEC.optionalFieldOf("enter_sound").forGetter(Statue::enterSound),
                SoundEvent.CODEC.optionalFieldOf("leave_sound").forGetter(Statue::leaveSound)
        ).apply(instance, Statue::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, Statue> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Statue::maxTicks,
                ByteBufCodecs.BOOL, Statue::invulnerable,
                TagKey.streamCodec(Registries.DAMAGE_TYPE), Statue::vulnerableTo,
                ParticleTypes.STREAM_CODEC.apply(ByteBufCodecs::optional), Statue::enterParticle,
                ParticleTypes.STREAM_CODEC.apply(ByteBufCodecs::optional), Statue::leaveParticle,
                SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), Statue::enterSound,
                SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), Statue::leaveSound,
                Statue::new
        );

        public static Statue of(int maxTicks) {
            return new Statue(maxTicks, true, DamageTypeTags.BYPASSES_INVULNERABILITY, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        }
    }
}
