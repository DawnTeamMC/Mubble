package hugman.mubble.util;

import hugman.mubble.util.creator.BlockTemplate;

public class MoreWordUtils {
	public static String numerate(int number, String text) {
		if(number == 0) {
			return "no " + text;
		}
		else if(number == 1) {
			return number + " " + text;
		}
		else {
			if(text.endsWith("y")) {
				return number + " " + text.substring(0, text.length() - 1) + "ies";
			}
			else {
				return number + " " + text + "s";
			}
		}
	}

	public static String parseShapeName(String name, BlockTemplate shape)
	{
		if(shape != BlockTemplate.CUBE && name.endsWith("bricks")){
			return name.substring(0, name.length() - 1);
		}
		else return name;
	}
}