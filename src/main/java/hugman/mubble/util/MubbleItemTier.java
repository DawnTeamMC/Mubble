package hugman.mubble.util;

import java.util.function.Supplier;

import hugman.mubble.init.data.MubbleTags;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;

public enum MubbleItemTier implements ToolMaterial
{
	BISMUTH(4, 2176, 11.0F, 3F, 20, () ->
	{
		return Ingredient.fromTag(MubbleTags.Items.GEMS_BISMUTH);
	});
	private final int harvestLevel;
	private final int maxUses;
	private final float efficiency;
	private final float attackDamage;
	private final int enchantability;
	private final Lazy<Ingredient> repairMaterial;

	private MubbleItemTier(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn)
	{
		this.harvestLevel = harvestLevelIn;
		this.maxUses = maxUsesIn;
		this.efficiency = efficiencyIn;
		this.attackDamage = attackDamageIn;
		this.enchantability = enchantabilityIn;
		this.repairMaterial = new Lazy<>(repairMaterialIn);
	}

	public int getDurability()
	{
		return this.maxUses;
	}

	public float getMiningSpeed()
	{
		return this.efficiency;
	}

	public float getAttackDamage()
	{
		return this.attackDamage;
	}

	public int getMiningLevel()
	{
		return this.harvestLevel;
	}

	public int getEnchantability()
	{
		return this.enchantability;
	}

	public Ingredient getRepairIngredient()
	{
		return this.repairMaterial.get();
	}
}
