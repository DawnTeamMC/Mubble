package fr.hugman.mubble.data.provider;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.data.PowerUpItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSources;
import net.minecraft.client.renderer.texture.atlas.sources.PalettedPermutations;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class MubbleAtlasProvider extends FabricCodecDataProvider<List<SpriteSource>> {
	private static final Map<String, Identifier> FULL_MAP = makeFullPalette();

    public MubbleAtlasProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture, PackOutput.Target.RESOURCE_PACK, "atlases", SpriteSources.FILE_CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, List<SpriteSource>> provider, HolderLookup.Provider registries) {
        provider.accept(Identifier.withDefaultNamespace("blocks"), List.of(
                new PalettedPermutations(
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
        provider.accept(Identifier.withDefaultNamespace("gui"), powerUpsAtlasSources());
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

    private static List<SpriteSource> powerUpsAtlasSources() {
        var list = new ArrayList<SpriteSource>();
        PowerUpItems.forEach(entry -> list.add(new SingleFile(
            Identifier.fromNamespaceAndPath(entry.item().identifier().getNamespace(), "item/" + entry.item().identifier().getPath()),
            Optional.of(entry.powerUp().identifier().withPrefix("power_up/")))
        ));
        return list;
    }

    @Override
    public String getName() {
        return "Atlases";
    }
}