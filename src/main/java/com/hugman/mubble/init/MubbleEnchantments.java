package com.hugman.mubble.init;

import com.hugman.dawn.api.creator.EnchantmentCreator;
import com.hugman.mubble.object.enchantment.IgnoranceCurseEnchantment;
import com.hugman.mubble.object.enchantment.TelekinesisEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;

public class MubbleEnchantments extends MubblePack {
	public static final Enchantment TELEKINESIS = register(new EnchantmentCreator.Builder("telekinesis", new TelekinesisEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND)));
	public static final Enchantment IGNORANCE_CURSE = register(new EnchantmentCreator.Builder("ignorance_curse", new IgnoranceCurseEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.values())));
}
