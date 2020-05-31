package hugman.mubble.objects.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.FeatureConfig;

public class HugeNetherMushroomFeatureConfig implements FeatureConfig
{
	public static final Codec<HugeNetherMushroomFeatureConfig> CODEC = RecordCodecBuilder.create((instance) ->
	{
		return instance.group(BlockState.field_24734.fieldOf("stem_state").forGetter((config) ->
		{
			return config.stemState;
		}), BlockState.field_24734.fieldOf("hat_state").forGetter((config) ->
		{
			return config.hatState;
		}), Codec.BOOL.fieldOf("flat_hat").withDefault(false).forGetter((config) ->
		{
			return config.flatHat;
		}), Codec.INT.fieldOf("foliage_radius").withDefault(2).forGetter((config) ->
		{
			return config.foliageRadius;
		}), BlockState.field_24734.fieldOf("decor_state").forGetter((config) ->
		{
			return config.decorationState;
		})).apply(instance, HugeNetherMushroomFeatureConfig::new);
	});
	public final BlockState stemState;
	public final BlockState hatState;
	public final boolean flatHat;
	public final int foliageRadius;
	public final BlockState decorationState;

	public HugeNetherMushroomFeatureConfig(BlockState stemState, BlockState hatState, boolean flatHat, int foliageRadius, BlockState decorationState)
	{
		this.stemState = stemState;
		this.hatState = hatState;
		this.flatHat = flatHat;
		this.foliageRadius = foliageRadius;
		this.decorationState = decorationState;
	}
}