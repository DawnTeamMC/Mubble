package hugman.mod.objects.block;

import java.util.Random;

import hugman.mod.Main;
import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleTabs;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFallingGlass extends BlockFalling implements IHasModel
{
	protected static final AxisAlignedBB[] AABB_BY_INDEX = new AxisAlignedBB[] {new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.3125D, 0.0D, 0.0D, 0.6875D, 0.875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.3125D, 1.0D, 0.875D, 0.6875D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};
	
    /** 
     * Open class - can be initialized for multiple items with variables.
     */
	public BlockFallingGlass(String name)
	{
		super(Material.GLASS);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(MubbleTabs.MUBBLE_BLOCKS);
		setHardness(0.3f);
		this.blockResistance = 1.5f;
		setSoundType(SoundType.GLASS);
		
		MubbleBlocks.BLOCKS.add(this);
		MubbleItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
    public int quantityDropped(Random random)
    {
        return 0;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
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
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side) {
        final BlockPos offset = pos.offset(side);
        return !access.getBlockState(offset).doesSideBlockRendering(access, offset, side.getOpposite());
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side) {
        return access.getBlockState(pos.offset(side)).getBlock() == this;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
	
	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
