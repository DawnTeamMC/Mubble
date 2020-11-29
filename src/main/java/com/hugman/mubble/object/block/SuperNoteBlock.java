package com.hugman.mubble.object.block;

import net.minecraft.sound.SoundEvent;

public class SuperNoteBlock extends NoteBlock {
	public SuperNoteBlock(SoundEvent lowJumpSound, SoundEvent highJumpSound, Settings builder) {
		super(lowJumpSound, highJumpSound, builder);
	}
	public SuperNoteBlock(SoundEvent jumpSound, Settings builder) {
		super(jumpSound, builder);
	}

	@Override
	public double getProperLaunchMotion() {
		return 1.5D;
	}
}
