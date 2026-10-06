package fr.hugman.mubble.super_mario.world.power_up.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.keybind.MubbleKeyBindingsKeys;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.world.power_up.PowerUpProperties;
import fr.hugman.mubble.world.power_up.action.PowerUpAction;
import fr.hugman.mubble.world.power_up.action.PowerUpActionType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Consumer;


public record SpawnCloudPlatformPowerUpAction(
        EntityType<?> entity,
        Optional<Integer> max
) implements PowerUpAction, TooltipProvider {
    /** How far under the player's feet the platform tries to appear. */
    private static final double DROP = 0.5D;
    /** The upward nudge the player gets, so that they land back on the platform they just made. */
    private static final double NUDGE = 0.2D;
    /** Step taken while looking for a spot free of blocks, between the ideal one and the feet. */
    private static final double SEARCH_STEP = 0.25D;
    /**
     * Width of the box checked for blocks. The platform is far wider than that, and testing it whole
     * would refuse to place it anywhere near a wall; what decides is the column the player stands in.
     */
    private static final double CLEARANCE_WIDTH = 1.0D;

    public static final MapCodec<SpawnCloudPlatformPowerUpAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity").forGetter(SpawnCloudPlatformPowerUpAction::entity),
            Codec.INT.optionalFieldOf("max").forGetter(SpawnCloudPlatformPowerUpAction::max)
    ).apply(instance, SpawnCloudPlatformPowerUpAction::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpawnCloudPlatformPowerUpAction> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(Registries.ENTITY_TYPE), (SpawnCloudPlatformPowerUpAction::entity),
            ByteBufCodecs.optional(ByteBufCodecs.INT), (SpawnCloudPlatformPowerUpAction::max),
            SpawnCloudPlatformPowerUpAction::new
    );

    @Override
    public PowerUpActionType<?> getType() {
        return SuperMarioPowerUpActionTypes.SPAWN_CLOUD_PLATFORM;
    }

    @Override
    public boolean canBeRefilled() {
        return true;
    }

    @Override
    public PowerUpProperties setUpProperties() {
        return new PowerUpProperties(PowerUpProperties.ChargeCounting.ONLY_DECREASE, max.orElse(Integer.MAX_VALUE));
    }

    @Override
    public boolean canBeTriggered(Player player) {
        var properties = player.getPowerUpProperties();

        if(properties == null) {
            properties = setUpProperties();
            player.setPowerUpProperties(properties);
        }

        var level = player.level();
        if (!level.isClientSide()) {
            properties.doSoftChecks(player);
        }
        return properties.getChargeCount() > 0;
    }

    @Override
    public InteractionResult trigger(Player player) {
        var properties = player.getPowerUpProperties();
        var level = player.level();

        if(properties == null) {
            properties = setUpProperties();
            player.setPowerUpProperties(properties);
        }

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        var dimensions = this.entity().getDimensions();
        var platformY = findPlatformY(level, player, dimensions.height());
        if (platformY.isEmpty()) {
            return InteractionResult.FAIL;
        }

        var entity = this.entity().create(level, EntitySpawnReason.TRIGGERED);
        if (null == entity) {
            return InteractionResult.FAIL;
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SuperMarioSounds.POWER_UP_SPIN_ATTACK, SoundSource.PLAYERS, 0.5F, 1.0F);
        entity.setPos(player.getX(), platformY.getAsDouble(), player.getZ());
        level.addFreshEntity(entity);
        properties.useCharge();

        // The platform had to be raised out of the ground, so the player rides up with it instead of
        // being left standing next to it.
        double lift = lift(player, platformY.getAsDouble(), dimensions.height());
        if (lift > 0.0D && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.teleportTo(player.getX(), player.getY() + lift, player.getZ());
        }

        player.setDeltaMovement(player.getDeltaMovement().x, NUDGE, player.getDeltaMovement().z);
        ((ServerPlayer) player).connection.send(new ClientboundSetEntityMotionPacket(player));
        entity.fallDistance = 0.0F;
        return InteractionResult.SUCCESS;
    }

    /**
     * Looks for the height the platform can appear at, starting from the ideal one under the
     * player's feet and going up, so that it never ends up buried in the ground.
     *
     * @return the Y the bottom of the platform goes to, or empty if there is no room for it at all
     */
    private static OptionalDouble findPlatformY(Level level, Player player, double height) {
        double ideal = player.getY() - DROP - height;
        // Any higher and the platform would appear above the player rather than under them.
        double highest = player.getY();
        int steps = Mth.ceil((highest - ideal) / SEARCH_STEP);

        for (int step = 0; step <= steps; step++) {
            double y = Math.min(ideal + step * SEARCH_STEP, highest);
            if (fits(level, player, y, height)) {
                return OptionalDouble.of(y);
            }
        }
        return OptionalDouble.empty();
    }

    /**
     * Whether the platform can appear with its bottom at {@code y}, together with the player it may
     * have to lift.
     */
    private static boolean fits(Level level, Player player, double y, double height) {
        double half = CLEARANCE_WIDTH / 2.0D;
        var box = new AABB(
                player.getX() - half, y, player.getZ() - half,
                player.getX() + half, y + height, player.getZ() + half
        );
        if (!level.noCollision(null, box)) {
            return false;
        }

        double lift = lift(player, y, height);
        return lift <= 0.0D || level.noCollision(player, player.getBoundingBox().move(0.0D, lift, 0.0D));
    }

    /** How far the player has to go up to end up standing on top of the platform. */
    private static double lift(Player player, double platformY, double height) {
        return platformY + height - player.getY();
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        this.getTranslationKey().ifPresent(key -> textConsumer.accept(Component.translatable(
                key + ".description",
                Component.keybind(MubbleKeyBindingsKeys.TRIGGER_POWER_UP)
        ).withStyle(ChatFormatting.GRAY)));
    }
}
