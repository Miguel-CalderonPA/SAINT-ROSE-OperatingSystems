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
                waitQueue.remove(pcb); // goes into process // ASK
            }
        }
        if(! readyQueue.isEmpty()) return readyQueue.remove(); // return the readied process
        return null; // only if no process
    }


    //--------------------------------------------------------------------------------------

    @Override
    public void execute(ProcessControlBlock pcb) {
        // while time slice isn't over execute
		int usedTime=0;
		int slice = 0;
		//int origClock;
        // set the count for slice
		// do the scheduled time 
       // while(pcb.state().equals(ProcessControlBlock.READY) )  {

        //    while(slice < timeSlice && pcb.state().equals(ProcessControlBlock.READY)) {
                usedTime += pcb.execute(timeSlice, clock); // quantum and clock
                slice++;
           //     tick();
         //   }
        System.out.print("Process: " + pcb.pid() + " ran from " + clock + " to ");
        tick(usedTime);
            System.out.print(clock - 1 + "\n");
       // }
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
