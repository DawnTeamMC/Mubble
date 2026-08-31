package fr.hugman.mubble.super_mario.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.world.entity.freeze.FreezeResistance;
import fr.hugman.mubble.super_mario.world.entity.freeze.Freezing;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

/**
 * {@code /freeze}, which puts an entity in a block of ice by hand and reads back whether one is in
 * there.
 * <p>
 * The query is the half data packs care about: it answers with the usual {@code 1} or {@code 0}, so
 * that {@code execute if} can be hung off it.
 *
 * @see Freezing
 */
public class FreezeCommand {
    public static final String FREEZE = "freeze";

    public static final String TARGET_ARG = "target";
    public static final String SET_ARG = "set";
    public static final String QUERY_ARG = "query";
    public static final String FROZEN_ARG = "frozen";

    private static final SimpleCommandExceptionType UNFREEZABLE_EXCEPTION = new SimpleCommandExceptionType(
            Component.translatable("commands." + SuperMario.MOD_ID + ".freeze.set.unfreezable")
    );
    private static final SimpleCommandExceptionType ALREADY_FROZEN_EXCEPTION = new SimpleCommandExceptionType(
            Component.translatable("commands." + SuperMario.MOD_ID + ".freeze.set.already_frozen")
    );
    private static final SimpleCommandExceptionType NOT_FROZEN_EXCEPTION = new SimpleCommandExceptionType(
            Component.translatable("commands." + SuperMario.MOD_ID + ".freeze.set.not_frozen")
    );

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(FREEZE)
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal(SET_ARG)
                        .then(Commands.argument(TARGET_ARG, EntityArgument.entity())
                                // no value at all flips whichever way the target is currently in
                                .executes(cc -> {
                                    var target = EntityArgument.getEntity(cc, TARGET_ARG);
                                    return setFrozen(cc.getSource(), target, !Freezing.isFrozen(target));
                                })
                                .then(Commands.argument(FROZEN_ARG, BoolArgumentType.bool())
                                        .executes(cc -> setFrozen(cc.getSource(), EntityArgument.getEntity(cc, TARGET_ARG), BoolArgumentType.getBool(cc, FROZEN_ARG))))))
                .then(Commands.literal(QUERY_ARG)
                        .then(Commands.argument(TARGET_ARG, EntityArgument.entity())
                                .executes(cc -> queryFrozen(cc.getSource(), EntityArgument.getEntity(cc, TARGET_ARG))))));
    }

    private static int setFrozen(CommandSourceStack source, Entity target, boolean frozen) throws CommandSyntaxException {
        // the target's own level, rather than the source's: the two part ways across dimensions
        ServerLevel level = (ServerLevel) target.level();
        if (!frozen) {
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
        if (!(target instanceof LivingEntity living) || Freezing.resistanceOf(target) == FreezeResistance.IMMUNE) {
            throw UNFREEZABLE_EXCEPTION.create();
        }

        int duration = Freezing.durationFor(living);
        Freezing.freezeFor(level, living, duration);
        source.sendSuccess(() -> Component.translatable("commands." + SuperMario.MOD_ID + ".freeze.set.frozen", target.getDisplayName(), duration), true);
        return 1;
    }

    private static int queryFrozen(CommandSourceStack source, Entity target) {
        int remaining = Freezing.getRemainingTicks(target);
        if (remaining <= 0) {
            source.sendSuccess(() -> Component.translatable("commands." + SuperMario.MOD_ID + ".freeze.query.thawed", target.getDisplayName()), false);
            return 0;
        }
        source.sendSuccess(() -> Component.translatable("commands." + SuperMario.MOD_ID + ".freeze.query.frozen", target.getDisplayName(), remaining), false);
        return 1;
    }
}
