package hugman.mod.objects.block;

import java.util.Random;

import hugman.mod.init.MubbleBlockStateProperties;
import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleCostumes;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleSounds;
import hugman.mod.objects.block_state.properties.GameStyle;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class QuestionBlock extends Block
{
	public static final EnumProperty<GameStyle> GAME_STYLE = MubbleBlockStateProperties.GAME_STYLE;
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.05D, 0.0D, 16.0D, 16.0D, 16.0D);
	
    public QuestionBlock()
    {
        super(Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(1.5F, 6.0F));
        this.setDefaultState(this.stateContainer.getBaseState().with(GAME_STYLE, GameStyle.SMB));
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
    	builder.add(GAME_STYLE);
	}
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
    	return SHAPE;
    }
    
    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean p_220082_5_)
    {
    	if (worldIn.isBlockPowered(pos)) loot(worldIn, pos);
    }
    
    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean p_220069_6_)
    {
    	if(worldIn.isBlockPowered(pos)) loot(worldIn, pos);
    	super.neighborChanged(state, worldIn, pos, blockIn, fromPos, p_220069_6_);
    }
    
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
    	loot(worldIn, pos);
    }
    
    public void loot(World worldIn, BlockPos pos)
	{
        BlockState empty_block = MubbleBlocks.EMPTY_BLOCK.getDefaultState();
        final double x = pos.getX() + 0.5D;
        final double y = pos.getY() + 0.5D + 0.6D;
        final double z = pos.getZ() + 0.5D;
        if (!worldIn.isRemote)
        {
        	Random rand = new Random();
    		int loot = rand.nextInt(11);
        	if (loot >= 0 && loot <= 3) worldIn.addEntity(new ItemEntity(worldIn, x, y, z, new ItemStack(MubbleItems.YELLOW_COIN, rand.nextInt(12) + 1)));
            else if (loot >= 3 && loot <= 7 ) worldIn.addEntity(new ItemEntity(worldIn, x, y, z, new ItemStack(MubbleItems.RED_COIN, rand.nextInt(2) + 1)));
            else if (loot == 8 ) worldIn.addEntity(new ItemEntity(worldIn, x, y, z, new ItemStack(MubbleItems.BLUE_COIN)));
            if (loot >= 0 && loot <= 8) worldIn.playSound((PlayerEntity)null, x, y - 0.6D, z, MubbleSounds.BLOCK_QUESTION_BLOCK_LOOT_COIN, SoundCategory.BLOCKS, 1f, 1f);
            else if (loot >= 9)
            {
        		loot = rand.nextInt(7);
        		if (loot >= 0 && loot <= 2 ) worldIn.addEntity(new ItemEntity(worldIn, x, y, z, new ItemStack(MubbleItems.SUPER_MUSHROOM)));
        		else if (loot >= 3 && loot <= 5 ) worldIn.addEntity(new ItemEntity(worldIn, x, y, z, new ItemStack(MubbleItems.CAPE_FEATHER)));
        		else if (loot == 6) worldIn.addEntity(new ItemEntity(worldIn, x, y, z, new ItemStack(MubbleCostumes.SUPER_CROWN)));
            	worldIn.playSound((PlayerEntity)null, x, y - 0.6D, z, MubbleSounds.BLOCK_QUESTION_BLOCK_LOOT_POWER_UP, SoundCategory.BLOCKS, 1f, 1f);
            }
            worldIn.setBlockState(pos, empty_block);
        }
	}
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
    	Item item = context.getItem().getItem();
    		 if(item == MubbleItems.QUESTION_BLOCK_SMB)		return this.getDefaultState().with(GAME_STYLE, GameStyle.SMB);
    	else if(item == MubbleItems.QUESTION_BLOCK_SMB3)	return this.getDefaultState().with(GAME_STYLE, GameStyle.SMB3);
    	else if(item == MubbleItems.QUESTION_BLOCK_SMW)		return this.getDefaultState().with(GAME_STYLE, GameStyle.SMW);
    	else if(item == MubbleItems.QUESTION_BLOCK_NSMBU)	return this.getDefaultState().with(GAME_STYLE, GameStyle.NSMBU);
    	else return super.getStateForPlacement(context);
    }
}
