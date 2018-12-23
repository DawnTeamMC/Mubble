package hugman.mod.objects.blocks;

import java.util.Random;

import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDirt extends BlockBase implements IHasModel
{
	/**
	 * Open class - can be initialized for multiple items with variables.
	 */
	public BlockDirt(String name, Material material, float hardness, float resistance, SoundType sound, int light)
	{
		super(name, material, hardness, resistance, sound, light);
	}
	
	/**
	 * Open class - can be initialized for multiple items with variables.
	 */
	public BlockDirt(String name, Material material, float hardness, float resistance, SoundType sound)
	{
		super(name, material, hardness, resistance, sound);
	}
    
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) 
	{
		return true;
	}
}
