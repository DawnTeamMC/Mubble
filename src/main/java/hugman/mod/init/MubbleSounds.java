package hugman.mod.init;

import hugman.mod.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class MubbleSounds 
{
	public static SoundEvent
	BLOCK_BRICK_BLOCK_BREAK,
	BLOCK_EMPTY_BLOCK_HIT,
	BLOCK_NOTE_BLOCK_JUMP_LOW,
	BLOCK_NOTE_BLOCK_JUMP_HIGH,
	BLOCK_QUESTION_BLOCK_LOOT_POWER_UP,
	BLOCK_QUESTION_BLOCK_LOOT_COIN,
	BLOCK_SPACE_MATTER_BREAK,
	BLOCK_SPACE_MATTER_FALL,
	BLOCK_SPACE_MATTER_PLACE,
	BLOCK_SPACE_MATTER_HIT,
	BLOCK_SPACE_MATTER_STEP,
	BLOCK_ULTIMATUM_PORTAL_AMBIENT,
	ITEM_ANNOYING_DOG_BARK,
	ITEM_ANNOYING_DOG_DISAPPEAR,
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
		BLOCK_BRICK_BLOCK_BREAK = register("block.brick_block.break");
		BLOCK_EMPTY_BLOCK_HIT = register("block.empty_block.hit");
		BLOCK_NOTE_BLOCK_JUMP_LOW = register("block.note_block.jump.low");
		BLOCK_NOTE_BLOCK_JUMP_HIGH = register("block.note_block.jump.high");
		BLOCK_QUESTION_BLOCK_LOOT_POWER_UP = register("block.question_block.loot.power_up");
		BLOCK_QUESTION_BLOCK_LOOT_COIN = register("block.question_block.loot.coin");
		BLOCK_SPACE_MATTER_BREAK = register("block.space_matter.break");
		BLOCK_SPACE_MATTER_FALL = register("block.space_matter.fall");
		BLOCK_SPACE_MATTER_PLACE = register("block.space_matter.place");
		BLOCK_SPACE_MATTER_HIT = register("block.space_matter.hit");
		BLOCK_SPACE_MATTER_STEP = register("block.space_matter.step");
		BLOCK_ULTIMATUM_PORTAL_AMBIENT = register("block.ultimatum_portal.ambient");
		ITEM_ANNOYING_DOG_DISAPPEAR = register("item.annoying_dog.disappear");
		ITEM_ANNOYING_DOG_BARK = register("item.annoying_dog.bark");
		ITEM_CAPE_FEATHER_USE = register("item.cape_feather.use");
		ITEM_SMASH_BALL_USE = register("item.smash_ball.use");
		ITEM_SUPER_MUSHROOM_CONSUME = register("item.super_mushroom.consume");
		ITEM_SUPER_STAR_THEME = register("item.super_star.theme");
		ENTITY_CHINCHO_AMBIENT = register("entity.chincho.ambient");
		ENTITY_CHINCHO_HURT = register("entity.chincho.hurt");
		ENTITY_CHINCHO_DEATH = register("entity.chincho.death");
		ENTITY_TOAD_AMBIENT = register("entity.toad.ambient");
		ENTITY_TOAD_HURT = register("entity.toad.hurt");
		ENTITY_TOAD_DEATH = register("entity.toad.death");
		COSTUME_CAPPY_AMBIENT = register("costume.cappy.ambient");
		COSTUME_CAPPY_AMBIENT_NETHER = register("costume.cappy.ambient.nether");
		COSTUME_CAPPY_EQUIP = register("costume.cappy.equip");
		COSTUME_CAPPY_HAPPY = register("costume.cappy.happy");
		COSTUME_CAPPY_HELP = register("costume.cappy.help");
		COSTUME_CAPPY_HELP_WATER = register("costume.cappy.help.water");
		RECORD_CHAMPIONS_ROAD = register("record.champions_road");
		RECORD_FLY_OCTO_FLY = register("record.fly_octo_fly");
		RECORD_VAMPIRE_KILLER = register("record.vampire_killer");
		RECORD_NB_BUOY_BASE_GALAXY = register("record.nb.buoy_base_galaxy");
		RECORD_NB_HARVEST_HAZARDS = register("record.nb.harvest_hazards");
		RECORD_NB_SWEDEN = register("record.nb.sweden");
		RECORD_NB_WALUIGI_PINBALL = register("record.nb.waluigi_pinball");
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