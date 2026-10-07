# Arcade movement — Phase 0 spike

What had to be true for the arcade movement layer to work in multiplayer without fighting vanilla, what was found, and what was decided. Everything marked *proven* is held by an automated test that runs in CI; everything marked 👁 needs a human in a real game.

## The four questions

### (a) Can the server accept a launch at 3× the run speed and a 5 second ledge hang, without pulling the player back or kicking them?

*Proven.* The tests drive a stand-in client through a real `Connection` (an `EmbeddedChannel`) against an actual survival `ServerPlayer` in the player list: every packet goes through vanilla's `handleMovePlayer`, its "moved too quickly" and "moved wrongly" checks, and `tickPlayer`'s floating kick.

- `ArcadeLockstepGameTest.aLaunchAtThreeTimesTheRunSpeedIsNotPulledBack`: the server pushes the running player at 3 × 0.4 b/t (plus 0.5 up) with `/mubble arcade impulse`; the push reaches the client as vanilla's `ClientboundSetEntityMotionPacket`, the client flies at ≥ 1.2 b/t, and the server teleports nobody, rejects nothing and ends where the client ends.
- `aFiveSecondLedgeHangIsNeitherPulledBackNorKicked`: with flight disallowed for that player, as on a dedicated server, the player hangs 6 seconds then climbs, with no teleport, rejection or kick.
- `aClientFloatingWithoutTheLayerIsKicked` is the control: the same setting does kick a vanilla client hovering in the air, so the test above is not passing for want of a kick.
- `aClientLyingAboutItsStepIsPulledBack`: a client reporting 1.5 blocks more than its input allows is sent back, and both sides agree again afterwards.
- `ArcadeGymGameTest`: every gym lane, built by the gym code, run flat out (jumps, rolls, long jumps, ground pounds, spins) with zero rejections and zero corrections.

### (b) Does a jump pressed one frame before landing still happen?

*Proven.* Input is sampled every frame (`Minecraft.runTick`, right after `pollEvents`), not every tick: a press is kept with its age in milliseconds until the tick consumes it, and the jump buffer counts from that age.

- `ArcadeInputCollectorTest`: a press and release between two ticks reaches the next tick; several frames keep the time of the first press.
- `ArcadeMovementGameTest.aJumpPressedOneFrameBeforeLandingStillHappens`: the press arrives on the last airborne tick, and the player jumps on landing.

### (c) Does the camera stay out of walls, and does its crosshair only pick what the player could reach?

*Proven* for the geometry, 👁 for the feel. The camera's collision and its crosshair pick are common code (`ArcadeAim`), so they are tested against real blocks:

- `ArcadeAimGameTest.theCameraStopsShortOfAWallBehindThePlayer`: the whole camera box stays in front of a wall behind the player.
- `theCrosshairOnlyPicksWhatTheEyesCanReach`: a block in reach is picked; a block between the camera and the player is looked past; a block the camera sees over a wall but the eyes do not is not picked; a block out of vanilla reach is not picked.

### (d) Can a backflip be animated with vanilla keyframes and the pose stack, from JSON, and be seen by other players?

*Proven* up to the renderer, 👁 for how it looks.

- `ArcadeAnimationDataTest` reads every shipped animation the way the client does: each names only bones the player model has, keeps its keyframes within its length, and the backflip turns the whole body +360° around X (the triple jump −360°, the side somersault 360° around Z).
- `ArcadeLockstepGameTest.aWholeRoutineStaysInLockstep`: every move the client plays shows in the player's synced `ArcadeVisual` on the server, which is what other clients animate from.
- The limbs go through `KeyframeAnimation` baked against the model's own parts (`HumanoidModel.setupAnim` tail), the body through the pose stack in `AvatarRenderer.setupRotations`. No animation library.

## What vanilla does

Read surgically from the 26.2 sources; these shaped every decision below.

- **Player movement is client-authoritative.** `ServerPlayer` never simulates its own movement: it takes the positions of `ServerboundMovePlayerPacket`, re-runs `move()` from the last good position and checks the result.
  - *Moved too quickly*: the squared distance from the first good position of the tick, minus the server's idea of the velocity, over `100 × packets` (300 gliding). Off for single player owners and with the `player_movement_check` game rule off.
  - *Moved wrongly*: after the server's own `move()`, a horizontal deviation over 0.25 blocks (0.0625 squared). The vertical deviation is always zeroed (`yDist > -0.5 || yDist < 0.5`), so vanilla never checks heights. A wrong move only sends the player back if their old box was free.
  - *Floating*: a client above nothing for more than 80 ticks is kicked, unless the server allows flight. `MinecraftServer.allowFlight()` is `true` by default, including on the game test server; a dedicated server reads `allow-flight`, `false` by default.
  - Leaving the ground upwards makes the server call `jumpFromGround()` itself, which would add vanilla's jump on top.
