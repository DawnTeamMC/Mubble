package fr.hugman.mubble.super_mario.world.entity.monster.goomba;

import com.mojang.serialization.MapCodec;
import fr.hugman.mubble.super_mario.core.component.SuperMarioDataComponents;
import fr.hugman.mubble.super_mario.core.registries.SuperMarioRegistries;
import fr.hugman.mubble.super_mario.network.syncher.SuperMarioEntityDataSerializers;
import fr.hugman.mubble.super_mario.references.GoombaVariantIds;
import fr.hugman.mubble.world.entity.ai.goal.target.SurprisedActiveTargetGoal;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.variant.VariantUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class Goomba extends AbstractGoomba {
    public static final String VARIANT_KEY = "variant";

    public static final MapCodec<Holder<GoombaVariant>> VARIANT_MAP_CODEC = GoombaVariant.CODEC.fieldOf(VARIANT_KEY);

    protected static final EntityDataAccessor<Holder<GoombaVariant>> VARIANT = SynchedEntityData.defineId(Goomba.class, SuperMarioEntityDataSerializers.GOOMBA_VARIANT);

    public Goomba(EntityType<? extends Goomba> type, Level level) {
        super(type, level);
    }

    // BEHAVIOR

    @Override
    protected Component getTypeName() {
        return this.getVariant().value().name().orElse(super.getTypeName());
    }

    public static AttributeSupplier.Builder createGoombaAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 10.0)
                .add(Attributes.MAX_HEALTH, 4.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 1.5);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, this.level()));
        // TODO: add attack animation (bite)
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new SurprisedActiveTargetGoal<>(this, Player.class, true));
    }

    // DATA TRACKER

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(VARIANT, this.registryAccess().lookupOrThrow(SuperMarioRegistries.GOOMBA_VARIANT).getOrThrow(GoombaVariantIds.NORMAL));
    }

    public void setVariant(Holder<GoombaVariant> variant) {
        this.entityData.set(VARIANT, variant);
        this.getVariant().value().applyAttributes(this); //TODO: only apply attributes when entity is summoned/spawns
    }

    public Holder<GoombaVariant> getVariant() {
        return this.entityData.get(VARIANT);
    }

    // NBT DATA

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		VariantUtils.writeVariant(output, this.getVariant());
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		VariantUtils.readVariant(input, SuperMarioRegistries.GOOMBA_VARIANT).ifPresent(this::setVariant);
	}

    // TEXTURE

    @Override
    public Identifier getTexture() {
        if (this.isSurprised()) {
            return this.getVariant().value().assetInfo().surprised().texturePath();
        }
        return this.getVariant().value().assetInfo().texture().texturePath();
    }

	@Nullable
	@Override
	public <T> T get(DataComponentType<? extends T> type) {
		return type == SuperMarioDataComponents.GOOMBA_VARIANT ? castComponentValue((DataComponentType<T>)type, this.getVariant()) : super.get(type);
	}

	@Override
	protected void applyImplicitComponents(DataComponentGetter components) {
		this.applyImplicitComponentIfPresent(components, SuperMarioDataComponents.GOOMBA_VARIANT);
		super.applyImplicitComponents(components);
	}

	@Override
	protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
		if (type == SuperMarioDataComponents.GOOMBA_VARIANT) {
			this.setVariant(castComponentValue(SuperMarioDataComponents.GOOMBA_VARIANT, value));
			return true;
		} else {
			return super.applyImplicitComponent(type, value);
		}
	}
}
