package process;

import java.io.Serializable;

import org.continuent.appia.protocols.common.InetWithPort;

//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
/**
 * Represents a process in the system.
 * 
 * @author nuno
 */
public class Process implements Serializable {
	private static final long serialVersionUID = 3677871909022936117L;

	private InetWithPort inetWithPort;

	private int processNumber;

	private boolean self, correct, initialized;

	// private int[][] matrix;
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	public Process(InetWithPort inetWithPort, int proc, boolean self) {
		this.inetWithPort = inetWithPort;
		this.processNumber = proc;
		// this.matrix=m;
		this.self = self;
		correct = true;
		initialized = false;
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	/**
	 * Gets the address, InetWithPort, of the procees.
	 * 
	 * @return the address of the process
	 */
	public InetWithPort getInetWithPort() {
		return inetWithPort;
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	/**
	 * Gets the process number.
	 * 
	 * @return the process number
	 */
	public int getProcessNumber() {
		return processNumber;
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	/*
	 * public int[][]getMatrix() { return matrix; }
	 * //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	 * public void setMatrix(int[][]ar) { matrix=ar; }
	 */
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	/**
	 * Is it my own process.
	 * 
	 * @return true if the process is my self.
	 */
	public boolean isSelf() {
		return self;
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object test) {
		if (!(test instanceof Process))
			return false;
		Process proc = (Process) test;
		return inetWithPort.equals(proc.inetWithPort)
				&& (processNumber == proc.processNumber) && (self == proc.self);
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	/**
	 * Is the process correct. Gets the correctness value.
	 * 
	 * @return true if correct, false otherwise
	 */
	public boolean isCorrect() {
		return correct;
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	/**
	 * Sets the correctness of the process.
	 * 
	 * @param b
	 *            the correct value
	 */
	public void setCorrect(boolean b) {
		correct = b;
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	/**
	 * Is the process initialized. Gets the initialized value.
	 * 
	 * @return true if initialized, false otherwise
	 */
	public boolean isInitialized() {
		return initialized;
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	/**
	 * Sets if the process has initiated.
	 * 
	 * @param initialized
	 *            the initialized value
	 */
	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	/**
	 * Creates a clone of the process.
	 * 
	 * @return the process clone
	 */
	public Process cloneProcess() {
		Process p = new Process(inetWithPort, processNumber, self);
		p.correct = correct;
		p.initialized = initialized;
		return p;
	}
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
}
