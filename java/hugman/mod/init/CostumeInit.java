package hugman.mod.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.objects.costumes.CostumeCappy;
import hugman.mod.objects.costumes.CostumeGooigiCap;
import hugman.mod.objects.costumes.CostumeHeadBase;
import hugman.mod.objects.costumes.CostumeMayroCap;
import hugman.mod.objects.costumes.CostumeSimpleEffect;
import hugman.mod.objects.costumes.CostumeVanishCap;
import hugman.mod.objects.costumes.CostumeWingCap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/** 
 * Init class - used to initialize costumes.
 */
public class CostumeInit
{
	public static final List<Item> COSTUMES = new ArrayList<Item>();
	
	public static final Item HEADPHONES = new CostumeHeadBase("headphones", CreativeTabs.MISC, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
	public static final Item CHRISTMAS_HAT = new CostumeHeadBase("christmas_hat", CreativeTabs.MISC, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
	public static final Item CAPPY = new CostumeCappy();
	public static final Item LUIGI_CAP = new CostumeSimpleEffect("luigi_cap", CreativeTabInit.SUPER_MARIO, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, new PotionEffect(Potion.getPotionById(8), 1, 0), new PotionEffect(Potion.getPotionById(1), 1, 0));
	public static final Item WARIO_CAP = new CostumeSimpleEffect("wario_cap", CreativeTabInit.SUPER_MARIO, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, new PotionEffect(Potion.getPotionById(2), 1, 0), new PotionEffect(Potion.getPotionById(5), 1, 0));
	public static final Item WALUIGI_CAP = new CostumeSimpleEffect("waluigi_cap", CreativeTabInit.SUPER_MARIO, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, new PotionEffect(Potion.getPotionById(8), 1, 1));
	public static final Item VANISH_CAP = new CostumeVanishCap();
	public static final Item WING_CAP = new CostumeWingCap();
	public static final Item GOOIGI_CAP = new CostumeGooigiCap();
	public static final Item PRINCESS_PEACH_CROWN = new CostumeHeadBase("princess_peach_crown", CreativeTabInit.SUPER_MARIO, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
	public static final Item PRINCESS_DAISY_CROWN = new CostumeHeadBase("princess_daisy_crown", CreativeTabInit.SUPER_MARIO, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
	public static final Item ROSALINA_CROWN = new CostumeHeadBase("rosalina_crown", CreativeTabInit.SUPER_MARIO, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
	public static final Item PINK_GOLD_PEACH_CROWN = new CostumeHeadBase("pink_gold_peach_crown", CreativeTabInit.SUPER_MARIO, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
	public static final Item SUPER_CROWN = new CostumeHeadBase("super_crown", CreativeTabInit.SUPER_MARIO, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
	public static final Item BROQUE_MONSIEUR_HEAD = new CostumeHeadBase("broque_monsieur_head", CreativeTabInit.SUPER_MARIO, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
	public static final Item BANDANA = new CostumeHeadBase("bandana", CreativeTabInit.MUBBLE_MISC, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
	public static final Item TOP_HAT = new CostumeHeadBase("top_hat", CreativeTabInit.MUBBLE_MISC, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
	public static final Item RALSEI_HAT = new CostumeHeadBase("ralsei_hat", CreativeTabInit.UNDERTALE_DELTARUNE, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
	public static final Item BALDI_HEAD = new CostumeHeadBase("baldi_head", CreativeTabInit.MUBBLE_MISC, SoundEvents.E_PARROT_IM_VEX);
	public static final Item MAYRO_CAP = new CostumeMayroCap();
	public static final Item NOTEBLOCK_HEAD = new CostumeHeadBase("noteblock_head", CreativeTabInit.YOUTUBE, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
	public static final Item SNORLAX_HAT = new CostumeHeadBase("snorlax_hat", CreativeTabInit.MUBBLE_MISC, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
}
