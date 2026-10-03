package fr.hugman.mubble.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.tags.ArcadeMoveTags;
import fr.hugman.mubble.world.arcade.ArcadeController;
import fr.hugman.mubble.world.arcade.ArcadeProfile;
import fr.hugman.mubble.world.arcade.ArcadeUnlocks;
import fr.hugman.mubble.world.arcade.access.AccessMode;
import fr.hugman.mubble.world.arcade.access.ArcadeSource;
import fr.hugman.mubble.world.arcade.access.ArcadeSources;
import fr.hugman.mubble.world.arcade.access.MoveSelector;
import fr.hugman.mubble.world.arcade.gym.ArcadeGym;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.ResourceOrTagKeyArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

/**
 * {@code /mubble arcade}: everything about the arcade movement of players, for operators.
 */
public class ArcadeCommand {
    private static final String TARGETS = "targets";
    private static final String TARGET = "target";
    private static final String ID = "id";
    private static final String DEFINITION = "definition";
    private static final String MOVE = "move";
    private static final String PROFILE = "profile";
    private static final String POS = "pos";
    private static final String VELOCITY = "velocity";

    /** The id of the source {@code /mubble arcade profile} manages. */
    public static final Identifier COMMAND_PROFILE_SOURCE = Mubble.id("command/profile");

    private static final DynamicCommandExceptionType INVALID_SOURCE = new DynamicCommandExceptionType(error -> Component.translatable("commands.mubble.arcade.source.invalid", error));
    private static final DynamicCommandExceptionType UNKNOWN_SOURCE = new DynamicCommandExceptionType(id -> Component.translatable("commands.mubble.arcade.source.unknown", id));
    private static final DynamicCommandExceptionType INVALID_MOVE = new DynamicCommandExceptionType(move -> Component.translatable("commands.mubble.arcade.move.invalid", move));
    private static final DynamicCommandExceptionType INVALID_PROFILE = new DynamicCommandExceptionType(profile -> Component.translatable("commands.mubble.arcade.profile.invalid", profile));
    private static final SimpleCommandExceptionType NOTHING_CHANGED = new SimpleCommandExceptionType(Component.translatable("commands.mubble.arcade.unchanged"));

