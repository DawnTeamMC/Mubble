package fr.hugman.mubble.test.gametest.zelda;

import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import fr.hugman.mubble.zelda.references.LoftwingVariantIds;
import fr.hugman.mubble.zelda.world.entity.ZeldaEntityTypes;
import fr.hugman.mubble.zelda.world.entity.animal.loftwing.Loftwing;
import fr.hugman.mubble.zelda.world.item.ZeldaItems;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;

/**
 * The loftwing itself: the shape it takes in each state, the gear only it can wear, and the bond
 * that decides, once and for all, whose bird it is.
 */
public class LoftwingGameTest {
	private static final BlockPos GROUND = new BlockPos(4, Arena.FLOOR_Y + 1, 3);

	@GameTest
	public void aloftwingStartsOwnedByNobody(GameTestHelper helper) {
		var loftwing = loftwing(helper);

		helper.assertFalse(loftwing.isTame(), "a fresh loftwing already has an owner");
		helper.assertFalse(loftwing.isFlying(), "a loftwing standing on the ground should not have its wings out");
		helper.assertFalse(loftwing.hasBelt(), "a fresh loftwing is already wearing a belt");

		helper.succeed();
	}

	/** The three shapes of the issue: folded on the ground, spread in flight, wide in a panic. */
	@GameTest
	public void thehitboxFollowsTheState(GameTestHelper helper) {
		var loftwing = loftwing(helper);
		float standingWidth = loftwing.getBbWidth();

		loftwing.setFlying(true);
		helper.assertTrue(loftwing.getBbWidth() > standingWidth, "a flying loftwing should be wider than a standing one");
		helper.assertTrue(loftwing.getBbHeight() < Loftwing.STANDING_DIMENSIONS.height(), "a flying loftwing should be flatter than a standing one");
		helper.assertTrue(loftwing.getPose() == Pose.FALL_FLYING, "a flying loftwing should be in the flying pose");

		loftwing.setFlying(false);
		loftwing.setPanicking(true);
		loftwing.refreshDimensions();
		helper.assertTrue(loftwing.getBbWidth() > standingWidth, "a panicking loftwing should throw its wings open");
		helper.assertTrue(loftwing.getBbHeight() > Loftwing.FLYING_DIMENSIONS.height(), "a panicking loftwing stands as tall as ever");

		helper.succeed();
	}

	/** Only the belt and the helmet, and nothing a mob would normally pick up. */
	@GameTest
	public void onlyTheBeltAndTheHelmetFit(GameTestHelper helper) {
		var loftwing = loftwing(helper);

		helper.assertTrue(loftwing.canUseSlot(EquipmentSlot.SADDLE), "a loftwing should be able to wear a belt");
		helper.assertTrue(loftwing.canUseSlot(EquipmentSlot.HEAD), "a loftwing should be able to wear a helmet");
		helper.assertFalse(loftwing.canUseSlot(EquipmentSlot.CHEST), "a loftwing has no business wearing a chestplate");
		helper.assertFalse(loftwing.canUseSlot(EquipmentSlot.MAINHAND), "a loftwing should not be holding gear");

		helper.succeed();
	}

