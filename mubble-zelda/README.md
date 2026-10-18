# 🗡️ Mubble: The Legend of Zelda

Content from the Zelda licence, in a module of its own.

- Mod id `zelda`, nested in the Mubble jar. Its content lives in the `zelda` namespace.
- Depends on Mubble's core.

## Working on it

| Task | Command |
|---|---|
| Play in a dev client | `./gradlew :mubble-testmod:runClient` |
| Regenerate data | `./gradlew :mubble-zelda:runDatagen` |
| Unit tests | `./gradlew :mubble-test:test` |
| Game tests | `./gradlew :mubble-test:runGameTest`, or a subset with `-PgameTestFilter='mubble-gametest:zelda*'` |

The tests of the module live in `mubble-test`, under `zelda` packages. Generated files in
`src/generated` are committed: regenerate them rather than editing them by hand.
