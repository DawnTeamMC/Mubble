package com.hugman.mubble.init;

import com.hugman.mubble.Mubble;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class MubbleItemGroups {
	public static final ItemGroup COSTUMES_GROUP = FabricItemGroupBuilder.build(Mubble.MOD_DATA.id("costumes"),
			() -> new ItemStack(MubbleCostumePack.CAPPY)
	);
	public static final ItemGroup INSTRUMENTS_GROUP = FabricItemGroupBuilder.build(Mubble.MOD_DATA.id("instruments"),
			() -> new ItemStack(MubbleItemPack.JINGLE_BELLS)
	);
}
