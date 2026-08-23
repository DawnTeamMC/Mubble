# Voyage sessions — phase 3

Notes for [#119](https://github.com/DawnTeamMC/Mubble/issues/119), Minecraft `26.2`.

> Same placement caveat as the others: an engineering note living in the published wiki root because
> the issue asked for that path. Not in `_meta.json`, so it should stay out of the navigation.

## What phase 3 added

A voyage you can actually run. `VoyageSessions` owns everything about a run in progress: who is in
one, opening a level per trial and deleting it on the way out, and — the part that matters — taking
a player's belongings away and giving them back.

It also absorbed the phase-0 `VoyageWorlds` holder, which is what that class's own javadoc said
would happen once this existed.

```
/voyagespike run <voyage> [seed]   start one
/voyagespike quit                  end it as an abandonment
```

Both are stand-ins. Phase 4's `/voyage start` / `abandon` / `status` replaces them and deletes the
spike command.

## The stash

On entry the player's inventory (armour and offhand included), held slot, effects, attribute
modifiers, game mode, health, hunger, saturation, total experience, and exact position, dimension and
rotation go into a `PlayerStash`. Then the player is emptied.

**The ender chest is deliberately untouched.** It is not in the level being deleted and nothing about
a voyage reads it, so stashing it would only add a way to lose it.

Two details that are not obvious:

- **Experience is stored as the total, not as level plus progress.** The three have to agree, and the
  total is the only one of them that is authoritative. Restoring sets the level and progress to zero
  and re-awards the total, which reconstructs the other two exactly.
- **The inventory is walked by container size, not by 36.** `Inventory#getItem` maps slots 36–42 onto
  the equipment, so armour, offhand, body and saddle come along without being special-cased. Walking
  only the main slots would lose someone's armour silently, which is why there is a test that puts a
  chestplate in slot 38 and asserts it comes back.

## Getting out

Five endings, all through one method:

| | |
|---|---|
| completing the last trial | ADVANCE on the final trial |
| failing | FAIL item, or dying inside a trial |
| abandoning | `/voyagespike quit`, later `/voyage abandon` |
| disconnecting | logging out mid-voyage |
| the server stopping | clean shutdown |

A sixth — the server **crashing** — cannot run any code at all, and is the entire reason the stash
is written to disk rather than kept in a map.

### Why disconnect and shutdown do not consume the stash

They restore the player and then deliberately **leave the stash in place** for the next login to
consume. Restoring twice is harmless, because the second one writes the same values. Consuming the
stash before being certain the player's own data was written is how an inventory disappears.

This makes the recovery correct without having to be right about save ordering:

- if the disconnect-time restore *was* saved, the player logs in where they belong and the second
  restore changes nothing;
- if it was *not*, the player logs in inside a dimension that no longer exists, and the second
  restore is the one that saves them.

Worth knowing about that second case, because it is not hypothetical. `PrepareSpawnTask` resolves a
saved dimension with `loadedPosition.dimension().map(server::getLevel)`, and `getLevel` returns null
for a deleted one, so the `Optional` collapses and the overworld fallback fires — but the saved
*position* is used as-is. A crash mid-voyage therefore drops the player into the overworld **at the
trial's coordinates**, which could be inside terrain or in mid-air. The join-time restore is what
fixes that, so it is load-bearing rather than a nicety.

### What a restart does not do

It does not resume a voyage. Trial levels are temporary and are deleted on shutdown, so a saved
session would point at nothing. `VoyageSessionData` therefore holds stashes and nothing else, and a
restart *ends* voyages in progress — which is exactly what the acceptance criterion asks for
("leaves the player safe in the Overworld with inventory intact").

### One limitation, stated rather than hidden

The stash holds item stacks and effects, whose stored form changes between Minecraft versions, and no
data fixer knows this layout. Fixes only run when a world is opened on a newer version than it was
saved with, so the exposure is narrow: upgrading Minecraft with a voyage in progress. Finish or
abandon voyages before upgrading. Inventing a fixer schema for a POC would cost more than it is
worth, but this should be revisited before anything ships.

## Control items

An emerald called *Complete Trial* and a redstone dust called *Forfeit Voyage*, handed out at the
start of each trial and stripped on the way out.

They are **ordinary vanilla items with a data component marker**. Nothing subclasses `Item` and
nothing is registered, so the gate is the marker and only the marker — an emerald from anywhere else
is inert. There is a test that puts a plain emerald in the inventory alongside them and asserts that
stripping the control items leaves it alone.

Stripping sweeps the whole inventory rather than the two slots they were placed in, because a player
can move them, and a control item that outlives its voyage is an item that ends somebody else's.

These are a placeholder for real objectives. Phase 2 left objectives deliberately unbuilt, so "the
player decides when the trial is over" is the stand-in.

## Seeding

`VoyageSeeds.random()` is the only place in any of this that draws a random number, and it is not a
roll *inside* a voyage — it is the choice of which voyage to run. Once it returns, the whole run is
determined, which is what makes the number worth sharing. A player who supplies their own seed never
reaches it.

Everything downstream is the derivation phase 2 built: `node_seed = hash(voyage_seed, node_path)`,
then a sub-seed per attribute.

## Acceptance criteria

| Criterion | State |
|---|---|
| No live `random()` in voyage code paths | **Met.** The one draw is the initial seed, in a method that says so. |
| Stash and restore everything listed | **Met**, covered by a unit test for the on-disk form and a game test for reading it off a real player and putting it back. Game mode is the one field with no test — the game test framework's mock player hard-codes `gameMode()` to creative, so a round trip through it would prove nothing. |
| Survives a server restart mid-voyage | **Met** by design; **(unverified — needs a human in-game)** end to end. |
| Logging out mid-voyage and back in | Same. |
| Control items gate on the marker | **Met**, with the negative test being the point. |
| Chat feedback | **Met.** "Trial 2 of 3: Void Platform", "Voyage complete." |

## Manual test

**(unverified — needs a human in-game.)** The parts a test cannot reach are the ones involving a real
client disconnecting and a real server stopping.

```
/voyagespike run mubble-testmod:voyage_poc
```

1. **You lose everything on entry and get it back on exit.** Go in carrying a full inventory, armour,
   an effect and some levels. Right-click the emerald three times to finish all three trials. Your
   position, facing, inventory, armour, effect, health, hunger and XP should all be exactly as they
   were.
2. **Each trial looks different**, and the chat says which one you are on.
3. **Forfeiting works.** Start again, right-click the redstone, and you should land back where you
   started with everything intact and "Voyage lost."
4. **Dying works.** Start again and die in a trial. The voyage ends as a loss and you are put back
   where you started the voyage from, not at your bed.
5. **Logging out mid-voyage.** Quit to title inside a trial, load the world again. You should be back
   at your starting position with your inventory. Check the log for "Restoring … from an unfinished
   voyage".
6. **A crash mid-voyage.** Kill the process rather than quitting. On restart the stash on disk should
   still put you right. This is the one that exercises the join-time recovery on its own.
7. **The control items do not escape.** After any ending, you should have no emerald called *Complete
   Trial* anywhere, and any ordinary emeralds you were carrying should be untouched.
8. **`<world>/dimensions/mubble/voyage/` is empty** afterwards, including after the crash test.

## Still open

- **Phase 4** replaces `/voyagespike run|quit` with `/voyage start|abandon|status`, including
  generating and echoing an omitted seed, and refusing to start a second voyage.
- **Phase 5** hands out `completion_rewards`, which the definition has carried since phase 2 and
  nothing reads yet.
- **Objectives** replace the control items. That is the trial's own business, and phase 2 left the
  seam on `TrialDefinition`.
- **Multiplayer sharing.** Sessions are per player, so two players run separate voyages in separate
  levels. Whether a voyage is ever a party activity is a design question, not a gap.
