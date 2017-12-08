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

package tcp;

import org.continuent.appia.core.*;
import org.continuent.appia.core.events.SendableEvent;
import org.continuent.appia.protocols.common.RegisterSocketEvent;
import org.continuent.appia.core.events.channel.*;


/**
 * Layer of the 
 * TCP-Based Perfect Point-to-Point Links protocol.
 * @author pedrofrv
 */
public class TcpBasedPerfectP2PLayer extends Layer
    implements
    org.continuent.appia.core.events.AppiaMulticastSupport {

  public TcpBasedPerfectP2PLayer() {
    evProvide = new Class[]{TcpUndeliveredEvent.class, SendableEvent.class,
        TcpTimer.class,};

    evAccept = new Class[]{RegisterSocketEvent.class, SendableEvent.class,
        ChannelInit.class, ChannelClose.class, TcpTimer.class,};

    evRequire = new Class[]{RegisterSocketEvent.class, SendableEvent.class,
        ChannelInit.class,};
  }

  /**
   * @see appia.Layer#createSession()
   */
  public Session createSession() {
    return new TcpBasedPerfectP2PSession(this);
  }

}
