package hugman.mod.init;

import hugman.mod.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class MubbleSounds 
{
	public static SoundEvent
	BLOCK_EMPTY_BLOCK_HIT,
	BLOCK_QUESTION_BLOCK_LOOT_POWER_UP,
	BLOCK_QUESTION_BLOCK_LOOT_COIN;
	
	public static void registerSounds()
	{
		BLOCK_EMPTY_BLOCK_HIT = registerSound("block.empty_block.hit");
		BLOCK_QUESTION_BLOCK_LOOT_POWER_UP = registerSound("block.question_block.loot.power_up");
		BLOCK_QUESTION_BLOCK_LOOT_COIN = registerSound("block.question_block.loot.coin");
	}
	
	private static SoundEvent registerSound(String name)
	{
		ResourceLocation location = new ResourceLocation(Reference.MOD_ID, name);
		SoundEvent event = new SoundEvent(location);
		event.setRegistryName(name);
		ForgeRegistries.SOUND_EVENTS.register(event);
		return event;
	}
}