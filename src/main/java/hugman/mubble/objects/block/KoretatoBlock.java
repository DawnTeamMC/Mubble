package hugman.mubble.objects.block;

import hugman.mubble.init.data.MubbleBlockStateProperties;
import hugman.mubble.objects.block.block_state_property.Princess;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;

public class KoretatoBlock extends DirectionalBlock
{
	public static final EnumProperty<Princess> PRINCESS = MubbleBlockStateProperties.PRINCESS;

	public KoretatoBlock()
	{
		super(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MaterialColor.YELLOW_TERRACOTTA).strength(0.4f, 2f).sounds(BlockSoundGroup.SNOW));
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.UP).with(PRINCESS, Princess.NONE));
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation direction)
	{
		return state.with(FACING, direction.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirrorIn)
	{
		return state.mirror(mirrorIn);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context)
	{
		return this.getDefaultState().with(FACING, context.getPlayerLookDirection().getOpposite());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		builder.add(PRINCESS, FACING);
	}
}
