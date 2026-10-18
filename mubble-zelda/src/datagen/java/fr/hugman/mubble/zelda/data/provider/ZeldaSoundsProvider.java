package fr.hugman.mubble.zelda.data.provider;

import fr.hugman.mubble.zelda.sounds.ZeldaSounds;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;

import java.util.concurrent.CompletableFuture;

public class ZeldaSoundsProvider extends FabricSoundsProvider {
    public ZeldaSoundsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public String getName() {
        return "Sounds";
    }

    @Override
    protected void configure(HolderLookup.Provider wrapperLookup, SoundExporter soundExporter) {
        soundExporter.add(ZeldaSounds.LOFTWING_AMBIENT, soundBuilder(ZeldaSounds.LOFTWING_AMBIENT));
        soundExporter.add(ZeldaSounds.LOFTWING_HURT, soundBuilder(ZeldaSounds.LOFTWING_HURT));
        soundExporter.add(ZeldaSounds.LOFTWING_DEATH, soundBuilder(ZeldaSounds.LOFTWING_DEATH));
        soundExporter.add(ZeldaSounds.LOFTWING_FLAP, soundBuilder(ZeldaSounds.LOFTWING_FLAP));
        soundExporter.add(ZeldaSounds.LOFTWING_PREEN, soundBuilder(ZeldaSounds.LOFTWING_PREEN));
        soundExporter.add(ZeldaSounds.LOFTWING_STEP, soundBuilder(ZeldaSounds.LOFTWING_STEP).subtitle("subtitles.block.generic.footsteps"));
    }

    /** One file per sound, named after the id with the dots turned into folders. */
    private SoundTypeBuilder soundBuilder(SoundEvent soundEvent) {
        var fileId = soundEvent.location().withPath(path -> path.replace(".", "/"));
        return SoundTypeBuilder.of(soundEvent).sound(SoundTypeBuilder.RegistrationBuilder.ofFile(fileId));
    }
}
