package com.hugman.mubble.init;

import com.hugman.dawn.api.creator.ItemGroupCreator;
import com.hugman.mubble.Mubble;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class MubbleTabs extends MubblePack {
	public static final ItemGroup COSTUMES = register(new ItemGroupCreator.Builder("costumes", MubbleCostumes.CAPPY));
	public static final ItemGroup INSTRUMENTS = register(new ItemGroupCreator.Builder("instruments", MubbleItems.JINGLE_BELLS));
}