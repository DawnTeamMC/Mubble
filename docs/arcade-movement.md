# Arcade movement

An optional movement layer in the spirit of Super Mario Odyssey and Donkey Kong Bananza: jump chains, long jumps, backflips, ground pounds, wall jumps, ledge grabs, rolls, dives, spins. It stays Minecraft: same hitboxes, same collisions, same blocks, same server rules.

When nothing gives a player the layer, nothing changes for them: vanilla movement, vanilla jump, vanilla camera, vanilla animations.

## How a player gets it

Movement is **resolved per player** from three things:

- **Sources** — `{source_id, profile?, moves, priority, expiry?}`, stored on the player (`mubble:arcade_sources` attachment, kept on death) or given by worn items. A command, a minigame or a datapack adds and removes them.
- **Unlocks** — the moves a player owns (`mubble:arcade_unlocks` attachment, kept on death), given by `/mubble arcade unlock`, by the `mubble:arcade_move` quest reward, or by code (`ArcadeUnlocks.unlock`).
- **The profile** — the numbers the moves run with, chosen by the source with the highest priority that names one (ties go to the source id sorting last).

For each move, the modes of every source that names it combine as **deny > force > enable**:

| Mode | Effect |
|---|---|
| `deny` | The move is never available, whoever else allows it. Items cannot deny. |
| `force` | The move is available, owned or not. |
| `enable` | The move is available if the player owns it. |
| *(nothing)* | The move is not available. |

The layer is **on** when a profile is chosen *and* at least one move is available. Mario Boots enable every move, so a player who owns no move wearing them stays vanilla. Walking, falling and landing are not moves of their own and are always there once the layer is on. Parts of a move follow it: owning `ground_pound` also gives `ground_pound_land`, `ledge_grab` gives `ledge_climb`, `dive` gives `rollout`.

`#mubble:speed` moves need food when the profile says so (`costs.hunger_gates_speed_moves`): at 6 food or less, as for sprinting, they are unavailable.

### Suspension

The layer steps aside, and vanilla takes over, while the player is dead, sleeping, riding, a spectator, gliding with an elytra, flying, on a ladder or vine, in lava, or in water deeper than the jump threshold, and while changing dimension. It picks up again on the next tick it can.

### The item component

`mubble:arcade_movement` gives the layer while the item is in one of its slots:

```json
"mubble:arcade_movement": {
  "profile": "mubble:overworld",
  "slots": "feet",
  "moves": { "#mubble:all": "enable" }
}
```

`moves` keys are moves (`mubble:dive`) or tags (`#mubble:aerial`); values are `enable` or `force`. Each item is a source with id `<item namespace>:equipment/<slot>/<item path>` and priority 0.

### Mario Boots

`mubble:mario_boots`: leather boots (vanilla model, tinted red-brown), profile `mubble:overworld`, enable all moves. Found in the creative Combat tab after leather boots. The crafting recipe `mubble:mario_boots` ships disabled (`fabric:false` condition) until it is decided; the loot table `mubble:gameplay/mario_boots` is the hook to hand them out from a quest, a chest or a gift.

## Profiles

Profiles are a dynamic registry, `mubble:arcade_profile`, read from `data/<namespace>/mubble/arcade_profile/<name>.json` and synced to clients when they join. Two ship:

| Profile | Use | Differences |
|---|---|---|
| `mubble:overworld` | Survival with Mario Boots | run 0.34 b/t, jumps at 0.9×, safety ceiling 2 b/t, full interaction, costs food, fall damage with 3 extra safe blocks |
| `mubble:trial` | Minigames, parkour, the gym | run 0.40 b/t, full jumps, safety ceiling 4 b/t, combat only, free, no fall damage |

A data pack reload (`/reload`) re-reads every profile file and sends the new values to every client: players keep moving with the new numbers without rejoining.

