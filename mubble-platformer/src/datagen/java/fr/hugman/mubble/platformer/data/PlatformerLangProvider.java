package fr.hugman.mubble.platformer.data;

import fr.hugman.mubble.platformer.MubblePlatformer;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

/**
 * The texts of the platformer movement layer, in one language. They are written to a file of their own,
 * in the namespace of the module, so that they never overwrite the texts of the core.
 */
public class PlatformerLangProvider extends FabricLanguageProvider {
    private final int language;

    public PlatformerLangProvider(FabricPackOutput output, int language, String languageCode, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, languageCode, registryLookup);
        this.language = language;
    }

    @Override
    public void generateTranslations(HolderLookup.Provider wrapperLookup, TranslationBuilder builder) {
        PlatformerTranslations.addAll(builder, this.language);
    }

    @Override
    protected Path getLangFilePath(String code) {
        return this.packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "lang").json(Identifier.fromNamespaceAndPath(MubblePlatformer.MOD_ID, code));
    }
}
