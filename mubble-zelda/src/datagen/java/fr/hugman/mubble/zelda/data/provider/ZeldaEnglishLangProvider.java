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
        builder.add(ZeldaSounds.LOFTWING_PREEN, "Loftwing preens");
        builder.add(ZeldaSounds.LOFTWING_CALL, "Loftwing calls for its owner");
        builder.add(ZeldaSounds.LOFTWING_BOND, "Loftwing picks its owner");
        builder.add(ZeldaSounds.LOFTWING_CRASH, "Loftwing crashes");
        builder.add(ZeldaSounds.LOFTWING_WHISTLE, "Whistle blows");
        builder.add(ZeldaSounds.LOFTWING_EGG_CRACK, "Loftwing egg cracks");
        builder.add(ZeldaSounds.LOFTWING_EGG_HATCH, "Loftwing egg hatches");

        builder.add("item." + Zelda.MOD_ID + ".loftwing_whistle.no_answer", "No loftwing answers");

        builder.add("death.attack." + Zelda.MOD_ID + ".loftwing_crash", "%1$s flew into a wall");
        builder.add("death.attack." + Zelda.MOD_ID + ".loftwing_crash.entity", "%1$s was flown into a wall by %2$s");
    }

    private static void variant(TranslationBuilder builder, String name, String english) {
        builder.add(ZeldaLoftwingVariantProvider.variantKey(name), english);
        builder.add(ZeldaLoftwingVariantProvider.spawnEggKey(name), english + " Spawn Egg");
    }
}
