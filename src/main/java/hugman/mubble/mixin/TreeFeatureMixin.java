package hugman.mubble.mixin;

import hugman.mubble.init.world.MubbleFeatureConfigs;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;
import java.util.Set;

import static net.minecraft.world.gen.feature.Feature.isSoil;

@Mixin(TreeFeature.class)
public abstract class TreeFeatureMixin {
	@Redirect(method = "generate", at = @At(value = "INVOKE", target = "isDirtOrGrass"))
	private boolean mubble_isDirtOrGrass(TestableWorld testableWorld, BlockPos blockPos, ModifiableTestableWorld world, Random random, BlockPos pos, Set<BlockPos> logPositions, Set<BlockPos> leavesPositions, BlockBox box, TreeFeatureConfig config) {
		if(config == MubbleFeatureConfigs.PALM_TREE) {
			return testableWorld.testBlockState(blockPos, (state) -> {
				Block block = state.getBlock();
				return block == Blocks.SAND || block == Blocks.RED_SAND;
			});
		}
		return testableWorld.testBlockState(blockPos, (state) -> {
			Block block = state.getBlock();
			return isSoil(block) || block == Blocks.FARMLAND;
		});
	}
}
