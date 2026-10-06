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

    private static void settings(java.util.Map<String, String[]> t, String name, String english, String french, String englishTooltip, String frenchTooltip) {
        t.put("arcade." + M + ".settings." + name, pair(english, french));
        t.put("arcade." + M + ".settings." + name + ".tooltip", pair(englishTooltip, frenchTooltip));
    }

    /** {@code key -> [english, french]}. */
    public static Map<String, String[]> all() {
        var t = new LinkedHashMap<String, String[]>();

        // keys
        t.put("key.category." + M + ".arcade", pair("Arcade Movement", "Déplacements arcade"));
        t.put("key." + M + ".arcade_forward", pair("Move Forward", "Avancer"));
        t.put("key." + M + ".arcade_backward", pair("Move Backward", "Reculer"));
        t.put("key." + M + ".arcade_left", pair("Move Left", "Aller à gauche"));
        t.put("key." + M + ".arcade_right", pair("Move Right", "Aller à droite"));
        t.put("key." + M + ".arcade_jump", pair("Jump", "Sauter"));
        t.put("key." + M + ".arcade_jump_alt", pair("Jump (second key)", "Sauter (seconde touche)"));
        t.put("key." + M + ".arcade_crouch", pair("Crouch, Ground Pound", "S'accroupir, charge au sol"));
        t.put("key." + M + ".arcade_sprint", pair("Sprint", "Sprinter"));
        t.put("key." + M + ".arcade_attack", pair("Attack · Roll, Dive in a combo", "Attaquer · roulade, plongeon en combo"));
        t.put("key." + M + ".arcade_use", pair("Use Item · Roll, Dive in a combo", "Utiliser l'objet · roulade, plongeon en combo"));
        t.put("key." + M + ".arcade_spin", pair("Spin", "Tourbillon"));
        t.put("key." + M + ".arcade_recenter", pair("Recenter Camera", "Recentrer la caméra"));
        t.put("key." + M + ".arcade_debug_hud", pair("Arcade Debug HUD", "Interface de débogage arcade"));
        // the controller bindings bringing back what the arcade layout moves aside, see ArcadeControllerLayout
        t.put("key." + M + ".arcade_power_up", pair("Trigger Power-Up (arcade layout)", "Déclencher le power-up (disposition arcade)"));
        t.put("key." + M + ".arcade_inventory", pair("Inventory (arcade layout)", "Inventaire (disposition arcade)"));
        t.put("key." + M + ".arcade_auto_camera", pair("Toggle Camera Follow (controller)", "Basculer le suivi de la caméra (manette)"));
        t.put("key." + M + ".arcade_auto_camera.description", pair("Whether the camera swings round behind the player as they move, playing on a controller.",
                "Si la caméra revient derrière le joueur à mesure qu'il se déplace, en jouant à la manette."));

        // the settings, in the settings of Mubble
        settings(t, "orbit_camera", "Orbit Camera", "Caméra orbitale",
                "In the third person view from the back, the camera turns freely around the player, and the player moves relative to it.",
                "En vue à la troisième personne de dos, la caméra tourne librement autour du joueur, qui se déplace par rapport à elle.");
        settings(t, "camera_distance", "Camera Distance", "Distance de la caméra",
                "How far behind the player the orbit camera stays.", "À quelle distance derrière le joueur se tient la caméra orbitale.");
        settings(t, "auto_camera_keyboard", "Camera Follows (Keyboard)", "Suivi de la caméra (clavier)",
                "Playing on the keyboard and mouse, the orbit camera swings round behind the player as they move, never while they stand still.",
                "Au clavier et à la souris, la caméra orbitale revient derrière le joueur à mesure qu'il se déplace, jamais quand il est immobile.");
        settings(t, "auto_camera_controller", "Camera Follows (Controller)", "Suivi de la caméra (manette)",
                "Playing on a controller, the orbit camera swings round behind the player as they move, never while they stand still.",
                "À la manette, la caméra orbitale revient derrière le joueur à mesure qu'il se déplace, jamais quand il est immobile.");
        settings(t, "auto_camera_speed", "Camera Follow Speed", "Vitesse de suivi de la caméra",
                "How fast the camera swings round when following the player at full speed.", "La vitesse à laquelle la caméra revient derrière le joueur lancé à pleine vitesse.");
        settings(t, "follow_lag", "Camera Lag", "Retard de la caméra",
                "How long the camera takes to catch up with the player. None follows rigidly.", "Le temps que met la caméra à rattraper le joueur. Aucun : elle le suit de près.");
        settings(t, "orbit_sensitivity", "Camera Sensitivity", "Sensibilité de la caméra",
                "How fast the mouse and the right stick turn the orbit camera.", "La vitesse à laquelle la souris et le stick droit font tourner la caméra orbitale.");
        settings(t, "fov_kick", "Speed Field of View", "Champ de vision à pleine vitesse",
                "How much wider the view gets at full speed.", "De combien le champ de vision s'élargit à pleine vitesse.");
        settings(t, "respect_profile_hints", "Profile Camera Hints", "Indications des profils",
                "Let arcade profiles scale the camera distance and the speed field of view.",
                "Laisser les profils arcade ajuster la distance de la caméra et le champ de vision à pleine vitesse.");
        settings(t, "debug_hud", "Debug HUD on Start", "Interface de débogage au démarrage",
                "Show the arcade debug HUD when the game starts.", "Afficher l'interface de débogage arcade au démarrage du jeu.");
        t.put("arcade." + M + ".settings.unit.degrees_per_second", pair("%s°/s", "%s°/s"));
        t.put("arcade." + M + ".settings.unit.blocks", pair("%s blocks", "%s blocs"));
        t.put("arcade." + M + ".settings.unit.milliseconds", pair("%s ms", "%s ms"));
        t.put("arcade." + M + ".settings.unit.percent", pair("%s%%", "%s %%"));
        t.put("arcade." + M + ".settings.auto_camera_controller.on", pair("Camera follows the player", "La caméra suit le joueur"));
        t.put("arcade." + M + ".settings.auto_camera_controller.off", pair("Camera stays put", "La caméra reste en place"));

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
        t.put(move("swim"), pair("Swim", "Nage"));
        t.put(move("swim_dash"), pair("Swim Dash", "Élan aquatique"));
        t.put(move("climb"), pair("Climb", "Escalade"));
        // the button guide of a controller
        t.put("arcade." + M + ".guide.hold_on", pair("Hold On", "Se tenir"));

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
