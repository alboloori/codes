/*
 *
 * Hands-On code of the book Introduction to Reliable Distributed Programming
 * by Rachid Guerraoui and Luís Rodrigues
 * Copyright (C) 2005 Luís Rodrigues
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 * 
 * Contact
 * 	Address:
 *		Departamento de Informática, FCUL, 
 *		Bloco C6, Sala 6.3.12, Campo Grande, 
 *		1749-016 Lisboa, 
 *		PORTUGAL
 * 	Email:
 * 		ler@di.fc.ul.pt
 * 	Web:
 *		http://www.di.fc.ul.pt/~ler/
 * 
 */

package beb;
import org.continuent.appia.core.*;
import org.continuent.appia.core.events.channel.ChannelInit;
import org.continuent.appia.core.events.SendableEvent;
import org.continuent.appia.protocols.common.RegisterSocketEvent;
import org.continuent.appia.core.events.channel.ChannelClose;

import process.ProcessInitEvent;


/**
 * Layer of the Basic Broadcast protocol.
 * 
 * @author nuno
 */
public class BEBLayer extends Layer {

  public BEBLayer() {
    /* events that the protocol will create */
    evProvide = new Class[1];
    evProvide[0] = SendableEvent.class;
    /*
     * events that the protocol require to work. This is a subset of the
     * accepted events
     */
    evRequire = new Class[3];
    evRequire[0] = SendableEvent.class;
    evRequire[1] = ChannelInit.class;
    evRequire[2] = ProcessInitEvent.class;

    /* events that the protocol will accept */
    evAccept = new Class[7];
    evAccept[0] = SendableEvent.class;
    evAccept[1] = ChannelInit.class;
    evAccept[2] = ChannelClose.class;
    evAccept[3] = ProcessInitEvent.class;
    evAccept[4] = beb.BebBroadcastEvent.class;
	evAccept[5] =ac.Pp2pSendEvent.class;
	evAccept[6]=paxos.DecidedEvent.class;
  }

  /**
   * Creates a new session to this protocol.
   * 
   * @see appia.Layer#createSession()
   */
  public Session createSession() {
    return new BEBSession(this);
  }

}
