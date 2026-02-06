package fr.hugman.mubble.splatoon.data.provider;

import fr.hugman.mubble.data.AutomaticEnglish;
import fr.hugman.mubble.splatoon.Splatoon;
import fr.hugman.mubble.splatoon.sounds.SplatoonSounds;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class SplatoonEnglishLangProvider extends FabricLanguageProvider {

    public SplatoonEnglishLangProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider wrapperLookup, TranslationBuilder builder) {
        AutomaticEnglish.generateAutomaticTranslations(Splatoon.MOD_ID, wrapperLookup, builder);

        builder.add(SplatoonSounds.INK_SPLASH, "Ink splashes");
        builder.add(SplatoonSounds.SPLATTERSHOT_SHOOT, "Splattershot shoots"); //TODO: make it generic for all automatic shooters...
    }
}