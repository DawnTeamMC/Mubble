package hugman.mod.objects.block;

import java.util.Random;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleItems;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockOre extends BlockBase implements IHasModel
{	
    /** 
     * Open class - can be initialized for multiple items with variables.
     */
	public BlockOre(String name)
	{
		super(name, Material.ROCK, 3f, 15f, SoundType.STONE);
		setHarvestLevel("pickaxe", 2);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		if (this == MubbleBlocks.VANADIUM_ORE)
        {
            return MubbleItems.VANADIUM;
        }
		else return null;
	}
    @Override
    public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        Random rand = world instanceof World ? ((World)world).rand : new Random();
        if (this.getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this))
        {
            int i = 0;

            if (this == MubbleBlocks.VANADIUM_ORE)
            {
            	i = MathHelper.getInt(rand, 3, 7);
            }
            return i;
        }
        return 0;
    }
}
