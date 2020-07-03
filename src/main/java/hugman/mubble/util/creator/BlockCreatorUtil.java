package hugman.mubble.util.creator;

import hugman.mubble.Mubble;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.Material;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

public class BlockCreatorUtil {
	// Registers

	public static Block registerBlock(Block block, String name) {
		return Registry.register(Registry.BLOCK, new Identifier(Mubble.MOD_ID, name), block);
	}

	public static Item registerBlockItem(Block block, ItemGroup itemGroup) {
		return Registry.register(Registry.ITEM, Registry.BLOCK.getId(block), new BlockItem(block, new Item.Settings().group(itemGroup)));
	}

	public static Item registerBlockItem(Block block, Block baseBlock) {
		return registerBlockItem(block, baseBlock.asItem().getGroup());
	}

	public static void setFlammability(Block block, int burn, int spread) {
		FlammableBlockRegistry.getDefaultInstance().add(block, burn, spread);
	}

	public static void setFlammability(Block block, Block baseBlock) {
		FlammableBlockRegistry.Entry entry = FlammableBlockRegistry.getDefaultInstance().get(baseBlock);
		setFlammability(block, entry.getBurnChance(), entry.getSpreadChance());
	}

	// Predicates

	public static Boolean canSpawnOnLeaves(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
		return type == EntityType.OCELOT || type == EntityType.PARROT;
	}

	public static boolean never(BlockState state, BlockView world, BlockPos pos) {
		return false;
	}
}
