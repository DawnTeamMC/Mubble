package hugman.mubble.objects.item;

import hugman.mubble.init.MubbleSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;

public class SuperStarItem extends Item {
	public SuperStarItem(Item.Settings builder) {
		super(builder);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		if(!worldIn.isClient) {
			ServerWorld serverWorldIn = (ServerWorld) worldIn;
			StopSoundS2CPacket sstopsoundpacket = new StopSoundS2CPacket(MubbleSounds.ITEM_SUPER_STAR_THEME.getId(), SoundCategory.PLAYERS);
			for(ServerPlayerEntity serverplayerentity : serverWorldIn.getPlayers()) {
				serverplayerentity.networkHandler.sendPacket(sstopsoundpacket);
			}
		}
		worldIn.playSoundFromEntity(null, entityLiving, MubbleSounds.ITEM_SUPER_STAR_THEME, SoundCategory.PLAYERS, 1.0F, 1.0F);
		return super.finishUsing(stack, worldIn, entityLiving);
	}
}
