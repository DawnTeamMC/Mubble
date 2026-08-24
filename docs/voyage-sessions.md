# Voyage sessions, the command and rewards — phases 3 to 5

Notes for [#119](https://github.com/DawnTeamMC/Mubble/issues/119), Minecraft `26.2`.

> Same placement caveat as the others: an engineering note living in the published wiki root because
> the issue asked for that path. Not in `_meta.json`, so it should stay out of the navigation.

## What phase 3 added

A voyage you can actually run. `VoyageSessions` owns everything about a run in progress: who is in
one, opening a level per trial and deleting it on the way out, and — the part that matters — taking
a player's belongings away and giving them back.

It also absorbed the phase-0 `VoyageWorlds` holder, which is what that class's own javadoc said
would happen once this existed.

Phase 4 then put the only player-facing surface on it, and deleted the spike command that had stood
in for one since phase 0:

```
/voyage start <voyage_id> [seed]
/voyage abandon
/voyage status
```

## The stash is the player's whole save tag

`PlayerStash` holds `saveWithoutId` — the same serialisation the game uses to write a player to disk
— plus the return dimension, position and rotation.

**It started as a list of fields** (inventory, effects, attributes, hunger, …) and that was wrong by
construction: it restores exactly the list somebody thought of and silently ignores everything else.
Mubble's own power-up walked straight through a voyage that way. Any other mod's player state would
have too, which is the real problem — a POC that only survives contact with its own mod is not much
of a POC.

Using the game's own serialisation means anything persisted through `addAdditionalSaveData`, the
normal way to do it, comes along without this class knowing the mod exists.

### Clearing cannot be generic, and that is the honest limit

Restoring works from a saved tag, so it covers whatever was in it. **Clearing has to know what to
reset**, and loading a stripped tag does not do it: the usual way to read persisted state is

```java
input.read(key, CODEC).ifPresent(value -> …)
```

so an *absent* key leaves the field exactly as it was. Mubble's power-up reads that way and most mods
will too.

So `clear()` resets vanilla's player state and Mubble's, and a third party's state will survive into
a trial. It is still put back correctly on the way out, which is the direction that matters: nothing
gained inside a voyage escapes it.

That is also why **restoring clears first and loads second**. Loading alone would not undo a power-up
picked up *during* a voyage — the snapshot has no key for one, and the reader ignores absent keys.
Clearing first means the snapshot lands on a blank player instead of being layered over whatever the
voyage left behind. There is a test for each direction.

### The ender chest, which took two goes

It should be untouched: it is not in the level being deleted, and rolling it back would delete
anything the player put in during a voyage.

Dropping its key from the snapshot is **not** enough on its own. `Player` reads it with
`listOrEmpty`, so an absent key *empties the chest* rather than leaving it alone — the first attempt
at this deleted it outright, and a test caught it. The key is dropped on the way in and the player's
current contents are written back into the tag on the way out, which is the only combination that
actually leaves it be.

## Getting out

Five endings, all through one method:

| | |
|---|---|
| completing the last trial | ADVANCE on the final trial |
| failing | FAIL item, or dying inside a trial |
| abandoning | `/voyage abandon` |
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

## The command

`VoyageCommand` is thin on purpose: every branch is one call into `VoyageSessions`. What it owns is
turning a refusal into a sentence somebody can act on.

- **`start`** checks "already in a voyage" *before* looking the id up, so a player who is mid-voyage
  is told that rather than being told the id they fat-fingered does not exist.
- **An omitted seed is generated and reported.** The session already announces the seed on entry,
  which is the whole point — a seed a player cannot read is a seed they cannot share. There is a test
  asserting the generated one actually reaches them.
- **An unknown id names itself** in the refusal, and tab-completion lists the ones that exist.
- **`abandon` is a loss**: same restore, no rewards. It says something different in chat, which is
  the only difference.
- **`status`** reports the voyage, the trial index and the active seed.

### Where a permission level would go

There is none, per the issue. If one is ever wanted it goes on the root literal as
`.requires(Commands.hasPermission(…))` — but note that gating the root would also hide `status` and
`abandon` from a player already inside a voyage, which is the wrong shape. It belongs on `start`
alone.

## Rewards

Granted on completion only, and only once the player is back in their own world with their own
inventory. Losing, forfeiting and abandoning pay nothing, which is what makes finishing worth
anything.

**The ordering is the part worth stating.** Rewards go out *after* the stash is restored. Granting
them first would put them into an inventory the restore then overwrites, and they would vanish with
no error anywhere — a silent failure that only shows up as "I finished and got nothing". There is a
test that finishes with the player's own sword in slot 20 and asserts both the sword and the carrots
survive.

Anything that will not fit is dropped at the player's feet with no pickup delay, aimed at them. Two
notes on that:

- Rewards are split by stack size the way `/give` does, since a reward may name more than one stack's
  worth.
- **Nothing is dropped for a player with infinite materials.** That is vanilla's doing rather than a
  gap: `Inventory#add` deletes an overflow outright for them and reports success. It also means the
  drop path cannot be reached through a game test without taking the ability away first, because the
  framework's mock player is creative — so that test calls `VoyageReward#grantTo` directly and the
  voyage plumbing around it is covered by the other tests.

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
| Stash and restore everything listed | **Met, and then some** — the stash is the player's whole save tag rather than the issue's list, so it covers mod state as well. Covered by a unit test for the on-disk form and game tests for reading it off a real player and putting it back. Game mode is the one field with no test: the framework's mock player hard-codes `gameMode()` to creative, so a round trip through it would prove nothing. |
| Nothing leaks either way | **Out of a voyage: met**, including third-party mod state. **Into a voyage: partial** — see the clearing note above. |
| Survives a server restart mid-voyage | **Met** by design; **(unverified — needs a human in-game)** end to end. |
| Logging out mid-voyage and back in | Same. |
| Control items gate on the marker | **Met**, with the negative test being the point. |
| Chat feedback | **Met.** "Trial 2 of 3: Void Platform", "Voyage complete." |
| Completing pays the reward, after the restore | **Met**, with a test that finishes holding a sword and asserts both the sword and the carrots survive. |
| Losing and abandoning pay nothing | **Met**, covered for the fail item, `abandon`, and abandoning partway. |
| Drop at feet if full | **Met**, tested on the reward directly — vanilla deletes an overflow for a creative player, and the framework's mock player is creative. |

## Manual test

**(unverified — needs a human in-game.)** The parts a test cannot reach are the ones involving a real
client disconnecting and a real server stopping.

```
/voyage start mubble-testmod:voyage_poc
```

1. **You lose everything on entry and get it back on exit, plus a carrot.** Go in carrying a full
   inventory, armour, an effect, a power-up and some levels. Right-click the emerald three times to
   finish all three trials. Your position, facing, inventory, armour, effect, power-up, health,
   hunger and XP should all be exactly as they were, with one carrot added — and none of it should
   have come into the trials with you.
2. **Nothing you pick up inside comes out.** Give yourself a power-up mid-trial and finish the
   voyage; it should be gone.
3. **The ender chest is yours throughout.** Put something in before a voyage and something else in
   during one. Both should be there afterwards.
4. **Each trial looks different**, and the chat says which one you are on. Moving between trials
   should log nothing — an "evacuating to spawn" error there means the old level is being deleted
   before the player has left it.
5. **Forfeiting works, and pays nothing.** Start again, right-click the redstone, and you should
   land back where you started with everything intact, "Voyage lost.", and no carrot.
6. **Dying works.** Start again and die in a trial. The voyage ends as a loss and you are put back
   where you started the voyage from, not at your bed, and with no carrot.
7. **A full inventory keeps the reward.** Fill every slot and finish a voyage in survival; the carrot
   should be lying at your feet rather than gone.
8. **Logging out mid-voyage.** Quit to title inside a trial, load the world again. You should be back
   at your starting position with your inventory. Check the log for "Restoring … from an unfinished
   voyage".
9. **A crash mid-voyage.** Kill the process rather than quitting. On restart the stash on disk should
   still put you right. This is the one that exercises the join-time recovery on its own.
10. **The control items do not escape.** After any ending, you should have no emerald called *Complete
   Trial* anywhere, and any ordinary emeralds you were carrying should be untouched.
11. **`<world>/dimensions/mubble/voyage/` is empty** afterwards, including after the crash test.

## Still open

- **Objectives** replace the control items. That is the trial's own business, and phase 2 left the
  seam on `TrialDefinition`.
- **Clearing third-party mod state on entry.** Restoring already covers it; clearing would need a
  hook mods can register against. Worth doing when there is a second mod to test it with, not before.
- **Multiplayer sharing.** Sessions are per player, so two players run separate voyages in separate
  levels. Whether a voyage is ever a party activity is a design question, not a gap.