	/** The helmet is where the trade between protection and turning speed is written down. */
	@GameTest
	public void theheavierHelmetProtectsMoreAndTurnsSlower(GameTestHelper helper) {
		var loftwing = loftwing(helper);

		var bareheaded = loftwing.getHelmetSettings();
		loftwing.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ZeldaItems.LEATHER_LOFTWING_HELMET));
		var leather = loftwing.getHelmetSettings();
		loftwing.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ZeldaItems.NETHERITE_LOFTWING_HELMET));
		var netherite = loftwing.getHelmetSettings();

		helper.assertTrue(bareheaded.crashProtection() == 0.0F, "a bare head should soak up nothing");
		helper.assertTrue(netherite.crashProtection() > leather.crashProtection(), "netherite should protect more than leather");
		helper.assertTrue(netherite.maxTurnPerTick() < leather.maxTurnPerTick(), "netherite should turn slower than leather");
		helper.assertTrue(leather.maxTurnPerTick() < bareheaded.maxTurnPerTick(), "any helmet should turn slower than none at all");

		helper.succeed();
	}

	/** Wearing the belt is what separates a mount from a bird that simply likes you. */
	@GameTest
	public void thebeltIsWhatMakesItRideable(GameTestHelper helper) {
		var loftwing = loftwing(helper);
		var player = TestPlayers.at(helper, GROUND);
		loftwing.bondWith(player);

		helper.assertFalse(loftwing.hasBelt(), "the loftwing should start without a belt");
		loftwing.setItemSlot(EquipmentSlot.SADDLE, new ItemStack(ZeldaItems.LOFTWING_BELT));
		helper.assertTrue(loftwing.hasBelt(), "the belt should sit in the saddle slot");

		helper.succeed();
	}

	/** The point of the mechanic: a refusal is remembered, and it is final. */
	@GameTest
	public void arefusalIsRememberedAndFinal(GameTestHelper helper) {
		var loftwing = loftwing(helper);
		var player = TestPlayers.at(helper, GROUND);

		helper.assertFalse(loftwing.hasRejected(player), "a loftwing should not have turned anyone down yet");
		helper.assertTrue(loftwing.isCuriousAbout(player), "an unknown player should be worth a look");

		loftwing.reject(player);

		helper.assertTrue(loftwing.hasRejected(player), "the refusal was forgotten straight away");
		helper.assertFalse(loftwing.isCuriousAbout(player), "a loftwing should not come back to someone it turned down");

		helper.succeed();
	}

	@GameTest
	public void thebondSurvivesSaveAndLoad(GameTestHelper helper) {
		var loftwing = loftwing(helper);
		var player = TestPlayers.at(helper, GROUND);
		loftwing.reject(player);

		var registries = helper.getLevel().registryAccess();
		var output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registries);
		loftwing.saveWithoutId(output);

		var reloaded = helper.spawnWithNoFreeWill(ZeldaEntityTypes.LOFTWING, GROUND);
		reloaded.load(TagValueInput.create(ProblemReporter.DISCARDING, registries, output.buildResult()));

		helper.assertTrue(reloaded.hasRejected(player), "the list of refusals was lost through the save file");
		helper.succeed();
	}

	/** The breed comes from a data pack, so the entity has to default to one that exists. */
	@GameTest
	public void aloftwingHasABreed(GameTestHelper helper) {
		var loftwing = loftwing(helper);

		helper.assertTrue(loftwing.getVariant().is(LoftwingVariantIds.BLUE), "a loftwing should default to the blue breed");

		var crimson = helper.getLevel().registryAccess()
				.lookupOrThrow(fr.hugman.mubble.zelda.core.registries.ZeldaRegistries.LOFTWING_VARIANT)
				.getOrThrow(LoftwingVariantIds.CRIMSON);
		helper.assertTrue(crimson.value().weight() < loftwing.getVariant().value().weight(),
				"the crimson breed should be rarer than the common one");

		helper.succeed();
	}

	/** A bottle of spores is what heals a loftwing, and nothing else is. */
	@GameTest
	public void onlySporesCountAsFood(GameTestHelper helper) {
		var loftwing = loftwing(helper);

		helper.assertTrue(loftwing.isFood(new ItemStack(ZeldaItems.MUSHROOM_SPORES_BOTTLE)), "spores should feed a loftwing");
		helper.assertFalse(loftwing.isFood(new ItemStack(Items.WHEAT)), "wheat is not loftwing food");

		helper.succeed();
	}

	private static Loftwing loftwing(GameTestHelper helper) {
		Arena.buildFloor(helper);
		return helper.spawnWithNoFreeWill(ZeldaEntityTypes.LOFTWING, GROUND);
	}
}
