package hugman.mubble.init;

import hugman.mubble.Mubble;
import hugman.mubble.init.client.MubbleShaders;
import hugman.mubble.object.block.block_state_property.Princess;
import hugman.mubble.object.item.costume.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import hugman.mubble.util.entry.ItemEntry.Builder;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MubbleCostumes {
	/* Templates */
	protected static final Item.Settings pSimple = new Item.Settings().group(MubbleTabs.COSTUMES).maxCount(1);
	protected static final Item.Settings pBlockDecorations = new Item.Settings().group(ItemGroup.DECORATIONS);

	/* MINECRAFT */
	public static final Item HEADPHONES = new Builder("headphones", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON)).build();

	public static final Item WHITE_VIRTUAL_GOOGLES = new Builder("white_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.WHITE_RETRO)).build();
	public static final Item LIGHT_GRAY_VIRTUAL_GOOGLES = new Builder("light_gray_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.LIGHT_GRAY_RETRO)).build();
	public static final Item GRAY_VIRTUAL_GOOGLES = new Builder("gray_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.GRAY_RETRO)).build();
	public static final Item BLACK_VIRTUAL_GOOGLES = new Builder("black_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.BLACK_RETRO)).build();
	public static final Item BROWN_VIRTUAL_GOOGLES = new Builder("brown_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.BROWN_RETRO)).build();
	public static final Item RED_VIRTUAL_GOOGLES = new Builder("red_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.RED_RETRO)).build();
	public static final Item ORANGE_VIRTUAL_GOOGLES = new Builder("orange_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.ORANGE_RETRO)).build();
	public static final Item YELLOW_VIRTUAL_GOOGLES = new Builder("yellow_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.YELLOW_RETRO)).build();
	public static final Item LIME_VIRTUAL_GOOGLES = new Builder("lime_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.LIME_RETRO)).build();
	public static final Item GREEN_VIRTUAL_GOOGLES = new Builder("green_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.GREEN_RETRO)).build();
	public static final Item CYAN_VIRTUAL_GOOGLES = new Builder("cyan_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.CYAN_RETRO)).build();
	public static final Item LIGHT_BLUE_VIRTUAL_GOOGLES = new Builder("light_blue_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.LIGHT_BLUE_RETRO)).build();
	public static final Item BLUE_VIRTUAL_GOOGLES = new Builder("blue_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.BLUE_RETRO)).build();
	public static final Item PURPLE_VIRTUAL_GOOGLES = new Builder("purple_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.PURPLE_RETRO)).build();
	public static final Item MAGENTA_VIRTUAL_GOOGLES = new Builder("magenta_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.MAGENTA_RETRO)).build();
	public static final Item PINK_VIRTUAL_GOOGLES = new Builder("pink_virtual_googles", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON, MubbleShaders.PINK_RETRO)).build();

	public static final Item CHRISTMAS_HAT = new Builder("christmas_hat", new ChristmasHatCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)).build();

	/* SUPER MARIO */
	public static final Item CAPPY = new Builder("cappy", new CappyCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)).build();
	public static final Item LUIGI_CAP = new Builder("luigi_cap", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, new StatusEffectInstance(StatusEffects.JUMP_BOOST, 5, 0), new StatusEffectInstance(StatusEffects.SPEED, 5, 0))).build();
	public static final Item WARIO_CAP = new Builder("wario_cap", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 0), new StatusEffectInstance(StatusEffects.STRENGTH, 5, 0))).build();
	public static final Item WALUIGI_CAP = new Builder("waluigi_cap", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, new StatusEffectInstance(StatusEffects.JUMP_BOOST, 5, 1))).build();
	public static final Item VANISH_CAP = new Builder("vanish_cap", new VanishCapCostume(pSimple)).build();
	public static final Item WING_CAP = new Builder("wing_cap", new WingCapCostume(pSimple.maxDamageIfAbsent(600))).build();
	public static final Item GOOIGI_CAP = new Builder("gooigi_cap", new GooigiCapCostume(pSimple, SoundEvents.BLOCK_SLIME_BLOCK_PLACE)).build();
	public static final Item GOLD_MARIO_CAP = new Builder("gold_mario_cap", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_GOLD)).build();
	public static final Item SILVER_LUIGI_CAP = new Builder("silver_luigi_cap", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON)).build();
	public static final Item PRINCESS_PEACH_CROWN = new Builder("princess_peach_crown", new CrownCostume(pSimple, Princess.PEACH)).build();
	public static final Item PRINCESS_DAISY_CROWN = new Builder("princess_daisy_crown", new CrownCostume(pSimple, Princess.DAISY)).build();
	public static final Item ROSALINA_CROWN = new Builder("rosalina_crown", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON)).build();
	public static final Item PINK_GOLD_PEACH_CROWN = new Builder("pink_gold_peach_crown", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON)).build();
	public static final Item SUPER_CROWN = new Builder("super_crown", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON)).build();
	public static final Item MARIO_WEDDING_HAT = new Builder("mario_wedding_hat", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)).build();
	public static final Item BOO_HAT = new Builder("boo_hat", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)).build();
	public static final Item BROQUE_MONSIEUR_HEAD = new Builder("broque_monsieur_head", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON)).build();

	/* KIRBY */
	public static final Item BANDANA = new Builder("bandana", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)).build();

	/* POKEMON */
	public static final Item PIKACHU_EARS = new Builder("pikachu_ears", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)).build();
	public static final Item SNORLAX_HAT = new Builder("snorlax_hat", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)).build();

	/* PROFESSOR LAYTON */
	public static final Item TOP_HAT = new Builder("top_hat", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)).build();

	/* UNDERTALE / DELTARUNE */
	public static final Item RALSEI_HAT = new Builder("ralsei_hat", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)).build();
	public static final Item KING_ROUND_MASK = new Builder("king_round_mask", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)).build();

	/* A HAT IN TIME */
	public static final Item KID_HAT = new Builder("kid_hat", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)).build();

	/* PUYO PUYO */
	public static final Item RED_PUYO = new Builder("red_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlot.HEAD, MubbleBlocks.RED_PUYO)).build();
	public static final Item YELLOW_PUYO = new Builder("yellow_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlot.HEAD, MubbleBlocks.YELLOW_PUYO)).build();
	public static final Item GREEN_PUYO = new Builder("green_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlot.HEAD, MubbleBlocks.GREEN_PUYO)).build();
	public static final Item TURQUOISE_PUYO = new Builder("turquoise_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlot.HEAD, MubbleBlocks.TURQUOISE_PUYO)).build();
	public static final Item BLUE_PUYO = new Builder("blue_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlot.HEAD, MubbleBlocks.BLUE_PUYO)).build();
	public static final Item PURPLE_PUYO = new Builder("purple_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlot.HEAD, MubbleBlocks.PURPLE_PUYO)).build();
	public static final Item GRAY_PUYO = new Builder("gray_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlot.HEAD, MubbleBlocks.GRAY_PUYO)).build();
	public static final Item GARBAGE_PUYO = new Builder("garbage_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_STONE_PLACE, EquipmentSlot.HEAD, MubbleBlocks.GARBAGE_PUYO)).build();
	public static final Item POINT_PUYO = new Builder("point_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_STONE_PLACE, EquipmentSlot.HEAD, MubbleBlocks.POINT_PUYO)).build();
	public static final Item HARD_PUYO = new Builder("hard_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_STONE_PLACE, EquipmentSlot.HEAD, MubbleBlocks.HARD_PUYO)).build();
	public static final Item IRON_PUYO = new Builder("iron_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_METAL_PLACE, EquipmentSlot.HEAD, MubbleBlocks.IRON_PUYO)).build();

	/* BALDI'S BASICS IN EDUCATION AND LEARNING */
	public static final Item BALDI_HEAD = new Builder("baldi_head", new HeadCostume(pSimple, SoundEvents.ENTITY_PARROT_IMITATE_VEX)).build();

	/* PETSCOP */
	public static final Item GUARDIAN_MASK = new Builder("guardian_mask", new GuardianMaskCostume(pSimple)).build();

	/* YOUTUBE */
	public static final Item MAYRO_CAP = new Builder("mayro_cap", new MayroCapCostume(pSimple)).build();
	public static final Item KORETATO_BLOCK = new Builder("koretato_block", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SNOW_PLACE, EquipmentSlot.HEAD, MubbleBlocks.KORETATO_BLOCK)).build();
	public static final Item NOTEBLOCK_HEAD = new Builder("noteblock_head", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON)).build();
}
