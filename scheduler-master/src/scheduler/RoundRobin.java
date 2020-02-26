package scheduler;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by calderonm063 on 2/17/20.
 * if still remaining go to ready, if i/o go to waiting
 * CPU utliz is time not idle
 * Waiting - time in ready
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
		// instantiating the queues
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
        if(! readyQueue.isEmpty()) { return readyQueue.remove(); } // return the readied process
        return null; // if no process in ready queue
    }


    //--------------------------------------------------------------------------------------

    @Override
    public void execute(ProcessControlBlock pcb) {
        // while time slice isn't over execute
		int usedTime=0;
		usedTime += pcb.execute(timeSlice, clock); // quantum and clock
        System.out.print("|         Process " + pcb.pid() + " ran from " + clock + " to ");
        tick(usedTime);
        System.out.print(clock - 1 + "\n");

    }

    //--------------------------------------------------------------------------------------

    @Override
    public void add(ProcessControlBlock pcb) { // just sorts the processes
        switch (pcb.state()) {
            case ProcessControlBlock.READY: readyQueue.add(pcb);break;
            case ProcessControlBlock.WAITING: waitQueue.add(pcb);break;
            case ProcessControlBlock.TERMINATED:terminated.add(pcb);break;
            case ProcessControlBlock.NEW: pcb.update(clock); readyQueue.add(pcb);break;
            default:
                throw new RuntimeException("Process " + pcb.pid() + " in illegal state: " + pcb.state());
        }
    }
    //--------------------------------------------------------------------------------------



    //--------------------------------------------------------------------------------------

    @Override
    public Iterator<ProcessControlBlock> iterator() { // iterates through list
        LinkedList<ProcessControlBlock> everything = new LinkedList<ProcessControlBlock>();
        everything.addAll(readyQueue);
        everything.addAll(waitQueue);
        everything.addAll(terminated);
        return everything.iterator();
    }

    //--------------------------------------------------------------------------------------
}
