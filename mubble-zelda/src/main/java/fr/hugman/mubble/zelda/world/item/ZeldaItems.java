package fr.hugman.mubble.zelda.world.item;

import fr.hugman.mubble.world.item.helper.ItemFactory;
import fr.hugman.mubble.zelda.core.component.ZeldaDataComponents;
import fr.hugman.mubble.zelda.references.ZeldaItemIds;
import fr.hugman.mubble.zelda.world.entity.ZeldaEntityTypes;
import fr.hugman.mubble.zelda.world.item.component.LoftwingHelmetComponent;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.equipment.Equippable;

import java.util.function.Function;

public class ZeldaItems {
	/** Four leads knotted into a harness: without one, a loftwing carries nobody. */
	public static final Item LOFTWING_BELT = register(ZeldaItemIds.LOFTWING_BELT, new Item.Properties()
			.stacksTo(1)
			.component(DataComponents.EQUIPPABLE, loftwingGear(EquipmentSlot.SADDLE).setEquipSound(SoundEvents.HORSE_SADDLE).build()));

	// Heavier is safer and slower. The numbers live in the component, not in the code that reads them.
	public static final Item LEATHER_LOFTWING_HELMET = registerHelmet(ZeldaItemIds.LEATHER_LOFTWING_HELMET, 2.0F, 1.0F);
	public static final Item GOLDEN_LOFTWING_HELMET = registerHelmet(ZeldaItemIds.GOLDEN_LOFTWING_HELMET, 4.0F, 1.5F);
	/** Never crafted: a loftwing that turned you down drops one on you now and then. */
	public static final Item CHAINMAIL_LOFTWING_HELMET = registerHelmet(ZeldaItemIds.CHAINMAIL_LOFTWING_HELMET, 6.0F, 2.0F);
	public static final Item IRON_LOFTWING_HELMET = registerHelmet(ZeldaItemIds.IRON_LOFTWING_HELMET, 8.0F, 3.0F);
	public static final Item DIAMOND_LOFTWING_HELMET = registerHelmet(ZeldaItemIds.DIAMOND_LOFTWING_HELMET, 12.0F, 4.0F);
	/** Nothing gets through it, and nothing turns slower either. */
	public static final Item NETHERITE_LOFTWING_HELMET = registerHelmet(ZeldaItemIds.NETHERITE_LOFTWING_HELMET, Float.MAX_VALUE, 5.0F);

	public static final SpawnEggItem LOFTWING_SPAWN_EGG = register(ZeldaItemIds.LOFTWING_SPAWN_EGG, ItemFactory.spawnEgg(ZeldaEntityTypes.LOFTWING));

	/**
	 * Gear only a loftwing can wear, put on by hand or by a dispenser and taken back off the same
	 * way. No asset id yet: the helmet will only show on the bird once it has a model to hang off.
	 */
	private static Equippable.Builder loftwingGear(EquipmentSlot slot) {
		return Equippable.builder(slot)
				.setAllowedEntities(ZeldaEntityTypes.LOFTWING)
				.setEquipOnInteract(true)
				.setSwappable(true);
	}

	/** Helmets never wear out: the issue asks for gear you fit once and forget. */
	private static Item registerHelmet(ResourceKey<Item> key, float crashProtection, float turnSeconds) {
		return register(key, new Item.Properties()
				.stacksTo(1)
				.component(DataComponents.EQUIPPABLE, loftwingGear(EquipmentSlot.HEAD).setEquipSound(SoundEvents.ARMOR_EQUIP_IRON).build())
				.component(ZeldaDataComponents.LOFTWING_HELMET, new LoftwingHelmetComponent(crashProtection, turnSeconds)));
	}

	private static <O extends Item> O register(ResourceKey<Item> key, Function<Item.Properties, O> factory, Item.Properties settings) {
		return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(settings.setId(key)));
	}

	private static <O extends Item> O register(ResourceKey<Item> key, Function<Item.Properties, O> factory) {
		return register(key, factory, new Item.Properties());
	}

	private static Item register(ResourceKey<Item> key, Item.Properties settings) {
		return register(key, Item::new, settings);
	}
}
