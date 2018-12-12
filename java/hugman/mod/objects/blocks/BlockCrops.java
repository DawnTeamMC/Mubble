package hugman.mod.objects.blocks;

import java.util.Random;

import hugman.mod.Main;
import hugman.mod.init.BlockInit;
import hugman.mod.init.ItemInit;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCrops extends net.minecraft.block.BlockCrops implements IHasModel, IGrowable
{
	Item seed;
	Item food;
	
	/** 
	 * Open class - can be initialized for multiple items with variables.
	 */
	public BlockCrops(String name, Item seed)
	{
		setTranslationKey(name);
		setRegistryName(name);
		this.seed = seed;
		setCreativeTab(Main.MUBBLE_BLOCKS);
        this.setHardness(0.0F);
        this.setResistance(0.0F);
		setSoundType(SoundType.PLANT);
		this.setDefaultState(this.blockState.getBaseState().withProperty(this.getAgeProperty(), Integer.valueOf(0)));
        this.setTickRandomly(true);
        this.disableStats();
        
		BlockInit.BLOCKS.add(this);
	}
	
	/** 
	 * Open class - can be initialized for multiple items with variables.
	 */
	public BlockCrops(String name, Item seed, Item food)
	{
		setTranslationKey(name);
		setRegistryName(name);
		this.seed = seed;
		this.food = food;
		setCreativeTab(Main.MUBBLE_BLOCKS);
        this.setHardness(0.0F);
        this.setResistance(0.0F);
		setSoundType(SoundType.PLANT);
		this.setDefaultState(this.blockState.getBaseState().withProperty(this.getAgeProperty(), Integer.valueOf(0)));
        this.setTickRandomly(true);
        this.disableStats();
        
		BlockInit.BLOCKS.add(this);
	}
	
	@Override
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        IBlockState soil = worldIn.getBlockState(pos.down());
        return (worldIn.getLight(pos) >= 8 || worldIn.canSeeSky(pos)) && this.canSustainBush(soil);
    }
	
	@Override
    protected Item getSeed()
    {
        return this.seed;
    }
	
    @Override
    protected Item getCrop()
    {
        return this.food;
    }
    
	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
