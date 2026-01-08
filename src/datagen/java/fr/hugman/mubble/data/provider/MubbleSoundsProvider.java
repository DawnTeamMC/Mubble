package fr.hugman.mubble.data.provider;

import fr.hugman.mubble.sounds.MubbleSounds;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import java.util.concurrent.CompletableFuture;

public class MubbleSoundsProvider extends FabricSoundsProvider {
	public MubbleSoundsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	public String getName() {
		return "Sounds";
	}

	@Override
	protected void configure(HolderLookup.Provider wrapperLookup, SoundExporter soundExporter) {
		// Blocks
		soundExporter.add(MubbleSounds.BUMPABLE_BLOCK_BUMP, variantSoundBuilder(MubbleSounds.BUMPABLE_BLOCK_BUMP, 1));
		soundExporter.add(MubbleSounds.BUMPABLE_BLOCK_CHANGE_LOOT, variantSoundBuilder(MubbleSounds.BUMPABLE_BLOCK_CHANGE_LOOT, 1));
		soundExporter.add(MubbleSounds.BUMPABLE_BLOCK_DESTROY, variantSoundBuilder(MubbleSounds.BUMPABLE_BLOCK_DESTROY, 1));
		soundExporter.add(MubbleSounds.BUMPABLE_BLOCK_LOOT, variantSoundBuilder(MubbleSounds.BUMPABLE_BLOCK_LOOT, 1));
		soundExporter.add(MubbleSounds.BUMPABLE_BLOCK_LOOT_COIN, variantSoundBuilder(MubbleSounds.BUMPABLE_BLOCK_LOOT_COIN, 1));
		soundExporter.add(MubbleSounds.NOTE_BLOCK_JUMP_HIGH, variantSoundBuilder(MubbleSounds.NOTE_BLOCK_JUMP_HIGH, 1).subtitle("subtitles.mubble.block.note_block.jump"));
		soundExporter.add(MubbleSounds.NOTE_BLOCK_JUMP_LOW, variantSoundBuilder(MubbleSounds.NOTE_BLOCK_JUMP_LOW, 2).subtitle("subtitles.mubble.block.note_block.jump"));

		// Items
		soundExporter.add(MubbleSounds.CAPE_FEATHER_USE, variantSoundBuilder(MubbleSounds.CAPE_FEATHER_USE,1));

		// Entities
		soundExporter.add(MubbleSounds.GOOMBA_WALK_STEP, variantSoundBuilder(MubbleSounds.GOOMBA_WALK_STEP, 1).subtitle("subtitles.block.generic.footsteps"));
		soundExporter.add(MubbleSounds.GOOMBA_RUN_STEP, variantSoundBuilder(MubbleSounds.GOOMBA_RUN_STEP, 1).subtitle("subtitles.block.generic.footsteps"));
		soundExporter.add(MubbleSounds.GOOMBA_FIND_TARGET, variantSoundBuilder(MubbleSounds.GOOMBA_FIND_TARGET, 1));
		soundExporter.add(MubbleSounds.GOOMBA_DEATH, variantSoundBuilder(MubbleSounds.GOOMBA_DEATH, 1));
		soundExporter.add(MubbleSounds.GOOMBA_STOMP, variantSoundBuilder(MubbleSounds.GOOMBA_STOMP, 1));

		soundExporter.add(MubbleSounds.KOOPA_SHELL_SLIDE, variantSoundBuilder(MubbleSounds.KOOPA_SHELL_SLIDE, 1));
		soundExporter.add(MubbleSounds.KOOPA_SHELL_HOMING, variantSoundBuilder(MubbleSounds.KOOPA_SHELL_HOMING, 1));
		soundExporter.add(MubbleSounds.KOOPA_SHELL_HIT_BLOCK, variantSoundBuilder(MubbleSounds.KOOPA_SHELL_HIT_BLOCK, 1));
		soundExporter.add(MubbleSounds.KOOPA_SHELL_BREAK, variantSoundBuilder(MubbleSounds.KOOPA_SHELL_BREAK, 1));
		soundExporter.add(MubbleSounds.KOOPA_SHELL_KICK, variantSoundBuilder(MubbleSounds.KOOPA_SHELL_KICK, 1));

		soundExporter.add(MubbleSounds.FIREBALL_HIT_BLOCK, variantSoundBuilder(MubbleSounds.FIREBALL_HIT_BLOCK, 1).subtitle("subtitles.mubble.entity.fireball.hit"));
		soundExporter.add(MubbleSounds.FIREBALL_HIT_ENTITY, variantSoundBuilder(MubbleSounds.FIREBALL_HIT_ENTITY, 1).subtitle("subtitles.mubble.entity.fireball.hit"));
		soundExporter.add(MubbleSounds.FIREBALL_MELT_BLOCK, variantSoundBuilder(MubbleSounds.FIREBALL_MELT_BLOCK, 1).subtitle("subtitles.mubble.entity.fireball.melt_block"));
		soundExporter.add(MubbleSounds.FIREBALL_THROW, variantSoundBuilder(MubbleSounds.FIREBALL_THROW, 1));

		soundExporter.add(MubbleSounds.ICEBALL_HIT_BLOCK, variantSoundBuilder(MubbleSounds.ICEBALL_HIT_BLOCK, 1).subtitle("subtitles.mubble.entity.iceball.hit"));
		soundExporter.add(MubbleSounds.ICEBALL_HIT_ENTITY, variantSoundBuilder(MubbleSounds.ICEBALL_HIT_ENTITY, 1).subtitle("subtitles.mubble.entity.iceball.hit"));
		soundExporter.add(MubbleSounds.ICEBALL_THROW, variantSoundBuilder(MubbleSounds.ICEBALL_THROW, 1));

		// Power-Up
		soundExporter.add(MubbleSounds.POWER_UP_OBTAIN, variantSoundBuilder(MubbleSounds.POWER_UP_OBTAIN, 1));
		soundExporter.add(MubbleSounds.POWER_UP_OBTAIN_MINI, variantSoundBuilder(MubbleSounds.POWER_UP_OBTAIN_MINI, 1).subtitle("subtitles.mubble.power_up.obtain"));
		soundExporter.add(MubbleSounds.POWER_UP_OBTAIN_SUPER_STAR, variantSoundBuilder(MubbleSounds.POWER_UP_OBTAIN_SUPER_STAR, 1).subtitle("subtitles.mubble.power_up.obtain"));
		soundExporter.add(MubbleSounds.POWER_UP_LOOSE, variantSoundBuilder(MubbleSounds.POWER_UP_LOOSE, 1));
	}

