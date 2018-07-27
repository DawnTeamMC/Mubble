package hugman.mod.util.handlers;

import hugman.mod.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundHandler 
{
	public static SoundEvent
	BLOCK_QUESTION_BLOCK_LOOT,
	BLOCK_QUESTION_BLOCK_LOOT_COIN,
	BLOCK_EMPTY_BLOCK_HIT,
	BLOCK_BRICK_BLOCK_BREAK,
	BLOCK_NOTE_BLOCK_JUMP,
	BLOCK_NOTE_BLOCK_JUMP_HIGH,
	ITEM_SUPER_MUSHROOM_CONSUME,
	ITEM_SUPER_STAR_THEME,
	ENTITY_TOAD_AMBIENT,
	ENTITY_TOAD_HURT,
	ENTITY_TOAD_DEATH,
	RECORD_EBB_FLOW_DEMO,
	RECORD_CHAMPIONS_ROAD;
	
	public static void registerSounds()
	{
		BLOCK_QUESTION_BLOCK_LOOT = registerSound("block.question_block.loot");
		BLOCK_QUESTION_BLOCK_LOOT_COIN = registerSound("block.question_block.loot_coin");
		BLOCK_EMPTY_BLOCK_HIT = registerSound("block.empty_block.hit");
		BLOCK_BRICK_BLOCK_BREAK = registerSound("block.brick_block.break");
		BLOCK_NOTE_BLOCK_JUMP = registerSound("block.note_block.jump");
		BLOCK_NOTE_BLOCK_JUMP_HIGH = registerSound("block.note_block.jump_high");
		ITEM_SUPER_MUSHROOM_CONSUME = registerSound("item.super_mushroom.consume");
		ITEM_SUPER_STAR_THEME = registerSound("item.super_star.theme");
		ENTITY_TOAD_AMBIENT = registerSound("entity.toad.ambient");
		ENTITY_TOAD_HURT = registerSound("entity.toad.hurt");
		ENTITY_TOAD_DEATH = registerSound("entity.toad.death");
		RECORD_EBB_FLOW_DEMO = registerSound("record.ebb_flow_demo");
		RECORD_CHAMPIONS_ROAD = registerSound("record.champions_road");
	}
	
	private static SoundEvent registerSound(String name)
	{
		ResourceLocation location = new ResourceLocation(Reference.MODID, name);
		SoundEvent event = new SoundEvent(location);
		event.setRegistryName(name);
		ForgeRegistries.SOUND_EVENTS.register(event);
		return event;
	}
}