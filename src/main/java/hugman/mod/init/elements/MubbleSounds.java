package hugman.mod.init.elements;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.Mubble;
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
	BLOCK_SPACE_JAM_BREAK = sound("block.space_jam.break"),
	BLOCK_SPACE_JAM_FALL = sound("block.space_jam.fall"),
	BLOCK_SPACE_JAM_PLACE = sound("block.space_jam.place"),
	BLOCK_SPACE_JAM_HIT = sound("block.space_jam.hit"),
	BLOCK_SPACE_JAM_STEP = sound("block.space_jam.step"),
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
	MUSIC_DISC_BATTLEFIELD = sound("music_disc.battlefield"),
	MUSIC_DISC_CHAMPIONS_ROAD = sound("music_disc.champions_road"),
	MUSIC_DISC_CONFRONTING_MYSELF = sound("music_disc.confronting_myself"),
	MUSIC_DISC_ELECTROPLANKTON_MELDEY = sound("music_disc.electroplankton_medley"),
	MUSIC_DISC_FIELDS_OF_HOPES_AND_DREAMS = sound("music_disc.fields_of_hopes_and_dreams"),
	MUSIC_DISC_KASS_THEME = sound("music_disc.kass_theme"),
	MUSIC_DISC_LOST_PAINTING = sound("music_disc.lost_painting"),
	MUSIC_DISC_MAD_MEW_MEW = sound("music_disc.mad_mew_mew"),
	MUSIC_DISC_MEGALOVANIA = sound("music_disc.megalovania"),
	MUSIC_DISC_POPPLE_BATTLE = sound("music_disc.popple_battle"),
	MUSIC_DISC_REFLECTION = sound("music_disc.reflection"),
	MUSIC_DISC_RUDE_BUSTER = sound("music_disc.rude_buster"),
	MUSIC_DISC_SHARK_BYTES = sound("music_disc.shark_bytes"),
	MUSIC_DISC_SOUND_STAGE = sound("music_disc.sound_stage"),
	MUSIC_DISC_STUDIOPOLIS_ACT1 = sound("music_disc.studiopolis_act1"),
	MUSIC_DISC_THE_GRAND_FINALE = sound("music_disc.the_grand_finale"),
	MUSIC_DISC_TYPE_A = sound("music_disc.type_a"),
	MUSIC_DISC_VAMPIRE_KILLER = sound("music_disc.vampire_killer"),
	MUSIC_DISC_VS_CHAMPION_RED_AND_BLUE = sound("music_disc.vs_champion_red_and_blue"),
	REMIX_DISC_NB_BEWARE_THE_FOREST_MUSHROOMS = sound("remix_disc.nb.beware_the_forest_mushrooms"),
	REMIX_DISC_NB_BUOY_BASE_GALAXY = sound("remix_disc.nb.buoy_base_galaxy"),
	REMIX_DISC_NB_FLY_OCTO_FLY = sound("remix_disc.nb.fly_octo_fly"),
	REMIX_DISC_NB_HARVEST_HAZARDS = sound("remix_disc.nb.harvest_hazards"),
	REMIX_DISC_NB_SWEDEN = sound("remix_disc.nb.sweden"),
	REMIX_DISC_NB_WALUIGI_PINBALL = sound("remix_disc.nb.waluigi_pinball");
	
	public static SoundEvent sound(String name)
	{
		ResourceLocation path = new ResourceLocation(Mubble.MOD_ID, name);
		SoundEvent sound = new SoundEvent(path).setRegistryName(path);
		SOUNDS.add(sound);
		return sound;
	}
}