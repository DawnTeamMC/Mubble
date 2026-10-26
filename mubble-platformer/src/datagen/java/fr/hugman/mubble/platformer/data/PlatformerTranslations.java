package fr.hugman.mubble.platformer.data;

import fr.hugman.mubble.platformer.registries.PlatformerBuiltInRegistries;
import fr.hugman.mubble.Mubble;
import java.util.LinkedHashMap;
import java.util.Map;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

/**
 * Every text of the platformer movement layer, in English and in French, side by side so that neither
 * language can miss a key the other has.
 */
public final class PlatformerTranslations {
    private static final String M = Mubble.MOD_ID;

    private PlatformerTranslations() {
    }

    private static void settings(java.util.Map<String, String[]> t, String name, String english, String french, String englishTooltip, String frenchTooltip) {
        t.put("platformer." + M + ".settings." + name, pair(english, french));
        t.put("platformer." + M + ".settings." + name + ".tooltip", pair(englishTooltip, frenchTooltip));
    }

    /** {@code key -> [english, french]}. */
    public static Map<String, String[]> all() {
        var t = new LinkedHashMap<String, String[]>();

        // keys
        t.put("key.category." + M + ".platformer", pair("Platformer Movement", "Déplacements plateforme"));
        t.put("key." + M + ".platformer_forward", pair("Move Forward", "Avancer"));
        t.put("key." + M + ".platformer_backward", pair("Move Backward", "Reculer"));
        t.put("key." + M + ".platformer_left", pair("Move Left", "Aller à gauche"));
        t.put("key." + M + ".platformer_right", pair("Move Right", "Aller à droite"));
        t.put("key." + M + ".platformer_jump", pair("Jump", "Sauter"));
        t.put("key." + M + ".platformer_jump_alt", pair("Jump (second key)", "Sauter (seconde touche)"));
        t.put("key." + M + ".platformer_crouch", pair("Crouch, Ground Pound", "S'accroupir, charge au sol"));
        t.put("key." + M + ".platformer_sprint", pair("Sprint", "Sprinter"));
        t.put("key." + M + ".platformer_attack", pair("Attack · Roll, Dive in a combo", "Attaquer · roulade, plongeon en combo"));
        t.put("key." + M + ".platformer_use", pair("Use Item · Roll, Dive in a combo", "Utiliser l'objet · roulade, plongeon en combo"));
        t.put("key." + M + ".platformer_spin", pair("Spin", "Tourbillon"));
        t.put("key." + M + ".platformer_recenter", pair("Recenter Camera", "Recentrer la caméra"));
        t.put("key." + M + ".platformer_debug_hud", pair("Platformer Debug HUD", "Interface de débogage plateforme"));
        // the controller bindings bringing back what the platformer layout moves aside, see PlatformerControllerLayout
        t.put("key." + M + ".platformer_power_up", pair("Trigger Power-Up (platformer layout)", "Déclencher le power-up (disposition plateforme)"));
        t.put("key." + M + ".platformer_inventory", pair("Inventory (platformer layout)", "Inventaire (disposition plateforme)"));
        t.put("key." + M + ".platformer_auto_camera", pair("Toggle Camera Follow (controller)", "Basculer le suivi de la caméra (manette)"));
        t.put("key." + M + ".platformer_auto_camera.description", pair("Whether the camera swings round behind the player as they move, playing on a controller.",
                "Si la caméra revient derrière le joueur à mesure qu'il se déplace, en jouant à la manette."));

        // the settings, in the settings of Mubble
        settings(t, "orbit_camera", "Orbit Camera", "Caméra orbitale",
                "In the third person view from the back, the camera turns freely around the player, and the player moves relative to it.",
                "En vue à la troisième personne de dos, la caméra tourne librement autour du joueur, qui se déplace par rapport à elle.");
        settings(t, "camera_distance_scale", "Camera Distance", "Distance de la caméra",
                "How far behind the player the orbit camera stays, as a share of the distance of the usual third person view: 4 blocks, unless something changes it.",
                "À quelle distance derrière le joueur se tient la caméra orbitale, par rapport à celle de la vue à la troisième personne habituelle : 4 blocs, sauf si quelque chose la modifie.");
        settings(t, "auto_camera_keyboard", "Camera Follows (Keyboard)", "Suivi de la caméra (clavier)",
                "Playing on the keyboard and mouse, the orbit camera slowly swings round behind the player as they move on, never while they stand still or just after the camera was turned.",
                "Au clavier et à la souris, la caméra orbitale revient doucement derrière le joueur à mesure qu'il avance, jamais quand il est immobile ou juste après qu'il a tourné la caméra.");
        settings(t, "auto_camera_controller", "Camera Follows (Controller)", "Suivi de la caméra (manette)",
                "Playing on a controller, the orbit camera slowly swings round behind the player as they move on, never while they stand still or just after the camera was turned.",
                "À la manette, la caméra orbitale revient doucement derrière le joueur à mesure qu'il avance, jamais quand il est immobile ou juste après qu'il a tourné la caméra.");
        settings(t, "auto_camera_speed", "Camera Follow Speed", "Vitesse de suivi de la caméra",
                "How fast the camera swings round at its quickest, when the player rolls or dives sideways. Walking, it hardly moves, unless the player goes a long way.",
                "La vitesse à laquelle la caméra revient derrière le joueur au plus vite, quand il roule ou plonge de côté. En marchant, elle bouge à peine, sauf si le joueur va loin.");
        settings(t, "camera_margin", "Camera Margin", "Marge de la caméra",
                "How much room the camera leaves around the player before following them: a step aside or a jump leaves it still, moving on brings the player back to the middle. 0% keeps the player in the middle.",
                "La marge que laisse la caméra autour du joueur avant de le suivre : un pas de côté ou un saut la laissent immobile, et c'est en avançant que le joueur revient au centre. 0 % garde le joueur au centre.");
        settings(t, "orbit_sensitivity", "Camera Sensitivity", "Sensibilité de la caméra",
                "How fast the mouse and the right stick turn the orbit camera.", "La vitesse à laquelle la souris et le stick droit font tourner la caméra orbitale.");
        settings(t, "fov_kick", "Speed Field of View", "Champ de vision à pleine vitesse",
                "How much wider the view gets at full speed.", "De combien le champ de vision s'élargit à pleine vitesse.");
        settings(t, "silhouette", "Show Player Through Walls", "Joueur visible à travers les murs",
                "When something stands between the orbit camera and the player, the player shows through it in light gray.",
                "Quand quelque chose se trouve entre la caméra orbitale et le joueur, le joueur apparaît au travers en gris clair.");
        settings(t, "respect_profile_hints", "Profile Camera Hints", "Indications des profils",
                "Let platformer profiles scale the camera distance and the speed field of view.",
                "Laisser les profils plateforme ajuster la distance de la caméra et le champ de vision à pleine vitesse.");
        settings(t, "debug_hud", "Debug HUD on Start", "Interface de débogage au démarrage",
                "Show the platformer debug HUD when the game starts.", "Afficher l'interface de débogage plateforme au démarrage du jeu.");
        t.put("platformer." + M + ".settings.unit.degrees_per_second", pair("%s°/s", "%s°/s"));
        t.put("platformer." + M + ".settings.unit.percent", pair("%s%%", "%s %%"));
        t.put("platformer." + M + ".settings.auto_camera_controller.on", pair("Camera follows the player", "La caméra suit le joueur"));
        t.put("platformer." + M + ".settings.auto_camera_controller.off", pair("Camera stays put", "La caméra reste en place"));

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
        t.put("platformer." + M + ".guide.hold_on", pair("Hold On", "Se tenir"));

        // the item
        t.put("item." + M + ".mario_boots", pair("Mario Boots", "Bottes de Mario"));
        t.put("item." + M + ".platformer_movement.tooltip", pair("Platformer movement when worn", "Déplacements plateforme une fois portées"));
        t.put("item." + M + ".platformer_movement.profile", pair("Profile: %s", "Profil : %s"));

        // attributes
        t.put(attribute("platformer_run_speed"), pair("Platformer Run Speed", "Vitesse de course plateforme"));
        t.put(attribute("platformer_jump_height"), pair("Platformer Jump Height", "Hauteur de saut plateforme"));
        t.put(attribute("platformer_roll_boost"), pair("Platformer Roll Boost", "Relance de roulade plateforme"));
        t.put(attribute("platformer_air_drag"), pair("Platformer Air Drag", "Traînée aérienne plateforme"));
        t.put(attribute("platformer_air_control"), pair("Platformer Air Control", "Contrôle aérien plateforme"));
        t.put(attribute("platformer_coyote_ticks"), pair("Platformer Coyote Time", "Temps de grâce plateforme"));
        t.put(attribute("platformer_wall_slide_speed"), pair("Platformer Wall Slide Speed", "Vitesse de glissade murale plateforme"));
        t.put(attribute("platformer_ground_pound_speed"), pair("Platformer Ground Pound Speed", "Vitesse de charge au sol plateforme"));

        // commands
        t.put(command("source.invalid"), pair("Invalid platformer source: %s", "Source plateforme invalide : %s"));
        t.put(command("source.unknown"), pair("No player has an platformer source named %s", "Aucun joueur n'a de source plateforme nommée %s"));
        t.put(command("move.invalid"), pair("%s is not an platformer move", "%s n'est pas un mouvement plateforme"));
        t.put(command("profile.invalid"), pair("%s is not an platformer profile", "%s n'est pas un profil plateforme"));
        t.put(command("unchanged"), pair("Nothing changed", "Rien n'a changé"));
        t.put(command("source.add.success"), pair("Added the platformer source %s to %s player(s)", "Source plateforme %s ajoutée à %s joueur(s)"));
        t.put(command("source.remove.success"), pair("Removed the platformer source %s from %s player(s)", "Source plateforme %s retirée de %s joueur(s)"));
        t.put(command("source.list.none"), pair("%s has no platformer source", "%s n'a aucune source plateforme"));
        t.put(command("source.list.header"), pair("%s has %s platformer source(s):", "%s a %s source(s) plateforme :"));
        t.put(command("unlock.success"), pair("Unlocked %s for %s player(s)", "%s débloqué pour %s joueur(s)"));
        t.put(command("lock.success"), pair("Locked %s for %s player(s)", "%s verrouillé pour %s joueur(s)"));
        t.put(command("profile.success"), pair("Set the platformer profile %s for %s player(s)", "Profil plateforme %s appliqué à %s joueur(s)"));
        t.put(command("inspect.header"), pair("Platformer movement of %s:", "Déplacements plateforme de %s :"));
        t.put(command("gym.success"), pair("Built the movement gym at %s, %s, %s", "Gymnase de déplacement construit en %s, %s, %s"));
        t.put(command("impulse.success"), pair("Pushed %s player(s)", "%s joueur(s) poussé(s)"));

        // the recorder, a client command
        t.put(clientCommand("not_driving"), pair("Platformer movement is not on", "Les déplacements plateforme ne sont pas actifs"));
        t.put(clientCommand("record.started"), pair("Recording platformer inputs…", "Enregistrement des entrées plateforme…"));
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
        for (var id : PlatformerBuiltInRegistries.PLATFORMER_MOVE.keySet()) {
            if (!all.containsKey(move(id.getPath()))) {
                throw new IllegalStateException("The platformer move " + id + " has no translation");
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
        return "platformer_move." + M + "." + path;
    }

    private static String attribute(String path) {
        return "attribute.name." + M + "." + path;
    }

    private static String command(String path) {
        return "commands." + M + ".platformer." + path;
    }

    private static String clientCommand(String path) {
        return "commands." + M + "_client.platformer." + path;
    }
}
