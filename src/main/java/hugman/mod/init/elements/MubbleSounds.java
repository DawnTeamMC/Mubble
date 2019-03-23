package hugman.mod.init.elements;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.Mubble;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class MubbleSounds 
{	
	public static final List<SoundEvent> SOUNDS = new ArrayList<SoundEvent>();
	
	public static final SoundEvent BLOCK_BRICK_BLOCK_BREAK = sound("block.brick_block.break");
	public static final SoundEvent BLOCK_EMPTY_BLOCK_HIT = sound("block.empty_block.hit");
	public static final SoundEvent BLOCK_NOTE_BLOCK_JUMP_LOW = sound("block.note_block.jump.low");
	public static final SoundEvent BLOCK_NOTE_BLOCK_JUMP_HIGH = sound("block.note_block.jump.high");
	public static final SoundEvent BLOCK_QUESTION_BLOCK_LOOT_POWER_UP = sound("block.question_block.loot.power_up");
	public static final SoundEvent BLOCK_QUESTION_BLOCK_LOOT_COIN = sound("block.question_block.loot.coin");
	public static final SoundEvent BLOCK_SPACE_JAM_BREAK = sound("block.space_jam.break");
	public static final SoundEvent BLOCK_SPACE_JAM_FALL = sound("block.space_jam.fall");
	public static final SoundEvent BLOCK_SPACE_JAM_PLACE = sound("block.space_jam.place");
	public static final SoundEvent BLOCK_SPACE_JAM_HIT = sound("block.space_jam.hit");
	public static final SoundEvent BLOCK_SPACE_JAM_STEP = sound("block.space_jam.step");
	public static final SoundEvent BLOCK_SPRING_TRIGGER = sound("block.spring.trigger");
	public static final SoundEvent BLOCK_ULTIMATUM_PORTAL_AMBIENT = sound("block.ultimatum_portal.ambient");

	public static final SoundEvent ITEM_ANNOYING_DOG_DISAPPEAR = sound("item.annoying_dog.disappear");
	public static final SoundEvent ITEM_ANNOYING_DOG_BARK = sound("item.annoying_dog.bark");
	public static final SoundEvent ITEM_CAPE_FEATHER_USE = sound("item.cape_feather.use");
	public static final SoundEvent ITEM_SMASH_BALL_USE = sound("item.smash_ball.use");
	public static final SoundEvent ITEM_SUPER_MUSHROOM_CONSUME = sound("item.super_mushroom.consume");
	public static final SoundEvent ITEM_SUPER_STAR_THEME = sound("item.super_star.theme");
	
	public static final SoundEvent COSTUME_CAPPY_AMBIENT = sound("costume.cappy.ambient");
	public static final SoundEvent COSTUME_CAPPY_AMBIENT_NETHER = sound("costume.cappy.ambient.nether");
	public static final SoundEvent COSTUME_CAPPY_EQUIP = sound("costume.cappy.equip");
	public static final SoundEvent COSTUME_CAPPY_HAPPY = sound("costume.cappy.happy");
	public static final SoundEvent COSTUME_CAPPY_HELP = sound("costume.cappy.help");
	public static final SoundEvent COSTUME_CAPPY_HELP_WATER = sound("costume.cappy.help.water");

	public static final SoundEvent ENTITY_CHINCHO_AMBIENT = sound("entity.chincho.ambient");
	public static final SoundEvent ENTITY_CHINCHO_HURT = sound("entity.chincho.hurt");
	public static final SoundEvent ENTITY_CHINCHO_DEATH = sound("entity.chincho.death");
	public static final SoundEvent ENTITY_TOAD_AMBIENT = sound("entity.toad.ambient");
	public static final SoundEvent ENTITY_TOAD_HURT = sound("entity.toad.hurt");
	public static final SoundEvent ENTITY_TOAD_DEATH = sound("entity.toad.death");
	
	public static final SoundEvent MUSIC_DISC_BATTLEFIELD = sound("music_disc.battlefield");
	public static final SoundEvent MUSIC_DISC_CHAMPIONS_ROAD = sound("music_disc.champions_road");
	public static final SoundEvent MUSIC_DISC_CONFRONTING_MYSELF = sound("music_disc.confronting_myself");
	public static final SoundEvent MUSIC_DISC_ELECTROPLANKTON_MELDEY = sound("music_disc.electroplankton_medley");
	public static final SoundEvent MUSIC_DISC_FIELDS_OF_HOPES_AND_DREAMS = sound("music_disc.fields_of_hopes_and_dreams");
	public static final SoundEvent MUSIC_DISC_KASS_THEME = sound("music_disc.kass_theme");
	public static final SoundEvent MUSIC_DISC_LOST_PAINTING = sound("music_disc.lost_painting");
	public static final SoundEvent MUSIC_DISC_MAD_MEW_MEW = sound("music_disc.mad_mew_mew");
	public static final SoundEvent MUSIC_DISC_MEGALOVANIA = sound("music_disc.megalovania");
	public static final SoundEvent MUSIC_DISC_POPPLE_BATTLE = sound("music_disc.popple_battle");
	public static final SoundEvent MUSIC_DISC_REFLECTION = sound("music_disc.reflection");
	public static final SoundEvent MUSIC_DISC_RUDE_BUSTER = sound("music_disc.rude_buster");
	public static final SoundEvent MUSIC_DISC_SHARK_BYTES = sound("music_disc.shark_bytes");
	public static final SoundEvent MUSIC_DISC_SOUND_STAGE = sound("music_disc.sound_stage");
	public static final SoundEvent MUSIC_DISC_STUDIOPOLIS_ACT1 = sound("music_disc.studiopolis_act1");
	public static final SoundEvent MUSIC_DISC_THE_GRAND_FINALE = sound("music_disc.the_grand_finale");
	public static final SoundEvent MUSIC_DISC_TYPE_A = sound("music_disc.type_a");
	public static final SoundEvent MUSIC_DISC_VAMPIRE_KILLER = sound("music_disc.vampire_killer");
	public static final SoundEvent MUSIC_DISC_VS_CHAMPION_RED_AND_BLUE = sound("music_disc.vs_champion_red_and_blue");
	public static final SoundEvent REMIX_DISC_NB_BEWARE_THE_FOREST_MUSHROOMS = sound("remix_disc.nb.beware_the_forest_mushrooms");
	public static final SoundEvent REMIX_DISC_NB_BUOY_BASE_GALAXY = sound("remix_disc.nb.buoy_base_galaxy");
	public static final SoundEvent REMIX_DISC_NB_FLY_OCTO_FLY = sound("remix_disc.nb.fly_octo_fly");
	public static final SoundEvent REMIX_DISC_NB_HARVEST_HAZARDS = sound("remix_disc.nb.harvest_hazards");
	public static final SoundEvent REMIX_DISC_NB_SWEDEN = sound("remix_disc.nb.sweden");
	public static final SoundEvent REMIX_DISC_NB_WALUIGI_PINBALL = sound("remix_disc.nb.waluigi_pinball");
	
	public static SoundEvent sound(String name)
	{
		ResourceLocation path = new ResourceLocation(Mubble.MOD_ID, name);
		SoundEvent sound = new SoundEvent(path).setRegistryName(path);
		SOUNDS.add(sound);
		return sound;
	}
}