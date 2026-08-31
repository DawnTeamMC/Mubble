package fr.hugman.mubble.super_mario.data.provider;

import fr.hugman.mubble.data.AutomaticEnglish;
import fr.hugman.mubble.super_mario.references.SuperMarioCreativeModeTabIds;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.SuperMario;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.Util;

import java.util.concurrent.CompletableFuture;

public class SuperMarioEnglishLangProvider extends FabricLanguageProvider {

    public SuperMarioEnglishLangProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider wrapperLookup, TranslationBuilder builder) {
        builder.add(Util.makeDescriptionId("item_group", SuperMarioCreativeModeTabIds.YOSHI_ISLAND.identifier()), "Yoshi's Island");

        AutomaticEnglish.generateAutomaticTranslations(SuperMario.MOD_ID, wrapperLookup, builder);

        builder.add("block." + SuperMario.MOD_ID + ".bumpable.drop.all", "Drop all");
        builder.add("block." + SuperMario.MOD_ID + ".bumpable.drop.all.description", "The block will drop the entire item stack when bumped");
        builder.add("block." + SuperMario.MOD_ID + ".bumpable.drop.one", "Drop one");
        builder.add("block." + SuperMario.MOD_ID + ".bumpable.drop.one.description", "The block will drop one item per bump");

        builder.add("power_up." + SuperMario.MOD_ID + ".mini.description.size", "Shrinks you to a third of your size.");
        builder.add("power_up." + SuperMario.MOD_ID + ".mini.description.trade_off", "Weaker, but a better jumper.");
        builder.add("power_up." + SuperMario.MOD_ID + ".mini.description.water", "Sprint off land to run on water.");
        builder.add("power_up." + SuperMario.MOD_ID + ".mega.description.size", "Grows you to three times your size.");
        builder.add("power_up." + SuperMario.MOD_ID + ".mega.description.trade_off", "Faster, tougher and stronger, but slow to swing.");
        builder.add("power_up." + SuperMario.MOD_ID + ".cloud.description.float", "You jump higher and fall slower.");
        builder.add("power_up." + SuperMario.MOD_ID + ".cloud.description.weather", "Water and rain wash it away.");
        builder.add("power_up_action_type." + SuperMario.MOD_ID + ".spawn_cloud_platform.description", "Press %s to summon a cloud platform.");

        builder.add("entity." + SuperMario.MOD_ID + ".goomba.mini", "Mini Goomba");
        builder.add("item." + SuperMario.MOD_ID + ".mini_goomba_spawn_egg", "Mini Goomba Spawn Egg");

        builder.add("subtitles." + SuperMario.MOD_ID + ".block.bumpable_block.bump", "Block bumped");
        builder.add("subtitles." + SuperMario.MOD_ID + ".block.bumpable_block.change_loot", "Block filled");
        builder.add("subtitles." + SuperMario.MOD_ID + ".block.bumpable_block.destroy", "Block destroyed");
        builder.add("subtitles." + SuperMario.MOD_ID + ".block.bumpable_block.loot", "Block dropped item");
        builder.add("subtitles." + SuperMario.MOD_ID + ".block.bumpable_block.loot.coin", "Block dropped coin");
        builder.add("subtitles." + SuperMario.MOD_ID + ".block.note_block.jump", "Note Block used");
        builder.add(SuperMarioSounds.COIN_COLLECT.value(), "Coin collected");
        builder.add("subtitles." + SuperMario.MOD_ID + ".item.cape_feather.use", "Cape Feather used");
        builder.add(SuperMarioSounds.GOLDEN_EXPLOSION.value(), "Golden explosion");
        builder.add(SuperMarioSounds.CLOUD_PLATFORM_APPEAR.value(), "Cloud Platform appears");
        builder.add(SuperMarioSounds.CLOUD_PLATFORM_DISAPPEAR.value(), "Cloud Platform disappears");
        builder.add("subtitles." + SuperMario.MOD_ID + ".entity.goomba.find_target", "Goomba finds a target");
        builder.add("subtitles." + SuperMario.MOD_ID + ".entity.goomba.death", "Goomba dies");
        builder.add("subtitles." + SuperMario.MOD_ID + ".entity.goomba.stomp", "Goomba stomped");
        builder.add(SuperMarioSounds.KOOPA_SHELL_SLIDE, "Koopa Shell slides");
        builder.add(SuperMarioSounds.KOOPA_SHELL_HOMING, "Koopa Shell homing");
        builder.add(SuperMarioSounds.KOOPA_SHELL_HIT_BLOCK, "Koopa Shell hits block");
        builder.add(SuperMarioSounds.KOOPA_SHELL_BREAK, "Koopa Shell breaks");
        builder.add(SuperMarioSounds.KOOPA_SHELL_KICK, "Koopa Shell kicked");
        builder.add("subtitles." + SuperMario.MOD_ID + ".entity.fireball.hit", "Fireball hits");
        builder.add("subtitles." + SuperMario.MOD_ID + ".entity.fireball.melt_block", "Fireball melts block");
        builder.add("subtitles." + SuperMario.MOD_ID + ".entity.fireball.throw", "Fireball thrown");
        builder.add("subtitles." + SuperMario.MOD_ID + ".entity.iceball.hit", "Iceball hits");
        builder.add("subtitles." + SuperMario.MOD_ID + ".entity.iceball.throw", "Iceball thrown");
        builder.add(SuperMarioSounds.GOLD_FIREBALL_THROW.value(), "Gold Fireball thrown");
        builder.add(SuperMarioSounds.BUBBLE_APPEAR.value(), "Bubble appears");
        builder.add(SuperMarioSounds.BUBBLE_POP.value(), "Bubble pops");
        builder.add(SuperMarioSounds.BUBBLE_FILL.value(), "Bubble fills");
        builder.add("subtitles." + SuperMario.MOD_ID + ".power_up.obtain", "Power-up obtained");
        builder.add("subtitles." + SuperMario.MOD_ID + ".power_up.loose", "Power-up lost");
        builder.add("subtitles." + SuperMario.MOD_ID + ".power_up.refill", "Power-up refilled");

        builder.add("gamerule.beepBlockCooldown", "Beep Block cooldown");

        builder.add("death.attack." + SuperMario.MOD_ID + ".stomp", "%1$s was stomped by %2$s");
        builder.add("death.attack." + SuperMario.MOD_ID + ".koopa_shell", "%1$s was shelled by %2$s");
        builder.add("death.attack." + SuperMario.MOD_ID + ".koopa_shell.player", "%1$s was shelled while fighting %2$s");
        builder.add("death.attack." + SuperMario.MOD_ID + ".fireball", "%1$s was fireballed by %2$s");
        builder.add("death.attack." + SuperMario.MOD_ID + ".fireball.player", "%1$s was fireballed while fighting %2$s");
        builder.add("death.attack." + SuperMario.MOD_ID + ".iceball", "%1$s was iceballed by %2$s");
        builder.add("death.attack." + SuperMario.MOD_ID + ".iceball.player", "%1$s was iceballed while fighting %2$s");
        builder.add("death.attack." + SuperMario.MOD_ID + ".gold_fireball", "%1$s was gold-blasted by %2$s");
        builder.add("death.attack." + SuperMario.MOD_ID + ".gold_fireball.player", "%1$s was gold-blasted while fighting %2$s");
    }
}