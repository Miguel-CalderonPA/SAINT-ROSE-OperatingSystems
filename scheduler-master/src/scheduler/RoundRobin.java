package scheduler;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by calderonm063 on 2/17/20.
 */
public class RoundRobin extends Scheduler {

	// Declaring the Queues
    private LinkedList<ProcessControlBlock> readyQueue;
    private LinkedList<ProcessControlBlock> waitQueue;
    private LinkedList<ProcessControlBlock> terminated;
    private int timeSlice; // the time slice
    //--------------------------------------------------------------------------------------

	// Constructor
    public RoundRobin(int contextSwitchTime, int slice) {
        super(contextSwitchTime); // goes to parent and assigns 
		// instaniting the queues
        readyQueue = new LinkedList<ProcessControlBlock>();
        waitQueue = new LinkedList<ProcessControlBlock>();
        terminated = new LinkedList<ProcessControlBlock>();
        timeSlice = slice; // assigning time slice
    }

    //--------------------------------------------------------------------------------------

    @Override
    public boolean isEmpty() {
        return readyQueue.isEmpty() && waitQueue.isEmpty(); // simple if done basically
    }

    @Override
    public ProcessControlBlock next() { // not sure
        for(ProcessControlBlock pcb : waitQueue) { // for each process in waitQueue
            if(pcb.state().equals(ProcessControlBlock.READY)) {
                readyQueue.add(pcb); // adds to ready
                waitQueue.remove(pcb); // goes into process
            }
        }
        if(! readyQueue.isEmpty()) return readyQueue.remove(); // return the readied process
        return null; // only if no process
    }


    //--------------------------------------------------------------------------------------

    @Override
    public void execute(ProcessControlBlock pcb) {
        // while time slice isn't over execute
		
        int slice = 0;// set the count for slice
		// do the scheduled time 
        while(pcb.state().equals(ProcessControlBlock.READY) && slice < timeSlice )  {
            pcb.execute(1, clock); // quantum and clock
            slice++;
            tick();
        }
    }

    //--------------------------------------------------------------------------------------

    @Override
    public void add(ProcessControlBlock pcb) { // just sorts the processes
        if(pcb.state().equals(ProcessControlBlock.READY)) readyQueue.add(pcb);
        else if(pcb.state().equals(ProcessControlBlock.WAITING)) waitQueue.add(pcb);
        else if(pcb.state().equals(ProcessControlBlock.TERMINATED)) terminated.add(pcb);
        else throw new RuntimeException("Process " + pcb.pid() + " in illegal state: " + pcb.state());
    }

    //--------------------------------------------------------------------------------------

    @Override
    public Iterator<ProcessControlBlock> iterator() { // iterates through list
        LinkedList<ProcessControlBlock> everything = new LinkedList<ProcessControlBlock>();
        everything.addAll(readyQueue);
        everything.addAll(waitQueue);
        return everything.iterator();
    }

    //--------------------------------------------------------------------------------------
}