    /**
     * The body of {@code /mubble arcade source add}: a source without its id, and with a duration in
     * ticks rather than an expiry date.
     */
    private record SourceBody(Optional<ResourceKey<ArcadeProfile>> profile, Map<MoveSelector, AccessMode> moves, int priority, Optional<Integer> duration) {
        private static final Codec<SourceBody> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceKey.codec(MubbleRegistries.ARCADE_PROFILE).optionalFieldOf("profile").forGetter(SourceBody::profile),
                Codec.unboundedMap(MoveSelector.CODEC, AccessMode.CODEC).optionalFieldOf("moves", Map.of()).forGetter(SourceBody::moves),
                Codec.INT.optionalFieldOf("priority", 0).forGetter(SourceBody::priority),
                Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("duration").forGetter(SourceBody::duration)
        ).apply(instance, SourceBody::new));
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(Commands.literal(Mubble.MOD_ID)
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(arcade()));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> arcade() {
        return Commands.literal("arcade")
                .then(Commands.literal("source")
                        .then(Commands.literal("add")
                                .then(Commands.argument(TARGETS, EntityArgument.players())
                                        .then(Commands.argument(ID, IdentifierArgument.id())
                                                .then(Commands.argument(DEFINITION, CompoundTagArgument.compoundTag())
                                                        .executes(cc -> addSource(cc.getSource(), EntityArgument.getPlayers(cc, TARGETS), IdentifierArgument.getId(cc, ID), parseSource(cc.getSource(), CompoundTagArgument.getCompoundTag(cc, DEFINITION))))))))
                        .then(Commands.literal("remove")
                                .then(Commands.argument(TARGETS, EntityArgument.players())
                                        .then(Commands.argument(ID, IdentifierArgument.id())
                                                .executes(cc -> removeSource(cc.getSource(), EntityArgument.getPlayers(cc, TARGETS), IdentifierArgument.getId(cc, ID))))))
                        .then(Commands.literal("list")
                                .executes(cc -> listSources(cc.getSource(), cc.getSource().getPlayerOrException()))
                                .then(Commands.argument(TARGET, EntityArgument.player())
                                        .executes(cc -> listSources(cc.getSource(), EntityArgument.getPlayer(cc, TARGET))))))
                .then(Commands.literal("unlock")
                        .then(Commands.argument(TARGETS, EntityArgument.players())
                                .then(Commands.argument(MOVE, ResourceOrTagKeyArgument.resourceOrTagKey(MubbleRegistries.ARCADE_MOVE))
                                        .executes(cc -> unlock(cc.getSource(), EntityArgument.getPlayers(cc, TARGETS), selector(cc.getArgument(MOVE, ResourceOrTagKeyArgument.Result.class)), true)))))
                .then(Commands.literal("lock")
                        .then(Commands.argument(TARGETS, EntityArgument.players())
                                .then(Commands.argument(MOVE, ResourceOrTagKeyArgument.resourceOrTagKey(MubbleRegistries.ARCADE_MOVE))
                                        .executes(cc -> unlock(cc.getSource(), EntityArgument.getPlayers(cc, TARGETS), selector(cc.getArgument(MOVE, ResourceOrTagKeyArgument.Result.class)), false)))))
                .then(Commands.literal("inspect")
                        .executes(cc -> inspect(cc.getSource(), cc.getSource().getPlayerOrException()))
                        .then(Commands.argument(TARGET, EntityArgument.player())
                                .executes(cc -> inspect(cc.getSource(), EntityArgument.getPlayer(cc, TARGET)))))
                .then(Commands.literal("profile")
                        .then(Commands.argument(TARGETS, EntityArgument.players())
                                .then(Commands.literal("clear")
                                        .executes(cc -> removeSource(cc.getSource(), EntityArgument.getPlayers(cc, TARGETS), COMMAND_PROFILE_SOURCE)))
                                .then(profileArgument())))
                .then(Commands.literal("gym")
                        .executes(cc -> buildGym(cc.getSource(), net.minecraft.core.BlockPos.containing(cc.getSource().getPosition())))
                        .then(Commands.argument(POS, BlockPosArgument.blockPos())
                                .executes(cc -> buildGym(cc.getSource(), BlockPosArgument.getLoadedBlockPos(cc, POS)))))
                .then(Commands.literal("impulse")
                        .then(Commands.argument(TARGETS, EntityArgument.players())
                                .then(Commands.argument(VELOCITY, Vec3Argument.vec3(false))
                                        .executes(cc -> impulse(cc.getSource(), EntityArgument.getPlayers(cc, TARGETS), Vec3Argument.getVec3(cc, VELOCITY))))));
    }

    /** The profile to set, optionally followed by the mode every move gets; modes are literals so that clients need no custom argument type. */
    private static com.mojang.brigadier.builder.RequiredArgumentBuilder<CommandSourceStack, ResourceKey<ArcadeProfile>> profileArgument() {
        var argument = Commands.argument(PROFILE, ResourceKeyArgument.key(MubbleRegistries.ARCADE_PROFILE))
                .executes(cc -> setProfile(cc.getSource(), EntityArgument.getPlayers(cc, TARGETS), profileKey(cc.getArgument(PROFILE, ResourceKey.class)), Optional.empty()));
        for (var mode : AccessMode.values()) {
            argument.then(Commands.literal(mode.getSerializedName())
                    .executes(cc -> setProfile(cc.getSource(), EntityArgument.getPlayers(cc, TARGETS), profileKey(cc.getArgument(PROFILE, ResourceKey.class)), Optional.of(mode))));
        }
        return argument;
    }

    @SuppressWarnings("unchecked")
    private static ResourceKey<ArcadeProfile> profileKey(ResourceKey<?> key) throws CommandSyntaxException {
        return ((ResourceKey<Object>) key).cast(MubbleRegistries.ARCADE_PROFILE).orElseThrow(() -> INVALID_PROFILE.create(key.identifier()));
    }

    @SuppressWarnings("unchecked")
    private static MoveSelector selector(ResourceOrTagKeyArgument.Result<?> result) throws CommandSyntaxException {
        var typed = ((ResourceOrTagKeyArgument.Result<Object>) result).cast(MubbleRegistries.ARCADE_MOVE).orElseThrow(() -> INVALID_MOVE.create(result.asPrintable()));
        return new MoveSelector(typed.unwrap());
    }

    private static SourceBody parseSource(CommandSourceStack source, net.minecraft.nbt.CompoundTag tag) throws CommandSyntaxException {
        var ops = source.registryAccess().createSerializationContext(NbtOps.INSTANCE);
        return SourceBody.CODEC.parse(ops, tag).getOrThrow(INVALID_SOURCE::create);
    }

    private static int addSource(CommandSourceStack source, Collection<ServerPlayer> targets, Identifier id, SourceBody body) {
        long now = source.getLevel().getGameTime();
        var arcadeSource = new ArcadeSource(id, body.profile(), body.moves(), body.priority(), body.duration().map(duration -> now + duration));
        targets.forEach(player -> ArcadeSources.add(player, arcadeSource));
        source.sendSuccess(() -> Component.translatable("commands.mubble.arcade.source.add.success", id.toString(), targets.size()), true);
        return targets.size();
    }

    private static int removeSource(CommandSourceStack source, Collection<ServerPlayer> targets, Identifier id) throws CommandSyntaxException {
        int removed = 0;
        for (var player : targets) {
            if (ArcadeSources.remove(player, id)) {
                removed++;
            }
        }
        if (removed == 0) {
            throw UNKNOWN_SOURCE.create(id.toString());
        }
        int count = removed;
        source.sendSuccess(() -> Component.translatable("commands.mubble.arcade.source.remove.success", id.toString(), count), true);
        return removed;
    }

    private static int listSources(CommandSourceStack source, ServerPlayer target) {
        var access = ArcadeController.of(target).access();
        if (access.sources().isEmpty()) {
            source.sendSuccess(() -> Component.translatable("commands.mubble.arcade.source.list.none", target.getDisplayName()), false);
            return 0;
        }
        source.sendSuccess(() -> Component.translatable("commands.mubble.arcade.source.list.header", target.getDisplayName(), access.sources().size()), false);
        for (var arcadeSource : access.sources()) {
            source.sendSuccess(() -> Component.literal(" - " + describe(arcadeSource)), false);
        }
        return access.sources().size();
    }

    private static String describe(ArcadeSource source) {
        var builder = new StringBuilder(source.id().toString());
        source.profile().ifPresent(profile -> builder.append(" profile=").append(profile.identifier()));
        builder.append(" priority=").append(source.priority());
        source.expiry().ifPresent(expiry -> builder.append(" expiry=").append(expiry));
        source.moves().forEach((selector, mode) -> builder.append(' ').append(selector).append('=').append(mode.getSerializedName()));
        return builder.toString();
    }

    private static int unlock(CommandSourceStack source, Collection<ServerPlayer> targets, MoveSelector selector, boolean unlock) throws CommandSyntaxException {
        int changed = 0;
        for (var player : targets) {
            changed += unlock ? ArcadeUnlocks.unlock(player, selector) : ArcadeUnlocks.lock(player, selector);
        }
        if (changed == 0) {
            throw NOTHING_CHANGED.create();
        }
        String key = unlock ? "commands.mubble.arcade.unlock.success" : "commands.mubble.arcade.lock.success";
        source.sendSuccess(() -> Component.translatable(key, selector.toString(), targets.size()), true);
        return changed;
    }

    private static int setProfile(CommandSourceStack source, Collection<ServerPlayer> targets, ResourceKey<ArcadeProfile> profile, Optional<AccessMode> mode) {
        Map<MoveSelector, AccessMode> moves = mode.map(m -> Map.of(MoveSelector.tag(ArcadeMoveTags.ALL), m)).orElse(Map.of());
        var arcadeSource = new ArcadeSource(COMMAND_PROFILE_SOURCE, Optional.of(profile), moves, 1000, Optional.empty());
        targets.forEach(player -> ArcadeSources.add(player, arcadeSource));
        source.sendSuccess(() -> Component.translatable("commands.mubble.arcade.profile.success", profile.identifier().toString(), targets.size()), true);
        return targets.size();
    }

    private static int inspect(CommandSourceStack source, ServerPlayer target) {
        var controller = ArcadeController.of(target);
        var access = controller.access();
        var state = controller.state();
        var validation = controller.validation();
        source.sendSuccess(() -> Component.translatable("commands.mubble.arcade.inspect.header", target.getDisplayName()), false);
        source.sendSuccess(() -> Component.literal(" profile: " + access.profile().map(key -> key.identifier().toString()).orElse("none")
                + (controller.isSuspended() ? " (suspended)" : "")), false);
        if (controller.isDriving()) {
            source.sendSuccess(() -> Component.literal(" state: " + state), false);
        }
        source.sendSuccess(() -> Component.literal(" sources: " + access.sources().stream().map(s -> s.id().toString()).toList()), false);
        source.sendSuccess(() -> Component.literal(" modes: " + access.modes().entrySet().stream().map(e -> e.getKey() + "=" + e.getValue().getSerializedName()).sorted().toList()), false);
        source.sendSuccess(() -> Component.literal(" owned: " + access.unlocks().moves().stream().map(key -> key.identifier().toString()).sorted().toList()), false);
        source.sendSuccess(() -> Component.literal(" validation: " + validation.accepted + " accepted, " + validation.corrected + " corrected, " + validation.rejected + " rejected"), false);
        if (validation.lastRejection != null) {
            source.sendSuccess(() -> Component.literal(" last rejection: " + validation.lastRejection), false);
        }
        return controller.isDriving() ? 1 : 0;
    }

    private static int buildGym(CommandSourceStack source, net.minecraft.core.BlockPos origin) {
        ArcadeGym.build(source.getLevel(), origin);
        source.sendSuccess(() -> Component.translatable("commands.mubble.arcade.gym.success", origin.getX(), origin.getY(), origin.getZ()), true);
        return 1;
    }

    private static int impulse(CommandSourceStack source, Collection<ServerPlayer> targets, Vec3 velocity) {
        for (var player : targets) {
            player.setDeltaMovement(velocity);
            player.hurtMarked = true;
        }
        source.sendSuccess(() -> Component.translatable("commands.mubble.arcade.impulse.success", targets.size()), true);
        return targets.size();
    }
}
