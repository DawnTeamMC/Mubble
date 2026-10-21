# 🗡️ Mubble: The Legend of Zelda

Content from the Zelda licence, in a module of its own. So far: the **loftwings** of *Skyward Sword*
([issue #75](https://github.com/DawnTeamMC/Mubble/issues/75)) — giant birds that live above the
clouds, pick their own owner, and carry them.

- Mod id `zelda`, nested in the Mubble jar. Its content lives in the `zelda` namespace.
- Depends on Mubble's core.

## Documentation

- **Players**: the wiki pages in [`docs/loftwings`](../docs/loftwings), published on the
  [Modded Minecraft Wiki](https://moddedmc.wiki).

## How it works

- **The bird is a `TamableAnimal` that tames itself.** `Loftwing` sizes up anyone within eight
  blocks and either takes to them or turns them down for good; the refusals live in its save data,
  capped so the list cannot grow without end. Nothing a player does changes its mind.
- **One flag byte, three shapes.** Standing, flying and panicking each have their own
  `EntityDimensions`, picked in `getDefaultDimensions` and refreshed whenever the flags change.
- **Wild life is goals, riding is `tickRidden`.** Left alone, the bird soars above the cloud level
  and comes down only on an errand (`LoftwingSoarGoal` and friends). Under a rider those goals all
  stand down, and `Loftwing#tickRidden` drives it instead: the jump button takes off, the bird comes
  round to where the rider looks as fast as its helmet allows, and wingbeats pay for height.
- **The rescue blinks, on purpose.** A fall is over in under two seconds and nothing with wings
  crosses a hundred blocks in that time, so a whistled bird flies for half a second and then
  appears under its falling owner and takes them on its back (`Loftwing#catchFalling`). The blink
  is the mechanic rather than a shortcut around it, which is why it is reserved for a fall: called
  to someone standing safely on the ground, the bird flies the whole way. Every number behind a
  call — how far the whistle carries, how long the bird gets — lives in `LoftwingCall`.
- **Numbers are data.** Breeds are a synced dynamic registry (`zelda:loftwing_variant`) with a
  weight that makes the crimson one rare; what a helmet is worth is a data component on the item;
  what a bird catches when it fishes is a loot table.
- **Cloud level comes from the game.** `LoftwingSky` reads vanilla's `CLOUD_HEIGHT` environment
  attribute rather than a constant, so lowering the clouds in a data pack lowers the loftwings too.

## Still to come

The module ships no art: textures, sounds and the entity model are placeholders waiting on real
assets. `LoftwingModel` is a single box marked TODO, and the helmets declare no `EquipmentAsset`
yet, so nothing is drawn on the bird's head.

## Working on it

| Task | Command |
|---|---|
| Play in a dev client | `./gradlew :mubble-testmod:runClient` |
| Regenerate data (recipes, loot, tags, breeds, models, sounds, translations) | `./gradlew :mubble-zelda:runDatagen` |
| Unit tests | `./gradlew :mubble-test:test` |
| Game tests | `./gradlew :mubble-test:runGameTest`, or a subset with `-PgameTestFilter='mubble-gametest:loftwing*'` |

The tests of the module live in `mubble-test`, under `zelda` packages. Everything under
`src/generated` is written by `runDatagen` and is not committed: run it once before building, as
the CI workflow does.
