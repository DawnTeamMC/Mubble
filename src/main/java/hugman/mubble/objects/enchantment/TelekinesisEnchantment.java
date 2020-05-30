package hugman.mubble.objects.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class TelekinesisEnchantment extends Enchantment
{
	public TelekinesisEnchantment(Enchantment.Rarity rarity, EquipmentSlot... slots)
	{
		super(rarity, EnchantmentTarget.WEAPON, slots);
	}

	@Override
	public int getMaxLevel()
	{
		return 2;
	}

	@Override
	public int getMinPower(int enchantmentLevel)
	{
		return 25 + enchantmentLevel * 5;
	}

	@Override
	public int getMaxPower(int enchantmentLevel)
	{
		return this.getMinPower(enchantmentLevel) + 15;
	}
}