- **Collisions resolve the vertical axis first**, then the larger horizontal one. A step that should go sideways then down has to be two `move()` calls; and a player walking off an edge is on the ground for that tick, held up by the edge before sliding past it.
- **`onGround` is only refreshed** by a `move()` that moved vertically, or by an entity simulated where it runs.
- **Packet order.** Vanilla packets are queued and handled once per server tick (`PacketProcessor`), but Fabric's custom payloads are handed to `server.execute`, which may run them before or after the move packet that followed them on the wire.
- **Animations.** `KeyframeAnimation.bake` throws on a bone the model lacks; armor models lack most of the player's. `AvatarRenderer.setupRotations` applies the body turn, swimming and elytra rotations.
- **Camera.** `Camera.alignWithEntity` places the third person camera, `getMaxZoom` casts eight rays from the corners of a 0.2 box, measuring from the focus to where a corner hit — which lets the camera itself sit a hair inside the block.

## Decisions

- **Client prediction, server validation, one simulation.** The client steps the shared simulation every tick and moves through vanilla collisions; it sends its input, start, result and a fingerprint of its state. The server plans the same step from the reported start, checks the result axis by axis against what the plan allows (collisions only take away; stepping up is the only thing added; the safety ceiling caps everything; a claimed landing needs something underfoot), then settles its own copy of the state with the result. The client keeps the feel of vanilla — no input delay — and the server never trusts more than the input explains. Server-authoritative movement was ruled out: it would add a round trip to every jump.
- **Determinism.** Trigonometry goes through `Mth`'s tables, vertical motion uses the trapezoidal rule (which also lands jumps exactly on their authored height), and the state has a fingerprint (FNV-1a of its encoding). When fingerprints differ, the server sends its state and the client replays its later steps on top (up to 64 steps). An honest client never needs one: the routine and gym tests assert zero corrections.
- **Vouching.** The vanilla move packet right behind a validated step must land where the step said; the validator then turns vanilla's speed check off for it and keeps its floating flag down. Packets no step announced stay entirely vanilla's.
- **Payload order.** `ServerboundCustomPayloadPacket.handle` reschedules the arcade input onto vanilla's packet queue, so it is always handled right before the move packet sent after it.
- **Two-leg steps.** A step is a first leg and a main leg (`ArcadeBody.move`): snapping down stairs, climbing over a ledge and corner correction move sideways before going down, as vanilla would never do in one call. The validator looks for support under each leg's start.
- **Impulses.** A velocity set from outside the simulation (knockback, explosions, launchers) is taken in by both sides through `ArcadeSimulation.applyImpulse`; an upward one throws the player into a fall so ground friction does not eat it. The server accepts it if it is small, or if it pushed the player about that hard within the last 40 ticks.
- **Vanilla's jump is cancelled** while the layer drives (`jumpFromGround` on both `LivingEntity` and `ServerPlayer`).
- **Profiles** are a synced dynamic registry, which data packs can add to. Dynamic registries do not reload, so a reload listener re-reads the files into a live override that `/reload` updates and sends to every client.
- **Animations** are keyframe JSON read by common code (testable without a client) and baked by the client against each model, leaving out the parts a model lacks. Remote players animate from a synced entity data value (move, sequence number, intensity, profile), so every client agrees on what to play without streaming poses.
- **The orbit camera** is our own, written against `Camera`'s invokers; its geometry is common code. It measures how far each corner of its box travels instead of the distance to the hit, so the box never enters a block.
- **Zero diff when off.** Every hook checks `isDriving()` first. All 191 game tests that existed before the layer pass unchanged.
- **Dependencies.** Controlify 3.5.3 as an optional compile-only dependency, for controller bindings. No animation or camera library.

## Risks

- **High latency.** Corrections replay the steps the client still remembers, at most 64 (3.2 s); a client further behind than that takes the server's state as it is, which shows as a snap.
- **Other movement mods.** A mod moving players in a way the simulation does not know (custom vehicles, grappling hooks, conveyor blocks) will look like an unexplained impulse and get steps rejected. The reason shows in `/mubble arcade inspect`; such cases need either a suspension rule or an impulse the server records.
- **Other renderers.** Mods replacing the player renderer or animating it (emote mods, Figura) may fight over `setupAnim` and `setupRotations`.
- **Fabric networking.** The payload reordering hooks `ServerboundCustomPayloadPacket.handle`; a change in how Fabric dispatches payloads would break the ordering — the lockstep tests would catch it.
- **Controlify's API** is pinned to 3.5.3 for 26.2; a breaking update would only disable the controller bindings, the keyboard path does not depend on it.

## For a human to check 👁

Not self-certified; these need a person in a real client, ideally against a dedicated server with `allow-flight=false` and some latency:

- How every move feels: heights, timings, momentum, the jump chain window, coyote time, the buffers.
- The orbit camera: follow lag, recenter, sensitivity, the field of view kick, first person.
- How the animations look, and that flips turn the right way for others watching.
- Sounds and particles of every move, locally and from other players.
- The Mario Boots' look (tinted leather boots) and tooltip.
- Controller play with Controlify: analog walk and run, the action button, the spin gesture.
- That nothing changes for a player without a source.