	private SoundTypeBuilder variantSoundBuilder(Holder<SoundEvent> soundEvent, int count) {
		return variantSoundBuilder(soundEvent.value(), count);
	}

	private SoundTypeBuilder variantSoundBuilder(SoundEvent soundEvent, int count) {
		return variantSoundBuilder(SoundTypeBuilder.of(soundEvent), count, soundEvent.location().withPath(s -> s.replace(".", "/")));
	}

	private SoundTypeBuilder variantSoundBuilder(SoundEvent soundEvent, int count, Identifier baseId) {
		return variantSoundBuilder(SoundTypeBuilder.of(soundEvent), count, baseId.withPath(s -> s.replace(".", "/")));
	}

	private SoundTypeBuilder variantSoundBuilder(int count, Identifier baseId) {
		return variantSoundBuilder(SoundTypeBuilder.of(), count, baseId.withPath(s -> s.replace(".", "/")));
	}

	private SoundTypeBuilder variantSoundBuilder(SoundTypeBuilder builder, int count, Identifier baseId) {
		if (count > 1) {
			for (int i = 1; i <= count; i++) {
				Identifier soundId = baseId.withSuffix("/" + i);
				builder.sound(SoundTypeBuilder.RegistrationBuilder.ofFile(soundId));
			}
		} else {
			builder.sound(SoundTypeBuilder.RegistrationBuilder.ofFile(baseId));
		}
		return builder;
	}
}
