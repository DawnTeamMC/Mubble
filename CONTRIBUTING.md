### Submitting translations
Localization of the Dawn Team mods is managed through the [Crowdin](https://crowdin.com/project/dawnteam) project.

### Submitting ideas
You can submit ideas for new features over on the [issue tracker](https://github.com/DawnTeamMC/Mubble/issues).

### Getting started for code modification
We're excited to hear that you're interested in contributing to Mubble!

Before getting started, you'll need to install the latest 64-bit version of the OpenJDK 8 for your environment.
- Windows users: We **strongly** recommend you use the Hotspot OpenJDK 8 builds provided by the [AdoptOpenJDK project](https://adoptopenjdk.net/) instead of the builds provided by Oracle.
- macOS and Linux users: If you are already using a package manager, OpenJDK builds should be present in your software repositories. If not, we recommend using [SDKMan](https://sdkman.io/) to install the Hotspot OpenJDK 8 builds provided by the [AdoptOpenJDK](https://adoptopenjdk.net/) project.

We strongly recommend you use [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/) when making code contributions. While other IDEs may work (in theory, anyway), you will often run into issues and other roadblocks. If you're not familiar with setting up IntelliJ IDEA for use with Fabric projects, the community of Fabric has created a wiki which runs over a lot of the basics of Fabric [here](https://fabricmc.net/wiki/doku.php).

If you have any questions or issues, or would just like to discuss Mubble development, feel free to [join us on Discord](https://discord.gg/8ksTVJu).

### Running the tests
All the tests of the project live in the `mubble-test` module, which is never shipped nor published.
They come in two flavours:

- **Unit tests** (`mubble-test/src/test`) run on plain JUnit 5, with Minecraft bootstrapped by
  [`fabric-loader-junit`](https://docs.fabricmc.net/develop/automatic-testing/unit-tests) but no
  world loaded. Use them for logic that does not need a level.
- **Game tests** (`mubble-test/src/gametest`) run inside a headless Minecraft server through the
  [Fabric Game Test API](https://docs.fabricmc.net/develop/automatic-testing/game-tests). Use them
  for in-game behaviour such as blocks, entities or projectiles. Each test method is annotated with
  `@GameTest` and must be listed (through its class) in `mubble-test/src/gametest/resources/fabric.mod.json`.

```sh
./gradlew runDatagen   # game tests load the generated data pack, so generate it first
./gradlew test         # unit tests
./gradlew runGameTest  # game tests
```

`./gradlew build` runs both suites, but it still needs `runDatagen` to have been run once beforehand,
since a data pack cannot be generated and consumed within the same invocation. This is why the CI
workflow keeps them as two separate steps.

### Creating pull requests
Please make sure before opening a pull request that:

- Your pull request has an overview of the changes it makes, along with a link to the open issue(s) it resolves, if applicable.
- Your changes include appropriate documentation and conform to our style guidelines.
- If your merge request contains multiple commits, that you squash them before submitting.
- You state in the description of your merge request that you agree to the Contributor License Agreement (CLA) found below.

### Contributor License Agreement
By submitting code, assets, or documentation to the repository you are hereby agreeing that:

- You grant Hugman the right to use your contributions under the [LGPL v3.0](https://www.gnu.org/licenses/lgpl-3.0.en.html) license.
- Your contributions are of your own work and are free of legal restrictions (such as patents or copyrights).

If you have any questions about these terms, please get in contact with us.  
**If you do not agree to these terms, please do not submit contributions to this repository.**