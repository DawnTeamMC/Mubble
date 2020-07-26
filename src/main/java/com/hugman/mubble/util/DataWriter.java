package com.hugman.mubble.util;

import com.google.gson.annotations.Expose;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DataWriter {
	public static EntriesData blockEntries = new EntriesData("blocks");
	public static EntriesData blockItemsEntries = new EntriesData("block_items");
	public static EntriesData itemsEntries = new EntriesData("items");
	public static EntriesData enchantmentsEntries = new EntriesData("enchantments");
	public static EntriesData entitiesEntries = new EntriesData("entities");
	public static EntriesData paintingTypesEntries = new EntriesData("painting_types");
	public static EntriesData statusEffectsEntries = new EntriesData("status_effects");
	public static EntriesData soundsEntries = new EntriesData("sounds");
	public static EntriesData featuresEntries = new EntriesData("features");
	public static EntriesData configuredFeaturesEntries = new EntriesData("configured_features");
	public static EntriesData biomesEntries = new EntriesData("biomes");
	public static EntriesData screenHandlersEntries = new EntriesData("screen_handlers");
	public static EntriesData statsEntries = new EntriesData("stats");

	public static File createFile(String directoryName, String fileName) {
		Path path = Paths.get(directoryName);
		if(!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		return new File(directoryName + "/" + fileName);
	}

	public static final class EntriesData {
		private final File file;
		@Expose
		protected int count;
		@Expose
		protected List<String> values = new ArrayList<>();

		protected EntriesData(String name) {
			file = createFile("debug/mubble/entries", name + ".json");
		}

		public void add(Identifier value) {
			values.add(value.toString());
			count++;
			DataSerialization.saveToFile(DataSerialization.PRETTY_GSON, file, EntriesData.class, this);
		}
	}
}
