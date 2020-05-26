package hugman.mubble.objects.world.feature_config;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import hugman.mubble.init.MubbleBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.predicate.block.BlockPredicate;
import net.minecraft.world.gen.feature.FeatureConfig;

public class MubbleOreFeatureConfig implements FeatureConfig
{
	public final MubbleOreFeatureConfig.Target target;
	public final int size;
	public final BlockState state;
	
	public MubbleOreFeatureConfig(MubbleOreFeatureConfig.Target target, BlockState state, int size)
	{
		this.size = size;
		this.state = state;
		this.target = target;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> Dynamic<T> serialize(DynamicOps<T> ops)
	{
		return new Dynamic(ops, ops.createMap(ImmutableMap.of(ops.createString("size"), ops.createInt(this.size), ops.createString("target"), ops.createString(this.target.getName()), ops.createString("state"), BlockState.serialize(ops, this.state).getValue())));
	}
	
	public static MubbleOreFeatureConfig deserialize(Dynamic<?> dynamic)
	{
		int i = dynamic.get("size").asInt(0);
		MubbleOreFeatureConfig.Target target = MubbleOreFeatureConfig.Target.byName(dynamic.get("target").asString(""));
		BlockState blockState = (BlockState) dynamic.get("state").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
		return new MubbleOreFeatureConfig(target, blockState, i);
	}
	
	public static enum Target
	{
		PERMAROCK("permarock", new BlockPredicate(MubbleBlocks.PERMAROCK));
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private static final Map<String, MubbleOreFeatureConfig.Target> nameMap = (Map) Arrays.stream(values()).collect(Collectors.toMap(MubbleOreFeatureConfig.Target::getName, (target) -> {
			return target;
		}));
		private final String name;
		private final Predicate<BlockState> predicate;
		
		private Target(String name, Predicate<BlockState> predicate)
		{
			this.name = name;
			this.predicate = predicate;
		}
		
		public String getName()
		{
			return this.name;
		}
		
		public static MubbleOreFeatureConfig.Target byName(String name)
		{
			return (MubbleOreFeatureConfig.Target) nameMap.get(name);
		}
		
		public Predicate<BlockState> getCondition()
		{
			return this.predicate;
		}
	}
}
