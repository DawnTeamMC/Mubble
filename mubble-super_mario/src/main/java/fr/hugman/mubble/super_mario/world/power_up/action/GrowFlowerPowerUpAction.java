package fr.hugman.mubble.super_mario.world.power_up.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.keybind.MubbleKeyBindingsKeys;
import fr.hugman.mubble.super_mario.world.entity.projectile.Flower;
import fr.hugman.mubble.world.power_up.PowerUpCharges;
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
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

/**
 * Grows a huge flower in front of its holder, which then rises on its own and defeats whatever it meets.
 * <p>
 * Unlike the balls the other forms throw, nothing about the shot is aimed: the flower always goes straight
 * up, and where the holder is looking only decides which side of them it is planted on.
 *
 * @param entity          the flower to grow
 * @param speed           how fast it rises, in blocks per tick
 * @param lifetime        how long it lasts at most, in ticks
 * @param maxClimb        how high it can climb before it wilts, in blocks
 * @param stoppedByBlocks whether it pops against the first solid block, rather than growing through it
 * @param charges         how many flowers the holder gets, and how spent ones come back
 */
public record GrowFlowerPowerUpAction(
        EntityType<?> entity,
        double speed,
        int lifetime,
        double maxClimb,
        boolean stoppedByBlocks,
        PowerUpCharges charges
) implements PowerUpAction, TooltipProvider {
    /** How far in front of the holder the flower is planted, so that its 2×2 model does not clip into them. */
    private static final double REACH = 1.0D;

    public static final MapCodec<GrowFlowerPowerUpAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity").forGetter(GrowFlowerPowerUpAction::entity),
            Codec.DOUBLE.optionalFieldOf("speed", Flower.DEFAULT_SPEED).forGetter(GrowFlowerPowerUpAction::speed),
            Codec.INT.optionalFieldOf("lifetime", Flower.DEFAULT_LIFETIME).forGetter(GrowFlowerPowerUpAction::lifetime),
            Codec.DOUBLE.optionalFieldOf("max_climb", Flower.DEFAULT_MAX_CLIMB).forGetter(GrowFlowerPowerUpAction::maxClimb),
            Codec.BOOL.optionalFieldOf("stopped_by_blocks", false).forGetter(GrowFlowerPowerUpAction::stoppedByBlocks),
            PowerUpCharges.CODEC.optionalFieldOf("charges", PowerUpCharges.DEFAULT).forGetter(GrowFlowerPowerUpAction::charges)
    ).apply(instance, GrowFlowerPowerUpAction::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, GrowFlowerPowerUpAction> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(Registries.ENTITY_TYPE), GrowFlowerPowerUpAction::entity,
            ByteBufCodecs.DOUBLE, GrowFlowerPowerUpAction::speed,
            ByteBufCodecs.INT, GrowFlowerPowerUpAction::lifetime,
            ByteBufCodecs.DOUBLE, GrowFlowerPowerUpAction::maxClimb,
            ByteBufCodecs.BOOL, GrowFlowerPowerUpAction::stoppedByBlocks,
            PowerUpCharges.STREAM_CODEC, GrowFlowerPowerUpAction::charges,
            GrowFlowerPowerUpAction::new
    );

    @Override
    public PowerUpActionType<?> getType() {
        return SuperMarioPowerUpActionTypes.GROW_FLOWER;
    }

    @Override
    public boolean canBeRefilled() {
        return true;
    }

    @Override
    public PowerUpProperties setUpProperties() {
        return this.charges.createProperties();
    }

    @Override
    public boolean canBeTriggered(Player player) {
        var properties = properties(player);
        if (!player.level().isClientSide()) {
            properties.doSoftChecks(player);
        }
        return properties.getChargeCount() > 0;
    }

    @Override
    public InteractionResult trigger(Player player) {
        var properties = properties(player);
        var level = player.level();

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        var entity = this.entity.create(level, EntitySpawnReason.TRIGGERED);
        if (entity == null) {
            return InteractionResult.FAIL;
        }
        if (entity instanceof Flower flower) {
            flower.setOwner(player);
            flower.setSpeed(this.speed);
            flower.setLifetime(this.lifetime);
            flower.setMaxClimb(this.maxClimb);
            flower.setStoppedByBlocks(this.stoppedByBlocks);
        }

        Vec3 spot = plantingSpot(player, entity.getBbWidth());
        entity.setPos(spot.x(), spot.y(), spot.z());
        level.addFreshEntity(entity);

        properties.useCharge();
        properties.trackEntity(entity.getUUID());
        return InteractionResult.SUCCESS;
    }

    /**
     * Where the flower is planted: at the feet of the holder, one step ahead of them in the direction they
     * are facing, and centred on the spot rather than standing next to it.
     */
    public static Vec3 plantingSpot(Player player, float width) {
        float yaw = player.getYRot() * (float) (Math.PI / 180.0);
        double reach = REACH + width / 2.0D;
        return new Vec3(
                player.getX() - Mth.sin(yaw) * reach,
                player.getY(),
                player.getZ() + Mth.cos(yaw) * reach
        );
    }

    private PowerUpProperties properties(Player player) {
        var properties = player.getPowerUpProperties();
        if (properties == null) {
            properties = this.setUpProperties();
            player.setPowerUpProperties(properties);
        }
        return properties;
    }

    @Override
    public boolean shouldSwingOtherHand() {
        return true;
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        this.getTranslationKey().ifPresent(key -> textConsumer.accept(Component.translatable(
                key + ".description",
                Component.keybind(MubbleKeyBindingsKeys.TRIGGER_POWER_UP)
        ).withStyle(ChatFormatting.GRAY)));
    }
}
