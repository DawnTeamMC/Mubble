package hugman.mubble.objects.costume;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.objects.block.KoretatoBlock;
import hugman.mubble.objects.block.block_state_property.Princess;
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

public class CrownCostume extends HeadCostume
{
	protected final Princess princess;
	
    public CrownCostume(Item.Properties builder, Princess princess)
    {
        super(builder, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
        this.princess = princess;
    }
    
    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        World world = context.getWorld();
        BlockPos blockPos = context.getPos();
        BlockState iblockstate = world.getBlockState(blockPos);
        if(iblockstate.getBlock() == MubbleBlocks.KORETATO_BLOCK && iblockstate.get(KoretatoBlock.PRINCESS) == Princess.NONE)
        {
            world.addParticle(ParticleTypes.HEART, (double)((float)blockPos.getX() + 0.5F), (double)((float)blockPos.getY() + 1.1F), (double)((float)blockPos.getZ() + 0.5F), 0.0D, 0.0D, 0.0D);
        	if(world.isRemote) return ActionResultType.SUCCESS;
        	else
        	{
        		BlockState blockState1 = iblockstate.with(KoretatoBlock.PRINCESS, princess);
        		world.setBlockState(blockPos, blockState1, 2);
        		context.getItem().shrink(1);
        		world.playSound((PlayerEntity)null, blockPos, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.BLOCKS, 1.0F, 1.0F);
        		world.playSound((PlayerEntity)null, blockPos, this.getEquipSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
        		return ActionResultType.SUCCESS;
        	}
        }
        else return ActionResultType.PASS;
    }
}