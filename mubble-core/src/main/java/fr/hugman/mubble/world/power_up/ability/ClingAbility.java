package fr.hugman.mubble.world.power_up.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.Optional;

/**
 * The holder climbs the blocks of a set like a ladder, as long as they are pressed against one.
 *
 * @param reach how far from the side of the holder a block still counts as pressed against, in blocks
 */
public record ClingAbility(
        HolderSet<Block> blocks,
        float reach,
        Optional<Holder<SoundEvent>> sound,
        Optional<ParticleOptions> particle
) {
    public static final float DEFAULT_REACH = 0.05F;

    public ClingAbility {
        reach = Math.max(0.0F, reach);
    }

    public static final Codec<ClingAbility> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks").forGetter(ClingAbility::blocks),
            Codec.FLOAT.optionalFieldOf("reach", DEFAULT_REACH).forGetter(ClingAbility::reach),
            SoundEvent.CODEC.optionalFieldOf("sound").forGetter(ClingAbility::sound),
            ParticleTypes.CODEC.optionalFieldOf("particle").forGetter(ClingAbility::particle)
    ).apply(instance, ClingAbility::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClingAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderSet(Registries.BLOCK), ClingAbility::blocks,
            ByteBufCodecs.FLOAT, ClingAbility::reach,
            SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), ClingAbility::sound,
            ParticleTypes.STREAM_CODEC.apply(ByteBufCodecs::optional), ClingAbility::particle,
            ClingAbility::new
    );

    public static ClingAbility of(HolderSet<Block> blocks) {
        return new ClingAbility(blocks, DEFAULT_REACH, Optional.empty(), Optional.empty());
    }

    /**
     * @return the first block of the set beside the entity, floor and ceiling left out
     */
    public Optional<BlockPos> wallBeside(Entity entity) {
        AABB box = entity.getBoundingBox();
        AABB area = new AABB(box.minX - this.reach, box.minY + 1.0E-3D, box.minZ - this.reach,
                box.maxX + this.reach, box.maxY - 1.0E-3D, box.maxZ + this.reach);
        var level = entity.level();
        for (BlockPos pos : BlockPos.betweenClosed(BlockPos.containing(area.minX, area.minY, area.minZ), BlockPos.containing(area.maxX, area.maxY, area.maxZ))) {
            var state = level.getBlockState(pos);
            if (!state.is(this.blocks)) {
                continue;
            }
            var shape = state.getCollisionShape(level, pos, CollisionContext.of(entity));
            if (!shape.isEmpty() && shape.bounds().move(pos).intersects(area)) {
                return Optional.of(pos.immutable());
            }
        }
        return Optional.empty();
    }
}
