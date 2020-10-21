package com.hugman.mubble.init.data;

import com.hugman.dawn.api.creator.StatCreator;
import com.hugman.mubble.init.MubblePack;
import net.minecraft.util.Identifier;

public class MubbleStats extends MubblePack {
	public static void init() {

	}

	public static final Identifier INSPECT_PLACER = register(new StatCreator.Builder("inspect_placer"));
	public static final Identifier INTERACT_WITH_TIMESWAP_TABLE = register(new StatCreator.Builder("interact_with_timeswap_table"));
	public static final Identifier OPEN_PRESENT = register(new StatCreator.Builder("open_present"));
	public static final Identifier TALKED_TO_TOAD = register(new StatCreator.Builder("talked_to_toad"));
}
