package hugman.mubble.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DataWriter {
	private static File entryNamesFile;
	private static File entryCountsFile;

	public static EntryNamesData entryNamesData;
	public static EntryCountsData entryCountsData;

	public static void setup() {
		entryNamesFile = createFile("debug/mubble", "entry_names.json");
		entryCountsFile = createFile("debug/mubble", "entry_counts.json");
		load();
	}

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

	public static void load() {
		entryNamesData = new EntryNamesData();
		entryCountsData = new EntryCountsData();
	}

	public static void save() {
		DataSerialization.saveToFile(DataSerialization.PRETTY_GSON, entryNamesFile, EntryNamesData.class, entryNamesData);
		DataSerialization.saveToFile(DataSerialization.PRETTY_GSON, entryCountsFile, EntryCountsData.class, entryCountsData);
	}

	public static final class EntryNamesData {
		public List<String> blocks = new ArrayList<>();
		public List<String> block_items = new ArrayList<>();
		public List<String> items = new ArrayList<>();
		public List<String> enchantments = new ArrayList<>();
		public List<String> entities = new ArrayList<>();
		public List<String> painting_types = new ArrayList<>();
		public List<String> status_effects = new ArrayList<>();
		public List<String> sounds = new ArrayList<>();
		public List<String> features = new ArrayList<>();
		public List<String> configured_features = new ArrayList<>();
		public List<String> biomes = new ArrayList<>();
		public List<String> screen_handlers = new ArrayList<>();
		public List<String> stats = new ArrayList<>();
	}

	public static final class EntryCountsData {
		public int blocks = 0;
		public int block_items = 0;
		public int items = 0;
		public int enchantments = 0;
		public int entities = 0;
		public int painting_types = 0;
		public int status_effects = 0;
		public int sounds = 0;
		public int features = 0;
		public int configured_features = 0;
		public int biomes = 0;
		public int screen_handlers = 0;
		public int stats = 0;
	}
}
