package hugman.mubble.init;

import hugman.mubble.Mubble;
import hugman.mubble.util.DataWriter;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MubbleSounds {
	public static final SoundEvent BLOCK_PRESENT_CLOSE = register("block.present.close");
	public static final SoundEvent BLOCK_PRESENT_OPEN = register("block.present.open");
	public static final SoundEvent BLOCK_QUESTION_BLOCK_LOOT_POWER_UP_SMB = register("block.question_block.loot.power_up.smb");
	public static final SoundEvent BLOCK_QUESTION_BLOCK_LOOT_POWER_UP_SMB3 = register("block.question_block.loot.power_up.smb3");
	public static final SoundEvent BLOCK_QUESTION_BLOCK_LOOT_POWER_UP_SMW = register("block.question_block.loot.power_up.smw");
	public static final SoundEvent BLOCK_QUESTION_BLOCK_LOOT_POWER_UP_NSMBU = register("block.question_block.loot.power_up.nsmbu");
	public static final SoundEvent BLOCK_QUESTION_BLOCK_LOOT_COIN_SMB = register("block.question_block.loot.coin.smb");
	public static final SoundEvent BLOCK_QUESTION_BLOCK_LOOT_COIN_SMB3 = register("block.question_block.loot.coin.smb3");
	public static final SoundEvent BLOCK_QUESTION_BLOCK_LOOT_COIN_SMW = register("block.question_block.loot.coin.smw");
	public static final SoundEvent BLOCK_QUESTION_BLOCK_LOOT_COIN_NSMBU = register("block.question_block.loot.coin.nsmbu");
	public static final SoundEvent BLOCK_BRICK_BLOCK_BREAK_SMB = register("block.brick_block.break.smb");
	public static final SoundEvent BLOCK_BRICK_BLOCK_BREAK_SMB3 = register("block.brick_block.break.smb3");
	public static final SoundEvent BLOCK_BRICK_BLOCK_BREAK_SMW = register("block.brick_block.break.smw");
	public static final SoundEvent BLOCK_BRICK_BLOCK_BREAK_NSMBU = register("block.brick_block.break.nsmbu");
	public static final SoundEvent BLOCK_EMPTY_BLOCK_HIT_SMB = register("block.empty_block.hit.smb");
	public static final SoundEvent BLOCK_EMPTY_BLOCK_HIT_SMB3 = register("block.empty_block.hit.smb3");
	public static final SoundEvent BLOCK_EMPTY_BLOCK_HIT_SMW = register("block.empty_block.hit.smw");
	public static final SoundEvent BLOCK_EMPTY_BLOCK_HIT_NSMBU = register("block.empty_block.hit.nsmbu");
	public static final SoundEvent BLOCK_NOTE_BLOCK_JUMP_SMB = register("block.note_block.jump.smb");
	public static final SoundEvent BLOCK_NOTE_BLOCK_JUMP_SMB3 = register("block.note_block.jump.smb3");
	public static final SoundEvent BLOCK_NOTE_BLOCK_JUMP_SMW = register("block.note_block.jump.smw");
	public static final SoundEvent BLOCK_NOTE_BLOCK_JUMP_LOW_NSMBU = register("block.note_block.jump.low.nsmbu");
	public static final SoundEvent BLOCK_NOTE_BLOCK_JUMP_HIGH_NSMBU = register("block.note_block.jump.high.nsmbu");
	public static final SoundEvent BLOCK_DOOR_OPEN_SMB = register("block.door.open.smb");
	public static final SoundEvent BLOCK_DOOR_OPEN_SMB3 = register("block.door.open.smb3");
	public static final SoundEvent BLOCK_DOOR_OPEN_SMW = register("block.door.open.smw");
	public static final SoundEvent BLOCK_DOOR_OPEN_NSMBU = register("block.door.open.nsmbu");
	public static final SoundEvent BLOCK_DOOR_CLOSE_SMB = register("block.door.close.smb");
	public static final SoundEvent BLOCK_DOOR_CLOSE_SMB3 = register("block.door.close.smb3");
	public static final SoundEvent BLOCK_DOOR_CLOSE_SMW = register("block.door.close.smw");
	public static final SoundEvent BLOCK_DOOR_CLOSE_NSMBU = register("block.door.close.nsmbu");
	public static final SoundEvent BLOCK_DOOR_KEY_SUCCESS_SMB = register("block.door.key.success.smb");
	public static final SoundEvent BLOCK_DOOR_KEY_SUCCESS_SMB3 = register("block.door.key.success.smb3");
	public static final SoundEvent BLOCK_DOOR_KEY_SUCCESS_SMW = register("block.door.key.success.smw");
	public static final SoundEvent BLOCK_DOOR_KEY_SUCCESS_NSMBU = register("block.door.key.success.nsmbu");
	public static final SoundEvent BLOCK_DOOR_KEY_FAIL_SMB = register("block.door.key.fail.smb");
	public static final SoundEvent BLOCK_DOOR_KEY_FAIL_SMB3 = register("block.door.key.fail.smb3");
	public static final SoundEvent BLOCK_DOOR_KEY_FAIL_SMW = register("block.door.key.fail.smw");
	public static final SoundEvent BLOCK_DOOR_KEY_FAIL_NSMBU = register("block.door.key.fail.nsmbu");
	public static final SoundEvent BLOCK_DREAM_BLOCK_BREAK = register("block.dream_block.break");
	public static final SoundEvent BLOCK_DREAM_BLOCK_FALL = register("block.dream_block.fall");
	public static final SoundEvent BLOCK_DREAM_BLOCK_PLACE = register("block.dream_block.place");
	public static final SoundEvent BLOCK_DREAM_BLOCK_HIT = register("block.dream_block.hit");
	public static final SoundEvent BLOCK_DREAM_BLOCK_STEP = register("block.dream_block.step");
	public static final SoundEvent BLOCK_SPRING_TRIGGER = register("block.spring.trigger");

	public static final SoundEvent ITEM_CAPE_FEATHER_USE = register("item.cape_feather.use");
	public static final SoundEvent ITEM_JINGLE_BELLS_USE = register("item.jingle_bells.use");
	public static final SoundEvent ITEM_SMASH_BALL_USE = register("item.smash_ball.use");
	public static final SoundEvent ITEM_SUPER_MUSHROOM_CONSUME = register("item.super_mushroom.consume");
	public static final SoundEvent ITEM_SUPER_STAR_THEME = register("item.super_star.theme");

	public static final SoundEvent ITEM_LIGHTSABER_BLOCK = register("item.lightsaber.block");
	public static final SoundEvent ITEM_LIGHTSABER_HIT = register("item.lightsaber.hit");
	public static final SoundEvent ITEM_LIGHTSABER_IDLE = register("item.lightsaber.idle");
	public static final SoundEvent ITEM_LIGHTSABER_PULL_IN = register("item.lightsaber.pull_in");
	public static final SoundEvent ITEM_LIGHTSABER_PULL_OUT = register("item.lightsaber.pull_out");
	public static final SoundEvent ITEM_LIGHTSABER_SPARK = register("item.lightsaber.spark");
	public static final SoundEvent ITEM_LIGHTSABER_SWIPE = register("item.lightsaber.swipe");
	public static final SoundEvent ITEM_LIGHTSABER_THROW_FAR = register("item.lightsaber.throw.far");
	public static final SoundEvent ITEM_LIGHTSABER_THROW_NEAR = register("item.lightsaber.throw.near");

	public static final SoundEvent COSTUME_CAPPY_AMBIENT = register("costume.cappy.ambient");
	public static final SoundEvent COSTUME_CAPPY_AMBIENT_NETHER = register("costume.cappy.ambient.nether");
	public static final SoundEvent COSTUME_CAPPY_EQUIP = register("costume.cappy.equip");
	public static final SoundEvent COSTUME_CAPPY_HAPPY = register("costume.cappy.happy");
	public static final SoundEvent COSTUME_CAPPY_HELP = register("costume.cappy.help");
	public static final SoundEvent COSTUME_CAPPY_HELP_WATER = register("costume.cappy.help.water");

	public static final SoundEvent ENTITY_CHINCHO_AMBIENT = register("entity.chincho.ambient");
	public static final SoundEvent ENTITY_CHINCHO_HURT = register("entity.chincho.hurt");
	public static final SoundEvent ENTITY_CHINCHO_DEATH = register("entity.chincho.death");
	public static final SoundEvent ENTITY_DUCK_AMBIENT = register("entity.duck.ambient");
	public static final SoundEvent ENTITY_DUCK_HURT = register("entity.duck.hurt");
	public static final SoundEvent ENTITY_DUCK_DEATH = register("entity.duck.death");
	public static final SoundEvent ENTITY_DUCK_STEP = register("entity.duck.step");
	public static final SoundEvent ENTITY_FIREBALL_HIT_BLOCK = register("entity.fireball.hit.block");
	public static final SoundEvent ENTITY_FIREBALL_HIT_ENTITY = register("entity.fireball.hit.entity");
	public static final SoundEvent ENTITY_FIREBALL_HIT_MELTABLE = register("entity.fireball.hit.meltable");
	public static final SoundEvent ENTITY_FIREBALL_THROW = register("entity.fireball.throw");
	public static final SoundEvent ENTITY_ICEBALL_HIT_BLOCK = register("entity.iceball.hit.block");
	public static final SoundEvent ENTITY_ICEBALL_HIT_ENTITY = register("entity.iceball.hit.entity");
	public static final SoundEvent ENTITY_ICEBALL_THROW = register("entity.iceball.throw");
	public static final SoundEvent ENTITY_KIRBY_BALL_HIT_BLOCK = register("entity.kirby_ball.hit.block");
	public static final SoundEvent ENTITY_KIRBY_BALL_HIT_ENTITY = register("entity.kirby_ball.hit.entity");
	public static final SoundEvent ENTITY_KIRBY_BALL_REBOUND = register("entity.kirby_ball.rebound");
	public static final SoundEvent ENTITY_KIRBY_BALL_THROW = register("entity.kirby_ball.throw");
	public static final SoundEvent ENTITY_TOAD_AMBIENT = register("entity.toad.ambient");
	public static final SoundEvent ENTITY_TOAD_BUP = register("entity.toad.bup");
	public static final SoundEvent ENTITY_TOAD_NO = register("entity.toad.no");
	public static final SoundEvent ENTITY_TOAD_TRADE = register("entity.toad.trade");
	public static final SoundEvent ENTITY_TOAD_YES = register("entity.toad.yes");
	public static final SoundEvent ENTITY_TOAD_HURT = register("entity.toad.hurt");
	public static final SoundEvent ENTITY_TOAD_DEATH = register("entity.toad.death");
	public static final SoundEvent ENTITY_GOOMBA_STEP = register("entity.goomba.step");
	public static final SoundEvent ENTITY_GOOMBA_CRUSH = register("entity.goomba.crush");
	public static final SoundEvent ENTITY_GOOMBA_HURT = register("entity.goomba.hurt");
	public static final SoundEvent ENTITY_GOOMBA_DEATH = register("entity.goomba.death");
	public static final SoundEvent ENTITY_PUFFERFISH_AEUGH = register("entity.pufferfish.aeugh");
	public static final SoundEvent ENTITY_ZOMBIE_COWMAN_AMBIENT = register("entity.zombie_cowman.ambient");
	public static final SoundEvent ENTITY_ZOMBIE_COWMAN_STEP = register("entity.zombie_cowman.step");
	public static final SoundEvent ENTITY_ZOMBIE_COWMAN_ANGRY = register("entity.zombie_cowman.angry");
	public static final SoundEvent ENTITY_ZOMBIE_COWMAN_HURT = register("entity.zombie_cowman.hurt");
	public static final SoundEvent ENTITY_ZOMBIE_COWMAN_DEATH = register("entity.zombie_cowman.death");

	public static final SoundEvent UI_TIMESWAP_TABLE_TAKE_RESULT = register("ui.timeswap_table.take_result");

	private static SoundEvent register(String name) {
		DataWriter.soundsEntries.add(Mubble.id(name));
		return Registry.register(Registry.SOUND_EVENT, Mubble.id(name), new SoundEvent(Mubble.id(name)));
	}
}