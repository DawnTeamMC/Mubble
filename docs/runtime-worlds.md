# Runtime worlds — phase 0 spike

Findings for [#119](https://github.com/DawnTeamMC/Mubble/issues/119). Target: Minecraft `26.2-snapshot-3`,
Fabric loader `0.19.2`, Fabric API `0.146.1+26.2`.

> **Note on placement.** `docs/` is the published Sinytra wiki root (`build.gradle` → `wiki.docs.mubble`).
> This is an internal engineering note, not player documentation. It is here because the issue asked for
> this exact path; it is not listed in `docs/_meta.json`, so it should not appear in the wiki navigation.
> `design/` or a `docs/dev/` subfolder would be a better home — say the word and I will move it.

## Summary

| Question | Answer |
|---|---|
| Can we create, tick, enter and destroy a `ServerLevel` at runtime with no restart? | **Yes.** No blockers found. |
| Does the client tolerate a brand-new dimension id? | **Yes**, provided the *dimension type* is one it already knows. Verified by reading the packet handler, not assumed. |
| Fantasy, or our own? | **Our own**, ~250 lines in one package. Reasoning below — it is close, and the reasons are about this project's release cadence, not about code quality. |

Everything below marked **(read)** was verified by reading the decompiled sources for this exact
version. Everything marked **(built)** is verified by the compiler or by a Gradle check.
Everything marked **(unverified)** needs a human in-game — see [Manual test](#manual-test).

---

## 1. Creating a level at runtime

Vanilla builds its levels once, in `MinecraftServer.createLevels()`, and never adds another. It does
not, however, *prevent* another. **(read)**

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
timer, or a death.

So `MinecraftServerMixin` redirects that one call and ticks an immutable copy:

```java
@Redirect(method = "tickChildren",
          at = @At(value = "INVOKE",
                   target = "Lnet/minecraft/server/MinecraftServer;getAllLevels()Ljava/lang/Iterable;"))
private Iterable<ServerLevel> mubble$snapshotLevelsBeforeTicking(MinecraftServer server) {
    return ImmutableList.copyOf(server.getAllLevels());
}
```

Cost is one small array per tick. A level added mid-tick starts ticking next tick.

Two deliberate choices here. It targets `getAllLevels()` rather than `Iterable.iterator()` with an
ordinal — `tickChildren` contains exactly one such call **(read)**, so the target is unambiguous and
does not shift if Mojang adds another loop above it. And it does **not** set `require = 0`: if a
future version stops calling `getAllLevels()` there, mixin should fail loudly at load rather than
quietly reintroduce the hazard. (Fantasy uses `require = 0` on the equivalent redirect, which trades
a hard failure for a silent one. On a project that tracks snapshots I would rather have the crash at
load.)

### Access widener

Two fields, in `mubble-core/src/main/resources/mubble.accesswidener`, validated by Gradle's
`validateAccessWidener` task **(built)**:

- `MinecraftServer.levels` — the map we add to and remove from.
- `MinecraftServer.storageSource` — needed by the `ServerLevel` constructor and to locate the
  directory to delete.

`executor` did not need widening: it is assigned `Util.backgroundExecutor()`, which is public. **(read)**

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

Fantasy is a live option, not a dead one — `0.8.2+26.2` was published in June 2026 and is a proper
port, not a stale artifact. I read its sources rather than judging it by reputation.

**It is good.** It solves the same problems in the same places, and in two spots it solves problems I
had not yet reached:

- `SafeIterator` + a `tickChildren` redirect — the same CME hazard, found independently.
- `RuntimeLevel.save()` suppresses saving for temporary levels, so closing does not write chunks it
  is about to delete.
- `RuntimeClockManager` — a per-level clock. **This is how per-level time works in 26.x** and it will
  matter for `fixed_time` in phase 1.
- Per-level `GameRules` via `DelegatingGameRules` — which is roughly what a Ruleset will want later.

**Correcting one of the issue's caveats:** Fantasy is not "purely server-side" in the sense that
would hurt us. Its `fabric.mod.json` declares `environment: "*"`, and nothing about our client needs
it, because — per section 2 — the client learns about a runtime level purely through vanilla packets.
That caveat does not bite. It is also LGPLv3, same as Mubble, so bundling is clean.

**The reason I still recommend our own is release cadence, and only that.** Mubble tracks Minecraft
*snapshots* — the repo is on `26.2-snapshot-3` while Fantasy targets `26.2` release. Fantasy mixes
into `tickChildren`, `ChunkMap`, `ServerChunkCache`, `ServerLevel`, `ServerClockManager` and the
registry internals. That is a wide surface against a moving target, and when it breaks, Mubble's
port is blocked until someone else ships. Our own version needs one mixin and two widened fields,
and when *that* breaks we fix it the same afternoon.

The scoreboard, honestly:

| | Fantasy | Our own |
|---|---|---|
| Time to a working POC | Hours | ~250 lines, done |
| Mixin surface | 13 mixins | 1 |
| Snapshot breakage | Blocks us until upstream ports | We fix it |
| Per-level clocks / gamerules | Already solved | Not yet needed |
| Save-file contamination | Prevented by a mixin | Prevented by not registering |

**This decision should be revisited at phase 1** if `fixed_time` turns out to need a per-level
`ServerClockManager`. Rebuilding `RuntimeClockManager` is a genuinely larger job than everything in
this spike put together, and if we need it, adopting Fantasy wholesale becomes the better trade. The
seam exists so that switch stays a one-class change.

**The pooled fallback** the issue describes is not needed and is not implemented. It remains the
right escape hatch and costs one class implementing `VoyageWorldProvider`; nothing outside
`…voyage.level.runtime` touches the server's level map, so nothing else would change.

---

## 4. The abstraction

`fr.hugman.mubble.world.voyage.level`:

```java
public interface VoyageWorldProvider {
    VoyageWorldHandle open(TrialInstance trial, long seed);
    void close(VoyageWorldHandle handle);   // must delete, not leak
}
```

`VoyageWorldHandle` exposes `level()`, `dimension()` and `isOpen()`, and throws if you touch
`level()` after a close. `TrialInstance` is a phase-0 placeholder carrying an id and a node path;
phase 2 fills it in.

The implementation lives in `…voyage.level.runtime` and is the only code in the mod that touches
`server.levels`.

### What the implementation guarantees

- **Server thread only.** `open` and `close` throw if called off-thread.
- **Deletes, does not leak.** Close removes the map entry, calls `ServerLevel.close()`, and deletes
  the dimension directory.
- **No stranded players.** `close` evacuates anyone still inside to the overworld and logs an error.
  The caller is supposed to move them first; this is a backstop, not the mechanism.
- **Crash cleanup.** Leftover directories are purged at server start, so the world folder does not
  grow after a hard kill.
- **Shutdown cleanup.** `SERVER_STOPPING` closes every open handle before vanilla walks the level map.
- **No randomness.** Level ids come from a counter, not `UUID.randomUUID()` — the design doc's §6.9
  rule is about voyage reproducibility, but the acceptance criterion is written as an absolute and
  there is no reason to spend the exception here.

### Known imperfection

`ServerChunkCache.close()` calls `save(true)` **(read)**, so closing a level writes chunks to disk
milliseconds before we delete the directory. Wasteful, not incorrect. Fantasy avoids it by
overriding `ServerLevel.save()` in a subclass; we can do the same if the write cost shows up, but I
would rather not subclass `ServerLevel` for a POC.

---

## 5. Manual test

**(unverified — needs a human in-game.)** I have not launched the game. Everything above is from
reading this version's sources and from the build; the runtime behaviour below is exactly what needs
your eyes.

```
/voyagespike open [seed]
```

Creates a level, lays a 9×9 stone platform at y=64 and teleports you onto it. Chat reports the
dimension id.

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
5. **Kill the server while inside** (not a clean stop), restart, and confirm the leftover directory
   is gone from the log line `Deleted N orphaned voyage level(s)`. Note that the *player* recovery
   path is phase 3's job, not this spike's — expect to be stranded, that is the bug phase 3 fixes.

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

**`fixed_time` will need a per-level clock.** 26.x routes time through `ServerClockManager` /
`WorldClock`. That is why Fantasy needed `RuntimeClockManager`. See the revisit note in section 3.

**There is no testmod module.** `settings.gradle` includes only `mubble-bom`, `mubble-core` and
`mubble-super_mario`. `mubble-testmod/` and `mubble-test/` exist on disk as build output and
uncommitted, partly-deleted sources, and are in neither the build nor git history. Phase 2 puts
"three trial JSONs, three environment profile JSONs, one voyage JSON" in a testmod that currently
does not exist, so phase 2 starts with reinstating that module. Flagging early because it is a
prerequisite, not a detail.

**The design doc is at `design/story_roguelike.md`**, not `design/design-document.md` as the issue's
Reference section says. Same document.
