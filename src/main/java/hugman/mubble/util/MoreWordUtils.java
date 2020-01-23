package hugman.mubble.util;

public class MoreWordUtils
{
	public static String numerate(int number, String text)
	{
		if(number == 0)
		{
			return "no " + text;
		}
		else if(number == 1)
		{
			return number + " " + text;
		}
		else
		{
			if(text.endsWith("y"))
			{
				return number + " " + text.substring(0, text.length() - 1) + "ies";
			}
			else
			{
				return number + " " + text + "s";
			}
		}
	}
}