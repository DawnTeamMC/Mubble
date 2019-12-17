package hugman.mubble.util;

import java.util.function.Supplier;

import hugman.mubble.init.data.MubbleTags;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyLoadBase;

public enum MubbleItemTier implements IItemTier
{
	BISMUTH(4, 2176, 11.0F, 3.5F, 20, () ->
	{
		return Ingredient.fromTag(MubbleTags.Items.GEMS_BISMUTH);
	});
	private final int harvestLevel;
	private final int maxUses;
	private final float efficiency;
	private final float attackDamage;
	private final int enchantability;
	private final LazyLoadBase<Ingredient> repairMaterial;

	private MubbleItemTier(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn)
	{
		this.harvestLevel = harvestLevelIn;
		this.maxUses = maxUsesIn;
		this.efficiency = efficiencyIn;
		this.attackDamage = attackDamageIn;
		this.enchantability = enchantabilityIn;
		this.repairMaterial = new LazyLoadBase<>(repairMaterialIn);
	}

	public int getMaxUses()
	{
		return this.maxUses;
	}

	public float getEfficiency()
	{
		return this.efficiency;
	}

	public float getAttackDamage()
	{
		return this.attackDamage;
	}

	public int getHarvestLevel()
	{
		return this.harvestLevel;
	}

	public int getEnchantability()
	{
		return this.enchantability;
	}

	public Ingredient getRepairMaterial()
	{
		return this.repairMaterial.getValue();
	}
}
