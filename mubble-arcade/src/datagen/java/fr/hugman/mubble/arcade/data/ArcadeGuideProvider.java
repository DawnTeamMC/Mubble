package fr.hugman.mubble.arcade.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.arcade.registries.ArcadeBuiltInRegistries;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

/**
 * The rules of the button guide of Controlify while the arcade layer drives: for every button and
 * every move, the name of the move next to the glyph of the button, shown when the button would start
 * that move. The facts they read are contributed by the client, see {@code ArcadeGuide}.
 * <p>
 * The file adds to Controlify's own rules rather than replacing them, so nothing changes without the
 * layer, nor without Controlify, which is the only one to read it.
 */
public class ArcadeGuideProvider implements DataProvider {
    /** The buttons of the guide: the name their facts use, their binding and their side of the screen. */
    private static final String[][] BUTTONS = {
            {"jump", "arcade_jump", "left"},
            {"crouch", "arcade_crouch", "left"},
            {"spin", "arcade_spin", "left"},
            {"action", "arcade_attack", "right"},
    };

    private final PackOutput.PathProvider paths;

    public ArcadeGuideProvider(FabricPackOutput output) {
        this.paths = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "contextual/guide");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        var rules = new JsonArray();
        var moves = ArcadeBuiltInRegistries.ARCADE_MOVE.keySet().stream().sorted(Comparator.comparing(Identifier::toString)).toList();
        for (var button : BUTTONS) {
            for (var move : moves) {
                rules.add(rule(Mubble.id(button[1]), button[2],
                        "controlify:verbosity_minimal", Mubble.id("arcade/" + button[0] + "/" + move.getPath()).toString(),
                        "arcade_move." + move.getNamespace() + "." + move.getPath()));
            }
        }
        rules.add(rule(Mubble.id("arcade_recenter"), "right",
                "controlify:verbosity_reduced", Mubble.id("arcade_orbiting").toString(),
                "key." + Mubble.MOD_ID + ".arcade_recenter"));
        var root = new JsonObject();
        root.add("rules", rules);
        return DataProvider.saveStable(cache, root, this.paths.json(Identifier.fromNamespaceAndPath("controlify", "in_game")));
    }

    private static JsonObject rule(Identifier binding, String where, String verbosity, String fact, String translation) {
        var rule = new JsonObject();
        rule.addProperty("for", binding.toString());
        rule.addProperty("where", where);
        var predicate = new JsonObject();
        var allOf = new JsonArray();
        allOf.add(verbosity);
        allOf.add(fact);
        predicate.add("all_of", allOf);
        rule.add("if", predicate);
        var text = new JsonObject();
        text.addProperty("translate", translation);
        rule.add("then", text);
        return rule;
    }

    @Override
    public String getName() {
        return "Arcade Movement/Controller Guide";
    }
}
