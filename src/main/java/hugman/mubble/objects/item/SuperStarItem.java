package hugman.mubble.objects.item;

import hugman.mubble.init.MubbleSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SStopSoundPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class SuperStarItem extends Item
{    
    public SuperStarItem(Item.Properties builder)
    {
        super(builder);
    }
    
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving)
    {
    	if(!worldIn.isRemote)
    	{
    		ServerWorld serverWorldIn = (ServerWorld)worldIn;
    		SStopSoundPacket sstopsoundpacket = new SStopSoundPacket(MubbleSounds.ITEM_SUPER_STAR_THEME.getName(), SoundCategory.PLAYERS);
    		for(ServerPlayerEntity serverplayerentity : serverWorldIn.getPlayers())
    		{
    			serverplayerentity.connection.sendPacket(sstopsoundpacket);
    		}
        	worldIn.playMovingSound((PlayerEntity)null, entityLiving, MubbleSounds.ITEM_SUPER_STAR_THEME, SoundCategory.PLAYERS, 1.0F, 1.0F);
    	}
		return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
