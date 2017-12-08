package paxos;

import org.continuent.appia.core.events.SendableEvent;

public class DecidedEvent extends SendableEvent {
	public int id;
	public int result;
	public DecidedEvent(int id, int r)
	{
		this.id=id;
		this.result=r;
	}

}
