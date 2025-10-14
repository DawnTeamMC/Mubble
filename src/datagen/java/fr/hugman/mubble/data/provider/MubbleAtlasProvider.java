package fr.hugman.mubble.data.provider;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.data.PowerUpItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.client.texture.atlas.*;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class MubbleAtlasProvider extends FabricCodecDataProvider<List<AtlasSource>> {
	private static final Map<String, Identifier> FULL_MAP = makeFullPalette();

    public MubbleAtlasProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture, DataOutput.OutputType.RESOURCE_PACK, "atlases", AtlasSourceManager.LIST_CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, List<AtlasSource>> provider, RegistryWrapper.WrapperLookup lookup) {
        provider.accept(Identifier.ofVanilla("blocks"), List.of(
                new PalettedPermutationsAtlasSource(
						List.of(
								Mubble.id("block/brick_block"),
								Mubble.id("block/bolted_block/normal"),
								Mubble.id("block/bolted_block/question"),
								Mubble.id("block/smooth_block/note"),
								Mubble.id("block/smooth_block/marimba"),
								Mubble.id("block/dotted_block/normal_1"),
								Mubble.id("block/dotted_block/exclamation_1"),
								Mubble.id("block/dotted_block/exclamation_2"),
								Mubble.id("block/smooth_block/straight"),
								Mubble.id("block/smooth_block/wavy"),
								Mubble.id("block/egg_block/side"),
								Mubble.id("block/egg_block/end")
						),
                        Mubble.id("color_palettes/mario_blocks"),
						FULL_MAP
                )
        ));
        provider.accept(Identifier.ofVanilla("gui"), powerUpsAtlasSources());
    }

	private static Map<String, Identifier> makeFullPalette() {
		var keys = List.of(
				"blue",
				"cyan",
				"green",
				"yellow",
				"orange",
				"red",
				"purple",
				"magenta",
				"brown",
				"white",
				"black",
				"iron",
				"gold",
				"diamond",
				"emerald",
				"amethyst"
		);
		var map = new HashMap<String, Identifier>();
		for (String key : keys) {
			map.put(key, Mubble.id("color_palettes/mario_blocks/" + key));
		}
		return map;
	}

    private static List<AtlasSource> powerUpsAtlasSources() {
        var list = new ArrayList<AtlasSource>();
        PowerUpItems.forEach(entry -> list.add(new SingleAtlasSource(
            Identifier.of(entry.item().getValue().getNamespace(), "item/" + entry.item().getValue().getPath()),
            Optional.of(entry.powerUp().getValue().withPrefixedPath("power_up/")))
        ));
        return list;
    }

    @Override
    public String getName() {
        return "Atlases";
    }
}