package hugman.mod.objects.item;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleTabs;
import hugman.mod.util.handlers.SoundHandler;
import hugman.mod.util.interfaces.IHasModel;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemSuperStar extends ItemFood implements IHasModel
{
	/**
	 * Static class - can only be initialized once.
	 */
	public ItemSuperStar()
	{
		super(3, 2.4f, false);
		setTranslationKey("super_star");
		setRegistryName("super_star");
		setCreativeTab(MubbleTabs.MUBBLE_ITEMS);
		setAlwaysEdible();
		MubbleItems.ITEMS.add(this);
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		playerIn.addPotionEffect(new PotionEffect(Potion.getPotionById(1), 600, 1));
		playerIn.addPotionEffect(new PotionEffect(Potion.getPotionById(5), 600, 0));
		playerIn.addPotionEffect(new PotionEffect(Potion.getPotionById(10), 600, 1));
		playerIn.addPotionEffect(new PotionEffect(Potion.getPotionById(11), 600, 1));
		playerIn.addPotionEffect(new PotionEffect(Potion.getPotionById(16), 600, 0));
		
		/*if(!worldIn.isRemote && playerIn instanceof EntityPlayerMP)
		{
			EntityPlayerMP entityplayermp = (EntityPlayerMP) playerIn;

	        PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
	        packetbuffer.writeString(SoundCategory.PLAYERS.getName());
	        packetbuffer.writeString("mubble:item.super_star.theme");
	        entityplayermp.connection.sendPacket(new SPacketCustomPayload("MC|StopSound", packetbuffer));
		}*/
		
		playerIn.playSound(SoundHandler.ITEM_SUPER_STAR_THEME, 60000000, 1);
	}
	
	@Override
	public void registerModels()
	{
		Mubble.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
