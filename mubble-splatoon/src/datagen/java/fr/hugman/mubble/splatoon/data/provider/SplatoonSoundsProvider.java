package fr.hugman.mubble.splatoon.data.provider;

import fr.hugman.mubble.splatoon.sounds.SplatoonSounds;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

import java.util.concurrent.CompletableFuture;

public class SplatoonSoundsProvider extends FabricSoundsProvider {
    public SplatoonSoundsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public String getName() {
        return "Sounds";
    }

    @Override
    protected void configure(HolderLookup.Provider wrapperLookup, SoundExporter soundExporter) {
        // Blocks
        soundExporter.add(SplatoonSounds.INK_SPLASH, variantSoundBuilder(SplatoonSounds.INK_SPLASH, 21));

        // Weapons
        soundExporter.add(SplatoonSounds.SPLATTERSHOT_SHOOT, variantSoundBuilder(SplatoonSounds.SPLATTERSHOT_SHOOT, 2));
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
