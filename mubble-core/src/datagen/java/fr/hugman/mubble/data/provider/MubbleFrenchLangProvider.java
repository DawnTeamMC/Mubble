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

        builder.add("commands.mubble.power_up.remove.no_power_up", "Rien n'a changé. Le joueur n'a aucun power-up à retirer");
        builder.add("commands.mubble.power_up.remove.success", "Power-up de %s retiré");
        builder.add("commands.mubble.power_up.remove.success_named", "Power-up %2$s de %1$s retiré");
        builder.add("commands.mubble.power_up.set.success", "Power-up de %s modifié");
        builder.add("commands.mubble.power_up.set.success_named", "Power-up de %s changé en %s");
        builder.add("commands.mubble.power_up.set.unchanged", "Rien n'a changé. Le joueur a déjà ce power-up");
        builder.add("entity.mubble.collectible", "Objet à collecter");
        builder.add("key.mubble.trigger_power_up", "Déclencher le power-up");
        builder.add("modmenu.descriptionTranslation.mubble", "Le crossover ultime avec toutes vos franchises préférées ! Principalement axé sur Nintendo.");
        builder.add("power_up_action_type.mubble.shoot_projectile.description", "Appuyez sur %s pour tirer %s");
    }
}
