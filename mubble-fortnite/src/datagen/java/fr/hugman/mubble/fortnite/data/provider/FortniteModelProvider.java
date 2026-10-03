package fr.hugman.mubble.fortnite.data.provider;

import fr.hugman.mubble.fortnite.world.item.FortniteItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

public class FortniteModelProvider extends FabricModelProvider {
    public FortniteModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators gen) {
    }

    @Override
    public void generateItemModels(ItemModelGenerators gen) {
        gen.generateFlatItem(FortniteItems.IMPULSE_GRENADE, ModelTemplates.FLAT_ITEM);
    }
}
