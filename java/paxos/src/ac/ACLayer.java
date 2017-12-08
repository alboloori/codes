/**
 * 
 */
package ac;

import org.continuent.appia.core.Layer;
import org.continuent.appia.core.Session;
import org.continuent.appia.core.events.SendableEvent;
import org.continuent.appia.core.events.channel.ChannelClose;
import org.continuent.appia.core.events.channel.ChannelInit;

import process.ProcessInitEvent;
import beb.BEBSession;
import beb.BebBroadcastEvent;

import org.continuent.appia.core.events.channel.ChannelInit;
import org.continuent.appia.core.events.SendableEvent;
import org.continuent.appia.protocols.common.RegisterSocketEvent;
import org.continuent.appia.core.events.channel.ChannelClose;

import eld.ELDSession;

/**
 * @author Ali Javadzadeh
 *
 */
public class ACLayer extends Layer{
	public ACLayer() {
	    /* events that the protocol will create */
	    evProvide = new Class[3];
	  //  evProvide[0]=ACProposeEvent.class;
	    evProvide[0]=BebBroadcastEvent.class;
	    evProvide[1]=Pp2pSendEvent.class;
	    evProvide[2]=SendableEvent.class;

	    /*
	     * events that the protocol require to work. This is a subset of the
	     * accepted events
	     */
	    evRequire = new Class[3];
	    evRequire[0] = SendableEvent.class;
	    evRequire[1] = ChannelInit.class;
	    evRequire[2] = ProcessInitEvent.class;

	    /* events that the protocol will accept */
	    evAccept = new Class[6];
	    evAccept[0] = SendableEvent.class;
	    evAccept[1] = ChannelInit.class;
	    evAccept[2] = ChannelClose.class;
	    evAccept[3] = ProcessInitEvent.class;
        evAccept[4]=  ACProposeEvent.class;
        evAccept[5]=eld.TrustEvent.class;
	  }

	  /**
	   * Creates a new session to this protocol.
	   * 
	   * @see appia.Layer#createSession()
	   */
	  public Session createSession() {
	    return new ACSession(this);
	  }
}
