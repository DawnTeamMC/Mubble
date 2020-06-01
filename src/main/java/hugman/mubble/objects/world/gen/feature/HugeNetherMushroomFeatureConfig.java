package hugman.mubble.objects.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.FeatureConfig;

public class HugeNetherMushroomFeatureConfig implements FeatureConfig
{
	public static final Codec<HugeNetherMushroomFeatureConfig> CODEC = RecordCodecBuilder.create((instance) ->
	{
		return instance.group(BlockState.field_24734.fieldOf("stem_state").forGetter((config) ->
		{
			return config.stemState;
		}), Codec.INT.fieldOf("base_height").forGetter((config) ->
		{
			return config.baseHeight;
		}), Codec.INT.fieldOf("random_height").forGetter((config) ->
		{
			return config.randomHeight;
		}), BlockState.field_24734.fieldOf("hat_state").forGetter((config) ->
		{
			return config.hatState;
		}), Codec.INT.fieldOf("hat_size").withDefault(2).forGetter((config) ->
		{
			return config.hatSize;
		}), Codec.BOOL.fieldOf("flat_hat").withDefault(false).forGetter((config) ->
		{
			return config.flatHat;
		}), BlockState.field_24734.fieldOf("decor_state").forGetter((config) ->
		{
			return config.decorationState;
		}), Codec.DOUBLE.fieldOf("decor_chance").forGetter((config) ->
		{
			return config.decorationChance;
		}), Codec.DOUBLE.fieldOf("vine_chance").forGetter((config) ->
		{
			return config.vineChance;
		}), Codec.BOOL.fieldOf("upside_down").withDefault(false).forGetter((config) ->
		{
			return config.upsideDown;
		})).apply(instance, HugeNetherMushroomFeatureConfig::new);
	});
	public final BlockState stemState;
	public final int baseHeight;
	public final int randomHeight;
	public final BlockState hatState;
	public final int hatSize;
	public final boolean flatHat;
	public final BlockState decorationState;
	public final double decorationChance;
	public final double vineChance;
	public final boolean upsideDown;

	private HugeNetherMushroomFeatureConfig(BlockState stemState, int baseHeight, int randomHeight, BlockState hatState, int hatSize, boolean flatHat, BlockState decorationState, double decorationChance, double vineChance, boolean upsideDown)
	{
		this.stemState = stemState;
		this.baseHeight = baseHeight;
		this.randomHeight = randomHeight;
		this.hatState = hatState;
		this.hatSize = hatSize;
		this.flatHat = flatHat;
		this.decorationState = decorationState;
		this.decorationChance = decorationChance;
		this.vineChance = vineChance;
		this.upsideDown = upsideDown;
	}

	public HugeNetherMushroomFeatureConfig(int baseHeight, int randomHeight, BlockState hatState, int hatSize, BlockState decorationState, double decorationChance, double vineChance)
	{
		this.stemState = Blocks.MUSHROOM_STEM.getDefaultState();
		this.baseHeight = baseHeight;
		this.randomHeight = randomHeight;
		this.hatState = hatState;
		this.hatSize = hatSize;
		this.flatHat = false;
		this.decorationState = decorationState;
		this.decorationChance = decorationChance;
		this.vineChance = vineChance;
		this.upsideDown = false;
	}

	public HugeNetherMushroomFeatureConfig setUpsideDown()
	{
		return new HugeNetherMushroomFeatureConfig(this.stemState, this.baseHeight, this.randomHeight, this.hatState, this.hatSize, this.flatHat, this.decorationState, this.decorationChance, this.vineChance, true);
	}

	public HugeNetherMushroomFeatureConfig setFlatHat()
	{
		return new HugeNetherMushroomFeatureConfig(this.stemState, this.baseHeight, this.randomHeight, this.hatState, this.hatSize, true, this.decorationState, this.decorationChance, this.vineChance, this.upsideDown);
	}

	public HugeNetherMushroomFeatureConfig setFlatHat(int hatSize)
	{
		return new HugeNetherMushroomFeatureConfig(this.stemState, this.baseHeight, this.randomHeight, this.hatState, hatSize, true, this.decorationState, this.decorationChance, this.vineChance, this.upsideDown);
	}
}