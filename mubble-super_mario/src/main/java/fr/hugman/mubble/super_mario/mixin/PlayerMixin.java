package fr.hugman.mubble.super_mario.mixin;

import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.world.entity.monster.goomba.MiniGoomba;
import fr.hugman.mubble.super_mario.world.entity.monster.goomba.MiniGoombaCarrier;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Makes players the thing mini goombas hold onto.
 * <p>
 * A clinging mini goomba is a number here rather than an entity in the world, so all that is kept is how
 * many are hanging on. Everything else is worked out from that: how far the player is slowed, how much of
 * their jump is left, and how much shaking is still needed to be rid of them.
 */
@Mixin(Player.class)
public class PlayerMixin implements MiniGoombaCarrier {
    @Unique
    private static final EntityDataAccessor<Integer> CLINGING_MINI_GOOMBAS = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);

    @Unique
    private static final String CLINGING_MINI_GOOMBAS_KEY = "clinging_mini_goombas";

    @Unique
    private static final Identifier CLING_MODIFIER_ID = SuperMario.id("clinging_mini_goombas");

    /** How much of the player's speed each clinging mini goomba takes away. */
    @Unique
    private static final double SLOWDOWN_PER_GOOMBA = 0.12D;
    /** However many pile on, they never take more than this share of the player's speed. */
    @Unique
    private static final double MAX_SLOWDOWN = 0.6D;
    /** How much of the player's jump each clinging mini goomba takes away, as in Super Mario Bros. 3. */
    @Unique
    private static final double JUMP_PENALTY_PER_GOOMBA = 0.08D;
    @Unique
    private static final double MAX_JUMP_PENALTY = 0.5D;

    /**
     * How many sneak presses shake one mini goomba loose, and how long the player has between two of them
     * before the struggle is counted as given up on.
     */
    @Unique
    private static final int STRUGGLE_TOGGLES = 4;
    @Unique
    private static final int STRUGGLE_WINDOW = 20;

    /** How often a clinging mini goomba is heard gnawing away, in ticks. */
    @Unique
    private static final int NIBBLE_INTERVAL = 15;

    /** The count the attribute modifiers currently stand for, so they are only rebuilt when it moves. */
    @Unique
    private int super_mario$appliedClingCount = -1;
    @Unique
    private boolean super_mario$wasSneaking;
    @Unique
    private int super_mario$struggleToggles;
    @Unique
    private int super_mario$lastStruggleTick;

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void super_mario$defineClingingMiniGoombas(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(CLINGING_MINI_GOOMBAS, 0);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void super_mario$writeClingingMiniGoombas(ValueOutput output, CallbackInfo ci) {
        int count = this.getClingingMiniGoombas();
        if (count > 0) {
            output.putInt(CLINGING_MINI_GOOMBAS_KEY, count);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void super_mario$readClingingMiniGoombas(ValueInput input, CallbackInfo ci) {
        this.setClingingMiniGoombas(input.getIntOr(CLINGING_MINI_GOOMBAS_KEY, 0));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void super_mario$tickClingingMiniGoombas(CallbackInfo ci) {
        var this_ = (Player) (Object) this;
        if (!(this_.level() instanceof ServerLevel level)) {
            return;
        }
        int count = this.getClingingMiniGoombas();
        this.super_mario$refreshClingModifiers(this_, count);
        if (count <= 0) {
            this.super_mario$struggleToggles = 0;
            this.super_mario$wasSneaking = this_.isShiftKeyDown();
            return;
        }
        this.super_mario$nibble(this_, level, count);
        this.super_mario$struggle(this_, level, count);
    }

    /**
     * Weighs the player down by however many mini goombas are hanging on.
     * <p>
     * The modifiers are transient: they are rebuilt from the count whenever it moves, and the count is what
     * gets saved, so there is nothing to leave behind on a world that loads without them.
     */
    @Unique
    private void super_mario$refreshClingModifiers(Player player, int count) {
        if (count == this.super_mario$appliedClingCount) {
            return;
        }
        this.super_mario$appliedClingCount = count;
        this.super_mario$applyClingModifier(player.getAttribute(Attributes.MOVEMENT_SPEED), count, SLOWDOWN_PER_GOOMBA, MAX_SLOWDOWN);
        this.super_mario$applyClingModifier(player.getAttribute(Attributes.JUMP_STRENGTH), count, JUMP_PENALTY_PER_GOOMBA, MAX_JUMP_PENALTY);
    }

    @Unique
    private void super_mario$applyClingModifier(AttributeInstance attribute, int count, double per, double max) {
        if (attribute == null) {
            return;
        }
        if (count <= 0) {
            attribute.removeModifier(CLING_MODIFIER_ID);
            return;
        }
        double penalty = Math.min(max, per * count);
        attribute.addOrUpdateTransientModifier(new AttributeModifier(CLING_MODIFIER_ID, -penalty, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }

    /** The gnawing that says something is still holding on, without ever taking any health for it. */
    @Unique
    private void super_mario$nibble(Player player, ServerLevel level, int count) {
        if (player.tickCount % NIBBLE_INTERVAL != 0) {
            return;
        }
        //TODO: a mini goomba nibble of its own, rather than a goomba footstep pitched up
        float pitch = 1.6F + level.getRandom().nextFloat() * 0.3F;
        level.playSound(null, player, SuperMarioSounds.GOOMBA_WALK_STEP, SoundSource.HOSTILE, 0.3F + 0.05F * count, pitch);
    }

    /**
     * Counts the player thrashing about, and lets one mini goomba go once they have shaken hard enough.
     * <p>
     * Sneak is the shake: every press and every release counts, so holding it down does nothing and only
     * hammering the key gets anywhere. Stop for {@value #STRUGGLE_WINDOW} ticks and the effort is lost.
     */
    @Unique
    private void super_mario$struggle(Player player, ServerLevel level, int count) {
        boolean sneaking = player.isShiftKeyDown();
        if (sneaking != this.super_mario$wasSneaking) {
            this.super_mario$wasSneaking = sneaking;
            this.super_mario$struggleToggles++;
            this.super_mario$lastStruggleTick = player.tickCount;
        } else if (player.tickCount - this.super_mario$lastStruggleTick > STRUGGLE_WINDOW) {
            this.super_mario$struggleToggles = 0;
        }
        if (this.super_mario$struggleToggles < STRUGGLE_TOGGLES) {
            return;
        }
        this.super_mario$struggleToggles = 0;
        // Only counted as shaken off once one is actually back in the world, so a mini goomba that could
        // not be put there is not quietly lost instead.
        if (MiniGoomba.shakeOff(level, player) != null) {
            this.setClingingMiniGoombas(count - 1);
        }
    }

    @Override
    public int getClingingMiniGoombas() {
        return ((Player) (Object) this).getEntityData().get(CLINGING_MINI_GOOMBAS);
    }

    @Override
    public void setClingingMiniGoombas(int count) {
        ((Player) (Object) this).getEntityData().set(CLINGING_MINI_GOOMBAS, Math.max(0, count));
    }
}
