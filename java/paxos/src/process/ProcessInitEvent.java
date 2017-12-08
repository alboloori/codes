package process;

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

import org.continuent.appia.core.AppiaEventException;
import org.continuent.appia.core.Channel;
import org.continuent.appia.core.Event;
import org.continuent.appia.core.Session;

import process.ProcessSet;


// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
/**
 * Distributes the process set to the interested Sessions.
 * 
 * @author nuno
 */
public class ProcessInitEvent extends Event {

	private ProcessSet processSet;

	// / private int [][] matrix;
	/**
	 * Default constructor.
	 */
	public ProcessInitEvent() {
		super();
	}

	/**
	 * Constructor of the event.
	 * 
	 * @param ps
	 *            The process set.
	 */
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	public ProcessInitEvent(ProcessSet ps) {
		super();
		processSet = ps;
	}

	/**
	 * Constructor of the event.
	 * 
	 * @param channel
	 *            The Appia Channel.
	 * @param dir
	 *            The direction of the event.
	 * @param source
	 *            the session that created the event.
	 * @throws AppiaEventException
	 */
	public ProcessInitEvent(Channel channel, int dir, Session source)
			throws AppiaEventException {
		super(channel, dir, source);
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	/**
	 * Gets a copy of the process set.
	 * 
	 * @return a copy of the process set.
	 */
	public ProcessSet getProcessSet() {
		return processSet.cloneProcessSet();
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	/**
	 * Sets the process set.
	 * 
	 * @param set
	 *            the process set.
	 */
	public void setProcessSet(ProcessSet set) {
		processSet = set;
	}
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
}
