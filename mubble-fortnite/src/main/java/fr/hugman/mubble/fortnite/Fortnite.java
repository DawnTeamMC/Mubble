package fr.hugman.mubble.fortnite;

import com.google.common.reflect.Reflection;
import fr.hugman.mubble.fortnite.world.entity.FortniteEntityTypes;
import fr.hugman.mubble.fortnite.world.item.FortniteCreativeModeTabs;
import fr.hugman.mubble.fortnite.world.item.FortniteItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.DispenserBlock;

public class Fortnite implements ModInitializer {
    public static final String MOD_ID = "fortnite";

    @Override
    public void onInitialize() {
        Reflection.initialize(FortniteEntityTypes.class);
        Reflection.initialize(FortniteItems.class);

        FortniteCreativeModeTabs.appendItemGroups();

        DispenserBlock.registerProjectileBehavior(FortniteItems.IMPULSE_GRENADE);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
