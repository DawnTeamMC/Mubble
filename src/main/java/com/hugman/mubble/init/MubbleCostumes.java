package com.hugman.mubble.init;

import com.hugman.dawn.api.creator.ItemCreator;
import com.hugman.mubble.object.block.block_state_property.Princess;
import com.hugman.mubble.object.item.costume.HatItem;
import com.hugman.mubble.object.item.costume.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.SoundEvents;

public class MubbleCostumes extends MubblePack {
	/* MUBBLE */
	public static final Item HEADPHONES = register(new ItemCreator.Builder("headphones", new HatItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, false)));
	public static final Item WHITE_VIRTUAL_GOOGLES = register(new ItemCreator.Builder("white_virtual_googles", new MaskItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.WHITE_RETRO)));
	public static final Item LIGHT_GRAY_VIRTUAL_GOOGLES = register(new ItemCreator.Builder("light_gray_virtual_googles", new MaskItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.LIGHT_GRAY_RETRO)));
	public static final Item GRAY_VIRTUAL_GOOGLES = register(new ItemCreator.Builder("gray_virtual_googles", new MaskItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.GRAY_RETRO)));
	public static final Item BLACK_VIRTUAL_GOOGLES = register(new ItemCreator.Builder("black_virtual_googles", new MaskItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.BLACK_RETRO)));
	public static final Item BROWN_VIRTUAL_GOOGLES = register(new ItemCreator.Builder("brown_virtual_googles", new MaskItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.BROWN_RETRO)));
	public static final Item RED_VIRTUAL_GOOGLES = register(new ItemCreator.Builder("red_virtual_googles", new MaskItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.RED_RETRO)));
	public static final Item ORANGE_VIRTUAL_GOOGLES = register(new ItemCreator.Builder("orange_virtual_googles", new MaskItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.ORANGE_RETRO)));
	public static final Item YELLOW_VIRTUAL_GOOGLES = register(new ItemCreator.Builder("yellow_virtual_googles", new MaskItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.YELLOW_RETRO)));
	public static final Item LIME_VIRTUAL_GOOGLES = register(new ItemCreator.Builder("lime_virtual_googles", new MaskItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.LIME_RETRO)));
	public static final Item GREEN_VIRTUAL_GOOGLES = register(new ItemCreator.Builder("green_virtual_googles", new MaskItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.GREEN_RETRO)));
	public static final Item CYAN_VIRTUAL_GOOGLES = register(new ItemCreator.Builder("cyan_virtual_googles", new MaskItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.CYAN_RETRO)));
	public static final Item LIGHT_BLUE_VIRTUAL_GOOGLES = register(new ItemCreator.Builder("light_blue_virtual_googles", new MaskItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.LIGHT_BLUE_RETRO)));
	public static final Item BLUE_VIRTUAL_GOOGLES = register(new ItemCreator.Builder("blue_virtual_googles", new MaskItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.BLUE_RETRO)));
	public static final Item PURPLE_VIRTUAL_GOOGLES = register(new ItemCreator.Builder("purple_virtual_googles", new MaskItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.PURPLE_RETRO)));
	public static final Item MAGENTA_VIRTUAL_GOOGLES = register(new ItemCreator.Builder("magenta_virtual_googles", new MaskItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.MAGENTA_RETRO)));
	public static final Item PINK_VIRTUAL_GOOGLES = register(new ItemCreator.Builder("pink_virtual_googles", new MaskItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.PINK_RETRO)));
	public static final Item CHRISTMAS_HAT = register(new ItemCreator.Builder("christmas_hat", new ChristmasHatItem(Settings.COSTUME)));

	/* SUPER MARIO */
	public static final Item CAPPY = register(new ItemCreator.Builder("cappy", new CappyItem(Settings.COSTUME)));
	public static final Item LUIGI_CAP = register(new ItemCreator.Builder("luigi_cap", new HatItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, false, new StatusEffectInstance(StatusEffects.JUMP_BOOST, 5, 0), new StatusEffectInstance(StatusEffects.SPEED, 5, 0))));
	public static final Item WARIO_CAP = register(new ItemCreator.Builder("wario_cap", new HatItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, false, new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 0), new StatusEffectInstance(StatusEffects.STRENGTH, 5, 0))));
	public static final Item WALUIGI_CAP = register(new ItemCreator.Builder("waluigi_cap", new HatItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, false, new StatusEffectInstance(StatusEffects.JUMP_BOOST, 5, 1))));
	public static final Item VANISH_CAP = register(new ItemCreator.Builder("vanish_cap", new VanishCapItem(Settings.COSTUME)));
	public static final Item WING_CAP = register(new ItemCreator.Builder("wing_cap", new WingCapItem(Settings.COSTUME.maxDamageIfAbsent(600))));
	public static final Item GOOIGI_CAP = register(new ItemCreator.Builder("gooigi_cap", new GooigiCapItem(Settings.COSTUME)));
	public static final Item GOLD_MARIO_CAP = register(new ItemCreator.Builder("gold_mario_cap", new HatItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, false)));
	public static final Item SILVER_LUIGI_CAP = register(new ItemCreator.Builder("silver_luigi_cap", new HatItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, false)));
	public static final Item PRINCESS_PEACH_CROWN = register(new ItemCreator.Builder("princess_peach_crown", new CrownItem(Settings.COSTUME, Princess.PEACH)));
	public static final Item PRINCESS_DAISY_CROWN = register(new ItemCreator.Builder("princess_daisy_crown", new CrownItem(Settings.COSTUME, Princess.DAISY)));
	public static final Item ROSALINA_CROWN = register(new ItemCreator.Builder("rosalina_crown", new HatItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, false)));
	public static final Item PINK_GOLD_PEACH_CROWN = register(new ItemCreator.Builder("pink_gold_peach_crown", new HatItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, false)));
	public static final Item SUPER_CROWN = register(new ItemCreator.Builder("super_crown", new HatItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, false)));
	public static final Item PROPELLER_BOX = register(new ItemCreator.Builder("propeller_box", new PropellerBoxItem(Settings.COSTUME)));
	public static final Item MARIO_WEDDING_HAT = register(new ItemCreator.Builder("mario_wedding_hat", new HatItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, false)));
	public static final Item BOO_HAT = register(new ItemCreator.Builder("boo_hat", new HatItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, false)));
	public static final Item BROQUE_MONSIEUR_HEAD = register(new ItemCreator.Builder("broque_monsieur_head", new HatItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, true)));

	/* KIRBY */
	public static final Item BANDANA = register(new ItemCreator.Builder("bandana", new HatItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, false)));

	/* POKEMON */
	public static final Item PIKACHU_EARS = register(new ItemCreator.Builder("pikachu_ears", new HatItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, false)));
	public static final Item SNORLAX_HAT = register(new ItemCreator.Builder("snorlax_hat", new HatItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, true)));

	/* PROFESSOR LAYTON */
	public static final Item TOP_HAT = register(new ItemCreator.Builder("top_hat", new HatItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, false)));

	/* UNDERTALE / DELTARUNE */
	public static final Item RALSEI_HAT = register(new ItemCreator.Builder("ralsei_hat", new HatItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, false)));
	public static final Item KING_ROUND_MASK = register(new ItemCreator.Builder("king_round_mask", new MaskItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)));

	/* A HAT IN TIME */
	public static final Item KID_HAT = register(new ItemCreator.Builder("kid_hat", new HatItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, false)));

	/* PUYO PUYO */
	public static final Item RED_PUYO = register(new ItemCreator.Builder("red_puyo", new WearableBlockItem(MubbleBlocks.RED_PUYO, Settings.DECORATION_BLOCK, SoundEvents.BLOCK_SLIME_BLOCK_PLACE)));
	public static final Item YELLOW_PUYO = register(new ItemCreator.Builder("yellow_puyo", new WearableBlockItem(MubbleBlocks.YELLOW_PUYO, Settings.DECORATION_BLOCK, SoundEvents.BLOCK_SLIME_BLOCK_PLACE)));
	public static final Item GREEN_PUYO = register(new ItemCreator.Builder("green_puyo", new WearableBlockItem(MubbleBlocks.GREEN_PUYO, Settings.DECORATION_BLOCK, SoundEvents.BLOCK_SLIME_BLOCK_PLACE)));
	public static final Item TURQUOISE_PUYO = register(new ItemCreator.Builder("turquoise_puyo", new WearableBlockItem(MubbleBlocks.TURQUOISE_PUYO, Settings.DECORATION_BLOCK, SoundEvents.BLOCK_SLIME_BLOCK_PLACE)));
	public static final Item BLUE_PUYO = register(new ItemCreator.Builder("blue_puyo", new WearableBlockItem(MubbleBlocks.BLUE_PUYO, Settings.DECORATION_BLOCK, SoundEvents.BLOCK_SLIME_BLOCK_PLACE)));
	public static final Item PURPLE_PUYO = register(new ItemCreator.Builder("purple_puyo", new WearableBlockItem(MubbleBlocks.PURPLE_PUYO, Settings.DECORATION_BLOCK, SoundEvents.BLOCK_SLIME_BLOCK_PLACE)));
	public static final Item GRAY_PUYO = register(new ItemCreator.Builder("gray_puyo", new WearableBlockItem(MubbleBlocks.GRAY_PUYO, Settings.DECORATION_BLOCK, SoundEvents.BLOCK_SLIME_BLOCK_PLACE)));
	public static final Item GARBAGE_PUYO = register(new ItemCreator.Builder("garbage_puyo", new WearableBlockItem(MubbleBlocks.GARBAGE_PUYO, Settings.DECORATION_BLOCK, SoundEvents.BLOCK_STONE_PLACE)));
	public static final Item POINT_PUYO = register(new ItemCreator.Builder("point_puyo", new WearableBlockItem(MubbleBlocks.POINT_PUYO, Settings.DECORATION_BLOCK, SoundEvents.BLOCK_STONE_PLACE)));
	public static final Item HARD_PUYO = register(new ItemCreator.Builder("hard_puyo", new WearableBlockItem(MubbleBlocks.HARD_PUYO, Settings.DECORATION_BLOCK, SoundEvents.BLOCK_STONE_PLACE)));
	public static final Item IRON_PUYO = register(new ItemCreator.Builder("iron_puyo", new WearableBlockItem(MubbleBlocks.IRON_PUYO, Settings.DECORATION_BLOCK, SoundEvents.BLOCK_METAL_PLACE)));

	/* BALDI'S BASICS IN EDUCATION AND LEARNING */
	public static final Item BALDI_HEAD = register(new ItemCreator.Builder("baldi_head", new HatItem(Settings.COSTUME, SoundEvents.ENTITY_PARROT_IMITATE_VEX, true)));

	/* PETSCOP */
	public static final Item GUARDIAN_MASK = register(new ItemCreator.Builder("guardian_mask", new GuardianMaskItem(Settings.COSTUME)));

	/* YOUTUBE */
	public static final Item MAYRO_CAP = register(new ItemCreator.Builder("mayro_cap", new HatItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, false)));
	public static final Item KORETATO_BLOCK = register(new ItemCreator.Builder("koretato_block", new WearableBlockItem(MubbleBlocks.KORETATO_BLOCK, Settings.DECORATION_BLOCK, SoundEvents.BLOCK_SNOW_PLACE)));
	public static final Item NOTEBLOCK_HEAD = register(new ItemCreator.Builder("noteblock_head", new HatItem(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, true)));

	public static void init() {

	}

	public static class Settings {
		protected static final Item.Settings COSTUME = new Item.Settings().group(MubbleItemGroups.COSTUMES_GROUP).maxCount(1);
		protected static final Item.Settings DECORATION_BLOCK = new Item.Settings().group(ItemGroup.DECORATIONS);
	}
}