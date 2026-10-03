package fr.hugman.mubble.world.item;

import fr.hugman.mubble.core.component.MubbleDataComponents;
import fr.hugman.mubble.references.ArcadeProfileIds;
import fr.hugman.mubble.references.MubbleItemIds;
import fr.hugman.mubble.tags.ArcadeMoveTags;
import fr.hugman.mubble.world.arcade.access.AccessMode;
import fr.hugman.mubble.world.arcade.access.MoveSelector;
import fr.hugman.mubble.world.item.component.ArcadeMovementComponent;
import java.util.Map;
import java.util.Optional;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;

public class MubbleItems {
    /** The brown of the boots of Mario, which both the item and the worn boots are dyed with. */
    public static final int MARIO_BOOTS_COLOR = 0x6B3A1F;

    /**
     * Boots enabling every arcade move the player owns, under the {@code mubble:overworld} profile.
     * They protect no more than leather boots: giving up real boots is the trade-off.
     */
    public static final Item MARIO_BOOTS = register(MubbleItemIds.MARIO_BOOTS, new Item.Properties()
            .humanoidArmor(ArmorMaterials.LEATHER, ArmorType.BOOTS)
            .component(DataComponents.DYED_COLOR, new DyedItemColor(MARIO_BOOTS_COLOR))
            .component(MubbleDataComponents.ARCADE_MOVEMENT, new ArcadeMovementComponent(
                    Optional.of(ArcadeProfileIds.OVERWORLD),
                    EquipmentSlotGroup.FEET,
                    Map.of(MoveSelector.tag(ArcadeMoveTags.ALL), AccessMode.ENABLE)
            )));

    private static Item register(ResourceKey<Item> key, Item.Properties properties) {
        return Registry.register(BuiltInRegistries.ITEM, key, new Item(properties.setId(key)));
    }

    public static void registerCreativeTabs() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(output -> output.insertAfter(Items.LEATHER_BOOTS, MARIO_BOOTS));
    }
}
