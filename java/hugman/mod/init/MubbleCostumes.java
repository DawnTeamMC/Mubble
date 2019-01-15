package hugman.mod.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.objects.costume.CostumeCappy;
import hugman.mod.objects.costume.CostumeGooigiCap;
import hugman.mod.objects.costume.CostumeHeadBase;
import hugman.mod.objects.costume.CostumeMayroCap;
import hugman.mod.objects.costume.CostumeSimpleEffect;
import hugman.mod.objects.costume.CostumeVanishCap;
import hugman.mod.objects.costume.CostumeWingCap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/** 
 * Init class - used to initialize costumes.
 */
public class MubbleCostumes
{
	public static final List<Item> COSTUMES = new ArrayList<Item>();
	
	public static final Item HEADPHONES = new CostumeHeadBase("headphones", CreativeTabs.MISC, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
	public static final Item CHRISTMAS_HAT = new CostumeHeadBase("christmas_hat", CreativeTabs.MISC, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
	public static final Item CAPPY = new CostumeCappy();
	public static final Item LUIGI_CAP = new CostumeSimpleEffect("luigi_cap", MubbleTabs.MUBBLE_COSTUMES, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, new PotionEffect(Potion.getPotionById(8), 1, 0), new PotionEffect(Potion.getPotionById(1), 1, 0));
	public static final Item WARIO_CAP = new CostumeSimpleEffect("wario_cap", MubbleTabs.MUBBLE_COSTUMES, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, new PotionEffect(Potion.getPotionById(2), 1, 0), new PotionEffect(Potion.getPotionById(5), 1, 0));
	public static final Item WALUIGI_CAP = new CostumeSimpleEffect("waluigi_cap", MubbleTabs.MUBBLE_COSTUMES, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, new PotionEffect(Potion.getPotionById(8), 1, 1));
	public static final Item VANISH_CAP = new CostumeVanishCap();
	public static final Item WING_CAP = new CostumeWingCap();
	public static final Item GOOIGI_CAP = new CostumeGooigiCap();
	public static final Item PRINCESS_PEACH_CROWN = new CostumeHeadBase("princess_peach_crown", MubbleTabs.MUBBLE_COSTUMES, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
	public static final Item PRINCESS_DAISY_CROWN = new CostumeHeadBase("princess_daisy_crown", MubbleTabs.MUBBLE_COSTUMES, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
	public static final Item ROSALINA_CROWN = new CostumeHeadBase("rosalina_crown", MubbleTabs.MUBBLE_COSTUMES, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
	public static final Item PINK_GOLD_PEACH_CROWN = new CostumeHeadBase("pink_gold_peach_crown", MubbleTabs.MUBBLE_COSTUMES, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
	public static final Item SUPER_CROWN = new CostumeHeadBase("super_crown", MubbleTabs.MUBBLE_COSTUMES, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
	public static final Item BROQUE_MONSIEUR_HEAD = new CostumeHeadBase("broque_monsieur_head", MubbleTabs.MUBBLE_COSTUMES, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
	public static final Item BANDANA = new CostumeHeadBase("bandana", MubbleTabs.MUBBLE_COSTUMES, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
	public static final Item TOP_HAT = new CostumeHeadBase("top_hat", MubbleTabs.MUBBLE_COSTUMES, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
	public static final Item RALSEI_HAT = new CostumeHeadBase("ralsei_hat", MubbleTabs.MUBBLE_COSTUMES, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
	public static final Item BALDI_HEAD = new CostumeHeadBase("baldi_head", MubbleTabs.MUBBLE_COSTUMES, SoundEvents.E_PARROT_IM_VEX);
	public static final Item MAYRO_CAP = new CostumeMayroCap();
	public static final Item NOTEBLOCK_HEAD = new CostumeHeadBase("noteblock_head", MubbleTabs.MUBBLE_COSTUMES, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
	public static final Item SNORLAX_HAT = new CostumeHeadBase("snorlax_hat", MubbleTabs.MUBBLE_COSTUMES, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
	public static final Item KING_ROUND_MASK = new CostumeHeadBase("king_round_mask", MubbleTabs.MUBBLE_COSTUMES, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
}
