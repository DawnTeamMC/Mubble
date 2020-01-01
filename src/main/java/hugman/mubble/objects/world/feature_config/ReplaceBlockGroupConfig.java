package hugman.mubble.objects.world.feature_config;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.FeatureConfig;

public class ReplaceBlockGroupConfig implements FeatureConfig
{
	public final BlockState target;
	public final BlockState state;
	public final int size;

	public ReplaceBlockGroupConfig(BlockState target, BlockState state, int sizeIn)
	{
		this.target = target;
		this.state = state;
		this.size = sizeIn;
	}

	public <T> Dynamic<T> serialize(DynamicOps<T> ops)
	{
		return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(ops.createString("size"), ops.createInt(this.size), ops.createString("target"), BlockState.serialize(ops, this.target).getValue(), ops.createString("state"), BlockState.serialize(ops, this.state).getValue())));
	}

	public static <T> ReplaceBlockGroupConfig deserialize(Dynamic<T> dynamic)
	{
		int size = dynamic.get("size").asInt(0);
		BlockState blockstate = dynamic.get("target").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
		BlockState blockstate1 = dynamic.get("state").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
		return new ReplaceBlockGroupConfig(blockstate, blockstate1, size);
	}
}