package hugman.mubble.init.world;

import hugman.mubble.Mubble;
import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.objects.world.feature.PermafrostSpringFeature;
import hugman.mubble.objects.world.feature.ReplaceBlockGroupFeature;
import hugman.mubble.objects.world.feature.ScarletFlowersFeature;
import hugman.mubble.objects.world.feature.TomatoFeature;
import hugman.mubble.objects.world.feature.tree.PalmTreeFeature;
import hugman.mubble.objects.world.feature.tree.template.LargeTreeFeature;
import hugman.mubble.objects.world.feature.tree.template.MegaTreeFeature;
import hugman.mubble.objects.world.feature.tree.template.TallTreeFeature;
import hugman.mubble.objects.world.feature.tree.template.TreeFeature;
import hugman.mubble.objects.world.feature_config.ReplaceBlockGroupConfig;
import net.minecraft.block.Blocks;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.BranchedTreeFeatureConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.gen.feature.HellLavaConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.ScatteredPlantFeature;
import net.minecraft.world.gen.feature.ShrubFeature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.JungleGroundBushFeature;
import net.minecraft.world.gen.feature.MegaTreeFeatureConfig;
import net.minecraft.world.gen.feature.RandomPatchFeature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.SimpleBlockFeature;
import net.minecraft.world.gen.feature.SpringFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

public class MubbleFeatures
{
	public static final Feature<NoFeatureConfig> AUTUMN_OAK_TREE = new TreeFeature(NoFeatureConfig::deserialize, true, Blocks.OAK_LOG, MubbleBlocks.AUTUMN_OAK_LEAVES, MubbleBlocks.AUTUMN_OAK_SAPLING);
	public static final Feature<NoFeatureConfig> TALL_AUTUMN_OAK_TREE = new TallTreeFeature(NoFeatureConfig::deserialize, true, Blocks.OAK_LOG, MubbleBlocks.AUTUMN_OAK_LEAVES, MubbleBlocks.AUTUMN_OAK_SAPLING);
	public static final Feature<NoFeatureConfig> PINK_CHERRY_OAK_TREE = new TreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.CHERRY_OAK_LOG, MubbleBlocks.PINK_CHERRY_OAK_LEAVES, MubbleBlocks.PINK_CHERRY_OAK_SAPLING);
	public static final Feature<NoFeatureConfig> TALL_PINK_CHERRY_OAK_TREE = new TallTreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.CHERRY_OAK_LOG, MubbleBlocks.PINK_CHERRY_OAK_LEAVES, MubbleBlocks.PINK_CHERRY_OAK_SAPLING);
	public static final Feature<NoFeatureConfig> WHITE_CHERRY_OAK_TREE = new TreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.CHERRY_OAK_LOG, MubbleBlocks.WHITE_CHERRY_OAK_LEAVES, MubbleBlocks.WHITE_CHERRY_OAK_SAPLING);
	public static final Feature<NoFeatureConfig> TALL_WHITE_CHERRY_OAK_TREE = new TallTreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.CHERRY_OAK_LOG, MubbleBlocks.WHITE_CHERRY_OAK_LEAVES, MubbleBlocks.WHITE_CHERRY_OAK_SAPLING);
	public static final Feature<NoFeatureConfig> RED_PRESS_GARDEN_TREE = new TreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.PRESS_GARDEN_LOG, MubbleBlocks.RED_PRESS_GARDEN_LEAVES, MubbleBlocks.RED_PRESS_GARDEN_SAPLING);
	public static final Feature<NoFeatureConfig> MEGA_RED_PRESS_GARDEN_TREE = new MegaTreeFeature(NoFeatureConfig::deserialize, true, 30, 40, MubbleBlocks.PRESS_GARDEN_LOG, MubbleBlocks.RED_PRESS_GARDEN_LEAVES, MubbleBlocks.RED_PRESS_GARDEN_SAPLING);
	public static final Feature<NoFeatureConfig> PINK_PRESS_GARDEN_TREE = new TreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.PRESS_GARDEN_LOG, MubbleBlocks.PINK_PRESS_GARDEN_LEAVES, MubbleBlocks.PINK_PRESS_GARDEN_SAPLING);
	public static final Feature<NoFeatureConfig> MEGA_PINK_PRESS_GARDEN_TREE = new MegaTreeFeature(NoFeatureConfig::deserialize, true, 30, 40, MubbleBlocks.PRESS_GARDEN_LOG, MubbleBlocks.PINK_PRESS_GARDEN_LEAVES, MubbleBlocks.PINK_PRESS_GARDEN_SAPLING);
	public static final Feature<BranchedTreeFeatureConfig> PALM_TREE = register("palm_tree", new PalmTreeFeature(BranchedTreeFeatureConfig::deserialize));
	public static final Feature<BranchedTreeFeatureConfig> SCARLET_TREE = register("scarlet_tree", new TreeFeature(BranchedTreeFeatureConfig::deserialize));
	public static final Feature<BranchedTreeFeatureConfig> TALL_SCARLET_TREE = register("tall_scarlet_tree", new TallTreeFeature(BranchedTreeFeatureConfig::deserialize));
	public static final Feature<MegaTreeFeatureConfig> LARGE_SCARLET_TREE = register("large_scarlet_tree", new LargeTreeFeature(MegaTreeFeatureConfig::deserialize));
	
	public static final Feature<TreeFeatureConfig> PINK_PRESS_GARDEN_GROUND_BUSH = register("pink_press_garden_ground_bush", new JungleGroundBushFeature(TreeFeatureConfig::deserialize));
	
	public static final Feature<TreeFeatureConfig> SCARLET_GROUND_BUSH = register("scarlet_ground_bush", new JungleGroundBushFeature(TreeFeatureConfig::deserialize));
	public static final FlowerFeature<DefaultFeatureConfig> SCARLET_FLOWERS = register("scarlet_flowers", new ScarletFlowersFeature(DefaultFeatureConfig::deserialize));

	public static final Feature<DefaultFeatureConfig> TOMATOES = register("tomatoes", new TomatoFeature(DefaultFeatureConfig::deserialize));
	public static final Feature<RandomPatchFeatureConfig> BLUEBERRY_BUSH = register("blueberry_bush", new RandomPatchFeature(RandomPatchFeatureConfig::deserialize));
	
	public static final Feature<SpringFeatureConfig> PERMAFROST_SPRING = register("permafrost_spring", new PermafrostSpringFeature(SpringFeatureConfig::deserialize));
	
	public static final Feature<ReplaceBlockGroupConfig> REPLACE_BLOCK_GROUP = register("replace_block_group", new ReplaceBlockGroupFeature(ReplaceBlockGroupConfig::deserialize));
	
	private static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
		return Registry.register(Registry.FEATURE, new Identifier(Mubble.MOD_ID, name), feature);
	}
}