package hugman.mubble.init;

import hugman.mubble.Mubble;
import hugman.mubble.object.effect.HiccupEffect;
import hugman.mubble.object.effect.SimpleEffect;
import hugman.mubble.util.DataWriter;
import hugman.mubble.util.MoreWordUtils;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.ArrayList;
import java.util.List;

public class MubbleStatusEffects {
	private static StatusEffect register(String name, StatusEffect statusEffect) {
		DataWriter.entryNamesData.status_effects.add(Mubble.id(name).toString());
		DataWriter.entryCountsData.status_effects++;
		DataWriter.save();
		return Registry.register(Registry.STATUS_EFFECT, new Identifier(Mubble.MOD_ID, name), statusEffect);
	}

	public static final StatusEffect HEAVINESS = register("heaviness", new SimpleEffect(StatusEffectType.HARMFUL, 9198906));
	//public static final StatusEffect SNEEZING = new SimpleEffect("sneezing", true, 9753716);
	public static final StatusEffect HICCUP = register("hiccup", new HiccupEffect(StatusEffectType.NEUTRAL, 9198906));
}