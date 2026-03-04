package fr.hugman.mubble.super_mario.data.provider;

import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

import java.util.concurrent.CompletableFuture;

public class SuperMarioSoundsProvider extends FabricSoundsProvider {
    public SuperMarioSoundsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public String getName() {
        return "Sounds";
    }

    @Override
    protected void configure(HolderLookup.Provider wrapperLookup, SoundExporter soundExporter) {
        // Blocks
        soundExporter.add(SuperMarioSounds.BUMPABLE_BLOCK_BUMP, variantSoundBuilder(SuperMarioSounds.BUMPABLE_BLOCK_BUMP, 1));
        soundExporter.add(SuperMarioSounds.BUMPABLE_BLOCK_CHANGE_LOOT, variantSoundBuilder(SuperMarioSounds.BUMPABLE_BLOCK_CHANGE_LOOT, 1));
        soundExporter.add(SuperMarioSounds.BUMPABLE_BLOCK_DESTROY, variantSoundBuilder(SuperMarioSounds.BUMPABLE_BLOCK_DESTROY, 1));
        soundExporter.add(SuperMarioSounds.BUMPABLE_BLOCK_LOOT, variantSoundBuilder(SuperMarioSounds.BUMPABLE_BLOCK_LOOT, 1));
        soundExporter.add(SuperMarioSounds.BUMPABLE_BLOCK_LOOT_COIN, variantSoundBuilder(SuperMarioSounds.BUMPABLE_BLOCK_LOOT_COIN, 1));
        soundExporter.add(SuperMarioSounds.NOTE_BLOCK_JUMP_HIGH, variantSoundBuilder(SuperMarioSounds.NOTE_BLOCK_JUMP_HIGH, 1).subtitle("subtitles." + SuperMario.MOD_ID + ".block.note_block.jump"));
        soundExporter.add(SuperMarioSounds.NOTE_BLOCK_JUMP_LOW, variantSoundBuilder(SuperMarioSounds.NOTE_BLOCK_JUMP_LOW, 2).subtitle("subtitles." + SuperMario.MOD_ID + ".block.note_block.jump"));

        // Items
        soundExporter.add(SuperMarioSounds.COIN_COLLECT, variantSoundBuilder(SuperMarioSounds.COIN_COLLECT, 1));
        soundExporter.add(SuperMarioSounds.COIN_BOUNCE, variantSoundBuilder(SuperMarioSounds.COIN_BOUNCE, 1).subtitle(null));
        soundExporter.add(SuperMarioSounds.CAPE_FEATHER_USE, variantSoundBuilder(SuperMarioSounds.CAPE_FEATHER_USE, 1));

        // Entities
        soundExporter.add(SuperMarioSounds.GOLDEN_EXPLOSION, variantSoundBuilder(SuperMarioSounds.GOLDEN_EXPLOSION, 1));

        soundExporter.add(SuperMarioSounds.CLOUD_PLATFORM_APPEAR, variantSoundBuilder(SuperMarioSounds.CLOUD_PLATFORM_APPEAR, 1));
        soundExporter.add(SuperMarioSounds.CLOUD_PLATFORM_DISAPPEAR, variantSoundBuilder(SuperMarioSounds.CLOUD_PLATFORM_DISAPPEAR, 1));

        soundExporter.add(SuperMarioSounds.GOOMBA_WALK_STEP, variantSoundBuilder(SuperMarioSounds.GOOMBA_WALK_STEP, 1).subtitle("subtitles.block.generic.footsteps"));
        soundExporter.add(SuperMarioSounds.GOOMBA_RUN_STEP, variantSoundBuilder(SuperMarioSounds.GOOMBA_RUN_STEP, 1).subtitle("subtitles.block.generic.footsteps"));
        soundExporter.add(SuperMarioSounds.GOOMBA_FIND_TARGET, variantSoundBuilder(SuperMarioSounds.GOOMBA_FIND_TARGET, 1));
        soundExporter.add(SuperMarioSounds.GOOMBA_DEATH, variantSoundBuilder(SuperMarioSounds.GOOMBA_DEATH, 1));
        soundExporter.add(SuperMarioSounds.GOOMBA_STOMP, variantSoundBuilder(SuperMarioSounds.GOOMBA_STOMP, 1));

        soundExporter.add(SuperMarioSounds.KOOPA_SHELL_SLIDE, variantSoundBuilder(SuperMarioSounds.KOOPA_SHELL_SLIDE, 1));
        soundExporter.add(SuperMarioSounds.KOOPA_SHELL_HOMING, variantSoundBuilder(SuperMarioSounds.KOOPA_SHELL_HOMING, 1));
        soundExporter.add(SuperMarioSounds.KOOPA_SHELL_HIT_BLOCK, variantSoundBuilder(SuperMarioSounds.KOOPA_SHELL_HIT_BLOCK, 1));
        soundExporter.add(SuperMarioSounds.KOOPA_SHELL_BREAK, variantSoundBuilder(SuperMarioSounds.KOOPA_SHELL_BREAK, 1));
        soundExporter.add(SuperMarioSounds.KOOPA_SHELL_KICK, variantSoundBuilder(SuperMarioSounds.KOOPA_SHELL_KICK, 1));

        soundExporter.add(SuperMarioSounds.FIREBALL_HIT_BLOCK, variantSoundBuilder(SuperMarioSounds.FIREBALL_HIT_BLOCK, 1).subtitle("subtitles." + SuperMario.MOD_ID + ".entity.fireball.hit"));
        soundExporter.add(SuperMarioSounds.FIREBALL_HIT_ENTITY, variantSoundBuilder(SuperMarioSounds.FIREBALL_HIT_ENTITY, 1).subtitle("subtitles." + SuperMario.MOD_ID + ".entity.fireball.hit"));
        soundExporter.add(SuperMarioSounds.FIREBALL_MELT_BLOCK, variantSoundBuilder(SuperMarioSounds.FIREBALL_MELT_BLOCK, 1).subtitle("subtitles." + SuperMario.MOD_ID + ".entity.fireball.melt_block"));
        soundExporter.add(SuperMarioSounds.FIREBALL_THROW, variantSoundBuilder(SuperMarioSounds.FIREBALL_THROW, 1));

        soundExporter.add(SuperMarioSounds.ICEBALL_HIT_BLOCK, variantSoundBuilder(SuperMarioSounds.ICEBALL_HIT_BLOCK, 1).subtitle("subtitles." + SuperMario.MOD_ID + ".entity.iceball.hit"));
        soundExporter.add(SuperMarioSounds.ICEBALL_HIT_ENTITY, variantSoundBuilder(SuperMarioSounds.ICEBALL_HIT_ENTITY, 1).subtitle("subtitles." + SuperMario.MOD_ID + ".entity.iceball.hit"));
        soundExporter.add(SuperMarioSounds.ICEBALL_THROW, variantSoundBuilder(SuperMarioSounds.ICEBALL_THROW, 1));

        soundExporter.add(SuperMarioSounds.GOLD_FIREBALL_THROW, variantSoundBuilder(SuperMarioSounds.FIREBALL_THROW, 1).subtitle("subtitles." + SuperMario.MOD_ID + ".entity.gold_fireball.throw"));

        // Power-Up
        var obtainSub = "subtitles." + SuperMario.MOD_ID + ".power_up.obtain";

        soundExporter.add(SuperMarioSounds.POWER_UP_OBTAIN, variantSoundBuilder(SuperMarioSounds.POWER_UP_OBTAIN, 1));
        soundExporter.add(SuperMarioSounds.POWER_UP_OBTAIN_MINI, variantSoundBuilder(SuperMarioSounds.POWER_UP_OBTAIN_MINI, 1).subtitle(obtainSub));
        soundExporter.add(SuperMarioSounds.POWER_UP_OBTAIN_SUPER_STAR, variantSoundBuilder(SuperMarioSounds.POWER_UP_OBTAIN_SUPER_STAR, 1).subtitle(obtainSub));
        soundExporter.add(SuperMarioSounds.POWER_UP_OBTAIN_GOLD, variantSoundBuilder(SuperMarioSounds.POWER_UP_OBTAIN_GOLD, 1).subtitle(obtainSub));
        soundExporter.add(SuperMarioSounds.POWER_UP_EMIT_GOLD, variantSoundBuilder(SuperMarioSounds.POWER_UP_EMIT_GOLD, 1).subtitle(null));
        soundExporter.add(SuperMarioSounds.POWER_UP_SPIN_ATTACK, variantSoundBuilder(SuperMarioSounds.POWER_UP_SPIN_ATTACK, 1).subtitle(null));
        soundExporter.add(SuperMarioSounds.POWER_UP_LOOSE, variantSoundBuilder(SuperMarioSounds.POWER_UP_LOOSE, 1));
        soundExporter.add(SuperMarioSounds.POWER_UP_REFILL, variantSoundBuilder(SuperMarioSounds.POWER_UP_REFILL, 1));

        // Bubble
        soundExporter.add(SuperMarioSounds.BUBBLE_APPEAR, variantSoundBuilder(SuperMarioSounds.BUBBLE_APPEAR, 1));
        soundExporter.add(SuperMarioSounds.BUBBLE_POP, variantSoundBuilder(SuperMarioSounds.BUBBLE_POP, 1));
        soundExporter.add(SuperMarioSounds.BUBBLE_REBOUND, variantSoundBuilder(SuperMarioSounds.BUBBLE_REBOUND, 1));
        soundExporter.add(SuperMarioSounds.BUBBLE_FILL, variantSoundBuilder(SuperMarioSounds.BUBBLE_FILL, 1));
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
