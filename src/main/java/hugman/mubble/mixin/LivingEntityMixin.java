package hugman.mubble.mixin;

import hugman.mubble.init.MubbleEffects;
import hugman.mubble.init.MubbleEnchantments;
import hugman.mubble.init.data.MubbleTags;
import hugman.mubble.objects.item.LightsaberItem;
import hugman.mubble.util.EnchantmentUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
{
	@Inject(method = "jump", at = @At(value = "TAIL"), cancellable = true)
	private void jump(CallbackInfo ci)
	{
		LivingEntity entity = (LivingEntity) (Object) this;
		if (entity.hasStatusEffect(MubbleEffects.HEAVINESS))
		{
			Vec3d vec3d = entity.getVelocity();
			entity.setVelocity(vec3d.x, vec3d.y - (float) (entity.getStatusEffect(MubbleEffects.HEAVINESS).getAmplifier() + 1) * 0.05F, vec3d.z);
		}
	}

	@Inject(method = "tick", at = @At(value = "TAIL"), cancellable = true)
	private void tick(CallbackInfo ci)
	{
		LivingEntity entity = (LivingEntity) (Object) this;
		World world = entity.getEntityWorld();
		ItemStack headItem = entity.getEquippedStack(EquipmentSlot.HEAD);
		if (!world.isClient)
		{
			if (MubbleTags.Items.WEIGHT_HEAVY.contains(headItem.getItem()))
			{
				entity.addStatusEffect(new StatusEffectInstance(MubbleEffects.HEAVINESS, 25, 0));
			}
		}
	}

	@Inject(method = "swingHand", at = @At(value = "TAIL"), cancellable = true)
	private void swingHand(Hand hand, CallbackInfo ci)
	{
		LivingEntity entity = (LivingEntity) (Object) this;
		ItemStack stack = entity.getMainHandStack();
		if (stack.getItem() instanceof LightsaberItem)
		{
			((LightsaberItem) stack.getItem()).onSwing(entity, false);
		}
	}

	@Inject(method = "dropLoot", at = @At(value = "HEAD"), cancellable = true)
	private void dropLoot(DamageSource source, boolean causedByPlayer, CallbackInfo ci)
	{
		LivingEntity entity = (LivingEntity) (Object) this;
		World world = entity.getEntityWorld();
		if (causedByPlayer && source.getAttacker() instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) source.getAttacker();
			LootTable lootTable = world.getServer().getLootManager().getTable(entity.getLootTable());
			LootContext.Builder builder = this.getLootContextBuilder(causedByPlayer, source);
			if (EnchantmentUtil.hasEnchantment(MubbleEnchantments.TELEKINESIS, player.getMainHandStack()))
			{
				for (ItemStack stack : lootTable.generateLoot(builder.build(LootContextTypes.ENTITY)))
				{
					player.inventory.insertStack(stack);
				}
				ci.cancel();
			}
		}
	}

	@Inject(method = "dropXp", at = @At(value = "HEAD"), cancellable = true)
	private void dropXp(CallbackInfo ci)
	{
		LivingEntity entity = (LivingEntity) (Object) this;
		LivingEntity attacker = entity.getAttacker();
		if (attacker instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) attacker;
			if (!player.getMainHandStack().isEmpty())
			{
				if (EnchantmentUtil.hasEnchantment(MubbleEnchantments.TELEKINESIS, player.getMainHandStack()))
				{
					player.addExperience(this.getCurrentExperience(player));
					ci.cancel();
				}
			}
		}
	}

	@Shadow
	protected abstract int getCurrentExperience(PlayerEntity player);

	@Shadow
	protected abstract LootContext.Builder getLootContextBuilder(boolean causedByPlayer, DamageSource source);
}
