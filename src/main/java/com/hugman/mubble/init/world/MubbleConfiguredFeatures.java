package com.hugman.mubble.init.world;

import com.hugman.mubble.Mubble;
import com.hugman.mubble.init.MubbleBlockPack;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.ThreeLayersFeatureSize;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.*;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.DarkOakTrunkPlacer;
import net.minecraft.world.gen.trunk.LargeOakTrunkPlacer;
import net.minecraft.world.gen.trunk.MegaJungleTrunkPlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.OptionalInt;
import java.util.Set;

public class MubbleConfiguredFeatures {
	private static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> feature) {
		return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Mubble.MOD_DATA.id(name), feature);
	}

	public static final ConfiguredFeature<TreeFeatureConfig, ?> RED_PRESS_GARDEN_TREE = register("red_press_garden_tree", Feature.TREE.configure((new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(States.PRESS_GARDEN_LOG), new SimpleBlockStateProvider(States.RED_PRESS_GARDEN_LEAVES), new BlobFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(0), 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
	public static final ConfiguredFeature<TreeFeatureConfig, ?> FANCY_RED_PRESS_GARDEN_TREE = register("fancy_red_press_garden_tree", Feature.TREE.configure((new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(States.PRESS_GARDEN_LOG), new SimpleBlockStateProvider(States.RED_PRESS_GARDEN_LEAVES), new LargeOakFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(4), 4), new LargeOakTrunkPlacer(3, 11, 0), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().heightmap(Heightmap.Type.MOTION_BLOCKING).build()));
	public static final ConfiguredFeature<TreeFeatureConfig, ?> MEGA_RED_PRESS_GARDEN_TREE = register("mega_red_press_garden_tree", Feature.TREE.configure((new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(States.PRESS_GARDEN_LOG), new SimpleBlockStateProvider(States.RED_PRESS_GARDEN_LEAVES), new JungleFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(0), 2), new MegaJungleTrunkPlacer(30, 2, 20), new TwoLayersFeatureSize(1, 1, 2))).build()));
	public static final ConfiguredFeature<?, ?> PATCH_RED_PRESS_GARDEN_LEAF_PILE = register("patch_red_press_garden_leaf_pile", Configs.patch(States.RED_PRESS_GARDEN_LEAF_PILE, 32));
	public static final ConfiguredFeature<?, ?> PATCH_RED_PRESS_GARDEN_LEAF_PILE_NORMAL = register("patch_red_press_garden_leaf_pile_normal", PATCH_RED_PRESS_GARDEN_LEAF_PILE.decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(5));
	public static final ConfiguredFeature<TreeFeatureConfig, ?> PINK_PRESS_GARDEN_TREE = register("pink_press_garden_tree", Feature.TREE.configure((new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(States.PRESS_GARDEN_LOG), new SimpleBlockStateProvider(States.PINK_PRESS_GARDEN_LEAVES), new BlobFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(0), 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
	public static final ConfiguredFeature<TreeFeatureConfig, ?> FANCY_PINK_PRESS_GARDEN_TREE = register("fancy_pink_press_garden_tree", Feature.TREE.configure((new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(States.PRESS_GARDEN_LOG), new SimpleBlockStateProvider(States.PINK_PRESS_GARDEN_LEAVES), new LargeOakFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(4), 4), new LargeOakTrunkPlacer(3, 11, 0), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().heightmap(Heightmap.Type.MOTION_BLOCKING).build()));
	public static final ConfiguredFeature<TreeFeatureConfig, ?> MEGA_PINK_PRESS_GARDEN_TREE = register("mega_press_garden_tree", Feature.TREE.configure((new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(States.PRESS_GARDEN_LOG), new SimpleBlockStateProvider(States.PINK_PRESS_GARDEN_LEAVES), new JungleFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(0), 2), new MegaJungleTrunkPlacer(30, 2, 20), new TwoLayersFeatureSize(1, 1, 2))).build()));
	public static final ConfiguredFeature<?, ?> PATCH_PINK_PRESS_GARDEN_LEAF_PILE = register("patch_pink_press_garden_leaf_pile", Configs.patch(States.RED_PRESS_GARDEN_LEAF_PILE, 32));
	public static final ConfiguredFeature<?, ?> PATCH_PINK_PRESS_GARDEN_LEAF_PILE_NORMAL = register("patch_pink_press_garden_leaf_pile_normal", PATCH_PINK_PRESS_GARDEN_LEAF_PILE.decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(5));
	public static final ConfiguredFeature<?, ?> PINK_PRESS_GARDEN_BUSH = register("pink_press_garden_bush", Feature.TREE.configure((new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(States.PRESS_GARDEN_LOG), new SimpleBlockStateProvider(States.PINK_PRESS_GARDEN_LEAVES), new BlobFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(1), 2), new StraightTrunkPlacer(1, 0, 0), new TwoLayersFeatureSize(0, 0, 0))).heightmap(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES).build()));

	public static final ConfiguredFeature<TreeFeatureConfig, ?> SCARLET_TREE = register("scarlet_tree", Feature.TREE.configure((new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(States.SCARLET_LOG), new SimpleBlockStateProvider(States.SCARLET_LEAVES), new BlobFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(0), 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
	public static final ConfiguredFeature<TreeFeatureConfig, ?> FANCY_SCARLET_TREE = register("fancy_scarlet_tree", Feature.TREE.configure((new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(States.SCARLET_LOG), new SimpleBlockStateProvider(States.SCARLET_LEAVES), new LargeOakFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(4), 4), new LargeOakTrunkPlacer(3, 11, 0), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().heightmap(Heightmap.Type.MOTION_BLOCKING).build()));
	public static final ConfiguredFeature<TreeFeatureConfig, ?> HUGE_SCARLET_TREE = register("huge_scarlet_tree", Feature.TREE.configure((new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(States.SCARLET_LOG), new SimpleBlockStateProvider(States.SCARLET_LEAVES), new DarkOakFoliagePlacer(UniformIntDistribution.of(0), UniformIntDistribution.of(0)), new DarkOakTrunkPlacer(6, 2, 1), new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty()))).maxWaterDepth(Integer.MAX_VALUE).heightmap(Heightmap.Type.MOTION_BLOCKING).ignoreVines().build()));
	public static final ConfiguredFeature<?, ?> PATCH_SCARLET_LEAF_PILE = register("patch_scarlet_leaf_pile", Configs.patch(States.SCARLET_LEAF_PILE, 32));
	public static final ConfiguredFeature<?, ?> PATCH_SCARLET_LEAF_PILE_NORMAL = register("patch_scarlet_leaf_pile_normal", PATCH_SCARLET_LEAF_PILE.decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(5));
	public static final ConfiguredFeature<?, ?> SCARLET_BUSH = register("scarlet_bush", Feature.TREE.configure((new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(States.SCARLET_LOG), new SimpleBlockStateProvider(States.SCARLET_LEAVES), new BushFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(1), 2), new StraightTrunkPlacer(1, 0, 0), new TwoLayersFeatureSize(0, 0, 0))).heightmap(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES).build()));

	public static final ConfiguredFeature<?, ?> PATCH_SCARLET_MUSHROOM = register("patch_scarlet_mushroom", Configs.patch(States.SCARLET_MUSHROOM, 64));
	public static final ConfiguredFeature<?, ?> PATCH_SCARLET_MUSHROOM_NORMAL = register("patch_scarlet_mushroom_normal", PATCH_SCARLET_MUSHROOM.decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).applyChance(4));
	public static final ConfiguredFeature<?, ?> PATCH_SCARLET_ORCHID = register("patch_scarlet_orchid", Feature.FLOWER.configure(Configs.patch(States.SCARLET_ORCHID, 64).getConfig()).decorate(ConfiguredFeatures.Decorators.SPREAD_32_ABOVE).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).repeat(4));

	public static final class Configs {
		protected final static ConfiguredFeature<RandomPatchFeatureConfig, ?> patch(BlockState blockState, int tries) {
			return Feature.RANDOM_PATCH.configure((new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(blockState), SimpleBlockPlacer.INSTANCE)).tries(tries).cannotProject().build());
		}

		protected final static ConfiguredFeature<RandomPatchFeatureConfig, ?> patch(BlockState blockState, int tries, Set<Block> whitelist) {
			return Feature.RANDOM_PATCH.configure((new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(blockState), SimpleBlockPlacer.INSTANCE)).tries(tries).whitelist(whitelist).cannotProject().build());
		}
	}

	public static final class States {
		protected static final BlockState PRESS_GARDEN_LOG = MubbleBlockPack.PRESS_GARDEN_WOOD.getLog().getDefaultState();
		protected static final BlockState RED_PRESS_GARDEN_LEAVES = MubbleBlockPack.RED_PRESS_GARDEN_LEAVES.getLeaves().getDefaultState();
		protected static final BlockState RED_PRESS_GARDEN_LEAF_PILE = MubbleBlockPack.RED_PRESS_GARDEN_LEAVES.getLeafPile().getDefaultState();
		protected static final BlockState PINK_PRESS_GARDEN_LEAVES = MubbleBlockPack.PINK_PRESS_GARDEN_LEAVES.getLeaves().getDefaultState();
		protected static final BlockState PINK_PRESS_GARDEN_LEAF_PILE = MubbleBlockPack.PINK_PRESS_GARDEN_LEAVES.getLeafPile().getDefaultState();

		protected static final BlockState SCARLET_LOG = MubbleBlockPack.SCARLET_WOOD.getLog().getDefaultState();
		protected static final BlockState SCARLET_LEAVES = MubbleBlockPack.SCARLET_WOOD.getLeaves().getDefaultState();
		protected static final BlockState SCARLET_LEAF_PILE = MubbleBlockPack.SCARLET_WOOD.getLeafPile().getDefaultState();
		protected static final BlockState SCARLET_ORCHID = MubbleBlockPack.SCARLET_ORCHID.getPlant().getDefaultState();

		protected static final BlockState SCARLET_MUSHROOM = MubbleBlockPack.SCARLET_MUSHROOM.getPlant().getDefaultState();
	}
}
