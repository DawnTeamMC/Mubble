package hugman.mod.objects.blocks;

import hugman.mod.Main;
import hugman.mod.init.BlockInit;
import hugman.mod.init.ItemInit;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRotating extends Block implements IHasModel
{
	public BlockRotating()
	{
		super(Material.ROCK);
		setTranslationKey("rotating_block");
		setRegistryName("rotating_block");
		setCreativeTab(Main.NINTENDO_BLOCKS);
		setHardness(1.5f);
		this.blockResistance = 20f;
		setSoundType(SoundType.STONE);
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}

	@Override
    public void onLanded(World worldIn, Entity entityIn)
    {
		if(entityIn.isSneaking() && entityIn.motionY < -0.1)
		{
			if(!worldIn.isRemote)  worldIn.destroyBlock(new BlockPos(entityIn).down(), false);
			entityIn.motionY = 0.625D;
		}
    }
}
