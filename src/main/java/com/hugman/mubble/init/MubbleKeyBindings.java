package com.hugman.mubble.init;

import com.hugman.mubble.Mubble;
import com.hugman.mubble.object.item.costume.CostumeItem;
import dev.emi.trinkets.api.TrinketSlots;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFW;

public class MubbleKeyBindings {
	public static final KeyBinding USE_COSTUME_ABILITY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.mubble.use_costume_ability", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "key.categories.gameplay"));

	public static void clientInit() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if(USE_COSTUME_ABILITY.isPressed()) {
				ClientPlayNetworking.send(Mubble.MOD_DATA.id("costume_ability"), PacketByteBufs.empty());
				for(TrinketSlots.Slot slot : TrinketSlots.getAllSlots()) {
					ItemStack stack = TrinketsApi.getTrinketComponent(client.player).getStack(slot.getSlotGroup().getName(), slot.getName());
					if(!stack.isEmpty()) {
						if(stack.getItem() instanceof CostumeItem) {
							CostumeItem item = (CostumeItem) stack.getItem();
							item.useAbility(client.player, stack);
						}
					}
				}
			}
		});
	}

	public static void serverInit() {
		ServerPlayNetworking.registerGlobalReceiver(Mubble.MOD_DATA.id("costume_ability"), (server, player, handler, buf, responseSender) -> server.execute(() -> {
			for(TrinketSlots.Slot slot : TrinketSlots.getAllSlots()) {
				ItemStack stack = TrinketsApi.getTrinketComponent(player).getStack(slot.getSlotGroup().getName(), slot.getName());
				if(!stack.isEmpty()) {
					if(stack.getItem() instanceof CostumeItem) {
						CostumeItem item = (CostumeItem) stack.getItem();
						item.useAbility(player, stack);
					}
				}
			}
		}));
	}
}
