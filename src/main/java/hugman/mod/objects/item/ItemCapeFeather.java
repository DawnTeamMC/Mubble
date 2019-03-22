package hugman.mod.objects.item;

import java.util.Random;

import hugman.mod.Mubble;
import hugman.mod.init.elements.MubbleItems;
import hugman.mod.init.elements.MubbleSounds;
import hugman.mod.init.technical.MubbleTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Particles;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemCapeFeather extends Item
{
    public ItemCapeFeather(String name)
    {
        super(new Item.Properties().group(MubbleTabs.MUBBLE_ITEMS));
        setRegistryName(Mubble.MOD_ID, name);
		MubbleItems.register(this);
    }
    
    public ItemCapeFeather(String name, EnumRarity rarity)
    {
        super(new Item.Properties().group(MubbleTabs.MUBBLE_ITEMS).rarity(rarity));
        setRegistryName(Mubble.MOD_ID, name);
		MubbleItems.register(this);
    }
    
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        Random rand = new Random();
        for (int i = 0; i < rand.nextInt(6) + 1; i++)
        {
        	worldIn.spawnParticle(Particles.CLOUD, playerIn.posX + (rand.nextInt(11) - 5) / 10F, playerIn.posY, playerIn.posZ + (rand.nextInt(11) - 5) / 10F, 0.0D, (rand.nextInt(3) + 1) / 10F, 0);
        }
        playerIn.motionY = 0.7D;
        playerIn.fallDistance = 0f;
        if (!playerIn.abilities.isCreativeMode && this != MubbleItems.SUPER_CAPE_FEATHER) stack.shrink(1);
        playerIn.addStat(StatList.ITEM_USED.get(this));
        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, MubbleSounds.ITEM_CAPE_FEATHER_USE, SoundCategory.PLAYERS, 0.5F, 1F);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
}
