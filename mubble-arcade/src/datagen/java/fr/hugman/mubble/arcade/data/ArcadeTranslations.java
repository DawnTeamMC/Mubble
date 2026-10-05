package fr.hugman.mubble.arcade.data;

import fr.hugman.mubble.arcade.registries.ArcadeBuiltInRegistries;
import fr.hugman.mubble.Mubble;
import java.util.LinkedHashMap;
import java.util.Map;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

/**
 * Every text of the arcade movement layer, in English and in French, side by side so that neither
 * language can miss a key the other has.
 */
public final class ArcadeTranslations {
    private static final String M = Mubble.MOD_ID;

    private ArcadeTranslations() {
    }

    /** {@code key -> [english, french]}. */
    public static Map<String, String[]> all() {
        var t = new LinkedHashMap<String, String[]>();

        // keys
        t.put("key.category." + M + ".arcade", pair("Arcade Movement", "Déplacements arcade"));
        t.put("key." + M + ".arcade_jump", pair("Arcade Jump (unbound: vanilla Jump)", "Saut arcade (non assigné : Sauter)"));
        t.put("key." + M + ".arcade_crouch", pair("Arcade Crouch (unbound: vanilla Sneak)", "Accroupissement arcade (non assigné : S'accroupir)"));
        t.put("key." + M + ".arcade_action", pair("Arcade Action (Dive, Roll)", "Action arcade (plongeon, roulade)"));
        t.put("key." + M + ".arcade_recenter", pair("Recenter Camera", "Recentrer la caméra"));
        t.put("key." + M + ".arcade_spin", pair("Spin", "Tourbillon"));
        t.put("key." + M + ".arcade_debug_hud", pair("Arcade Debug HUD", "Interface de débogage arcade"));
        // controller only: the second buttons of SMO, and where vanilla actions go while the arcade layout is on
        t.put("key." + M + ".arcade_crouch_alt", pair("Arcade Crouch (second button)", "Accroupissement arcade (second bouton)"));
        t.put("key." + M + ".arcade_action_alt", pair("Arcade Action (second button)", "Action arcade (second bouton)"));
        t.put("key." + M + ".arcade_use", pair("Use (arcade layout)", "Utiliser (disposition arcade)"));
        t.put("key." + M + ".arcade_attack", pair("Attack (arcade layout)", "Attaquer (disposition arcade)"));
        t.put("key." + M + ".arcade_inventory", pair("Inventory (arcade layout)", "Inventaire (disposition arcade)"));
        t.put("key." + M + ".arcade_swap_hands", pair("Swap Hands (arcade layout)", "Changer de main (disposition arcade)"));
        t.put("key." + M + ".arcade_prev_slot", pair("Previous Slot (arcade layout)", "Emplacement précédent (disposition arcade)"));
        t.put("key." + M + ".arcade_drop", pair("Drop (arcade layout)", "Jeter (disposition arcade)"));
        t.put("key." + M + ".arcade_pick_block", pair("Pick Block (arcade layout)", "Choisir le bloc (disposition arcade)"));
        t.put("key." + M + ".arcade_open_chat", pair("Chat (arcade layout)", "Discussion (disposition arcade)"));
        t.put("key." + M + ".arcade_radial_menu", pair("Radial Menu (arcade layout)", "Menu radial (disposition arcade)"));

        // moves
        t.put(move("walk"), pair("Walk", "Marche"));
        t.put(move("fall"), pair("Fall", "Chute"));
        t.put(move("land"), pair("Landing", "Atterrissage"));
        t.put(move("run"), pair("Run", "Course"));
        t.put(move("skid"), pair("Skid", "Dérapage"));
        t.put(move("jump"), pair("Jump", "Saut"));
        t.put(move("double_jump"), pair("Double Jump", "Double saut"));
        t.put(move("triple_jump"), pair("Triple Jump", "Triple saut"));
        t.put(move("crouch"), pair("Crouch", "Accroupissement"));
        t.put(move("ground_pound"), pair("Ground Pound", "Charge au sol"));
        t.put(move("ground_pound_land"), pair("Ground Pound Landing", "Impact de charge au sol"));
        t.put(move("ground_pound_jump"), pair("Ground Pound Jump", "Saut après charge au sol"));
        t.put(move("ledge_grab"), pair("Ledge Grab", "Accroche au rebord"));
        t.put(move("ledge_climb"), pair("Ledge Climb", "Hissage"));
        t.put(move("wall_slide"), pair("Wall Slide", "Glissade murale"));
        t.put(move("wall_jump"), pair("Wall Jump", "Saut mural"));
        t.put(move("roll"), pair("Roll", "Roulade"));
        t.put(move("roll_jump"), pair("Roll Jump", "Saut roulé"));
        t.put(move("long_jump"), pair("Long Jump", "Saut en longueur"));
        t.put(move("backflip"), pair("Backflip", "Salto arrière"));
        t.put(move("side_somersault"), pair("Side Somersault", "Salto latéral"));
        t.put(move("dive"), pair("Dive", "Plongeon"));
        t.put(move("rollout"), pair("Rollout", "Roulade de réception"));
        t.put(move("spin"), pair("Spin", "Tourbillon"));
        t.put(move("vault"), pair("Vault", "Franchissement"));
        t.put(move("slide"), pair("Slide", "Glissade"));

        // the item
        t.put("item." + M + ".mario_boots", pair("Mario Boots", "Bottes de Mario"));
        t.put("item." + M + ".arcade_movement.tooltip", pair("Arcade movement when worn", "Déplacements arcade une fois portées"));
        t.put("item." + M + ".arcade_movement.profile", pair("Profile: %s", "Profil : %s"));

        // attributes
        t.put(attribute("arcade_run_speed"), pair("Arcade Run Speed", "Vitesse de course arcade"));
        t.put(attribute("arcade_jump_height"), pair("Arcade Jump Height", "Hauteur de saut arcade"));
        t.put(attribute("arcade_roll_boost"), pair("Arcade Roll Boost", "Relance de roulade arcade"));
        t.put(attribute("arcade_air_drag"), pair("Arcade Air Drag", "Traînée aérienne arcade"));
        t.put(attribute("arcade_air_control"), pair("Arcade Air Control", "Contrôle aérien arcade"));
        t.put(attribute("arcade_coyote_ticks"), pair("Arcade Coyote Time", "Temps de grâce arcade"));
        t.put(attribute("arcade_wall_slide_speed"), pair("Arcade Wall Slide Speed", "Vitesse de glissade murale arcade"));
        t.put(attribute("arcade_ground_pound_speed"), pair("Arcade Ground Pound Speed", "Vitesse de charge au sol arcade"));

        // commands
        t.put(command("source.invalid"), pair("Invalid arcade source: %s", "Source arcade invalide : %s"));
        t.put(command("source.unknown"), pair("No player has an arcade source named %s", "Aucun joueur n'a de source arcade nommée %s"));
        t.put(command("move.invalid"), pair("%s is not an arcade move", "%s n'est pas un mouvement arcade"));
        t.put(command("profile.invalid"), pair("%s is not an arcade profile", "%s n'est pas un profil arcade"));
        t.put(command("unchanged"), pair("Nothing changed", "Rien n'a changé"));
        t.put(command("source.add.success"), pair("Added the arcade source %s to %s player(s)", "Source arcade %s ajoutée à %s joueur(s)"));
        t.put(command("source.remove.success"), pair("Removed the arcade source %s from %s player(s)", "Source arcade %s retirée de %s joueur(s)"));
        t.put(command("source.list.none"), pair("%s has no arcade source", "%s n'a aucune source arcade"));
        t.put(command("source.list.header"), pair("%s has %s arcade source(s):", "%s a %s source(s) arcade :"));
        t.put(command("unlock.success"), pair("Unlocked %s for %s player(s)", "%s débloqué pour %s joueur(s)"));
        t.put(command("lock.success"), pair("Locked %s for %s player(s)", "%s verrouillé pour %s joueur(s)"));
        t.put(command("profile.success"), pair("Set the arcade profile %s for %s player(s)", "Profil arcade %s appliqué à %s joueur(s)"));
        t.put(command("inspect.header"), pair("Arcade movement of %s:", "Déplacements arcade de %s :"));
        t.put(command("gym.success"), pair("Built the movement gym at %s, %s, %s", "Gymnase de déplacement construit en %s, %s, %s"));
        t.put(command("impulse.success"), pair("Pushed %s player(s)", "%s joueur(s) poussé(s)"));

        // the recorder, a client command
        t.put(clientCommand("not_driving"), pair("Arcade movement is not on", "Les déplacements arcade ne sont pas actifs"));
        t.put(clientCommand("record.started"), pair("Recording arcade inputs…", "Enregistrement des entrées arcade…"));
        t.put(clientCommand("record.none"), pair("Nothing is being recorded or replayed", "Aucun enregistrement ni lecture en cours"));
        t.put(clientCommand("record.saved"), pair("Saved %s steps to %s", "%s pas enregistrés dans %s"));
        t.put(clientCommand("file_error"), pair("Could not use the recording %s: %s", "Impossible d'utiliser l'enregistrement %s : %s"));
        t.put(clientCommand("replay.started"), pair("Replaying %s, %s steps", "Lecture de %s, %s pas"));
        t.put(clientCommand("replay.identical"), pair("Replay of %s: identical to the recording", "Lecture de %s : identique à l'enregistrement"));
        t.put(clientCommand("replay.diverged"), pair("Replay of %s: left the recording at step %s", "Lecture de %s : écart avec l'enregistrement au pas %s"));
        t.put(clientCommand("cancelled"), pair("Stopped recording or replaying", "Enregistrement ou lecture arrêté"));
        return t;
    }

    /** Checked against the registry, so that a new move cannot ship without a name. */
    public static void addAll(FabricLanguageProvider.TranslationBuilder builder, int language) {
        var all = all();
        for (var id : ArcadeBuiltInRegistries.ARCADE_MOVE.keySet()) {
            if (!all.containsKey(move(id.getPath()))) {
                throw new IllegalStateException("The arcade move " + id + " has no translation");
            }
        }
        all.forEach((key, texts) -> builder.add(key, texts[language]));
    }

    public static final int ENGLISH = 0;
    public static final int FRENCH = 1;

    private static String[] pair(String english, String french) {
        return new String[]{english, french};
    }

    private static String move(String path) {
        return "arcade_move." + M + "." + path;
    }

    private static String attribute(String path) {
        return "attribute.name." + M + "." + path;
    }

    private static String command(String path) {
        return "commands." + M + ".arcade." + path;
    }

    private static String clientCommand(String path) {
        return "commands." + M + "_client.arcade." + path;
    }
}
