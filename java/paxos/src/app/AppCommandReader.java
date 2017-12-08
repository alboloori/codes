package app;

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

import java.util.StringTokenizer;

import org.continuent.appia.core.Direction;

import org.continuent.appia.core.events.*;
// import appia.message.ExtendedMessage;
// import appia.protocols.tutorialDA.events.SampleSendableEvent;
import org.continuent.appia.core.message.Message;
import org.continuent.appia.core.AppiaEventException;

import process.SampleSendableEvent;



/**
 * Class that reads from the keyboard and generates events to the appia Channel.
 * 
 * @author nuno
 */
public class AppCommandReader extends Thread {

	private ApplSession parentSession;

	private java.io.BufferedReader keyb;

	private String local = null;

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	public AppCommandReader(ApplSession parentSession) {
		super();
		this.parentSession = parentSession;
		keyb = new java.io.BufferedReader(new java.io.InputStreamReader(
				System.in));
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	public void run() {
		//System.out.println("-----AppCommandReader.Run()--------");
		while (true) {
			try {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
				System.out.print("> ");
				local = keyb.readLine();

				if (local.equalsIgnoreCase(""))
					continue;
				StringTokenizer st = new StringTokenizer(local);

				SampleSendableEvent asyn = new SampleSendableEvent();
			//	System.out.println("-----sample sendable created--------");
				Message message = (Message) asyn.getMessage();

				String cm = st.nextToken();
				System.out.println(cm);
				asyn.setCommand(cm);
			 
				String msg = "";
				while (st.hasMoreTokens())
					msg += (st.nextToken() + " ");
				message.pushString(msg);
				asyn.asyncGo(parentSession.channel, Direction.DOWN);
			//	System.out.println("Messege:" + msg + " sent.");
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}catch (AppiaEventException e) {
				e.printStackTrace();
			}
		}
	}
}
