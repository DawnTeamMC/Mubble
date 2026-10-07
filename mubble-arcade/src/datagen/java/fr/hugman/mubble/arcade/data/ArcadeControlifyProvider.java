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
 * The rules of the button guide of Controlify while the arcade layer drives: for every arcade binding
 * and every move, the name of the move next to the glyph of the button, shown when the button would
 * start that move. The facts they read are contributed by the client, see {@code ArcadeGuide}. The
 * file adds to Controlify's own rules, and only names the bindings of the arcade layout.
 */
public class ArcadeControlifyProvider implements DataProvider {
    /** The buttons of the guide: the name their facts use, their binding and their side of the screen. */
    private static final String[][] BUTTONS = {
            {"jump", "mubble:arcade_jump", "left"},
            {"crouch", "mubble:arcade_crouch", "left"},
            {"spin", "mubble:arcade_spin", "left"},
            {"action", "mubble:arcade_attack", "right"},
            {"action", "mubble:arcade_use", "right"},
    };

    private final PackOutput.PathProvider guides;

    public ArcadeControlifyProvider(FabricPackOutput output) {
        this.guides = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "contextual/guide");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return DataProvider.saveStable(cache, guide(), this.guides.json(Identifier.fromNamespaceAndPath("controlify", "in_game")));
    }

    private static JsonObject guide() {
        var rules = new JsonArray();
        rules.add(rule("mubble:arcade_crouch", "left",
                "controlify:verbosity_minimal", Mubble.id("arcade_climbing").toString(),
                "arcade.mubble.guide.hold_on"));
        var moves = ArcadeBuiltInRegistries.ARCADE_MOVE.keySet().stream().sorted(Comparator.comparing(Identifier::toString)).toList();
        for (var button : BUTTONS) {
            for (var move : moves) {
                rules.add(rule(button[1], button[2],
                        "controlify:verbosity_minimal", Mubble.id("arcade/" + button[0] + "/" + move.getPath()).toString(),
                        "arcade_move." + move.getNamespace() + "." + move.getPath()));
            }
        }
        // the rest of the time, attack and use are for the hands, as Controlify words it
        rules.add(rule("mubble:arcade_attack", "right",
                "controlify:looking_at_entity", Mubble.id("arcade").toString(),
                "controlify.guide.ingame.attack"));
        rules.add(rule("mubble:arcade_use", "right",
                "controlify:has_item_in_either_hand", Mubble.id("arcade").toString(),
                "controlify.guide.ingame.use"));
        rules.add(rule("mubble:arcade_power_up", "right",
                "controlify:verbosity_reduced", Mubble.id("arcade_power_up").toString(),
                "key." + Mubble.MOD_ID + ".trigger_power_up"));
        rules.add(rule("mubble:arcade_inventory", "left",
                "controlify:verbosity_reduced", Mubble.id("arcade").toString(),
                "controlify.guide.ingame.inventory"));
        rules.add(rule("mubble:arcade_recenter", "left",
                "controlify:verbosity_reduced", Mubble.id("arcade_orbiting").toString(),
                "key." + Mubble.MOD_ID + ".arcade_recenter"));
        var root = new JsonObject();
        root.add("rules", rules);
        return root;
    }

    private static JsonObject rule(String binding, String where, String verbosity, String fact, String translation) {
        var rule = new JsonObject();
        rule.addProperty("for", binding);
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
        return "Arcade Movement/Controlify";
    }
}
