package fr.hugman.mubble.splatoon.world.item.weapon.param;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.codec.MubbleCodecs;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

/**
 * The droplets a bullet lets fall on its way, as {@code spl__BulletSplashShooterSpawnParam} in Splatoon 3. Lengths are
 * in units.
 *
 * <p>Shots go in cycles of {@code splitNum}. Over a cycle, a weapon drops {@code spawnNum} droplets per shot on
 * average, spread as evenly as whole droplets allow (1, 2, 1, 2... for 1.5). A shot's droplets fall one per
 * {@code spawnBetweenLength} of its flight, each at a random point of its stretch, starting {@code spawnNearestLength}
 * away from the muzzle. On top of these, the last shot of a cycle and the ones listed in
 * {@code forceSpawnNearestAddNumArray} (counted from 1) drop a bigger droplet right at {@code spawnNearestLength},
 * which keeps ink under the shooter's feet.
 *
 * @author Hugman
 * @since v4.0.0
 */
public record SplashSpawnParam(
        float spawnBetweenLength,
        float spawnNearestLength,
        float spawnNum,
        int splitNum,
        List<Integer> forceSpawnNearestAddNumArray
) {
    public static final SplashSpawnParam NONE = new SplashSpawnParam(0.0F, 0.0F, 0.0F, 1, List.of());

    public static final Codec<SplashSpawnParam> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("spawn_between_length").forGetter(SplashSpawnParam::spawnBetweenLength),
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("spawn_nearest_length").forGetter(SplashSpawnParam::spawnNearestLength),
            MubbleCodecs.NONNEGATIVE_FLOAT.fieldOf("spawn_num").forGetter(SplashSpawnParam::spawnNum),
            ExtraCodecs.POSITIVE_INT.fieldOf("split_num").forGetter(SplashSpawnParam::splitNum),
            ExtraCodecs.POSITIVE_INT.listOf().optionalFieldOf("force_spawn_nearest_add_num_array", List.of()).forGetter(SplashSpawnParam::forceSpawnNearestAddNumArray)
    ).apply(instance, SplashSpawnParam::new));

    public static final StreamCodec<ByteBuf, SplashSpawnParam> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, SplashSpawnParam::spawnBetweenLength,
            ByteBufCodecs.FLOAT, SplashSpawnParam::spawnNearestLength,
            ByteBufCodecs.FLOAT, SplashSpawnParam::spawnNum,
            ByteBufCodecs.VAR_INT, SplashSpawnParam::splitNum,
            ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.list()), SplashSpawnParam::forceSpawnNearestAddNumArray,
            SplashSpawnParam::new
    );

    /**
     * @param shot the index of the shot since the shooter started firing, from 0
     * @return how many droplets this shot lets fall on its way
     */
    public int dropletCount(int shot) {
        int inCycle = Math.floorMod(shot, this.splitNum);
        return (int) Math.floor((inCycle + 1) * this.spawnNum) - (int) Math.floor(inCycle * this.spawnNum);
    }

    /**
     * @param shot the index of the shot since the shooter started firing, from 0
     * @return whether this shot drops ink right in front of the shooter
     */
    public boolean dropsNearest(int shot) {
        if (this.spawnNum <= 0 && this.forceSpawnNearestAddNumArray.isEmpty()) {
            return false;
        }
        int number = Math.floorMod(shot, this.splitNum) + 1;
        return number == this.splitNum || this.forceSpawnNearestAddNumArray.contains(number);
    }
}
