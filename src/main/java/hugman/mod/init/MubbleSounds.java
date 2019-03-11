package hugman.mod.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class MubbleSounds 
{	
	public static final List<SoundEvent> SOUNDS = new ArrayList<SoundEvent>();
	
	public static final SoundEvent
	BLOCK_BRICK_BLOCK_BREAK = sound("block.brick_block.break"),
	BLOCK_EMPTY_BLOCK_HIT = sound("block.empty_block.hit"),
	BLOCK_NOTE_BLOCK_JUMP_LOW = sound("block.note_block.jump.low"),
	BLOCK_NOTE_BLOCK_JUMP_HIGH = sound("block.note_block.jump.high"),
	BLOCK_QUESTION_BLOCK_LOOT_POWER_UP = sound("block.question_block.loot.power_up"),
	BLOCK_QUESTION_BLOCK_LOOT_COIN = sound("block.question_block.loot.coin"),
	BLOCK_SPACE_MATTER_BREAK = sound("block.space_matter.break"),
	BLOCK_SPACE_MATTER_FALL = sound("block.space_matter.fall"),
	BLOCK_SPACE_MATTER_PLACE = sound("block.space_matter.place"),
	BLOCK_SPACE_MATTER_HIT = sound("block.space_matter.hit"),
	BLOCK_SPACE_MATTER_STEP = sound("block.space_matter.step"),
	BLOCK_ULTIMATUM_PORTAL_AMBIENT = sound("block.ultimatum_portal.ambient"),
	ITEM_ANNOYING_DOG_DISAPPEAR = sound("item.annoying_dog.disappear"),
	ITEM_ANNOYING_DOG_BARK = sound("item.annoying_dog.bark"),
	ITEM_CAPE_FEATHER_USE = sound("item.cape_feather.use"),
	ITEM_SMASH_BALL_USE = sound("item.smash_ball.use"),
	ITEM_SUPER_MUSHROOM_CONSUME = sound("item.super_mushroom.consume"),
	ITEM_SUPER_STAR_THEME = sound("item.super_star.theme"),
	ENTITY_CHINCHO_AMBIENT = sound("entity.chincho.ambient"),
	ENTITY_CHINCHO_HURT = sound("entity.chincho.hurt"),
	ENTITY_CHINCHO_DEATH = sound("entity.chincho.death"),
	ENTITY_TOAD_AMBIENT = sound("entity.toad.ambient"),
	ENTITY_TOAD_HURT = sound("entity.toad.hurt"),
	ENTITY_TOAD_DEATH = sound("entity.toad.death"),
	COSTUME_CAPPY_AMBIENT = sound("costume.cappy.ambient"),
	COSTUME_CAPPY_AMBIENT_NETHER = sound("costume.cappy.ambient.nether"),
	COSTUME_CAPPY_EQUIP = sound("costume.cappy.equip"),
	COSTUME_CAPPY_HAPPY = sound("costume.cappy.happy"),
	COSTUME_CAPPY_HELP = sound("costume.cappy.help"),
	COSTUME_CAPPY_HELP_WATER = sound("costume.cappy.help.water"),
	RECORD_CHAMPIONS_ROAD = sound("record.champions_road"),
	RECORD_FLY_OCTO_FLY = sound("record.fly_octo_fly"),
	RECORD_VAMPIRE_KILLER = sound("record.vampire_killer"),
	RECORD_NB_BUOY_BASE_GALAXY = sound("record.nb.buoy_base_galaxy"),
	RECORD_NB_HARVEST_HAZARDS = sound("record.nb.harvest_hazards"),
	RECORD_NB_SWEDEN = sound("record.nb.sweden"),
	RECORD_NB_WALUIGI_PINBALL = sound("record.nb.waluigi_pinball");
	
	public static SoundEvent sound(String name)
	{
		ResourceLocation path = new ResourceLocation(Reference.MOD_ID, name);
		SoundEvent sound = new SoundEvent(path).setRegistryName(path);
		SOUNDS.add(sound);
		return sound;
	}
}