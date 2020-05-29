package hugman.mubble.init.data;

import hugman.mubble.Mubble;
import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.objects.block.block_entity.PlacerBlockEntity;
import hugman.mubble.objects.block.block_entity.PresentBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MubbleTileEntityTypes
{
	public static final BlockEntityType<DispenserBlockEntity> PLACER = register("placer", BlockEntityType.Builder.create(PlacerBlockEntity::new, MubbleBlocks.PLACER));
	public static final BlockEntityType<PresentBlockEntity> PRESENT = register("present", BlockEntityType.Builder.create(PresentBlockEntity::new, MubbleBlocks.WHITE_PRESENT, MubbleBlocks.BLACK_PRESENT, MubbleBlocks.BLUE_PRESENT, MubbleBlocks.GREEN_PRESENT, MubbleBlocks.YELLOW_PRESENT, MubbleBlocks.RED_PRESENT, MubbleBlocks.PURPLE_PRESENT, MubbleBlocks.GOLDEN_PRESENT));

	private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType.Builder<T> builder)
	{
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Mubble.MOD_ID, name), builder.build(null));
	}
}