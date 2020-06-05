package hugman.mubble.init;

import hugman.mubble.Mubble;
import hugman.mubble.objects.enchantment.IgnoranceCurseEnchantment;
import hugman.mubble.objects.enchantment.TelekinesisEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MubbleEnchantments
{
	public static final Enchantment TELEKINESIS = register("telekinesis", new TelekinesisEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));
	public static final Enchantment IGNORANCE_CURSE = register("ignorance_curse", new IgnoranceCurseEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.values()));

	private static Enchantment register(String name, Enchantment enchantment)
	{
		return Registry.register(Registry.ENCHANTMENT, new Identifier(Mubble.MOD_ID, name), enchantment);
	}
}
