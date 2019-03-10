package hugman.mod.init;

import hugman.mod.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class MubbleSounds 
{
	public static SoundEvent
	BLOCK_EMPTY_BLOCK_HIT,
	BLOCK_NOTE_BLOCK_JUMP_HIGH,
	BLOCK_NOTE_BLOCK_JUMP_LOW,
	BLOCK_QUESTION_BLOCK_LOOT_POWER_UP,
	BLOCK_QUESTION_BLOCK_LOOT_COIN,
	ITEM_ANNOYING_DOG_BARK,
	ITEM_ANNOYING_DOG_DISAPPEAR,
	ITEM_CAPE_FEATHER_USE,
	ITEM_SMASH_BALL_USE,
	ITEM_SUPER_STAR_THEME;
	
	public static void registerSounds()
	{
		BLOCK_EMPTY_BLOCK_HIT = register("block.empty_block.hit");
		BLOCK_NOTE_BLOCK_JUMP_HIGH = register("block.note_block.jump.high");
		BLOCK_NOTE_BLOCK_JUMP_LOW = register("block.note_block.jump.low");
		BLOCK_QUESTION_BLOCK_LOOT_POWER_UP = register("block.question_block.loot.power_up");
		BLOCK_QUESTION_BLOCK_LOOT_COIN = register("block.question_block.loot.coin");
		ITEM_ANNOYING_DOG_DISAPPEAR = register("item.annoying_dog.bark");
		ITEM_CAPE_FEATHER_USE = register("item.cape_feather.use");
		ITEM_SMASH_BALL_USE = register("item.smash_ball.use");
		ITEM_SUPER_STAR_THEME = register("item.super_star.theme");
	}
	
	private static SoundEvent register(String name)
	{
		ResourceLocation location = new ResourceLocation(Reference.MOD_ID, name);
		SoundEvent event = new SoundEvent(location);
		event.setRegistryName(name);
		ForgeRegistries.SOUND_EVENTS.register(event);
		return event;
	}
}