```jsonc
{
  "physics": {
    "ground": { "walk_speed": 0.216, "run_speed": 0.34, "accel_ticks": 8, "decel_ticks": 4, "turn_speed": 25,
                "over_cap_drag": 0.9, "analog_run_threshold": 0.6, "stick_deadzone": 0.1, "min_grip": 0.05 },
    "air": { "drag": 0.99, "control": 1.0, "accel_ticks": 12, "turn_speed": 12, "brake": 0.03 },
    "gravity": { "base_gravity": 0.09, "fall_multiplier": 1.6, "apex_hang_multiplier": 0.5, "apex_hang_threshold": 0.08,
                 "release_cut": 0.5, "terminal_velocity": 3.92, "jump_height_multiplier": 0.9 },
    "slope": { "window_ticks": 4, "snap_down": 1.0 },
    "bounce": { "height": 4, "ticks_to_apex": 10, "ground_pound_height": 7, "min_fall_speed": 0.2 },
    "effects": { "jump_boost_height_per_level": 0.35, "slow_falling_max_fall_speed": 0.1,
                 "levitation_speed_per_level": 0.05, "levitation_response": 0.2 },
    "safety_ceiling": 2.0
  },
  "grace": { "coyote_ticks": 3, "jump_buffer_ms": 150, "action_buffer_ms": 150, "chain_window_ticks": 4,
             "ledge_magnetism": 0.3, "ledge_facing_dot": 0.5, "ledge_regrab_ticks": 8, "corner_correction": 0.35,
             "wall_jump_leniency_ticks": 4, "landing_lag_ticks": 1 },
  "interaction": "full",                    // full | combat_only | none
  "costs": { "exhaustion": true, "hunger_gates_speed_moves": true },
  "fall_damage": { "multiplier": 1.0, "safe_distance_bonus": 3.0 },
  "camera": { "distance_scale": 1.0, "fov_kick_scale": 1.0 },          // optional, hints for clients
  "validation": { "position_tolerance": 0.1, "displacement_tolerance": 0.05, "impulse_window_ticks": 40,
                  "free_impulse": 0.15, "impulse_margin": 0.3 },        // optional
  "moves": {
    "mubble:jump": {
      "exhaustion": 0.05,                   // food exhaustion each time the move starts
      "hands_busy": false,                  // optional, overrides the move's default
      "animation": "mubble:jump",           // optional, defaults to the id of the move
      "cues": { "start": { "sound": "minecraft:entity.breeze.jump", "volume": 0.35, "pitch": 1.6,
                           "particle": { "type": "minecraft:poof" }, "surface": true, "count": 5 } },
      "params": { "height": 2.2, "ticks_to_apex": 7, "variable": 1, "air_control": 1, "cancel_window_ticks": 3 }
    }
  }
}
```

Speeds are in blocks per tick, durations in ticks unless the name says `_ms`, heights in blocks. A move missing from `moves` is not supported by the profile, whatever the sources say. A parameter a move does not have, or out of its range, fails the profile instead of being ignored. Parameters left out take the defaults below.

**Physics.** Ground speed moves toward the stick target in `accel_ticks` (and stops in `decel_ticks`); speed above the run speed is kept and only eased off by `over_cap_drag` per tick, so a launch, a roll boost or a slope carries. Air keeps momentum (`drag` per tick); the stick only steers it above the run speed. Jumps are authored as a **height** and **ticks to apex**: gravity and launch speed follow from them (`g = 2h/t²`, `v₀ = 2h/t`, integrated with the trapezoidal rule so the apex lands on the height exactly). Letting go of jump early cuts the rise (`release_cut`), gravity eases near the apex while jump is held (`apex_hang_*`), and is `fall_multiplier` times stronger on the way down. Rolling or sliding down a slope (`slope.window_ticks` of history) gains speed; `snap_down` keeps the player glued to stairs going down.

**Grace.** Coyote time, a jump/action input buffer counted in milliseconds from the frame the key went down (not the tick), the window to chain a jump into the next, ledge magnetism, head-bonk corner correction, and wall jump leniency.

**Effects.** Jump Boost adds height per level, Slow Falling caps the fall, Levitation lifts. Speed and Slowness scale the run speed.

**Cues** (sound and particles) are data: per move, `start`, `tick`, `land` and `boost` cues with `sound`, `volume`, `pitch`, `particle`, `surface` (particles of the block underfoot), `shape` (`burst`, `ring`, `trail`), `count`, `count_per_speed`, `spread`, `speed`, `interval` (ticks, for `tick` cues). Other players' clients play them too.

## Moves

All are in the `mubble` namespace, registered by code (`mubble:arcade_move` registry), and tuned by profiles.

