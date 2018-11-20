package hugman.mod.util.handlers;

import hugman.mod.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundHandler 
{
	public static SoundEvent
	BLOCK_QUESTION_BLOCK_LOOT_POWER_UP,
	BLOCK_QUESTION_BLOCK_LOOT_COIN,
	BLOCK_EMPTY_BLOCK_BREAK,
	BLOCK_BRICK_BLOCK_BREAK,
	BLOCK_NOTE_BLOCK_JUMP_SMALL,
	BLOCK_NOTE_BLOCK_JUMP_HIGH,
	BLOCK_SPACE_MATTER_BREAK,
	BLOCK_SPACE_MATTER_FALL,
	BLOCK_SPACE_MATTER_PLACE,
	BLOCK_SPACE_MATTER_HIT,
	BLOCK_SPACE_MATTER_STEP,
	ITEM_ANNOYING_DOG_DISAPPEAR,
	ITEM_ANNOYING_DOG_WAF,
	ITEM_CAPE_FEATHER_USE,
	ITEM_SMASH_BALL_USE,
	ITEM_SUPER_MUSHROOM_CONSUME,
	ITEM_SUPER_STAR_THEME,
	ENTITY_CHINCHO_AMBIENT,
	ENTITY_CHINCHO_HURT,
	ENTITY_CHINCHO_DEATH,
	ENTITY_TOAD_AMBIENT,
	ENTITY_TOAD_HURT,
	ENTITY_TOAD_DEATH,
	COSTUME_CAPPY_AMBIENT,
	COSTUME_CAPPY_AMBIENT_NETHER,
	COSTUME_CAPPY_EQUIP,
	COSTUME_CAPPY_HAPPY,
	COSTUME_CAPPY_HELP,
	COSTUME_CAPPY_HELP_WATER,
	RECORD_CHAMPIONS_ROAD,
	RECORD_FLY_OCTO_FLY,
	RECORD_VAMPIRE_KILLER,
	RECORD_NB_BUOY_BASE_GALAXY,
	RECORD_NB_HARVEST_HAZARDS,
	RECORD_NB_SWEDEN,
	RECORD_NB_WALUIGI_PINBALL;
	
	public static void registerSounds()
	{
		BLOCK_QUESTION_BLOCK_LOOT_POWER_UP = registerSound("block.question_block.loot.power_up");
		BLOCK_QUESTION_BLOCK_LOOT_COIN = registerSound("block.question_block.loot.coin");
		BLOCK_EMPTY_BLOCK_BREAK = registerSound("block.empty_block.break");
		BLOCK_BRICK_BLOCK_BREAK = registerSound("block.brick_block.break");
		BLOCK_NOTE_BLOCK_JUMP_SMALL = registerSound("block.note_block.jump.small");
		BLOCK_NOTE_BLOCK_JUMP_HIGH = registerSound("block.note_block.jump.high");
		BLOCK_SPACE_MATTER_BREAK = registerSound("block.space_matter.break");
		BLOCK_SPACE_MATTER_FALL = registerSound("block.space_matter.fall");
		BLOCK_SPACE_MATTER_PLACE = registerSound("block.space_matter.place");
		BLOCK_SPACE_MATTER_HIT = registerSound("block.space_matter.hit");
		BLOCK_SPACE_MATTER_STEP = registerSound("block.space_matter.step");
		ITEM_ANNOYING_DOG_DISAPPEAR = registerSound("item.annoying_dog.disappear");
		ITEM_ANNOYING_DOG_WAF = registerSound("item.annoying_dog.waf");
		ITEM_SMASH_BALL_USE = registerSound("item.smash_ball.use");
		ITEM_CAPE_FEATHER_USE = registerSound("item.cape_feather.use");
		ITEM_SUPER_MUSHROOM_CONSUME = registerSound("item.super_mushroom.consume");
		ITEM_SUPER_STAR_THEME = registerSound("item.super_star.theme");
		ENTITY_CHINCHO_AMBIENT = registerSound("entity.chincho.ambient");
		ENTITY_CHINCHO_HURT = registerSound("entity.chincho.hurt");
		ENTITY_CHINCHO_DEATH = registerSound("entity.chincho.death");
		ENTITY_TOAD_AMBIENT = registerSound("entity.toad.ambient");
		ENTITY_TOAD_HURT = registerSound("entity.toad.hurt");
		ENTITY_TOAD_DEATH = registerSound("entity.toad.death");
		COSTUME_CAPPY_AMBIENT = registerSound("costume.cappy.ambient");
		COSTUME_CAPPY_AMBIENT_NETHER = registerSound("costume.cappy.ambient.nether");
		COSTUME_CAPPY_EQUIP = registerSound("costume.cappy.equip");
		COSTUME_CAPPY_HAPPY = registerSound("costume.cappy.happy");
		COSTUME_CAPPY_HELP = registerSound("costume.cappy.help");
		COSTUME_CAPPY_HELP_WATER = registerSound("costume.cappy.help.water");
		RECORD_CHAMPIONS_ROAD = registerSound("record.champions_road");
		RECORD_FLY_OCTO_FLY = registerSound("record.fly_octo_fly");
		RECORD_VAMPIRE_KILLER = registerSound("record.vampire_killer");
		RECORD_NB_BUOY_BASE_GALAXY = registerSound("record.nb.buoy_base_galaxy");
		RECORD_NB_HARVEST_HAZARDS = registerSound("record.nb.harvest_hazards");
		RECORD_NB_SWEDEN = registerSound("record.nb.sweden");
		RECORD_NB_WALUIGI_PINBALL = registerSound("record.nb.waluigi_pinball");
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