package hugman.mod.objects.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

public class ItemTotemOfAscending extends Item
{    
    public ItemTotemOfAscending(Item.Properties builder)
    {
        super(builder);
    }
    
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        BlockPos desPos = new BlockPos(playerIn.posX, worldIn.getChunk(playerIn.getPosition()).getTopBlockY(Heightmap.Type.WORLD_SURFACE, (int)playerIn.posX, (int)playerIn.posZ), playerIn.posZ);
        if(desPos.getY() <= playerIn.posY) playerIn.setPosition(playerIn.posX, playerIn.posY + 20D, playerIn.posZ);
        else playerIn.setPosition(playerIn.posX, desPos.getY() + 2D, playerIn.posZ);
        if (!playerIn.abilities.isCreativeMode) stack.shrink(1);
        playerIn.fallDistance = 0f;
        playerIn.getCooldownTracker().setCooldown(this, 25);
        playerIn.addStat(StatList.ITEM_USED.get(this));
        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1f, 1f);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
}
