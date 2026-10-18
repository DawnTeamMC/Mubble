package fr.hugman.mubble.zelda.data.provider;

import fr.hugman.mubble.zelda.world.item.ZeldaItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

public class ZeldaModelProvider extends FabricModelProvider {
    public ZeldaModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators gen) {
    }

    @Override
    public void generateItemModels(ItemModelGenerators gen) {
        gen.generateFlatItem(ZeldaItems.MUSHROOM_SPORES_BOTTLE, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ZeldaItems.LOFTWING_WHISTLE, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ZeldaItems.LOFTWING_BELT, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ZeldaItems.LEATHER_LOFTWING_HELMET, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ZeldaItems.GOLDEN_LOFTWING_HELMET, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ZeldaItems.CHAINMAIL_LOFTWING_HELMET, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ZeldaItems.IRON_LOFTWING_HELMET, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ZeldaItems.DIAMOND_LOFTWING_HELMET, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(ZeldaItems.NETHERITE_LOFTWING_HELMET, ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(ZeldaItems.LOFTWING_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
    }
}
