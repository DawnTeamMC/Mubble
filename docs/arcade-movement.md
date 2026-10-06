# Arcade movement

An optional movement layer in the spirit of Super Mario Odyssey and Donkey Kong Bananza: jump chains, long jumps, backflips, ground pounds, wall jumps, ledge grabs, rolls, dives, spins. It stays Minecraft: same hitboxes, same collisions, same blocks, same server rules.

When nothing gives a player the layer, nothing changes for them: vanilla movement, vanilla jump, vanilla camera, vanilla animations.

The layer is a module of its own, `mubble-arcade` (mod id `mubble_arcade`), built on top of the core. Its content keeps the `mubble` namespace, so ids, commands and data read the same as the rest of Mubble.

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

The layer steps aside, and vanilla takes over, while the player is dead, sleeping, riding, a spectator, gliding with an elytra, flying, or in lava, and while changing dimension. It picks up again on the next tick it can. Water and climbing are not among these: the layer swims and climbs, see [Swimming](#swimming) and [Climbing](#climbing).

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

**Cues** (sound, particles and rumble) are data: per move, `start`, `tick`, `land` and `boost` cues with `sound`, `volume`, `pitch`, `particle`, `surface` (particles of the block underfoot), `shape` (`burst`, `ring`, `trail`), `count`, `count_per_speed`, `spread`, `speed`, `interval` (ticks, for `tick` cues), and `rumble`: `{"strong": 0–1, "weak": 0–1, "ticks": n, "scale_with_speed": bool}`, the two motors of the controller, felt only by the player making the move, only on a controller (through Controlify). Other players' clients play the sounds and particles too. The shipped moves rumble lightly: a tap for jumps, a thud for impacts.

## Moves

All are in the `mubble` namespace, registered by code (`mubble:arcade_move` registry), and tuned by profiles.

| Input | Move |
|---|---|
| Jump | Jump; jump again on landing for a double then a triple jump (needs speed) |
| Crouch in the air | Ground pound; jump right after landing for a ground pound jump |
| Ground pound, then action | Dive, as in Super Mario Odyssey, landing into a rollout |
| Keep crouch held through a ground pound, action as it lands | Ground pound roll, starting faster than a roll |
| Crouch, then jump | Backflip (standing) or long jump (running) |
| Reverse at speed, then jump | Skid, then side somersault |
| Crouch + action while running | Roll (action again to boost; it climbs steps up to a block), jump out of it for a roll jump |
| Spin key, or the stick turned all the way around | Spin, once per jump |
| Fall along a wall | Wall slide; jump to wall jump |
| Fall next to an edge facing it | Ledge grab; jump to climb, crouch to drop, stick sideways to shimmy |
| Run into a 1 block step | Vault |
| Crouch while running down a slope | Slide |
| In water | Swim, and the moves of the water, see [Swimming](#swimming) |
| On a ladder, vines, scaffolding | Climb, see [Climbing](#climbing) |

**Action** is not a button of its own: it is attack and use, as Cappy's buttons are in Super Mario Odyssey. A press goes to the moves while crouch is held (a roll, or a dash in water) and while the move keeps the hands busy (a dive out of a ground pound, a roll boost); the rest of the time, the hands hit and use the item they hold. A press given to the moves is kept from the hands until the button is let go.

### Swimming

Water deep enough to swim in (deeper than vanilla wades in) is the layer's own, on the numbers of vanilla swimming: slow strokes towards the stick (0.1 b/t), faster with sprint held, a slow sink, rising while jump is held and sinking faster while crouch is, depth strider and dolphin's grace included. On top of that:

| Input | In water |
|---|---|
| Jump, under the surface | A stroke upward |
| Jump, head out of the water | Jump out of it |
| Crouch | Ground pound, slowed down by the water (`water_drop`) |
| Ground pound, or crouch, then action | Swim dash: forward, body flat, until the water slows it down; a dive plunging into water carries on as one |
| Fall next to an edge facing it | Ledge grab, to climb out |

`mubble:swim` is not a move of its own, like walking and falling; `mubble:swim_dash` comes with `mubble:dive`. The player leaves the water moves once out of the water, or standing where it is too shallow to swim. Lava stays vanilla's.

### Climbing

Whatever vanilla lets a player climb (`#minecraft:climbable`: ladders, vines, scaffolding, twisting and weeping vines…, and an open trapdoor over a ladder) catches the player, whatever they were doing, and is the layer's own, on the numbers of vanilla climbing:

| Input | On a ladder |
|---|---|
| Stick into the wall, or jump held | Climb, at vanilla's pace (0.2 b/t, 0.12 once gravity has its share) |
| Nothing | Slide down, no faster than 0.15 b/t |
| Crouch held | Hold on (not on scaffolding, as in vanilla) |
| Stick sideways or away | Move along it or off it, at walking pace, no faster than 0.15 b/t |

The player lets go by walking off, or at the top, onto it or into a ledge grab. Climbers take no fall damage. `mubble:climb` is not a move of its own either, and its numbers can be tuned like any other's (`climb_speed`, `speed_cap`, `side_accel`, `turn_speed`).
### Tags

| Tag | Moves |
|---|---|
| `#mubble:all` | every move |
| `#mubble:aerial` | jump, double/triple jump, ground pound, ground pound jump, wall jump, roll jump, long jump, backflip, side somersault, dive, spin |
| `#mubble:wall` | wall slide, wall jump |
| `#mubble:ledge` | ledge grab, vault |
| `#mubble:speed` | run, roll, roll jump, long jump, dive, slide |
| `#mubble:ground` | run, skid, crouch, roll, slide, vault |

Parts of a move are not tagged: ground pound landing, ledge climb, rollout and swim dash follow the move they belong to.

### Parameters

| Move | Tags | Parameters (trial / overworld) |
|---|---|---|
| `mubble:backflip` | `#mubble:all` `#mubble:aerial` | `air_control` 0.5, `back_speed` 0.08, `cancel_window_ticks` 3, `height` 5.2, `max_speed` 0.3, `ticks_to_apex` 12, `variable` 0 |
| `mubble:climb` | — | `climb_speed` 0.2, `side_accel` 0.05, `speed_cap` 0.15, `turn_speed` 20 |
| `mubble:crouch` | `#mubble:all` `#mubble:ground` | `slide_ticks` 12 |
| `mubble:dive` | `#mubble:all` `#mubble:aerial` `#mubble:speed` | `air_control` 0.3, `lift` 0.25, `speed` 0.5 |
| `mubble:double_jump` | `#mubble:all` `#mubble:aerial` | `air_control` 1, `cancel_window_ticks` 3, `height` 3.2, `min_speed` 0.2, `ticks_to_apex` 8, `variable` 1 |
| `mubble:fall` | — | — |
| `mubble:ground_pound` | `#mubble:all` `#mubble:aerial` | `drop_speed` 1.2, `hang_ticks` 6, `min_height` 1, `water_drop` 0.35 |
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
| `mubble:swim` | — | `accel` 0.02, `drag` 0.8, `rise` 0.04, `sink` 0.04, `sprint_drag` 0.9, `stroke` 0.2, `surface_depth` 1.4, `turn_speed` 15 |
| `mubble:swim_dash` | — | `drag` 0.92, `speed` 0.45, `ticks` 12, `turn_speed` 4 |
| `mubble:triple_jump` | `#mubble:all` `#mubble:aerial` | `air_control` 1, `cancel_window_ticks` 3, `height` 4.6, `min_speed` 0.7, `ticks_to_apex` 10, `variable` 0 |
| `mubble:vault` | `#mubble:all` `#mubble:ledge` `#mubble:ground` | `max_height` 1, `min_speed` 0.5, `ticks` 4 |
| `mubble:walk` | — | — |
| `mubble:wall_jump` | `#mubble:all` `#mubble:aerial` `#mubble:wall` | `air_control` 1, `cancel_window_ticks` 3, `height` 2.2, `lock_ticks` 6, `push` 0.35, `ticks_to_apex` 7, `variable` 1 |
| `mubble:wall_slide` | `#mubble:all` `#mubble:wall` | `max_fall_speed` 0.15, `min_height` 1, `reach` 0.1, `stick_dot` 0.4 |

| Parameter | Meaning |
|---|---|
| `accel` | Speed the stick adds every tick, as vanilla's. |
| `air_control` | Scale of the air control during the dive. |
| `back_speed` | Speed of the backward drift. |
| `boost` | Speed a boost adds; the base value of {@code mubble:arcade_roll_boost}, which is what the roll reads. |
| `boost_cooldown_ticks` | Ticks between two boosts. |
| `brake` | Share of its speed the skid loses every tick. |
| `cancel_window_ticks` | Ticks an airborne move lasts before the aerial follow-ups may cut it short. |
| `climb_height` | The highest step a roll gets onto without stopping, in blocks: a roll goes up a hill of full blocks. |
| `climb_speed` | Upward speed climbing gives, as vanilla's. |
| `decel` | Speed lost every tick on flat ground. |
| `drag` | Share of its speed the dash keeps every tick. |
| `drop_speed` | Speed of the drop; the base value of {@code mubble:arcade_ground_pound_speed}, which is what the drop reads. |
| `exhaustion_per_block` | Food exhaustion per block travelled, when the profile charges for its moves. |
| `exit_speed` | Speed below which the slide stops. |
| `fall_speed` | The fastest fall while spinning. |
| `flat_decel` | Speed lost every tick on flat ground. |
| `forward` | How far past the edge the pull-up ends, in blocks. |
| `gain` | Speed gained every tick for every unit of slope. |
| `grab_band` | How far below the hanging height an edge still gets caught. |
| `hang_depth` | How far below the edge the top of the hitbox hangs. |
| `hang_ticks` | How long the player hangs before dropping. |
| `jump_height` | Height of the spinning jump. |
| `jump_ticks` | Ticks the spinning jump takes to reach that height. |
| `jump_window_ticks` | Ticks after the impact during which a jump is a ground pound jump. |
| `lift` | Upward speed the lunge starts with. |
| `lift_height` | Height of the lift of a spin started in the air. |
| `lift_ticks` | Ticks the lift takes to reach that height. |
| `lock_ticks` | Ticks during which the stick cannot steer back, so that the kick is not undone right away. |
| `max_boosts` | How many boosts a roll can take. |
| `max_fall_speed` | The fastest slide down the wall; the base value of {@code mubble:arcade_wall_slide_speed}, which is what the slide reads. |
| `max_height` | The highest obstacle vaulted over, in blocks. |
| `max_speed` | Share of the run speed above which crouching and jumping is a long jump rather than a backflip. |
| `min_height` | How high above the ground the player has to be. |
| `min_slope` | The gentlest descent a slide starts on, as height lost per block travelled. |
| `min_speed` | Share of the run speed the player needs to move at. |
| `push` | Speed the kick pushes the player away from the wall at. |
| `reach` | How far the wall may be from the player. |
| `reverse_dot` | How opposed the stick has to be to the velocity, as the cosine of the angle between them. |
| `rise` | Upward speed added every tick jump is held, as vanilla's. |
| `roll_speed` | Speed a roll out of the impact starts at. |
| `roll_window_ticks` | Ticks after the impact during which crouch and action roll out of it, faster than a roll from standing. |
| `shimmy_speed` | Speed of the shimmy along the edge. |
| `side_accel` | Speed the stick adds or takes away every tick, sideways. |
| `side_speed` | Speed the somersault carries the player towards the new direction. |
| `sink` | Downward speed added every tick crouch is held, as vanilla's. |
| `slide_ticks` | Ticks a crouch started at the run speed takes to slide to a stop. |
| `slope_gain` | Speed gained per block of step-down, see {@link MoveContext#hugGround}. |
| `speed` | Speed the long jump launches at, unless the player already goes faster. |
| `speed_cap` | The fastest the player goes sideways and slides down, on each axis, as vanilla's. |
| `sprint_drag` | The same with sprint held, as vanilla's sprint swimming. |
| `stick_dot` | How squarely the stick has to point at the wall, as the cosine of the largest angle allowed. |
| `stroke` | Upward speed a press of jump strokes up to, under the surface. |
| `surface_depth` | The deepest the feet may be for jump to jump out of the water rather than stroke: the head is out of it. |
| `ticks` | How long the pull-up lasts. |
| `turn_speed` | How fast the body turns towards the stick, in degrees per tick. |
| `variable` | 1 when releasing jump early cuts the arc short, 0 when the arc always goes all the way. |
| `water_drop` | Share of the drop speed left in water, which slows the pound down. |

### Hands, attacks and the world

`interaction` decides what a player with the layer on can do: `full` (everything), `combat_only` (attack and use items, but not break, place or use blocks and entities), `none`. On top of that, moves with busy hands (triple jump, backflip, side somersault, ground pound and its landing, ledge grab and climb, wall slide, roll, dive, swim dash) block attacks and item use while they last.

With the orbit camera on, the hands do not follow the camera: they reach out where the body faces, level with the horizon, to the nearest entity in front within the vanilla reach (a little off to the side still counts, walls do not let it through). Attack hits it, use uses the item, on it or in that direction: a bow shoots where the player faces, food is eaten. Blocks are never targeted, so nothing is mined, placed or used, whatever the policy; first person keeps the vanilla crosshair. The rules are enforced on the server.

Using an item slows the player down as in vanilla: drawing a bow, eating, blocking with a shield, the stick counts for the `use_effects` speed of the item (a fifth by default), and running is off meanwhile. The client slows its own input down, the server holds it to the item it knows is in use: a frame that goes faster than the item allows is slowed down before it is replayed.

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

Every arcade action has its own key, in the *Arcade Movement* category of the controls menu, and can be rebound without touching vanilla's. The basic ones default to the keys of their vanilla counterparts; vanilla flags no conflict between two keys left on their defaults, and the two never act at once: while the layer drives, the vanilla jump, sneak, sprint, attack and use stand aside (what vanilla does with them, such as the server hearing of sneaking, follows the arcade keys), and the rest of the time the arcade keys do nothing.

| Key | Default | |
|---|---|---|
| Move Forward / Backward / Left / Right | W / S / A / D | The stick: relative to the orbit camera |
| Jump | Space | Jump, and every jump of the chain |
| Jump (second key) | unbound | For the second jump button of a controller |
| Crouch, Ground Pound | Left Shift | Crouching shows, and the server knows it |
| Sprint | Left Control | Full speed right away; faster swimming |
| Attack · Roll, Dive in a combo | Left mouse button | Hit; roll with crouch held, dive out of a ground pound |
| Use Item · Roll, Dive in a combo | Right mouse button | Use the item; roll and dive like attack |
| Spin | B | The stick (or the movement keys) turned all the way around also spins |
| Recenter Camera | unbound | Swings the orbit camera behind the player, until the mouse or the stick turns it |
| Arcade Debug HUD | unbound | |

With [Controlify](https://modrinth.com/mod/controlify) installed, every arcade action also has a controller binding of its own, in the *Arcade Movement* category of Controlify's controls, laid out by default like **Super Mario Odyssey**. Controlify's own bindings and their defaults are left as they are: the arcade bindings belong to a context of their own, which only applies while the layer drives and no screen is open. Buttons go by their place: on a Switch controller, A is east; on an Xbox one, B is.

| Button (Switch) | Arcade binding | Controlify's own binding there, which gives way meanwhile |
|---|---|---|
| Left stick | Move | walk |
| A / B | Jump / Jump (second key) | jump / — |
| Y | Attack · roll, dive in a combo | swap hands (in the radial menu) |
| X | Use Item · roll, dive in a combo | inventory |
| ZR | Crouch, Ground Pound | attack |
| ZL | Trigger Power-Up (arcade layout) | use |
| Left stick press | Sprint | sprint |
| Right stick press | Recenter Camera | sneak |
| D-pad ↑ | Inventory (arcade layout) | chat (in the radial menu) |
| — | Spin, Arcade Debug HUD, Toggle Camera Follow (controller) | |

The buttons press the arcade keys, so that the layer reads one set of keys whatever plays it, and the stick is read analog: tilting it a little walks, all the way runs. Spin has no button, as in SMO: turn the stick all the way around. Everything else (the hotbar on L and R, drop, pick block, the radial menu, pause) stays where Controlify puts it. Toggle Camera Follow (controller) switches the [camera following the player](#camera), and can be put in Controlify's radial menu, with an icon. The power-up key of Mubble has a binding of its own too, on B, without the arcade layout.

While the layer drives, a binding of Controlify's on a button an arcade binding uses reads as unbound: it neither acts, nor presses its key, nor shows in the button guide. Controlify has no API for a mod to take buttons over for a while, so this one hook (`InputBindingImplMixin`) reaches into it, only while the arcade layout is in use: any screen, its controls menu and radial menu among them, sees every binding as it is.

**The button guide** of Controlify follows the moves. Every tick, the client plans the next tick on a copy of the state as if each button were pressed, and names the move it would start next to the glyph of the arcade binding, translated: *Jump*, then *Double Jump* on landing, *Long Jump* or *Backflip* while crouching, *Roll* on attack and use with crouch held, *Dive* during a ground pound, *Ground Pound* in the air, *Hold On* on crouch while climbing… The rules are data, `assets/controlify/contextual/guide/in_game.json`, reading the facts `mubble:arcade`, `mubble:arcade_orbiting`, `mubble:arcade_climbing`, `mubble:arcade_power_up` and `mubble:arcade/<button>/<move>` the client contributes.

**Rumble**: see the `rumble` of the cues, under [Profiles](#profiles). It stays light, a hint more than a shake; Controlify's own rumble settings scale it further.

To try it in the dev client, run the sandbox with `./gradlew :mubble-testmod:runClient -Pcontrolify`, which loads Controlify and what it needs; `-Pmodmenu` adds Mod Menu, to open the settings.

Input is sampled every **frame**, not every tick: a press is remembered with the time it happened, so a jump pressed one frame before landing still jumps, however low the tick rate.

## Camera

While the layer drives, the third person view is an orbit camera the mouse (or right stick) turns freely around the player. Movement is relative to the camera, and the player looks at the horizon unless aiming. It stays out of walls, follows with a slight lag that never grows past 1.5 blocks (when a step down or a corner hides where it trails, it pulls in only as far as the block requires), widens the field of view with speed, and blends with the vanilla view over a quarter of a second whenever one takes over from the other (flying in creative, lava). The hands do not aim with it: they reach out where the body faces, see [Hands, attacks and the world](#hands-attacks-and-the-world). First person stays fully playable, with the vanilla crosshair.

**Following the player**: as in Nintendo's games, the orbit camera can swing round behind the player as they move. It only ever moves while the player does, faster the faster they go, and the more they go sideways to the view, so that running towards the camera never turns it around; turning it by hand pauses it for a moment. It is a setting of its own for the keyboard and mouse (off by default) and for controllers (on by default), the one that applies following what the player plays on; the controller one can also be switched from Controlify's controls or radial menu.

Client settings, in `config/mubble-arcade-client.json`, and on the settings screen of Mubble, which Mod Menu opens from the entry of Mubble or of the arcade movement (the camera height, recenter speed and pitch limits are only in the file):

| Setting | Default | |
|---|---|---|
| `orbit_camera` | true | Off: the vanilla third person camera |
| `auto_camera_keyboard` | false | The camera follows the player round, on the keyboard and mouse |
| `auto_camera_controller` | true | The same, on a controller |
| `auto_camera_speed` | 120 | Degrees per second, at run speed |
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

Moves animate the player model with vanilla's keyframe animation system: the limbs through the model, the whole body through the pose stack (for flips, spins, and squash and stretch), for the local player and for everyone watching. Only moves play animations; walking, running and crouching stay vanilla.

Going from one animation to the next, or between an animation and the vanilla pose, the model never snaps: it is posed both ways and eased from one into the other (`in_out_sine`) over the `blend_in` of the animation it goes into, 0.15 s back to vanilla. Flips ease out the short way round. The debug HUD shows the blend as it happens.

Animations are JSON files in a resource pack, `assets/<namespace>/animations/arcade/<move>.json`, reloaded with F3+T. A move plays the animation with its own id unless its profile settings name another.

```json
{
  "length": 0.6,
  "loop": false,
  "linger": false,
  "blend_in": 0.06,
  "axes": "java",
  "bones": {
    "right_arm": { "rotation": { "0.0": [-60, 0, 10], "0.35": { "post": [0, 0, 30], "easing": "out_back" } } },
    "left_leg":  { "rotation": [{ "time": 0.0, "value": [0, 0, 0] }, { "time": 0.3, "value": [-70, 0, 0] }] }
  },
  "body": {
    "pivot": [0, 0.9, 0],
    "rotation": { "0.0": [0, 0, 0], "0.3": { "post": [-360, 0, 0], "easing": "in_out_quad" } },
    "scale": { "0.0": [1, 1, 1], "0.38": { "post": [0.82, 1.28, 0.82], "easing": "out_cubic" } }
  }
}
```

- `length` in seconds; `loop` repeats it; `linger` lets it finish after the move ends; `blend_in` is how long the model takes to ease into it, in seconds (0.12 by default).
- `bones`: `head`, `hat`, `body`, `right_arm`, `left_arm`, `right_leg`, `left_leg`, each with `rotation` (degrees), `position` (pixels) and `scale` channels.
- `body`: the whole player, turned around `pivot` (blocks above the feet), and scaled from the feet: squash and stretch. A positive X rotation tips the head backwards, so `360` is a backflip and `-360` a front flip.
- A channel is a list of `{time, value}`, an object keyed by time, or one value held all along. A keyframe value is three numbers, or `{post, pre?, lerp_mode?, easing?}`: `lerp_mode` is `linear` or `catmullrom`, and `easing` shapes the way to that keyframe, with any of vanilla's easing types (`in_sine`, `out_quad`, `in_out_cubic`, `out_back`, `out_elastic`, `out_bounce`… all of `in_`, `out_` and `in_out_` with `sine`, `quad`, `cubic`, `quart`, `quint`, `expo`, `circ`, `back`, `elastic` and `bounce`), or `{"cubic_bezier": [x1, y1, x2, y2]}`.
- `axes: "bedrock"` takes numbers as Blockbench exports them for Bedrock models.

The shipped animations stretch the body on take-offs (jumps, wall jumps, the ground pound jump), squash it on landings, and drop a ground pound stretched along its fall, flattened on impact before it springs back past its shape.

A file naming a bone the player does not have, a keyframe past its length or an unknown easing is refused with an error in the log.

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
