package fr.hugman.mubble.super_mario.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.world.entity.freeze.Freezing;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;

/**
 * {@code /freeze}, which puts an entity in a block of ice by hand and reads back whether one is in
 * there. Being an operator's tool, it freezes what an ice ball would leave alone — a creative
 * player, say — and only turns down what no ice can hold at all.
 *
 * @see Freezing
 */
public class FreezeCommand {
    public static final String FREEZE = "freeze";

    public static final String TARGET_ARG = "target";
    public static final String SET_ARG = "set";
    public static final String QUERY_ARG = "query";
    public static final String FROZEN_ARG = "frozen";
    public static final String TICKS_ARG = "ticks";
    public static final String INFINITE_ARG = "infinite";

    /** Stands in for a tick count on a freeze that never runs out on its own. */
    private static final int INFINITE_TICKS = -1;

    private static final SimpleCommandExceptionType UNFREEZABLE_EXCEPTION = new SimpleCommandExceptionType(
            Component.translatable("commands." + SuperMario.MOD_ID + ".freeze.set.unfreezable")
    );
    private static final SimpleCommandExceptionType ALREADY_FROZEN_EXCEPTION = new SimpleCommandExceptionType(
            Component.translatable("commands." + SuperMario.MOD_ID + ".freeze.set.already_frozen")
    );
    private static final SimpleCommandExceptionType NOT_FROZEN_EXCEPTION = new SimpleCommandExceptionType(
            Component.translatable("commands." + SuperMario.MOD_ID + ".freeze.set.not_frozen")
    );
    private static final SimpleCommandExceptionType THAW_DURATION_EXCEPTION = new SimpleCommandExceptionType(
            Component.translatable("commands." + SuperMario.MOD_ID + ".freeze.set.thaw_duration")
    );

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(FREEZE)
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal(SET_ARG)
                        .then(Commands.argument(TARGET_ARG, EntityArgument.entity())
                                .then(Commands.argument(FROZEN_ARG, BoolArgumentType.bool())
                                        // no duration at all leaves the target to its own, which a tough one shrugs off sooner
                                        .executes(cc -> setFrozen(cc, null))
                                        .then(Commands.argument(TICKS_ARG, IntegerArgumentType.integer(1))
                                                .executes(cc -> setFrozen(cc, IntegerArgumentType.getInteger(cc, TICKS_ARG))))
                                        .then(Commands.literal(INFINITE_ARG)
                                                .executes(cc -> setFrozen(cc, INFINITE_TICKS))))))
                .then(Commands.literal(QUERY_ARG)
                        .then(Commands.argument(TARGET_ARG, EntityArgument.entity())
                                .executes(cc -> queryFrozen(cc.getSource(), EntityArgument.getEntity(cc, TARGET_ARG))))));
    }

    /**
     * @param ticks how long to freeze the target for, {@link #INFINITE_TICKS} for a freeze that
     *              never runs out, or {@code null} to leave it to whatever the target's bulk gives it
     */
    private static int setFrozen(CommandContext<CommandSourceStack> cc, @Nullable Integer ticks) throws CommandSyntaxException {
        CommandSourceStack source = cc.getSource();
        Entity target = EntityArgument.getEntity(cc, TARGET_ARG);
        boolean frozen = BoolArgumentType.getBool(cc, FROZEN_ARG);
        // the target's own level, rather than the source's: the two part ways across dimensions
        ServerLevel level = (ServerLevel) target.level();

        if (!frozen) {
            if (ticks != null) {
                throw THAW_DURATION_EXCEPTION.create();
            }
            if (!Freezing.thaw(level, target)) {
                throw NOT_FROZEN_EXCEPTION.create();
            }
            source.sendSuccess(() -> Component.translatable("commands." + SuperMario.MOD_ID + ".freeze.set.thawed", target.getDisplayName()), true);
            return 1;
        }

        if (Freezing.isFrozen(target)) {
            throw ALREADY_FROZEN_EXCEPTION.create();
        }
        // the ice has nothing to hold on to on anything else, and bosses shatter it outright
        if (Freezing.isUnfreezable(target)) {
            throw UNFREEZABLE_EXCEPTION.create();
        }
        LivingEntity living = (LivingEntity) target;

        if (ticks != null && ticks == INFINITE_TICKS) {
            Freezing.freezeEndlessly(level, living);
            source.sendSuccess(() -> Component.translatable("commands." + SuperMario.MOD_ID + ".freeze.set.frozen_endlessly", target.getDisplayName()), true);
            return 1;
        }

        int duration = ticks == null ? Freezing.durationFor(living) : ticks;
        Freezing.freezeFor(level, living, duration);
        source.sendSuccess(() -> Component.translatable("commands." + SuperMario.MOD_ID + ".freeze.set.frozen", target.getDisplayName(), duration), true);
        return 1;
    }

    private static int queryFrozen(CommandSourceStack source, Entity target) {
        var state = Freezing.getState(target);
        if (state != null && state.isEndless()) {
            source.sendSuccess(() -> Component.translatable("commands." + SuperMario.MOD_ID + ".freeze.query.frozen_endlessly", target.getDisplayName()), false);
            return 1;
        }
        int remaining = Freezing.getRemainingTicks(target);
        if (remaining <= 0) {
            source.sendSuccess(() -> Component.translatable("commands." + SuperMario.MOD_ID + ".freeze.query.thawed", target.getDisplayName()), false);
            return 0;
        }
        source.sendSuccess(() -> Component.translatable("commands." + SuperMario.MOD_ID + ".freeze.query.frozen", target.getDisplayName(), remaining), false);
        return 1;
    }
}
