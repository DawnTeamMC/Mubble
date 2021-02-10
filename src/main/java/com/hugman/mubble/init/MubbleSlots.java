package com.hugman.mubble.init;

import com.hugman.mubble.Mubble;
import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.TrinketSlots;
import net.minecraft.util.Identifier;

public class MubbleSlots {
	public static final String HAT = "hat";

	public static void init() {
		TrinketSlots.addSlot(SlotGroups.HEAD, MubbleSlots.HAT, Mubble.MOD_DATA.id("textures/item/empty_trinket_slot_hat.png"));
		TrinketSlots.addSlot(SlotGroups.HEAD, Slots.MASK, new Identifier("trinkets", "textures/item/empty_trinket_slot_mask.png"));
		TrinketSlots.addSlot(SlotGroups.CHEST, Slots.NECKLACE, new Identifier("trinkets", "textures/item/empty_trinket_slot_necklace.png"));
	}
}
