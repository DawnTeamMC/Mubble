package hugman.mubble.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mubble.Mubble;
import hugman.mubble.objects.block.block_state_property.Princess;
import hugman.mubble.objects.costume.BlockCostume;
import hugman.mubble.objects.costume.CappyCostume;
import hugman.mubble.objects.costume.GooigiCapCostume;
import hugman.mubble.objects.costume.GuardianMaskCostume;
import hugman.mubble.objects.costume.HeadCostume;
import hugman.mubble.objects.costume.MayroCapCostume;
import hugman.mubble.objects.costume.CrownCostume;
import hugman.mubble.objects.costume.VanishCapCostume;
import hugman.mubble.objects.costume.WingCapCostume;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;

public class MubbleCostumes
{
	/* All Content Bag */
	public static final List<Item> COSTUMES = new ArrayList<Item>();

	/* Templates */
	public static final Item.Properties pSimple = new Item.Properties().group(MubbleTabs.COSTUMES).maxStackSize(1);
	public static final Item.Properties pBlockDecorations = new Item.Properties().group(ItemGroup.DECORATIONS);
	
	/* MINECRAFT */
	public static final Item HEADPHONES = costume("headphones", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
	public static final Item CHRISTMAS_HAT = costume("christmas_hat", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));

	/* SUPER MARIO */
	public static final Item CAPPY = costume("cappy", new CappyCostume());
	public static final Item LUIGI_CAP = costume("luigi_cap", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, new EffectInstance(Effects.JUMP_BOOST, 5, 0), new EffectInstance(Effects.SPEED, 5, 0)));
	public static final Item WARIO_CAP = costume("wario_cap", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, new EffectInstance(Effects.SLOWNESS, 5, 0), new EffectInstance(Effects.STRENGTH, 5, 0)));
	public static final Item WALUIGI_CAP = costume("waluigi_cap", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, new EffectInstance(Effects.JUMP_BOOST, 5, 1)));
	public static final Item VANISH_CAP = costume("vanish_cap", new VanishCapCostume(pSimple));
	public static final Item WING_CAP = costume("wing_cap", new WingCapCostume(pSimple.defaultMaxDamage(600)));
	public static final Item GOOIGI_CAP = costume("gooigi_cap", new GooigiCapCostume(pSimple));
	public static final Item GOLD_MARIO_CAP = costume("gold_mario_cap", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_GOLD));
	public static final Item SILVER_LUIGI_CAP = costume("silver_luigi_cap", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
	public static final Item PRINCESS_PEACH_CROWN = costume("princess_peach_crown", new CrownCostume(pSimple, Princess.PEACH));
	public static final Item PRINCESS_DAISY_CROWN = costume("princess_daisy_crown", new CrownCostume(pSimple, Princess.DAISY));
	public static final Item ROSALINA_CROWN = costume("rosalina_crown", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
	public static final Item PINK_GOLD_PEACH_CROWN = costume("pink_gold_peach_crown", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
	public static final Item SUPER_CROWN = costume("super_crown", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
	public static final Item MARIO_WEDDING_HAT = costume("mario_wedding_hat", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));
	public static final Item BOO_HAT = costume("boo_hat", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));
	public static final Item BROQUE_MONSIEUR_HEAD = costume("broque_monsieur_head", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
	
	/* KIRBY */
	public static final Item BANDANA = costume("bandana", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));
	
	/* POKEMON */
	public static final Item PIKACHU_EARS = costume("pikachu_ears", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));
	public static final Item SNORLAX_HAT = costume("snorlax_hat", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));

	/* PROFESSOR LAYTON */
	public static final Item TOP_HAT = costume("top_hat", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));
	
	/* UNDERTALE / DELTARUNE */
	public static final Item RALSEI_HAT = costume("ralsei_hat", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));
	public static final Item KING_ROUND_MASK = costume("king_round_mask", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));
	
	/* A HAT IN TIME */
	public static final Item KID_HAT = costume("kid_hat", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));

	/* PUYO PUYO */
	public static final Item RED_PUYO = costume("red_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.RED_PUYO));
	public static final Item YELLOW_PUYO = costume("yellow_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.YELLOW_PUYO));
	public static final Item GREEN_PUYO = costume("green_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.GREEN_PUYO));
	public static final Item TURQUOISE_PUYO = costume("turquoise_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.TURQUOISE_PUYO));
	public static final Item BLUE_PUYO = costume("blue_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.BLUE_PUYO));
	public static final Item PURPLE_PUYO = costume("purple_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.PURPLE_PUYO));
	public static final Item GRAY_PUYO = costume("gray_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.GRAY_PUYO));
	public static final Item GARBAGE_PUYO = costume("garbage_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_STONE_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.GARBAGE_PUYO));
	public static final Item POINT_PUYO = costume("point_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_STONE_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.POINT_PUYO));
	public static final Item HARD_PUYO = costume("hard_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_STONE_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.HARD_PUYO));
	public static final Item IRON_PUYO = costume("iron_puyo", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_METAL_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.IRON_PUYO));
	
	/* BALDI'S BASICS IN EDUCATION AND LEARNING */
	public static final Item BALDI_HEAD = costume("baldi_head", new HeadCostume(pSimple, SoundEvents.ENTITY_PARROT_IMITATE_VEX));

	/* PETSCOP */
	public static final Item GUARDIAN_MASK = costume("guardian_mask", new GuardianMaskCostume(pSimple));

	/* YOUTUBE */
	public static final Item MAYRO_CAP = costume("mayro_cap", new MayroCapCostume(pSimple));
	public static final Item KORETATO_BLOCK = costume("koretato_block", new BlockCostume(pBlockDecorations, SoundEvents.BLOCK_SNOW_PLACE, EquipmentSlotType.HEAD, MubbleBlocks.KORETATO_BLOCK));
	public static final Item NOTEBLOCK_HEAD = costume("noteblock_head", new HeadCostume(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
	
    public static Item costume(String name, Item item)
    {
    	Item fCostume = item.setRegistryName(Mubble.MOD_ID, name);
        COSTUMES.add(fCostume);
		return fCostume;
    }
}
