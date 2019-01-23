package hugman.mod.objects.block;

import java.util.Random;

import com.google.common.collect.Lists;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleCostumes;
import hugman.mod.init.MubbleItems;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCloud extends BlockBase implements IHasModel
{
	/**
	 * Static class - can only be initialized once.
	 */
	public BlockCloud(String color)
	{
		super(color + "_cloud_block", Material.CIRCUITS, 0, 0, SoundType.CLOTH);
	}
	
	@Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
    }
    
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return null;
	}
    
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }
    
	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
		EntityPlayer playerIn;
		if(entityIn instanceof EntityPlayer)
		{
			playerIn = (EntityPlayer) entityIn;
			ItemStack armor;
			armor = playerIn.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
			if(armor.getItem() == MubbleCostumes.SUPER_CROWN)
			{
		        Random rand = new Random();
				if(!playerIn.isSneaking())
				{
					if(!playerIn.isSprinting()) entityIn.motionY = (rand.nextInt(31) + 40) / 100D;
					else playerIn.motionY = 0.7D;
				}
				playerIn.fallDistance = 0f;
			}
		}
		if(entityIn instanceof EntityItem)
		{
			EntityItem itemEntity = (EntityItem) entityIn;
			if(itemEntity.getItem().getItem() == MubbleCostumes.SUPER_CROWN) itemEntity.motionY = 0.3D;
			if(Lists.newArrayList(
					Items.FEATHER,
					MubbleItems.CAPE_FEATHER,
					MubbleItems.WHEAT_FLOUR,
					Item.getItemFromBlock(MubbleBlocks.CLOUD_FLOWER),
					Item.getItemFromBlock(MubbleBlocks.WHITE_CLOUD_BLOCK),
					Item.getItemFromBlock(MubbleBlocks.LIGHT_GRAY_CLOUD_BLOCK),
					Item.getItemFromBlock(MubbleBlocks.GRAY_CLOUD_BLOCK),
					Item.getItemFromBlock(MubbleBlocks.BLACK_CLOUD_BLOCK)).contains(itemEntity.getItem().getItem())
			) itemEntity.motionY = 0.1D;
		}
    }
}
