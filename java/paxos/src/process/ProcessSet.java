package process;

import java.io.Serializable;
import org.continuent.appia.protocols.common.InetWithPort;


//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
/**
 * A set of sample processes.
 * 
 * @author nuno
 */
public class ProcessSet implements Serializable {
	private static final long serialVersionUID = -8520712350015155147L;

	Process[] processes;

	public static int SIZE = 20;

	private int[][] matrix;

	private int delta;
	private int timeDelay;
	private int k;
	
	private int self;

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	/**
	 * Constructor of the class.
	 * 
	 * @param n
	 *            number of processes.
	 */
	public ProcessSet(int n) {
		processes = new Process[n];
		// matrix=new int[n][n];
	}

	/**
	 * 
	 */
	public ProcessSet() {
		processes = new Process[0];
		// matrix=new int[SIZE][SIZE];
	}

	/**
	 * Gets an array with all processes.
	 * 
	 * @return Array with all processes
	 */
	public Process[] getAllProcesses() {
		return processes;
	}
    public void setTimeDelay (int t)
    {
    	timeDelay=t;
    	
    }
    public void setDelta (int d)
    {
    	delta=d;
    }
    public void setK(int k)
    {
    	this.k=k;
    }
    
    public int getK()
    {
    	return k;
    }
    public int getDelta()
    {
    	return delta;
    }
  
    public int getTimeDelay()
    {
    	return timeDelay;
    }
	/**
	 * Gets the number of processes.
	 * 
	 * @return number of processes
	 */
	public int getSize() {
		return processes.length;
	}

	/**
	 * Gets the rank of the specified process.
	 * 
	 * @param addr
	 *            the address of the process
	 * @return the rank of the process
	 */
	public int getRank(InetWithPort addr) {
		for (int i = 0; i < processes.length; i++) {
			if ((processes[i] != null)
					&& processes[i].getInetWithPort().equals(addr))
				return i;
		}
		return -1;
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	/**
	 * Adds a process into the process set.
	 * 
	 * @param process
	 *            the process to add.
	 * @param pr
	 *            the rank of the process.
	 */
	public void addProcess(Process process, int pr) {
		if (pr >= processes.length) {
			Process[] temp = new Process[processes.length + 1];
			for (int i = 0; i < processes.length; i++)
				temp[i] = processes[i];
			processes = temp;
		}
		processes[pr] = process;
		if (process.isSelf())
			self = pr;
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	/**
	 * Sets the process specified by the rank "proc" to correct or crashed.
	 * 
	 * @param proc
	 *            the process rank.
	 * @param correct
	 *            true if the process is correct, false if the process crashed.
	 */
	public void setCorrect(int proc, boolean correct) {
		processes[proc].setCorrect(correct);
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	/**
	 * Gets the process with rank "i"
	 * 
	 * @param i
	 *            the process rank
	 * @return the process
	 */
	public Process getProcess(int i) {
		return processes[i];
	}

	/**
	 * Gets the process with address "addr".
	 * 
	 * @param addr
	 *            the process address
	 * @return the process.
	 */
	public Process getProcess(InetWithPort addr) {
		int i = getRank(addr);
		if (i == -1)
			return null;
		else
			return processes[i];
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	/**
	 * Gets the self rank.
	 * 
	 * @return My rank
	 */
	public int getSelfRank() {
		return self;
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	/**
	 * Gets the self process.
	 * 
	 * @return My process
	 */
	public Process getSelfProcess() {
		return processes[self];
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	/**
	 * Clones the process set.
	 * 
	 * @return a clone of the process set.
	 */
	public ProcessSet cloneProcessSet() {
		ProcessSet set = new ProcessSet(getSize());
		Process[] procs = getAllProcesses();
		for (int i = 0; i < procs.length; i++)
			set.addProcess(procs[i].cloneProcess(), i);
		set.self = self;
		set.delta=delta;
		set.k=k;
		set.timeDelay=timeDelay;
		set.setMatrix(this.getMatrix());
		return set;
	}

	public int[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(int[][] ar) {
		matrix = ar;
	}
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
}
