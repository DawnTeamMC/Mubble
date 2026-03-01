# Copilot Instructions for Mubble

## Project Overview

Mubble is a Fabric Minecraft mod that adds pop-culture content to Minecraft, primarily focused on Nintendo licenses (e.g., Super Mario). It is developed in Java using the Fabric mod framework.

- **Mod ID:** `mubble`
- **Root package:** `fr.hugman.mubble`
- **Build system:** Gradle with Fabric Loom
- **Minecraft version:** See `gradle.properties` for the current `minecraft_version`

## Repository Structure

The project is split into multiple Gradle submodules:

- **`mubble-core`** — Core shared logic: power-up framework, collectible entities, item components, network payloads, commands, registries
- **`mubble-super_mario`** — Super Mario themed content built on top of `mubble-core`
- **`mubble-bom`** — Bill of Materials for version management

Each submodule follows a split source set layout provided by Fabric Loom:

- `src/main/java` — Common (server + client) code
- `src/client/java` — Client-only code (renderers, HUD, keybinds, GUI)
- `src/datagen/java` — Data generation code

## Coding Conventions

- **Java version:** Java 25 (compiler target set in `build.gradle`)
- **Naming:** Follow standard Java naming conventions; use PascalCase for classes and camelCase for methods/fields
- **Package layout:** `fr.hugman.mubble.<module>.<domain>` (e.g., `fr.hugman.mubble.super_mario.powerup`)
- **Registry pattern:** Register all game objects (items, entities, components, etc.) via dedicated registry classes
- **Mixins:** Place Minecraft integration code in `*Mixin.java` files in the appropriate `mixin` package; annotate with `@Mixin`
- **Client/server separation:** Never reference client-only classes from common code; use the `src/client/java` source set for client-side logic
- **Data generation:** Add data providers to `src/datagen/java`; run the `datagen` Gradle task to regenerate resources

## Key Frameworks and APIs

- **Fabric API** — Used extensively; prefer Fabric events and hooks over mixins where possible
- **Dawn API** — Internal Dawn Team library; provides base classes and utilities used across Dawn Team mods
- **Logging:** Use `LogManager.getLogger()` with the mod ID `"mubble"` as the logger name

## Building and Testing

```bash
# Build the mod
./gradlew build

# Run the data generation
./gradlew runDatagen

# Check for duplicate files across nested JARs (part of the `check` task)
./gradlew check
```

> **Note:** The project currently targets a snapshot Minecraft version. Ensure JDK 25 is available to match the compiler target.

## Contributing Guidelines

- Follow the conventions in `CONTRIBUTING.md`
- Translations are managed via [Crowdin](https://crowdin.com/project/dawnteam) — do not edit `en_us.json` translation files directly for translation PRs
- Pull requests should reference the relevant issue(s) and include an overview of changes
- Squash commits before submitting a PR
- By submitting code, you agree to the Contributor License Agreement described in `CONTRIBUTING.md`
