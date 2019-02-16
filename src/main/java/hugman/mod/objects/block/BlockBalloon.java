package hugman.mod.objects.block;

import java.util.Random;

import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBalloon extends BlockFlying implements IHasModel
{
    /** 
     * Open class - can be initialized for multiple items with variables.
     */
	public BlockBalloon(String color)
	{
		super(color + "_balloon", Material.CLOTH, 0f, 0f, SoundType.CLOTH);
	}
	
	@Override
    public int quantityDropped(Random random)
    {
        return 0;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }
    
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    @Override
    protected boolean canSilkHarvest()
    {
        return true;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
}
