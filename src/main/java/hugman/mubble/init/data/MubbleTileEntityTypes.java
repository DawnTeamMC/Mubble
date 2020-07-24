package hugman.mubble.init.data;

import hugman.mubble.Mubble;
import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.object.block.block_entity.PlacerBlockEntity;
import hugman.mubble.object.block.block_entity.PresentBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MubbleTileEntityTypes {
	public static final BlockEntityType<DispenserBlockEntity> PLACER = register("placer", BlockEntityType.Builder.create(PlacerBlockEntity::new, MubbleBlocks.PLACER.getBlock()));
	public static final BlockEntityType<PresentBlockEntity> PRESENT = register("present", BlockEntityType.Builder.create(PresentBlockEntity::new, MubbleBlocks.WHITE_PRESENT.getBlock(), MubbleBlocks.BLACK_PRESENT.getBlock(), MubbleBlocks.BLUE_PRESENT.getBlock(), MubbleBlocks.GREEN_PRESENT.getBlock(), MubbleBlocks.YELLOW_PRESENT.getBlock(), MubbleBlocks.RED_PRESENT.getBlock(), MubbleBlocks.PURPLE_PRESENT.getBlock(), MubbleBlocks.GOLDEN_PRESENT.getBlock()));

	private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType.Builder<T> builder) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Mubble.MOD_ID, name), builder.build(null));
	}
}