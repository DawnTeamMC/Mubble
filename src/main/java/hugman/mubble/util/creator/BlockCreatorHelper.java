package hugman.mubble.util.creator;

import hugman.mubble.Mubble;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

public class BlockCreatorHelper {

	public static void addEntries(BlockEntry... entries) {
		for(BlockEntry entry : entries) {
			registerBlock(entry);
		}
	}

	// Registers
	public static void registerBlock(BlockEntry entry) {
		Block newBlock = Registry.register(Registry.BLOCK, new Identifier(Mubble.MOD_ID, entry.getName()), entry.getBlock());
		BlockRenderLayerMap.INSTANCE.putBlock(newBlock, entry.getRenderLayer());
		if(entry.getItemGroup() != null) {
			BlockCreatorHelper.registerBlockItem(newBlock, entry.getItemGroup());
		}
		BlockCreatorHelper.registerFlammability(newBlock, entry.getBurn(), entry.getSpread());
	}

	private static Item registerBlockItem(Block block, ItemGroup itemGroup) {
		return Registry.register(Registry.ITEM, Registry.BLOCK.getId(block), new BlockItem(block, new Item.Settings().group(itemGroup)));
	}

	private static void registerFlammability(Block block, int burn, int spread) {
		FlammableBlockRegistry.getDefaultInstance().add(block, burn, spread);
	}

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
