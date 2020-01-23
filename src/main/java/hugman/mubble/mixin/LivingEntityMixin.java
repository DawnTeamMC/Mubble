package hugman.mubble.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import hugman.mubble.init.MubbleEffects;
import hugman.mubble.init.data.MubbleTags;
import hugman.mubble.objects.item.LightsaberItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public class LivingEntityMixin
{
	@Inject(method = "jump", at = @At(value = "TAIL"), cancellable = true)
	private void jump(CallbackInfo ci)
	{
		LivingEntity entity = (LivingEntity) (Object) this;
		if(entity.hasStatusEffect(MubbleEffects.HEAVINESS))
		{
			Vec3d vec3d = entity.getVelocity();
			entity.setVelocity(vec3d.x, vec3d.y - (float)(entity.getStatusEffect(MubbleEffects.HEAVINESS).getAmplifier() + 1) * 0.05F, vec3d.z);
		}
	}
	
	@Inject(method = "tick", at = @At(value = "TAIL"), cancellable = true)
	private void tick(CallbackInfo ci)
	{
		LivingEntity entity = (LivingEntity) (Object) this;
		World world = entity.getEntityWorld();
		ItemStack headItem = entity.getEquippedStack(EquipmentSlot.HEAD);
		if(!world.isClient)
		{
			if(MubbleTags.Items.WEIGHT_HEAVY.contains(headItem.getItem()))
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
		if(stack.getItem() instanceof LightsaberItem)
		{
			((LightsaberItem) stack.getItem()).onSwing(entity, false);
		}
	}
}
