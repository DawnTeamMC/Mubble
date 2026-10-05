package fr.hugman.mubble.arcade.data;

import fr.hugman.mubble.arcade.registries.ArcadeRegistries;
import static fr.hugman.mubble.arcade.data.CueBuilder.cue;
import static fr.hugman.mubble.arcade.data.MoveBuilder.move;

import fr.hugman.mubble.arcade.references.ArcadeProfileIds;
import fr.hugman.mubble.arcade.ArcadeCameraHints;
import fr.hugman.mubble.arcade.ArcadeCosts;
import fr.hugman.mubble.arcade.ArcadeFallDamage;
import fr.hugman.mubble.arcade.ArcadeGrace;
import fr.hugman.mubble.arcade.ArcadePhysics;
import fr.hugman.mubble.arcade.ArcadeProfile;
import fr.hugman.mubble.arcade.ArcadeValidation;
import fr.hugman.mubble.arcade.InteractionPolicy;
import fr.hugman.mubble.arcade.cue.CueEvent;
import fr.hugman.mubble.arcade.cue.CueShape;
import fr.hugman.mubble.arcade.move.ArcMove;
import fr.hugman.mubble.arcade.move.ArcadeMove;
import fr.hugman.mubble.arcade.move.ArcadeMoves;
import fr.hugman.mubble.arcade.move.BackflipMove;
import fr.hugman.mubble.arcade.move.GroundMove;
import fr.hugman.mubble.arcade.move.JumpMove;
import fr.hugman.mubble.arcade.move.LongJumpMove;
import fr.hugman.mubble.arcade.move.MoveSettings;
import fr.hugman.mubble.arcade.move.SideSomersaultMove;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.sounds.SoundEvents;

/**
 * The profiles shipped with Mubble: {@code mubble:trial} at full strength, and {@code mubble:overworld},
 * slightly softer and charging food for its moves. Every number lives here, and ends up in the
 * generated profile files.
 */
