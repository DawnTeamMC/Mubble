package hugman.mubble.objects.item;

import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;

public class InstrumentItem extends Item
{
	protected final SoundEvent sound;

	public InstrumentItem(Item.Settings builder, SoundEvent soundIn)
	{
		super(builder);
		this.sound = soundIn;
	}

	public SoundEvent getInstrumentSound()
	{
		return sound;
	}
}
