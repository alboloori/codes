/*
 *
 * Hands-On code of the book Introduction to Reliable Distributed Programming
 * by Rachid Guerraoui and Lu�s Rodrigues
 * Copyright (C) 2005 Lu�s Rodrigues
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
 *		Departamento de Inform�tica, FCUL, 
 *		Bloco C6, Sala 6.3.12, Campo Grande, 
 *		1749-016 Lisboa, 
 *		PORTUGAL
 * 	Email:
 * 		ler@di.fc.ul.pt
 * 	Web:
 *		http://www.di.fc.ul.pt/~ler/
 * 
 */

package process;

import org.continuent.appia.core.*;
import org.continuent.appia.core.Session;
import org.continuent.appia.core.events.SendableEvent;
import org.continuent.appia.core.message.Message;

/**
 * Sendable Event used by the application.
 * 
 * @author nuno
 */
public class SampleSendableEvent extends
		org.continuent.appia.core.events.SendableEvent {

	private String command;

	/**
	 * Default constructor.
	 */
	public SampleSendableEvent() {
		// super(new ExtendedMessage());
	}

	/**
	 * Constructor if the event.
	 * 
	 * @param channel
	 *            The Appia channel.
	 * @param dir
	 *            The direction of the event.
	 * @param source
	 *            The session that created the event.
	 * @throws AppiaEventException
	 */
	public SampleSendableEvent(Channel channel, int dir, Session source)
			throws AppiaEventException {
		super(channel, dir, source);
	}

	/**
	 * Constructor of the event.
	 * 
	 * @param msg
	 *            The message to use.
	 */
	public SampleSendableEvent(Message msg) {
		super(msg);
	}

	/**
	 * Constructor of the event.
	 * 
	 * @param channel
	 *            The Appia channel.
	 * @param dir
	 *            The direction of the event.
	 * @param source
	 *            The session that created the event.
	 * @param msg
	 *            The message to use.
	 * @throws AppiaEventException
	 */
	public SampleSendableEvent(Channel channel, int dir, Session source,
			Message msg) throws AppiaEventException {
		super(channel, dir, source, msg);
	}

	/**
	 * @return Returns the command.
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param command
	 *            The command to set.
	 */
	public void setCommand(String command) {
		this.command = command;
	}

}
