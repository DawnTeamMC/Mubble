package fr.hugman.mubble.arcade.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.arcade.registries.ArcadeBuiltInRegistries;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

/**
 * What Controlify reads from the resources of the arcade layer, without which nothing changes.
 * <p>
 * The default bindings of the controller, laid out as in Super Mario Odyssey: jump on A and B, Cappy's
 * buttons Y and X to attack and use (a roll when crouching, a dive out of a ground pound), crouch and
 * ground pound on ZR, the power-up on ZL, recentering the camera on L. What sat there moves aside:
 * the radial menu to R, the inventory, drop and hotbar to the D-pad. Swapping hands, picking a block
 * and the chat stay in the radial menu. Controlify layers these defaults over its own, and the player
 * can rebind every one of them.
 * <p>
 * The rules of the button guide: for every button and every move, the name of the move next to the
 * glyph of the button, shown when the button would start that move. The facts they read are
 * contributed by the client, see {@code ArcadeGuide}. They come before Controlify's own rules, and
 * win the buttons they match.
 */
public class ArcadeControlifyProvider implements DataProvider {
    /** The buttons of the guide: the name their facts use, their binding and their side of the screen, Controlify's own. */
    private static final String[][] BUTTONS = {
            {"jump", "controlify:jump", "left"},
            {"crouch", "controlify:sneak", "left"},
            {"spin", "mubble:arcade_spin", "left"},
            {"action", "controlify:attack", "right"},
            {"action", "controlify:use", "right"},
    };

    private final PackOutput.PathProvider guides;
    private final PackOutput.PathProvider defaults;

    public ArcadeControlifyProvider(FabricPackOutput output) {
        this.guides = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "contextual/guide");
        this.defaults = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "controllers/default_bind");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return CompletableFuture.allOf(
                DataProvider.saveStable(cache, guide(), this.guides.json(Identifier.fromNamespaceAndPath("controlify", "in_game"))),
                DataProvider.saveStable(cache, defaults(), this.defaults.json(Identifier.fromNamespaceAndPath("controlify", "default")))
        );
    }

    private static JsonObject defaults() {
        var binds = new LinkedHashMap<String, String>();
        // the moves; vanilla jump stays on south, so that both A and B jump
        binds.put(Mubble.id("arcade_jump").toString(), "button/east");
        binds.put("controlify:sneak", "axis/right_trigger");
        binds.put(Mubble.id("trigger_power_up").toString(), "axis/left_trigger");
        binds.put(Mubble.id("arcade_recenter").toString(), "button/left_shoulder");
        // the hands, on Cappy's buttons
        binds.put("controlify:attack", "button/west");
        binds.put("controlify:use", "button/north");
        binds.put("controlify:swap_hands", null);
        // where the rest goes meanwhile
        binds.put("controlify:radial_menu", "button/right_shoulder");
        binds.put("controlify:inventory", "button/dpad_up");
        binds.put("controlify:open_chat", null);
        binds.put("controlify:prev_slot", "button/dpad_left");
        binds.put("controlify:pick_block", null);
        binds.put("controlify:next_slot", "button/dpad_right");
        var map = new JsonObject();
        binds.forEach((binding, input) -> map.add(binding, input(input)));
        var root = new JsonObject();
        root.add("defaults", map);
        return root;
    }

    private static JsonObject input(@Nullable String input) {
        var json = new JsonObject();
        if (input == null) {
            json.addProperty("type", "empty");
        } else {
            json.addProperty(input.substring(0, input.indexOf('/')), "controlify:" + input);
        }
        return json;
    }

    private static JsonObject guide() {
        var rules = new JsonArray();
        rules.add(rule("controlify:sneak", "left",
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
        rules.add(rule(Mubble.id("arcade_recenter").toString(), "left",
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
