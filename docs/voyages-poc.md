# Voyages POC — where everything is

Index and status for [#119](https://github.com/DawnTeamMC/Mubble/issues/119), Minecraft `26.2`.
All six phases are built.

> Same placement caveat as the notes it links: these are engineering notes living in the published
> wiki root because the issue asked for that path. None are in `_meta.json`, so they should stay out
> of the navigation.

| Phase | Note | What it covers |
|---|---|---|
| 0 | [runtime-worlds.md](runtime-worlds.md) | Runtime level provisioning, and why it is Fantasy |
| 1 | [environment-profiles.md](environment-profiles.md) | Environment profiles and the override stack |
| 2 | [trials-and-voyages.md](trials-and-voyages.md) | Trial and voyage definitions, seed-resolved candidates |
| 3–5 | [voyage-sessions.md](voyage-sessions.md) | The session runtime, `/voyage`, and rewards |

Try it with:

```
/voyage start mubble-testmod:voyage_poc
```

## Acceptance criteria

The issue marks four criteria 👁 — things only a human looking at the game can answer. Those are the
four still open. Everything else is covered by a test.

| Criterion | State |
|---|---|
| `/voyage start` teleports the player into trial 1 | **Met**, game test |
| 👁 All three trials look obviously different on arrival | **Needs eyes** |
| 👁 Trial 2's sky changes with the seed, identical for the same seed | **Met** in a game test that walks 100 seeds; the *look* needs eyes |
| No new dimension type or biome per trial | **Met.** Every trial reuses `minecraft:overworld` and `minecraft:the_void` |
| ADVANCE moves through trials in order; the third completes | **Met**, game test |
| Completing returns the player to their exact position with 1 carrot | **Met**, game test |
| FAIL and `/voyage abandon` return the player with no carrot | **Met**, game test |
| Inventory, effects, health, hunger, XP and gamemode identical before and after | **Met** — game mode is the one field with no test, because the framework's mock player hard-codes it |
| Control items do not persist after the voyage | **Met**, game test |
| Voyage levels are deleted on exit | **Met** by Fantasy's temporary levels; **needs eyes** on the folder not growing |
| Two players can run separate voyages simultaneously | **Met** by construction — sessions are keyed by player UUID and each trial opens its own level — but **not tested**, since a game test cannot hold two connected clients |
| Logging out mid-voyage and back in | **Met** by design; **needs eyes** end to end |
| Restarting the server mid-voyage | Same |
| A fourth trial with no Java changes | **Met.** `mubble-testmod:trial_plain` is exactly that |
| 👁 A server-only profile renders on the client | **Needs eyes** |
| 👁 `/reload` updates an already-connected client | **Needs eyes** |
| Same voyage id + seed produces an identical run | **Met**, and the derivation is pinned by a test so it cannot drift silently |
| No `random()` in voyage code paths | **Met.** The one draw is the initial seed, in a method that says so |
| No compatibility shims or fallbacks for vanilla clients | **Met.** There are none in the diff |

## Where the POC knowingly stops

Written down so none of it is mistaken for finished:

- **Objectives and rulesets.** A trial is a look and a floor; two control items stand in for "the
  trial is over". Left as a seam on `TrialDefinition` rather than stubbed.
- **Branching.** A voyage is a flat ordered list, not the three-choice Waystation tree. Nothing
  consumes the list positionally except the node path, which is already a tree address.
- **Clearing third-party mod state on entry.** Restoring already covers it; clearing cannot be
  generic without a hook mods register against.
- **Surviving a Minecraft version upgrade mid-voyage.** The stash holds item stacks and effects and
  no data fixer knows its layout.
- **`/voyage` has no permission level**, per the issue. `voyage-sessions.md` notes where one goes.
