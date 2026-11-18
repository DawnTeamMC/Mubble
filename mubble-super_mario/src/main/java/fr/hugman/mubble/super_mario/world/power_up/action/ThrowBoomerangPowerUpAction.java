package fr.hugman.mubble.super_mario.world.power_up.action;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.keybind.MubbleKeyBindingsKeys;
import fr.hugman.mubble.super_mario.core.particles.SuperMarioParticleTypes;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.world.entity.projectile.Boomerang;
import fr.hugman.mubble.world.power_up.PowerUpCharges;
import fr.hugman.mubble.world.power_up.PowerUpProperties;
import fr.hugman.mubble.world.power_up.action.PowerUpAction;
import fr.hugman.mubble.world.power_up.action.PowerUpActionType;
import fr.hugman.mubble.world.power_up.action.ShootProjectilePowerUpAction;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

/**
 * @param maxInFlight   how many boomerangs the holder can have out at once
 * @param readyParticle shown to the thrower only, once the miss cooldown is over
 */
public record ThrowBoomerangPowerUpAction(
        Boomerang.Flight flight,
        Boomerang.Look look,
        int maxInFlight,
        Holder<SoundEvent> throwSound,
        Holder<SoundEvent> readySound,
        ParticleOptions readyParticle
) implements PowerUpAction, TooltipProvider {
    public static final ThrowBoomerangPowerUpAction DEFAULT = new ThrowBoomerangPowerUpAction(
            Boomerang.Flight.DEFAULT,
            Boomerang.Look.DEFAULT,
            1,
            SuperMarioSounds.BOOMERANG_THROW,
            SuperMarioSounds.BOOMERANG_READY,
            SuperMarioParticleTypes.COIN_SPARKLE
    );
    /** Only gives the boomerang its heading: how fast it actually flies is up to its flight. */
    private static final float THROW_SPEED = 1.0F;

    public static final MapCodec<ThrowBoomerangPowerUpAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Boomerang.Flight.MAP_CODEC.forGetter(ThrowBoomerangPowerUpAction::flight),
            Boomerang.Look.MAP_CODEC.forGetter(ThrowBoomerangPowerUpAction::look),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("max_in_flight", DEFAULT.maxInFlight).forGetter(ThrowBoomerangPowerUpAction::maxInFlight),
            SoundEvent.CODEC.optionalFieldOf("throw_sound", DEFAULT.throwSound).forGetter(ThrowBoomerangPowerUpAction::throwSound),
            SoundEvent.CODEC.optionalFieldOf("ready_sound", DEFAULT.readySound).forGetter(ThrowBoomerangPowerUpAction::readySound),
            ParticleTypes.CODEC.optionalFieldOf("ready_particle", DEFAULT.readyParticle).forGetter(ThrowBoomerangPowerUpAction::readyParticle)
    ).apply(instance, ThrowBoomerangPowerUpAction::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ThrowBoomerangPowerUpAction> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

    @Override
    public PowerUpActionType<?> getType() {
        return SuperMarioPowerUpActionTypes.THROW_BOOMERANG;
    }

    @Override
    public boolean canBeRefilled() {
        return true;
    }

    @Override
    public PowerUpProperties setUpProperties() {
        return PowerUpCharges.fromActiveEntities(this.maxInFlight).createProperties();
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
    public boolean canBeTriggered(Player player) {
        var properties = this.properties(player);
        if (!player.level().isClientSide()) {
            properties.doSoftChecks(player);
        }
        return properties.getChargeCount() > 0;
    }

    @Override
    public InteractionResult trigger(Player player) {
        var properties = this.properties(player);
        var level = player.level();
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        var boomerang = new Boomerang(level, this.flight, this.look);
        boomerang.setOwner(player);
        boomerang.setPos(player.getX(), player.getEyeY() - 0.1F - boomerang.getBbHeight() / 2.0F, player.getZ());
        Vec3 look = player.getLookAngle();
        ShootProjectilePowerUpAction.setVelocity(boomerang, look.x, look.y, look.z, THROW_SPEED, 0.0F);
        level.addFreshEntity(boomerang);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), this.throwSound, SoundSource.PLAYERS, 0.5F, 1.0F);
        properties.useCharge();
        properties.trackEntity(boomerang.getUUID());
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onCooldownEnd(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            Vec3 pos = player.getEyePosition();
            serverPlayer.connection.send(new ClientboundSoundPacket(this.readySound, SoundSource.PLAYERS, pos.x, pos.y, pos.z, 0.6F, 1.2F, player.getRandom().nextLong()));
            serverPlayer.level().sendParticles(serverPlayer, this.readyParticle, false, false, pos.x, pos.y - 0.3D, pos.z, 6, 0.3D, 0.3D, 0.3D, 0.0D);
        }
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
