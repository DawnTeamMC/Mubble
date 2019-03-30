package hugman.mod.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.objects.costume.CostumeBlock;
import hugman.mod.objects.costume.CostumeCappy;
import hugman.mod.objects.costume.CostumeEffect;
import hugman.mod.objects.costume.CostumeGooigiCap;
import hugman.mod.objects.costume.CostumeMayroCap;
import hugman.mod.objects.costume.CostumePaulMask;
import hugman.mod.objects.costume.CostumePrincessPeachCrown;
import hugman.mod.objects.costume.CostumeSimple;
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
	public static final List<Item> COSTUMES = new ArrayList<Item>();
	
	public static final Item HEADPHONES = new CostumeSimple("headphones", SoundEvents.ITEM_ARMOR_EQUIP_IRON, EntityEquipmentSlot.HEAD);
	public static final Item CHRISTMAS_HAT = new CostumeSimple("christmas_hat", SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, EntityEquipmentSlot.HEAD);

	public static final Item CAPPY = new CostumeCappy();
	public static final Item LUIGI_CAP = new CostumeEffect("luigi_cap", SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, EntityEquipmentSlot.HEAD, new PotionEffect(MobEffects.JUMP_BOOST, 1, 0), new PotionEffect(MobEffects.SPEED, 1, 0));
	public static final Item WARIO_CAP = new CostumeEffect("wario_cap", SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, EntityEquipmentSlot.HEAD, new PotionEffect(MobEffects.SLOWNESS, 1, 0), new PotionEffect(MobEffects.STRENGTH, 1, 0));
	public static final Item WALUIGI_CAP = new CostumeEffect("waluigi_cap", SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, EntityEquipmentSlot.HEAD, new PotionEffect(MobEffects.JUMP_BOOST, 2, 0));
	public static final Item VANISH_CAP = new CostumeVanishCap();
	public static final Item WING_CAP = new CostumeWingCap();
	public static final Item GOOIGI_CAP = new CostumeGooigiCap();
	public static final Item GOLD_MARIO_CAP = new CostumeSimple("gold_mario_cap", SoundEvents.ITEM_ARMOR_EQUIP_GOLD, EntityEquipmentSlot.HEAD);
	public static final Item SILVER_LUIGI_CAP = new CostumeSimple("silver_luigi_cap", SoundEvents.ITEM_ARMOR_EQUIP_IRON, EntityEquipmentSlot.HEAD);
	public static final Item PRINCESS_PEACH_CROWN = new CostumePrincessPeachCrown();
	public static final Item PRINCESS_DAISY_CROWN = new CostumeSimple("princess_daisy_crown", SoundEvents.ITEM_ARMOR_EQUIP_IRON, EntityEquipmentSlot.HEAD);
	public static final Item ROSALINA_CROWN = new CostumeSimple("rosalina_crown", SoundEvents.ITEM_ARMOR_EQUIP_IRON, EntityEquipmentSlot.HEAD);
	public static final Item PINK_GOLD_PEACH_CROWN = new CostumeSimple("pink_gold_peach_crown", SoundEvents.ITEM_ARMOR_EQUIP_IRON, EntityEquipmentSlot.HEAD);
	public static final Item SUPER_CROWN = new CostumeSimple("super_crown", SoundEvents.ITEM_ARMOR_EQUIP_IRON, EntityEquipmentSlot.HEAD);
	public static final Item MARIO_WEDDING_HAT = new CostumeSimple("mario_wedding_hat", SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, EntityEquipmentSlot.HEAD);
	public static final Item BROQUE_MONSIEUR_HEAD = new CostumeSimple("broque_monsieur_head", SoundEvents.ITEM_ARMOR_EQUIP_IRON, EntityEquipmentSlot.HEAD);
	
	public static final Item BANDANA = new CostumeSimple("bandana", SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, EntityEquipmentSlot.HEAD);
	
	public static final Item SNORLAX_HAT = new CostumeSimple("snorlax_hat", SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, EntityEquipmentSlot.HEAD);

	public static final Item TOP_HAT = new CostumeSimple("top_hat", SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, EntityEquipmentSlot.HEAD);
	
	public static final Item RALSEI_HAT = new CostumeSimple("ralsei_hat", SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, EntityEquipmentSlot.HEAD);
	public static final Item KING_ROUND_MASK = new CostumeSimple("king_round_mask", SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, EntityEquipmentSlot.HEAD);

	public static final Item RED_PUYO = new CostumeBlock(MubbleBlocks.RED_PUYO, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EntityEquipmentSlot.HEAD, ItemGroup.DECORATIONS);
	public static final Item YELLOW_PUYO = new CostumeBlock(MubbleBlocks.YELLOW_PUYO, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EntityEquipmentSlot.HEAD, ItemGroup.DECORATIONS);
	public static final Item GREEN_PUYO = new CostumeBlock(MubbleBlocks.GREEN_PUYO, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EntityEquipmentSlot.HEAD, ItemGroup.DECORATIONS);
	public static final Item TURQUOISE_PUYO = new CostumeBlock(MubbleBlocks.TURQUOISE_PUYO, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EntityEquipmentSlot.HEAD, ItemGroup.DECORATIONS);
	public static final Item BLUE_PUYO = new CostumeBlock(MubbleBlocks.BLUE_PUYO, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EntityEquipmentSlot.HEAD, ItemGroup.DECORATIONS);
	public static final Item PURPLE_PUYO = new CostumeBlock(MubbleBlocks.PURPLE_PUYO, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, EntityEquipmentSlot.HEAD, ItemGroup.DECORATIONS);
	public static final Item GARBAGE_PUYO = new CostumeBlock(MubbleBlocks.GARBAGE_PUYO, SoundEvents.BLOCK_STONE_PLACE, EntityEquipmentSlot.HEAD, ItemGroup.DECORATIONS);
	public static final Item POINT_PUYO = new CostumeBlock(MubbleBlocks.POINT_PUYO, SoundEvents.BLOCK_STONE_PLACE, EntityEquipmentSlot.HEAD, ItemGroup.DECORATIONS);
	public static final Item HARD_PUYO = new CostumeBlock(MubbleBlocks.HARD_PUYO, SoundEvents.BLOCK_STONE_PLACE, EntityEquipmentSlot.HEAD, ItemGroup.DECORATIONS);
	public static final Item IRON_PUYO = new CostumeBlock(MubbleBlocks.IRON_PUYO, SoundEvents.BLOCK_METAL_PLACE, EntityEquipmentSlot.HEAD, ItemGroup.DECORATIONS);
	
	public static final Item BALDI_HEAD = new CostumeSimple("baldi_head", SoundEvents.ENTITY_PARROT_IMITATE_VEX, EntityEquipmentSlot.HEAD);

	public static final Item PAUL_MASK = new CostumePaulMask();

	public static final Item MAYRO_CAP = new CostumeMayroCap();
	public static final Item KORETATO_BLOCK = new CostumeBlock(MubbleBlocks.KORETATO_BLOCK, SoundEvents.BLOCK_SNOW_PLACE, EntityEquipmentSlot.HEAD, ItemGroup.DECORATIONS);
	public static final Item NOTEBLOCK_HEAD = new CostumeSimple("noteblock_head", SoundEvents.ITEM_ARMOR_EQUIP_IRON, EntityEquipmentSlot.HEAD);
	
	
    public static void register(Item item)
    {
    	COSTUMES.add(item);
    }
}
