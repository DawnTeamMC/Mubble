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
	public int getMaximumLevel()
	{
		return 2;
	}
	
	@Override
	public int getMinimumPower(int enchantmentLevel)
	{
		return 25 + enchantmentLevel * 5;
	}
	
	@Override
	public int getMaximumPower(int enchantmentLevel)
	{
		return this.getMinimumPower(enchantmentLevel) + 15;
	}
}
