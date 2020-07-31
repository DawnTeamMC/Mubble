package com.hugman.mubble.init;

import com.hugman.dawn.api.creator.ItemGroupCreator;
import net.minecraft.item.ItemGroup;

public class MubbleTabs extends MubblePack {
	public static final ItemGroup COSTUMES = register(new ItemGroupCreator.Builder("costumes", MubbleCostumes.CAPPY));
	public static final ItemGroup INSTRUMENTS = register(new ItemGroupCreator.Builder("instruments", MubbleItems.JINGLE_BELLS));
}