public class ArcadeProfileProvider extends FabricDynamicRegistryProvider {
    public ArcadeProfileProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(ArcadeRegistries.ARCADE_PROFILE));
    }

    @Override
    public String getName() {
        return "Arcade Profiles";
    }

    public static void bootstrap(BootstrapContext<ArcadeProfile> context) {
        context.register(ArcadeProfileIds.TRIAL, trial());
        context.register(ArcadeProfileIds.OVERWORLD, overworld());
    }

    /** The full values, from the design table. */
    public static ArcadeProfile trial() {
        return new ArcadeProfile(
                physics(0.40D, 1.0D, 4.0D),
                grace(),
                InteractionPolicy.COMBAT_ONLY,
                new ArcadeCosts(false, false),
                new ArcadeFallDamage(0.0D, 0.0D),
                ArcadeCameraHints.NEUTRAL,
                ArcadeValidation.DEFAULT,
                moves(false)
        );
    }

    /** The same moves, a slower run, jumps 10% lower, a lower safety ceiling for chunk loading, and food costs. */
    public static ArcadeProfile overworld() {
        return new ArcadeProfile(
                physics(0.34D, 0.9D, 2.0D),
                grace(),
                InteractionPolicy.FULL,
                new ArcadeCosts(true, true),
                new ArcadeFallDamage(1.0D, 3.0D),
                ArcadeCameraHints.NEUTRAL,
                ArcadeValidation.DEFAULT,
                moves(true)
        );
    }

    private static ArcadePhysics physics(double runSpeed, double jumpHeightMultiplier, double safetyCeiling) {
        return new ArcadePhysics(
                new ArcadePhysics.Ground(0.216D, runSpeed, 8.0D, 4.0D, 25.0D, 0.9D, 0.6D, 0.1D, 0.05D),
                new ArcadePhysics.Air(0.99D, 1.0D, 12.0D, 12.0D, 0.03D),
                new ArcadePhysics.Gravity(0.09D, 1.6D, 0.5D, 0.08D, 0.5D, 3.92D, jumpHeightMultiplier),
                new ArcadePhysics.Slope(4, 1.0D),
                new ArcadePhysics.Bounce(4.0D, 10.0D, 7.0D, 0.2D),
                new ArcadePhysics.Effects(0.35D, 0.1D, 0.05D, 0.2D),
                safetyCeiling
        );
    }

    private static ArcadeGrace grace() {
        return new ArcadeGrace(3, 150, 150, 4, 0.3D, 0.5D, 8, 0.35D, 4, 1);
    }

    private static Map<ArcadeMove, MoveSettings> moves(boolean costs) {
        // only the overworld charges food: the trial lists the same moves at no cost
        UnaryOperator<Float> cost = amount -> costs ? amount : 0.0F;
        var dust = cue().surface(5).spread(0.25F).speed(0.04F);
        var landing = cue().surface(2).perSpeed(14.0F).spread(0.35F).speed(0.06F);
        var whoosh = cue().sound(SoundEvents.BREEZE_JUMP, 0.35F, 1.6F).surface(5).spread(0.25F).speed(0.04F);

        List<MoveBuilder> builders = List.of(
                move(ArcadeMoves.WALK),
                move(ArcadeMoves.FALL)
                        .cue(CueEvent.BOOST, cue().sound(SoundEvents.SLIME_JUMP, 0.8F, 1.0F).particle(ParticleTypes.ITEM_SLIME, 10).spread(0.4F).speed(0.1F)),
                move(ArcadeMoves.LAND)
                        .cue(CueEvent.LAND, landing),
                move(ArcadeMoves.RUN)
                        .set(GroundMove.EXHAUSTION_PER_BLOCK, costs ? 0.08D : 0.0D),
                move(ArcadeMoves.SKID)
                        .cue(CueEvent.START, cue().sound(SoundEvents.GRAVEL_STEP, 0.6F, 1.4F))
                        .cue(CueEvent.TICK, cue().surface(2).shape(CueShape.TRAIL).spread(0.2F).speed(0.1F)),
                move(ArcadeMoves.JUMP)
                        .exhaustion(cost.apply(0.05F))
                        .cue(CueEvent.START, whoosh),
                move(ArcadeMoves.DOUBLE_JUMP)
                        .set(ArcMove.HEIGHT, 3.2D).set(ArcMove.TICKS_TO_APEX, 8.0D).set(JumpMove.MIN_SPEED, 0.2D)
                        .exhaustion(cost.apply(0.07F))
                        .cue(CueEvent.START, cue().sound(SoundEvents.BREEZE_JUMP, 0.4F, 1.3F).surface(7).spread(0.3F).speed(0.05F)),
                move(ArcadeMoves.TRIPLE_JUMP)
                        .set(ArcMove.HEIGHT, 4.6D).set(ArcMove.TICKS_TO_APEX, 10.0D).set(ArcMove.VARIABLE, 0.0D).set(JumpMove.MIN_SPEED, 0.7D)
                        .exhaustion(cost.apply(0.1F))
                        .cue(CueEvent.START, cue().sound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.5F, 1.4F).particle(ParticleTypes.CLOUD, 6).spread(0.3F).speed(0.05F)),
                move(ArcadeMoves.CROUCH),
                move(ArcadeMoves.GROUND_POUND)
                        .exhaustion(cost.apply(0.05F))
                        .cue(CueEvent.START, cue().sound(SoundEvents.TRIDENT_RIPTIDE_1, 0.5F, 1.6F)),
                move(ArcadeMoves.GROUND_POUND_LAND)
                        .cue(CueEvent.LAND, cue().sound(SoundEvents.MACE_SMASH_GROUND, 0.7F, 1.2F).particle(ParticleTypes.CLOUD, 16).shape(CueShape.RING).spread(0.6F).speed(0.3F)),
                move(ArcadeMoves.GROUND_POUND_JUMP)
                        .set(ArcMove.HEIGHT, 5.0D).set(ArcMove.TICKS_TO_APEX, 11.0D).set(ArcMove.VARIABLE, 0.0D)
                        .exhaustion(cost.apply(0.1F))
                        .cue(CueEvent.START, cue().sound(SoundEvents.BREEZE_WIND_CHARGE_BURST, 0.6F, 1.2F).particle(ParticleTypes.GUST, 1).spread(0.0F).speed(0.0F)),
                move(ArcadeMoves.LEDGE_GRAB)
                        .cue(CueEvent.START, cue().sound(SoundEvents.LADDER_STEP, 0.6F, 1.0F)),
                move(ArcadeMoves.LEDGE_CLIMB),
                move(ArcadeMoves.WALL_SLIDE)
                        .cue(CueEvent.TICK, cue().surface(1).shape(CueShape.TRAIL).spread(0.1F).speed(0.02F).interval(2)),
                move(ArcadeMoves.WALL_JUMP)
                        .exhaustion(cost.apply(0.1F))
                        .cue(CueEvent.START, cue().sound(SoundEvents.BREEZE_JUMP, 0.4F, 1.4F).particle(ParticleTypes.POOF, 4).spread(0.2F).speed(0.04F)),
                move(ArcadeMoves.ROLL)
                        .exhaustion(cost.apply(0.1F))
                        .cue(CueEvent.START, cue().sound(SoundEvents.ARMADILLO_ROLL, 0.6F, 1.2F).surface(4).spread(0.25F).speed(0.05F))
                        .cue(CueEvent.BOOST, cue().sound(SoundEvents.ARMADILLO_ROLL, 0.6F, 1.5F).particle(ParticleTypes.POOF, 4).shape(CueShape.TRAIL).spread(0.2F).speed(0.05F))
                        .cue(CueEvent.TICK, cue().surface(1).shape(CueShape.TRAIL).spread(0.2F).speed(0.05F).interval(3)),
                move(ArcadeMoves.ROLL_JUMP)
                        .set(ArcMove.HEIGHT, 1.4D).set(ArcMove.TICKS_TO_APEX, 6.0D).set(ArcMove.AIR_CONTROL, 0.6D)
                        .exhaustion(cost.apply(0.1F))
                        .cue(CueEvent.START, whoosh),
                move(ArcadeMoves.LONG_JUMP)
                        .set(ArcMove.HEIGHT, 1.6D).set(ArcMove.TICKS_TO_APEX, 7.0D).set(ArcMove.VARIABLE, 0.0D).set(ArcMove.AIR_CONTROL, 0.6D)
                        .set(LongJumpMove.SPEED, 0.55D).set(LongJumpMove.MIN_SPEED, 0.5D)
                        .exhaustion(cost.apply(0.2F))
                        .cue(CueEvent.START, cue().sound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.4F, 1.1F).surface(6).shape(CueShape.TRAIL).spread(0.3F).speed(0.06F)),
                move(ArcadeMoves.BACKFLIP)
                        .set(ArcMove.HEIGHT, 5.2D).set(ArcMove.TICKS_TO_APEX, 12.0D).set(ArcMove.VARIABLE, 0.0D).set(ArcMove.AIR_CONTROL, 0.5D)
                        .set(BackflipMove.BACK_SPEED, 0.08D).set(BackflipMove.MAX_SPEED, 0.3D)
                        .exhaustion(cost.apply(0.1F))
                        .cue(CueEvent.START, cue().sound(SoundEvents.BREEZE_JUMP, 0.45F, 1.1F).particle(ParticleTypes.CLOUD, 5).spread(0.3F).speed(0.04F)),
                move(ArcadeMoves.SIDE_SOMERSAULT)
                        .set(ArcMove.HEIGHT, 4.6D).set(ArcMove.TICKS_TO_APEX, 11.0D).set(ArcMove.VARIABLE, 0.0D).set(ArcMove.AIR_CONTROL, 0.6D)
                        .set(SideSomersaultMove.SIDE_SPEED, 0.15D)
                        .exhaustion(cost.apply(0.1F))
                        .cue(CueEvent.START, cue().sound(SoundEvents.BREEZE_JUMP, 0.45F, 1.2F).particle(ParticleTypes.CLOUD, 5).spread(0.3F).speed(0.04F)),
                move(ArcadeMoves.DIVE)
                        .exhaustion(cost.apply(0.1F))
                        .cue(CueEvent.START, cue().sound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.5F, 0.8F).particle(ParticleTypes.POOF, 3).shape(CueShape.TRAIL).spread(0.2F).speed(0.03F)),
                move(ArcadeMoves.ROLLOUT)
                        .cue(CueEvent.LAND, cue().sound(SoundEvents.ARMADILLO_ROLL, 0.5F, 1.0F).surface(3).perSpeed(10.0F).spread(0.3F).speed(0.05F)),
                move(ArcadeMoves.SPIN)
                        .exhaustion(cost.apply(0.05F))
                        .cue(CueEvent.START, cue().sound(SoundEvents.TRIDENT_RIPTIDE_1, 0.4F, 2.0F).particle(ParticleTypes.CLOUD, 8).shape(CueShape.RING).spread(0.5F).speed(0.08F)),
                move(ArcadeMoves.VAULT)
                        .exhaustion(cost.apply(0.05F))
                        .cue(CueEvent.START, cue().sound(SoundEvents.PLAYER_ATTACK_NODAMAGE, 0.4F, 1.3F)),
                move(ArcadeMoves.SLIDE)
                        .cue(CueEvent.START, dust)
                        .cue(CueEvent.TICK, cue().surface(1).shape(CueShape.TRAIL).spread(0.25F).speed(0.05F).interval(2))
        );

        // ordered by id, so that the generated files come out the same on every run
        var moves = new LinkedHashMap<ArcadeMove, MoveSettings>();
        builders.stream()
                .sorted(java.util.Comparator.comparing(builder -> builder.target().id().toString()))
                .forEach(builder -> moves.put(builder.target(), builder.build()));
        return Collections.unmodifiableMap(moves);
    }
}
