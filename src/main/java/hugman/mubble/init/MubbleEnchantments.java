package hugman.mubble.init;

import hugman.mubble.Mubble;
import hugman.mubble.objects.enchantment.TelekinesisEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MubbleEnchantments
{
	public static final Enchantment TELEKINESIS = register("telekinesis", new TelekinesisEnchantment(Enchantment.Weight.VERY_RARE, EquipmentSlot.MAINHAND));
	
	private static Enchantment register(String name, Enchantment enchantment)
	{
		return Registry.register(Registry.ENCHANTMENT, new Identifier(Mubble.MOD_ID, name), enchantment);
	}
}
