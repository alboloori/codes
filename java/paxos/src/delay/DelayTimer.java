
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
package delay;

import org.continuent.appia.core.Session;
import org.continuent.appia.core.events.SendableEvent;
import org.continuent.appia.core.Channel;
import org.continuent.appia.core.AppiaEventException;
import org.continuent.appia.core.events.channel.Timer;
import org.continuent.appia.core.Direction;
import org.continuent.appia.core.AppiaException;
import org.continuent.appia.core.EventQualifier;

public class DelayTimer extends Timer {
	public SendableEvent event;

	public int source;

	public int dest;

	public long delay;
	 //private String tempHeader;
	   // private int[] tempArray;
	public DelayTimer(long delay, Channel channel, Session source,
			SendableEvent e, int so, int de) throws AppiaEventException,
			AppiaException {
		super(delay * 1000, String.valueOf(Math.random()), channel,
				Direction.DOWN, source, EventQualifier.ON);
		this.source = so;
		this.dest = de;
		this.delay = delay;
		event=e;
		 
		
	}

	/*public void setHeader(String h)
	 {
		 tempHeader=h;
	 }
	public void setArray(int [] ar)
	 {
		 tempArray=ar;
	 }
	public String getHeader()
	 {
		 return tempHeader;
	 }
	public int[] getArray()
	 {
		 return tempArray;
	 }*/
}
