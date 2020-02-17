package scheduler;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by calderonm063 on 2/17/20.
 */
public class RoundRobin extends Scheduler {

    private LinkedList<ProcessControlBlock> readyQueue;
    private LinkedList<ProcessControlBlock> waitQueue;
    private LinkedList<ProcessControlBlock> terminated;
    private int timeSlice;
    //--------------------------------------------------------------------------------------

    public RoundRobin(int contextSwitchTime, int slice) {
        super(contextSwitchTime);
        readyQueue = new LinkedList<ProcessControlBlock>();
        waitQueue = new LinkedList<ProcessControlBlock>();
        terminated = new LinkedList<ProcessControlBlock>();
        timeSlice = slice;
    }

    //--------------------------------------------------------------------------------------

    @Override
    public boolean isEmpty() {
        return readyQueue.isEmpty() && waitQueue.isEmpty();
    }

    @Override
    public ProcessControlBlock next() {
        for(ProcessControlBlock pcb : waitQueue) {
            if(pcb.state().equals(ProcessControlBlock.READY)) {
                readyQueue.add(pcb);
                waitQueue.remove(pcb);
            }
        }
        if(! readyQueue.isEmpty()) return readyQueue.remove();
        return null;
    }


    //--------------------------------------------------------------------------------------

    @Override
    public void execute(ProcessControlBlock pcb) {
        // while time slice isn't over execute
        int slice = 0;
        while(pcb.state().equals(ProcessControlBlock.READY) && slice < timeSlice )  {
            pcb.execute(1, clock);
            slice++;
            tick();
        }
    }

    //--------------------------------------------------------------------------------------

    @Override
    public void add(ProcessControlBlock pcb) {
        if(pcb.state().equals(ProcessControlBlock.READY)) readyQueue.add(pcb);
        else if(pcb.state().equals(ProcessControlBlock.WAITING)) waitQueue.add(pcb);
        else if(pcb.state().equals(ProcessControlBlock.TERMINATED)) terminated.add(pcb);
        else throw new RuntimeException("Process " + pcb.pid() + " in illegal state: " + pcb.state());
    }

    //--------------------------------------------------------------------------------------

    @Override
    public Iterator<ProcessControlBlock> iterator() {
        LinkedList<ProcessControlBlock> everything = new LinkedList<ProcessControlBlock>();
        everything.addAll(readyQueue);
        everything.addAll(waitQueue);
        return everything.iterator();
    }

    //--------------------------------------------------------------------------------------
}
