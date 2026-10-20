package fr.hugman.mubble.test.gametest.zelda;

import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import fr.hugman.mubble.zelda.world.entity.ZeldaEntityTypes;
import fr.hugman.mubble.zelda.world.entity.animal.loftwing.Loftwing;
import fr.hugman.mubble.zelda.world.item.ZeldaItems;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

/**
 * Right-clicking a loftwing, through the path a real click takes rather than by calling the
 * entity's setters.
 * <p>
 * The distinction matters: {@code Player.interactOn} asks the entity first and only falls through
 * to vanilla's equip-on-interact handling when the entity leaves the interaction alone. An entity
 * that answers every right click swallows the belt, and the belt is what makes it rideable — which
 * no amount of poking at {@code setItemSlot} from a test would ever show.
 */
public class LoftwingInteractionGameTest {
	private static final BlockPos GROUND = new BlockPos(4, Arena.FLOOR_Y + 1, 3);

	@GameTest
	public void thebeltGoesOnWithARightClick(GameTestHelper helper) {
		Loftwing loftwing = bondedLoftwing(helper);
		ServerPlayer owner = owner(helper, loftwing);
		owner.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ZeldaItems.LOFTWING_BELT));

		rightClick(owner, loftwing);

		helper.assertTrue(loftwing.hasBelt(), "right-clicking a loftwing while holding the belt should put it on");
		helper.assertFalse(loftwing.isOrderedToSit(), "putting the belt on should not also sit the bird down");
		helper.succeed();
	}

	@GameTest
	public void thehelmetGoesOnWithARightClick(GameTestHelper helper) {
		Loftwing loftwing = bondedLoftwing(helper);
		ServerPlayer owner = owner(helper, loftwing);
		owner.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ZeldaItems.IRON_LOFTWING_HELMET));

		rightClick(owner, loftwing);

		helper.assertFalse(loftwing.getItemBySlot(EquipmentSlot.HEAD).isEmpty(), "right-clicking with a helmet should put it on");
		helper.succeed();
	}

	@GameTest
	public void abeltedLoftwingIsMountedWithAnEmptyHand(GameTestHelper helper) {
		Loftwing loftwing = bondedLoftwing(helper);
		ServerPlayer owner = owner(helper, loftwing);
		loftwing.setItemSlot(EquipmentSlot.SADDLE, new ItemStack(ZeldaItems.LOFTWING_BELT));
		owner.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);

		rightClick(owner, loftwing);

		helper.assertTrue(owner.getVehicle() == loftwing, "an empty-handed right click on a belted loftwing should mount it");
		helper.succeed();
	}

	@GameTest
	public void crouchingSitsTheBirdDown(GameTestHelper helper) {
		Loftwing loftwing = bondedLoftwing(helper);
		ServerPlayer owner = owner(helper, loftwing);
		owner.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
		owner.setShiftKeyDown(true);

		rightClick(owner, loftwing);

		helper.assertTrue(loftwing.isOrderedToSit(), "crouching and right-clicking should sit the bird down");
		helper.succeed();
	}

	/** Sitting stops everything that steers the bird, so in the air it would simply drop. */
	@GameTest
	public void aflyingLoftwingCannotBeToldToSit(GameTestHelper helper) {
		Loftwing loftwing = bondedLoftwing(helper);
		ServerPlayer owner = owner(helper, loftwing);
		owner.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
		owner.setShiftKeyDown(true);
		loftwing.setPos(loftwing.getX(), loftwing.getY() + 4.0, loftwing.getZ());
		loftwing.setFlying(true);

		rightClick(owner, loftwing);

		helper.assertFalse(loftwing.isOrderedToSit(), "a loftwing in the air should not be sat down out from under itself");
		helper.succeed();
	}

	private static void rightClick(ServerPlayer player, Loftwing loftwing) {
		player.interactOn(loftwing, InteractionHand.MAIN_HAND, Vec3.ZERO);
	}

	private static Loftwing bondedLoftwing(GameTestHelper helper) {
		Arena.buildFloor(helper);
		return helper.spawnWithNoFreeWill(ZeldaEntityTypes.LOFTWING, GROUND);
	}

	private static ServerPlayer owner(GameTestHelper helper, Loftwing loftwing) {
		ServerPlayer player = TestPlayers.at(helper, GROUND);
		loftwing.bondWith(player);
		return player;
	}
}
