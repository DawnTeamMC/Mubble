package com.hugman.mubble.init;

import com.hugman.dawn.api.creator.ItemCreator.Builder;
import com.hugman.mubble.init.client.MubbleShaders;
import com.hugman.mubble.object.block.block_state_property.Princess;
import com.hugman.mubble.object.item.costume.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.SoundEvents;

public class MubbleCostumes extends MubblePack {
	/* MINECRAFT */
	public static final Item HEADPHONES = register(new Builder("headphones", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON)));

	public static final Item WHITE_VIRTUAL_GOOGLES = register(new Builder("white_virtual_googles", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.WHITE_RETRO)));
	public static final Item LIGHT_GRAY_VIRTUAL_GOOGLES = register(new Builder("light_gray_virtual_googles", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.LIGHT_GRAY_RETRO)));
	public static final Item GRAY_VIRTUAL_GOOGLES = register(new Builder("gray_virtual_googles", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.GRAY_RETRO)));
	public static final Item BLACK_VIRTUAL_GOOGLES = register(new Builder("black_virtual_googles", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.BLACK_RETRO)));
	public static final Item BROWN_VIRTUAL_GOOGLES = register(new Builder("brown_virtual_googles", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.BROWN_RETRO)));
	public static final Item RED_VIRTUAL_GOOGLES = register(new Builder("red_virtual_googles", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.RED_RETRO)));
	public static final Item ORANGE_VIRTUAL_GOOGLES = register(new Builder("orange_virtual_googles", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.ORANGE_RETRO)));
	public static final Item YELLOW_VIRTUAL_GOOGLES = register(new Builder("yellow_virtual_googles", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.YELLOW_RETRO)));
	public static final Item LIME_VIRTUAL_GOOGLES = register(new Builder("lime_virtual_googles", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.LIME_RETRO)));
	public static final Item GREEN_VIRTUAL_GOOGLES = register(new Builder("green_virtual_googles", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.GREEN_RETRO)));
	public static final Item CYAN_VIRTUAL_GOOGLES = register(new Builder("cyan_virtual_googles", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.CYAN_RETRO)));
	public static final Item LIGHT_BLUE_VIRTUAL_GOOGLES = register(new Builder("light_blue_virtual_googles", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.LIGHT_BLUE_RETRO)));
	public static final Item BLUE_VIRTUAL_GOOGLES = register(new Builder("blue_virtual_googles", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.BLUE_RETRO)));
	public static final Item PURPLE_VIRTUAL_GOOGLES = register(new Builder("purple_virtual_googles", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.PURPLE_RETRO)));
	public static final Item MAGENTA_VIRTUAL_GOOGLES = register(new Builder("magenta_virtual_googles", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.MAGENTA_RETRO)));
	public static final Item PINK_VIRTUAL_GOOGLES = register(new Builder("pink_virtual_googles", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.PINK_RETRO)));

	public static final Item CHRISTMAS_HAT = register(new Builder("christmas_hat", new ChristmasHatCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)));

	/* SUPER MARIO */
	public static final Item CAPPY = register(new Builder("cappy", new CappyCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)));
	public static final Item LUIGI_CAP = register(new Builder("luigi_cap", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, new StatusEffectInstance(StatusEffects.JUMP_BOOST, 5, 0), new StatusEffectInstance(StatusEffects.SPEED, 5, 0))));
	public static final Item WARIO_CAP = register(new Builder("wario_cap", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 0), new StatusEffectInstance(StatusEffects.STRENGTH, 5, 0))));
	public static final Item WALUIGI_CAP = register(new Builder("waluigi_cap", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, new StatusEffectInstance(StatusEffects.JUMP_BOOST, 5, 1))));
	public static final Item VANISH_CAP = register(new Builder("vanish_cap", new VanishCapCostume(Settings.COSTUME)));
	public static final Item WING_CAP = register(new Builder("wing_cap", new WingCapCostume(Settings.COSTUME.maxDamageIfAbsent(600))));
	public static final Item GOOIGI_CAP = register(new Builder("gooigi_cap", new GooigiCapCostume(Settings.COSTUME, SoundEvents.BLOCK_SLIME_BLOCK_PLACE)));
	public static final Item GOLD_MARIO_CAP = register(new Builder("gold_mario_cap", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_GOLD)));
	public static final Item SILVER_LUIGI_CAP = register(new Builder("silver_luigi_cap", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON)));
	public static final Item PRINCESS_PEACH_CROWN = register(new Builder("princess_peach_crown", new CrownCostume(Settings.COSTUME, Princess.PEACH)));
	public static final Item PRINCESS_DAISY_CROWN = register(new Builder("princess_daisy_crown", new CrownCostume(Settings.COSTUME, Princess.DAISY)));
	public static final Item ROSALINA_CROWN = register(new Builder("rosalina_crown", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON)));
	public static final Item PINK_GOLD_PEACH_CROWN = register(new Builder("pink_gold_peach_crown", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON)));
	public static final Item SUPER_CROWN = register(new Builder("super_crown", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON)));
	public static final Item MARIO_WEDDING_HAT = register(new Builder("mario_wedding_hat", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)));
	public static final Item BOO_HAT = register(new Builder("boo_hat", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)));
	public static final Item BROQUE_MONSIEUR_HEAD = register(new Builder("broque_monsieur_head", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON)));

	/* KIRBY */
	public static final Item BANDANA = register(new Builder("bandana", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)));

	/* POKEMON */
	public static final Item PIKACHU_EARS = register(new Builder("pikachu_ears", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)));
	public static final Item SNORLAX_HAT = register(new Builder("snorlax_hat", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)));

	/* PROFESSOR LAYTON */
	public static final Item TOP_HAT = register(new Builder("top_hat", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)));

	/* UNDERTALE / DELTARUNE */
	public static final Item RALSEI_HAT = register(new Builder("ralsei_hat", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)));
	public static final Item KING_ROUND_MASK = register(new Builder("king_round_mask", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)));

	/* A HAT IN TIME */
	public static final Item KID_HAT = register(new Builder("kid_hat", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)));

	/* PUYO PUYO */
	public static final Item RED_PUYO = register(new Builder("red_puyo", new BlockCostume(Settings.DECORATION_BLOCK, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlot.HEAD, MubbleBlocks.RED_PUYO)));
	public static final Item YELLOW_PUYO = register(new Builder("yellow_puyo", new BlockCostume(Settings.DECORATION_BLOCK, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlot.HEAD, MubbleBlocks.YELLOW_PUYO)));
	public static final Item GREEN_PUYO = register(new Builder("green_puyo", new BlockCostume(Settings.DECORATION_BLOCK, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlot.HEAD, MubbleBlocks.GREEN_PUYO)));
	public static final Item TURQUOISE_PUYO = register(new Builder("turquoise_puyo", new BlockCostume(Settings.DECORATION_BLOCK, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlot.HEAD, MubbleBlocks.TURQUOISE_PUYO)));
	public static final Item BLUE_PUYO = register(new Builder("blue_puyo", new BlockCostume(Settings.DECORATION_BLOCK, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlot.HEAD, MubbleBlocks.BLUE_PUYO)));
	public static final Item PURPLE_PUYO = register(new Builder("purple_puyo", new BlockCostume(Settings.DECORATION_BLOCK, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlot.HEAD, MubbleBlocks.PURPLE_PUYO)));
	public static final Item GRAY_PUYO = register(new Builder("gray_puyo", new BlockCostume(Settings.DECORATION_BLOCK, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlot.HEAD, MubbleBlocks.GRAY_PUYO)));
	public static final Item GARBAGE_PUYO = register(new Builder("garbage_puyo", new BlockCostume(Settings.DECORATION_BLOCK, SoundEvents.BLOCK_STONE_PLACE, EquipmentSlot.HEAD, MubbleBlocks.GARBAGE_PUYO)));
	public static final Item POINT_PUYO = register(new Builder("point_puyo", new BlockCostume(Settings.DECORATION_BLOCK, SoundEvents.BLOCK_STONE_PLACE, EquipmentSlot.HEAD, MubbleBlocks.POINT_PUYO)));
	public static final Item HARD_PUYO = register(new Builder("hard_puyo", new BlockCostume(Settings.DECORATION_BLOCK, SoundEvents.BLOCK_STONE_PLACE, EquipmentSlot.HEAD, MubbleBlocks.HARD_PUYO)));
	public static final Item IRON_PUYO = register(new Builder("iron_puyo", new BlockCostume(Settings.DECORATION_BLOCK, SoundEvents.BLOCK_METAL_PLACE, EquipmentSlot.HEAD, MubbleBlocks.IRON_PUYO)));

	/* BALDI'S BASICS IN EDUCATION AND LEARNING */
	public static final Item BALDI_HEAD = register(new Builder("baldi_head", new HeadCostume(Settings.COSTUME, SoundEvents.ENTITY_PARROT_IMITATE_VEX)));

	/* PETSCOP */
	public static final Item GUARDIAN_MASK = register(new Builder("guardian_mask", new GuardianMaskCostume(Settings.COSTUME)));

	/* YOUTUBE */
	public static final Item MAYRO_CAP = register(new Builder("mayro_cap", new MayroCapCostume(Settings.COSTUME)));
	public static final Item KORETATO_BLOCK = register(new Builder("koretato_block", new BlockCostume(Settings.DECORATION_BLOCK, SoundEvents.BLOCK_SNOW_PLACE, EquipmentSlot.HEAD, MubbleBlocks.KORETATO_BLOCK)));
	public static final Item NOTEBLOCK_HEAD = register(new Builder("noteblock_head", new HeadCostume(Settings.COSTUME, SoundEvents.ITEM_ARMOR_EQUIP_IRON)));

	public static class Settings {
		protected static final Item.Settings COSTUME = new Item.Settings().group(MubbleTabs.COSTUMES).maxCount(1);
		protected static final Item.Settings DECORATION_BLOCK = new Item.Settings().group(ItemGroup.DECORATIONS);
	}
}
