package hugman.mubble.util;

import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.TridentItem;

public class MubbleEnchantmentType
{
	public static final EnchantmentType LOOTING = EnchantmentType.create("LOOTING", p -> (
		   p instanceof ToolItem
		|| p instanceof BowItem
		|| p instanceof CrossbowItem
		|| p instanceof TridentItem));
}