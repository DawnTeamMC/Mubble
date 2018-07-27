package hugman.mod.objects.blocks;

import java.util.Random;

import hugman.mod.Main;
import hugman.mod.init.BlockInit;
import hugman.mod.init.ItemInit;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBrick extends Block implements IHasModel
{
	String type;
	public BlockBrick(String type)
	{
		super(Material.ROCK);
		this.type = type;
		if(type == "normal")
		{
			setTranslationKey("brick_block");
			setRegistryName("brick_block");
		}
		if(type == "golden")
		{
			setTranslationKey("golden_brick_block");
			setRegistryName("golden_brick_block");
		}
		setCreativeTab(Main.NINTENDO_BLOCKS);
		setHardness(1.5f);
		this.blockResistance = 20f;
		setSoundType(SoundType.STONE);
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		if(type == "normal")
		{
			int drop = rand.nextInt(3);
			if(drop == 2) return ItemInit.YELLOW_COIN;
			else return Item.getItemFromBlock(this);
		}
		if(type == "golden") return ItemInit.YELLOW_COIN;
		else return Item.getItemFromBlock(this);
	}
	
	@Override
	public int quantityDropped(Random rand)
	{
		if(type == "golden") return rand.nextInt(4) + 1;
		else return 1;
	}
	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
