package hugman.mod.objects.blocks;

import java.util.Random;

import com.google.common.collect.Lists;

import hugman.mod.Main;
import hugman.mod.init.BlockInit;
import hugman.mod.init.CostumeInit;
import hugman.mod.init.ItemInit;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFlower extends BlockBush implements IHasModel
{   
    /** 
     * Open class - can be initialized for multiple items with variables.
     */
	public BlockFlower(String name, int light)
	{
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(Main.MUBBLE_BLOCKS);
		setSoundType(SoundType.PLANT);
		this.lightValue = light;
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return super.getBoundingBox(state, source, pos).offset(state.getOffset(source, pos));
    }
    
	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
		if(this == BlockInit.CLOUD_FLOWER) {
			EntityPlayer playerIn;
			if(entityIn instanceof EntityPlayer)
			{
				playerIn = (EntityPlayer) entityIn;
				ItemStack armor;
				armor = playerIn.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
				if(armor.getItem() == CostumeInit.SUPER_CROWN)
				{
			        Random rand = new Random();
					if(!playerIn.isSneaking())
					{
						if(!playerIn.isSprinting()) entityIn.motionY = (rand.nextInt(16) + 20) / 100D;
						else playerIn.motionY = 0.35D;
					}
					playerIn.fallDistance = 0f;
				}
			}
			if(entityIn instanceof EntityItem)
			{
				EntityItem itemEntity = (EntityItem) entityIn;
				if(itemEntity.getItem().getItem() == CostumeInit.SUPER_CROWN) itemEntity.motionY = 0.15D;
				if(Lists.newArrayList(
						Items.FEATHER,
						ItemInit.CAPE_FEATHER,
						ItemInit.WHEAT_FLOUR,
						Item.getItemFromBlock(BlockInit.CLOUD_BLOCK),
						Item.getItemFromBlock(this)).contains(itemEntity.getItem().getItem())
				) itemEntity.motionY = 0.05D;
			}
		}
    }
    
    public Block.EnumOffsetType getOffsetType()
    {
        return Block.EnumOffsetType.XZ;
    }
	
	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
