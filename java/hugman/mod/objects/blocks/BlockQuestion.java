package hugman.mod.objects.blocks;

import java.util.Random;

import hugman.mod.Main;
import hugman.mod.init.BlockInit;
import hugman.mod.init.ItemInit;
import hugman.mod.util.handlers.SoundHandler;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockQuestion extends Block implements IHasModel
{
	public BlockQuestion()
	{
		super(Material.IRON);
		setTranslationKey("question_block");
		setRegistryName("question_block");
		setCreativeTab(Main.NINTENDO_BLOCKS);
		setHardness(1.5f);
		this.blockResistance = 30f;
		setSoundType(SoundType.METAL);
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        Block block = BlockInit.EMPTY_BLOCK;
        IBlockState state0=block.getDefaultState();
        final double x = pos.getX() + 0.5D;
        final double y = pos.getY() + 0.5D + 0.6D;
        final double z = pos.getZ() + 0.5D;
        if (!worldIn.isRemote)
        {
        	Random rand = new Random();
    		int loot = rand.nextInt(11);
        	if (loot >= 0 && loot <= 4) worldIn.spawnEntity(new EntityItem(worldIn, x, y, z, new ItemStack(ItemInit.YELLOW_COIN)));
            else if (loot >= 5 && loot <= 7 ) worldIn.spawnEntity(new EntityItem(worldIn, x, y, z, new ItemStack(ItemInit.RED_COIN)));
            else if (loot >= 8 && loot <= 9 ) worldIn.spawnEntity(new EntityItem(worldIn, x, y, z, new ItemStack(ItemInit.BLUE_COIN)));
            if (loot >= 0 && loot <= 9) worldIn.playSound(null, x, y, z, SoundHandler.BLOCK_QUESTION_BLOCK_LOOT_COIN, SoundCategory.BLOCKS, 1f, 1f);
            else if (loot == 10)
            {
            	worldIn.spawnEntity(new EntityItem(worldIn, x, y, z, new ItemStack(ItemInit.SUPER_MUSHROOM)));
            	worldIn.playSound(null, x, y, z, SoundHandler.BLOCK_QUESTION_BLOCK_LOOT_SUPER_MUSHROOM, SoundCategory.BLOCKS, 1f, 1f);
            }
    		worldIn.setBlockState(pos, state0);
        }
        return true;
    }

	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
