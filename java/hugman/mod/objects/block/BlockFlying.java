package hugman.mod.objects.block;

import java.util.Random;

import hugman.mod.entity.EntityFlyingBlock;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFlying extends BlockBase implements IHasModel
{	
	public static boolean flyInstantly;
	
    /** 
     * Open class - can be initialized for multiple items with variables.
     */
	public BlockFlying(String name, CreativeTabs tab, Material material, float hardness, float resistance, SoundType sound)
	{
		super(name, tab, material, hardness, resistance, sound);
	}
	
	@Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }
	
	@Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

	@Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            this.checkFlyable(worldIn, pos);
        }
    }
	
	private void checkFlyable(World worldIn, BlockPos pos)
    {
        if ((worldIn.isAirBlock(pos.up()) || canFlyThrough(worldIn.getBlockState(pos.up()))) && pos.getY() >= 0)
        {
            int i = 32;

            if (!flyInstantly && worldIn.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32)))
            {
                if (!worldIn.isRemote)
                {
                    EntityFlyingBlock entityflyingblock = new EntityFlyingBlock(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, worldIn.getBlockState(pos));
                    this.onStartFlying(entityflyingblock);
                    worldIn.spawnEntity(entityflyingblock);
                }
            }
            else
            {
                IBlockState state = worldIn.getBlockState(pos);
                worldIn.setBlockToAir(pos);
                BlockPos blockpos;

                for (blockpos = pos.up(); (worldIn.isAirBlock(blockpos) || canFlyThrough(worldIn.getBlockState(blockpos))) && blockpos.getY() > 0; blockpos = blockpos.up())
                {
                    ;
                }

                if (blockpos.getY() > 0)
                {
                    worldIn.setBlockState(blockpos.up(), state);
                }
            }
        }
    }

    protected void onStartFlying(EntityFlyingBlock flyingEntity)
    {
    }
    
    @Override
    public int tickRate(World worldIn)
    {
        return 2;
    }

    public static boolean canFlyThrough(IBlockState state)
    {
        Block block = state.getBlock();
        Material material = state.getMaterial();
        return block == Blocks.FIRE || material == Material.AIR || material == Material.WATER || material == Material.LAVA;
    }

    public void onEndFlying(World worldIn, BlockPos pos, IBlockState flyingState, IBlockState hitState)
    {
    }

    public void onBroken(World worldIn, BlockPos pos)
    {
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (rand.nextInt(16) == 0)
        {
            BlockPos blockpos = pos.up();

            if (canFlyThrough(worldIn.getBlockState(blockpos)))
            {
                double d0 = (double)((float)pos.getX() + rand.nextFloat());
                double d1 = (double)pos.getY() - 0.05D;
                double d2 = (double)((float)pos.getZ() + rand.nextFloat());
            }
        }
    }
}
