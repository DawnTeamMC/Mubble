package fr.hugman.mubble.zelda.data.provider;

import fr.hugman.mubble.zelda.world.item.ZeldaItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class ZeldaRecipeProvider extends FabricRecipeProvider {
	public ZeldaRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	public String getName() {
		return "Recipes";
	}

	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
		return new RecipeProvider(registries, output) {
			@Override
			public void buildRecipes() {
				var items = registries.lookupOrThrow(Registries.ITEM);

				// A feather and a copper ingot: the bird teaches you its call, this is just what you blow it on.
				ShapelessRecipeBuilder.shapeless(items, RecipeCategory.TRANSPORTATION, ZeldaItems.LOFTWING_WHISTLE)
						.requires(Items.FEATHER)
						.requires(Items.COPPER_INGOT)
						.unlockedBy("has_feather", this.has(Items.FEATHER))
						.save(output);

				// Four leads knotted into a ring, the way the issue draws it.
				ShapedRecipeBuilder.shaped(items, RecipeCategory.TRANSPORTATION, ZeldaItems.LOFTWING_BELT)
						.pattern(" l ")
						.pattern("l l")
						.pattern(" l ")
						.define('l', Items.LEAD)
						.unlockedBy("has_lead", this.has(Items.LEAD))
						.save(output);

				// Seven of a material around a redstone lamp. No chainmail: that one is a gift only.
				helmet(items, ZeldaItems.LEATHER_LOFTWING_HELMET, Items.LEATHER);
				helmet(items, ZeldaItems.GOLDEN_LOFTWING_HELMET, Items.GOLD_INGOT);
				helmet(items, ZeldaItems.IRON_LOFTWING_HELMET, Items.IRON_INGOT);
				helmet(items, ZeldaItems.DIAMOND_LOFTWING_HELMET, Items.DIAMOND);

				this.netheriteSmithing(ZeldaItems.DIAMOND_LOFTWING_HELMET, RecipeCategory.TRANSPORTATION, (Item) ZeldaItems.NETHERITE_LOFTWING_HELMET);
			}

			private void helmet(HolderLookup.RegistryLookup<Item> items, ItemLike result, ItemLike material) {
				ShapedRecipeBuilder.shaped(items, RecipeCategory.TRANSPORTATION, result)
						.pattern("mmm")
						.pattern("mlm")
						.pattern("m m")
						.define('m', material)
						.define('l', Items.REDSTONE_LAMP)
						.unlockedBy("has_redstone_lamp", this.has(Items.REDSTONE_LAMP))
						.save(output);
			}
		};
	}
}
