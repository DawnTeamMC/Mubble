package hugman.mod.objects.blocks;

import java.util.Random;

import com.google.common.collect.Lists;

import hugman.mod.init.BlockInit;
import hugman.mod.init.CostumeInit;
import hugman.mod.init.ItemInit;
import hugman.mod.util.Teleporter;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPortal extends BlockBase implements IHasModel
{
	/**
	 * Static class - can only be initialized once.
	 */
	public BlockPortal(String portal)
	{
		super(portal, Material.PORTAL, 0, 0, SoundType.GLASS);
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
		if(entityIn instanceof EntityPlayer && !worldIn.isRemote)
		{
			EntityPlayer playerIn = (EntityPlayer) entityIn;
			int desDimInt = 64;
			if(playerIn.dimension == 64) desDimInt = 0;		
			World desDimWorld = worldIn.getMinecraftServer().getWorld(desDimInt);
			BlockPos desPos = desDimWorld.getTopSolidOrLiquidBlock(new BlockPos(0.5, 0, 0.5));
			Teleporter.teleportToDimension(playerIn, desDimInt, desPos.getX(), desPos.getY(), desPos.getZ());
		}
    }
}
