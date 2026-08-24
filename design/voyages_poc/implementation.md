# Voyages POC — implementation notes

What was built for [#119](https://github.com/DawnTeamMC/Mubble/issues/119) on Minecraft `26.2`, and
what you need to know to poke at it. Design rationale lives in `story_roguelike.md`; this is only the
implementation.

## Running it

```
/voyage start <voyage_id> [seed]     omitted seed is generated and reported in chat
/voyage abandon                      ends as a loss
/voyage status                       voyage, trial index, seed
```

Inside a trial you hold two marked items: an emerald (**Complete Trial**) and redstone
(**Forfeit Voyage**). Right-click to use. They stand in for real objectives, which the POC does not
have.

Testmod content: `mubble-testmod:voyage_poc` runs `trial_dawn` → `trial_shifting` → `trial_toxic` and
pays one carrot. `trial_plain` exists unused, as proof a fourth trial needs no Java.

## Datapack schema

Registries are `mubble:`-namespaced, so the folder has an extra segment — `data/<ns>/mubble/trial/`,
not `data/<ns>/trial/` as the issue writes it.

```json
// mubble/trial/<id>.json
{ "display_name": "Dawn Platform",
  "environment": "mubble-testmod:env_dawn",
  "platform": { "block": "minecraft:smooth_sandstone", "radius": 8, "spawn_y": 65 } }
```

```json
// mubble/voyage/<id>.json
{ "display_name": "POC Voyage",
  "trials": ["mubble-testmod:trial_dawn", "mubble-testmod:trial_toxic"],
  "completion_rewards": [{ "item": "minecraft:carrot", "count": 1 }] }
```

```json
// mubble/environment_profile/<id>.json
{ "attributes": {
    "visual/sky_color": "#ffa120",
    "visual/fog_color": ["#ff2244", "#3cb043"],   // list = pick one by seed
    "gameplay/sky_light_level": 12.0 },
  "fixed_time": 23000,
  "weather": "thunder" }
```

Only `display_name` and `environment` are required on a trial; everything else defaults.

## Things that will surprise you

- **Attribute names are vanilla's, not the issue's.** `visual/sky_color`, not `sky_color`. Colours
  are hex strings, not integers. `fog_density` does not exist — fog is a start and an end distance.
  This is 26.2's own `EnvironmentAttributeMap` vocabulary; aliasing it would have meant inventing a
  parallel naming scheme for engine ids.
- **Any attribute may be a list**, not just sky colour, and a plain value equals a one-entry list.
  The pick comes from `hash(voyage_seed, node_path)` then a sub-seed per attribute, so two lists of
  the same length vary independently. The derivation is pinned by a test — changing it changes what
  every previously shared seed means.
- **Rewards are `{item, count}`, not `ItemStack`.** `ItemStack.CODEC` resolves default components,
  which are not bound while dynamic registries load; using it kills the whole datapack.
- **`environment` must be an id**, never an inlined profile, because the client is told which
  environment to apply by name.
- **The client never sees a candidate list or a seed.** It is sent the resolved value.

## Decisions worth knowing

- **Fantasy** (`xyz.nucleoid:fantasy`) owns runtime dimension lifecycle, bundled via JiJ. Only
  `FantasyVoyageWorldProvider` names it; everything else goes through `VoyageWorldProvider`.
- **No new dimension type or biome per trial.** Every trial reuses `minecraft:overworld` over
  `the_void`; the look is entirely environment profiles. A runtime dimension type could not be named
  to an already-connected client.
- **26.2 already has a layered environment system**, so a profile is one layer in vanilla's stack
  rather than a bespoke resolver plus a render hook. Overriding one accessor covers sky, fog, clouds,
  light tint, particles and ambient sound at once.
- **Trial levels own their clock and weather.** Both are server-global in 26.2. The clock comes from
  Fantasy's level config at creation; weather needed a one-method override so a trial's storm does
  not rain on someone's base, and `/weather` outside cannot cancel it. Consequence: neither can be
  changed on a level that already exists.
- **The stash is the player's whole save tag**, not a list of fields. An enumerated list restores
  exactly what someone thought of — Mubble's own power-up leaked through one, and any other mod's
  state would have. Restoring clears first and loads second, because most persisted state is read
  with `ifPresent` and an absent key would leave voyage-gained state in place.
- **Disconnect and shutdown restore the player but leave the stash on disk** for the next login to
  consume. Restoring twice is harmless; consuming it before the player's own data is written is how
  an inventory disappears. This matters: after a crash, vanilla drops the player in the Overworld at
  the *trial's* coordinates, and the join-time restore is what fixes that.
- **A restart ends voyages, it does not resume them.** Trial levels are temporary, so a saved session
  would point at nothing.

## Known limits

- No objectives or rulesets — the two control items stand in. Seam left on `TrialDefinition`.
- A voyage is a flat list, not the branching Waystation tree. Node paths are already tree addresses.
- Third-party mod state is **restored** on exit but not **cleared** on entry; clearing generically
  needs a hook mods register against.
- A voyage in progress is not expected to survive a Minecraft version upgrade — the stash holds item
  stacks and no data fixer knows its layout.
- `/voyage` has no permission level, per the issue. It belongs on `start`, not the root literal —
  gating the root would also hide `status` and `abandon` from someone already inside a voyage.

## Acceptance criteria

166 game tests and 89 unit tests pass. The one failure, `koopa_shell`, is pre-existing on `dev` and
unrelated. Everything below marked **needs eyes** is untested by me.

| # | Criterion | Status | How to check |
|---|---|---|---|
| 1 | `/voyage start` teleports into trial 1 | Game test | `/voyage start mubble-testmod:voyage_poc` |
| 2 | All three trials look obviously different | **Needs eyes** | Advance through all three. Dawn: warm orange, long fog, sunrise. Shifting: seed-picked sky over near-black fog. Toxic: green, fog closing at 28 blocks, midday, thunder. Each has its own floor block. |
| 3 | Trial 2's sky changes with seed, identical for the same seed | Logic tested; **look needs eyes** | Run seed 1 and seed 2 — different skies. Re-run seed 1 later — same sky as the first time. |
| 4 | No new dimension type or biome per trial | Met by construction | F3 in a trial shows `mubble:voyage/<n>`; the dimension type is `minecraft:overworld`. |
| 5 | ADVANCE moves through trials in order; the third completes | Game test | Right-click the emerald three times. |
| 6 | Completing returns you to your exact position with 1 carrot | Game test | Note where you stand before starting. |
| 7 | FAIL and `/voyage abandon` return you with no carrot | Game test | Try both. |
| 8 | Inventory, effects, health, hunger, XP, gamemode identical before and after | Game test, except gamemode | Go in with a full inventory, armour, an effect and some levels. Gamemode has no test — the test framework's mock player hard-codes it. |
| 9 | Control items do not persist | Game test | After any ending, no emerald called *Complete Trial* anywhere. Ordinary emeralds you carried must be untouched. |
| 10 | Voyage levels deleted on exit | Fantasy's temporary levels | `<world>/dimensions/mubble/voyage/` empty afterwards. Run ten times; the folder must not grow. |
| 11 | Two players run separate voyages without interfering | **Not tested** — a game test cannot hold two clients | Two clients, two voyages at once. |
| 12 | Logging out mid-voyage and back in leaves you safe with inventory intact | **Needs eyes** | Quit to title inside a trial, load again. Log should say "Restoring … from an unfinished voyage". |
| 13 | Restarting the server mid-voyage does the same | **Needs eyes** | Kill the process rather than quitting — this is the one that exercises crash recovery on its own. |
| 14 | A fourth trial with no Java changes | Met | Add `mubble-testmod:trial_plain` to `voyage_poc` and run it. |
| 15 | A server-only profile renders on the client | **Needs eyes** | Put a profile in the server's `world/datapacks/` only, point a trial at it, run the voyage. |
| 16 | `/reload` updates an already-connected client | **Needs eyes** | While standing in a trial, edit a colour in `env_dawn.json`, run `/reload`. Sky changes without reconnecting. |
| 17 | Same voyage id + seed produces an identical run | Met, derivation pinned by test | Run the same seed twice. |
| 18 | No `random()` in voyage code paths | Met | The one draw is the initial seed, in `VoyageSeeds.random()`. |
| 19 | No compatibility shims or fallbacks for vanilla clients | Met | None in the diff. |

Also worth a look while you are in there, since neither is a listed criterion:

- **Moving between trials should log nothing.** An "evacuating to spawn" error means a level is being
  deleted before the player has left it.
- **The ender chest is yours throughout.** Put something in before a voyage and something else in
  during one; both should survive.
- **A full inventory keeps the reward** — the carrot should be at your feet, not gone. Survival only;
  vanilla deletes the overflow for creative players.
