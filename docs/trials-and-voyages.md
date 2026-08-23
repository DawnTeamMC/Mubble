# Trials and voyages — phase 2

Notes for [#119](https://github.com/DawnTeamMC/Mubble/issues/119), Minecraft `26.2`.

> Same placement caveat as the other two: this is an engineering note living in the published wiki
> root because the issue asked for that path. Not in `_meta.json`, so it should stay out of the
> navigation.

## What phase 2 added

Two datapack registries and one general capability:

| | |
|---|---|
| `mubble:trial` | a look and a floor |
| `mubble:voyage` | an ordered list of trials, plus what completing them pays |
| candidate lists | any environment attribute may name several values and pick one by seed |

Nothing runs a voyage yet. Phase 3 owns the session that walks one; this phase is the data it will
walk, plus the resolver it will call.

## Schema

```json
// data/<namespace>/mubble/trial/<id>.json
{
  "display_name": "Dawn Platform",
  "environment": "mubble-testmod:env_dawn",
  "platform": { "block": "minecraft:smooth_sandstone", "radius": 8, "spawn_y": 65 }
}
```

```json
// data/<namespace>/mubble/voyage/<id>.json
{
  "display_name": "POC Voyage",
  "trials": ["mubble-testmod:trial_dawn", "mubble-testmod:trial_shifting", "mubble-testmod:trial_toxic"],
  "completion_rewards": [{ "item": "minecraft:carrot", "count": 1 }]
}
```

Three notes where this differs from the issue, all forced rather than chosen:

1. **The path has an extra segment.** The issue writes `data/<namespace>/trial/`. That spelling is
   only right for `minecraft:`-namespaced registries; ours are `mubble:`-namespaced, so the folder is
   `data/<namespace>/mubble/trial/`. Same rule the existing `mubble/environment_profile` folder
   follows.
2. **`environment` must be an id, never an inline profile.** `RegistryFileCodec` allows inlining by
   default and it is switched off here. The client is told which environment to apply *by naming the
   profile*, so an inline one would apply server-side and never render. Failing while the data pack
   loads beats a sky that only changes in single player.
3. **`radius` and `spawn_y` are bounded**, at 0–64 and −63–319. The upper and lower ends are the
   overworld's build limits, because voyage levels reuse the overworld dimension type
   (`runtime-worlds.md` §2), and the slab goes one block below `spawn_y`.

Everything except `display_name` and `environment` has a default, so the shortest valid trial is two
lines. `mubble-testmod:trial_plain` is exactly that, and exists to keep the "a fourth trial needs no
Java" criterion honest.

### Rewards are not `ItemStack`s

`{ "item": …, "count": … }`, parsed into a `VoyageReward`, not vanilla's `ItemStack.CODEC`. Not a
style preference: `ItemStack.CODEC` resolves the item's default components, and those are not bound
yet while dynamic registries load. Using it fails the whole data pack with

```
Item minecraft:carrot does not have components yet
```

An item and a count are all a reward needs to name, and `toStack()` turns one into a stack when
phase 5 actually hands it over. This also happens to be the spelling the issue used.

## Seed-resolved candidates

Any attribute on an environment profile may name a list instead of a value:

```json
{
  "attributes": {
    "visual/sky_color": ["#ff2244", "#3cb043", "#ffe000", "#7b2fff"],
    "visual/fog_color": "#222222"
  }
}
```

A plain value and a one-entry list mean the same thing. An empty list is rejected at load. This
works on any attribute — `gameplay/sky_light_level`, the gameplay booleans, anything — because it is
a property of the map codec and not of a field.

### How one is picked

```
node seed      = hash(voyage_seed, node_path)
per-attribute  = hash(node seed, attribute id)
index          = per-attribute mod candidate count
```

Both hashes are the same function, `VoyageSeeds.derive`: FNV-1a over the salt, bracketed by the
SplitMix64 finaliser. Two details are load-bearing.

**Why not `String.hashCode`.** It is stable across JVMs, which is the usual reason to reach for it,
but it barely avalanches: `"voyage/1"` and `"voyage/2"` differ by one. Since the last step is a
modulo, adjacent nodes would draw adjacent candidates and a voyage would march through its options in
order instead of looking varied.

**Why the attribute id is mixed in.** Without it, every list in a profile resolves from the same
number, so two four-entry lists always land on the same index — a profile offering four skies and
four fogs would show four combinations instead of sixteen. There is a test that counts exactly this.

The derivation is **pinned by a test** with hard-coded expected values. Changing the algorithm is
allowed, but it silently changes what every previously shared run code means, so it should be a
decision rather than a refactor.

### The client never sees a list

Resolution happens server-side, once, on entry. The split is:

| | carries | reaches the client as |
|---|---|---|
| `EnvironmentAttributeChoices.fixed()` | attributes naming one value | the synced profile registry |
| `resolveCandidates(nodeSeed)` | the picks, plus per-instance overrides | `ActiveEnvironmentPayload` |

Both sides then stack the same two layers, `[fixed, resolved]`, so a mistake shows up as the wrong
sky on both rather than as a desync where only one is wrong. The profile's `NETWORK_CODEC` drops
candidate lists entirely, so a client cannot read one even by accident — and cannot use one to
predict what a future trial will look like.

It still *reads* the full form, candidate lists included. Registry sync does not promise that both
ends use the same codec, which is the same trap that crashed clients on join in phase 1.

### A rebuilt vanilla codec

`EnvironmentAttributeMap.CODEC` dispatches each attribute to a per-attribute entry codec that has no
list form, and that entry codec is **private**. `EnvironmentAttributeChoices` rebuilds it from the
public parts (`valueCodec()`, `type().modifierCodec()`, `AttributeModifier#argumentCodec`) so it can
wrap it in `either(entry, entry.listOf())`.

That copy can drift. The mitigation is a test that feeds the same JSON — plain values, a non-colour
value, a non-syncable attribute, and the long `{"modifier": …, "argument": …}` form — to both codecs
and asserts the results are equal. If Mojang changes the shape, that test fails instead of data packs
quietly changing meaning.

## Acceptance criteria

| Criterion | State |
|---|---|
| No new dimension type or biome per trial | **Met.** Every trial reuses `minecraft:overworld` and `minecraft:the_void`; the look is entirely profile-driven. |
| All three trials look obviously different | **(unverified — needs a human in-game.)** |
| Trial 2's sky changes with seed, identical across matching seeds | **Met**, covered by a game test that walks 100 voyage seeds and asserts all four candidates appear and one seed always gives the same colour. Still worth looking at. |
| A fourth trial with no Java changes | **Met.** `trial_plain` is a fourth trial that also exercises every default. |

## Manual test

**(unverified — needs a human in-game.)** The codecs and the resolver are covered by tests; the two
things tests cannot answer are whether the three trials actually look different and whether the
platform is somewhere sane to stand.

```
/voyagespike open mubble-testmod:trial_dawn
/voyagespike close
/voyagespike open mubble-testmod:trial_shifting 1
/voyagespike close
/voyagespike open mubble-testmod:trial_shifting 2
```

1. **Each trial looks obviously different**, and you land on its own block type — sandstone for
   dawn, deepslate tiles for shifting, moss for toxic.
2. **`trial_shifting` changes with the seed.** Seeds 1 and 2 should give different skies. Re-open
   seed 1 later and it must be the same sky it was the first time.
3. **Sky and fog vary independently.** Across a handful of seeds you should not see the same
   sky-and-fog pairing every time.
4. **The time of day is per trial.** Dawn sits at 23000 and toxic at 6000, both paused, and the
   overworld's clock must not move when you enter either. This is Fantasy's per-level clock rather
   than vanilla's, which is server-wide, so it is the piece most worth a suspicious look.
5. **`trial_plain` works**, landing you on the default 17×17 stone slab with an unmodified sky. It is
   the fourth trial, added with no Java.
6. **`/voyagespike voyage mubble-testmod:voyage_poc`** lists three trials in order and one carrot.

## Still open

- **Running a voyage** is phase 3: the session, the player-state stash, and walking node to node.
  `VoyageDefinition.nodePath(int)` is the addressing this phase leaves behind for it.
- **Rulesets and objectives** are deliberately absent from `TrialDefinition` rather than stubbed. Both
  would be optional fields when they arrive, so adding them breaks no existing file — whereas an
  empty registry designed now would have to be designed wrong.
- **Branching.** The flat `trials` list is a stand-in for three-choice Waystations. Nothing consumes
  the list positionally except the node path, which is already a tree address.
- **`weather`** is still where phase 1 left it: server-global, so it leaks out of the trial. It is
  the last field that must not ship as it is. `fixed_time` is no longer on this list — opening the
  level from a trial definition is exactly what it was waiting for, and it is applied at creation
  through Fantasy's clock config.
