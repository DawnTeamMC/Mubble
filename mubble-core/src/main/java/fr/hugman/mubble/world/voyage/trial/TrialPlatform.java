package fr.hugman.mubble.world.voyage.trial;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

/**
 * The ground a trial gives the player to stand on.
 *
 * <p>Trial levels are generated void (see {@code docs/runtime-worlds.md}), so a trial that builds
 * nothing is a trial the player falls out of. For the POC that ground is a square slab of one block
 * type; the real thing will place a structure, and this is the seam it will replace.
 *
 * @param block  what the slab is made of
 * @param radius how far the slab extends from the centre, so the side is {@code 2 * radius + 1}
 * @param spawnY the height the player stands at; the slab itself is the layer directly below
 */
public record TrialPlatform(Block block, int radius, int spawnY) {
    /** Big enough to stand on and see the sky, which is all the POC needs. */
    public static final TrialPlatform DEFAULT = new TrialPlatform(Blocks.STONE, 8, 65);

    public static final Codec<TrialPlatform> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().optionalFieldOf("block", DEFAULT.block()).forGetter(TrialPlatform::block),
            Codec.intRange(0, 64).optionalFieldOf("radius", DEFAULT.radius()).forGetter(TrialPlatform::radius),
            // Voyage levels reuse the overworld dimension type, so they have the overworld's build
            // limits; one below the bottom and one below the top are reserved for the slab and for
            // headroom respectively.
            Codec.intRange(-63, 319).optionalFieldOf("spawn_y", DEFAULT.spawnY()).forGetter(TrialPlatform::spawnY)
    ).apply(instance, TrialPlatform::new));

    /**
     * {@return where the player should be put down}, on the middle of the slab.
     *
     * <p>The column is a parameter and not a field: {@code spawn_y} is a property of the trial, but
     * where in the level the trial sits is a property of the level. Today every trial gets its own
     * level and so is centred on the origin.
     */
    public Vec3 spawnPos(int centreX, int centreZ) {
        return new Vec3(centreX + 0.5D, this.spawnY, centreZ + 0.5D);
    }

    /** Builds the slab, one layer below {@link #spawnPos}. Meant for a freshly opened, empty level. */
    public void place(ServerLevel level, int centreX, int centreZ) {
        int y = this.spawnY - 1;
        for (int x = -this.radius; x <= this.radius; x++) {
            for (int z = -this.radius; z <= this.radius; z++) {
                level.setBlockAndUpdate(new BlockPos(centreX + x, y, centreZ + z), this.block.defaultBlockState());
            }
        }
    }
}
