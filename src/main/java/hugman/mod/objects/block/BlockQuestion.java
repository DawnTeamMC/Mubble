package hugman.mod.objects.block;

import java.util.Random;

import hugman.mod.Reference;
import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleSounds;
import hugman.mod.init.MubbleTabs;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockQuestion extends Block
{
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.05D, 0.0D, 16.0D, 16.0D, 16.0D);
	
    public BlockQuestion()
    {
        super(Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(1.5F, 6.0F));
        setRegistryName(Reference.MOD_ID, "question_block");
        Item.Properties blocks = new Item.Properties().group(MubbleTabs.MUBBLE_BLOCKS);
        
		MubbleBlocks.BLOCKS.add(this);
		MubbleItems.ITEMS.add(new ItemBlock(this, blocks).setRegistryName(this.getRegistryName()));
    }
    
    @Override
    public VoxelShape getCollisionShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
    {
    	return SHAPE;
    }
    
    @Override
    public void onBlockAdded(IBlockState state, World worldIn, BlockPos pos, IBlockState oldState)
    {
    	if (worldIn.isBlockPowered(pos)) loot(worldIn, pos);
    }
    
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
    	if (worldIn.isBlockPowered(pos)) loot(worldIn, pos);
    }
    
    @Override
    public void onEntityCollision(IBlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
    	loot(worldIn, pos);
    }
    
    public void loot(World worldIn, BlockPos pos)
	{
        IBlockState empty_block = MubbleBlocks.EMPTY_BLOCK.getDefaultState();
        final double x = pos.getX() + 0.5D;
        final double y = pos.getY() + 0.5D + 0.6D;
        final double z = pos.getZ() + 0.5D;
        if (!worldIn.isRemote)
        {
        	Random rand = new Random();
    		int loot = rand.nextInt(11);
        	if (loot >= 0 && loot <= 3) worldIn.spawnEntity(new EntityItem(worldIn, x, y, z, new ItemStack(MubbleItems.YELLOW_COIN, rand.nextInt(12) + 1)));
            else if (loot >= 3 && loot <= 7 ) worldIn.spawnEntity(new EntityItem(worldIn, x, y, z, new ItemStack(MubbleItems.RED_COIN, rand.nextInt(2) + 1)));
            else if (loot == 8 ) worldIn.spawnEntity(new EntityItem(worldIn, x, y, z, new ItemStack(MubbleItems.BLUE_COIN)));
            if (loot >= 0 && loot <= 8) worldIn.playSound((EntityPlayer)null, x, y - 0.6D, z, MubbleSounds.BLOCK_QUESTION_BLOCK_LOOT_COIN, SoundCategory.BLOCKS, 1f, 1f);
            else if (loot >= 9)
            {
        		loot = rand.nextInt(7);
        		if (loot >= 0 && loot <= 2 ) worldIn.spawnEntity(new EntityItem(worldIn, x, y, z, new ItemStack(MubbleItems.SUPER_MUSHROOM)));
        		else if (loot >= 3 && loot <= 5 ) worldIn.spawnEntity(new EntityItem(worldIn, x, y, z, new ItemStack(MubbleItems.CAPE_FEATHER)));
        		else if (loot == 6) worldIn.spawnEntity(new EntityItem(worldIn, x, y, z, new ItemStack(MubbleItems.SUPER_CROWN)));
            	worldIn.playSound((EntityPlayer)null, x, y - 0.6D, z, MubbleSounds.BLOCK_QUESTION_BLOCK_LOOT_POWER_UP, SoundCategory.BLOCKS, 1f, 1f);
            }
            worldIn.setBlockState(pos, empty_block);
        }
	}
}
