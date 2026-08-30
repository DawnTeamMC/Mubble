package fr.hugman.mubble.testmod;

import com.google.common.reflect.Reflection;
import fr.hugman.mubble.testmod.world.item.TestModCreativeModeTabs;
import fr.hugman.mubble.testmod.world.item.TestModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A sandbox that only ever exists in the development environment: it is never shipped, nested in the
 * release jar nor published.
 * <p>
 * Everything it adds is there to be played with in a dev run
 * ({@code ./gradlew :mubble-testmod:runClient}), so that reaching a state worth looking at does not
 * take a build of the mod or a pile of commands. Whatever a data pack can define is defined there;
 * only the items and the creative tab need code. It is not where the automated tests get their
 * content: {@code mubble-test} brings its own, see {@code CONTRIBUTING.md}.
 */
public class MubbleTestMod implements ModInitializer {
    public static final String MOD_ID = "mubble-testmod";

    @Override
    public void onInitialize() {
        Reflection.initialize(TestModItems.class);
        TestModCreativeModeTabs.appendItemGroups();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
