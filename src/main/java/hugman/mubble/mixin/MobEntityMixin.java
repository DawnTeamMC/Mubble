package hugman.mubble.mixin;

import hugman.mubble.init.MubbleCostumes;
import hugman.mubble.init.data.MubbleTags;
import hugman.mubble.objects.item.costume.BlockCostume;
import hugman.mubble.objects.item.costume.Costume;
import hugman.mubble.util.CalendarEvents;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(MobEntity.class)
public class MobEntityMixin
{
	@Inject(method = "getPreferredEquipmentSlot", at = @At(value = "TAIL"), cancellable = true)
	private static void getPreferredEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> cir)
	{
		Item item = stack.getItem();
		if (item instanceof Costume)
		{
			Costume costume = (Costume) item;
			cir.setReturnValue(costume.getArmorType());
		}
		else if (item instanceof BlockCostume)
		{
			BlockCostume costume = (BlockCostume) item;
			cir.setReturnValue(costume.getArmorType());
		}
	}

	@Inject(method = "initialize", at = @At(value = "TAIL"), cancellable = true)
	private void initialize(WorldAccess world, LocalDifficulty difficulty, SpawnReason spawnType, EntityData entityData, CompoundTag entityTag, CallbackInfoReturnable<EntityData> cir)
	{
		MobEntity entity = (MobEntity) (Object) this;
		Random rand = new Random();
		if (MubbleTags.EntityTypes.CAN_WEAR_HELMET.contains(entity.getType()))
		{
			if (entity.getEquippedStack(EquipmentSlot.HEAD).isEmpty() && CalendarEvents.isChristmasSeason)
			{
				if (rand.nextFloat() < (float) CalendarEvents.getDayToday() / 25.0f)
				{
					entity.equipStack(EquipmentSlot.HEAD, new ItemStack(MubbleCostumes.CHRISTMAS_HAT));
					entity.setEquipmentDropChance(EquipmentSlot.HEAD, 0.0F);
				}
			}
		}
		cir.setReturnValue(entityData);
	}
}
