package hugman.mod.objects.block;

import java.util.Random;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import hugman.mod.util.Teleporter;
import hugman.mod.util.handlers.SoundHandler;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPortal extends Block implements IHasModel
{
	protected static final AxisAlignedBB PORTAL_AABB = new AxisAlignedBB(0.0D, 0.375D, 0.0D, 1.0D, 0.625D, 1.0D);
	int dim;
	
	/**
	 * Static class - can only be initialized once.
	 */
	public BlockPortal(String portal, int dim)
	{
		super(Material.PORTAL);
		setTranslationKey(portal + "_portal");
		setRegistryName(portal + "_portal");
		setCreativeTab(null);
		setSoundType(SoundType.GLASS);
		setHardness(-1.0f);
		this.blockResistance = 6000000.0F;
		this.lightValue = 15;
		this.dim = dim;
		
		MubbleBlocks.BLOCKS.add(this);
	}
	
	@Override
    public AxisAlignedBB getBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return PORTAL_AABB;
    }
	
	@Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }
	
	@Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
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
    
    /* ULTIMATUM LOCK
	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
		if(entityIn instanceof EntityPlayer && !worldIn.isRemote)
		{
			EntityPlayer playerIn = (EntityPlayer) entityIn;
			int desDimInt = this.dim;
			if(playerIn.dimension == this.dim) desDimInt = 0;		
			World desDimWorld = worldIn.getMinecraftServer().getWorld(desDimInt);
			BlockPos desPos = desDimWorld.getTopSolidOrLiquidBlock(new BlockPos(10.5, 0, 10.5));
			if(playerIn.dimension != this.dim) Teleporter.teleportToDimension(playerIn, desDimInt, -4, 91, 0);
			else Teleporter.teleportToDimension(playerIn, desDimInt, desPos.getX(), desPos.getY(), desPos.getZ());
		}
    }
    
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
    	if(this == MubbleBlocks.ULTIMATUM_PORTAL)
    	{
            if (rand.nextInt(100) == 0)
            {
                worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundHandler.BLOCK_ULTIMATUM_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
            }
            if (rand.nextInt(30) == 0)
            {
                EnumParticleTypes particle = null;
                switch (rand.nextInt(4))
                {
                	case 0: particle = EnumParticleTypes.FLAME;
        					break;
                	case 1: particle = EnumParticleTypes.SMOKE_NORMAL;
    						break;
                	case 2: particle = EnumParticleTypes.CLOUD;
    						break;
                	case 3: particle = EnumParticleTypes.FIREWORKS_SPARK;
    						break;
                }
                for (int i = 0; i < rand.nextInt(11) + 5; i++)
                {
                	worldIn.spawnParticle(particle, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, ((double)rand.nextFloat() - 0.5D) * 0.3D, (double)rand.nextFloat() * 0.9D, ((double)rand.nextFloat() - 0.5D) * 0.3D);
                }
            }
    	}
    }*/
	
	@Override
    public int quantityDropped(Random random)
    {
        return 0;
    }
	
	@Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return ItemStack.EMPTY;
    }
	
	@Override
	public void registerModels(){}
}
