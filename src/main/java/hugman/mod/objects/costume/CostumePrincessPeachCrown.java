package hugman.mod.objects.costume;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.objects.block.BlockKoretato;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CostumePrincessPeachCrown extends CostumeHead
{    
    public CostumePrincessPeachCrown(Item.Properties builder)
    {
        super(builder, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
    }
    
    @Override
    public EnumActionResult onItemUse(ItemUseContext context)
    {
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        IBlockState iblockstate = world.getBlockState(blockpos);
        if(iblockstate.getBlock() == MubbleBlocks.KORETATO_BLOCK && !iblockstate.get(BlockKoretato.PRINCESS))
        {
            world.spawnParticle(Particles.HEART, (double)((float)blockpos.getX() + 0.5F), (double)((float)blockpos.getY() + 1.1F), (double)((float)blockpos.getZ() + 0.5F), 0.0D, 0.0D, 0.0D);
        	if(world.isRemote) return EnumActionResult.SUCCESS;
        	else
        	{
        		IBlockState iblockstate1 = iblockstate.with(BlockKoretato.PRINCESS, true);
        		world.setBlockState(blockpos, iblockstate1, 2);
        		context.getItem().shrink(1);
        		world.playSound((EntityPlayer)null, blockpos, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.BLOCKS, 1.0F, 1.0F);
        		world.playSound((EntityPlayer)null, blockpos, SoundEvents.ITEM_ARMOR_EQUIP_IRON, SoundCategory.BLOCKS, 1.0F, 1.0F);
        		return EnumActionResult.SUCCESS;
        	}
        }
        else return EnumActionResult.PASS;
     }
}