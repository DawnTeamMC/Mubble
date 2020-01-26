package hugman.mubble.objects.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

public class TotemOfAscendingItem extends Item
{    
    public TotemOfAscendingItem(Item.Properties builder)
    {
        super(builder);
    }
    
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        BlockPos desPos = new BlockPos(playerIn.getX(), worldIn.getChunk(playerIn.getPosition()).getTopBlockY(Heightmap.Type.WORLD_SURFACE, (int)playerIn.getX(), (int)playerIn.getZ()), playerIn.getZ());
        
        worldIn.playSound((PlayerEntity)null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1f, 1f);
        if(desPos.getY() <= playerIn.getY())
        {
        	playerIn.setPosition(playerIn.getX(), playerIn.getY() + 20D, playerIn.getZ());
        }
        else
        {
        	playerIn.setPosition(playerIn.getX(), desPos.getY() + 2D, playerIn.getZ());
        }
        playerIn.fallDistance = 0f;
        
        if(!playerIn.abilities.isCreativeMode)
        {
        	stack.shrink(1);
        }
        playerIn.getCooldownTracker().setCooldown(this, 25);
        playerIn.addStat(Stats.ITEM_USED.get(this));
        
        return ActionResult.success(stack);
    }
}
