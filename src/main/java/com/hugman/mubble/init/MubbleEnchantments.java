package com.hugman.mubble.init;

import com.hugman.mubble.Mubble;
import com.hugman.mubble.object.enchantment.IgnoranceCurseEnchantment;
import com.hugman.mubble.object.enchantment.TelekinesisEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.registry.Registry;

public class MubbleEnchantments {
	public static final Enchantment TELEKINESIS = register("telekinesis", new TelekinesisEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));
	public static final Enchantment IGNORANCE_CURSE = register("ignorance_curse", new IgnoranceCurseEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.values()));

	private static Enchantment register(String name, Enchantment enchantment) {
		return Registry.register(Registry.ENCHANTMENT, Mubble.id(name), enchantment);
	}
}
