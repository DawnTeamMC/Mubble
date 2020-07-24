package hugman.mubble.util.entry;

import hugman.mubble.Mubble;
import hugman.mubble.util.DataWriter;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemEntry extends Entry<Item> {
	protected final Item baseItem;
	protected final int cookTime;

	private ItemEntry(String name, Item baseItem, int cookTime) {
		super(name);
		this.baseItem = baseItem;
		this.cookTime = cookTime;
	}

	@Override
	protected Item register() {
		value = Registry.register(Registry.ITEM, Mubble.id(name), baseItem);
		if(cookTime != 0) FuelRegistry.INSTANCE.add(value, cookTime);
		DataWriter.entryNamesData.items.add(Mubble.id(name).toString());
		DataWriter.entryCountsData.items++;
		DataWriter.save();
		return value;
	}

	public static class Builder {
		protected final String name;
		protected final Item baseItem;
		protected int cookTime;

		/**
		 * Creates a simple item with no cook time.
		 * @param name The name of the item.
		 * @param baseItem The item itself.
		 */
		public Builder(String name, Item baseItem) {
			this.name = name;
			this.baseItem = baseItem;
			this.cookTime = 0;
		}

		public Builder setCookTime(int cookTime) {
			this.cookTime = cookTime;
			return this;
		}

		/**
		 * Builds the entry and registers the item with all its settings.
		 */
		public Item build() {
			return new ItemEntry(this.name, this.baseItem, this.cookTime).register();
		}
	}
}
