package fr.hugman.mubble.zelda.data.provider;

import fr.hugman.mubble.data.AutomaticEnglish;
import fr.hugman.mubble.zelda.Zelda;
import fr.hugman.mubble.zelda.references.ZeldaCreativeModeTabIds;
import fr.hugman.mubble.zelda.sounds.ZeldaSounds;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.Util;

import java.util.concurrent.CompletableFuture;

public class ZeldaEnglishLangProvider extends FabricLanguageProvider {
    public ZeldaEnglishLangProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider wrapperLookup, TranslationBuilder builder) {
        builder.add(Util.makeDescriptionId("item_group", ZeldaCreativeModeTabIds.ZELDA.identifier()), "The Legend of Zelda");

        AutomaticEnglish.generateAutomaticTranslations(Zelda.MOD_ID, wrapperLookup, builder);

        // Loftwing breeds, and the spawn eggs that summon them on purpose.
        variant(builder, "green", "Green Loftwing");
        variant(builder, "yellow", "Yellow Loftwing");
        variant(builder, "violet", "Violet Loftwing");
        variant(builder, "crimson", "Crimson Loftwing");

        builder.add(ZeldaSounds.LOFTWING_AMBIENT, "Loftwing cries");
        builder.add(ZeldaSounds.LOFTWING_HURT, "Loftwing hurts");
        builder.add(ZeldaSounds.LOFTWING_DEATH, "Loftwing dies");
        builder.add(ZeldaSounds.LOFTWING_FLAP, "Loftwing flaps");
    }

    private static void variant(TranslationBuilder builder, String name, String english) {
        builder.add(ZeldaLoftwingVariantProvider.variantKey(name), english);
        builder.add(ZeldaLoftwingVariantProvider.spawnEggKey(name), english + " Spawn Egg");
    }
}