| Input | Move |
|---|---|
| Jump | Jump; jump again on landing for a double then a triple jump (needs speed) |
| Crouch in the air | Ground pound; jump right after landing for a ground pound jump |
| Keep crouch held through a ground pound, action as it lands | Ground pound roll, starting faster than a roll |
| Crouch, then jump | Backflip (standing) or long jump (running) |
| Reverse at speed, then jump | Skid, then side somersault |
| Crouch + action while running | Roll (action again to boost; it climbs steps up to a block), jump out of it for a roll jump |
| Action in the air | Dive, landing into a rollout |
| Spin key, or the stick turned all the way around | Spin, once per jump |
| Fall along a wall | Wall slide; jump to wall jump |
| Fall next to an edge facing it | Ledge grab; jump to climb, crouch to drop, stick sideways to shimmy |
| Run into a 1 block step | Vault |
| Crouch while running down a slope | Slide |

### Tags

| Tag | Moves |
|---|---|
| `#mubble:all` | every move |
| `#mubble:aerial` | jump, double/triple jump, ground pound, ground pound jump, wall jump, roll jump, long jump, backflip, side somersault, dive, spin |
| `#mubble:wall` | wall slide, wall jump |
| `#mubble:ledge` | ledge grab, vault |
| `#mubble:speed` | run, roll, roll jump, long jump, dive, slide |
| `#mubble:ground` | run, skid, crouch, roll, slide, vault |

### Parameters

| Move | Tags | Parameters (trial / overworld) |
|---|---|---|
| `mubble:backflip` | `#mubble:all` `#mubble:aerial` | `air_control` 0.5, `back_speed` 0.08, `cancel_window_ticks` 3, `height` 5.2, `max_speed` 0.3, `ticks_to_apex` 12, `variable` 0 |
| `mubble:crouch` | `#mubble:all` `#mubble:ground` | `slide_ticks` 12 |
| `mubble:dive` | `#mubble:all` `#mubble:aerial` `#mubble:speed` | `air_control` 0.3, `lift` 0.25, `speed` 0.5 |
| `mubble:double_jump` | `#mubble:all` `#mubble:aerial` | `air_control` 1, `cancel_window_ticks` 3, `height` 3.2, `min_speed` 0.2, `ticks_to_apex` 8, `variable` 1 |
| `mubble:fall` | — | — |
| `mubble:ground_pound` | `#mubble:all` `#mubble:aerial` | `drop_speed` 1.2, `hang_ticks` 6, `min_height` 1 |
| `mubble:ground_pound_jump` | `#mubble:all` `#mubble:aerial` | `air_control` 1, `cancel_window_ticks` 3, `height` 5, `ticks_to_apex` 11, `variable` 0 |
| `mubble:ground_pound_land` | — | `jump_window_ticks` 5, `roll_speed` 0.8, `roll_window_ticks` 6, `ticks` 8 |
| `mubble:jump` | `#mubble:all` `#mubble:aerial` | `air_control` 1, `cancel_window_ticks` 3, `height` 2.2, `ticks_to_apex` 7, `variable` 1 |
| `mubble:land` | — | — |
| `mubble:ledge_climb` | — | `forward` 0.7, `ticks` 8 |
| `mubble:ledge_grab` | `#mubble:all` `#mubble:ledge` | `grab_band` 0.6, `hang_depth` 0.3, `min_height` 1, `shimmy_speed` 0.1 |
| `mubble:long_jump` | `#mubble:all` `#mubble:aerial` `#mubble:speed` | `air_control` 0.6, `cancel_window_ticks` 3, `height` 1.6, `min_speed` 0.5, `speed` 0.55, `ticks_to_apex` 7, `variable` 0 |
| `mubble:roll` | `#mubble:all` `#mubble:speed` `#mubble:ground` | `boost` 0.1, `boost_cooldown_ticks` 4, `climb_height` 1, `decel` 0.004, `max_boosts` 3, `min_speed` 0.15, `slope_gain` 0.12, `speed` 0.6, `turn_speed` 8 |
| `mubble:roll_jump` | `#mubble:all` `#mubble:aerial` `#mubble:speed` | `air_control` 0.6, `cancel_window_ticks` 3, `height` 1.4, `ticks_to_apex` 6, `variable` 1 |
| `mubble:rollout` | — | `decel` 0.03, `ticks` 10 |
| `mubble:run` | `#mubble:all` `#mubble:speed` `#mubble:ground` | `exhaustion_per_block` 0 / 0.08, `slope_gain` 0.02 |
| `mubble:side_somersault` | `#mubble:all` `#mubble:aerial` | `air_control` 0.6, `cancel_window_ticks` 3, `height` 4.6, `side_speed` 0.15, `ticks_to_apex` 11, `variable` 0 |
| `mubble:skid` | `#mubble:all` `#mubble:ground` | `brake` 0.3, `exit_speed` 0.1, `min_speed` 0.6, `reverse_dot` -0.5, `ticks` 6 |
| `mubble:slide` | `#mubble:all` `#mubble:speed` `#mubble:ground` | `exit_speed` 0.15, `flat_decel` 0.02, `gain` 0.08, `min_slope` 0.25, `min_speed` 0.6, `slope_gain` 0.12, `turn_speed` 6 |
| `mubble:spin` | `#mubble:all` `#mubble:aerial` | `air_control` 1, `cancel_window_ticks` 3, `fall_speed` 0.08, `jump_height` 3, `jump_ticks` 9, `lift_height` 0.6, `lift_ticks` 4, `ticks` 14 |
| `mubble:triple_jump` | `#mubble:all` `#mubble:aerial` | `air_control` 1, `cancel_window_ticks` 3, `height` 4.6, `min_speed` 0.7, `ticks_to_apex` 10, `variable` 0 |
| `mubble:vault` | `#mubble:all` `#mubble:ledge` `#mubble:ground` | `max_height` 1, `min_speed` 0.5, `ticks` 4 |
| `mubble:walk` | — | — |
| `mubble:wall_jump` | `#mubble:all` `#mubble:aerial` `#mubble:wall` | `air_control` 1, `cancel_window_ticks` 3, `height` 2.2, `lock_ticks` 6, `push` 0.35, `ticks_to_apex` 7, `variable` 1 |
| `mubble:wall_slide` | `#mubble:all` `#mubble:wall` | `max_fall_speed` 0.15, `min_height` 1, `reach` 0.1, `stick_dot` 0.4 |

