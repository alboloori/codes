package delay;

/*
 *
 * APPIA: Protocol composition and execution framework
 * Copyright (C) 2005 Laboratorio de Sistemas Informaticos de Grande Escala (LASIGE)
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
 * 		LASIGE, Departamento de Informatica, Bloco C6
 * 		Faculdade de Ciencias, Universidade de Lisboa
 * 		Campo Grande, 1749-016 Lisboa
 * 		Portugal
 * 	Email:
 * 		appia@di.fc.ul.pt
 * 	Web:
 * 		http://appia.di.fc.ul.pt
 * 
 */

import org.continuent.appia.core.*;
import org.continuent.appia.core.events.channel.ChannelInit;

import process.ProcessInitEvent;


public class DelayLayer extends Layer {

	public DelayLayer() {
		// System.out.println("--------DelayLayer()------------------");
		Class timer = delay.DelayTimer.class;

		Class cinit = ChannelInit.class;

		// Class smpe=echo.SampleSendableEvent.class;
		Class initprocess = process.ProcessInitEvent.class;

		Class sendableEvent = org.continuent.appia.core.events.SendableEvent.class;

		evProvide = new Class[2];
		evProvide[0] = timer;
		evProvide[1] = sendableEvent;
		// evProvide[1]=initiate;
		// evProvide[3]=delayEvent;
		evRequire = new Class[1];

		evRequire[0] = timer;

		evAccept = new Class[5];

		// System.out.println("--------DelayLayer...SendableSet------------------");
		evAccept[0] = cinit;
		evAccept[1] = timer;

		evAccept[2] = initprocess;
		evAccept[3] = sendableEvent;
		evAccept[4] =ac.Pp2pSendEvent.class;

		// evAccept[7]=delayEvent;
		// System.out.println("--------Exit DelayLayer()------------------");
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	public Session createSession() {
		//System.out.println("--------DelayLayer.createSession()------------------");
		return new DelaySession(this);
	}
}
