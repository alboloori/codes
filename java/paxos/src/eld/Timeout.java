package eld;

import org.continuent.appia.core.AppiaEventException;
import org.continuent.appia.core.AppiaException;
import org.continuent.appia.core.Channel;
import org.continuent.appia.core.Direction;
import org.continuent.appia.core.EventQualifier;
import org.continuent.appia.core.Session;
import org.continuent.appia.core.events.SendableEvent;
import org.continuent.appia.core.events.channel.Timer;

public class Timeout extends Timer {

	public SendableEvent event;

	public long delay;

	public Timeout(long delay, Channel channel, Session source,
			SendableEvent e) throws AppiaEventException, AppiaException {

		super(delay * 1000, String.valueOf(Math.random()), channel,
				Direction.DOWN, source, EventQualifier.ON);

		this.delay = delay;
		event = e;
	}
}
