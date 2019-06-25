package hugman.mod.objects.costume;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.objects.block.KoretatoBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PrincessPeachCrownCostume extends HeadCostume
{    
    public PrincessPeachCrownCostume(Item.Properties builder)
    {
        super(builder, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
    }
    
    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        BlockState iblockstate = world.getBlockState(blockpos);
        if(iblockstate.getBlock() == MubbleBlocks.KORETATO_BLOCK && !iblockstate.get(KoretatoBlock.PRINCESS))
        {
            world.addParticle(ParticleTypes.HEART, (double)((float)blockpos.getX() + 0.5F), (double)((float)blockpos.getY() + 1.1F), (double)((float)blockpos.getZ() + 0.5F), 0.0D, 0.0D, 0.0D);
        	if(world.isRemote) return ActionResultType.SUCCESS;
        	else
        	{
        		BlockState iblockstate1 = iblockstate.with(KoretatoBlock.PRINCESS, true);
        		world.setBlockState(blockpos, iblockstate1, 2);
        		context.getItem().shrink(1);
        		world.playSound((PlayerEntity)null, blockpos, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.BLOCKS, 1.0F, 1.0F);
        		world.playSound((PlayerEntity)null, blockpos, SoundEvents.ITEM_ARMOR_EQUIP_IRON, SoundCategory.BLOCKS, 1.0F, 1.0F);
        		return ActionResultType.SUCCESS;
        	}
        }
        else return ActionResultType.PASS;
     }
}