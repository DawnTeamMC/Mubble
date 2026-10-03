package fr.hugman.mubble.data.provider;

import fr.hugman.mubble.data.arcade.ArcadeTranslations;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

/**
 * The French texts of the arcade movement layer. The rest of the French translation comes from
 * Crowdin; this one ships with the feature.
 */
public class MubbleFrenchLangProvider extends FabricLanguageProvider {
    public MubbleFrenchLangProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, "fr_fr", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider wrapperLookup, TranslationBuilder builder) {
        ArcadeTranslations.addAll(builder, ArcadeTranslations.FRENCH);
    }
}
