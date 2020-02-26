package scheduler;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by calderonm063 on 2/17/20.
 * Last Modified: 2/26/20
 */

public class RoundRobin extends Scheduler {

	// Declaring the Queues and PDMs
    private LinkedList<ProcessControlBlock> readyQueue;
    private LinkedList<ProcessControlBlock> waitQueue;
    private LinkedList<ProcessControlBlock> terminated;
    private int timeSlice; // the quantum

    //--------------------------------------------------------------------------------------

	// Constructor
    public RoundRobin(int contextSwitchTime, int slice) {
        super(contextSwitchTime); // goes to parent and assigns 
		// instantiating the queues
        readyQueue = new LinkedList<>();
        waitQueue = new LinkedList<>();
        terminated = new LinkedList<>();
        timeSlice = slice; // assigning time slice
    } // end non-default constructor

    //--------------------------------------------------------------------------------------

    @Override
    public boolean isEmpty() {
        return readyQueue.isEmpty() && waitQueue.isEmpty(); // simply if done
    } // end isEmpty

    //--------------------------------------------------------------------------------------

    @Override
    public ProcessControlBlock next() {
        for(ProcessControlBlock pcb : waitQueue) { // for each process in waitQueue
            if(pcb.state().equals(ProcessControlBlock.READY)) { // if its ready now
                readyQueue.add(pcb); // adds to ready
                waitQueue.remove(pcb); // removes from wait
            } // end if process ready
        } // end for each in waitQ
        if(! readyQueue.isEmpty()) { return readyQueue.remove(); } // return the readied process
        return null; // if no process in ready queue
    } // end next


    //--------------------------------------------------------------------------------------

    @Override
    public void execute(ProcessControlBlock pcb) {
		int usedTime=0; // this time slice usage
		usedTime += pcb.execute(timeSlice, clock); // when it calculates it also returns time used
        System.out.print("|         Process " + pcb.pid() + " ran from " + clock + " to ");
        tick(usedTime); // tick the cpu clock
        System.out.print(clock - 1 + "\n"); // -1 to show where the process ended not where scheduler is at currently
        if (readyQueue.size() > 0) // if there is another process in readyQ then its waiting for CPU time
            setWaitTime(usedTime); // used to calculate average wait time later
    } // end execute

    //--------------------------------------------------------------------------------------

    @Override
    public void add(ProcessControlBlock pcb) { // just sorts the processes
        switch (pcb.state()) {
            case ProcessControlBlock.READY: readyQueue.add(pcb);break;
            case ProcessControlBlock.WAITING: waitQueue.add(pcb);break;
            case ProcessControlBlock.TERMINATED:terminated.add(pcb);break;
            case ProcessControlBlock.NEW: pcb.update(clock); readyQueue.add(pcb);break; // mainly cosmetic to simulate new -> ready
            default:
                throw new RuntimeException("Process " + pcb.pid() + " in illegal state: " + pcb.state());
        } // end switch
    } // end add

    //--------------------------------------------------------------------------------------

    @Override
    public Iterator<ProcessControlBlock> iterator() { // iterates through list
        LinkedList<ProcessControlBlock> everything = new LinkedList<ProcessControlBlock>();
        everything.addAll(readyQueue);
        everything.addAll(waitQueue);
        everything.addAll(terminated);
        return everything.iterator();
    } // end iterator

    //--------------------------------------------------------------------------------------
} // end round-robin