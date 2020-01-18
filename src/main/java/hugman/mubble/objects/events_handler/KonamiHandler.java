/*package hugman.mubble.objects.events_handler;

import java.util.Map;
import java.util.TreeMap;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.LiteralText;

public class KonamiHandler
{
	private static int[] konamiCode =
	{
		GLFW.GLFW_KEY_UP,
		GLFW.GLFW_KEY_UP,
		GLFW.GLFW_KEY_DOWN,
		GLFW.GLFW_KEY_DOWN,
		GLFW.GLFW_KEY_LEFT,
		GLFW.GLFW_KEY_RIGHT,
		GLFW.GLFW_KEY_LEFT,
		GLFW.GLFW_KEY_RIGHT
	};
	private static Map<Integer, Integer>[] graph = generateSequenceMap(konamiCode);
	private static int currentNode = 0;
	
	@SuppressWarnings("unchecked")
	private static Map<Integer, Integer>[] generateSequenceMap(int[] sequence)
	{
		Map<Integer, Integer>[] graph = new Map[sequence.length];
		for(int i = 0; i < sequence.length; i++)
		{
			graph[i] = new TreeMap<Integer, Integer>();
		}

		for(int i = 0; i < sequence.length; i++)
		{
			loop: for (int j = i; j < sequence.length - 1; j++)
			{
				if(sequence[j - i] == sequence[j])
				{
					Integer value = graph[j].get(sequence[j - i + 1]);
					if (value == null || value < j - i + 1)
					{
						graph[j].put(sequence[j - i + 1], j - i + 1);
					}
				}
				else
				{
					break loop;
				}
			}
		}
		return graph;
	}
	
	private static boolean checkKonami(int keyPressed)
	{
		Integer nextNode = graph[currentNode].get(keyPressed);
		currentNode = (nextNode == null ? 0 : nextNode);
		return currentNode == konamiCode.length - 1;
	}
	
	public void onKeyPressed(GuiScreenEvent.KeyboardKeyPressedEvent.Post event)
	{
		if(event.getGui() instanceof TitleScreen)
		{
			if(checkKonami(event.getKeyCode()))
			{
				// TODO Implement here whatever you want the konami code to do
				MinecraftClient.getInstance().openScreen(new Screen(new LiteralText("bruh")));
			}
		}
	}
}*/
