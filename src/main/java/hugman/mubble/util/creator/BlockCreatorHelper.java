package hugman.mubble.util.creator;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class BlockCreatorHelper {
	// Registers
	public static int getFlammabilityBurn(Block block) {
		return FlammableBlockRegistry.getDefaultInstance().get(block).getBurnChance();
	}

	public static int getFlammabilitySpread(Block block) {
		return FlammableBlockRegistry.getDefaultInstance().get(block).getSpreadChance();
	}

	// Predicates
	public static Boolean canSpawnOnLeaves(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
		return type == EntityType.OCELOT || type == EntityType.PARROT;
	}

	public static boolean never(BlockState state, BlockView world, BlockPos pos) {
		return false;
	}
}
