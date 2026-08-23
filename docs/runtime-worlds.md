# Runtime worlds — phase 0 spike

Findings for [#119](https://github.com/DawnTeamMC/Mubble/issues/119). Target: Minecraft `26.2`,
Fabric loader `0.19.3`, Fabric API `0.158.0+26.2`.

> Originally written against `26.2-snapshot-3`. Every claim below was re-checked against the `26.2`
> release: the `ServerLevel` constructor, the two widened fields, the mixin target and its bytecode
> offsets, the client respawn path and the `LEVEL_STEM` consumer set are all unchanged. Two findings
> in section 6 did change and are marked.

> **Note on placement.** `docs/` is the published Sinytra wiki root (`build.gradle` → `wiki.docs.mubble`).
> This is an internal engineering note, not player documentation. It is here because the issue asked for
> this exact path; it is not listed in `docs/_meta.json`, so it should not appear in the wiki navigation.
> `design/` or a `docs/dev/` subfolder would be a better home — say the word and I will move it.

## Summary

| Question | Answer |
|---|---|
| Can we create, tick, enter and destroy a `ServerLevel` at runtime with no restart? | **Yes.** No blockers found. |
| Does the client tolerate a brand-new dimension id? | **Yes**, provided the *dimension type* is one it already knows. Verified by reading the packet handler, not assumed. |
| Fantasy, or our own? | **Fantasy.** Reasoning below. (The spike first recommended our own on release-cadence grounds; that argument turned out not to apply — see §3.) |

Everything below marked **(read)** was verified by reading the decompiled sources for this exact
version. Everything marked **(built)** is verified by the compiler or by a Gradle check.
Everything marked **(unverified)** needs a human in-game — see [Manual test](#manual-test).

---

## 1. Creating a level at runtime

Vanilla builds its levels once, in `MinecraftServer.createLevels()`, and never adds another. It does
not, however, *prevent* another. **(read)**

> Fantasy does all of this for us now (§3). This section stays because it is what to re-derive if
> Fantasy ever has to be debugged or replaced, and because §2's constraint is ours to respect
> regardless of who creates the level.

The recipe is a direct copy of what `createLevels` does for the nether and the end:

```java
new ServerLevel(
    server,
    Util.backgroundExecutor(),        // exactly what MinecraftServer.executor is assigned
    server.storageSource,
    new DerivedLevelData(server.getWorldData(), server.getWorldData().overworldData()),
    dimensionKey,
    levelStem,
    false,                            // isDebug
    BiomeManager.obfuscateSeed(seed),
    List.of(),                        // custom spawners
    false                             // tickTime
);
```

Four things make this work, and each was a place it could have failed.

**The constructor is public and takes the `LevelStem` directly.** It does not look the stem up in a
registry. **(read)**

**Nothing reads the `LEVEL_STEM` registry after boot.** Grepping every consumer of
`Registries.LEVEL_STEM` across the decompiled sources turns up world creation screens, the world
upgrader, `WorldGenSettings.of`, and `createLevels` — all boot-time or UI. **(read)**

So we do not register a stem at all. This is worth dwelling on, because it is where Fantasy has to
work hardest: it *does* register the stem (thawing the frozen registry to do so), and then needs a
mixin to stop that registration being persisted. By staying out of the registry we get the same
result with no mixin and no thaw, and a runtime level can never be written into the save and
resurrected on restart. The cost is that `/execute in mubble:voyage/0` will not autocomplete — the
level is real and tickable, just anonymous to the world-config layer.

**Level directories are derived from the dimension key**, via
`DimensionType.getStorageFolder` → `<world>/dimensions/<namespace>/<path>/`. **(read)** Runtime levels
are named `mubble:voyage/<n>`, so everything lands under `<world>/dimensions/mubble/voyage/` and
deletion is a single subtree. Orphans from a crash are purged at server start.

**`DerivedLevelData` is safe to share.** Its `setGameTime` and `setGameType` are no-ops and reads
delegate to the overworld — vanilla uses it for the nether and end for exactly this reason. **(read)**
No custom level-data class is needed for the POC.

### The one real hazard

`MinecraftServer.getAllLevels()` returns `this.levels.values()` — a **live view** of a plain
`LinkedHashMap` — and `tickChildren` iterates it directly. **(read)** Adding or removing a level from
inside that iteration is a `ConcurrentModificationException`.

Today's triggers are safe by accident: `tickConnection()` (where command execution and item
right-clicks are handled) sits *after* the level loop closes. Confirmed in bytecode — the
`getAllLevels()` call is at offset 118 and `tickConnection()` at 233 of the same method. **(read)**
But "safe by accident" stops being true the moment phase 3 ends a voyage from a player tick, a
timer, or a death — which it now does, on all three.

**Fantasy already fixes this**, with a `@Redirect` on `tickChildren` that snapshots the collection
before iterating (`SafeIterator`). We do not need our own.

One thing to know about their fix: it carries `require = 0`, so if a future Minecraft version stops
calling `getAllLevels()` there, the redirect silently does not apply and the hazard comes back as an
intermittent crash rather than a load failure. Worth an upstream issue, and worth checking after
every port. A one-line smoke test — open and close a level from inside a player tick — would catch
it.

### Access wideners

None. The bespoke provider needed `MinecraftServer.levels` and `MinecraftServer.storageSource`;
Fantasy ships its own access widener and we touch neither.

---

## 2. Client tolerance of an unknown dimension id

**The client does not validate dimension ids at all.** `ClientPacketListener.handleRespawn` builds
its `ClientLevel` straight from `spawnInfo.dimensionType()` and uses `spawnInfo.dimension()` only as
a label. It never consults `this.levels`, the set it was given at login. **(read)**

On the wire, `CommonPlayerSpawnInfo` reads the dimension id with
`input.readResourceKey(Registries.DIMENSION)` — a bare identifier, no registry lookup, no
validation. **(read)**

The **dimension type** is the part that must already be known: it is sent as
`DimensionType.STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.DIMENSION_TYPE)`, a registry
*reference*. **(read)** Dimension types reach the client during configuration, so a type invented at
runtime could not be named to a client that is already connected.

**This is the constraint that shapes the whole feature**, and it is the reason the issue's
"no new dimension type or biome per trial" acceptance criterion is not merely tidiness — it is
forced. Every runtime level reuses `minecraft:overworld` as its dimension type. Trials differentiate
themselves through environment profiles (phase 1), never by owning a dimension type.

The stale client-side `levels` set has two consequences, both cosmetic: the F3 debug screen and
client-side `/execute in` suggestions won't list voyage dimensions. `ClientSuggestionProvider` and
`DebugScreenEntries` are its only meaningful consumers. **(read)**

---

## 3. Fantasy, or our own?

**Decision: Fantasy** (`xyz.nucleoid:fantasy:0.8.2+26.2`), bundled as a nested jar.

I read its sources rather than judging it by reputation, and it is good. It solves the same problems
in the same places, and in three spots it solves problems this spike had not reached:

- A `tickChildren` redirect that snapshots the level list — the same concurrent-modification hazard
  described in §1, found independently.
- `RuntimeLevel.save()` suppresses saving for temporary levels, so closing does not write chunks it
  is about to delete.
- `RuntimeClockManager` and `RuntimeLevelConfig.setClockTime(clock, time, paused)` — a per-level
  clock. **This is how per-level time works in 26.x**, and it is what phase 1's `fixed_time` needs.
- Per-level `GameRules` via `DelegatingGameRules`, which is roughly what a Ruleset will want later.

Two of the issue's caveats do not hold up:

- **"Purely server-side."** Its `fabric.mod.json` declares `environment: "*"`, and nothing about our
  client needs it, because — per §2 — the client learns about a runtime level purely through vanilla
  packets. It is also LGPLv3, same as Mubble, so bundling is clean.
- **"A reputation for instability."** `0.8.2+26.2` is a current, maintained port that handles the new
  clock system. Whatever the reputation was built on, it is not this version.

### How this decision moved

The spike originally recommended writing our own, on one argument: Mubble tracks Minecraft
*snapshots*, Fantasy ships against releases, and Fantasy's 13 mixins are a wide surface against a
moving target — so a snapshot bump could block Mubble's port on someone else's release.

That argument is void: Hugman is a Fantasy contributor, so the ports are not someone else's to wait
on, and a fork is available if a release ever lags. With the cadence risk gone there is nothing left
on the other side of the scale — Fantasy does strictly more, is already correct on the things that
are easy to get subtly wrong, and deletes code we would otherwise own.

**What this bought us**, concretely, versus the bespoke implementation this spike shipped first:

| | Bespoke (removed) | Fantasy |
|---|---|---|
| Our mixins | 1 (`tickChildren` snapshot) | 0 |
| Our access wideners | 2 fields | 0 |
| Per-level clocks | Not implemented | `setClockTime` |
| Per-level gamerules | Not implemented | `setGameRule` |
| Save-suppression for temp levels | Not implemented (wrote then deleted) | Built in |

The bespoke provider, its mixin and its access widener are gone. If Fantasy ever has to go, the
implementation is recoverable from history and the seam means it is a one-class swap.

**The pooled fallback** the issue describes is not needed and is not implemented. It remains the
right escape hatch and costs one class implementing `VoyageWorldProvider`.

### What we still own

§1 and §2 are not obsolete — they are why the Fantasy integration is shaped the way it is, and they
are what to re-derive if Fantasy ever has to be replaced or debugged. In particular the constraint
in §2 (reuse a known dimension type; never invent one at runtime) is ours to respect: Fantasy will
happily let you register a new dimension type and it would break connected clients.

## 4. The abstraction

`fr.hugman.mubble.world.voyage.level`:

```java
public interface VoyageWorldProvider {
    VoyageWorldHandle open(TrialInstance trial);
    void close(VoyageWorldHandle handle);   // must delete, not leak
}
```

`VoyageWorldHandle` exposes `level()`, `dimension()` and `isOpen()`, and throws if you touch
`level()` after a close. `TrialInstance` carries the definition, the node path and the node seed —
phase 2 filled in what phase 0 left as a placeholder, and the seed moved inside it rather than
travelling as a second argument, so there is no way for two call sites to disagree about it.

The implementation lives in `…voyage.level.fantasy` and is the only code in the mod that names
Fantasy. It is owned by `VoyageSessions` since phase 3, which is what the phase-0 scaffolding said
would happen; the `VoyageWorlds` holder that stood in for it is gone.

### What the implementation guarantees

- **Server thread only.** `open` and `close` throw if called off-thread.
- **Deletes, does not leak.** Close calls `RuntimeLevelHandle.delete()`, which unregisters the level
  and removes its directory.
- **No stranded players.** `close` evacuates anyone still inside to the **overworld's world spawn**
  and logs an error. The caller is supposed to move them first; this is a backstop, not the
  mechanism. It deliberately does not reuse the player's current coordinates — a trial platform sits
  wherever suited the trial, and copying that position into the overworld drops people inside terrain
  or in mid-air. Restoring the *correct* position belongs to the session, not here.
- **Shutdown cleanup.** `SERVER_STOPPING` closes every open handle before vanilla walks the level map.
- **Void levels.** Fantasy's `VoidChunkGenerator` over `minecraft:the_void`, so a trial starts from
  nothing. The caller builds the platform: `open` returns an empty level, because what a trial
  contains is not this seam's business.
- **Its own clock.** If the trial's environment names a `fixed_time`, the level is created with its
  clock set there and paused. It has to happen at creation: vanilla's clock manager belongs to the
  server, so anything applied afterwards would move every level at once.
- **Its own weather.** Every trial level gets a fresh `WeatherData` and has `advance_weather` turned
  off, so it starts clear, stays whatever its profile asked for, and neither reads nor writes the
  weather everyone else is standing in.
- **No randomness.** Level ids come from a counter, not `UUID.randomUUID()` — the design doc's §6.9
  rule is about voyage reproducibility, but the acceptance criterion is written as an absolute and
  there is no reason to spend the exception here.

---

## 5. Manual test

**(unverified — needs a human in-game.)** I have not launched the game. Everything above is from
reading this version's sources and from the build; the runtime behaviour below is exactly what needs
your eyes.

```
/voyagespike open <trial> [seed]
```

Creates a level, builds the trial's platform, applies its environment and teleports you onto it.
Chat reports the dimension id, the trial name and the node seed. Tab-completion lists the loaded
trials; the testmod ships `mubble-testmod:trial_dawn`, `trial_shifting`, `trial_toxic` and
`trial_plain`.

```
/voyagespike status
/voyagespike close
```

`close` returns you to your exact starting position and destroys the level.

What to check:

1. **F3 shows a new dimension** named `mubble:voyage/<n>` and you are standing on stone in a void.
2. **No crash, no console spam** on entry or exit.
3. **`<world>/dimensions/mubble/voyage/` is empty** after `close`. Open and close ten times; the
   folder must not accumulate.
4. **Two players can each have one open** simultaneously without interfering.
5. **Quit to title while inside**, then load the world again. You come back at the overworld's world
   spawn, because shutdown evacuates you before saving. Landing back where you *started* is a
   property of a voyage session and not of a bare trial level; `/voyagespike open` has no stash, so
   it still cannot do it. See `voyage-sessions.md`.

`VoyageSpikeCommand` is throwaway. Phase 4's `/voyage` replaces it and it should be deleted then.

---

## 6. Things found on the way that change later phases

**Minecraft 26.2 has a built-in environment attribute system.** `net.minecraft.world.attribute`
contains `EnvironmentAttribute`, `EnvironmentAttributeMap`, `EnvironmentAttributeSystem`, and
`EnvironmentAttributeLayer` with `Constant` / `TimeBased` / `Positional` variants. `ServerLevel`'s
constructor ends with:

```java
this.environmentAttributes = EnvironmentAttributeSystem.builder().addDefaultLayers(this).build();
```

and `DimensionType.DIRECT_CODEC` is built over `EnvironmentAttributeMap.CODEC`. **(read)** Mubble
already registers a custom `AttributeType` in `MubbleAttributeTypes`.

This matters a lot for phase 1. The issue specifies environment profiles as a bespoke layer applied
by hooking the render path. There is a strong chance the right implementation is **an extra
`EnvironmentAttributeLayer`** on the existing system instead — which would give per-field
fall-through for free (it is already a layered resolver), cover Tier A and much of Tier B through
one mechanism, and cost far less than a render hook. Fabric API even exposes
`DimensionEvents.MODIFY_ATTRIBUTES` for contributing to a dimension type's attribute map. I have not
designed this yet; flagging it before phase 1 starts, because it may make a chunk of the phase-1
plan unnecessary.

**Weather looks server-global now.** `ServerLevel`'s constructor calls
`this.prepareWeather(server.getWeatherData())`, with `WeatherData` living on the server. **(read)** If
that means weather is no longer per-level, the profile's `weather` field cannot be a Tier B field in
the way the issue assumes. Needs confirming during phase 1.

> **Confirmed, and since fixed in phase 2.** It was global. Levels already keep their own rain and
> thunder *levels*, though, and the shared `WeatherData` is reached through one overridable method,
> so giving trial levels their own was enough — see `WeatherOverridable`.

**`fixed_time` will need a per-level clock.** 26.x routes time through `ServerClockManager` /
`WorldClock`. That is why Fantasy needed `RuntimeClockManager`. See the revisit note in section 3.

**~~There is no testmod module.~~ Resolved.** This was true when the spike was written; `mubble-testmod`
and `mubble-test` landed on `dev` in #115 and are now in `settings.gradle`. Phase 2's content has a
home: hand-written datapack JSON under `mubble-testmod/src/main/resources/data/mubble-testmod/`
(the module deliberately has no data generation), and `mubble-test` carries both unit tests and
gametests.

Note for phase 2: `mubble-testmod` declares its own mod id, so its datapack namespace is
`mubble-testmod`, not `testmod` as the issue's examples write it (`testmod:trial_dawn`). The real ids
will be `mubble-testmod:trial_dawn` unless we want to change the module's mod id.

**The design doc is at `design/story_roguelike.md`**, not `design/design-document.md` as the issue's
Reference section says. Same document.
