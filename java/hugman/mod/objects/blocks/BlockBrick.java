package hugman.mod.objects.blocks;

import java.util.Random;

import hugman.mod.Main;
import hugman.mod.init.BlockInit;
import hugman.mod.init.ItemInit;
import hugman.mod.init.SoundTypeInit;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockBrick extends BlockBase implements IHasModel
{
	/** 
	 * Open class - can be initialized for multiple items with variables.
	 */
	public BlockBrick(String name)
	{
		super(name, Material.ROCK, 1.5f, 20f, SoundTypeInit.BRICK_BLOCK);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		if(this == BlockInit.BRICK_BLOCK)
		{
			int drop = rand.nextInt(3);
			if(drop == 2) return ItemInit.YELLOW_COIN;
			else return Item.getItemFromBlock(this);
		}
		if(this == BlockInit.GOLDEN_BRICK_BLOCK) return ItemInit.YELLOW_COIN;
		else return Item.getItemFromBlock(this);
	}
	
	@Override
	public int quantityDropped(Random rand)
	{
		if(this == BlockInit.GOLDEN_BRICK_BLOCK) return rand.nextInt(4) + 1;
		else return 1;
	}
	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
