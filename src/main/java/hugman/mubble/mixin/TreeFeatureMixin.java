package hugman.mubble.mixin;

import com.sun.istack.internal.Nullable;
import hugman.mubble.init.MubbleEnchantments;
import hugman.mubble.init.MubbleSounds;
import hugman.mubble.init.world.MubbleFeatureConfigs;
import hugman.mubble.init.world.MubbleFeatures;
import hugman.mubble.objects.item.LightsaberItem;
import hugman.mubble.util.EnchantmentUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;
import java.util.Set;

import static net.minecraft.world.gen.feature.Feature.isDirt;

@Mixin(TreeFeature.class)
public abstract class TreeFeatureMixin {
	@Redirect(method = "generate", at = @At(value = "INVOKE", target = "isDirtOrGrass"))
	private boolean mubble_isDirtOrGrass(TestableWorld testableWorld, BlockPos blockPos, ModifiableTestableWorld world, Random random, BlockPos pos, Set<BlockPos> logPositions, Set<BlockPos> leavesPositions, BlockBox box, TreeFeatureConfig config) {
		if(config == MubbleFeatureConfigs.PALM_TREE)
		{
			return testableWorld.testBlockState(blockPos, (state) -> {
				Block block = state.getBlock();
				return block == Blocks.SAND || block == Blocks.RED_SAND;
			});
		}
		return testableWorld.testBlockState(blockPos, (state) -> {
			Block block = state.getBlock();
			return isDirt(block) || block == Blocks.FARMLAND;
		});
	}
}
