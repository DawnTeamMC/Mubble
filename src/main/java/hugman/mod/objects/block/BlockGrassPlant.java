package hugman.mod.objects.block;

import java.util.Random;

import javax.annotation.Nullable;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleTabs;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGrassPlant extends BlockBush implements IHasModel
{
	protected static final AxisAlignedBB GRASS_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

	/**
	 * Open class - can be initialized for multiple items with variables.
	 */
	public BlockGrassPlant(String name, int light)
	{
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(MubbleTabs.MUBBLE_BLOCKS);
		setSoundType(SoundType.PLANT);
		this.blockHardness = 0f;
		this.blockResistance = 0f;
		this.lightValue = light;
		MubbleBlocks.BLOCKS.add(this);
		MubbleItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	/**
	 * Open class - can be initialized for multiple items with variables.
	 */
	public BlockGrassPlant(String name)
	{
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(MubbleTabs.MUBBLE_BLOCKS);
		setSoundType(SoundType.PLANT);
		this.blockHardness = 0f;
		this.blockResistance = 0f;
		MubbleBlocks.BLOCKS.add(this);
		MubbleItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return GRASS_AABB;
    }

    @Override
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        return super.canBlockStay(worldIn, pos, state);
    }
    
    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return null;
    }
    
    @Override
    public int quantityDroppedWithBonus(int fortune, Random random)
    {
        return 1 + random.nextInt(fortune * 2 + 1);
    }
    
    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (!worldIn.isRemote && stack.getItem() == Items.SHEARS)
        {
            player.addStat(StatList.getBlockStats(this));
            spawnAsEntity(worldIn, pos, new ItemStack(this, 1));
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }
    
    @Override
    public Block.EnumOffsetType getOffsetType()
    {
        return Block.EnumOffsetType.XYZ;
    }
    
    public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos)
    {
    	return true;
    }
    
    public NonNullList<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
    {
        return NonNullList.withSize(1, new ItemStack(this, 1));
    }
	
	@Override
	public void registerModels()
	{
		Mubble.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