| Parameter | Meaning |
|---|---|
| `air_control` | Scale of the air control during the dive. |
| `back_speed` | Crouching and jumping while standing: very high, drifting slightly backwards. / public class BackflipMove extends ArcMove { /** Speed of the backward drift. |
| `boost` | Speed a boost adds; the base value of {@code mubble:arcade_roll_boost}, which is what the roll reads. |
| `boost_cooldown_ticks` | Ticks between two boosts. |
| `brake` | Share of its speed the skid loses every tick. |
| `cancel_window_ticks` | Every move of the arcade movement layer. <p> The entry priorities decide which move wins when several could start on the same tick: the more specific a move is, the higher it ranks, so that crouching and jumping is a backflip before it is a jump, and jumping right after a ground pound is a ground pound jump before anything else. / public final class ArcadeMoves { /** Ticks an airborne move lasts before the aerial follow-ups may cut it short. |
| `climb_height` | The highest step a roll gets onto without stopping, in blocks: a roll goes up a hill of full blocks. |
| `decel` | Speed lost every tick on flat ground. |
| `drop_speed` | Speed of the drop; the base value of {@code mubble:arcade_ground_pound_speed}, which is what the drop reads. |
| `exhaustion_per_block` | Moving about on the ground: walking, running, and the landing that leads back to either. <p> Running is the move; walking is what the ground is without it, at the vanilla pace. Both share the same locomotion, only the speed they aim for differs. / public class GroundMove extends ArcadeMove { /** Food exhaustion per block travelled, when the profile charges for its moves. |
| `exit_speed` | Speed below which the slide stops. |
| `fall_speed` | Spinning the stick around (or pressing the spin key): in the air, a small lift and a slowed fall, once per airtime; on the ground with jump, a spinning jump ending the same way. / public class SpinMove extends ArcadeMove { /** The fastest fall while spinning. |
| `flat_decel` | Speed lost every tick on flat ground. |
| `forward` | How far past the edge the pull-up ends, in blocks. |
| `gain` | Speed gained every tick for every unit of slope. |
| `grab_band` | How far below the hanging height an edge still gets caught. |
| `hang_depth` | Hanging from an edge: caught on the way down, as long as the player faces it. Jump climbs up, crouch lets go, and the stick shimmies along the edge. <p> The edge pulls a player within reach in (ledge magnetism), but it never catches anyone on the way up: a grab must never feel like it stole a jump. / public class LedgeGrabMove extends ArcadeMove { /** How far below the edge the top of the hitbox hangs. |
| `hang_ticks` | Crouching in the air: a brief hang, then a straight, fast drop. Its landing takes no fall damage and can be jumped out of very high, see {@link GroundPoundJumpMove}. / public class GroundPoundMove extends ArcadeMove { /** How long the player hangs before dropping. |
| `jump_height` | Height of the spinning jump. |
| `jump_ticks` | Ticks the spinning jump takes to reach that height. |
| `jump_window_ticks` | Ticks after the impact during which a jump is a ground pound jump. |
| `lift` | Upward speed the lunge starts with. |
| `lift_height` | Height of the lift of a spin started in the air. |
| `lift_ticks` | Ticks the lift takes to reach that height. |
| `lock_ticks` | Ticks during which the stick cannot steer back, so that the kick is not undone right away. |
| `max_boosts` | How many boosts a roll can take. |
| `max_fall_speed` | The fastest slide down the wall; the base value of {@code mubble:arcade_wall_slide_speed}, which is what the slide reads. |
| `max_height` | Running into an obstacle up to a block high: the player mantles over it without losing speed. Anything vanilla steps up on its own is left to vanilla. / public class VaultMove extends ArcadeMove { /** The highest obstacle vaulted over, in blocks. |
| `max_speed` | Share of the run speed above which crouching and jumping is a long jump rather than a backflip. |
| `min_height` | Holding towards a wall while falling: the fall is capped, and jump kicks off the wall. Letting go still leaves a few ticks to kick, see {@link WallJumpMove}. / public class WallSlideMove extends ArcadeMove { /** How high above the ground the player has to be. |
| `min_slope` | The gentlest descent a slide starts on, as height lost per block travelled. |
| `min_speed` | Share of the run speed the player needs to move at. |
| `push` | Kicking off a wall: up and away from it. <p> It is accepted while sliding down the wall, and for a few ticks after leaving it, see {@link fr.hugman.mubble.world.arcade.ArcadeGrace#wallJumpLeniencyTicks()}. / public class WallJumpMove extends ArcMove { /** Speed the kick pushes the player away from the wall at. |
| `reach` | How far the wall may be from the player. |
| `reverse_dot` | How opposed the stick has to be to the velocity, as the cosine of the angle between them. |
| `roll_speed` | Speed a roll out of the impact starts at. |
| `roll_window_ticks` | Ticks after the impact during which crouch and action roll out of it, faster than a roll from standing. |
| `shimmy_speed` | Speed of the shimmy along the edge. |
| `side_speed` | Jumping out of a skid: a high somersault towards the new direction. / public class SideSomersaultMove extends ArcMove { /** Speed the somersault carries the player towards the new direction. |
| `slide_ticks` | The vanilla crouch: slow, low, careful at edges. Crouching at speed slides to a stop first, which is when a jump turns into a long jump. / public class CrouchMove extends ArcadeMove { /** Ticks a crouch started at the run speed takes to slide to a stop. |
| `slope_gain` | Speed gained per block of step-down, see {@link MoveContext#hugGround}. |
| `speed` | Crouching and jumping while running: low, far and fast, body flat. / public class LongJumpMove extends ArcMove { /** Speed the long jump launches at, unless the player already goes faster. |
| `stick_dot` | How squarely the stick has to point at the wall, as the cosine of the largest angle allowed. |
| `ticks` | Pulling up from a ledge: up along the wall first, then forward onto the edge. / public class LedgeClimbMove extends ArcadeMove { /** How long the pull-up lasts. |
| `turn_speed` | How fast the stick steers the roll, in degrees per tick. |
| `variable` | A move launching the player on an arc authored as a height and a number of ticks to its apex: every jump, flip and somersault. / public abstract class ArcMove extends ArcadeMove { public static final MoveParam HEIGHT = MoveParam.blocks("height", 2.2D); public static final MoveParam TICKS_TO_APEX = MoveParam.of("ticks_to_apex", 7.0D, 1.0D, 100.0D, "ticks"); /** 1 when releasing jump early cuts the arc short, 0 when the arc always goes all the way. |

### Hands, attacks and the world

`interaction` decides what a player with the layer on can do: `full` (everything), `combat_only` (attack and use items, but not break, place or use blocks and entities), `none`. On top of that, moves with busy hands (triple jump, backflip, side somersault, ground pound and its landing, ledge grab and climb, wall slide, roll, dive) block attacks and item use while they last. The rules are enforced on the server.

## Block tags

| Tag | Effect | Default |
|---|---|---|
| `#mubble:no_wall_jump` | No wall slide or wall jump against these | ice, packed ice, blue ice |
| `#mubble:no_ledge_grab` | Edges of these cannot be grabbed | `#minecraft:leaves` |
| `#mubble:bounce` | Landing on these at speed bounces the player back up (higher out of a ground pound); crouching lands normally | slime block |
| `#mubble:keeps_momentum` | Ground speed is barely lost on these | ice, packed ice, blue ice |

## Attributes

The profile sets the base value of these attributes while it is active, and they go back to their defaults when the layer turns off. Equipment, effects and commands can add modifiers like for any attribute.

| Attribute | Default | From the profile |
|---|---|---|
| `mubble:arcade_run_speed` | 0.4 | `physics.ground.run_speed` |
| `mubble:arcade_jump_height` | 1.0 | `physics.gravity.jump_height_multiplier` (scales every jump) |
| `mubble:arcade_roll_boost` | 0.1 | `moves."mubble:roll".params.boost` |
| `mubble:arcade_air_drag` | 0.99 | `physics.air.drag` |
| `mubble:arcade_air_control` | 1.0 | `physics.air.control` |
| `mubble:arcade_coyote_ticks` | 3 | `grace.coyote_ticks` |
| `mubble:arcade_wall_slide_speed` | 0.15 | `moves."mubble:wall_slide".params.max_fall_speed` |
| `mubble:arcade_ground_pound_speed` | 1.2 | `moves."mubble:ground_pound".params.drop_speed` |

## Controls

| Key | Default | Notes |
|---|---|---|
| Arcade Jump | unbound | Falls back to vanilla Jump |
| Arcade Crouch | unbound | Falls back to vanilla Sneak |
| Arcade Action | B | Dive, roll |
| Recenter Camera | unbound | Swings the orbit camera behind the player |
| Spin | unbound | The stick (or the movement keys) turned all the way around also spins |
| Arcade Debug HUD | unbound | |

With [Controlify](https://modrinth.com/mod/controlify) installed, the same actions are controller bindings (`mubble:arcade_jump`, `arcade_crouch`, `arcade_action`, `arcade_recenter`, `arcade_spin`). Action defaults to the east face button (B on Xbox); jump and crouch follow Controlify's own jump and sneak until bound; recenter and spin have no default button, every one being taken in game. The left stick is read analog: tilting it a little walks, all the way runs. The debug HUD shows the glyph of the action binding.

Input is sampled every **frame**, not every tick: a press is remembered with the time it happened, so a jump pressed one frame before landing still jumps, however low the tick rate.

## Camera

While the layer drives, the third person view is an orbit camera the mouse (or right stick) turns freely around the player. Movement is relative to the camera, and the player looks at the horizon unless aiming. It stays out of walls, follows with a slight lag that never grows past 1.5 blocks, widens the field of view with speed, and blends with the vanilla view over a quarter of a second whenever one takes over from the other (flying in creative, swimming). The crosshair aims from the camera, but only picks what the player could reach from their own eyes, so it never selects through walls or past vanilla reach; attacking or using an item turns the player towards it. First person stays fully playable.

Client settings, in `config/mubble-arcade-client.json`:

| Setting | Default | |
|---|---|---|
| `orbit_camera` | true | Off: the vanilla third person camera |
| `camera_distance` | 5 | Blocks |
| `camera_height` | 0.3 | Above the eyes |
| `follow_lag` | 0.08 | Seconds; 0 follows rigidly |
| `recenter_speed` | 540 | Degrees per second |
| `orbit_sensitivity` | 1 | |
| `min_pitch` / `max_pitch` | -80 / 80 | |
| `fov_kick` | 0.08 | Extra field of view at run speed |
| `respect_profile_hints` | true | Let profiles scale the distance and the field of view kick |
| `debug_hud` | false | Show the debug HUD on start |

## Animations

Moves animate the player model with vanilla's keyframe animation system: the limbs through the model, the whole body through the pose stack (for flips and spins), for the local player and for everyone watching. Only moves play animations; walking, running and crouching stay vanilla.

Animations are JSON files in a resource pack, `assets/<namespace>/animations/arcade/<move>.json`, reloaded with F3+T. A move plays the animation with its own id unless its profile settings name another.

```json
{
  "length": 1.05,
  "loop": false,
  "linger": false,
  "axes": "java",
  "bones": {
    "right_arm": { "rotation": { "0.0": [0, 0, 0], "0.3": { "post": [-170, 0, 10], "lerp_mode": "catmullrom" } } },
    "left_leg":  { "rotation": [{ "time": 0.0, "value": [0, 0, 0] }, { "time": 0.3, "value": [-70, 0, 0] }] }
  },
  "body": {
    "pivot": [0, 0.9, 0],
    "rotation": { "0.0": [0, 0, 0], "0.85": { "post": [360, 0, 0], "lerp_mode": "catmullrom" } }
  }
}
```

- `length` in seconds; `loop` repeats it; `linger` lets it finish after the move ends.
- `bones`: `head`, `hat`, `body`, `right_arm`, `left_arm`, `right_leg`, `left_leg`, each with `rotation` (degrees), `position` (pixels) and `scale` channels.
- `body`: the whole player, turned around `pivot` (blocks above the feet). A positive X rotation tips the head backwards, so `360` is a backflip and `-360` a front flip.
- A channel is a list of `{time, value}`, an object keyed by time, or one value held all along. A keyframe value is three numbers, or `{post, pre?, lerp_mode?}` (`linear` or `catmullrom`).
- `axes: "bedrock"` takes numbers as Blockbench exports them for Bedrock models.

A file naming a bone the player does not have, or a keyframe past its length, is refused with an error in the log.

## Commands

All need permission level 2.

| Command | |
|---|---|
| `/mubble arcade source add <players> <id> <definition>` | Adds or replaces a source, e.g. `{profile: "mubble:trial", moves: {"#mubble:all": "force"}, priority: 10, duration: 1200}` (`duration` in ticks, optional) |
| `/mubble arcade source remove <players> <id>` | |
| `/mubble arcade source list [<player>]` | |
| `/mubble arcade unlock <players> <move or #tag>` / `lock` | |
| `/mubble arcade profile <players> <profile> [deny\|force\|enable]` | A source `mubble:command/profile` with priority 1000, applying the mode to `#mubble:all` if given |
| `/mubble arcade profile <players> clear` | Removes it |
| `/mubble arcade inspect [<player>]` | Profile, state, sources, modes, owned moves, and the validator's counts with the reason of the last rejection |
| `/mubble arcade gym [<pos>]` | Builds the movement gym |
| `/mubble arcade impulse <players> <velocity>` | Pushes players the way knockback or a launcher would |

Client side, `/mubble_client arcade record` starts recording the steps of your player, `stop <name>` saves them to `mubble/arcade_recordings/<name>.json` in the game directory, `replay <name>` plays the recorded inputs back from where you stand and reports the first step that left the recording, and `cancel` stops either.

## The gym

`/mubble arcade gym` builds nine 64 block lanes side by side, each with a sign: gaps of 2 to 8 blocks, walls and ledges of 2 to 6 blocks, a 10 block stair descent, a wall jump shaft, the same shaft in packed ice (no wall jump), slime (bounce), ice (keeps momentum) and 1 block steps (vault). Gold blocks mark take-off points.

## Debug HUD

Bound to the Arcade Debug HUD key, or on at start with `debug_hud`: profile, move with its sequence number and ticks, pose, the animation playing (or vanilla), speeds, slope, coyote and input buffers, jump chain, wall and ledge timers, sources, resolved modes, owned moves, and whether a recording or replay runs.

## Multiplayer

The client predicts its own movement with the same simulation the server runs, and sends each step with its input. The server replays the step from the reported start and checks it against what the input allows, axis by axis, within the collisions of its own world; a step it cannot explain sends the player back, a state that drifted is corrected, and the client replays its later steps on top. Vanilla's "moved too quickly" check and its floating kick are left to the validator for the moves it vouches for, and stay on for everything else. Pushes from the server (knockback, explosions) are taken in by both sides.
