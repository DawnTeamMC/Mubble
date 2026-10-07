# 🏃 Mubble: Arcade Movement

An optional movement layer for Mubble, in the spirit of *Super Mario Odyssey*: chained jumps, backflips, long jumps, ground pounds, dives, rolls, wall jumps, ledge grabs and spins, with an orbit camera. It keeps vanilla's hitboxes, collisions and server rules, and a player nothing gives it to plays vanilla.

- Mod id `mubble_arcade`, nested in the Mubble jar. Its content keeps the `mubble` namespace.
- Depends on Mubble's core. Optional: [Controlify](https://modrinth.com/mod/controlify) for controller bindings, its button guide and rumble; [Mod Menu](https://modrinth.com/mod/modmenu) for the settings screen.

## Documentation

- **Players**: the wiki pages in [`docs/arcade_movement`](../docs/arcade_movement), published on the [Modded Minecraft Wiki](https://moddedmc.wiki).
- **Pack makers and developers**: [`docs/reference.md`](docs/reference.md): sources, profiles, moves and their parameters, tags, attributes, animation files, commands and tools.
- **Design**: [`docs/spike.md`](docs/spike.md): what multiplayer required, and why the layer works as it does.

## How it works

- **Moves are code, numbers are data.** Each move (`move/`) is a state of one simulation (`sim/`), shared by the client and the server. Profiles, a dynamic registry, give every move its numbers.
- **Client prediction, server validation.** The client steps the simulation every tick and sends each step with its input. The server (`server/ArcadeValidator`) replays it from the reported start, checks the result, and sends back or corrects what the input cannot explain.
- **Who gets what.** Sources, from items or commands, and unlocks decide, per player, the profile and which moves are available (`access/`).
- **Client side** (`src/client`): input sampled every frame, the orbit camera and its silhouette, JSON keyframe animations played with vanilla's animation system, sounds and particles, the settings, and the Controlify integration (`compat/controlify`).

## Working on it

| Task | Command |
|---|---|
| Play in a dev client | `./gradlew :mubble-testmod:runClient` (add `-Pcontrolify` and `-Pmodmenu` to load those mods) |
| Regenerate data (profiles, tags, translations, guide rules) | `./gradlew :mubble-arcade:runDatagen` |
| Unit tests | `./gradlew :mubble-test:test` |
| Game tests | `./gradlew :mubble-test:runGameTest`, or a subset with `-PgameTestFilter='mubble-gametest:arcade*'` |

The tests of the module live in `mubble-test`, under `arcade` packages. Generated files in `src/generated` are committed: regenerate them rather than editing them by hand.
