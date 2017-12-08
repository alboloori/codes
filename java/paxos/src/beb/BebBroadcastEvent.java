package beb;

import org.continuent.appia.core.events.SendableEvent;

public class BebBroadcastEvent extends SendableEvent {
	int ARRAY_SIZE=5;
	public int [] contentArray;
	public BebBroadcastEvent()
	{
		contentArray=new int[ARRAY_SIZE];
	}
}
