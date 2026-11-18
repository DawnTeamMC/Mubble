package fr.hugman.mubble.fortnite.data.provider;

import fr.hugman.mubble.data.AutomaticEnglish;
import fr.hugman.mubble.fortnite.Fortnite;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class FortniteEnglishLangProvider extends FabricLanguageProvider {
    public FortniteEnglishLangProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider wrapperLookup, TranslationBuilder builder) {
        AutomaticEnglish.generateAutomaticTranslations(Fortnite.MOD_ID, wrapperLookup, builder);
    }
}
