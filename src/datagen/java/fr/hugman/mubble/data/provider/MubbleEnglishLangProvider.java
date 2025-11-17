package fr.hugman.mubble.data.provider;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.item_group.MubbleItemGroupKeys;
import fr.hugman.mubble.registry.MubbleRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MubbleEnglishLangProvider extends FabricLanguageProvider {
	private static final Set<String> DO_NOT_TITLE_CASE = Set.of(
			"of", "the", "and", "a", "an", "in", "on", "for", "to", "at", "by", "from", "with"
	);

	public MubbleEnglishLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, "en_us", registryLookup);
	}

	@Override
	public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder builder) {
		builder.add(Util.createTranslationKey("item_group", MubbleItemGroupKeys.YOSHI_ISLAND.getValue()), "Yoshi's Island");

		this.generateAutomaticTranslations(wrapperLookup, builder);

		builder.add("block.mubble.bumpable.drop.all", "Drop all");
		builder.add("block.mubble.bumpable.drop.all.description", "The block will drop the entire item stack when bumped");
		builder.add("block.mubble.bumpable.drop.one", "Drop one");
		builder.add("block.mubble.bumpable.drop.one.description", "The block will drop one item per bump");

		builder.add("key.mubble.trigger_power_up", "Trigger Power-Up");

		builder.add("subtitles.mubble.block.bumpable_block.bump", "Block bumped");
		builder.add("subtitles.mubble.block.bumpable_block.change_loot", "Block filled");
		builder.add("subtitles.mubble.block.bumpable_block.destroy", "Block destroyed");
		builder.add("subtitles.mubble.block.bumpable_block.loot", "Block dropped item");
		builder.add("subtitles.mubble.block.bumpable_block.loot.coin", "Block dropped coin");
		builder.add("subtitles.mubble.block.note_block.jump", "Note Block used");
		builder.add("subtitles.mubble.item.cape_feather.use", "Cape Feather used");
		builder.add("subtitles.mubble.entity.goomba.find_target", "Goomba finds a target");
		builder.add("subtitles.mubble.entity.goomba.death", "Goomba dies");
		builder.add("subtitles.mubble.entity.goomba.stomp", "Goomba stomped");
		builder.add("subtitles.mubble.entity.fireball.hit", "Fireball hits");
		builder.add("subtitles.mubble.entity.fireball.melt_block", "Fireball melts block");
		builder.add("subtitles.mubble.entity.fireball.throw", "Fireball thrown");
		builder.add("subtitles.mubble.entity.iceball.hit", "Iceball hits");
		builder.add("subtitles.mubble.entity.iceball.throw", "Iceball thrown");
		builder.add("subtitles.mubble.power_up.obtain", "Power-up obtained");
		builder.add("subtitles.mubble.power_up.loose", "Power-up lost");

		builder.add("gamerule.beepBlockCooldown", "Beep Block cooldown");

        builder.add("commands.mubble.power_up.set.success", "Changed %s's power-up");
        builder.add("commands.mubble.power_up.set.success_named", "Changed %s's power-up to %s");
        builder.add("commands.mubble.power_up.set.unchanged", "Nothing changed. The player already has this power-up");
        builder.add("commands.mubble.power_up.remove.success", "Successfully removed %s's power-up");
        builder.add("commands.mubble.power_up.remove.success_named", "Successfully removed %s's %s power-up");
        builder.add("commands.mubble.power_up.remove.no_power_up", "Nothing changed. The player doesn't have any power-up to remove");

        builder.add("power_up_action_type.mubble.shoot_projectile.description", "Press %s to shoot %s");

		builder.add("modmenu.descriptionTranslation.mubble", "Ultimate crossover mod with all your favorite franchises! Mainly focused on Nintendo.");
	}

	private void generateAutomaticTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder builder) {
		for (var block : getRegistryEntries(wrapperLookup, RegistryKeys.BLOCK)) {
			var path = block.registryKey().getValue().getPath()
					.replaceAll("exclamation", "!")
					.replaceAll("question", "?");
			try {
				builder.add(block.value(), snakeToTitleCase(path));
			} catch (RuntimeException ignored) {}
		}

		for (var item : getRegistryEntries(wrapperLookup, RegistryKeys.ITEM)) {
			if (item.value().getTranslationKey().startsWith("block.")) {
				continue;
			}
			var path = item.registryKey().getValue().getPath();
			if (path.endsWith("_chest_boat")) {
				path = path.replace("_chest_boat", "_boat_with_chest");
			}
			try {
				builder.add(item.value(), snakeToTitleCase(path));
			} catch (RuntimeException ignored) {}
		}

		for (var entity : getRegistryEntries(wrapperLookup, RegistryKeys.ENTITY_TYPE)) {
			var path = entity.registryKey().getValue().getPath();
			if (path.endsWith("_chest_boat")) {
				path = path.replace("_chest_boat", "_boat_with_chest");
			}
			builder.add(entity.value(), snakeToTitleCase(path));
		}

		for (var biome : getRegistryEntries(wrapperLookup, RegistryKeys.BIOME)) {
			var id = biome.registryKey().getValue();
			builder.add(Util.createTranslationKey("biome", id), snakeToTitleCase(id.getPath()));
		}

		for (var bannerPattern : getRegistryEntries(wrapperLookup, RegistryKeys.BANNER_PATTERN)) {
			var id = bannerPattern.registryKey().getValue();
			builder.add(
					Util.createTranslationKey("item", id.withPath(s -> s + "_banner_pattern.desc")),
					snakeToTitleCase(id.getPath())
			);
			for (DyeColor color : DyeColor.values()) {
				builder.add(
						Util.createTranslationKey("block", id.withPath(s -> "banner." + s + "." + color.getId())),
						snakeToTitleCase(color.getId() + "_" + id.getPath())
				);
			}
		}

		for (var paintingVariant : getRegistryEntries(wrapperLookup, RegistryKeys.PAINTING_VARIANT)) {
			var id = paintingVariant.registryKey().getValue();
			builder.add(Util.createTranslationKey("painting", id) + ".title", snakeToTitleCase(id.getPath()));
		}

		for (var itemGroup : getRegistryEntries(wrapperLookup, RegistryKeys.ITEM_GROUP)) {
			var id = itemGroup.registryKey().getValue();
			try {
				builder.add(Util.createTranslationKey("item_group", id), snakeToTitleCase(id.getPath()));
			}
            catch (RuntimeException ignored) {}
		}

		for (var powerUp : getRegistryEntries(wrapperLookup, MubbleRegistryKeys.POWER_UP)) {
			var id = powerUp.registryKey().getValue();
			try {
				builder.add(Util.createTranslationKey("power_up", id), snakeToTitleCase(id.getPath()));
			}
            catch (RuntimeException ignored) {}
		}
	}

	private static <O> List<RegistryEntry.Reference<O>> getRegistryEntries(RegistryWrapper.WrapperLookup wrapperLookup, RegistryKey<? extends Registry<O>> registryKey) {
		return wrapperLookup.getOrThrow(registryKey).streamEntries()
				.filter(entry -> entry.registryKey().getValue().getNamespace().equals(Mubble.MOD_ID))
				.toList();
	}


	private static String snakeToTitleCase(String str) {
		String[] words = str.split("_");
		StringBuilder titleCase = new StringBuilder();
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			if (word.isEmpty()) {
				continue;
			}
			if (i != 0 && DO_NOT_TITLE_CASE.contains(word.toLowerCase())) {
				titleCase.append(word.toLowerCase());
			} else {
				titleCase.append(Character.toUpperCase(word.charAt(0)))
						.append(word.substring(1).toLowerCase());
			}
			if (i < words.length - 1) {
				titleCase.append(" ");
			}
		}
		return titleCase.toString();
	}
}