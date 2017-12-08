package eld;

import org.continuent.appia.core.Layer;
import org.continuent.appia.core.Session;
import org.continuent.appia.core.events.channel.ChannelInit;

import process.ProcessInitEvent;


public class ELDLayer extends Layer {
	public ELDLayer() {
		Class cinit = ChannelInit.class;

		Class initprocess = process.ProcessInitEvent.class;

		Class sendableEvent = org.continuent.appia.core.events.SendableEvent.class;
		Class checkTimer = eld.Timeout.class;
		Class beb=beb.BebBroadcastEvent.class;
		Class pp2pSend=ac.Pp2pSendEvent.class;
		Class trust=eld.TrustEvent.class;
		evProvide = new Class[3];
		evProvide[0] = sendableEvent;
		evProvide[1] = checkTimer;
		evProvide[2]=trust;
		evRequire = new Class[2];

		evRequire[0] = initprocess;
		evRequire[1] = checkTimer;

		evAccept = new Class[7];
		evAccept[0] = cinit;
		evAccept[1] = initprocess;
		evAccept[2] = sendableEvent;
		evAccept[3] = checkTimer;
		evAccept[4] = beb;
		evAccept[5] =pp2pSend;
		evAccept[6]=paxos.DecidedEvent.class;
	}

	public Session createSession() {
		return new ELDSession(this);
	}
}
