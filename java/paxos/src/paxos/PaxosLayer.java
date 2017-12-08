/**
 * 
 */
package paxos;

import org.continuent.appia.core.Layer;
import org.continuent.appia.core.Session;
import org.continuent.appia.core.events.SendableEvent;
import org.continuent.appia.core.events.channel.ChannelClose;
import org.continuent.appia.core.events.channel.ChannelInit;
import org.continuent.appia.core.events.channel.Timer;
import ac.ACProposeEvent;

import eld.TrustEvent;


import process.ProcessInitEvent;


/**
 * @author Ali Javadzadeh
 *
 */
public class PaxosLayer extends Layer{
	public PaxosLayer() {
	    /* events that the protocol will create */
	    evProvide = new Class[3];
	    evProvide[0]=ACProposeEvent.class;
	    evProvide[1]=DecidedEvent.class;
	    evProvide[2]=Timer.class;
	    /*
	     * events that the protocol require to work. This is a subset of the
	     * accepted events
	     */
	    evRequire = new Class[4];
	    evRequire[0] = SendableEvent.class;
	    evRequire[1] = ChannelInit.class;
	    evRequire[2] = ProcessInitEvent.class;
	    evRequire[3]=Timer.class;
	    /* event s that the protocol will accept */
	    evAccept = new Class[8];
	    evAccept[0] = SendableEvent.class;
	    evAccept[1] = ChannelInit.class;
	    evAccept[2] = ChannelClose.class;
	    evAccept[3] = ProcessInitEvent.class;
	    evAccept[4] = TrustEvent.class;
	    evAccept[5] = ac.ACReturnEvent.class;
	    evAccept[6]=DecidedEvent.class;
	    evAccept[7]=Timer.class;
	  }

	  /**
	   * Creates a new session to this protocol.
	   * 
	   * @see appia.Layer#createSession()
	   */
	  public Session createSession() {
	    return new PaxosSession(this);
	  }

}
