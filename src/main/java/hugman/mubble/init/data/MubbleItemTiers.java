package hugman.mubble.init.data;

import java.util.function.Supplier;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;

public enum MubbleItemTiers implements ToolMaterial
{
	VANADIUM(3, 1756, 10.0F, 3.5F, 12, () ->
	{
		return Ingredient.fromTag(MubbleTags.Items.GEMS_KYBER);
	}),
	KYBER(3, 1932, 10.0F, 4.5F, 16, () ->
	{
		return Ingredient.fromTag(MubbleTags.Items.GEMS_KYBER);
	}),
	BISMUTH(4, 2145, 12.0F, 4F, 22, () ->
	{
		return Ingredient.fromTag(MubbleTags.Items.GEMS_BISMUTH);
	});

	private final int miningLevel;
	private final int itemDurability;
	private final float miningSpeed;
	private final float attackDamage;
	private final int enchantability;
	private final Lazy<Ingredient> repairIngredient;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private MubbleItemTiers(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantibility, Supplier<Ingredient> repairIngredient)
	{
		this.miningLevel = miningLevel;
		this.itemDurability = itemDurability;
		this.miningSpeed = miningSpeed;
		this.attackDamage = attackDamage;
		this.enchantability = enchantibility;
		this.repairIngredient = new Lazy(repairIngredient);
	}

	public int getDurability()
	{
		return this.itemDurability;
	}

	public float getMiningSpeedMultiplier()
	{
		return this.miningSpeed;
	}

	public float getAttackDamage()
	{
		return this.attackDamage;
	}

	public int getMiningLevel()
	{
		return this.miningLevel;
	}

	public int getEnchantability()
	{
		return this.enchantability;
	}

	public Ingredient getRepairIngredient()
	{
		return (Ingredient) this.repairIngredient.get();
	}
}
