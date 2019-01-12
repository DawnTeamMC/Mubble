package hugman.mod.objects.blocks;

import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;

public class BlockDirt extends BlockBase implements IHasModel
{
	/**
	 * Open class - can be initialized for multiple items with variables.
	 */
	public BlockDirt(String name, CreativeTabs tab, Material material, float hardness, float resistance, SoundType sound, int light)
	{
		super(name, tab, material, hardness, resistance, sound, light);
	}
	
	/**
	 * Open class - can be initialized for multiple items with variables.
	 */
	public BlockDirt(String name, CreativeTabs tab, Material material, float hardness, float resistance, SoundType sound)
	{
		super(name, tab, material, hardness, resistance, sound);
	}
    
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) 
	{
		return true;
	}
}
