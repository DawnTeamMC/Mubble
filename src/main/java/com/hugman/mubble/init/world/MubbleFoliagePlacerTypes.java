package com.hugman.mubble.init.world;

import com.hugman.mubble.Mubble;
import com.hugman.mubble.object.world.gen.foliage.CatFoliagePlacer;
import com.mojang.serialization.Codec;
import com.terraformersmc.terraform.tree.placer.PlacerTypes;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

public class MubbleFoliagePlacerTypes {
	public static final FoliagePlacerType<CatFoliagePlacer> CAT_FOLIAGE_PLACER = register("cat_foliage_placer", CatFoliagePlacer.CODEC);

	public static void init() {

	}

	private static <P extends FoliagePlacer> FoliagePlacerType<P> register(String name, Codec<P> codec) {
		return PlacerTypes.registerFoliagePlacer(Mubble.MOD_DATA.id(name), codec);
	}
}
