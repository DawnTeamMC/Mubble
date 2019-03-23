package hugman.mod.objects.block;

import java.util.Random;

import hugman.mod.Mubble;
import hugman.mod.init.elements.MubbleBlocks;
import hugman.mod.init.technical.MubbleTags;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockCloud extends Block
{
    public BlockCloud(String color)
    {
        super(Properties.create(Material.GLASS).sound(SoundType.CLOTH).hardnessAndResistance(0f));
        setRegistryName(Mubble.MOD_ID, color + "_cloud_block");
        MubbleBlocks.register(this);
    }
    
    @Override
    public boolean isFullCube(IBlockState state)
    {
    	return false;
    }
    
    @Override
    public VoxelShape getCollisionShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
    {
    	return VoxelShapes.empty();
    }
    
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
    	return BlockFaceShape.UNDEFINED;
	}
    
    @Override
    public BlockRenderLayer getRenderLayer()
    {
    	return BlockRenderLayer.TRANSLUCENT;
    }
    
    @Override
    public int getOpacity(IBlockState state, IBlockReader worldIn, BlockPos pos)
    {
    	return 0;
    }
    
    @Override
    public int quantityDropped(IBlockState state, Random random)
    {
    	return 0;
    }
    
    @Override
    public void onEntityCollision(IBlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
    	if(entityIn instanceof EntityPlayer)
    	{
    		EntityPlayer playerIn = (EntityPlayer)entityIn;
    		ItemStack armor;
    		armor = playerIn.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
    		if(MubbleTags.Items.CROWNS.contains(armor.getItem()) && !playerIn.isSneaking())
    		{
        		if(!playerIn.isSprinting()) playerIn.motionY = (this.RANDOM.nextInt(31) + 40) / 100D;
    			else playerIn.motionY = 0.7D;
        		playerIn.fallDistance = 0f;
    		}
    	}
    	if(entityIn instanceof EntityItem)
    	{
    		EntityItem itemIn = (EntityItem)entityIn;
        	if(MubbleTags.Items.CROWNS.contains(itemIn.getItem().getItem())) itemIn.motionY = 0.3D;
        	if(MubbleTags.Items.LIGHTWEIGHT_ITEMS.contains(itemIn.getItem().getItem())) itemIn.motionY = 0.1D;
    	}
    }
}
