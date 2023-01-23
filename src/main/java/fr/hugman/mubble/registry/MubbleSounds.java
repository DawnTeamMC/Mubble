package fr.hugman.mubble.registry;

import fr.hugman.dawn.Registrar;
import fr.hugman.mubble.Mubble;
import net.minecraft.sound.SoundEvent;

public class MubbleSounds {
	// SUPER MARIO
	public static final SoundEvent BUMPABLE_BLOCK_DESTROY = SoundEvent.of(Mubble.id("block.bumpable_block.destroy"));
	public static final SoundEvent BUMPABLE_BLOCK_BUMP = SoundEvent.of(Mubble.id("block.bumpable_block.bump"));
	public static final SoundEvent BUMPABLE_BLOCK_CHANGE_LOOT = SoundEvent.of(Mubble.id("block.bumpable_block.change_loot"));
	public static final SoundEvent BUMPABLE_BLOCK_LOOT = SoundEvent.of(Mubble.id("block.bumpable_block.loot"));
	public static final SoundEvent BUMPABLE_BLOCK_LOOT_COIN = SoundEvent.of(Mubble.id("block.bumpable_block.loot_coin"));

	public static final SoundEvent NOTE_BLOCK_JUMP_LOW = SoundEvent.of(Mubble.id("block.note_block.jump.low"));
	public static final SoundEvent NOTE_BLOCK_JUMP_HIGH = SoundEvent.of(Mubble.id("block.note_block.jump.high"));
	public static final SoundEvent CAPE_FEATHER_USE = SoundEvent.of(Mubble.id("item.cape_feather.use"));

	public static void init() {
		Registrar.add(NOTE_BLOCK_JUMP_LOW);
		Registrar.add(NOTE_BLOCK_JUMP_HIGH);
		Registrar.add(BUMPABLE_BLOCK_DESTROY);
		Registrar.add(BUMPABLE_BLOCK_BUMP);
		Registrar.add(BUMPABLE_BLOCK_CHANGE_LOOT);
		Registrar.add(BUMPABLE_BLOCK_LOOT);
		Registrar.add(BUMPABLE_BLOCK_LOOT_COIN);
		Registrar.add(CAPE_FEATHER_USE);
	}
}
