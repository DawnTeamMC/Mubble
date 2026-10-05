package fr.hugman.mubble.data.provider;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.data.AutomaticEnglish;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class MubbleEnglishLangProvider extends FabricLanguageProvider {
    public MubbleEnglishLangProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider wrapperLookup, TranslationBuilder builder) {
        AutomaticEnglish.generateAutomaticTranslations(Mubble.MOD_ID, wrapperLookup, builder);

        builder.add("key." + Mubble.MOD_ID + ".trigger_power_up", "Trigger Power-Up");

        builder.add("commands." + Mubble.MOD_ID + ".power_up.set.success", "Changed %s's power-up");
        builder.add("commands." + Mubble.MOD_ID + ".power_up.set.success_named", "Changed %s's power-up to %s");
        builder.add("commands." + Mubble.MOD_ID + ".power_up.set.unchanged", "Nothing changed. The player already has this power-up");
        builder.add("commands." + Mubble.MOD_ID + ".power_up.remove.success", "Successfully removed %s's power-up");
        builder.add("commands." + Mubble.MOD_ID + ".power_up.remove.success_named", "Successfully removed %s's %s power-up");
        builder.add("commands." + Mubble.MOD_ID + ".power_up.remove.no_power_up", "Nothing changed. The player doesn't have any power-up to remove");

        builder.add("power_up_action_type." + Mubble.MOD_ID + ".shoot_projectile.description", "Press %s to shoot %s");

        builder.add("modmenu.descriptionTranslation.mubble", "Ultimate crossover mod with all your favorite franchises! Mainly focused on Nintendo.");
    }
}