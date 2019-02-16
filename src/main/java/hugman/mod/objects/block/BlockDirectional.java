package hugman.mod.objects.block;

import java.util.Random;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDirectional extends BlockBase implements IHasModel
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    
    /** 
     * Open class - can be initialized for multiple items with variables.
     */
	public BlockDirectional(String name, Material material, float hardness, float resistance, SoundType sound, int light)
	{
		super(name, material, hardness, resistance, sound, light);
	}
	
    /** 
     * Open class - can be initialized for multiple items with variables.
     */
	public BlockDirectional(String name, Material material, float hardness, float resistance, SoundType sound)
	{
		super(name, material, hardness, resistance, sound);
	}
	
	@Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }
	
	@Override
    public int quantityDropped(Random random)
    {
    	if(this == MubbleBlocks.KORETATO_BLOCK) return 3;
    	else return 1;
    }
    
	@Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
    	if(this == MubbleBlocks.KORETATO_BLOCK) return Items.POTATO;
    	else return Item.getItemFromBlock(this);
    }
    
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | ((EnumFacing)state.getValue(FACING)).getIndex();
        return i;
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7));
    }
	
	@Override
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }
	
	@Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }
}
