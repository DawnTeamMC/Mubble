package hugman.mod.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.Mubble;
import hugman.mod.objects.costume.CostumeBlock;
import hugman.mod.objects.costume.CostumeCappy;
import hugman.mod.objects.costume.CostumeGooigiCap;
import hugman.mod.objects.costume.CostumeGuardianMask;
import hugman.mod.objects.costume.CostumeHead;
import hugman.mod.objects.costume.CostumeMayroCap;
import hugman.mod.objects.costume.CostumePrincessPeachCrown;
import hugman.mod.objects.costume.CostumeVanishCap;
import hugman.mod.objects.costume.CostumeWingCap;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.PotionEffect;

public class MubbleCostumes
{
	/* All Content Bag */
	public static final List<Item> COSTUMES = new ArrayList<Item>();

	/* Templates */
	public static final Item.Properties pSimple = new Item.Properties().group(MubbleTabs.COSTUMES).maxStackSize(1);
	public static final Item.Properties pBlockDecorations = new Item.Properties().group(ItemGroup.DECORATIONS);
	
	/* MINECRAFT */
	public static final Item HEADPHONES = costume("headphones", new CostumeHead(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
	public static final Item CHRISTMAS_HAT = costume("christmas_hat", new CostumeHead(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));

	/* SUPER MARIO */
	public static final Item CAPPY = costume("cappy", new CostumeCappy());
	public static final Item LUIGI_CAP = costume("luigi_cap", new CostumeHead(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, new PotionEffect(MobEffects.JUMP_BOOST, 5, 0), new PotionEffect(MobEffects.SPEED, 5, 0)));
	public static final Item WARIO_CAP = costume("wario_cap", new CostumeHead(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, new PotionEffect(MobEffects.SLOWNESS, 5, 0), new PotionEffect(MobEffects.STRENGTH, 5, 0)));
	public static final Item WALUIGI_CAP = costume("waluigi_cap", new CostumeHead(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, new PotionEffect(MobEffects.JUMP_BOOST, 5, 1)));
	public static final Item VANISH_CAP = costume("vanish_cap", new CostumeVanishCap(pSimple));
	public static final Item WING_CAP = costume("wing_cap", new CostumeWingCap(pSimple.defaultMaxDamage(600)));
	public static final Item GOOIGI_CAP = costume("gooigi_cap", new CostumeGooigiCap(pSimple));
	public static final Item GOLD_MARIO_CAP = costume("gold_mario_cap", new CostumeHead(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_GOLD));
	public static final Item SILVER_LUIGI_CAP = costume("silver_luigi_cap", new CostumeHead(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
	public static final Item PRINCESS_PEACH_CROWN = costume("princess_peach_crown", new CostumePrincessPeachCrown(pSimple));
	public static final Item PRINCESS_DAISY_CROWN = costume("princess_daisy_crown", new CostumeHead(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
	public static final Item ROSALINA_CROWN = costume("rosalina_crown", new CostumeHead(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
	public static final Item PINK_GOLD_PEACH_CROWN = costume("pink_gold_peach_crown", new CostumeHead(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
	public static final Item SUPER_CROWN = costume("super_crown", new CostumeHead(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
	public static final Item MARIO_WEDDING_HAT = costume("mario_wedding_hat", new CostumeHead(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));
	public static final Item BROQUE_MONSIEUR_HEAD = costume("broque_monsieur_head", new CostumeHead(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
	
	/* KIRBY */
	public static final Item BANDANA = costume("bandana", new CostumeHead(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));
	
	/* POKÉMON */
	public static final Item SNORLAX_HAT = costume("snorlax_hat", new CostumeHead(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));

	/* PROFESSOR LAYTON */
	public static final Item TOP_HAT = costume("top_hat", new CostumeHead(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));
	
	/* UNDERTALE / DELTARUNE */
	public static final Item RALSEI_HAT = costume("ralsei_hat", new CostumeHead(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));
	public static final Item KING_ROUND_MASK = costume("king_round_mask", new CostumeHead(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER));

	/* PUYO PUYO */
	public static final Item RED_PUYO = costume("red_puyo", new CostumeBlock(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EntityEquipmentSlot.HEAD, MubbleBlocks.RED_PUYO));
	public static final Item YELLOW_PUYO = costume("yellow_puyo", new CostumeBlock(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EntityEquipmentSlot.HEAD, MubbleBlocks.YELLOW_PUYO));
	public static final Item GREEN_PUYO = costume("green_puyo", new CostumeBlock(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EntityEquipmentSlot.HEAD, MubbleBlocks.GREEN_PUYO));
	public static final Item TURQUOISE_PUYO = costume("turquoise_puyo", new CostumeBlock(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EntityEquipmentSlot.HEAD, MubbleBlocks.TURQUOISE_PUYO));
	public static final Item BLUE_PUYO = costume("blue_puyo", new CostumeBlock(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EntityEquipmentSlot.HEAD, MubbleBlocks.BLUE_PUYO));
	public static final Item PURPLE_PUYO = costume("purple_puyo", new CostumeBlock(pBlockDecorations, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EntityEquipmentSlot.HEAD, MubbleBlocks.PURPLE_PUYO));
	public static final Item GARBAGE_PUYO = costume("garbage_puyo", new CostumeBlock(pBlockDecorations, SoundEvents.BLOCK_STONE_PLACE, EntityEquipmentSlot.HEAD, MubbleBlocks.GARBAGE_PUYO));
	public static final Item POINT_PUYO = costume("point_puyo", new CostumeBlock(pBlockDecorations, SoundEvents.BLOCK_STONE_PLACE, EntityEquipmentSlot.HEAD, MubbleBlocks.POINT_PUYO));
	public static final Item HARD_PUYO = costume("hard_puyo", new CostumeBlock(pBlockDecorations, SoundEvents.BLOCK_STONE_PLACE, EntityEquipmentSlot.HEAD, MubbleBlocks.HARD_PUYO));
	public static final Item IRON_PUYO = costume("iron_puyo", new CostumeBlock(pBlockDecorations, SoundEvents.BLOCK_METAL_PLACE, EntityEquipmentSlot.HEAD, MubbleBlocks.IRON_PUYO));
	
	/* BALDI’S BASICS IN EDUCATION AND LEARNING */
	public static final Item BALDI_HEAD = costume("baldi_head", new CostumeHead(pSimple, SoundEvents.ENTITY_PARROT_IMITATE_VEX));

	/* PETSCOP */
	public static final Item GUARDIAN_MASK = costume("guardian_mask", new CostumeGuardianMask(pSimple));

	/* YOUTUBE */
	public static final Item MAYRO_CAP = costume("mayro_cap", new CostumeMayroCap(pSimple));
	public static final Item KORETATO_BLOCK = costume("koretato_block", new CostumeBlock(pBlockDecorations, SoundEvents.BLOCK_SNOW_PLACE, EntityEquipmentSlot.HEAD, MubbleBlocks.KORETATO_BLOCK));
	public static final Item NOTEBLOCK_HEAD = costume("noteblock_head", new CostumeHead(pSimple, SoundEvents.ITEM_ARMOR_EQUIP_IRON));
	
    public static Item costume(String name, Item item)
    {
    	Item fCostume = item.setRegistryName(Mubble.MOD_ID, name);
        COSTUMES.add(fCostume);
		return fCostume;
    }
}
