package hugman.mubble.objects.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class TelekinesisEnchantment extends Enchantment
{
	public TelekinesisEnchantment(Rarity rarityIn, EquipmentSlotType... slots)
	{
		super(rarityIn, EnchantmentType.WEAPON, slots);
	}
	
	@Override
	public int getMaxLevel()
	{
		return 2;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel)
	{
		return 25 + enchantmentLevel * 5;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel)
	{
		return this.getMinEnchantability(enchantmentLevel) + 15;
	}
}
