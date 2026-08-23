# Environment profiles — phase 1

Notes for [#119](https://github.com/DawnTeamMC/Mubble/issues/119), Minecraft `26.2`.

> Same placement caveat as `runtime-worlds.md`: this is an engineering note living in the published
> wiki root because the issue asked for that path. Not in `_meta.json`, so it should stay out of the
> navigation.

## The short version

**Minecraft 26.2 already has the layered environment system the issue describes.** `net.minecraft.world.attribute`
provides `EnvironmentAttribute`, `EnvironmentAttributeMap` and `EnvironmentAttributeSystem`, and
`ServerLevel`/`ClientLevel` each bake one per level from a stack of layers.

So phase 1 is not "build a resolution stack and hook the render path". It is "add one layer to the
stack vanilla already resolves". The issue's resolution order maps onto it exactly:

| Issue | Vanilla |
|---|---|
| dimension type defaults | `addDimensionLayer` — `dimensionType.attributes()` |
| ← biome effects | `addBiomeLayer` — positional, per biome |
| *(not in the issue)* | timeline and weather layers |
| ← environment profile | **our constant layer** |
| ← per-instance override | **our constant layer, applied after** |

Per-field fall-through is inherent: a layer only touches the attributes it names.

This is a **deviation from the issue's plan**, and a large one — it removes the bespoke resolver, the
bespoke Tier A/Tier B split, and the render-path hook. What it costs is the issue's exact JSON
schema, which is the one thing that could not survive; see [Schema](#schema).

## What a profile is

```java
public record EnvironmentProfile(
        EnvironmentAttributeMap attributes,   // vanilla's own type
        Optional<Integer> fixedTime,          // ours
        Optional<WeatherState> weather        // ours
)
```

`attributes` carries everything vanilla can express. The other two exist because vanilla does not
model them as attributes at all — see [Tier B](#tier-b-what-actually-works).

Registered as a synced dynamic registry next to power-ups, using the loader's supported mechanism
rather than a bespoke packet:

```java
DynamicRegistries.registerSynced(MubbleRegistries.ENVIRONMENT_PROFILE,
        EnvironmentProfile.DIRECT_CODEC, EnvironmentProfile.NETWORK_CODEC);
```

The issue asks for "a network codec distinct from its file codec if the two ever diverge". They
diverge immediately: `NETWORK_CODEC` carries the attributes and nothing else, drops any attribute
vanilla marks non-syncable, and (since phase 2) drops every seed-resolved candidate list. There are
unit tests asserting none of those cross the wire.

**The two must keep the same shape, though.** Registry sync does not promise both ends use the same
codec: an entry read from disk with the file codec can be handed to the network codec. The first
version of this made `NETWORK_CODEC` a bare attribute map, which read the whole profile object as a
map of attribute ids and crashed the client on join with

```
Unknown registry key in ResourceKey[minecraft:root / minecraft:environment_attribute]: minecraft:attributes
```

— and in the other direction silently decoded to an empty profile, because every field is optional.
So both codecs read and write an object with an `attributes` field, and differ only in which fields
they carry. Two tests cover the crossings, and they assert the attributes *survive* rather than just
that parsing succeeded.

## Schema

```json
// data/<namespace>/environment_profile/<id>.json
{
  "attributes": {
    "visual/sky_color": "#ffa120",
    "visual/fog_color": "#ffb574",
    "visual/fog_start_distance": 40.0,
    "visual/fog_end_distance": 160.0,
    "gameplay/sky_light_level": 12.0
  },
  "fixed_time": 23000,
  "weather": "clear"
}
```

**This is not the schema the issue specifies**, in three ways. All three are vanilla's format, not
choices I made, and aliasing them back to the issue's spelling would mean inventing a parallel
naming scheme for engine ids — the exact thing the issue says not to do.

1. **Attributes are namespaced and categorised.** `visual/sky_color`, not `sky_color`. The
   `minecraft:` prefix is optional; the `visual/` or `gameplay/` part is not.
2. **Colours are hex strings.** `"#ffa120"`, not `16752928`. `sky_color` is an `RGB_COLOR`, so alpha
   is forced opaque on read — `#ffa120` comes back as `0xFFFFA120`. Writing a bare integer with a
   zero alpha byte and expecting it back is the one trap here, and there is a round-trip test
   pinning it.
3. **`fog_density` does not exist.** Fog is a start and an end distance. `env_toxic` gets its heavy
   fog from `fog_start_distance: 2.0, fog_end_distance: 28.0`.

In exchange, profiles get things the issue's schema could not express: the full 48-attribute
vocabulary, and modifiers beyond plain override —

```json
{ "attributes": { "visual/fog_color": { "modifier": "multiply", "argument": 0.5 } } }
```

An empty file (`{}`) is a valid no-op profile.

Since phase 2, any attribute may also name a list of seed-resolved candidates in place of a value.
That is documented in `trials-and-voyages.md`, because what it resolves against belongs to a trial.

## Where the layer goes in

`EnvironmentOverridable` is injected onto `Level` (same mechanism as `PowerUpHolder` on `Player`),
with `setEnvironmentOverrides(List<EnvironmentAttributeMap>)` rebuilding that level's attribute
system with the extra layers appended. Rebuilding is cheap and happens on trial entry, trial exit
and `/reload` — never per tick.

**Server** (`ServerLevelMixin`): no injection at all. `ServerLevel` already exposes
`setEnvironmentAttributes`, so the mixin only carries state. That method is `@VisibleForTesting`
(vanilla uses it to swap environments per gametest), so if Mojang removes it we get a compile error
rather than a class-load failure.

**Client** (`ClientLevelMixin`): `ClientLevel.environmentAttributes` is `private final` with no
setter, so this one injects into `environmentAttributes()`. Two details worth knowing:

- The inject uses the **full descriptor**
  (`environmentAttributes()Lnet/minecraft/world/attribute/EnvironmentAttributeSystem;`). There is a
  covariant bridge overload returning `EnvironmentAttributeReader`, and a bare method name matches
  both, which would run the handler twice.
- The rebuild reuses `ClientLevel.addEnvironmentAttributeLayers` rather than `addDefaultLayers`, so
  the two layers `ClientLevel` adds for lightning flashes survive inside a voyage.

Overriding the accessor means **the whole render path is covered by one hook** — sky, fog, clouds,
light tint, ambient particles, ambient sound all read back through it — and anything vanilla adds
later comes along for free. No per-renderer patching, and no "degraded visuals" path anywhere.

## Sync

Three requirements from the issue, and how each is met:

**1. A server-only profile must work.** Met by `registerSynced`: the registry is sent during the
configuration phase, so a profile that exists only in a server datapack reaches the client as data.
Nothing about the client needs the file.

**2. `/reload` must resync.** *Not* met by the loader — this is a real gap, not an oversight in the
issue. Fabric's registry sync is a `SyncConfigurationTask`; it runs once, during configuration, and
there is no vanilla or loader path to re-send a dynamic registry to a client that is already
playing. So `EnvironmentProfileSyncPayload` re-sends the (network-filtered) profile registry on
`END_DATA_PACK_RELOAD`, and the client keeps those in `ClientEnvironmentProfiles`, which wins over
the synced registry. Every active profile is then re-applied and re-sent, so the visuals change
without a reconnect.

That store is cleared on disconnect, so a second server cannot inherit the first one's profiles.

**3. An unknown profile id is a hard error.** The `ActiveEnvironment` payload carries the profile as
a **bare `Identifier`, not a registry reference**, specifically so this can fail well: a reference
the client cannot resolve blows up inside the packet decoder and disconnects the player with
something unreadable, whereas an id lets the client name the profile it was never sent. It logs at
`ERROR` and leaves the sky alone — no default, no fallback.

**Generalising.** This is the first datapack registry whose client-visible slice has to survive a
reload; constellations, worlds, quests, trials and arenas all follow. The reload payload is
deliberately one small class rather than a framework — when the second one arrives, that is the
moment to generalise it, not before.

## Tier B: what actually works

The issue asks for an honest account of which Tier B fields the profile layer genuinely controls.

| Field | Status |
|---|---|
| `gameplay/sky_light_level` | **Real.** An attribute; applied on both sides, so lighting and mob spawning see it. `updateSkyBrightness()` is called on apply so it takes effect immediately. |
| `visual/ambient_light_color`, `visual/block_light_tint`, `visual/sky_light_color` | **Real**, client-side rendering. |
| Gameplay booleans (`monsters_burn`, `piglins_zombify`, `water_evaporates`, …) | **Real**, server-side, and free — they are attributes like any other. The issue did not ask for these; they arrive with the vocabulary. |
| `fixed_time` | **Real since phase 2**, with one limit. Not an attribute, and `ServerClockManager` hangs off the **server**, not the level — `/time` moves every level at once. Fantasy gives a runtime level its own clock manager, so `RuntimeLevelConfig.setClockTime(clock, time, paused)` at creation is the only per-trial way to set one. That is where it is applied, from `TrialDefinition#fixedTime`. The limit follows from the mechanism: a profile applied to a level that already exists cannot change its time, so `/voyagespike environment` never will. |
| `weather` | **Real since phase 2.** Not an attribute, and `WeatherData` is one object on the server that every level reads through `ServerLevel#getWeatherData`. Trial levels are handed their own, so a trial is isolated in both directions: its storm does not rain on someone's Overworld build, and `/weather` outside cannot cancel it. Applied to a level that does *not* own its weather, the field is refused with a warning rather than written to the server's — that is `/voyagespike environment` in the Overworld. See `trials-and-voyages.md`. |
| ceiling / skylight | **Not available.** `hasCeiling` and `hasSkyLight` are `DimensionType` fields, not attributes, and the dimension type is shared across all trials by necessity (see `runtime-worlds.md` §2). A trial cannot change them without owning a dimension type, which would break connected clients. |

The short version: **Tier A and Tier B are both fully real**, and mostly for free — they are all just
attributes. The two fields that are genuinely not attributes, `fixed_time` and `weather`, were the
last two to work, and both needed the level to be opened by a trial rather than merely decorated by a
profile. Both landed in phase 2, and both carry the same limit as a result: they apply to a level
being created, not to one already running.

## Manual test

**(unverified — needs a human in-game.)** The unit tests cover the codecs; the visuals need eyes.

The testmod ships four profiles: `mubble-testmod:env_dawn`, `env_toxic`, `env_shifting` and
`env_empty`.

> Note the namespace: the module's mod id is `mubble-testmod`, so ids are `mubble-testmod:env_dawn`,
> not `testmod:env_dawn` as the issue writes them.

```
/voyagespike environment mubble-testmod:env_dawn
/voyagespike environment mubble-testmod:env_toxic
/voyagespike environment clear
```

Applies to whatever level you are standing in, so it works in the Overworld — you do not need a
voyage level to see it.

1. **The three profiles look obviously different.** Dawn is warm orange with long fog; toxic is green
   with fog closing in at 28 blocks; shifting is red sky over near-black fog.
2. **`env_empty` changes nothing.** It is a valid profile that names no attributes, so every field
   falls through to vanilla.
3. **`/reload` updates a connected client.** Edit a colour in
   `mubble-testmod/src/main/resources/data/mubble-testmod/mubble/environment_profile/env_dawn.json`,
   run `/reload`, and the sky should change without reconnecting.
4. **Server-only profile.** Put a profile in a datapack in the server's `world/datapacks/` only, with
   the client having never seen the file, and apply it. It should render.
5. **Unknown id is loud.** `/voyagespike environment mubble:nope` should log an error naming the
   profile and leave the sky untouched.

## Still open

- **Seed-resolved candidate lists** — **done in phase 2**, and it landed where this note predicted:
  our own codec wrapping the dispatched map, with the resolved result riding in the `overrides` half
  of the `ActiveEnvironment` payload. See `trials-and-voyages.md`.
- **`fixed_time`** — **done in phase 2**, wired through Fantasy's clock config at level creation, as
  this note predicted. It only applies to a level being opened, never to one already running.

- **`weather`** — **done in phase 2**, per-level rather than dropped. Nothing in a profile leaks out
  of a trial any more.
